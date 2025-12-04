/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.rendering.internal.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.xwiki.component.annotation.Component;
import org.xwiki.logging.LogLevel;
import org.xwiki.logging.LogUtils;
import org.xwiki.logging.event.LogEvent;
import org.xwiki.logging.marker.TranslationMarker;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.block.FormatBlock;
import org.xwiki.rendering.block.GroupBlock;
import org.xwiki.rendering.block.VerbatimBlock;
import org.xwiki.rendering.block.WordBlock;
import org.xwiki.rendering.block.match.ClassBlockMatcher;
import org.xwiki.rendering.block.match.OrBlockMatcher;
import org.xwiki.rendering.listener.Format;
import org.xwiki.rendering.util.ErrorBlockGenerator;

/**
 * Default implementation to generate error blocks to render an error in a wiki page.
 *
 * @version $Id$
 * @since 8.1M1
 */
@Component
@Singleton
public class DefaultErrorBlockGenerator implements ErrorBlockGenerator
{
    @Inject
    protected Logger logger;

    /**
     * Create a list of renderable error blocks containing a primary error message and, optionally,
     * a description (and throwable details) suitable for inline or block rendering.
     *
     * The provided messageId, if non-null, is used as a translation key for the message; the
     * description uses the same key suffixed with ".description". If defaultMessage does not end
     * with a period, a period is appended.
     *
     * @param inline             whether the resulting blocks should be inline (true) or block-level (false)
     * @param messageId          optional translation key for the main message; may be null
     * @param defaultMessage     fallback text for the main message; a trailing period will be added if missing
     * @param defaultDescription optional fallback text for the description; may be null
     * @param arguments          optional formatting arguments applied to the message and description
     * @return a list of Blocks representing the error message and any associated description/stack trace.
    @Override
    public List<Block> generateErrorBlocks(boolean inline, String messageId, String defaultMessage,
        String defaultDescription, Object... arguments)
    {
        LogEvent message = LogUtils.newLogEvent(messageId != null ? new TranslationMarker(messageId) : null,
            LogLevel.ERROR,
            defaultMessage != null && !defaultMessage.endsWith(".") ? defaultMessage + '.' : defaultMessage, arguments);
        LogEvent description = defaultDescription != null
            ? LogUtils.newLogEvent(messageId != null ? new TranslationMarker(messageId + ".description") : null,
                LogLevel.ERROR, defaultDescription, arguments)
            : null;

        return generateErrorBlocks(inline, message, description);
    }

    /**
     * Create block(s) representing a user-visible error message and optional description/stack trace.
     *
     * <p>If a description or a throwable is present, the main message is augmented with
     * "Click on this message for details." and, when a throwable exists, a "Cause: [..]." segment
     * is appended to the message; the full stack trace is added as a separate description block.</p>
     *
     * @param inline      true to produce inline FormatBlock(s), false to produce block-level GroupBlock(s)
     * @param message     the primary log event whose formatted message is shown; if it carries a Throwable the cause
     *                    and stack trace are included in the produced description block(s)
     * @param description an optional log event used as an additional description (its formatted message is used)
     * @return a list of Block objects containing the composed error message and, when present, description/stack trace blocks
     */
    protected List<Block> generateErrorBlocks(boolean inline, LogEvent message, LogEvent description)
    {
        List<Block> errorBlocks = new ArrayList<>();

        Map<String, String> errorBlockParams =
            Collections.singletonMap(CLASS_ATTRIBUTE_NAME, CLASS_ATTRIBUTE_MESSAGE_VALUE);
        Map<String, String> errorDescriptionBlockParams =
            Collections.singletonMap(CLASS_ATTRIBUTE_NAME, CLASS_ATTRIBUTE_DESCRIPTION_VALUE);

        StringBuilder messageBuilder = new StringBuilder();

        if (StringUtils.isNotEmpty(message.getMessage())) {
            messageBuilder.append(message.getFormattedMessage());
        }

        List<Block> descriptionChildren = new ArrayList<>();

        // Description
        addDescriptionBlock(inline, description, descriptionChildren);

        // Stack trace
        addStackTraceBlock(inline, message, messageBuilder, descriptionChildren);

        if (!descriptionChildren.isEmpty()) {
            messageBuilder.append(" Click on this message for details.");
        }

        if (inline) {
            errorBlocks.add(new FormatBlock(Arrays.asList(new WordBlock(messageBuilder.toString())), Format.NONE,
                errorBlockParams));
            if (!descriptionChildren.isEmpty()) {
                errorBlocks.add(new FormatBlock(descriptionChildren, Format.NONE, errorDescriptionBlockParams));
            }
        } else {
            errorBlocks.add(new GroupBlock(Arrays.asList(new WordBlock(messageBuilder.toString())), errorBlockParams));
            if (!descriptionChildren.isEmpty()) {
                errorBlocks.add(new GroupBlock(descriptionChildren, errorDescriptionBlockParams));
            }
        }

        return errorBlocks;
    }

    /**
     * Adds a verbatim block containing the description text to {@code descriptionChildren} if a description is provided.
     *
     * @param inline              whether the created VerbatimBlock should be inline
     * @param description         the log event whose formatted message will be used as the block content; ignored if null
     * @param descriptionChildren the list to which the new VerbatimBlock will be appended
     */
    private void addDescriptionBlock(boolean inline, LogEvent description, List<Block> descriptionChildren)
    {
        if (description != null) {
            descriptionChildren.add(new VerbatimBlock(description.getFormattedMessage(), inline));
        }
    }

    /**
     * If the provided LogEvent contains a Throwable, appends its full stack trace as a VerbatimBlock to
     * {@code descriptionChildren} and adds a short "Cause: [message]." segment to {@code messageBuilder}.
     *
     * The method resolves the deepest root cause; if none is found it uses the original throwable's
     * message for the appended cause. The {@code inline} flag controls whether the created VerbatimBlock
     * is inline.
     *
     * @param inline whether created VerbatimBlock(s) should be inline
     * @param message the LogEvent that may carry a Throwable (checked for presence)
     * @param messageBuilder the StringBuilder to append the cause text to (modified when a Throwable is present)
     * @param descriptionChildren list to which the VerbatimBlock containing the stack trace will be added (modified when a Throwable is present)
     */
    private void addStackTraceBlock(boolean inline, LogEvent message, StringBuilder messageBuilder,
        List<Block> descriptionChildren)
    {
        if (message.getThrowable() != null) {
            // Note: We're using ExceptionUtils.getRootCause(e).getMessage() instead of getRootCauseMessage()
            // below because getRootCauseMessage() adds a technical prefix (the name of the exception), that
            // we don't want to display to our users.
            Throwable rootCause = ExceptionUtils.getRootCause(message.getThrowable());
            if (rootCause == null) {
                // If there's no nested exception, fall back to the throwable itself for getting the cause
                rootCause = message.getThrowable();
            }

            descriptionChildren.add(new VerbatimBlock(ExceptionUtils.getStackTrace(message.getThrowable()), inline));

            // Also add more details to the message
            messageBuilder.append(" Cause: [");
            messageBuilder.append(rootCause.getMessage());
            messageBuilder.append("].");
        }
    }

    /**
     * Create error display blocks from a plain message and an optional description.
     *
     * @param message     the main error message text
     * @param description an optional detailed description (may be {@code null})
     * @param isInline    {@code true} to produce inline-format blocks, {@code false} to produce top-level group blocks
     * @return            a list of blocks representing the error content to render
     */
    @Override
    public List<Block> generateErrorBlocks(String message, String description, boolean isInline)
    {
        return generateErrorBlocks(isInline, null, message, description, ArrayUtils.EMPTY_OBJECT_ARRAY);
    }

    /**
     * Generate blocks that represent an error using a message prefix and an optional throwable.
     *
     * @param messagePrefix the primary error message text shown to the user
     * @param throwable an optional throwable whose cause and stack trace will be included in the description
     * @param isInline whether the resulting blocks should be rendered inline (true) or as block-level groups (false)
     * @return a list of blocks representing the error message and any accompanying description/stack trace
     */
    @Override
    public List<Block> generateErrorBlocks(String messagePrefix, Throwable throwable, boolean isInline)
    {
        return generateErrorBlocks(isInline, null, messagePrefix, null, throwable);
    }

    /**
     * Checks whether any descendant GroupBlock or FormatBlock of the given root block is marked as an error.
     *
     * @param parent the root block whose descendants will be searched for error blocks
     * @return `true` if an error block is found, `false` otherwise
     */
    @Override
    public boolean containsError(Block parent)
    {
        boolean foundError = false;
        List<Block> groupAndFormatBlocks = parent.getBlocks(
            new OrBlockMatcher(new ClassBlockMatcher(GroupBlock.class), new ClassBlockMatcher(FormatBlock.class)),
            Block.Axes.DESCENDANT);
        for (Block block : groupAndFormatBlocks) {
            String classParameter = block.getParameters().get(ErrorBlockGenerator.CLASS_ATTRIBUTE_NAME);
            if (classParameter != null && classParameter.contains(ErrorBlockGenerator.CLASS_ATTRIBUTE_MESSAGE_VALUE)) {
                foundError = true;
                break;
            }
        }
        return foundError;
    }
}
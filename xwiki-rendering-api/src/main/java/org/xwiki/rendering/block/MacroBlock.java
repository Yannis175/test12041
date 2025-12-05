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
package org.xwiki.rendering.block;

import java.util.Collections;
import java.util.Map;

import org.xwiki.rendering.listener.Listener;

/**
 * Represents a Macro (standalone or inline) defined in a page.
 * <p>
 * Note: You can get macro parameters using {@link #getParameters()} for example. Macro block is reusing Block standard
 * custom parameters API since macro by definition already have parameters and don't need also block parameters. So in
 * this case MacroBlock parameters and Block parameters are the same thing.
 *
 * @version $Id$
 * @since 1.8M2
 */
public class MacroBlock extends AbstractMacroBlock
{
    /**
     * Create a MacroBlock with the given id and parameters and no content.
     *
     * @param id the macro id
     * @param parameters the macro parameters (may be null or empty)
     * @param isInline true if the macro appears inside inline content (e.g., a paragraph)
     */
    public MacroBlock(String id, Map<String, String> parameters, boolean isInline)
    {
        this(id, parameters, null, isInline);
    }

    /**
     * Create a MacroBlock with the given macro id, parameters, optional content, and inline indicator.
     *
     * @param id the macro identifier
     * @param parameters the macro parameters (may be empty)
     * @param content the macro content, or {@code null} if the macro has no content
     * @param isInline {@code true} if the macro appears in inline content (e.g., within a paragraph), {@code false} otherwise
     */
    public MacroBlock(String id, Map<String, String> parameters, String content, boolean isInline)
    {
        super(Collections.<Block>emptyList(), parameters, id, content, isInline);
    }

    /**
     * Emit a macro event to the given listener and do not traverse or expand the macro here.
     *
     * <p>This method notifies the listener with this macro's id, parameters, content, and inline flag
     * but does not perform macro execution or replace this block; macro evaluation is performed by
     * the macro transformer in the rendering pipeline. The event is emitted so downstream listeners
     * (or unit tests) can react to the macro.
     *
     * @param listener the listener to receive the macro event
     */
    @Override
    public void traverse(Listener listener)
    {
        // Don't do anything here since we want the Macro Transformer component to take in charge
        // Macro execution. This is because Macro execution is a complex process that involves:
        // * computing the order in which the macros should be evaluated. For example the TOC macro
        // should evaluate last since other macros can contribute headers/sections blocks.
        // * some macros need to modify blocks in the XDOM object
        // * macro execution is a multi-pass process
        // In essence the Macro Transformer will replace all MacroBlock blocks with other Blocks
        // generated from the execution of the Macros when XDOM.traverse() is called there
        // won't be any MacroBlock.traverse() method called at all.

        // Note: We're calling the event to let other listener downstream decide what to do with it.
        // In practice as described above this method will never get called when the whole rendering
        // process is executed. This does get called during our unit tests though.
        listener.onMacro(getId(), getParameters(), getContent(), isInline());
    }
}
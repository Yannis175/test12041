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
package org.xwiki.rendering.internal.block;

import java.lang.reflect.Type;

import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.properties.converter.AbstractConverter;
import org.xwiki.properties.converter.ConversionException;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.block.match.BlockMatcher;
import org.xwiki.rendering.block.match.ClassBlockMatcher;

/**
 * Construct a BlockMatcher from a String to a Syntax object and the other way around.
 *
 * @version $Id$
 * @since 6.1RC1
 */
@Component
@Singleton
public class BlockMatcherConverter extends AbstractConverter<BlockMatcher>
{

    /**
     * Converts a string representation into a {@link BlockMatcher}.
     *
     * <p>Accepts values of the form {@code "class:Name"} where {@code Name} is either a fully-qualified class name or a
     * simple class name. When a simple name is provided, the {@code org.xwiki.rendering.block} package is assumed.
     *
     * @param targetType the desired target type (unused but kept for converter contract)
     * @param value the string representation of the matcher, or {@code null}
     * @return the corresponding {@link BlockMatcher}
     * @throws ConversionException if {@code value} is not {@code null} and does not resolve to a known matcher
     */
    @Override
    protected BlockMatcher convertToType(Type targetType, Object value)
    {
        if (value == null) {
            return null;
        }
        String matcherName = value.toString().trim();

        BlockMatcher matcher = null;
        if (matcherName.startsWith("class:")) {
            String blockClassName = matcherName.substring(6);
            if (blockClassName.indexOf('.') == -1) {
                blockClassName = "org.xwiki.rendering.block." + blockClassName;
            }
            try {
                Class<?> blockClass = Class.forName(blockClassName);
                if (Block.class.isAssignableFrom(blockClass)) {
                    matcher = new ClassBlockMatcher((Class<Block>) blockClass);
                }
            } catch (ClassNotFoundException c) {
                // keep matcher as null and throw new exception later on
            }
        }

        // still having null here means the matcher is not found, return an error
        if (matcher == null) {
            throw new ConversionException(String.format("Unknown BlockMatcher [%s]", matcherName));
        }
        return matcher;
    }

    /**
     * Convert a BlockMatcher into its string representation.
     *
     * @param value the matcher to convert
     * @return the string representation of the matcher
     * @throws ConversionException if the conversion is not supported or not implemented
     */
    @Override
    protected String convertToString(BlockMatcher value)
    {
        throw new ConversionException("not implemented yet");
    }
}
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
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.xwiki.rendering.listener.Format;
import org.xwiki.rendering.listener.Listener;

/**
 * Represents a text formatting block (bold, italic, etc).
 *
 * @version $Id$
 * @since 1.6M1
 */
public class FormatBlock extends AbstractBlock
{
    /**
     * The formatting to apply to the children blocks.
     */
    private Format format;

    /**
     * Creates an empty format block with no children.
     *
     * The block's format is initialized to {@link Format#NONE}, allowing callers to add children and set formatting later.
     */
    public FormatBlock()
    {
        this.format = Format.NONE;
    }

    /**
     * Create a format block that applies the given format to the provided child blocks.
     *
     * @param childrenBlocks the nested child blocks to which the format will be applied
     * @param format the formatting to apply to the child blocks
     */
    public FormatBlock(List<Block> childrenBlocks, Format format)
    {
        this(childrenBlocks, format, Collections.emptyMap());
    }

    /**
     * Constructs a FormatBlock that applies the given format to the specified child blocks and attaches optional parameters.
     *
     * @param childrenBlocks the nested child blocks to which the format will be applied
     * @param format the formatting to apply to the child blocks
     * @param parameters additional parameters for this block (may be empty)
     */
    public FormatBlock(List<Block> childrenBlocks, Format format, Map<String, String> parameters)
    {
        super(childrenBlocks, parameters);
        this.format = format;
    }

    /**
     * The formatting applied to this block's children.
     *
     * @return the {@link Format} to apply to child blocks
     */
    public Format getFormat()
    {
        return this.format;
    }

    /**
     * Signal the start of this format block to the listener.
     *
     * @param listener the listener to notify; receives a `beginFormat` event with this block's format and parameters
     */
    @Override
    public void before(Listener listener)
    {
        listener.beginFormat(getFormat(), getParameters());
    }

    /**
     * Notify the provided listener that this format block has ended, supplying the block's format and parameters.
     *
     * @param listener the rendering listener to notify
     */
    @Override
    public void after(Listener listener)
    {
        listener.endFormat(getFormat(), getParameters());
    }

    /**
     * Determine whether the given object is equal to this FormatBlock.
     *
     * Compares identity first, then verifies that the other object is a {@code FormatBlock}, that
     * the superclass equality holds, and that both blocks have the same {@code Format}.
     *
     * @param obj the object to compare with this block
     * @return {@code true} if {@code obj} is the same instance or is a {@code FormatBlock} with equal
     *         superclass state and an identical {@code Format}; {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) {
            return true;
        }

        if (obj instanceof FormatBlock && super.equals(obj)) {
            return getFormat() == ((FormatBlock) obj).getFormat();
        }

        return false;
    }

    /**
     * Compute a hash code that combines the superclass hash and this block's format.
     *
     * @return the computed hash code
     */
    @Override
    public int hashCode()
    {
        HashCodeBuilder builder = new HashCodeBuilder();

        builder.appendSuper(super.hashCode());
        builder.append(getFormat());

        return builder.toHashCode();
    }
}
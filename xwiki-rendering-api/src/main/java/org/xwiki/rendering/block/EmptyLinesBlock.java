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

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.xwiki.rendering.listener.Listener;

/**
 * Represents an empty line between 2 standalone Blocks. A standalone block is block that is not included in another
 * block. Standalone blocks are Paragraph, Standalone Macro, Lists, Table, etc.
 *
 * @version $Id$
 * @since 1.6M2
 */
public class EmptyLinesBlock extends AbstractBlock
{
    /**
     * Number of empty lines between 2 standalone Blocks.
     */
    private int count;

    /**
     * Create an EmptyLinesBlock representing the gap of empty lines between two standalone blocks.
     *
     * @param count the number of empty lines between two standalone blocks
     */
    public EmptyLinesBlock(int count)
    {
        super();
        setEmptyLinesCount(count);
    }

    /**
     * Number of empty lines between two standalone blocks.
     *
     * @return the number of empty lines between two standalone blocks
     */
    public int getEmptyLinesCount()
    {
        return this.count;
    }

    /**
     * Sets the number of empty lines between two standalone blocks.
     *
     * @param count the number of empty lines between two standalone blocks
     */
    public void setEmptyLinesCount(int count)
    {
        this.count = count;
    }

    /**
     * Notify the given listener of this block's number of empty lines during traversal.
     *
     * @param listener the traversal listener to be notified via {@code onEmptyLines(int)} with the current empty lines count
     */
    @Override
    public void traverse(Listener listener)
    {
        listener.onEmptyLines(getEmptyLinesCount());
    }

    /**
     * Determine whether another object is equal to this EmptyLinesBlock.
     *
     * @param obj the object to compare with this block
     * @return `true` if {@code obj} is the same reference or is an {@code EmptyLinesBlock} with the same empty
     *         lines count and for which the superclass equality holds, `false` otherwise
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) {
            return true;
        }

        if (obj instanceof EmptyLinesBlock) {
            return getEmptyLinesCount() == ((EmptyLinesBlock) obj).getEmptyLinesCount() && super.equals(obj);
        }

        return false;
    }

    /**
     * Compute a hash code based on the superclass hash and this block's empty lines count.
     *
     * @return the computed hash code
     */
    @Override
    public int hashCode()
    {
        HashCodeBuilder builder = new HashCodeBuilder();

        builder.append(super.hashCode());
        builder.append(getEmptyLinesCount());

        return builder.toHashCode();
    }
}
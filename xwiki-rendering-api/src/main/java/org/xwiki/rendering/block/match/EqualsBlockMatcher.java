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
package org.xwiki.rendering.block.match;

import org.xwiki.rendering.block.Block;

/**
 * Implementation of {@link BlockMatcher} which match {@link Block} equals to the registered {@link Block}.
 *
 * @version $Id$
 * @since 3.0M3
 */
public class EqualsBlockMatcher implements BlockMatcher
{
    /**
     * The block to match.
     */
    private Block block;

    /**
     * Create a matcher that considers a Block equal when it equals the provided reference block.
     *
     * @param block the reference block to compare against when matching
     */
    public EqualsBlockMatcher(Block block)
    {
        this.block = block;
    }

    /**
     * Determines whether the given block is equal to the registered block.
     *
     * @param block the block to compare with the stored block
     * @return true if the stored block equals the provided block, false otherwise
     */
    @Override
    public boolean match(Block block)
    {
        return this.block.equals(block);
    }
}
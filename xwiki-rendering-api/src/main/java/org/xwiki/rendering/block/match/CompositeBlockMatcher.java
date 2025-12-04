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

import java.util.ArrayList;
import java.util.List;

import org.xwiki.rendering.block.Block;

/**
 * Implementation of {@link BlockMatcher} which matches blocks using passed matchers in series.
 *
 * @version $Id$
 * @since 3.0M3
 */
public class CompositeBlockMatcher implements BlockMatcher
{
    /**
     * The matchers to match in series.
     */
    private List<BlockMatcher> matchers = new ArrayList<BlockMatcher>();

    /**
     * Create a composite matcher that evaluates blocks against the provided matchers in sequence.
     *
     * The provided list's elements are added to the composite in iteration order; each matcher will be applied
     * in that order when matching a block.
     *
     * @param matchers the matchers to include in the composite, in the order they should be applied
     */
    public CompositeBlockMatcher(List<BlockMatcher> matchers)
    {
        this.matchers.addAll(matchers);
    }

    /**
     * Constructs a CompositeBlockMatcher that applies the provided matchers in sequence.
     *
     * @param matchers the matchers to aggregate, applied in the provided order
     */
    public CompositeBlockMatcher(BlockMatcher... matchers)
    {
        for (BlockMatcher matcher : matchers) {
            this.matchers.add(matcher);
        }
    }

    /**
     * Checks whether all composed matchers accept the given block.
     *
     * @param block the block to test against the composed matchers
     * @return true if every matcher returns true for the block, false otherwise
     */
    @Override
    public boolean match(Block block)
    {
        for (BlockMatcher matcher : this.matchers) {
            if (!matcher.match(block)) {
                return false;
            }
        }
        return true;
    }
}
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
 * Used by {@link Block} search method to select {@link Block}.
 *
 * @version $Id$
 * @since 3.0M3
 */
public interface BlockMatcher
{
    /**
 * Determines whether the given block satisfies the match criteria.
 *
 * @param block the Block to evaluate
 * @return `true` if the block matches, `false` otherwise
 */
    boolean match(Block block);
}
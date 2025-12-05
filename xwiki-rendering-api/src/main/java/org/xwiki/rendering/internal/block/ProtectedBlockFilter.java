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

import java.util.ArrayList;
import java.util.List;

import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.block.BlockFilter;
import org.xwiki.rendering.block.MacroMarkerBlock;

/**
 * Used to manipulate Blocks but by filtering out protected blocks.
 * <p>
 * Note: This API is a work in progress and currently a protected block is a code macro marker block. In the future we
 * need to make this more generic and since we also need to review Transformations to make them more performant this
 * class may go away which is why it's currently located in an internal package.
 * </p>
 *
 * @version $Id$
 * @since 2.6
 */
public class ProtectedBlockFilter implements BlockFilter
{
    /**
     * Filter out protected blocks.
     *
     * @param block the block to evaluate
     * @return a list containing the input block if it is not protected, otherwise an empty list
     */
    @Override
    public List<Block> filter(Block block)
    {
        List<Block> blocks = new ArrayList<Block>();
        if (!isProtectedBlock(block)) {
            blocks.add(block);
        }
        return blocks;
    }

    /**
     * Filters out protected blocks from the given list.
     *
     * @param blocks the input blocks to filter; protected blocks are excluded from the result
     * @return the input blocks with protected blocks removed
     */
    public List<Block> filter(List<Block> blocks)
    {
        List<Block> filteredBlocks = new ArrayList<Block>();
        for (Block block : blocks) {
            filteredBlocks.addAll(filter(block));
        }
        return filteredBlocks;
    }

    /**
     * Finds the next sibling block that is not protected.
     *
     * @param block the block whose subsequent siblings will be scanned
     * @return the first following sibling that is not protected, or {@code null} if none exists
     */
    public Block getNextSibling(Block block)
    {
        Block sibling = block.getNextSibling();
        while (sibling != null && isProtectedBlock(sibling)) {
            sibling = sibling.getNextSibling();
        }
        return sibling;
    }

    /**
     * Collects child blocks of the specified type from the given block, optionally traversing descendants.
     *
     * @param block the parent block whose children (protected children are skipped) will be searched
     * @param blockClass the class of blocks to collect
     * @param recurse if true, also search descendants recursively
     * @param <T> the type of blocks to return
     * @return a list of child blocks that are instances of {@code blockClass}, in traversal order
     */
    public <T extends Block> List<T> getChildrenByType(Block block, Class<T> blockClass, boolean recurse)
    {
        List<T> typedBlocks = new ArrayList<T>();
        for (Block child : filter(block.getChildren())) {
            if (blockClass.isAssignableFrom(child.getClass())) {
                typedBlocks.add(blockClass.cast(child));
            }
            if (recurse && !child.getChildren().isEmpty()) {
                typedBlocks.addAll(getChildrenByType(child, blockClass, true));
            }
        }

        return typedBlocks;
    }

    /**
     * Checks whether the given block represents a protected code macro marker.
     *
     * @param block the block to test
     * @return `true` if the block is a `MacroMarkerBlock` whose id equals `"code"`, `false` otherwise
     */
    private boolean isProtectedBlock(Block block)
    {
        return (block instanceof MacroMarkerBlock)
            && "code".equals(((MacroMarkerBlock) block).getId());
    }
}
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
import java.util.Collections;
import java.util.List;

import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.block.Block.Axes;

/**
 * Tool to navigate in a tree of blocks and extract them based on configurable criteria.
 *
 * @version $Id$
 * @since 5.0M1
 */
public class BlockNavigator
{
    /**
     * Used to filter the result of the various methods.
     */
    private BlockMatcher matcher;

    /**
     * Creates a BlockNavigator that accepts all blocks by default.
     *
     * The navigator's matcher is initialized to AnyBlockMatcher.ANYBLOCKMATCHER.
     */
    public BlockNavigator()
    {
        this.matcher = AnyBlockMatcher.ANYBLOCKMATCHER;
    }

    /**
     * Create a BlockNavigator that filters navigated blocks using the provided matcher.
     *
     * @param matcher the BlockMatcher used to decide which blocks are included in results
     */
    public BlockNavigator(BlockMatcher matcher)
    {
        this.matcher = matcher;
    }

    // Blocks

    /**
     * Collects all blocks reachable from the given start block following the specified axes that match the configured BlockMatcher.
     *
     * @param <T> the type of blocks to return
     * @param currentBlock the block from which to start the search
     * @param currentAxes the navigation axes to follow during traversal
     * @return a list of matching blocks; empty list if none found
     */
    public <T extends Block> List<T> getBlocks(Block currentBlock, Axes currentAxes)
    {
        List<T> blocks = new ArrayList<T>();

        Block block = currentBlock;
        Axes axes = currentAxes;

        while (block != null) {
            Block nextBlock = null;

            switch (axes) {
            // SELF
                case SELF:
                    addBlock(block, blocks);
                    break;
                // ANCESTOR
                case ANCESTOR_OR_SELF:
                    addBlock(block, blocks);
                    nextBlock = block.getParent();
                    break;
                case ANCESTOR:
                    nextBlock = block.getParent();
                    axes = Axes.ANCESTOR_OR_SELF;
                    break;
                case PARENT:
                    nextBlock = block.getParent();
                    axes = Axes.SELF;
                    break;
                // DESCENDANT
                case CHILD:
                    if (!block.getChildren().isEmpty()) {
                        nextBlock = block.getChildren().get(0);
                        axes = Axes.FOLLOWING_SIBLING;
                        addBlock(nextBlock, blocks);
                    }
                    break;
                case DESCENDANT_OR_SELF:
                    addBlock(block, blocks);
                    blocks = getBlocks(block.getChildren(), Axes.DESCENDANT_OR_SELF, blocks);
                    break;
                case DESCENDANT:
                    blocks = getBlocks(block.getChildren(), Axes.DESCENDANT_OR_SELF, blocks);
                    break;
                // FOLLOWING
                case FOLLOWING_SIBLING:
                    nextBlock = block.getNextSibling();
                    addBlock(nextBlock, blocks);
                    break;
                case FOLLOWING:
                    for (Block nextSibling = block.getNextSibling(); nextSibling != null;
                        nextSibling = nextSibling.getNextSibling()) {
                        blocks = getBlocks(nextSibling, Axes.DESCENDANT_OR_SELF, blocks);
                    }
                    break;
                // PRECEDING
                case PRECEDING_SIBLING:
                    nextBlock = block.getPreviousSibling();
                    addBlock(nextBlock, blocks);
                    break;
                case PRECEDING:
                    for (Block previousSibling = block.getPreviousSibling(); previousSibling != null;
                        previousSibling = previousSibling.getPreviousSibling()) {
                        blocks = getBlocks(previousSibling, Axes.DESCENDANT_OR_SELF, blocks);
                    }
                    break;
                default:
                    break;
            }

            block = nextBlock;
        }

        return blocks != null ? blocks : Collections.<T>emptyList();
    }

    /**
     * Adds the current block to the provided list if it is non-null and matches the navigator's matcher.
     *
     * @param <T> the class of the blocks in the list
     * @param currentBlock the block to consider
     * @param blocks the list to append to; must be non-null
     */
    private <T extends Block> void addBlock(Block currentBlock, List<T> blocks)
    {
        if (currentBlock != null && this.matcher.match(currentBlock)) {
            blocks.add((T) currentBlock);
        }
    }

    /**
     * Collect matching blocks starting from each block in the provided list following the given axes and append them
     * to the provided accumulator.
     *
     * @param <T> the class of the blocks to return
     * @param blocks the blocks from which to start the search
     * @param axes the navigation axes to follow for each start block
     * @param blocksOut an accumulator list to fill; may be null
     * @return the accumulator list populated with matching blocks, or `null` if `blocksOut` was null and no matches were found
     */
    private <T extends Block> List<T> getBlocks(List<Block> blocks, Axes axes, List<T> blocksOut)
    {
        List<T> newBlocks = blocksOut;

        for (Block block : blocks) {
            newBlocks = getBlocks(block, axes, newBlocks);
        }

        return newBlocks;
    }

    /**
     * Accumulate blocks matching the navigator's matcher starting from a block using the given axes into an existing
     * list or a newly created list.
     *
     * @param <T> the class of the blocks to return
     * @param currentBlock the block to search from
     * @param axes the axes to use for navigation
     * @param blocksOut an optional accumulator list to append results to; may be null
     * @return the accumulator list containing found blocks, or a new list if none was provided and matches were found;
     *         returns null if no matches were found and no accumulator was provided
     */
    private <T extends Block> List<T> getBlocks(Block currentBlock, Axes axes, List<T> blocksOut)
    {
        List<T> newBlocks = blocksOut;

        List<T> nextBlocks = getBlocks(currentBlock, axes);
        if (!nextBlocks.isEmpty()) {
            if (newBlocks == null) {
                newBlocks = nextBlocks;
            } else {
                newBlocks.addAll(nextBlocks);
            }
        }

        return newBlocks;
    }

    // First block

    /**
     * Finds the first block that matches the configured matcher starting from the given block and following the specified axes.
     *
     * @param <T> the concrete Block type to return
     * @param currentBlock the block to start the search from
     * @param currentAxes the navigation axes that define how to traverse from the starting block
     * @return the first matching block, or {@code null} if none is found
     */
    public <T extends Block> T getFirstBlock(Block currentBlock, Axes currentAxes)
    {
        Block block = currentBlock;
        Axes axes = currentAxes;

        while (block != null) {
            Block nextBlock = null;
            switch (axes) {
            // SELF
                case SELF:
                    if (this.matcher.match(block)) {
                        return (T) block;
                    }
                    break;
                // ANCESTOR
                case ANCESTOR_OR_SELF:
                    if (this.matcher.match(block)) {
                        return (T) block;
                    }
                case ANCESTOR:
                case PARENT:
                    axes = axes == Axes.PARENT ? Axes.SELF : Axes.ANCESTOR_OR_SELF;
                    nextBlock = block.getParent();
                    break;
                // DESCENDANT
                case CHILD:
                    List<Block> children = block.getChildren();
                    if (!children.isEmpty()) {
                        nextBlock = children.get(0);
                        axes = Axes.FOLLOWING_SIBLING;
                        if (this.matcher.match(nextBlock)) {
                            return (T) nextBlock;
                        }
                    }
                    break;
                case DESCENDANT_OR_SELF:
                    if (this.matcher.match(block)) {
                        return (T) block;
                    }
                case DESCENDANT:
                    for (Block child : block.getChildren()) {
                        Block matchedBlock = getFirstBlock(child, Axes.DESCENDANT_OR_SELF);
                        if (matchedBlock != null) {
                            return (T) matchedBlock;
                        }
                    }
                    break;
                // FOLLOWING
                case FOLLOWING_SIBLING:
                    nextBlock = block.getNextSibling();
                    if (nextBlock != null && this.matcher.match(nextBlock)) {
                        return (T) nextBlock;
                    }
                    break;
                case FOLLOWING:
                    for (Block nextSibling = block.getNextSibling(); nextSibling != null;
                        nextSibling = nextSibling.getNextSibling()) {
                        Block matchedBlock = getFirstBlock(nextSibling, Axes.DESCENDANT_OR_SELF);
                        if (matchedBlock != null) {
                            return (T) matchedBlock;
                        }
                    }
                    break;
                // PRECEDING
                case PRECEDING_SIBLING:
                    nextBlock = block.getPreviousSibling();
                    if (nextBlock != null && this.matcher.match(nextBlock)) {
                        return (T) nextBlock;
                    }
                    break;
                case PRECEDING:
                    for (Block previousSibling = block.getPreviousSibling(); previousSibling != null;
                        previousSibling = previousSibling.getPreviousSibling()) {
                        Block matchedBlock = getFirstBlock(previousSibling, Axes.DESCENDANT_OR_SELF);
                        if (matchedBlock != null) {
                            return (T) matchedBlock;
                        }
                    }
                    break;
                default:
                    break;
            }

            block = nextBlock;
        }

        return (T) block;
    }
}
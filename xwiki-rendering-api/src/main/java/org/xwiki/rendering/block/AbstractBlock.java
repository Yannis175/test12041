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

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.xwiki.rendering.block.match.BlockMatcher;
import org.xwiki.rendering.block.match.BlockNavigator;
import org.xwiki.rendering.block.match.CounterBlockMatcher;
import org.xwiki.rendering.listener.Listener;

/**
 * Implementation for Block operations. All blocks should extend this class. Supports the notion of generic parameters
 * which can be added to a block (see {@link #getParameter(String)} for more details.
 *
 * @version $Id$
 * @since 1.5M2
 */
public abstract class AbstractBlock implements Block
{
    /**
     * Store parameters, see {@link #getParameter(String)} for more explanations on what parameters are.
     */
    private Map<String, String> parameters;

    /**
     * The Blocks this Block contains.
     */
    private List<Block> childrenBlocks;

    /**
     * The Block containing this Block.
     */
    private Block parentBlock;

    /**
     * The next Sibling Block or null if no next sibling exists.
     */
    private Block nextSiblingBlock;

    /**
     * The previous Sibling Block or null if no previous sibling exists.
     */
    private Block previousSiblingBlock;

    /**
     * Create a new empty AbstractBlock with no parameters, parent, siblings, or children.
     */
    public AbstractBlock()
    {
        // Nothing to do
    }

    /**
     * Create a block initialized with the provided parameters.
     *
     * @param parameters the parameter map whose entries are copied into this block's parameters
     */
    public AbstractBlock(Map<String, String> parameters)
    {
        setParameters(parameters);
    }

    /**
     * Constructs a block with a child block.
     *
     * @param childBlock the child block of this block
     * @since 3.0M1
     */
    public AbstractBlock(Block childBlock)
    {
        this(childBlock, Collections.emptyMap());
    }

    /**
     * Creates a block initialized with the provided child blocks.
     *
     * @param childrenBlocks the child blocks to add to this block
     * @since 3.0M1
     */
    public AbstractBlock(List<? extends Block> childrenBlocks)
    {
        this(childrenBlocks, Collections.emptyMap());
    }

    /**
     * Constructs an AbstractBlock initialized with the given child and parameters.
     *
     * @param childBlock the child block to add
     * @param parameters the parameters to set on the block
     * @since 3.0M1
     */
    public AbstractBlock(Block childBlock, Map<String, String> parameters)
    {
        this(parameters);

        addChild(childBlock);
    }

    /**
     * Create a block initialized with the given children and parameters.
     *
     * @param childrenBlocks the children to add to the block; may be null or empty
     * @param parameters the parameters to set on the block; may be null
     * @since 3.0M1
     */
    public AbstractBlock(List<? extends Block> childrenBlocks, Map<String, String> parameters)
    {
        this(parameters);

        addChildren(childrenBlocks);
    }

    /**
     * Finds the index of a specific block in a list using identity comparison.
     *
     * @param block the block whose position to find
     * @param blocks the list of blocks to search
     * @return the index of the block, or -1 if the block is not present
     */
    private static int indexOfBlock(Block block, List<Block> blocks)
    {
        int position = 0;

        for (Block child : blocks) {
            if (child == block) {
                return position;
            }
            ++position;
        }

        return -1;
    }

    /**
     * Appends the given block as the last child of this block.
     *
     * @param blockToAdd the block to append as a child
     */
    @Override
    public void addChild(Block blockToAdd)
    {
        insertChildAfter(blockToAdd, null);
    }

    /**
     * Appends the given blocks as children of this block, preserving their order.
     *
     * @param blocksToAdd the blocks to add as children; if the list is empty the method does nothing
     */
    @Override
    public void addChildren(List<? extends Block> blocksToAdd)
    {
        if (!blocksToAdd.isEmpty()) {
            if (this.childrenBlocks == null) {
                // Create the list with just the exact required size
                this.childrenBlocks = new ArrayList<>(blocksToAdd.size());
            }

            for (Block blockToAdd : blocksToAdd) {
                addChild(blockToAdd);
            }
        }
    }

    /**
     * Replace this block's children with the given list of children.
     *
     * If the provided list is empty, all existing children are removed. Otherwise existing children are cleared
     * and the provided children are added as this block's children.
     *
     * @param children the new children for this block; may be empty to remove all children
     */
    @Override
    public void setChildren(List<? extends Block> children)
    {
        if (children.isEmpty()) {
            if (this.childrenBlocks != null) {
                this.childrenBlocks.clear();
            }
        } else {
            if (this.childrenBlocks != null) {
                this.childrenBlocks.clear();
            }

            addChildren(children);
        }
    }

    /**
     * Set the next sibling block for this block.
     *
     * @param nextSiblingBlock the block that will be the next sibling, or {@code null} to unset it
     */
    @Override
    public void setNextSiblingBlock(Block nextSiblingBlock)
    {
        this.nextSiblingBlock = nextSiblingBlock;
    }

    /**
     * Set the previous sibling block reference for this block.
     *
     * @param previousSiblingBlock the block to set as this block's previous sibling, or {@code null} to clear it
     */
    @Override
    public void setPreviousSiblingBlock(Block previousSiblingBlock)
    {
        this.previousSiblingBlock = previousSiblingBlock;
    }

    /**
     * Inserts a child block into this block's children immediately before the specified next block.
     *
     * <p>The inserted block becomes a child of this block; sibling links and the internal children list are updated
     * accordingly. If {@code nextBlock} is {@code null}, the block is appended as the last child.</p>
     *
     * @param blockToInsert the block to insert; will have its parent and sibling references updated
     * @param nextBlock the existing child before which {@code blockToInsert} will be inserted, or {@code null} to append
     */
    @Override
    public void insertChildBefore(Block blockToInsert, Block nextBlock)
    {
        blockToInsert.setParent(this);

        if (nextBlock == null) {
            // Last block becomes last but one
            if (this.childrenBlocks != null && !this.childrenBlocks.isEmpty()) {
                Block lastBlock = this.childrenBlocks.get(this.childrenBlocks.size() - 1);
                blockToInsert.setPreviousSiblingBlock(lastBlock);
                lastBlock.setNextSiblingBlock(blockToInsert);
            } else {
                blockToInsert.setPreviousSiblingBlock(null);

                if (this.childrenBlocks == null) {
                    this.childrenBlocks = new ArrayList<>(1);
                }
            }
            blockToInsert.setNextSiblingBlock(null);
            this.childrenBlocks.add(blockToInsert);
        } else {
            // If there's a previous block to nextBlock then get it to set its next sibling
            Block previousBlock = nextBlock.getPreviousSibling();
            if (previousBlock != null) {
                previousBlock.setNextSiblingBlock(blockToInsert);
                blockToInsert.setPreviousSiblingBlock(previousBlock);
            } else {
                blockToInsert.setPreviousSiblingBlock(null);
            }
            blockToInsert.setNextSiblingBlock(nextBlock);
            nextBlock.setPreviousSiblingBlock(blockToInsert);
            if (this.childrenBlocks == null || this.childrenBlocks.isEmpty()) {
                this.childrenBlocks = new ArrayList<>(1);
                this.childrenBlocks.add(blockToInsert);
            } else {
                this.childrenBlocks.add(indexOfChild(nextBlock), blockToInsert);
            }
        }
    }

    /**
     * Inserts the given block immediately after the specified previous sibling in this block's children.
     *
     * If {@code previousBlock} is {@code null} the block is appended to the children.
     *
     * The method updates this block's children list and adjusts next/previous sibling references of the
     * inserted block and its neighboring siblings to maintain a consistent sibling chain.
     *
     * @param blockToInsert the block to insert
     * @param previousBlock the existing child after which {@code blockToInsert} must be placed, or {@code null} to append
     */
    @Override
    public void insertChildAfter(Block blockToInsert, Block previousBlock)
    {
        if (previousBlock == null) {
            insertChildBefore(blockToInsert, null);
        } else {
            // If there's a next block to previousBlock then get it to set its previous sibling
            Block nextBlock = previousBlock.getNextSibling();
            if (nextBlock != null) {
                nextBlock.setPreviousSiblingBlock(blockToInsert);
                blockToInsert.setNextSiblingBlock(nextBlock);
            } else {
                blockToInsert.setNextSiblingBlock(null);
            }
            blockToInsert.setPreviousSiblingBlock(previousBlock);
            previousBlock.setNextSiblingBlock(blockToInsert);
            if (this.childrenBlocks == null) {
                this.childrenBlocks = new ArrayList<>(1);
            }
            this.childrenBlocks.add(indexOfChild(previousBlock) + 1, blockToInsert);
        }
    }

    /**
     * Replace an existing child with a new child block.
     *
     * Replaces {@code oldBlock} among this block's children with {@code newBlock}. The method delegates to the
     * multi-replacement variant and preserves parent and sibling relationships as handled by that variant.
     *
     * @param newBlock the block to insert in place of {@code oldBlock}
     * @param oldBlock the existing child block to be replaced
     * @throws InvalidParameterException if {@code oldBlock} is not a direct child of this block
     */
    @Override
    public void replaceChild(Block newBlock, Block oldBlock)
    {
        replaceChild(Collections.singletonList(newBlock), oldBlock);
    }

    /**
     * Replaces the specified child block with the provided sequence of blocks in this block's children list.
     *
     * The method updates parent and previous/next sibling references for inserted blocks and clears those
     * references on the removed block.
     *
     * @param newBlocks the blocks to insert in place of the old child; may be empty to remove the child
     * @param oldBlock the child block to replace; must be a direct child of this block
     * @throws InvalidParameterException if {@code oldBlock} is not a child of this block
     */
    @Override
    public void replaceChild(List<Block> newBlocks, Block oldBlock)
    {
        int position = indexOfChild(oldBlock);

        if (position == -1) {
            throw new InvalidParameterException("Provided Block to replace is not a child");
        }

        List<Block> blocks = getChildren();

        // Remove old child
        blocks.remove(position);
        oldBlock.setParent(null);

        // Insert new children
        Block previousBlock = oldBlock.getPreviousSibling();
        if (newBlocks.isEmpty() && previousBlock != null) {
            previousBlock.setNextSiblingBlock(oldBlock.getNextSibling());
        }
        Block lastBlock = null;
        for (Block block : newBlocks) {
            block.setParent(this);
            block.setPreviousSiblingBlock(previousBlock);
            if (previousBlock != null) {
                previousBlock.setNextSiblingBlock(block);
            }
            previousBlock = block;
            lastBlock = block;
        }
        Block nextBlock = oldBlock.getNextSibling();
        if (nextBlock != null) {
            nextBlock.setPreviousSiblingBlock(lastBlock);
        }
        if (lastBlock != null) {
            lastBlock.setNextSiblingBlock(nextBlock);
        }

        blocks.addAll(position, newBlocks);

        oldBlock.setNextSiblingBlock(null);
        oldBlock.setPreviousSiblingBlock(null);
    }

    /**
     * Return the zero-based index of the specified child block within this block's children.
     *
     * @param block the child block to locate
     * @return the index of the block, or -1 if the block is not a child
     */
    private int indexOfChild(Block block)
    {
        return indexOfBlock(block, getChildren());
    }

    /**
     * Get the zero-based position of the specified block in a descendant-or-self traversal of this block.
     *
     * @param child the block to locate
     * @return the index of the block where 0 is this block, or -1 if the block is not found
     * @since 10.10RC1
     */
    public long indexOf(Block child)
    {
        CounterBlockMatcher counter = new CounterBlockMatcher(child);

        Block found = getFirstBlock(counter, Axes.DESCENDANT_OR_SELF);

        return found != null ? counter.getCount() : -1;
    }

    /**
     * Get the child blocks of this block.
     *
     * @return the list of child blocks, or an empty list if there are no children
     */
    @Override
    public List<Block> getChildren()
    {
        return this.childrenBlocks == null ? Collections.emptyList() : this.childrenBlocks;
    }

    /**
     * Get the parent of this block in the block tree.
     *
     * @return the parent {@link Block}, or {@code null} if this block has no parent.
     */
    @Override
    public Block getParent()
    {
        return this.parentBlock;
    }

    /**
     * Provide the block's parameters as a read-only map.
     *
     * @return an unmodifiable map of parameter names to values, or an empty map if no parameters are set
     */
    @Override
    public Map<String, String> getParameters()
    {
        return this.parameters == null ? Collections.emptyMap()
            : Collections.unmodifiableMap(this.parameters);
    }

    /**
     * Retrieve the value of the specified parameter for this block.
     *
     * @param name the parameter name
     * @return the parameter value, or null if the parameter is not present
     */
    @Override
    public String getParameter(String name)
    {
        return this.parameters == null ? null : this.parameters.get(name);
    }

    /**
     * Set the value of a named parameter for this block.
     *
     * If a parameter with the same name already exists, it is replaced.
     *
     * @param name the parameter name
     * @param value the parameter value
     */
    @Override
    public void setParameter(String name, String value)
    {
        if (this.parameters == null) {
            this.parameters = new LinkedHashMap<>(1);
        }

        this.parameters.put(name, value);
    }

    /**
     * Replace this block's parameters with the entries from the given map.
     *
     * The provided map is copied into the block's internal parameters storage so subsequent modifications to the
     * argument will not affect the block.
     *
     * @param parameters the map whose entries will become the block's parameters
     */
    @Override
    public void setParameters(Map<String, String> parameters)
    {
        if (this.parameters == null) {
            this.parameters = new LinkedHashMap<>(parameters);
        } else {
            this.parameters.clear();
            this.parameters.putAll(parameters);
        }
    }

    /**
     * Set this block's parent.
     *
     * @param parentBlock the parent block to set, or {@code null} to remove the parent
     */
    @Override
    public void setParent(Block parentBlock)
    {
        this.parentBlock = parentBlock;
    }

    /**
     * Locate the root ancestor of this block.
     *
     * @return the root ancestor block — the highest ancestor whose parent is null
     */
    @Override
    public Block getRoot()
    {
        Block block = this;

        while (block.getParent() != null) {
            block = block.getParent();
        }

        return block;
    }

    /**
     * Retrieve the next sibling block in the parent's children sequence.
     *
     * @return the next sibling block, or {@code null} if none.
     */
    @Override
    public Block getNextSibling()
    {
        return this.nextSiblingBlock;
    }

    /**
     * Get the previous sibling block.
     *
     * @return the previous sibling block, or {@code null} if none
     */
    @Override
    public Block getPreviousSibling()
    {
        return this.previousSiblingBlock;
    }

    /**
     * Removes the specified direct child from this block and detaches it from the sibling chain.
     *
     * The provided block must be an immediate child of this block; it will be removed from the children list,
     * the surrounding siblings will be re-linked, and the removed block's previous/next sibling references will
     * be cleared.
     *
     * @param childBlockToRemove the child block to remove from this block
     * @throws InvalidParameterException if the provided block is not a child of this block
     */
    @Override
    public void removeBlock(Block childBlockToRemove)
    {
        // Remove block
        List<Block> children = getChildren();
        int position = indexOfBlock(childBlockToRemove, children);
        if (position == -1) {
            throw new InvalidParameterException("Provided Block to remove is not a child");
        }
        getChildren().remove(position);

        // Re-calculate internal links between blocks
        if (childBlockToRemove != null) {
            Block previousBlock = childBlockToRemove.getPreviousSibling();
            if (previousBlock != null) {
                previousBlock.setNextSiblingBlock(childBlockToRemove.getNextSibling());
            }
            Block nextBlock = childBlockToRemove.getNextSibling();
            if (nextBlock != null) {
                nextBlock.setPreviousSiblingBlock(previousBlock);
            }
            childBlockToRemove.setNextSiblingBlock(null);
            childBlockToRemove.setPreviousSiblingBlock(null);
        }
    }

    /**
     * Compare this block with another object for structural equality.
     *
     * @param obj the object to compare with this block
     * @return `true` if {@code obj} is a {@link Block} whose children and parameters are equal to this block's,
     *         `false` otherwise
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) {
            return true;
        }

        if (obj instanceof Block) {
            EqualsBuilder builder = new EqualsBuilder();

            builder.append(getChildren(), ((Block) obj).getChildren());
            builder.append(getParameters(), ((Block) obj).getParameters());

            return builder.isEquals();
        }

        return false;
    }

    /**
     * Compute a hash code for this block based on its children and parameters.
     *
     * @return the hash code computed from the block's children list and parameters map
     */
    @Override
    public int hashCode()
    {
        HashCodeBuilder builder = new HashCodeBuilder();

        builder.append(this.childrenBlocks);
        builder.append(this.parameters);

        return builder.toHashCode();
    }

    /**
     * Create a copy of this block.
     *
     * @return the cloned block
     */
    @Override
    public Block clone()
    {
        return clone(null);
    }

    /**
         * Create a deep clone of this block, applying an optional filter to its children.
         *
         * The returned block is a copy of this block with parameters duplicated. Child blocks are cloned; when a
         * {@code blockFilter} is provided, each cloned child is passed to the filter and the filter's result is used
         * as the replacement children. If the filter returns an empty list for a cloned child, that cloned child's
         * own children (if any) are used instead.
         *
         * @param blockFilter the filter to apply to cloned child blocks, or {@code null} to clone children unchanged
         * @return the cloned block with copied parameters and cloned (and possibly filtered) children
         * @since 1.8RC2
         */
    @Override
    public Block clone(BlockFilter blockFilter)
    {
        Block block;
        try {
            block = (AbstractBlock) super.clone();
        } catch (CloneNotSupportedException e) {
            // Should never happen
            throw new RuntimeException("Failed to clone object", e);
        }

        if (this.parameters != null) {
            ((AbstractBlock) block).parameters = new LinkedHashMap<>(this.parameters);
        }

        if (this.childrenBlocks != null) {
            ((AbstractBlock) block).childrenBlocks = new ArrayList<>(this.childrenBlocks.size());
            for (Block childBlock : this.childrenBlocks) {
                if (blockFilter != null) {
                    Block clonedChildBlocks = childBlock.clone(blockFilter);

                    List<Block> filteredBlocks = blockFilter.filter(clonedChildBlocks);

                    if (filteredBlocks.isEmpty()) {
                        filteredBlocks = clonedChildBlocks.getChildren();
                    }

                    block.addChildren(filteredBlocks);
                } else {
                    block.addChild(childBlock.clone());
                }
            }
        }

        return block;
    }

    /**
     * Traverse this block and its subtree, emitting traversal events to the provided listener.
     *
     * @param listener the listener that will receive events for this block and its descendants; invoked for the block before its children are visited and after its children have been visited
     */
    @Override
    public void traverse(Listener listener)
    {
        before(listener);

        for (Block block : getChildren()) {
            block.traverse(listener);
        }

        after(listener);
    }

    /**
     * Emit listener events that mark the start of this block.
     *
     * These events are emitted before this block's children are traversed.
     *
     * @param listener the listener that will receive start events for this block
     */
    public void before(Listener listener)
    {
        // Do nothing by default, should be overridden by extending Blocks
    }

    /**
     * Emit end-of-block events to the provided listener (e.g., close a tag like </b>).
     *
     * Implementations should send the events that mark the end of this block's rendering; the default
     * implementation is a no-op and subclasses should override when they need to emit end events.
     *
     * @param listener the listener that will receive the end-of-block events
     */
    public void after(Listener listener)
    {
        // Do nothing by default, should be overridden by extending Blocks
    }

    /**
     * Find all blocks that match the given matcher within the specified search axes.
     *
     * @param matcher the predicate used to select matching blocks
     * @param axes the traversal axes that define the scope of the search
     * @return a list of matching blocks in traversal order, or an empty list if none match
     */
    @Override
    public <T extends Block> List<T> getBlocks(BlockMatcher matcher, Axes axes)
    {
        BlockNavigator navigator = new BlockNavigator(matcher);

        return navigator.getBlocks(this, axes);
    }

    /**
     * Finds the first block that matches the provided matcher within the search axes relative to this block.
     *
     * @param matcher the matcher used to test blocks for a match
     * @param axes the axes that define the search scope relative to this block
     * @return the first matching block, or {@code null} if no matching block is found
     */
    @Override
    public <T extends Block> T getFirstBlock(BlockMatcher matcher, Axes axes)
    {
        BlockNavigator navigator = new BlockNavigator(matcher);

        return navigator.getFirstBlock(this, axes);
    }
}
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

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.xwiki.component.util.DefaultParameterizedType;
import org.xwiki.rendering.block.match.BlockMatcher;
import org.xwiki.rendering.listener.Listener;

/**
 * Represents an element of a XWiki Document's content. For example there are Blocks for Paragraphs, Bold parts,
 * Sections, Links, etc. A block has a parent and can have children too for Blocks which are wrapper around other blocks
 * (e.g. Paragraph blocks, List blocks, Bold blocks).
 *
 * @version $Id$
 * @since 1.5M2
 */
public interface Block extends Cloneable
{
    /**
     * Helper to represent the Type for each List&lt;Block&gt;.
     *
     * @since 10.10RC1
     */
    Type LIST_BLOCK_TYPE = new DefaultParameterizedType(null, List.class, Block.class);

    /**
     * Search axes used in searching methods. Mostly taken from XPATH axes.
     *
     * @version $Id$
     * @since 3.0M3
     */
    enum Axes
    {

        /** Just the context block itself. */
        SELF,

        /** The parent of the context block, if there is one. */
        PARENT,

        /**
         * The ancestors of the context block; the ancestors of the context block consist of the parent of context block
         * and the parent's parent and so on; thus, the ancestor axis will always include the root block, unless the
         * context block is the root block.
         */
        ANCESTOR,

        /**
         * The context block and the ancestors of the context block; thus, the ancestor axis will always include the
         * root block.
         */
        ANCESTOR_OR_SELF,

        /** The children of the context block. */
        CHILD,

        /** The descendants of the context block; a descendant is a child or a child of a child and so on. */
        DESCENDANT,

        /** The context block and the descendants of the context block. */
        DESCENDANT_OR_SELF,

        /**
         * All blocks in the same document as the context block that are after the context block in document order,
         * excluding any descendants.
         */
        FOLLOWING,

        /** All the following siblings of the context block. */
        FOLLOWING_SIBLING,

        /**
         * All blocks in the same document as the context block that are before the context block in document order,
         * excluding any ancestors.
         */
        PRECEDING,

        /** All the preceding siblings of the context block. */
        PRECEDING_SIBLING
    }

    /**
 * Notify the given {@link Listener} of events that represent this block's content.
 *
 * For example, a paragraph block will send {@link org.xwiki.rendering.listener.Listener#beginParagraph}
 * and {@link org.xwiki.rendering.listener.Listener#endParagraph} events.
 *
 * @param listener the listener that will receive events for this block
 */
    void traverse(Listener listener);

    /**
 * Appends the given block to the end of this block's children.
 *
 * @param blockToAdd the block to append as a child
 */
    void addChild(Block blockToAdd);

    /**
 * Appends the given blocks to the end of this block's children list.
 *
 * @param blocksToAdd the blocks to add as children
 */
    void addChildren(List<? extends Block> blocksToAdd);

    /**
 * Replace the current children with the provided list of {@link Block} instances.
 *
 * @param children the list of blocks to use as the new children
 */
    void setChildren(List<? extends Block> children);

    /**
 * Insert a single child block immediately before a specified existing child.
 *
 * @param blockToInsert the child block to insert
 * @param nextBlock the existing child block that will follow the inserted block
 * @since 1.6M1
 */
    void insertChildBefore(Block blockToInsert, Block nextBlock);

    /**
 * Insert a child block immediately after a specified existing child.
 *
 * @param blockToInsert the child block to insert
 * @param previousBlock the existing child block that will precede the inserted block
 * @since 1.6M1
 */
    void insertChildAfter(Block blockToInsert, Block previousBlock);

    /**
 * Replace an existing child block with another block and set the new block's parent to this block.
 *
 * @param newBlock the block that will replace the existing child
 * @param oldBlock the existing child block to be replaced
 */
    void replaceChild(Block newBlock, Block oldBlock);

    /**
 * Replace an existing child block with the given list of blocks and set each new block's parent to this block.
 *
 * @param newBlocks the blocks to insert in place of the existing child; their parent will be set to this block
 * @param oldBlock the existing child block to be replaced
 */
    void replaceChild(List<Block> newBlocks, Block oldBlock);

    /**
 * Return the parent block in the document tree.
 *
 * @return the parent block; the top-level parent is the {@link XDOM} instance
 */
    Block getParent();

    /**
 * Set the parent of this block.
 *
 * @param parentBlock the parent block to assign
 */
    void setParent(Block parentBlock);

    /**
 * The list of this block's child blocks in document order.
 *
 * @return the list of child blocks; an empty list if there are no children
 * @see #addChildren(java.util.List)
 */
    List<Block> getChildren();

    /**
 * Get the top-level block of this block's tree.
 *
 * @return the root block of the tree, or this block if it is already the root
 */
    Block getRoot();

    /**
 * Removes the specified child block from this block's list of children.
 *
 * @param childBlockToRemove the child block to remove
 * @since 2.6RC1
 */
    void removeBlock(Block childBlockToRemove);

    /**
 * Get the next sibling of this block.
 *
 * @return the next sibling block, or {@code null} if there is no next sibling
 * @since 2.6RC1
 */
    Block getNextSibling();

    /**
 * Set the immediate next sibling of this block.
 *
 * @param nextSiblingBlock the block to set as the next sibling, or `null` to clear the reference
 * @since 2.6RC1
 */
    void setNextSiblingBlock(Block nextSiblingBlock);

    /**
 * Retrieve the previous sibling of this block within its parent's child list.
 *
 * @return the previous sibling block, or null if there is no previous sibling
 * @since 2.6RC1
 */
    Block getPreviousSibling();

    /**
 * Set the previous sibling of this block.
 *
 * @param previousSiblingBlock the block to set as this block's previous sibling, or {@code null} to clear it
 * @since 2.6RC1
 */
    void setPreviousSiblingBlock(Block previousSiblingBlock);

    /**
 * Create a copy of this block whose descendant children are limited to those accepted by the given filter.
 *
 * @param blockFilter the filter that decides which descendant blocks are included in the cloned block
 * @return the cloned block with its children filtered according to {@code blockFilter}
 * @since 1.8RC2
 */
    Block clone(BlockFilter blockFilter);

    /**
 * Create a clone of this block.
 *
 * @return the cloned Block
 */
    Block clone();

    /**
 * Parameters associated with this block.
 *
 * @return the map of parameters where keys are parameter names and values are parameter values
 * @since 3.0M1
 */
    Map<String, String> getParameters();

    /**
 * Retrieve the value of the named parameter attached to this block.
 *
 * <p>Parameters are arbitrary key/value metadata that renderers or extensions may use (for example, renderers
 * may convert them to element attributes).</p>
 *
 * @param name the parameter name
 * @return the parameter value, or `null` if no parameter with the given name exists
 * @since 3.0M1
 */
    String getParameter(String name);

    /**
 * Sets the value of a named parameter on this block.
 *
 * @param name the parameter name
 * @param value the parameter value, or null to remove the parameter
 * @since 3.0M1
 */
    void setParameter(String name, String value);

    /**
 * Set multiple parameters on the block from the provided map.
 *
 * Each entry in the map is applied as a parameter; entries override any existing parameter with the same name.
 *
 * @param parameters the parameters to set
 * @since 3.0M1
 */
    void setParameters(Map<String, String> parameters);

    /**
 * Retrieve blocks related to this block that match the given matcher according to the specified search axes.
 *
 * @param <T> the concrete Block type to return
 * @param matcher the matcher used to select blocks
 * @param axes the axes that define how the block tree is searched relative to this block
 * @return the matched blocks, or an empty list if none are found
 * @since 3.0M3
 */
    <T extends Block> List<T> getBlocks(BlockMatcher matcher, Axes axes);

    /**
 * Finds the first block that matches the given matcher within the specified axes.
 *
 * @param <T> the expected block type
 * @param matcher the matcher used to identify target blocks
 * @param axes the axes that define the search scope
 * @return the first matching {@link Block}, or {@code null} if none is found
 * @since 3.0M3
 */
    <T extends Block> T getFirstBlock(BlockMatcher matcher, Axes axes);
}
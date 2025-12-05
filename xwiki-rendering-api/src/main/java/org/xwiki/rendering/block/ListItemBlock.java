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

import java.util.List;
import java.util.Map;

import org.xwiki.rendering.listener.Listener;

/**
 * Represents a List item element in a page.
 *
 * @version $Id$
 * @since 1.5M2
 */
public class ListItemBlock extends AbstractBlock
{
    /**
     * Create a list item block containing the specified child blocks.
     *
     * @param childrenBlocks the child blocks that make up the list item's content
     */
    public ListItemBlock(List<Block> childrenBlocks)
    {
        super(childrenBlocks);
    }

    /**
     * Create a list item block with the given child blocks and parameters.
     *
     * @param childrenBlocks the blocks representing the list item's content
     * @param parameters a map of string parameters associated with this block (may be used to store rendering hints)
     * @since 10.0
     */
    public ListItemBlock(List<Block> childrenBlocks, Map<String, String> parameters)
    {
        super(childrenBlocks, parameters);
    }

    /**
     * Notifies the given listener that this list item is starting, providing this block's parameters.
     *
     * @param listener the listener to notify of the start of the list item
     */
    @Override
    public void before(Listener listener)
    {
        listener.beginListItem(getParameters());
    }

    /**
     * Notifies the provided listener that this list item has ended.
     *
     * @param listener the listener to notify; receives this block's parameters with the end event
     */
    @Override
    public void after(Listener listener)
    {
        listener.endListItem(getParameters());
    }
}
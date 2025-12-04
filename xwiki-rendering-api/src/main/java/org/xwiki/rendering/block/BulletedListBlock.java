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

import org.xwiki.rendering.listener.ListType;
import org.xwiki.rendering.listener.Listener;

/**
 * Represents a Bulleted list.
 *
 * @version $Id$
 * @since 1.5M2
 */
public class BulletedListBlock extends AbstractBlock implements ListBLock
{
    /**
     * Create a bulleted list block containing the given child blocks.
     *
     * @param childrenBlocks the child blocks that make up the list
     */
    public BulletedListBlock(List<Block> childrenBlocks)
    {
        super(childrenBlocks);
    }

    /**
     * Create a bulleted list block containing the given child blocks and configured with the provided parameters.
     *
     * @param childrenBlocks the blocks that form the list items
     * @param parameters additional block parameters (see {@link org.xwiki.rendering.block.AbstractBlock#getParameter(String)})
     */
    public BulletedListBlock(List<Block> childrenBlocks, Map<String, String> parameters)
    {
        super(childrenBlocks, parameters);
    }

    /**
     * Signals the start of a bulleted list to the provided rendering listener.
     *
     * Notifies the listener to begin a bulleted list and passes this block's parameters.
     *
     * @param listener the listener to notify
     */
    @Override
    public void before(Listener listener)
    {
        listener.beginList(ListType.BULLETED, getParameters());
    }

    /**
     * Signal the end of a bulleted list to the given listener.
     *
     * Notifies the listener that a bulleted list has ended and provides this block's parameters.
     *
     * @param listener the listener to notify
     */
    @Override
    public void after(Listener listener)
    {
        listener.endList(ListType.BULLETED, getParameters());
    }
}
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
 * Represents a definition list. For example in HTML this is the equivalent of &lt;dl&gt;.
 *
 * @version $Id$
 * @since 1.6M2
 */
public class DefinitionListBlock extends AbstractBlock implements ListBLock
{
    /**
     * Create a DefinitionListBlock containing the given child blocks and no parameters.
     *
     * @param childrenBlocks the child blocks that form the definition list
     */
    public DefinitionListBlock(List<Block> childrenBlocks)
    {
        super(childrenBlocks);
    }

    /**
     * Create a definition list block containing the given child blocks and configured with the provided parameters.
     *
     * @param childrenBlocks the child blocks contained in this definition list
     * @param parameters a map of block parameters (see {@link org.xwiki.rendering.block.AbstractBlock#getParameter(String)} for retrieval details)
     */
    public DefinitionListBlock(List<Block> childrenBlocks, Map<String, String> parameters)
    {
        super(childrenBlocks, parameters);
    }

    /**
     * Signals the start of this definition list to the given listener.
     *
     * @param listener the listener to notify; receives this block's parameters when the definition list begins
     */
    @Override
    public void before(Listener listener)
    {
        listener.beginDefinitionList(getParameters());
    }

    /**
     * Signal the end of this definition list to the provided listener.
     *
     * @param listener the rendering listener to notify; the block's parameters are passed to the listener
     */
    @Override
    public void after(Listener listener)
    {
        listener.endDefinitionList(getParameters());
    }
}
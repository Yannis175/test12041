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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.xwiki.rendering.listener.Listener;

/**
 * Represents a grouping of blocks.
 *
 * @version $Id$
 * @since 1.8.3
 */
public class GroupBlock extends AbstractBlock
{
    /**
     * Creates an empty GroupBlock with no children.
     */
    public GroupBlock()
    {
        this(Collections.emptyList());
    }

    /**
     * Creates an empty GroupBlock initialized with the given parameters.
     *
     * @param parameters the parameters of the group
     */
    public GroupBlock(Map<String, String> parameters)
    {
        this(Collections.emptyList(), parameters);
    }

    /**
     * Creates a GroupBlock containing the specified child blocks.
     *
     * @param blocks the child blocks to include in the group
     */
    public GroupBlock(List<Block> blocks)
    {
        super(blocks);
    }

    /**
     * Creates a GroupBlock containing the specified child blocks and associated parameters.
     *
     * @param blocks the child blocks contained in this group
     * @param parameters a map of parameter names to values applied to this group
     */
    public GroupBlock(List<Block> blocks, Map<String, String> parameters)
    {
        super(blocks, parameters);
    }

    /**
     * Notifies the listener of the start of this group.
     *
     * @param listener the listener to notify of the group's start, receiving this block's parameters
     */
    @Override
    public void before(Listener listener)
    {
        listener.beginGroup(getParameters());
    }

    /**
     * Signals the end of this group to the provided listener.
     *
     * @param listener the listener to notify of the group end; receives this block's parameters
     */
    @Override
    public void after(Listener listener)
    {
        listener.endGroup(getParameters());
    }
}
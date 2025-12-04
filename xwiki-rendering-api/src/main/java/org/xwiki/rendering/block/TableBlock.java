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
 * Represents a table.
 *
 * @version $Id$
 * @since 1.6M2
 */
public class TableBlock extends AbstractBlock
{
    /**
     * Create a table block containing the given child blocks.
     *
     * @param list the child blocks of the table (typically {@link TableRowBlock} instances)
     * @since 4.2M1
     */
    public TableBlock(List<Block> list)
    {
        super(list);
    }

    /**
     * Create a table block with the given child blocks and parameters.
     *
     * @param list the child blocks of the table (generally a list of {@link TableRowBlock})
     * @param parameters the parameters for the table
     */
    public TableBlock(List<Block> list, Map<String, String> parameters)
    {
        super(list, parameters);
    }

    /**
     * Notifies the given listener that table rendering is starting, supplying this block's parameters.
     *
     * @param listener the listener to notify
     */
    @Override
    public void before(Listener listener)
    {
        listener.beginTable(getParameters());
    }

    /**
     * Signals the end of this table to the given listener.
     *
     * @param listener the listener to notify of the table end; the block's parameters are passed to the listener
     */
    @Override
    public void after(Listener listener)
    {
        listener.endTable(getParameters());
    }
}
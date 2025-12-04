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
 * Represents a cell of a table.
 *
 * @version $Id$
 * @since 1.6M2
 */
public class TableCellBlock extends AbstractBlock
{
    /**
     * Create a table cell block containing the specified child blocks.
     *
     * @param list the child blocks contained in this table cell
     * @since 4.2M1
     */
    public TableCellBlock(List<Block> list)
    {
        super(list);
    }

    /**
     * Create a table cell block with the given child blocks and parameters.
     *
     * @param list the child blocks contained within this table cell
     * @param parameters a map of parameter names to values for this table cell
     */
    public TableCellBlock(List<Block> list, Map<String, String> parameters)
    {
        super(list, parameters);
    }

    /**
     * Notifies the given listener that rendering of this table cell is starting, passing this block's parameters.
     *
     * @param listener the listener to notify
     */
    @Override
    public void before(Listener listener)
    {
        listener.beginTableCell(getParameters());
    }

    /**
     * Signal the end of this table cell using the block's parameters.
     *
     * @param listener the listener to notify
     */
    @Override
    public void after(Listener listener)
    {
        listener.endTableCell(getParameters());
    }
}
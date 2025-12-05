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

import java.util.Map;

import org.xwiki.rendering.listener.Listener;

/**
 * Represents a Horizontal line.
 *
 * @version $Id$
 * @since 1.6M1
 */
public class HorizontalLineBlock extends AbstractBlock
{
    /**
     * Construct a Horizontal Line Block with no parameters.
     */
    public HorizontalLineBlock()
    {
        super();
    }

    /**
     * Create a horizontal line block with the specified parameters.
     *
     * @param parameters a map of parameter names to values used to initialize the block
     */
    public HorizontalLineBlock(Map<String, String> parameters)
    {
        super(parameters);
    }

    /**
     * Emits a horizontal-line event to the given listener.
     *
     * Invokes the listener's horizontal line callback with this block's parameters.
     *
     * @param listener the listener to notify about the horizontal line
     */
    @Override
    public void traverse(Listener listener)
    {
        listener.onHorizontalLine(getParameters());
    }
}
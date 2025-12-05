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
 * Tags the content as a figure (image(s), diagram, code fragment, audio, video, charts, etc).
 * This is similar to the HTML5 {@code <figure>} element), with the ability to associate a caption to the figure.
 *
 * @version $Id$
 * @since 10.2
 */
public class FigureBlock extends AbstractBlock
{
    /**
     * Create a FigureBlock containing the given child blocks.
     *
     * @param blocks the child blocks that make up the figure (for example caption and content blocks)
     */
    public FigureBlock(List<Block> blocks)
    {
        super(blocks);
    }

    /**
     * Create a figure container with the given child blocks and parameters.
     *
     * @param blocks the child blocks contained in the figure (may include a caption block)
     * @param parameters the parameters/attributes associated with the figure that are exposed to the renderer
     */
    public FigureBlock(List<Block> blocks, Map<String, String> parameters)
    {
        super(blocks, parameters);
    }

    /**
     * Notify the rendering listener that a figure block is starting, passing this block's parameters.
     */
    @Override
    public void before(Listener listener)
    {
        listener.beginFigure(getParameters());
    }

    /**
     * Signals the end of this figure block to the given rendering listener.
     *
     * @param listener the rendering listener to notify; it will receive this block's parameters when ending the figure
     */
    @Override
    public void after(Listener listener)
    {
        listener.endFigure(getParameters());
    }
}
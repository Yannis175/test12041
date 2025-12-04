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
 * Represents a figure caption (similar to the HTML5 {@code <figcaption>} element). Needs to be a children of the
 * {@link FigureBlock} block (either the first children block or the last children block).
 *
 * @version $Id$
 * @since 10.2
 */
public class FigureCaptionBlock extends AbstractBlock
{
    /**
     * Create a figure caption block containing the given child blocks.
     *
     * @param blocks the child blocks that compose the caption
     */
    public FigureCaptionBlock(List<Block> blocks)
    {
        super(blocks);
    }

    /**
     * Create a figure caption block with the given child blocks and parameters.
     *
     * @param blocks the child blocks that compose the caption
     * @param parameters the parameters associated with the caption
     */
    public FigureCaptionBlock(List<Block> blocks, Map<String, String> parameters)
    {
        super(blocks, parameters);
    }

    /**
     * Signal the start of this figure caption to the provided listener.
     *
     * Invokes {@code listener.beginFigureCaption} with this block's parameters.
     *
     * @param listener the listener to notify about the beginning of the figure caption
     */
    @Override
    public void before(Listener listener)
    {
        listener.beginFigureCaption(getParameters());
    }

    /**
     * Notify the listener that the figure caption has ended.
     *
     * @param listener the listener to notify; receives this block's parameters via {@code endFigureCaption}
     */
    @Override
    public void after(Listener listener)
    {
        listener.endFigureCaption(getParameters());
    }
}
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
 * @version $Id$
 * @since 1.5M2
 */
public class ParagraphBlock extends AbstractBlock
{
    /**
     * Create a paragraph block that contains the given child blocks.
     *
     * @param blocks the child blocks contained in this paragraph
     */
    public ParagraphBlock(List<Block> blocks)
    {
        super(blocks);
    }

    /**
     * Create a paragraph block containing the given child blocks and parameters.
     *
     * @param blocks the child blocks of the paragraph
     * @param parameters a map of parameter names to values for the paragraph
     */
    public ParagraphBlock(List<Block> blocks, Map<String, String> parameters)
    {
        super(blocks, parameters);
    }

    /**
     * Signals the start of this paragraph to the provided listener using this block's parameters.
     *
     * @param listener the listener to notify of the paragraph start
     */
    @Override
    public void before(Listener listener)
    {
        listener.beginParagraph(getParameters());
    }

    /**
     * Signals the end of this paragraph to the provided listener.
     *
     * @param listener the listener to notify; receives this block's parameters in the endParagraph call
     */
    @Override
    public void after(Listener listener)
    {
        listener.endParagraph(getParameters());
    }
}
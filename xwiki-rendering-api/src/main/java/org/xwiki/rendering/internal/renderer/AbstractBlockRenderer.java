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
package org.xwiki.rendering.internal.renderer;

import java.io.Flushable;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.renderer.BlockRenderer;
import org.xwiki.rendering.renderer.PrintRenderer;
import org.xwiki.rendering.renderer.PrintRendererFactory;
import org.xwiki.rendering.renderer.printer.WikiPrinter;

/**
 * Common code for BlockRender implementation that uses Print Renderer Factory.
 *
 * @version $Id$
 * @since 2.0M3
 */
public abstract class AbstractBlockRenderer implements BlockRenderer
{
    @Inject
    protected Logger logger;

    /**
 * Provide the factory used to create {@link PrintRenderer} instances.
 *
 * @return the {@link PrintRendererFactory} used to create {@link PrintRenderer} instances
 */
    protected abstract PrintRendererFactory getPrintRendererFactory();

    /**
     * Render a single block to the given WikiPrinter.
     *
     * @param block   the block to render
     * @param printer the printer that receives the rendered output
     */
    @Override
    public void render(Block block, WikiPrinter printer)
    {
        render(Collections.singletonList(block), printer);
    }

    /**
     * Renders a collection of blocks to the given WikiPrinter.
     *
     * <p>Creates a PrintRenderer via the factory returned by {@link #getPrintRendererFactory()}, traverses each block
     * with that renderer, and if the renderer implements {@link java.io.Flushable} attempts to flush it. If flushing
     * fails with an {@link java.io.IOException}, the error is logged when a logger is available.
     *
     * @param blocks  the blocks to render
     * @param printer the target printer receiving the rendered output
     */
    @Override
    public void render(Collection<Block> blocks, WikiPrinter printer)
    {
        PrintRenderer renderer = getPrintRendererFactory().createRenderer(printer);
        for (Block block : blocks) {
            block.traverse(renderer);
        }

        if (renderer instanceof Flushable) {
            try {
                ((Flushable) renderer).flush();
            } catch (IOException e) {
                if (this.logger != null) {
                    this.logger.error("Failed to flush renderer [{}]", renderer, e);
                }
            }
        }
    }
}
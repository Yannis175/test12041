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

package org.xwiki.rendering.internal.transformation;

import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.block.XDOM;
import org.xwiki.rendering.syntax.Syntax;
import org.xwiki.rendering.transformation.RenderingContext;
import org.xwiki.rendering.transformation.Transformation;
import org.xwiki.rendering.transformation.TransformationContext;
import org.xwiki.rendering.transformation.TransformationException;

/**
 * An interface to update the rendering context internally during transformations.
 *
 * @version $Id$
 * @since 6.0RC1
 */
public interface MutableRenderingContext extends RenderingContext
{
    /**
 * Pushes a new rendering context frame for the given transformation.
 *
 * Establishes a new context layer that will be active for subsequent transformation operations.
 *
 * @param transformation the transformation being performed
 * @param context the transformation context associated with the transformation
 */
    void push(Transformation transformation, TransformationContext context);

    /**
         * Pushes a new rendering context frame containing the provided transformation state.
         *
         * @param transformation the transformation being executed
         * @param xdom the complete XDOM currently being processed
         * @param syntax the current source syntax
         * @param transformationId identifier for the transformation instance
         * @param restricted whether the transformation is running in restricted mode
         * @param targetSyntax the syntax targeted by the renderer
         */
    void push(Transformation transformation, XDOM xdom, Syntax syntax, String transformationId, boolean restricted,
        Syntax targetSyntax);

    /**
 * Remove the current rendering context frame.
 *
 * After this call the previous rendering context frame is restored.
 */
    void pop();

    /**
         * Execute the given transformation on the specified block using the provided transformation context, ensuring
         * the rendering context is set appropriately for the operation.
         *
         * @param transformation the transformation to apply
         * @param context the transformation context to use while applying the transformation
         * @param block the block to transform
         * @throws TransformationException if the transformation fails ({@link Transformation#transform(Block, TransformationContext)})
         */
    void transformInContext(Transformation transformation, TransformationContext context, Block block)
        throws TransformationException;

    /**
 * Set the block currently being processed by the transformation.
 *
 * @param block the block that is now the current processing target
 */
    void setCurrentBlock(Block block);

    /**
 * Set the syntax to be used as the rendering target for subsequent transformations.
 *
 * @param targetSyntax the syntax to use as the rendering target
 */
    void setTargetSyntax(Syntax targetSyntax);
}
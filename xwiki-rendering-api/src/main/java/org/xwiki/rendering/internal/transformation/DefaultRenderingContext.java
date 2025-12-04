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

import java.util.ArrayDeque;
import java.util.Deque;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.context.Execution;
import org.xwiki.context.ExecutionContext;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.block.XDOM;
import org.xwiki.rendering.syntax.Syntax;
import org.xwiki.rendering.transformation.Transformation;
import org.xwiki.rendering.transformation.TransformationContext;
import org.xwiki.rendering.transformation.TransformationException;

/**
 * The complete context of the transformation process.
 *
 * @version $Id$
 * @since 6.0
 */
@Component
@Singleton
public class DefaultRenderingContext implements MutableRenderingContext
{
    /**
     * Key of the this context in the execution context.
     */
    private static final String EXECUTION_CONTEXT_KEY = "rendering.context";

    /**
     * A null context to avoid special cases.
     */
    private static final Context NULL_CONTEXT = new Context();

    /**
     * Used to access the rendering context stack from the execution context.
     */
    @Inject
    private Execution execution;

    protected static final class Context implements Cloneable
    {
        /**
         * The complete {@link org.xwiki.rendering.block.XDOM} of the content currently being transformed.
         */
        private final XDOM xdom;

        /**
         * The block current block processed by the transformation (ie the macro block).
         */
        private Block currentBlock;

        /**
         * The current syntax of transformation.
         */
        private final Syntax syntax;

        /**
         * In restricted mode, only transformations that are deemed safe for execution by untrusted users will be
         * performed.
         */
        private final boolean restricted;

        /**
         * The current Transformation instance being executed.
         */
        private final Transformation transformation;

        /**
         * An id representing the transformation being evaluated.
         */
        private final String transformationId;

        /**
         * The syntax of the renderer.
         */
        private Syntax targetSyntax;

        /**
         * Create a sentinel Context that represents the absence of any rendering context.
         */
        private Context()
        {
            this(null, null, null, null, false, null);
        }

        /**
         * Create a new rendering context frame capturing the current transformation state.
         *
         * @param transformation the Transformation being executed
         * @param xdom the complete XDOM being processed
         * @param syntax the current Syntax used for the transformation
         * @param transformationId an identifier for this transformation execution
         * @param restricted true if the transformation is running in restricted mode
         * @param targetSyntax the Syntax of the target renderer for the transformation output
         */
        private Context(Transformation transformation, XDOM xdom, Syntax syntax, String transformationId,
            boolean restricted, Syntax targetSyntax)
        {
            this.transformationId = transformationId;
            this.xdom = xdom;
            this.syntax = syntax;
            this.restricted = restricted;
            this.transformation = transformation;
            this.targetSyntax = targetSyntax;
        }

        /**
         * Retrieve the identifier of the current transformation.
         *
         * @return the identifier of the current transformation, or {@code null} if none is set
         */
        public String getTransformationId()
        {
            return this.transformationId;
        }

        /**
         * Create and return a shallow copy of this Context.
         *
         * @return a shallow clone of this Context
         * @throws RuntimeException if the clone operation is not supported (should not occur)
         */
        @Override
        public Context clone()
        {
            Context newContext;
            try {
                newContext = (Context) super.clone();
            } catch (CloneNotSupportedException e) {
                // Should never happen
                throw new RuntimeException("Failed to clone object", e);
            }

            return newContext;
        }
    }

    /**
     * Pushes a new rendering context frame onto the current execution context stack using the provided
     * Transformation and values extracted from the given TransformationContext.
     *
     * @param transformation the Transformation that will be associated with the new context frame
     * @param context the TransformationContext supplying XDOM, syntax, id, restriction flag and target syntax
     */
    @Override
    public void push(Transformation transformation, TransformationContext context)
    {
        push(transformation, context.getXDOM(), context.getSyntax(), context.getId(), context.isRestricted(),
            context.getTargetSyntax());
    }

    /**
     * Pushes a new rendering context frame containing the provided transformation state onto the per-execution context stack.
     *
     * @param transformation the Transformation being entered
     * @param xdom the XDOM being processed within this context
     * @param syntax the current source Syntax for this context
     * @param id an identifier for the transformation execution (may be null)
     * @param restricted whether the transformation should run in restricted (safe) mode
     * @param targetSyntax the Syntax of the target renderer for this context (may be null)
     */
    @Override
    public void push(Transformation transformation, XDOM xdom, Syntax syntax, String id, boolean restricted,
        Syntax targetSyntax)
    {
        Deque<Context> stack = getContextStack(true);
        if (stack != null) {
            stack.push(new Context(transformation, xdom, syntax, id, restricted, targetSyntax));
        }
    }

    /**
     * Remove the top rendering context frame from the per-execution stack.
     *
     * <p>If there is no context stack for the current execution this method does nothing.</p>
     */
    @Override
    public void pop()
    {
        Deque<Context> stack = getContextStack(false);
        if (stack != null) {
            stack.pop();
        }
    }

    /**
     * Execute a transformation on the given block while the transformation and its context are pushed
     * onto the rendering context stack, and ensure the previous context is restored afterwards.
     *
     * @param transformation the transformation to execute
     * @param context the transformation execution context to push
     * @param block the block to transform
     * @throws TransformationException if the executed transformation fails
     */
    @Override
    public void transformInContext(Transformation transformation, TransformationContext context, Block block)
        throws TransformationException
    {
        try {
            push(transformation, context);
            transformation.transform(block, context);
        } finally {
            pop();
        }
    }

    /**
     * Retrieve the per-execution rendering context stack, optionally creating and storing it when absent.
     *
     * @param create if {@code true}, create and store a new {@code Deque<Context>} in the current {@code ExecutionContext}
     *               when no stack is present
     * @return the existing or newly created {@code Deque<Context>} stored under {@code EXECUTION_CONTEXT_KEY}, or
     *         {@code null} if there is no current {@code ExecutionContext}
     */
    @SuppressWarnings("unchecked")
    private Deque<Context> getContextStack(boolean create)
    {
        ExecutionContext context = this.execution.getContext();

        if (context != null) {
            Deque<Context> stack = (Deque<Context>) context.getProperty(EXECUTION_CONTEXT_KEY);

            if (stack == null && create) {
                stack = new ArrayDeque<>();
                context.setProperty(EXECUTION_CONTEXT_KEY, stack);
            }

            return stack;
        }

        return null;
    }

    /**
     * Retrieve the current rendering context frame without removing it from the stack.
     *
     * @return the current {@code Context} from the execution stack, or the {@link #NULL_CONTEXT} sentinel if no context is available
     */
    protected Context peek()
    {
        Deque<Context> stack = getContextStack(false);
        return (stack != null && !stack.isEmpty()) ? stack.peek() : NULL_CONTEXT;
    }

    /**
     * Get the XDOM associated with the current rendering context.
     *
     * @return the `XDOM` of the current rendering context, or `null` if no rendering context is active
     */
    @Override
    public XDOM getXDOM()
    {
        return peek().xdom;
    }

    /**
     * Gets the block currently being processed in the active rendering context.
     *
     * @return the current block being processed, or {@code null} if none is set
     */
    @Override
    public Block getCurrentBlock()
    {
        return peek().currentBlock;
    }

    /**
     * Update the current block in the active rendering context.
     *
     * <p>If there is no active rendering context, this method has no effect.</p>
     *
     * @param block the block to mark as currently processed; may be {@code null} to clear the current block
     */
    @Override
    public void setCurrentBlock(Block block)
    {
        Context context = peek();
        if (context != null && context != NULL_CONTEXT) {
            context.currentBlock = block;
        }
    }

    /**
     * Retrieve the default Syntax for the current rendering context.
     *
     * @return the default Syntax from the active context, or {@code null} if no context is available
     */
    @Override
    public Syntax getDefaultSyntax()
    {
        return peek().syntax;
    }

    /**
     * Indicates whether the current rendering context is in restricted mode.
     *
     * @return `true` if the current context is restricted, `false` otherwise.
     */
    @Override
    public boolean isRestricted()
    {
        return peek().restricted;
    }

    /**
     * Get the transformation currently executing in this rendering context.
     *
     * @return the active {@link Transformation}, or {@code null} if no transformation is set
     */
    @Override
    public Transformation getTransformation()
    {
        return peek().transformation;
    }

    /**
     * Get the identifier of the currently active transformation.
     *
     * @return the transformation identifier, or {@code null} if no transformation is active.
     */
    @Override
    public String getTransformationId()
    {
        return peek().transformationId;
    }

    /**
     * Get the renderer target syntax for the current rendering context.
     *
     * @return the target {@link Syntax} of the current rendering context, or `null` if no target syntax is set
     */
    @Override
    public Syntax getTargetSyntax()
    {
        return peek().targetSyntax;
    }

    /**
     * Set the renderer target syntax for the current rendering context frame.
     *
     * @param targetSyntax the Syntax to use for the renderer; {@code null} clears the target syntax for the current frame
     */
    @Override
    public void setTargetSyntax(Syntax targetSyntax)
    {
        Context context = peek();
        if (context != null && context != NULL_CONTEXT) {
            context.targetSyntax = targetSyntax;
        }
    }
}
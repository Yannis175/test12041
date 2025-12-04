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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.xwiki.rendering.listener.Listener;

/**
 * A Verbatim block.
 *
 * @version $Id$
 * @since 1.8M2
 */
public class VerbatimBlock extends AbstractBlock
{
    /**
     * The string to protect from rendering.
     */
    private String protectedString;

    /**
     * If true the macro is located in a inline content (like paragraph, etc.).
     */
    private boolean inline;

    /**
     * Create a VerbatimBlock that preserves a protected string and indicates whether it is inline.
     *
     * @param protectedString the string to preserve verbatim (excluded from rendering)
     * @param isInline true if the block is located in inline content (for example, inside a paragraph)
     */
    public VerbatimBlock(String protectedString, boolean isInline)
    {
        this.protectedString = protectedString;
        this.inline = isInline;
    }

    /**
     * Create a VerbatimBlock that preserves the given protected string and records whether it is inline.
     *
     * @param protectedString the string that will be preserved from rendering
     * @param parameters custom parameters associated with this block
     * @param isInline {@code true} if the block appears in inline content (for example, inside a paragraph)
     */
    public VerbatimBlock(String protectedString, Map<String, String> parameters, boolean isInline)
    {
        super(parameters);

        this.protectedString = protectedString;
        this.inline = isInline;
    }

    /**
     * Get the verbatim string preserved from rendering.
     *
     * @return the verbatim string preserved from rendering
     */
    public String getProtectedString()
    {
        return this.protectedString;
    }

    /**
     * Indicates whether this verbatim block is located in inline content.
     *
     * @return `true` if the block is inline (for example, inside a paragraph), `false` otherwise.
     */
    public boolean isInline()
    {
        return this.inline;
    }

    /**
     * Dispatches this verbatim block to the given listener.
     *
     * Invokes {@code listener.onVerbatim} with this block's protected string, inline flag, and parameters.
     *
     * @param listener the listener to notify about this verbatim block
     */
    @Override
    public void traverse(Listener listener)
    {
        listener.onVerbatim(getProtectedString(), isInline(), getParameters());
    }

    /**
     * Represent this block by its protected string.
     *
     * @return the protected string of this block
     * @since 1.8RC2
     */
    @Override
    public String toString()
    {
        return getProtectedString();
    }

    /**
     * Indicates whether the given object is equal to this VerbatimBlock.
     *
     * @param obj the object to compare with this block
     * @return {@code true} if {@code obj} is a {@code VerbatimBlock} whose superclass state is equal to this block,
     *         and whose {@code inline} flag and protected string are equal; {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) {
            return true;
        }

        if (obj instanceof VerbatimBlock && super.equals(obj)) {
            EqualsBuilder builder = new EqualsBuilder();

            builder.append(isInline(), ((VerbatimBlock) obj).isInline());
            builder.append(getProtectedString(), ((VerbatimBlock) obj).getProtectedString());

            return builder.isEquals();
        }

        return false;
    }

    /**
     * Compute the hash code for this VerbatimBlock.
     *
     * The computed value incorporates the superclass hash, the inline flag, and the protected string.
     *
     * @return the hash code value for this block
     */
    @Override
    public int hashCode()
    {
        HashCodeBuilder builder = new HashCodeBuilder();

        builder.appendSuper(super.hashCode());
        builder.append(isInline());
        builder.append(getProtectedString());

        return builder.toHashCode();
    }
}
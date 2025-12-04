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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.xwiki.rendering.listener.Listener;
import org.xwiki.rendering.syntax.Syntax;

/**
 * Represents some raw content that shouldn't be parsed or modified and that should be injected as is in any output. The
 * content depends on a syntax and listeners decide if they can handle that syntax or not. For example if it's in
 * "xhtml/1.0" syntax then the XHTML Renderer can insert it directly in the XHTML output.
 *
 * @version $Id$
 * @since 1.8.3
 */
public class RawBlock extends AbstractBlock
{
    /**
     * @see #getRawContent()
     */
    private String rawContent;

    /**
     * @see #getSyntax()
     */
    private Syntax syntax;

    /**
     * Create a RawBlock that holds raw content associated with a specific syntax.
     *
     * @param rawContent the raw content to inject without modification
     * @param syntax the syntax that describes how the content should be interpreted
     */
    public RawBlock(String rawContent, Syntax syntax)
    {
        this.rawContent = rawContent;
        this.syntax = syntax;
    }

    /**
     * Emits this block's raw content to the provided listener using the block's associated syntax.
     *
     * @param listener the listener that will receive the raw content via {@code onRawText(String, Syntax)}
     */
    @Override
    public void traverse(Listener listener)
    {
        listener.onRawText(getRawContent(), getSyntax());
    }

    /**
     * The raw content to be injected into a listener without modification.
     *
     * @return the raw text content to emit as-is
     */
    public String getRawContent()
    {
        return this.rawContent;
    }

    /**
     * The syntax associated with this raw block's content.
     *
     * @return the syntax of the raw content
     */
    public Syntax getSyntax()
    {
        return this.syntax;
    }

    /**
     * Determine whether another object is equal to this RawBlock.
     *
     * @param obj the object to compare with this RawBlock
     * @return {@code true} if {@code obj} is the same instance or is a {@code RawBlock} whose superclass state,
     *         raw content, and syntax are equal to this instance; {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) {
            return true;
        }

        if (obj instanceof RawBlock && super.equals(obj)) {
            EqualsBuilder builder = new EqualsBuilder();

            builder.append(getRawContent(), ((RawBlock) obj).getRawContent());
            builder.append(getSyntax(), ((RawBlock) obj).getSyntax());

            return builder.isEquals();
        }

        return false;
    }

    /**
     * Compute a hash code for this block including the superclass state, the raw content, and the syntax.
     *
     * The result is consistent with {@link #equals(Object)}.
     *
     * @return the hash code computed from the superclass hash, the raw content, and the syntax
     */
    @Override
    public int hashCode()
    {
        HashCodeBuilder builder = new HashCodeBuilder();

        builder.appendSuper(super.hashCode());
        builder.append(getRawContent());
        builder.append(getSyntax());

        return builder.toHashCode();
    }
}
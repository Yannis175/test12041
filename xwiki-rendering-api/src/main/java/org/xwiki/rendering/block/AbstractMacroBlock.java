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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Common class to MacroBlock and MacroMakerBlock.
 *
 * @version $Id$
 * @since 10.0
 */
public abstract class AbstractMacroBlock extends AbstractBlock
{
    /**
     * @see #getId
     */
    private String id;

    /**
     * The macro content for macro that have content. Otherwise it's null.
     */
    private String content;

    /**
     * The macro is located in a inline content (like paragraph, etc.).
     */
    private boolean inline;

    /**
     * Create a new AbstractMacroBlock with the given children, parameters, identifier, content, and inline flag.
     *
     * @param childrenBlocks the list of child blocks for this macro block
     * @param parameters the macro parameters (name → value)
     * @param id the macro identifier/name
     * @param content the macro content, or {@code null} if the macro has no content
     * @param inline {@code true} if the macro is located inline within surrounding content, {@code false} otherwise
     */
    public AbstractMacroBlock(List<? extends Block> childrenBlocks, Map<String, String> parameters, String id,
        String content, boolean inline)
    {
        super(childrenBlocks, parameters);

        this.id = id;
        this.content = content;
        this.inline = inline;
    }

    /**
     * The macro's content.
     *
     * @return the macro content, or {@code null} if the macro has no content
     */
    public String getContent()
    {
        return content;
    }

    /**
     * Get the macro identifier.
     *
     * @return the macro identifier.
     */
    public String getId()
    {
        return id;
    }

    /**
     * Indicates whether the macro is located inside inline content.
     *
     * @return `true` if the macro is inside inline content (for example, a paragraph), `false` otherwise.
     */
    public boolean isInline()
    {
        return inline;
    }

    /**
     * Determines whether the given object is equal to this macro block.
     *
     * @param obj the object to compare with this block
     * @return {@code true} if {@code obj} is an {@link AbstractMacroBlock} and has the same superclass state,
     *         content, id, and inline flag as this block; {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) {
            return true;
        }

        if (!super.equals(obj)) {
            return false;
        }

        return equals((AbstractMacroBlock) obj);
    }

    /**
     * Determine whether the given AbstractMacroBlock has the same content, id, and inline flag as this block.
     *
     * @param obj the other AbstractMacroBlock to compare with
     * @return `true` if `obj`'s content, id, and inline flag are equal to this block's, `false` otherwise
     */
    private boolean equals(AbstractMacroBlock obj)
    {
        EqualsBuilder builder = new EqualsBuilder();

        builder.append(getContent(), obj.getContent());
        builder.append(getId(), obj.getId());
        builder.append(isInline(), obj.isInline());

        return builder.isEquals();
    }

    /**
     * Compute a hash code that combines the superclass hash with this block's content, id, and inline flag.
     *
     * @return the hash code computed from the superclass hash, content, id, and inline flag
     */
    @Override
    public int hashCode()
    {
        HashCodeBuilder builder = new HashCodeBuilder();

        builder.appendSuper(super.hashCode());
        builder.append(getContent());
        builder.append(getId());
        builder.append(isInline());

        return builder.toHashCode();
    }
}
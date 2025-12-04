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
import org.xwiki.rendering.listener.HeaderLevel;
import org.xwiki.rendering.listener.Listener;
import org.xwiki.stability.Unstable;

/**
 * @version $Id$
 * @since 1.5M2
 */
public class HeaderBlock extends AbstractBlock
{
    /**
     * The level of the header.
     */
    private HeaderLevel level;

    /**
     * The id of the header.
     */
    private String id;

    /**
     * Create a header block containing the given child blocks at the specified header level.
     *
     * @param childBlocks the blocks representing the header's content
     * @param level the header level (e.g., H1..H6)
     */
    public HeaderBlock(List<Block> childBlocks, HeaderLevel level)
    {
        super(childBlocks);

        this.level = level;
    }

    /**
     * Create a header block with the given child blocks, header level, and parameters.
     *
     * @param childBlocks the child blocks that form the header content
     * @param level the header's level (e.g., H1..H6)
     * @param parameters rendering parameters associated with the header
     */
    public HeaderBlock(List<Block> childBlocks, HeaderLevel level, Map<String, String> parameters)
    {
        super(childBlocks, parameters);

        this.level = level;
    }

    /**
     * Create a header block with the given child blocks, header level, and identifier.
     *
     * @param childBlocks the child blocks that make up the header content
     * @param level the header level (e.g., H1..H6)
     * @param id the header identifier (may be null)
     */
    public HeaderBlock(List<Block> childBlocks, HeaderLevel level, String id)
    {
        this(childBlocks, level);

        this.id = id;
    }

    /**
     * Create a header block with the given child blocks, header level, optional parameters, and optional identifier.
     *
     * @param childBlocks the child blocks contained by the header
     * @param level the header level
     * @param parameters parameters associated with the header (may be null)
     * @param id the identifier for the header (may be null)
     */
    public HeaderBlock(List<Block> childBlocks, HeaderLevel level, Map<String, String> parameters, String id)
    {
        this(childBlocks, level, parameters);

        this.id = id;
    }

    /**
     * Retrieve the header's level.
     *
     * @return the header level
     */
    public HeaderLevel getLevel()
    {
        return this.level;
    }

    /**
     * Get the header's identifier.
     *
     * @return the header identifier, or null if none is set
     */
    public String getId()
    {
        return this.id;
    }

    /**
     * Assigns the header's identifier.
     *
     * @param id the header identifier, or {@code null} to clear it
     * @since 14.2RC1
     */
    @Unstable
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * Returns the section that contains this header.
     *
     * @return the {@link SectionBlock} that contains this header, or {@code null} if this header has no parent
     */
    public SectionBlock getSection()
    {
        return (SectionBlock) getParent();
    }

    /**
     * Notifies the listener that this header block is beginning, supplying its level, id, and parameters.
     *
     * @param listener the listener to notify
     */
    @Override
    public void before(Listener listener)
    {
        listener.beginHeader(getLevel(), getId(), getParameters());
    }

    /**
     * Notify the listener that processing of this header has finished.
     *
     * Passes this header's level, id, and parameters to the listener's endHeader callback.
     */
    @Override
    public void after(Listener listener)
    {
        listener.endHeader(getLevel(), getId(), getParameters());
    }

    /**
     * Compares this HeaderBlock with another object for value equality.
     *
     * Two HeaderBlock instances are considered equal if they are the same instance or if the other object is a
     * HeaderBlock whose superclass state is equal and whose header level and id are equal.
     *
     * @return `true` if the given object is equal to this HeaderBlock, `false` otherwise.
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) {
            return true;
        }

        if (obj instanceof HeaderBlock && super.equals(obj)) {
            EqualsBuilder builder = new EqualsBuilder();

            builder.append(getLevel(), ((HeaderBlock) obj).getLevel());
            builder.append(getId(), ((HeaderBlock) obj).getId());

            return builder.isEquals();
        }

        return false;
    }

    /**
     * Computes a hash code for this header block that incorporates header-specific state.
     *
     * @return the hash code computed from the superclass hash, the header level, and the header id
     */
    @Override
    public int hashCode()
    {
        HashCodeBuilder builder = new HashCodeBuilder();

        builder.appendSuper(super.hashCode());
        builder.append(getLevel());
        builder.append(getId());

        return builder.toHashCode();
    }
}
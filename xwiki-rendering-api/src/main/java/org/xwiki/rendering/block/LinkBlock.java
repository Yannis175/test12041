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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.xwiki.rendering.listener.Listener;
import org.xwiki.rendering.listener.reference.ResourceReference;

/**
 * Represents a Link element in a page.
 *
 * @version $Id$
 * @since 1.5M2
 */
public class LinkBlock extends AbstractBlock
{
    /**
     * A reference to the link target. See {@link org.xwiki.rendering.listener.reference.ResourceReference} for more
     * details.
     */
    private ResourceReference reference;

    /**
     * If true then the link is a free standing URI directly in the text.
     */
    private boolean freestanding;

    /**
     * Create a link block with the given child blocks and target reference.
     *
     * @param childrenBlocks the nested child blocks that form the link label/content
     * @param reference the target resource reference for the link
     * @param freestanding true when the link is a free-standing URI directly in the text
     * @since 2.5RC1
     */
    public LinkBlock(List<Block> childrenBlocks, ResourceReference reference, boolean freestanding)
    {
        this(childrenBlocks, reference, freestanding, Collections.<String, String>emptyMap());
    }

    /**
     * Create a LinkBlock that represents a link to a target resource.
     *
     * @param childrenBlocks the nested child blocks that form the link's content
     * @param reference the target resource reference for the link
     * @param freestanding {@code true} if the link is a free-standing URI appearing directly in the text
     * @param parameters additional block parameters (may be empty)
     * @since 2.5RC1
     */
    public LinkBlock(List<Block> childrenBlocks, ResourceReference reference, boolean freestanding,
        Map<String, String> parameters)
    {
        super(childrenBlocks, parameters);
        this.reference = reference;
        this.freestanding = freestanding;
    }

    /**
     * Get the resource reference that identifies the link target.
     *
     * @return the resource reference pointing to the link target
     * @see org.xwiki.rendering.listener.reference.ResourceReference
     * @since 2.5RC1
     */
    public ResourceReference getReference()
    {
        return this.reference;
    }

    /**
     * Indicates whether the link is a free-standing URI appearing directly in the text.
     *
     * @return `true` if the link is a free-standing URI, `false` otherwise.
     */
    public boolean isFreeStandingURI()
    {
        return this.freestanding;
    }

    /**
     * Notify the given listener that a link node is starting by invoking its beginLink method.
     *
     * @param listener the rendering listener to notify
     */
    @Override
    public void before(Listener listener)
    {
        listener.beginLink(getReference(), isFreeStandingURI(), getParameters());
    }

    /**
     * Notify the listener that this link element has ended, passing the reference, freestanding flag, and parameters.
     */
    @Override
    public void after(Listener listener)
    {
        listener.endLink(getReference(), isFreeStandingURI(), getParameters());
    }

    /**
     * Create a copy of this LinkBlock, applying the given BlockFilter to its children.
     *
     * The returned LinkBlock contains a cloned ResourceReference so modifications to the clone's reference
     * do not affect the original.
     *
     * @param blockFilter the filter to apply to child blocks during cloning
     * @return a cloned LinkBlock with its ResourceReference cloned
     * @since 1.8RC2
     */
    @Override
    public LinkBlock clone(BlockFilter blockFilter)
    {
        LinkBlock clone = (LinkBlock) super.clone(blockFilter);
        clone.reference = getReference().clone();
        return clone;
    }

    /**
     * Indicates whether this LinkBlock is equal to the specified object.
     *
     * @param obj the object to compare with this LinkBlock
     * @return `true` if the specified object is a LinkBlock, the superclass equality holds, and both the `reference`
     *         and `freestanding` flag are equal; `false` otherwise.
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) {
            return true;
        }

        if (obj instanceof LinkBlock && super.equals(obj)) {
            EqualsBuilder builder = new EqualsBuilder();

            builder.append(getReference(), ((LinkBlock) obj).getReference());
            builder.append(isFreeStandingURI(), ((LinkBlock) obj).isFreeStandingURI());

            return builder.isEquals();
        }

        return false;
    }

    /**
     * Compute a hash code for this LinkBlock that incorporates the superclass state, the link reference, and the freestanding flag.
     *
     * @return the hash code computed from the superclass hash, the link reference, and the freestanding flag
     */
    @Override
    public int hashCode()
    {
        HashCodeBuilder builder = new HashCodeBuilder();

        builder.appendSuper(super.hashCode());
        builder.append(getReference());
        builder.append(isFreeStandingURI());

        return builder.toHashCode();
    }
}
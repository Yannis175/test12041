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
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.xwiki.rendering.listener.Listener;
import org.xwiki.rendering.listener.reference.ResourceReference;
import org.xwiki.stability.Unstable;

/**
 * Represents an image.
 *
 * @version $Id$
 * @since 1.7M2
 */
public class ImageBlock extends AbstractBlock
{
    /**
     * A reference to the image target. See {@link org.xwiki.rendering.listener.reference.ResourceReference} for more
     * details.
     */
    private ResourceReference reference;

    /**
     * If true then the image is defined as a free standing URI directly in the text.
     */
    private boolean freestanding;

    /**
     * The (automatically generated) id. Optional.
     *
     * @since 14.2RC1
     */
    private String id;

    /**
     * Create an ImageBlock for the given image reference using no id and an empty parameter map.
     *
     * @param reference the image target reference
     * @param freestanding true if the image is represented as a freestanding URI in the text, false otherwise
     * @since 2.5RC1
     */
    public ImageBlock(ResourceReference reference, boolean freestanding)
    {
        this(reference, freestanding, null, Collections.emptyMap());
    }

    /**
     * Create an ImageBlock with the given reference, freestanding flag, and parameters.
     *
     * @param reference the image target reference
     * @param freestanding whether the image is represented as a freestanding URI in the source
     * @param parameters custom parameters for the image (may be empty)
     * @since 2.5RC1
     */
    public ImageBlock(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        this(reference, freestanding, null, parameters);
    }

    /**
     * Create an ImageBlock with the specified reference, freestanding flag, optional id, and parameters.
     *
     * @param reference the image target reference
     * @param freestanding {@code true} when the image is represented as a freestanding URI in the source text
     * @param id an optional automatically generated id for the image, or {@code null} when none
     * @param parameters custom parameters for the image
     * @since 14.2RC1
     */
    @Unstable
    public ImageBlock(ResourceReference reference, boolean freestanding, String id, Map<String, String> parameters)
    {
        super(parameters);

        this.reference = reference;
        this.freestanding = freestanding;
        this.id = id;
    }

    /**
     * Gets the image's resource reference.
     *
     * @return the image's {@link org.xwiki.rendering.listener.reference.ResourceReference}
     * @see org.xwiki.rendering.listener.reference.ResourceReference
     * @since 2.5RC1
     */
    public ResourceReference getReference()
    {
        return this.reference;
    }

    /**
     * Indicates whether the image was written as a freestanding URI in the source text.
     *
     * @return `true` if the image is a freestanding URI in the text, `false` otherwise
     */
    public boolean isFreeStandingURI()
    {
        return this.freestanding;
    }

    /**
     * Set the image id used when traversing or rendering; pass {@code null} to remove any existing id.
     *
     * @param id the image id, or {@code null} to unset it
     * @since 14.2RC1
     */
    @Unstable
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * The optional identifier assigned to this image.
     *
     * @return the image id, or {@code null} if no id is set.
     * @since 14.2RC1
     */
    @Unstable
    public String getId()
    {
        return this.id;
    }

    /**
     * Emit an image event to the provided listener for this image block.
     *
     * If this image has a non-null id, the listener is notified with the id and parameters; otherwise the listener
     * is notified with only the reference and parameters.
     *
     * @param listener the listener that will receive the image event
     */
    @Override
    public void traverse(Listener listener)
    {
        String idParameter = getId();
        if (idParameter == null) {
            listener.onImage(getReference(), isFreeStandingURI(), getParameters());
        } else {
            listener.onImage(getReference(), isFreeStandingURI(), idParameter, getParameters());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.8RC2
     */
    @Override
    public ImageBlock clone(BlockFilter blockFilter)
    {
        ImageBlock clone = (ImageBlock) super.clone(blockFilter);
        clone.reference = getReference().clone();
        clone.freestanding = isFreeStandingURI();
        clone.id = getId();
        return clone;
    }

    /**
     * Determines whether this ImageBlock is equal to another object.
     *
     * @param obj the object to compare against
     * @return `true` if the given object is an {@code ImageBlock} and has equal superclass state, reference,
     *         freestanding flag, and id; `false` otherwise
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        ImageBlock that = (ImageBlock) obj;

        return new EqualsBuilder()
            .appendSuper(super.equals(obj))
            .append(getReference(), that.getReference())
            .append(isFreeStandingURI(), that.isFreeStandingURI())
            .append(getId(), that.getId())
            .isEquals();
    }

    /**
     * Compute the hash code for this ImageBlock.
     *
     * @return the hash code computed from the superclass hash, the image reference, the freestanding flag, and the id
     */
    @Override
    public int hashCode()
    {
        HashCodeBuilder builder = new HashCodeBuilder();

        builder.appendSuper(super.hashCode());
        builder.append(getReference());
        builder.append(isFreeStandingURI());
        builder.append(getId());

        return builder.toHashCode();
    }
}
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

/**
 * A reference/location in a page. In HTML for example this is called an Anchor. It allows pointing to that location,
 * for example in links.
 *
 * @version $Id$
 * @since 1.6M1
 * @see Listener#onId(String)
 */
public class IdBlock extends AbstractBlock
{
    /**
     * The unique name for the reference/location.
     */
    private String name;

    /**
     * Creates an IdBlock identifying a location (anchor) within a document.
     *
     * @param name the unique identifier for the anchor location
     */
    public IdBlock(String name)
    {
        this.name = name;
    }

    /**
     * Gets the unique name of this reference (anchor) within the page.
     *
     * @return the reference name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Notify the given listener about this block's identifier.
     *
     * @param listener the listener to notify; its {@code onId} callback will be invoked with this block's name
     */
    @Override
    public void traverse(Listener listener)
    {
        listener.onId(getName());
    }

    /**
     * Determine whether this block is equal to another object.
     *
     * @param obj the object to compare with this block
     * @return {@code true} if {@code obj} is an {@link IdBlock} with equal superclass state and the same name, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) {
            return true;
        }

        if (obj instanceof IdBlock) {
            EqualsBuilder builder = new EqualsBuilder();

            builder.appendSuper(super.equals(obj));
            builder.append(getName(), ((IdBlock) obj).getName());

            return builder.isEquals();
        }

        return false;
    }

    /**
     * Compute a hash code for this IdBlock.
     *
     * @return the hash code computed from the superclass state and this block's name
     */
    @Override
    public int hashCode()
    {
        HashCodeBuilder builder = new HashCodeBuilder();

        builder.appendSuper(super.hashCode());
        builder.append(getName());

        return builder.toHashCode();
    }
}
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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.xwiki.rendering.listener.Listener;
import org.xwiki.rendering.listener.MetaData;

/**
 * Represents any kind of MetaData in the XDOM (eg saving original blocks so that the XWiki Syntax Renderer can restore
 * them after a transformation has been executed, source reference, etc).
 *
 * @version $Id$
 * @since 3.0M2
 */
public class MetaDataBlock extends AbstractBlock
{
    /**
     * Contains all MetaData for this Block and its children.
     */
    private MetaData metaData;

    /**
     * Constructs a MetaDataBlock that associates the provided metadata with the given child blocks.
     *
     * @param childBlocks the child blocks contained by this block
     * @param metaData the metadata applied to this block and its descendants
     * @see AbstractBlock#AbstractBlock(List)
     */
    public MetaDataBlock(List<? extends Block> childBlocks, MetaData metaData)
    {
        super(childBlocks);
        this.metaData = metaData;
    }

    /**
     * Create a MetaDataBlock containing the given child blocks and a single metadata entry.
     *
     * @param childBlocks the child blocks to attach to the created MetaDataBlock
     * @param key the metadata key to store
     * @param value the metadata value to store for the given key
     * @see AbstractBlock#AbstractBlock(List)
     */
    public MetaDataBlock(List<? extends Block> childBlocks, String key, Object value)
    {
        this(childBlocks, new MetaData(Collections.singletonMap(key, value)));
    }

    /**
     * Create a MetaDataBlock containing the given child blocks and an empty MetaData.
     *
     * @param childBlocks the child blocks to include in this MetaDataBlock
     * @see AbstractBlock#AbstractBlock(List)
     */
    public MetaDataBlock(List<? extends Block> childBlocks)
    {
        this(childBlocks, new MetaData());
    }

    /**
     * Retrieve the metadata attached to this block and its descendants.
     *
     * @return the MetaData instance associated with this block and its descendants
     */
    public MetaData getMetaData()
    {
        return this.metaData;
    }

    /**
     * Notify the listener that metadata processing begins for this block and its descendants.
     *
     * @param listener the listener to notify of the metadata begin event
     */
    @Override
    public void before(Listener listener)
    {
        listener.beginMetaData(getMetaData());
    }

    /**
     * Notifies the listener that processing of this block's metadata has ended.
     *
     * @param listener the listener to receive the endMetaData event for this block's metadata
     */
    @Override
    public void after(Listener listener)
    {
        listener.endMetaData(getMetaData());
    }

    /**
     * Create a clone of this MetaDataBlock with its metadata copied into a new MetaData instance.
     *
     * @return the cloned MetaDataBlock whose {@code metaData} is a new copy of this block's metadata
     */
    @Override
    public MetaDataBlock clone()
    {
        MetaDataBlock cloned = (MetaDataBlock) super.clone();

        cloned.metaData = new MetaData(this.metaData.getMetaData());

        return cloned;
    }

    /**
     * Determine whether the given object is equal to this MetaDataBlock by checking superclass equality and
     * comparing the contained MetaData.
     *
     * @param obj the object to compare with this block
     * @return {@code true} if {@code obj} is a MetaDataBlock, the superclass parts are equal, and both blocks have
     *         equal metadata; {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) {
            return true;
        }

        if (obj instanceof MetaDataBlock && super.equals(obj)) {
            EqualsBuilder builder = new EqualsBuilder();

            builder.append(getMetaData(), ((MetaDataBlock) obj).getMetaData());

            return builder.isEquals();
        }

        return false;
    }

    /**
     * Compute a hash code that combines the superclass state with this block's metadata.
     *
     * @return the hash code value derived from the superclass hash and this block's {@link #getMetaData() metadata}
     */
    @Override
    public int hashCode()
    {
        HashCodeBuilder builder = new HashCodeBuilder();

        builder.appendSuper(super.hashCode());
        builder.append(getMetaData());

        return builder.toHashCode();
    }
}
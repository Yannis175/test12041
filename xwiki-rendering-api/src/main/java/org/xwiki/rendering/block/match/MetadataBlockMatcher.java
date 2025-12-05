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
package org.xwiki.rendering.block.match;

import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.block.MetaDataBlock;
import org.xwiki.rendering.listener.MetaData;

/**
 * Implementation of {@link BlockMatcher} which matches {@link MetaData} information.
 *
 * @version $Id$
 * @since 3.0M3
 */
public class MetadataBlockMatcher extends ClassBlockMatcher
{
    /**
     * The key of the {@link MetaData}.
     */
    private String metadataKey;

    /**
     * The value of the {@link MetaData}.
     */
    private Object metadataValue;

    /**
     * Create a matcher that matches MetaDataBlock instances containing a metadata entry with the given key.
     *
     * @param metadataKey the metadata key to check for presence
     */
    public MetadataBlockMatcher(String metadataKey)
    {
        this(metadataKey, null);
    }

    /**
     * Create a matcher that targets MetaDataBlock instances and matches a MetaData entry by key and optional value.
     *
     * @param metadataKey the metadata key to match
     * @param metadataValue the expected value for the key, or {@code null} to match only presence of the key
     */
    public MetadataBlockMatcher(String metadataKey, Object metadataValue)
    {
        super(MetaDataBlock.class);

        this.metadataKey = metadataKey;
        this.metadataValue = metadataValue;
    }

    /**
     * Checks whether the given block is a MetaDataBlock whose metadata matches the configured key and optional value.
     *
     * @param block the block to test for a metadata match
     * @return {@code true} if the block is a MetaDataBlock and its metadata contains the configured key (and equals the
     *         configured value when one was provided), {@code false} otherwise
     */
    @Override
    public boolean match(Block block)
    {
        return super.match(block) && matchMetadata(((MetaDataBlock) block).getMetaData());
    }

    /**
     * Check whether the provided MetaData contains the configured key and, if a value was specified, whether that
     * value equals the configured metadata value.
     *
     * @param metadata the MetaData to check for the configured key (and value, if specified)
     * @return {@code true} if the key is present (and equals the configured value when one is provided), {@code false} otherwise
     */
    private boolean matchMetadata(MetaData metadata)
    {
        boolean match;

        if (this.metadataValue != null) {
            Object value = metadata.getMetaData(this.metadataKey);
            match = value != null && value.equals(this.metadataValue);
        } else {
            match = metadata.contains(this.metadataKey);
        }

        return match;
    }
}
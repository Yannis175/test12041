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
package org.xwiki.rendering.listener;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a set of MetaData.
 *
 * @version $Id$
 * @since 3.0M2
 */
public class MetaData
{
    /**
     * Represents no MetaData.
     */
    public static final MetaData EMPTY = new MetaData();

    /**
     * Represents a source metaData, which corresponds to the reference to the source entity containing the content to
     * render. The reference is a free form text in a format that is understood by the Listeners supporting it.
     */
    public static final String SOURCE = "source";

    /**
     * Represents the syntax of the content found in macro containing wiki content (like a box macro for example). The
     * value has to be a {@link org.xwiki.rendering.syntax.Syntax} object.
     *
     * @since 3.0M3
     */
    public static final String SYNTAX = "syntax";

    /**
     * Represent the base reference to resolve all the references in the blocks (links, images, macro parameters, etc.).
     *
     * @since 3.4M1
     */
    public static final String BASE = "base";

    /**
     * Represents a non generated content: a content that has not been transformed in any way.
     *
     * @since 10.10
     */
    public static final String NON_GENERATED_CONTENT = "non-generated-content";

    /**
     * Represents a metadata attached to a specific parameter identified by the given name.
     *
     * @since 11.1RC1
     */
    public static final String PARAMETER_NAME = "parameter-name";

    /**
     * Contains all MetaData for this Block and its children. Note: we preserve the order of metadata elements as they
     * are added as a service for the user so he can count on it.
     */
    private final Map<String, Object> metadata;

    /**
     * Create an empty MetaData instance backed by an insertion-order map.
     */
    public MetaData()
    {
        this.metadata = new LinkedHashMap<String, Object>();
    }

    /**
     * Creates a MetaData instance containing a copy of the given map's entries, preserving their insertion order.
     *
     * @param metaData the map whose entries will be copied into the new MetaData
     */
    public MetaData(Map<String, Object> metaData)
    {
        this.metadata = new LinkedHashMap<String, Object>(metaData);
    }

    /**
     * Add or update a metadata entry with the given key and value.
     *
     * If an entry with the same key already exists, its value is replaced.
     *
     * @param key the metadata key (e.g. "syntax")
     * @param value the metadata value (e.g. a Syntax object)
     */
    public void addMetaData(String key, Object value)
    {
        this.metadata.put(key, value);
    }

    /**
     * Adds all entries from the given MetaData into this instance, preserving insertion order.
     *
     * @param metaData the metadata whose entries will be added; entries with keys that already exist overwrite current values
     */
    public void addMetaData(MetaData metaData)
    {
        this.metadata.putAll(metaData.getMetaData());
    }

    /**
     * Retrieve the metadata value for the given key.
     *
     * @param key the metadata key (for example, "syntax")
     * @return the value associated with {@code key}, or {@code null} if no value is present
     */
    public Object getMetaData(String key)
    {
        return this.metadata.get(key);
    }

    /**
     * Checks whether metadata contains the specified key.
     *
     * @param key the metadata key to check
     * @return `true` if metadata contains the given key, `false` otherwise
     * @since 3.0M3
     */
    public boolean contains(String key)
    {
        return this.metadata.containsKey(key);
    }

    /**
     * Get an unmodifiable view of all metadata entries.
     *
     * @return an unmodifiable map of metadata entries (in insertion order) where keys are metadata names and values are their associated objects
     */
    public Map<String, Object> getMetaData()
    {
        return Collections.unmodifiableMap(this.metadata);
    }

    /**
     * Determine whether this MetaData is equal to another object.
     *
     * @param obj the object to compare with this MetaData
     * @return {@code true} if {@code obj} is a {@link MetaData} instance whose metadata mappings are equal to this
     *         instance's mappings, {@code false} otherwise
     */

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) {
            return true;
        }

        return obj instanceof MetaData && this.metadata.equals(((MetaData) obj).metadata);
    }

    /**
     * Compute a hash code for this MetaData instance based on its stored entries.
     *
     * @return an int hash code reflecting the contents (and insertion order) of the stored metadata
     */
    @Override
    public int hashCode()
    {
        return this.metadata.hashCode();
    }

    /**
     * Produce a string representation of this MetaData including its internal metadata map.
     *
     * @return the string representation in the form "MetaData{metadata=...}"
     */
    @Override
    public String toString()
    {
        return "MetaData{metadata=" + metadata + '}';
    }
}
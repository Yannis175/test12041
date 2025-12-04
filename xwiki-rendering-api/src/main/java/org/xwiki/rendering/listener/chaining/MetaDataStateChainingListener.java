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
package org.xwiki.rendering.listener.chaining;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import org.xwiki.rendering.listener.MetaData;

/**
 * Provides the accumulated MetaData for all the previous blocks.
 *
 * @version $Id$
 * @since 3.0M2
 */
public class MetaDataStateChainingListener extends AbstractChainingListener
{
    /**
     * @see #getMetaData(String)
     */
    private Deque<MetaData> metaDataStack = new ArrayDeque<MetaData>();

    /**
     * Create a MetaDataStateChainingListener that delegates listener events to the given listener chain.
     *
     * @param listenerChain the ListenerChain to which this chaining listener will forward events
     */
    public MetaDataStateChainingListener(ListenerChain listenerChain)
    {
        setListenerChain(listenerChain);
    }

    /**
     * Collects all non-null metadata values for the specified key from the metadata stack, from most recent to oldest.
     *
     * @param <T> the expected type of the metadata values
     * @param key the metadata key to look up
     * @return a list of metadata values for the key in reverse-chronological order (most recent first); empty if none found
     */
    public <T> List<T> getAllMetaData(String key)
    {
        List<T> result = new ArrayList<T>();
        if (!this.metaDataStack.isEmpty()) {
            Iterator<MetaData> it = this.metaDataStack.descendingIterator();
            while (it.hasNext()) {
                MetaData metaData = it.next();
                Object value = metaData.getMetaData(key);
                if (value != null) {
                    result.add((T) metaData.getMetaData(key));
                }
            }
        }
        return result;
    }

    /**
     * Retrieves the first non-null metadata value associated with the given key from the stacked MetaData.
     *
     * @param <T> the expected type of the metadata value
     * @param key the metadata key to look up
     * @return the first non-null value for the key found in the stack, or {@code null} if none is present
     */
    public <T> T getMetaData(String key)
    {
        T result = null;
        if (!this.metaDataStack.isEmpty()) {
            for (MetaData metaData : this.metaDataStack) {
                result = (T) metaData.getMetaData(key);
                if (result != null) {
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Begins a document scope and records the provided MetaData on the internal stack for later retrieval.
     *
     * @param metaData the metadata associated with the document
     */
    @Override
    public void beginDocument(MetaData metaData)
    {
        this.metaDataStack.push(metaData);
        super.beginDocument(metaData);
    }

    /**
     * Ends the current document and removes its associated MetaData from the internal stack.
     *
     * @param metaData the MetaData associated with the document being ended
     */
    @Override
    public void endDocument(MetaData metaData)
    {
        super.endDocument(metaData);
        this.metaDataStack.pop();
    }

    /**
     * Registers the start of a metadata block by storing the provided metadata and forwarding the event
     * to the chained listener.
     *
     * @param metaData the metadata for the new metadata block that will be pushed onto the internal stack
     */
    @Override
    public void beginMetaData(MetaData metaData)
    {
        this.metaDataStack.push(metaData);
        super.beginMetaData(metaData);
    }

    /**
     * Handles the end of a metadata block by delegating to the chained listener and removing the corresponding
     * MetaData from the internal stack.
     *
     * @param metaData the MetaData instance for the metadata block being closed
     */
    @Override
    public void endMetaData(MetaData metaData)
    {
        super.endMetaData(metaData);
        this.metaDataStack.pop();
    }
}
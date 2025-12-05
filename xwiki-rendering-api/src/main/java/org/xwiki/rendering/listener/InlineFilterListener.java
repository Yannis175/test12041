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

import java.util.Map;

/**
 * Wrap a listener and skip begin/endDocument events.
 *
 * @version $Id$
 * @since 3.0M3
 */
public class InlineFilterListener extends WrappingListener
{
    /**
     * Suppresses the document begin event so the wrapped listener is not notified.
     *
     * @param metadata the document metadata associated with the event
     */
    @Override
    public void beginDocument(MetaData metadata)
    {
        // Disable this event
    }

    /**
     * Suppresses the document end event; this implementation performs no action.
     *
     * @param metadata the metadata associated with the document end event
     */
    @Override
    public void endDocument(MetaData metadata)
    {
        // Disable this event
    }

    /**
     * Suppresses the begin-section event by performing no action.
     *
     * @param parameters a map of section parameters, possibly empty
     */
    @Override
    public void beginSection(Map<String, String> parameters)
    {
        // Disable this event
    }

    /**
     * Suppresses the end of a section event so it is not forwarded to the wrapped listener.
     *
     * @param parameters section parameters (attributes) associated with the section
     */
    @Override
    public void endSection(Map<String, String> parameters)
    {
        // Disable this event
    }

    /**
     * Suppresses the paragraph begin event; this implementation performs no action.
     *
     * @param parameters a map of paragraph parameters (may be empty or null)
     */
    @Override
    public void beginParagraph(Map<String, String> parameters)
    {
        // Disable this event
    }

    /**
     * Suppresses the paragraph end event so it is not handled.
     *
     * @param parameters the paragraph event parameters, may be null or empty
     */
    @Override
    public void endParagraph(Map<String, String> parameters)
    {
        // Disable this event
    }
}
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
 * Automatically generate section events from header events.
 * 
 * @version $Id$
 * @since 8.2RC1
 */
public class SectionGeneratorListener extends WrappingListener
{
    private int sectionDepth = -1;

    /**
     * Create a SectionGeneratorListener that translates header events into section begin/end events and forwards events to the given wrapped listener.
     *
     * @param listener the listener to wrap and forward generated events to
     */
    public SectionGeneratorListener(Listener listener)
    {
        setWrappedListener(listener);
    }

    /**
     * Adjusts the current section nesting to match the given header level, then forwards the header event.
     *
     * Closes any open sections deeper than the header level and opens sections until the nesting depth equals the
     * header level's ordinal, then delegates the header event to the wrapped listener.
     *
     * @param level the header level indicating the target section depth
     * @param id an optional identifier for the header, or {@code null} if none
     * @param parameters additional header parameters (may be empty)
     */
    @Override
    public void beginHeader(HeaderLevel level, String id, Map<String, String> parameters)
    {
        // Close sections that need to be closed
        for (; this.sectionDepth >= level.ordinal(); --this.sectionDepth) {
            endSection(Listener.EMPTY_PARAMETERS);
        }

        // Open new sections
        for (; this.sectionDepth < level.ordinal(); ++this.sectionDepth) {
            beginSection(Listener.EMPTY_PARAMETERS);
        }

        super.beginHeader(level, id, parameters);
    }

    /**
     * Closes any open sections and then forwards the document end event to the wrapped listener.
     *
     * @param metadata document metadata passed to the wrapped listener
     */
    @Override
    public void endDocument(MetaData metadata)
    {
        // Close sections that need to be closed
        for (; this.sectionDepth > -1; --this.sectionDepth) {
            endSection(Listener.EMPTY_PARAMETERS);
        }

        super.endDocument(metadata);
    }
}
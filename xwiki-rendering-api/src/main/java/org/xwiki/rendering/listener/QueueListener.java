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

import java.util.LinkedList;
import java.util.Map;

import org.xwiki.rendering.listener.chaining.EventType;
import org.xwiki.rendering.listener.reference.ResourceReference;
import org.xwiki.rendering.syntax.Syntax;

/**
 * Manage a {@link java.util.Queue} of events.
 *
 * @version $Id$
 * @since 2.1M1
 */
public class QueueListener extends LinkedList<QueueListener.Event> implements Listener
{
    /**
     * Class ID for serialization.
     */
    private static final long serialVersionUID = 2869508092440006345L;

    /**
     * An event.
     *
     * @version $Id$
     */
    public class Event
    {
        /**
         * The type of the event.
         */
        public EventType eventType;

        /**
         * The parameters of the event.
         */
        public Object[] eventParameters;

        /**
         * Creates a new Event that records an event type and its associated parameters for queued replay.
         *
         * @param eventType the EventType identifying the event
         * @param eventParameters the parameters for the event (may be empty)
         */
        public Event(EventType eventType, Object... eventParameters)
        {
            this.eventType = eventType;
            this.eventParameters = eventParameters;
        }
    }

    /**
     * Retrieve the queued event at the given 1-based depth.
     *
     * @param depth the 1-based position of the event to retrieve (1 = head of queue)
     * @return the Event at the specified depth, or {@code null} if {@code depth} is less than 1 or greater than the queue size
     */
    public Event getEvent(int depth)
    {
        Event event = null;

        if (depth > 0 && size() > depth - 1) {
            event = get(depth - 1);
        }

        return event;
    }

    /**
     * Replays and removes all queued events by dispatching them to the given listener.
     *
     * @param listener the Listener that will receive the replayed events
     */
    public void consumeEvents(Listener listener)
    {
        while (!isEmpty()) {
            Event event = remove();
            event.eventType.fireEvent(listener, event.eventParameters);
        }
    }

    /**
     * Enqueues an Event constructed from the given type and parameters.
     *
     * @param eventType the event type
     * @param objects the event parameters
     */
    private void saveEvent(EventType eventType, Object... objects)
    {
        offer(new Event(eventType, objects));
    }

    /**
     * Records the start of a definition description element in this listener's event queue.
     */
    @Override
    public void beginDefinitionDescription()
    {
        saveEvent(EventType.BEGIN_DEFINITION_DESCRIPTION);
    }

    /**
     * Record the start of a definition list, capturing the provided parameters for later replay.
     *
     * @param parameters attributes for the definition list; may be {@code null}
     * @since 2.0RC1
     */
    @Override
    public void beginDefinitionList(Map<String, String> parameters)
    {
        saveEvent(EventType.BEGIN_DEFINITION_LIST, parameters);
    }

    /**
     * Enqueues an event that marks the beginning of a definition term.
     */
    @Override
    public void beginDefinitionTerm()
    {
        saveEvent(EventType.BEGIN_DEFINITION_TERM);
    }

    /**
     * {@inheritDoc}
     *
     * @since 3.0M2
     */
    @Override
    public void beginDocument(MetaData metadata)
    {
        saveEvent(EventType.BEGIN_DOCUMENT, metadata);
    }

    /**
     * Records the start of a group element with the provided parameters for later replay.
     *
     * @param parameters map of parameters for the group element, or {@code null} if none
     */
    @Override
    public void beginGroup(Map<String, String> parameters)
    {
        saveEvent(EventType.BEGIN_GROUP, parameters);
    }

    /**
     * Queue a BEGIN_FORMAT event for the specified format and parameters.
     *
     * @param format the formatting descriptor to begin
     * @param parameters optional rendering parameters associated with the format
     */
    @Override
    public void beginFormat(Format format, Map<String, String> parameters)
    {
        saveEvent(EventType.BEGIN_FORMAT, format, parameters);
    }

    /**
     * Enqueues a begin-header event with the specified header level, identifier, and parameters.
     *
     * @param level the header level
     * @param id the header identifier, or null if none
     * @param parameters additional parameters for the header, or null if none
     */
    @Override
    public void beginHeader(HeaderLevel level, String id, Map<String, String> parameters)
    {
        saveEvent(EventType.BEGIN_HEADER, level, id, parameters);
    }

    /**
     * Record the start of a link element with its target and attributes.
     *
     * @param reference   the link target reference
     * @param freestanding whether the link is freestanding (not inline)
     * @param parameters  additional link parameters/attributes
     * @since 2.5RC1
     */
    @Override
    public void beginLink(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        saveEvent(EventType.BEGIN_LINK, reference, freestanding, parameters);
    }

    /**
     * Queues a "begin list" event for a list of the specified type with the given rendering parameters.
     *
     * @param type the type of the list
     * @param parameters additional rendering parameters for the list; may be {@code null}
     */
    @Override
    public void beginList(ListType type, Map<String, String> parameters)
    {
        saveEvent(EventType.BEGIN_LIST, type, parameters);
    }

    /**
     * Queues an event marking the beginning of a list item for later replay.
     */
    @Override
    public void beginListItem()
    {
        saveEvent(EventType.BEGIN_LIST_ITEM);
    }

    /**
     * Enqueues a begin-list-item event using the provided parameters.
     *
     * @param parameters additional attributes for the list item, or {@code null} if none
     */
    @Override
    public void beginListItem(Map<String, String> parameters)
    {
        saveEvent(EventType.BEGIN_LIST_ITEM, parameters);
    }

    /**
     * Queues an event indicating the start of a macro marker.
     *
     * @param name the macro marker name
     * @param parameters parameters provided to the macro marker, or null if none
     * @param content the macro marker content, or null if none
     * @param isInline whether the macro marker is inline
     */
    @Override
    public void beginMacroMarker(String name, Map<String, String> parameters, String content, boolean isInline)
    {
        saveEvent(EventType.BEGIN_MACRO_MARKER, name, parameters, content, isInline);
    }

    /**
     * Queues a BEGIN_PARAGRAPH event containing the provided paragraph attributes for later replay.
     *
     * @param parameters paragraph attributes to attach to the event; may be {@code null}
     */
    @Override
    public void beginParagraph(Map<String, String> parameters)
    {
        saveEvent(EventType.BEGIN_PARAGRAPH, parameters);
    }

    /**
     * Record the start of a quotation block with optional rendering parameters.
     *
     * @param parameters mapping of parameter names to values for the quotation block, or {@code null} if none
     */
    @Override
    public void beginQuotation(Map<String, String> parameters)
    {
        saveEvent(EventType.BEGIN_QUOTATION, parameters);
    }

    /**
     * Record the start of a quotation line in the listener's event queue.
     */
    @Override
    public void beginQuotationLine()
    {
        saveEvent(EventType.BEGIN_QUOTATION_LINE);
    }

    /**
     * Enqueues a section-begin event for later replay.
     *
     * @param parameters a map of section attributes (e.g., id, classes), or {@code null} if there are no parameters
     */
    @Override
    public void beginSection(Map<String, String> parameters)
    {
        saveEvent(EventType.BEGIN_SECTION, parameters);
    }

    /**
     * Queues a begin-table event using the provided rendering parameters.
     *
     * @param parameters rendering parameters for the table (may be null)
     */
    @Override
    public void beginTable(Map<String, String> parameters)
    {
        saveEvent(EventType.BEGIN_TABLE, parameters);
    }

    /**
     * Record the start of a table cell with the provided parameters.
     *
     * @param parameters a map of parameters for the table cell (may be null)
     */
    @Override
    public void beginTableCell(Map<String, String> parameters)
    {
        saveEvent(EventType.BEGIN_TABLE_CELL, parameters);
    }

    /**
     * Signals the start of a table header cell.
     *
     * @param parameters optional attributes for the table header cell (may be {@code null})
     */
    @Override
    public void beginTableHeadCell(Map<String, String> parameters)
    {
        saveEvent(EventType.BEGIN_TABLE_HEAD_CELL, parameters);
    }

    /**
     * Enqueues a table-row begin event to be replayed later.
     *
     * @param parameters a map of parameters (attributes) for the table row; may be null
     */
    @Override
    public void beginTableRow(Map<String, String> parameters)
    {
        saveEvent(EventType.BEGIN_TABLE_ROW, parameters);
    }

    /**
     * Queues a BEGIN_METADATA event containing the given document metadata.
     *
     * @param metadata the document metadata to enqueue with the event
     * @since 3.0M2
     */
    @Override
    public void beginMetaData(MetaData metadata)
    {
        saveEvent(EventType.BEGIN_METADATA, metadata);
    }

    /**
     * Signal the end of a definition description.
     */
    @Override
    public void endDefinitionDescription()
    {
        saveEvent(EventType.END_DEFINITION_DESCRIPTION);
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.0RC1
     */
    @Override
    public void endDefinitionList(Map<String, String> parameters)
    {
        saveEvent(EventType.END_DEFINITION_LIST, parameters);
    }

    /**
     * Queues an event indicating the end of a definition term.
     */
    @Override
    public void endDefinitionTerm()
    {
        saveEvent(EventType.END_DEFINITION_TERM);
    }

    /**
     * {@inheritDoc}
     *
     * @since 3.0M2
     */
    @Override
    public void endDocument(MetaData metadata)
    {
        saveEvent(EventType.END_DOCUMENT, metadata);
    }

    /**
     * Records the end of the current group, capturing the provided parameters for later replay.
     *
     * @param parameters additional attributes for the group; may be null
     */
    @Override
    public void endGroup(Map<String, String> parameters)
    {
        saveEvent(EventType.END_GROUP, parameters);
    }

    /**
     * Record the end of a formatted region with the specified format and parameters.
     *
     * @param format the format that was applied to the region
     * @param parameters additional attributes for the format, or {@code null} if none
     */
    @Override
    public void endFormat(Format format, Map<String, String> parameters)
    {
        saveEvent(EventType.END_FORMAT, format, parameters);
    }

    /**
     * Queue an end-header event for the specified header level, id, and parameters.
     *
     * @param level the header level being closed
     * @param id an optional header identifier, may be {@code null}
     * @param parameters rendering parameters associated with the header, may be {@code null}
     */
    @Override
    public void endHeader(HeaderLevel level, String id, Map<String, String> parameters)
    {
        saveEvent(EventType.END_HEADER, level, id, parameters);
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.5RC1
     */
    @Override
    public void endLink(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        saveEvent(EventType.END_LINK, reference, freestanding, parameters);
    }

    /**
     * Records the end of a list by enqueueing an END_LIST event.
     *
     * @param type the type of list being ended
     * @param parameters optional parameters associated with the list (may be {@code null})
     */
    @Override
    public void endList(ListType type, Map<String, String> parameters)
    {
        saveEvent(EventType.END_LIST, type, parameters);
    }

    /**
     * Marks the end of the current list item by recording an END_LIST_ITEM event in the queue.
     */
    @Override
    public void endListItem()
    {
        saveEvent(EventType.END_LIST_ITEM);
    }

    /**
     * Record the end of a list item event and enqueue it for later replay.
     *
     * @param parameters additional attributes for the list item event, or {@code null} if there are none
     */
    @Override
    public void endListItem(Map<String, String> parameters)
    {
        saveEvent(EventType.END_LIST_ITEM, parameters);
    }

    /**
     * Records the end of a macro marker event into the queue.
     *
     * @param name the macro marker name
     * @param parameters attributes associated with the macro marker, or {@code null} if none
     * @param content the macro marker content, or {@code null} if none
     * @param isInline {@code true} if the macro marker is inline, {@code false} if it is block-level
     */
    @Override
    public void endMacroMarker(String name, Map<String, String> parameters, String content, boolean isInline)
    {
        saveEvent(EventType.END_MACRO_MARKER, name, parameters, content, isInline);
    }

    /**
     * Enqueues an event that signals the end of a paragraph, capturing any associated parameters.
     *
     * @param parameters optional rendering parameters for the paragraph, or {@code null} if none
     */
    @Override
    public void endParagraph(Map<String, String> parameters)
    {
        saveEvent(EventType.END_PARAGRAPH, parameters);
    }

    /**
     * Signals the end of a quotation block, providing optional rendering parameters.
     *
     * @param parameters rendering parameters associated with the quotation, or null if none
     */
    @Override
    public void endQuotation(Map<String, String> parameters)
    {
        saveEvent(EventType.END_QUOTATION, parameters);
    }

    /**
     * Queues an event indicating the end of a quotation line.
     */
    @Override
    public void endQuotationLine()
    {
        saveEvent(EventType.END_QUOTATION_LINE);
    }

    /**
     * Records the start of a figure element with the given parameters into the listener's event queue.
     *
     * @param parameters mapping of attribute names to values for the figure element, or {@code null} if none
     */
    @Override
    public void beginFigure(Map<String, String> parameters)
    {
        saveEvent(EventType.BEGIN_FIGURE, parameters);
    }

    /**
     * Records the end of a figure element by queuing an END_FIGURE event with the provided parameters.
     *
     * @param parameters rendering parameters for the figure, or {@code null} if none
     */
    @Override
    public void endFigure(Map<String, String> parameters)
    {
        saveEvent(EventType.END_FIGURE, parameters);
    }

    /**
     * Records a begin-figure-caption event with the given parameters for later replay.
     *
     * @param parameters a map of parameters to associate with the figure caption (e.g., attributes or metadata)
     */
    @Override
    public void beginFigureCaption(Map<String, String> parameters)
    {
        saveEvent(EventType.BEGIN_FIGURE_CAPTION, parameters);
    }

    /**
     * Mark the end of the current figure caption.
     *
     * @param parameters rendering parameters for the figure caption, or {@code null} if none
     */
    @Override
    public void endFigureCaption(Map<String, String> parameters)
    {
        saveEvent(EventType.END_FIGURE_CAPTION, parameters);
    }

    /**
     * Marks the end of a section by recording an END_SECTION event with the provided parameters.
     *
     * @param parameters a map of section parameters/attributes (may be {@code null})
     */
    @Override
    public void endSection(Map<String, String> parameters)
    {
        saveEvent(EventType.END_SECTION, parameters);
    }

    /**
     * Mark the end of the current table and capture its parameters.
     *
     * @param parameters mapping of table parameters (for example attributes); may be {@code null}
     */
    @Override
    public void endTable(Map<String, String> parameters)
    {
        saveEvent(EventType.END_TABLE, parameters);
    }

    /**
     * Records an event that marks the end of the current table cell, capturing the provided parameters.
     *
     * @param parameters map of parameters or attributes associated with the table cell, or {@code null} if none
     */
    @Override
    public void endTableCell(Map<String, String> parameters)
    {
        saveEvent(EventType.END_TABLE_CELL, parameters);
    }

    /**
     * Signals the end of a table header cell and records the event.
     *
     * @param parameters a map of attributes for the table header cell, or {@code null} if none
     */
    @Override
    public void endTableHeadCell(Map<String, String> parameters)
    {
        saveEvent(EventType.END_TABLE_HEAD_CELL, parameters);
    }

    /**
     * Record the end of the current table row as an event.
     *
     * @param parameters a map of parameters (attributes) for the table row, or {@code null} if none
     */
    @Override
    public void endTableRow(Map<String, String> parameters)
    {
        saveEvent(EventType.END_TABLE_ROW, parameters);
    }

    /**
     * {@inheritDoc}
     *
     * @since 3.0M2
     */
    @Override
    public void endMetaData(MetaData metadata)
    {
        saveEvent(EventType.END_METADATA, metadata);
    }

    /**
     * Queues a raw text event for later replay to another Listener.
     *
     * @param text   the raw text content
     * @param syntax the syntax associated with the raw text
     */
    @Override
    public void onRawText(String text, Syntax syntax)
    {
        saveEvent(EventType.ON_RAW_TEXT, text, syntax);
    }

    /**
     * Enqueues an event representing a sequence of consecutive empty lines.
     *
     * @param count the number of consecutive empty lines
     */
    @Override
    public void onEmptyLines(int count)
    {
        saveEvent(EventType.ON_EMPTY_LINES, count);
    }

    /**
     * Queues a horizontal line event with optional rendering parameters.
     *
     * @param parameters rendering parameters for the horizontal line, or {@code null} if none
     */
    @Override
    public void onHorizontalLine(Map<String, String> parameters)
    {
        saveEvent(EventType.ON_HORIZONTAL_LINE, parameters);
    }

    /**
     * Queues an identifier event with the given name for later replay to a target listener.
     *
     * @param name the identifier name
     */
    @Override
    public void onId(String name)
    {
        saveEvent(EventType.ON_ID, name);
    }

    /**
     * Records an image event in the queue for later delivery to a listener.
     *
     * @param reference the image resource reference
     * @param freestanding true if the image is freestanding (not inline), false otherwise
     * @param parameters rendering parameters for the image
     * @since 2.5RC1
     */
    @Override
    public void onImage(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        saveEvent(EventType.ON_IMAGE, reference, freestanding, parameters);
    }

    /**
     * Queues an image event containing the image reference, whether it's freestanding, an optional id, and rendering parameters.
     *
     * @param reference   the image resource reference
     * @param freestanding true if the image is freestanding (block-level), false if inline
     * @param id          an optional identifier for the image (may be null)
     * @param parameters  rendering parameters associated with the image
     */
    @Override
    public void onImage(ResourceReference reference, boolean freestanding, String id, Map<String, String> parameters)
    {
        saveEvent(EventType.ON_IMAGE, reference, freestanding, id, parameters);
    }

    /**
     * Queues a macro event containing the macro identifier, parameter map, macro content, and whether it is inline.
     *
     * @param id the macro identifier
     * @param parameters a map of macro parameters, or {@code null} if none
     * @param content the macro content, or {@code null} if none
     * @param inline {@code true} if the macro is inline, {@code false} if it is freestanding
     */
    @Override
    public void onMacro(String id, Map<String, String> parameters, String content, boolean inline)
    {
        saveEvent(EventType.ON_MACRO, id, parameters, content, inline);
    }

    /**
     * Records a new-line event in the queue for later replay to a target Listener.
     */
    @Override
    public void onNewLine()
    {
        saveEvent(EventType.ON_NEW_LINE);
    }

    /**
     * Records a space event in the listener's event queue for later processing.
     */
    @Override
    public void onSpace()
    {
        saveEvent(EventType.ON_SPACE);
    }

    /**
     * Record a special-symbol event for the given character.
     *
     * @param symbol the special character encountered
     */
    @Override
    public void onSpecialSymbol(char symbol)
    {
        saveEvent(EventType.ON_SPECIAL_SYMBOL, symbol);
    }

    /**
     * Queues a verbatim text event for later replay.
     *
     * @param content the verbatim text content
     * @param inline  true if the verbatim content is inline, false if block-level
     * @param parameters additional rendering parameters for the verbatim content
     */
    @Override
    public void onVerbatim(String content, boolean inline, Map<String, String> parameters)
    {
        saveEvent(EventType.ON_VERBATIM, content, inline, parameters);
    }

    /**
     * Queues a word token to be replayed later to a target Listener.
     *
     * @param word the word text to record
     */
    @Override
    public void onWord(String word)
    {
        saveEvent(EventType.ON_WORD, word);
    }
}
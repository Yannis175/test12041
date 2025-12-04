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

import java.util.Map;

import org.xwiki.rendering.listener.Format;
import org.xwiki.rendering.listener.HeaderLevel;
import org.xwiki.rendering.listener.ListType;
import org.xwiki.rendering.listener.MetaData;
import org.xwiki.rendering.listener.QueueListener;
import org.xwiki.rendering.listener.QueueListener.Event;
import org.xwiki.rendering.listener.reference.ResourceReference;
import org.xwiki.rendering.syntax.Syntax;

/**
 * Stores events without emitting them back in order to accumulate them and to provide a lookahead feature. The
 * lookahead depth is configurable.
 *
 * @version $Id$
 * @since 1.8RC1
 */
public class LookaheadChainingListener extends AbstractChainingListener
{
    private QueueListener previousEvents = new QueueListener();

    private int lookaheadDepth;

    /**
     * Create a LookaheadChainingListener that buffers incoming events and defers forwarding until the buffer
     * exceeds the configured lookahead depth.
     *
     * @param listenerChain the downstream listener chain to forward events to after lookahead conditions are met
     * @param lookaheadDepth the number of events to buffer before emitting them to the downstream listener
     */
    public LookaheadChainingListener(ListenerChain listenerChain, int lookaheadDepth)
    {
        setListenerChain(listenerChain);
        setLookaheadDepth(lookaheadDepth);
    }

    /**
     * Set how many events are buffered before being forwarded to the next listener.
     *
     * @param lookaheadDepth the number of events to keep in the lookahead buffer before emitting
     * @since 10.5RC1
     */
    protected void setLookaheadDepth(int lookaheadDepth)
    {
        this.lookaheadDepth = lookaheadDepth;
    }

    /**
     * Provides access to the internal buffer of previously received events.
     *
     * @return the QueueListener that stores buffered events
     * @since 10.5RC1
     */
    protected QueueListener getPreviousEvents()
    {
        return this.previousEvents;
    }

    /**
     * Retrieve the next buffered event at the default lookahead depth.
     *
     * @return the buffered event at lookahead depth 1, or {@code null} if the buffer is empty
     */
    public Event getNextEvent()
    {
        return getNextEvent(1);
    }

    /**
     * Retrieve the buffered event at the specified lookahead depth without removing it from the buffer.
     *
     * @param depth the 1-based depth in the lookahead buffer (1 is the next event to be emitted)
     * @return the buffered event at the specified depth, or {@code null} if no event exists at that depth
     */
    public Event getNextEvent(int depth)
    {
        return this.previousEvents.getEvent(depth);
    }

    /**
     * Buffers a "begin definition description" event for lookahead processing, then emits a previously buffered
     * event if the buffer has exceeded the configured lookahead depth.
     */
    @Override
    public void beginDefinitionDescription()
    {
        this.previousEvents.beginDefinitionDescription();
        firePreviousEvent();
    }

    /**
     * Buffers a "begin definition list" event and emits a previously buffered event when the lookahead threshold is exceeded.
     *
     * @param parameters parameters for the definition list element
     * @since 2.0RC1
     */
    @Override
    public void beginDefinitionList(Map<String, String> parameters)
    {
        this.previousEvents.beginDefinitionList(parameters);
        firePreviousEvent();
    }

    /**
     * Buffers a "begin definition term" event and, when the buffered events exceed the configured lookahead depth,
     * emits the next buffered event to the downstream listener.
     */
    @Override
    public void beginDefinitionTerm()
    {
        this.previousEvents.beginDefinitionTerm();
        firePreviousEvent();
    }

    /**
         * Buffers a begin-document event and then flushes all buffered events to the downstream listener.
         *
         * @param metadata the document metadata associated with the begin-document event
         * @since 3.0M2
         */
    @Override
    public void beginDocument(MetaData metadata)
    {
        this.previousEvents.beginDocument(metadata);
        flush();
    }

    /**
     * Buffers a "begin group" event with the given parameters and, if the buffered events
     * exceed the configured lookahead depth, emits the next buffered event.
     *
     * @param parameters event parameters for the group (may be null or empty)
     */
    @Override
    public void beginGroup(Map<String, String> parameters)
    {
        this.previousEvents.beginGroup(parameters);
        firePreviousEvent();
    }

    /**
     * Buffers a format begin event and may emit a previously buffered event if the lookahead depth is exceeded.
     *
     * @param format the format being started
     * @param parameters optional parameters for the format
     */
    @Override
    public void beginFormat(Format format, Map<String, String> parameters)
    {
        this.previousEvents.beginFormat(format, parameters);
        firePreviousEvent();
    }

    /**
     * Buffer the start of a header event with the specified level, identifier, and parameters.
     *
     * @param level the header level
     * @param id an optional header identifier, or {@code null} if none
     * @param parameters additional parameters for the header (may be empty)
     */
    @Override
    public void beginHeader(HeaderLevel level, String id, Map<String, String> parameters)
    {
        this.previousEvents.beginHeader(level, id, parameters);
        firePreviousEvent();
    }

    /**
     * Buffers a begin-link event and emits the next buffered event when the lookahead depth is exceeded.
     *
     * @param reference the target resource reference for the link
     * @param freestanding whether the link is freestanding (not part of surrounding text)
     * @param parameters additional parameters for the link event
     * @since 2.5RC1
     */
    @Override
    public void beginLink(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        this.previousEvents.beginLink(reference, freestanding, parameters);
        firePreviousEvent();
    }

    /**
     * Buffers a begin-list event and, if the internal buffer exceeds the configured lookahead depth, emits the next buffered event.
     *
     * @param type the list type (e.g., ordered or unordered)
     * @param parameters optional rendering parameters for the list
     */
    @Override
    public void beginList(ListType type, Map<String, String> parameters)
    {
        this.previousEvents.beginList(type, parameters);
        firePreviousEvent();
    }

    /**
     * Buffers a "begin list item" event and, if the buffer has exceeded the configured lookahead depth,
     * forwards the next buffered event to the downstream listener.
     */
    @Override
    public void beginListItem()
    {
        this.previousEvents.beginListItem();
        firePreviousEvent();
    }

    /**
     * Buffers the start of a list item event and, if the buffer has exceeded the lookahead depth, emits the next buffered event.
     *
     * @param parameters optional attributes for the list item (may be null)
     */
    @Override
    public void beginListItem(Map<String, String> parameters)
    {
        this.previousEvents.beginListItem(parameters);
        firePreviousEvent();
    }

    /**
     * Buffer the start of a macro marker event and defer emitting it until the lookahead policy allows.
     *
     * @param name the macro's name
     * @param parameters macro parameters; may be null or empty
     * @param content the macro content (may be null for empty content)
     * @param isInline true if the macro is inline, false if it is block-level
     */
    @Override
    public void beginMacroMarker(String name, Map<String, String> parameters, String content, boolean isInline)
    {
        this.previousEvents.beginMacroMarker(name, parameters, content, isInline);
        firePreviousEvent();
    }

    /**
     * {@inheritDoc}
     *
     * @since 3.0M2
     */
    @Override
    public void beginMetaData(MetaData metadata)
    {
        this.previousEvents.beginMetaData(metadata);
        firePreviousEvent();
    }

    /**
     * Buffer a paragraph-begin event and, if the buffer has exceeded the configured lookahead depth,
     * emit the next buffered event to the downstream listener.
     *
     * @param parameters paragraph parameters (attributes) or null if none
     */
    @Override
    public void beginParagraph(Map<String, String> parameters)
    {
        this.previousEvents.beginParagraph(parameters);
        firePreviousEvent();
    }

    /**
     * Buffer a quotation-begin event with the given parameters and, if the lookahead policy permits,
     * emit the next buffered event.
     *
     * @param parameters attributes for the quotation block (may be empty or null)
     */
    @Override
    public void beginQuotation(Map<String, String> parameters)
    {
        this.previousEvents.beginQuotation(parameters);
        firePreviousEvent();
    }

    /**
     * Buffers a begin-quotation-line event and, if the buffer exceeds the configured lookahead depth,
     * emits the next buffered event to the downstream listener.
     */
    @Override
    public void beginQuotationLine()
    {
        this.previousEvents.beginQuotationLine();
        firePreviousEvent();
    }

    /**
     * Buffers a section-begin event and, if the configured lookahead depth is exceeded, emits the next buffered event.
     *
     * @param parameters optional section parameters (attribute name → value), or {@code null} if none
     */
    @Override
    public void beginSection(Map<String, String> parameters)
    {
        this.previousEvents.beginSection(parameters);
        firePreviousEvent();
    }

    /**
     * Buffers a table-begin event with the given parameters and may emit a previously buffered event
     * if the lookahead threshold is exceeded.
     *
     * @param parameters table parameters/attributes (may be null)
     */
    @Override
    public void beginTable(Map<String, String> parameters)
    {
        this.previousEvents.beginTable(parameters);
        firePreviousEvent();
    }

    /**
     * Buffer a table cell begin event with the provided parameters and emit any previously buffered events
     * that exceed the configured lookahead depth.
     *
     * @param parameters mapping of table cell parameter names to values (may be null or empty)
     */
    @Override
    public void beginTableCell(Map<String, String> parameters)
    {
        this.previousEvents.beginTableCell(parameters);
        firePreviousEvent();
    }

    /**
     * Buffers a "begin table header cell" event with the given parameters and, if the buffer has exceeded
     * the configured lookahead depth, emits the next buffered event to the downstream listener.
     *
     * @param parameters the attributes for the table header cell (may be null or empty)
     */
    @Override
    public void beginTableHeadCell(Map<String, String> parameters)
    {
        this.previousEvents.beginTableHeadCell(parameters);
        firePreviousEvent();
    }

    /**
     * Buffers a table-row begin event with the given parameters and, if the internal buffer exceeds
     * the configured lookahead depth, emits the next previously buffered event.
     *
     * @param parameters attributes for the table row (may be {@code null})
     */
    @Override
    public void beginTableRow(Map<String, String> parameters)
    {
        this.previousEvents.beginTableRow(parameters);
        firePreviousEvent();
    }

    /**
     * Records a begin-figure event with the given parameters into the listener's buffer.
     *
     * @param parameters mapping of figure attributes (e.g., width, height, class, id) to their values
     */
    @Override
    public void beginFigure(Map<String, String> parameters)
    {
        this.previousEvents.beginFigure(parameters);
        firePreviousEvent();
    }

    /**
     * Buffers the start of a figure caption event and, if the buffered events exceed the configured lookahead depth,
     * emits the next previously buffered event to the downstream listener.
     *
     * @param parameters parameters associated with the figure caption event
     */
    @Override
    public void beginFigureCaption(Map<String, String> parameters)
    {
        this.previousEvents.beginFigureCaption(parameters);
        firePreviousEvent();
    }

    /**
     * Buffers an end-definition-description event and, if the buffer size exceeds the configured lookahead depth, emits the next buffered event to the downstream listener.
     */
    @Override
    public void endDefinitionDescription()
    {
        this.previousEvents.endDefinitionDescription();
        firePreviousEvent();
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.0RC1
     */
    @Override
    public void endDefinitionList(Map<String, String> parameters)
    {
        this.previousEvents.endDefinitionList(parameters);
        firePreviousEvent();
    }

    /**
     * Buffers an end-of-definition-term event and, if the buffer has exceeded the configured lookahead
     * depth, emits the next buffered event to the downstream listener.
     */
    @Override
    public void endDefinitionTerm()
    {
        this.previousEvents.endDefinitionTerm();
        firePreviousEvent();
    }

    /**
     * {@inheritDoc}
     *
     * @since 3.0M2
     */
    @Override
    public void endDocument(MetaData metadata)
    {
        this.previousEvents.endDocument(metadata);
        flush();
    }

    /**
     * Buffers an end-of-group event and triggers emission of previously buffered events when the
     * configured lookahead depth allows.
     *
     * @param parameters event parameters (attributes) associated with the end-of-group event
     */
    @Override
    public void endGroup(Map<String, String> parameters)
    {
        this.previousEvents.endGroup(parameters);
        firePreviousEvent();
    }

    /**
     * Buffer the end of a formatting span and, if the lookahead threshold is reached, emit the next buffered event.
     *
     * @param format the format being ended
     * @param parameters additional parameters associated with the format end event
     */
    @Override
    public void endFormat(Format format, Map<String, String> parameters)
    {
        this.previousEvents.endFormat(format, parameters);
        firePreviousEvent();
    }

    /**
     * Buffers an end-of-header event and, if the number of buffered events exceeds the configured lookahead depth,
     * emits the next buffered event to the downstream listener.
     *
     * @param level the header level being closed
     * @param id the identifier of the header, or {@code null} if none
     * @param parameters additional parameters associated with the header end event
     */
    @Override
    public void endHeader(HeaderLevel level, String id, Map<String, String> parameters)
    {
        this.previousEvents.endHeader(level, id, parameters);
        firePreviousEvent();
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.5RC1
     */
    @Override
    public void endLink(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        this.previousEvents.endLink(reference, freestanding, parameters);
        firePreviousEvent();
    }

    /**
     * Buffers an end-of-list event and, if the configured lookahead depth has been exceeded, emits the next buffered event.
     *
     * @param type the type of list ending
     * @param parameters optional parameters associated with the list event
     */
    @Override
    public void endList(ListType type, Map<String, String> parameters)
    {
        this.previousEvents.endList(type, parameters);
        firePreviousEvent();
    }

    /**
     * Buffers an end-of-list-item event and then attempts to emit a previously buffered event if lookahead limits permit.
     */
    @Override
    public void endListItem()
    {
        this.previousEvents.endListItem();
        firePreviousEvent();
    }

    /**
     * Buffers an "end list item" event (with its parameters) and, if the buffer exceeds the configured
     * lookahead depth, emits the next buffered event to the downstream listener.
     *
     * @param parameters parameters associated with the end of the list item
     */
    @Override
    public void endListItem(Map<String, String> parameters)
    {
        this.previousEvents.endListItem(parameters);
        firePreviousEvent();
    }

    /**
     * Buffers an end-of-macro-marker event and conditionally forwards a previously buffered event when the
     * configured lookahead depth is exceeded.
     *
     * @param name the macro marker name
     * @param parameters parameters associated with the macro marker
     * @param content the macro content
     * @param isInline true if the macro marker is inline, false if it is block-level
     */
    @Override
    public void endMacroMarker(String name, Map<String, String> parameters, String content, boolean isInline)
    {
        this.previousEvents.endMacroMarker(name, parameters, content, isInline);
        firePreviousEvent();
    }

    /**
     * Buffers the end-of-metadata event and, if the buffer has exceeded the configured lookahead depth,
     * emits the next buffered event to the downstream listener.
     *
     * @param metadata the metadata associated with the document end
     * @since 3.0M2
     */
    @Override
    public void endMetaData(MetaData metadata)
    {
        this.previousEvents.endMetaData(metadata);
        firePreviousEvent();
    }

    /**
     * Buffers an end-of-paragraph event and, if the buffer has exceeded the configured lookahead depth,
     * emits the next previously buffered event to the downstream listener.
     *
     * @param parameters the paragraph's parameters, if any
     */
    @Override
    public void endParagraph(Map<String, String> parameters)
    {
        this.previousEvents.endParagraph(parameters);
        firePreviousEvent();
    }

    /**
     * Buffer an end-quotation event with the given parameters and, if buffered events exceed the configured lookahead depth, emit the next buffered event to the downstream listener.
     *
     * @param parameters parameters associated with the quotation end event
     */
    @Override
    public void endQuotation(Map<String, String> parameters)
    {
        this.previousEvents.endQuotation(parameters);
        firePreviousEvent();
    }

    /**
     * Buffers an end-of-quotation-line event and, when the buffer exceeds the configured lookahead depth,
     * emits the next previously buffered event to the downstream listener.
     */
    @Override
    public void endQuotationLine()
    {
        this.previousEvents.endQuotationLine();
        firePreviousEvent();
    }

    /**
     * Buffers an end-of-section event and triggers emission of any previously buffered event that exceeds the configured lookahead depth.
     *
     * @param parameters event parameters associated with the end of the section, or null/empty if none
     */
    @Override
    public void endSection(Map<String, String> parameters)
    {
        this.previousEvents.endSection(parameters);
        firePreviousEvent();
    }

    /**
     * Buffers an end-of-table event and, if the buffer exceeds the configured lookahead depth, emits the next buffered event.
     *
     * @param parameters attributes associated with the table end event (may be empty)
     */
    @Override
    public void endTable(Map<String, String> parameters)
    {
        this.previousEvents.endTable(parameters);
        firePreviousEvent();
    }

    /**
     * Buffers an end-of-table-cell event and, if the buffer has exceeded the configured lookahead depth,
     * emits the next buffered event to the downstream listener.
     *
     * @param parameters the attributes associated with the table cell end event (may be empty or null)
     */
    @Override
    public void endTableCell(Map<String, String> parameters)
    {
        this.previousEvents.endTableCell(parameters);
        firePreviousEvent();
    }

    /**
     * Buffers the end-of-table-header-cell event and, if the internal buffer exceeds the configured
     * lookahead depth, emits the next buffered event to the downstream listener.
     *
     * @param parameters parameters associated with the table head cell
     */
    @Override
    public void endTableHeadCell(Map<String, String> parameters)
    {
        this.previousEvents.endTableHeadCell(parameters);
        firePreviousEvent();
    }

    /**
     * Buffers an end-of-table-row event with the given parameters and, if the lookahead depth is exceeded,
     * emits the next buffered event to the downstream listener.
     *
     * @param parameters metadata attributes for the table row (may be {@code null})
     */
    @Override
    public void endTableRow(Map<String, String> parameters)
    {
        this.previousEvents.endTableRow(parameters);
        firePreviousEvent();
    }

    /**
     * Buffers an end-of-figure event and, if the internal buffer has exceeded the configured lookahead depth,
     * emits the next buffered event to the downstream listener.
     *
     * @param parameters event parameters (attributes) associated with the end-of-figure event, may be null or empty
     */
    @Override
    public void endFigure(Map<String, String> parameters)
    {
        this.previousEvents.endFigure(parameters);
        firePreviousEvent();
    }

    /**
     * Buffers an end-of-figure-caption event and, if the buffer size exceeds the configured lookahead depth,
     * emits the next buffered event to the downstream listener.
     *
     * @param parameters the parameters associated with the figure caption event
     */
    @Override
    public void endFigureCaption(Map<String, String> parameters)
    {
        this.previousEvents.endFigureCaption(parameters);
        firePreviousEvent();
    }

    /**
     * Buffers a raw-text event with its syntax and, if the lookahead threshold is exceeded, emits the next buffered event.
     *
     * @param text   the raw text content
     * @param syntax the syntax associated with the text
     */
    @Override
    public void onRawText(String text, Syntax syntax)
    {
        this.previousEvents.onRawText(text, syntax);
        firePreviousEvent();
    }

    /**
     * Buffers an empty-lines event with the given count and may emit previously buffered events when the lookahead depth is exceeded.
     *
     * @param count the number of consecutive empty lines
     */
    @Override
    public void onEmptyLines(int count)
    {
        this.previousEvents.onEmptyLines(count);
        firePreviousEvent();
    }

    /**
     * Buffers a horizontal-line event with the given parameters and, if the buffer has exceeded
     * the configured lookahead depth, emits the oldest buffered event.
     *
     * @param parameters arbitrary attributes for the horizontal line event (may be null)
     */
    @Override
    public void onHorizontalLine(Map<String, String> parameters)
    {
        this.previousEvents.onHorizontalLine(parameters);
        firePreviousEvent();
    }

    /**
     * Buffers an identifier event and, if the buffered event count exceeds the configured lookahead depth,
     * emits the next pending event to the downstream listener.
     *
     * @param name the identifier name
     */
    @Override
    public void onId(String name)
    {
        this.previousEvents.onId(name);
        firePreviousEvent();
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.5RC1
     */
    @Override
    public void onImage(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        this.previousEvents.onImage(reference, freestanding, parameters);
        firePreviousEvent();
    }

    /**
     * Buffers an image event (with optional id) and, if the buffer exceeds the configured lookahead depth,
     * emits the next buffered event.
     *
     * @param reference   the image resource reference
     * @param freestanding whether the image is freestanding (not inline)
     * @param id          optional image identifier, or {@code null} if none
     * @param parameters  additional parameters for the image
     */
    @Override
    public void onImage(ResourceReference reference, boolean freestanding, String id, Map<String, String> parameters)
    {
        this.previousEvents.onImage(reference, freestanding, id, parameters);
        firePreviousEvent();
    }

    /**
     * Buffers a macro event and triggers emission of a previously buffered event if the lookahead threshold is exceeded.
     *
     * @param id the macro identifier
     * @param parameters macro parameters, may be empty
     * @param content the macro content, or {@code null} if none
     * @param inline {@code true} for an inline macro, {@code false} for a block-level macro
     */
    @Override
    public void onMacro(String id, Map<String, String> parameters, String content, boolean inline)
    {
        this.previousEvents.onMacro(id, parameters, content, inline);
        firePreviousEvent();
    }

    /**
     * Buffers a newline event and, if the buffered events exceed the configured lookahead depth, emits the next buffered event.
     */
    @Override
    public void onNewLine()
    {
        this.previousEvents.onNewLine();
        firePreviousEvent();
    }

    /**
     * Buffers a space event and emits the next buffered event when the configured lookahead depth is exceeded.
     *
     * Buffers the occurrence of a space; if buffering increases the stored events beyond the lookaheadDepth, the
     * earliest buffered event is forwarded to the next listener in the chain.
     */
    @Override
    public void onSpace()
    {
        this.previousEvents.onSpace();
        firePreviousEvent();
    }

    /**
     * Buffers a special symbol event and triggers emission of buffered events when the lookahead depth is exceeded.
     *
     * @param symbol the special symbol character to buffer
     */
    @Override
    public void onSpecialSymbol(char symbol)
    {
        this.previousEvents.onSpecialSymbol(symbol);
        firePreviousEvent();
    }

    /**
     * Buffers a verbatim event (code or preformatted text) and, if the configured lookahead depth is exceeded,
     * emits the next buffered event to the downstream listener.
     *
     * @param content the verbatim content
     * @param inline  true for inline verbatim, false for block verbatim
     * @param parameters additional parameters associated with the event
     */
    @Override
    public void onVerbatim(String content, boolean inline, Map<String, String> parameters)
    {
        this.previousEvents.onVerbatim(content, inline, parameters);
        firePreviousEvent();
    }

    /**
     * Buffers a word event and triggers emission of buffered events that have exceeded the configured lookahead depth.
     *
     * @param word the word text to buffer
     */
    @Override
    public void onWord(String word)
    {
        this.previousEvents.onWord(word);
        firePreviousEvent();
    }

    /**
     * Emit the next buffered event when the number of buffered events exceeds the configured lookahead depth.
     */
    private void firePreviousEvent()
    {
        if (this.previousEvents.size() > this.lookaheadDepth) {
            fireEvent();
        }
    }

    /**
     * Flushes the internal event buffer by emitting all buffered events to the next listener in the chain.
     */
    private void flush()
    {
        // Ensure that all remaining events are flushed
        while (!this.previousEvents.isEmpty()) {
            fireEvent();
        }
    }

    /**
     * Removes the next buffered event and forwards it to the next listener in the chain using the event's stored
     * type and parameters.
     */
    private void fireEvent()
    {
        Event event = this.previousEvents.remove();
        event.eventType.fireEvent(getListenerChain().getNextListener(getClass()), event.eventParameters);
    }

    /**
     * Prepends all events from the given queue into this listener's internal buffer, preserving their original order.
     *
     * @param eventsToTransfer the queue whose events will be moved and emptied
     * @since 10.5RC1
     */
    public void transferStart(QueueListener eventsToTransfer)
    {
        while (!eventsToTransfer.isEmpty()) {
            Event event = eventsToTransfer.removeLast();
            this.previousEvents.offerFirst(event);
        }
    }
}
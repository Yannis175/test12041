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
import java.util.Deque;
import java.util.Map;

import org.xwiki.rendering.listener.Format;
import org.xwiki.rendering.listener.HeaderLevel;
import org.xwiki.rendering.listener.ListType;
import org.xwiki.rendering.listener.MetaData;
import org.xwiki.rendering.listener.reference.ResourceReference;
import org.xwiki.rendering.syntax.Syntax;
import org.xwiki.stability.Unstable;

/**
 * Indicates block element for which we are inside and previous blocks.
 *
 * @version $Id$
 * @since 1.8RC1
 */
public class BlockStateChainingListener extends AbstractChainingListener implements StackableChainingListener
{
    public enum Event
    {
        NONE,
        DEFINITION_DESCRIPTION,
        DEFINITION_TERM,
        DEFINITION_LIST,
        DOCUMENT,
        FORMAT,
        HEADER,
        LINK,
        LIST,
        LIST_ITEM,
        MACRO_MARKER,
        PARAGRAPH,
        QUOTATION,
        QUOTATION_LINE,
        SECTION,
        TABLE,
        TABLE_CELL,
        TABLE_HEAD_CELL,
        TABLE_ROW,
        RAW_TEXT,
        EMPTY_LINES,
        HORIZONTAL_LINE,
        ID,
        IMAGE,
        NEW_LINE,
        SPACE,
        SPECIAL_SYMBOL,
        MACRO,
        VERBATIM_INLINE,
        VERBATIM_STANDALONE,
        WORD,
        FIGURE,
        FIGURE_CAPTION,
        META_DATA,
        GROUP
    }

    private Event previousEvent = Event.NONE;

    private final Deque<Event> eventStack = new ArrayDeque<>();

    private int inlineDepth;

    private boolean isInParagraph;

    private boolean isInHeader;

    private int linkDepth;

    private boolean isInTable;

    private boolean isInTableCell;

    private Deque<DefinitionListState> definitionListDepth = new ArrayDeque<>();

    private Deque<ListState> listDepth = new ArrayDeque<>();

    private int quotationDepth;

    private int quotationLineDepth;

    private int quotationLineIndex = -1;

    private int macroDepth;

    private int cellRow = -1;

    private int cellCol = -1;

    /**
     * Create a BlockStateChainingListener bound to the provided listener chain.
     *
     * @param listenerChain the listener chain this instance will delegate to and be bound with
     */
    public BlockStateChainingListener(ListenerChain listenerChain)
    {
        setListenerChain(listenerChain);
    }

    /**
     * Create a new BlockStateChainingListener bound to the same listener chain.
     *
     * @return a new {@link StackableChainingListener} that tracks block state and uses the same listener chain
     */
    @Override
    public StackableChainingListener createChainingListenerInstance()
    {
        return new BlockStateChainingListener(getListenerChain());
    }

    /**
     * Get the most recently completed block event.
     *
     * @return the last completed {@link Event}, or {@link Event#NONE} if no event has completed yet.
     */
    public Event getPreviousEvent()
    {
        return this.previousEvent;
    }

    /**
     * Get the current inline nesting depth.
     *
     * @return the number of nested inline-level contexts (0 when not inside any inline context)
     */
    public int getInlineDepth()
    {
        return this.inlineDepth;
    }

    /**
     * Indicates whether the current position is inside inline content.
     *
     * @return `true` if the current inline nesting depth is greater than zero, `false` otherwise.
     */
    public boolean isInLine()
    {
        return getInlineDepth() > 0;
    }

    /**
     * Indicates whether the listener is currently inside a paragraph.
     *
     * @return {@code true} if the current position is inside a paragraph, {@code false} otherwise.
     */
    public boolean isInParagraph()
    {
        return this.isInParagraph;
    }

    /**
     * Indicates whether the current position is inside a header.
     *
     * @return `true` if currently inside a header, `false` otherwise.
     */
    public boolean isInHeader()
    {
        return this.isInHeader;
    }

    /**
     * Indicates whether the listener is currently inside a table.
     *
     * @return `true` if the current position is inside a table, `false` otherwise.
     */
    public boolean isInTable()
    {
        return this.isInTable;
    }

    /**
     * Indicates whether the current listener context is inside a table cell.
     *
     * @return `true` if currently inside a table cell, `false` otherwise
     */
    public boolean isInTableCell()
    {
        return this.isInTableCell;
    }

    /**
     * Gets the current table cell column index, or -1 if not inside a table cell.
     *
     * @return the current table cell column index, or -1 when no cell is active
     */
    public int getCellCol()
    {
        return this.cellCol;
    }

    /**
     * Current table cell row index.
     *
     * @return the current table cell row index, or -1 when not inside any table row
     */
    public int getCellRow()
    {
        return this.cellRow;
    }

    /**
     * Retrieve the current nesting depth of definition lists.
     *
     * @return the number of nested definition lists; 0 when not inside any definition list
     */
    public int getDefinitionListDepth()
    {
        return this.definitionListDepth.size();
    }

    /**
     * Determines whether the current position is inside a definition list.
     *
     * @return {@code true} if currently inside one or more nested definition lists, {@code false} otherwise.
     */
    public boolean isInDefinitionList()
    {
        return getDefinitionListDepth() > 0;
    }

    /**
     * The zero-based index of the current item in the innermost definition list, or -1 when not inside a definition list.
     *
     * @return the current definition list item index, or -1 if not in a definition list
     */
    public int getDefinitionListItemIndex()
    {
        return isInDefinitionList() ? this.definitionListDepth.peek().definitionListItemIndex : -1;
    }

    /**
     * Retrieve the current nesting depth of list constructs.
     *
     * @return the number of nested lists currently open (0 when not inside any list)
     */
    public int getListDepth()
    {
        return this.listDepth.size();
    }

    /**
     * Indicates whether the current position is inside a list.
     *
     * @return `true` if inside a list, `false` otherwise
     */
    public boolean isInList()
    {
        return getListDepth() > 0;
    }

    /**
     * Retrieve the current list item index within the innermost open list.
     *
     * @return the zero-based index of the current list item, or -1 if not inside a list
     */
    public int getListItemIndex()
    {
        return isInList() ? this.listDepth.peek().listItemIndex : -1;
    }

    /**
     * Increase the current link nesting depth.
     *
     * Increments the internal counter that tracks how many link contexts are currently open; used when entering a link.
     */
    public void pushLinkDepth()
    {
        ++this.linkDepth;
    }

    /**
     * Decrements the current link nesting depth by one.
     *
     * Called when exiting a link context to update the internal nesting counter.
     */
    public void popLinkDepth()
    {
        --this.linkDepth;
    }

    /**
     * Reports the current nesting depth of link constructs.
     *
     * @return the number of currently open link levels, or 0 if not inside a link
     */
    public int getLinkDepth()
    {
        return this.linkDepth;
    }

    /**
     * Whether the current position is nested inside a link.
     *
     * @return `true` if the current listener is inside a link, `false` otherwise.
     */
    public boolean isInLink()
    {
        return getLinkDepth() > 0;
    }

    /**
     * Provides the current quotation nesting depth.
     *
     * @return the current quotation nesting depth; 0 when not inside a quotation
     */
    public int getQuotationDepth()
    {
        return this.quotationDepth;
    }

    /**
     * Determine whether the current position is inside a quotation block.
     *
     * @return `true` if currently inside a quotation block, `false` otherwise.
     */
    public boolean isInQuotation()
    {
        return getQuotationDepth() > 0;
    }

    /**
     * Provide the current nesting depth of quotation lines.
     *
     * @return the current quotation line depth; 0 when not inside any quotation line
     */
    public int getQuotationLineDepth()
    {
        return this.quotationLineDepth;
    }

    /**
     * Determines if the current position is inside a quotation line.
     *
     * @return `true` if inside a quotation line, `false` otherwise.
     */
    public boolean isInQuotationLine()
    {
        return getQuotationLineDepth() > 0;
    }

    /**
     * Get the zero-based index of the current quotation line within the enclosing quotation.
     *
     * @return the zero-based index of the current quotation line, or -1 if not inside a quotation line
     */
    public int getQuotationLineIndex()
    {
        return this.quotationLineIndex;
    }

    /**
     * Provide the current macro nesting depth.
     *
     * @return the current macro nesting depth (0 when not inside any macro)
     */
    public int getMacroDepth()
    {
        return this.macroDepth;
    }

    /**
     * Indicates whether the listener is currently inside a macro.
     *
     * @return `true` if inside a macro, `false` otherwise.
     */
    public boolean isInMacro()
    {
        return getMacroDepth() > 0;
    }

    /**
         * Get the event that currently encloses the active event.
         *
         * @return the enclosing {@link Event}, or {@code null} if there is no enclosing event
         * @since 14.0RC1
         */
    @Unstable
    public Event getParentEvent()
    {
        return this.eventStack.peek();
    }

    // Events

    /**
     * {@inheritDoc}
     *
     * @since 14.0RC1
     */
    @Override
    public void beginDocument(MetaData metadata)
    {
        super.beginDocument(metadata);

        this.eventStack.push(Event.DOCUMENT);
    }

    /**
     * Signals the start of a definition description block.
     *
     * Increments the inline nesting depth, advances the current definition-list item index,
     * delegates the event to the chained listener, and records `DEFINITION_DESCRIPTION` on the internal event stack.
     */
    @Override
    public void beginDefinitionDescription()
    {
        ++this.inlineDepth;
        ++this.definitionListDepth.peek().definitionListItemIndex;

        super.beginDefinitionDescription();

        this.eventStack.push(Event.DEFINITION_DESCRIPTION);
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.0RC1
     */
    @Override
    public void beginDefinitionList(Map<String, String> parameters)
    {
        this.definitionListDepth.push(new DefinitionListState());

        super.beginDefinitionList(parameters);

        this.eventStack.push(Event.DEFINITION_LIST);
    }

    /**
     * Begins a definition term block, updating the block state to reflect entering a term.
     *
     * <p>Increments the inline nesting depth, advances the current definition-list item index, and records
     * {@link Event#DEFINITION_TERM} as the active enclosing event.
     */
    @Override
    public void beginDefinitionTerm()
    {
        ++this.inlineDepth;
        ++this.definitionListDepth.peek().definitionListItemIndex;

        super.beginDefinitionTerm();

        this.eventStack.push(Event.DEFINITION_TERM);
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.5RC1
     */
    @Override
    public void beginLink(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        ++this.linkDepth;

        super.beginLink(reference, freestanding, parameters);

        this.eventStack.push(Event.LINK);
    }

    /**
     * Begins a list block of the given type and records the list in the listener's nesting state.
     *
     * @param type       the kind of list to begin (e.g., ordered or unordered)
     * @param parameters optional rendering parameters for the list
     */
    @Override
    public void beginList(ListType type, Map<String, String> parameters)
    {
        this.listDepth.push(new ListState());

        super.beginList(type, parameters);

        this.eventStack.push(Event.LIST);
    }

    /**
     * Begin a new list item: mark entry into an inline context, advance the current list's item index,
     * delegate to the chained listener, and record the LIST_ITEM event on the internal event stack.
     */
    @Override
    public void beginListItem()
    {
        ++this.inlineDepth;
        ++this.listDepth.peek().listItemIndex;

        super.beginListItem();

        this.eventStack.push(Event.LIST_ITEM);
    }

    /**
     * Begins a list item and updates the listener's internal block state accordingly.
     *
     * <p>Increments the current inline nesting and advances the current list's item index, then records that a
     * list-item block is now active.</p>
     *
     * @param parameters optional parameters for the list item (may be null or empty)
     */
    @Override
    public void beginListItem(Map<String, String> parameters)
    {
        ++this.inlineDepth;
        ++this.listDepth.peek().listItemIndex;

        super.beginListItem(parameters);

        this.eventStack.push(Event.LIST_ITEM);
    }

    /**
     * Begins a macro marker event, increments the macro nesting depth, and records the MACRO_MARKER event on the event stack.
     *
     * @param name the macro name
     * @param parameters the macro parameters (name → value)
     * @param content the macro content, or {@code null} if none
     * @param isInline {@code true} if the macro is inline, {@code false} if standalone
     */
    @Override
    public void beginMacroMarker(String name, Map<String, String> parameters, String content, boolean isInline)
    {
        ++this.macroDepth;

        super.beginMacroMarker(name, parameters, content, isInline);

        this.eventStack.push(Event.MACRO_MARKER);
    }

    /**
     * Begins a metadata block by delegating to the chained listener and recording a META_DATA event on the internal stack.
     *
     * @param metadata the metadata object describing the block being opened
     * @since 14.0RC1
     */
    @Override
    public void beginMetaData(MetaData metadata)
    {
        super.beginMetaData(metadata);

        this.eventStack.push(Event.META_DATA);
    }

    /**
     * {@inheritDoc}
     *
     * @since 14.0RC1
     */
    @Override
    public void beginGroup(Map<String, String> parameters)
    {
        super.beginGroup(parameters);

        this.eventStack.push(Event.GROUP);
    }

    /**
     * Signals the start of a paragraph and updates the listener's nesting and paragraph state.
     *
     * @param parameters paragraph parameters and attributes (may be null)
     */
    @Override
    public void beginParagraph(Map<String, String> parameters)
    {
        this.isInParagraph = true;
        ++this.inlineDepth;

        super.beginParagraph(parameters);

        this.eventStack.push(Event.PARAGRAPH);
    }

    /**
     * Begins a quotation block and updates the listener's quotation state.
     *
     * @param parameters the rendering parameters for the quotation, or {@code null} if none
     */
    @Override
    public void beginQuotation(Map<String, String> parameters)
    {
        ++this.quotationDepth;

        super.beginQuotation(parameters);

        this.eventStack.push(Event.QUOTATION);
    }

    /**
     * Begin a quotation line context.
     *
     * <p>Increments the quotation-line and inline depth counters, advances the quotation line index,
     * delegates the event to the chained listener, and records `QUOTATION_LINE` on the internal event stack.
     */
    @Override
    public void beginQuotationLine()
    {
        ++this.quotationLineDepth;
        ++this.inlineDepth;
        ++this.quotationLineIndex;

        super.beginQuotationLine();

        this.eventStack.push(Event.QUOTATION_LINE);
    }

    /**
     * Signals the start of a header block and updates the listener's block state.
     *
     * @param level the header level
     * @param id the header identifier, or {@code null} if none
     * @param parameters additional named parameters for the header
     */
    @Override
    public void beginHeader(HeaderLevel level, String id, Map<String, String> parameters)
    {
        this.isInHeader = true;
        ++this.inlineDepth;

        super.beginHeader(level, id, parameters);

        this.eventStack.push(Event.HEADER);
    }

    /**
     * Begin a table block, mark the listener as being inside a table, and record the table as the current enclosing event.
     *
     * @param parameters attributes or parameters associated with the table block (may be {@code null})
     */
    @Override
    public void beginTable(Map<String, String> parameters)
    {
        this.isInTable = true;

        super.beginTable(parameters);

        this.eventStack.push(Event.TABLE);
    }

    /**
     * Signals the start of a table row, increments the current table-row index, and records a TABLE_ROW event on the internal event stack.
     *
     * @param parameters attributes for the table row (may be {@code null})
     */
    @Override
    public void beginTableRow(Map<String, String> parameters)
    {
        ++this.cellRow;

        super.beginTableRow(parameters);

        this.eventStack.push(Event.TABLE_ROW);
    }

    /**
     * Signals the start of a table cell, updating the listener's table/cell state and forwarding the event to the chained listeners.
     *
     * @param parameters optional cell parameters/attributes (may be null or empty)
     */
    @Override
    public void beginTableCell(Map<String, String> parameters)
    {
        this.isInTableCell = true;
        ++this.inlineDepth;
        ++this.cellCol;

        super.beginTableCell(parameters);

        this.eventStack.push(Event.TABLE_CELL);
    }

    /**
     * Enter a table header cell context.
     *
     * <p>Updates the internal state to reflect that a table header cell has been entered, delegates the event to the
     * chained listener, and records the TABLE_HEAD_CELL event on the internal event stack.</p>
     *
     * @param parameters optional parameters/attributes associated with the header cell
     */
    @Override
    public void beginTableHeadCell(Map<String, String> parameters)
    {
        this.isInTableCell = true;
        ++this.inlineDepth;
        ++this.cellCol;

        super.beginTableHeadCell(parameters);

        this.eventStack.push(Event.TABLE_HEAD_CELL);
    }

    /**
     * Removes an event from the stack if it matches the passed event.
     *
     * @param event The event to remove.
     * @since 14.0RC1
     */
    private void removeEventFromStack(Event event)
    {
        if (this.eventStack.peek() == event) {
            this.eventStack.pop();
        }
    }

    /**
     * Mark the end of the current definition description block and update the listener's block state.
     *
     * <p>Removes the corresponding event from the event stack, delegates the end event to the chained listener,
     * decrements the inline nesting depth, and records this event as the most recently completed event.
     */
    @Override
    public void endDefinitionDescription()
    {
        removeEventFromStack(Event.DEFINITION_DESCRIPTION);

        super.endDefinitionDescription();

        --this.inlineDepth;
        this.previousEvent = Event.DEFINITION_DESCRIPTION;
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.0RC1
     */
    @Override
    public void endDefinitionList(Map<String, String> parameters)
    {
        removeEventFromStack(Event.DEFINITION_LIST);

        super.endDefinitionList(parameters);

        this.definitionListDepth.pop();

        this.previousEvent = Event.DEFINITION_LIST;
    }

    /**
     * Ends the current definition term block and updates internal block state.
     *
     * Removes DEFINITION_TERM from the internal event stack if present, delegates the end event to the chained
     * listener, decrements the inline nesting depth, and records `DEFINITION_TERM` as the last completed event.
     */
    @Override
    public void endDefinitionTerm()
    {
        removeEventFromStack(Event.DEFINITION_TERM);

        super.endDefinitionTerm();

        --this.inlineDepth;
        this.previousEvent = Event.DEFINITION_TERM;
    }

    /**
     * {@inheritDoc}
     *
     * @since 3.0M2
     */
    @Override
    public void endDocument(MetaData metadata)
    {
        removeEventFromStack(Event.DOCUMENT);

        super.endDocument(metadata);

        this.previousEvent = Event.DOCUMENT;
    }

    /**
     * {@inheritDoc}
     *
     * @since 14.0RC1
     */
    @Override
    public void beginFormat(Format format, Map<String, String> parameters)
    {
        super.beginFormat(format, parameters);

        this.eventStack.push(Event.FORMAT);
    }

    /**
     * Ends a formatting block, removes its event from the event stack, and records it as the last completed event.
     *
     * @param format     the formatting type being ended
     * @param parameters optional parameters associated with the format (may be {@code null})
     */
    @Override
    public void endFormat(Format format, Map<String, String> parameters)
    {
        removeEventFromStack(Event.FORMAT);

        super.endFormat(format, parameters);

        this.previousEvent = Event.FORMAT;
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.5RC1
     */
    @Override
    public void endLink(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        removeEventFromStack(Event.LINK);

        super.endLink(reference, freestanding, parameters);

        --this.linkDepth;
        this.previousEvent = Event.LINK;
    }

    /**
     * Ends the current list block and updates the listener's stacking and depth state.
     *
     * @param type the type of the list being ended (e.g., ordered, unordered)
     * @param parameters additional parameters associated with the list
     */
    @Override
    public void endList(ListType type, Map<String, String> parameters)
    {
        removeEventFromStack(Event.LIST);

        super.endList(type, parameters);

        this.listDepth.pop();

        this.previousEvent = Event.LIST;
    }

    /**
     * Ends the current list item: removes the enclosing LIST_ITEM event from the internal stack,
     * delegates the event to the chained listener, decrements the inline nesting depth, and
     * records LIST_ITEM as the most recently completed event.
     */
    @Override
    public void endListItem()
    {
        removeEventFromStack(Event.LIST_ITEM);

        super.endListItem();

        --this.inlineDepth;
        this.previousEvent = Event.LIST_ITEM;
    }

    /**
     * Ends the current list item and updates the listener's internal nesting state.
     *
     * This pops the list item event, delegates the end event to the chained listener, decrements the inline
     * nesting depth, and records that the last completed event was a list item.
     *
     * @param parameters the attributes/parameters associated with the list item (may be empty)
     */
    @Override
    public void endListItem(Map<String, String> parameters)
    {
        removeEventFromStack(Event.LIST_ITEM);

        super.endListItem(parameters);

        --this.inlineDepth;
        this.previousEvent = Event.LIST_ITEM;
    }

    /**
     * Signals the end of a macro marker and updates the listener's block state.
     *
     * <p>Delegates the event to the chained listener, sets the last completed event to {@code MACRO_MARKER},
     * and decrements the macro nesting depth.</p>
     *
     * @param name the macro name
     * @param parameters the macro parameters map
     * @param content the macro content, or {@code null} if none
     * @param isInline {@code true} if the macro was inline, {@code false} if standalone
     */
    @Override
    public void endMacroMarker(String name, Map<String, String> parameters, String content, boolean isInline)
    {
        removeEventFromStack(Event.MACRO_MARKER);

        super.endMacroMarker(name, parameters, content, isInline);

        this.previousEvent = Event.MACRO_MARKER;
        --this.macroDepth;
    }

    /**
     * {@inheritDoc}
     *
     * @since 14.0RC1
     */
    @Override
    public void endMetaData(MetaData metadata)
    {
        removeEventFromStack(Event.META_DATA);

        super.endMetaData(metadata);

        this.previousEvent = Event.META_DATA;
    }

    /**
     * {@inheritDoc}
     *
     * @since 14.0RC1
     */
    @Override
    public void endGroup(Map<String, String> parameters)
    {
        removeEventFromStack(Event.GROUP);

        super.endGroup(parameters);

        this.previousEvent = Event.GROUP;
    }

    /**
     * End the current paragraph block and update the listener's block state.
     *
     * <p>Clears the paragraph context, decrements the inline depth, and records the paragraph as the most recently
     * completed event.</p>
     *
     * @param parameters the paragraph parameters (may be empty)
     */
    @Override
    public void endParagraph(Map<String, String> parameters)
    {
        removeEventFromStack(Event.PARAGRAPH);

        super.endParagraph(parameters);

        this.isInParagraph = false;
        --this.inlineDepth;
        this.previousEvent = Event.PARAGRAPH;
    }

    /**
     * Signals the end of a quotation block and updates quotation-related state.
     *
     * Decrements the quotation nesting depth; if the depth reaches zero the quotation line index is reset
     * to -1. Records `QUOTATION` as the last completed event.
     *
     * @param parameters rendering parameters for the quotation block, or null if none
     */
    @Override
    public void endQuotation(Map<String, String> parameters)
    {
        removeEventFromStack(Event.QUOTATION);

        super.endQuotation(parameters);

        --this.quotationDepth;
        if (this.quotationDepth == 0) {
            this.quotationLineIndex = -1;
        }
        this.previousEvent = Event.QUOTATION;
    }

    /**
     * Ends the current quotation line context and updates the listener state.
     *
     * Delegates the end event to the chained listener, decrements the quotation-line and inline depth
     * counters, and marks `QUOTATION_LINE` as the most recently completed event.
     */
    @Override
    public void endQuotationLine()
    {
        removeEventFromStack(Event.QUOTATION_LINE);

        super.endQuotationLine();

        --this.quotationLineDepth;
        --this.inlineDepth;
        this.previousEvent = Event.QUOTATION_LINE;
    }

    /**
     * Begin a section block and record it as the current enclosing event.
     *
     * @param parameters rendering parameters for the section
     * @since 14.0RC1
     */
    @Override
    public void beginSection(Map<String, String> parameters)
    {
        super.beginSection(parameters);

        this.eventStack.push(Event.SECTION);
    }

    /**
     * Handles the end of a section block.
     *
     * @param parameters the section's parameters (attributes), may be empty or null
     */
    @Override
    public void endSection(Map<String, String> parameters)
    {
        removeEventFromStack(Event.SECTION);

        super.endSection(parameters);

        this.previousEvent = Event.SECTION;
    }

    /**
     * Handles the end of a header block and updates the listener's block state accordingly.
     *
     * @param level the header level
     * @param id an optional identifier for the header, or {@code null} if none
     * @param parameters additional parameters associated with the header
     */
    @Override
    public void endHeader(HeaderLevel level, String id, Map<String, String> parameters)
    {
        removeEventFromStack(Event.HEADER);

        super.endHeader(level, id, parameters);

        this.isInHeader = false;
        --this.inlineDepth;
        this.previousEvent = Event.HEADER;
    }

    /**
     * Marks the end of the current table block and resets table-related listener state.
     *
     * This exits the table context, clears the current table row index, and records `Event.TABLE`
     * as the most recently completed event.
     *
     * @param parameters table parameters/attributes (may be empty or null)
     */
    @Override
    public void endTable(Map<String, String> parameters)
    {
        removeEventFromStack(Event.TABLE);

        super.endTable(parameters);

        this.isInTable = false;
        this.cellRow = -1;
        this.previousEvent = Event.TABLE;
    }

    /**
     * Marks the end of the current table cell and updates the listener's block state accordingly.
     *
     * @param parameters map of parameters associated with the table cell (may be empty or null)
     */
    @Override
    public void endTableCell(Map<String, String> parameters)
    {
        removeEventFromStack(Event.TABLE_CELL);

        super.endTableCell(parameters);

        this.isInTableCell = false;
        --this.inlineDepth;
        this.previousEvent = Event.TABLE_CELL;
    }

    /**
     * Signal the end of the current table header cell and update the listener's state.
     *
     * @param parameters the parameters (attributes) associated with the table header cell
     */
    @Override
    public void endTableHeadCell(Map<String, String> parameters)
    {
        removeEventFromStack(Event.TABLE_HEAD_CELL);

        super.endTableHeadCell(parameters);

        this.isInTableCell = false;
        --this.inlineDepth;
        this.previousEvent = Event.TABLE_HEAD_CELL;
    }

    /**
     * Ends the current table row context and updates internal state accordingly.
     *
     * Delegates the end event to the chained listener, removes the TABLE_ROW event from the internal event
     * stack, records that the last completed event is TABLE_ROW, and resets the current cell column index.
     *
     * @param parameters a map of row-level parameters (may be empty or null)
     */
    @Override
    public void endTableRow(Map<String, String> parameters)
    {
        removeEventFromStack(Event.TABLE_ROW);

        super.endTableRow(parameters);

        this.previousEvent = Event.TABLE_ROW;
        this.cellCol = -1;
    }

    /**
     * {@inheritDoc}
     *
     * @since 14.0RC1
     */
    @Override
    public void beginFigure(Map<String, String> parameters)
    {
        super.beginFigure(parameters);

        this.eventStack.push(Event.FIGURE);
    }

    /**
     * Mark the end of the current figure block and update internal listener state.
     *
     * @param parameters the parameters (attributes) associated with the figure
     */
    @Override
    public void endFigure(Map<String, String> parameters)
    {
        removeEventFromStack(Event.FIGURE);

        super.endFigure(parameters);

        this.previousEvent = Event.FIGURE;
    }

    /**
     * Begins a figure caption context in the listener state.
     *
     * @param parameters additional parameters for the figure caption
     */
    @Override
    public void beginFigureCaption(Map<String, String> parameters)
    {
        super.beginFigureCaption(parameters);

        this.eventStack.push(Event.FIGURE_CAPTION);
    }

    /**
     * Handle the end of a figure caption block.
     *
     * @param parameters the parameters (attributes) associated with the figure caption, may be empty
     */
    @Override
    public void endFigureCaption(Map<String, String> parameters)
    {
        removeEventFromStack(Event.FIGURE_CAPTION);

        super.endFigureCaption(parameters);

        this.previousEvent = Event.FIGURE_CAPTION;
    }

    /**
     * Handle a raw text event: forwards the raw text to the chained listener and records that the last completed
     * event was a raw-text token.
     *
     * @param text the raw text content
     * @param syntax the syntax associated with the raw text
     */
    @Override
    public void onRawText(String text, Syntax syntax)
    {
        super.onRawText(text, syntax);

        this.previousEvent = Event.RAW_TEXT;
    }

    /**
     * Mark that empty line(s) were emitted.
     *
     * @param count the number of consecutive empty lines
     */
    @Override
    public void onEmptyLines(int count)
    {
        super.onEmptyLines(count);

        this.previousEvent = Event.EMPTY_LINES;
    }

    /**
     * Handle a horizontal line event and record it as the most recently completed event.
     *
     * @param parameters rendering parameters for the horizontal line
     */
    @Override
    public void onHorizontalLine(Map<String, String> parameters)
    {
        super.onHorizontalLine(parameters);

        this.previousEvent = Event.HORIZONTAL_LINE;
    }

    /**
     * Called when an identifier token is encountered and records this occurrence as the last completed event.
     *
     * @param name the identifier name
     */
    @Override
    public void onId(String name)
    {
        super.onId(name);

        this.previousEvent = Event.ID;
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.5RC1
     */
    @Override
    public void onImage(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        super.onImage(reference, freestanding, parameters);

        this.previousEvent = Event.IMAGE;
    }

    /**
     * Notifies the listener chain of an image occurrence and records `IMAGE` as the last completed event.
     *
     * @param reference the image resource reference
     * @param freestanding true if the image is freestanding (block-level), false if inline
     * @param id optional image id (may be {@code null})
     * @param parameters image parameters (attributes), may be empty
     */
    @Override
    public void onImage(ResourceReference reference, boolean freestanding, String id, Map<String, String> parameters)
    {
        super.onImage(reference, freestanding, id, parameters);

        this.previousEvent = Event.IMAGE;
    }

    /**
     * Notify the listener chain of a new line and record that a new-line event was completed.
     *
     * Delegates the new-line event to the chained listener and sets the last completed event to {@link Event#NEW_LINE}.
     */
    @Override
    public void onNewLine()
    {
        super.onNewLine();

        this.previousEvent = Event.NEW_LINE;
    }

    /**
     * Record that a space token was encountered and update listener state by setting {@link Event#SPACE} as the previous event.
     */
    @Override
    public void onSpace()
    {
        super.onSpace();

        this.previousEvent = Event.SPACE;
    }

    /**
     * Handle a special symbol event and record it as the last completed event.
     *
     * @param symbol the special symbol character encountered
     */
    @Override
    public void onSpecialSymbol(char symbol)
    {
        super.onSpecialSymbol(symbol);

        this.previousEvent = Event.SPECIAL_SYMBOL;
    }

    /**
     * Handle a verbatim element (inline or standalone) during rendering.
     *
     * Records the verbatim event and updates the listener's previous event:
     * `VERBATIM_INLINE` when `inline` is true, otherwise `VERBATIM_STANDALONE`.
     *
     * @param content the verbatim text content
     * @param inline  true for inline verbatim, false for a standalone verbatim block
     * @param parameters optional parameters/attributes associated with the verbatim element
     */
    @Override
    public void onVerbatim(String content, boolean inline, Map<String, String> parameters)
    {
        super.onVerbatim(content, inline, parameters);

        if (inline) {
            this.previousEvent = Event.VERBATIM_INLINE;
        } else {
            this.previousEvent = Event.VERBATIM_STANDALONE;
        }
    }

    /**
     * Processes a word token encountered by the listener and records that a word was the last completed event.
     *
     * @param word the word token encountered
     */
    @Override
    public void onWord(String word)
    {
        super.onWord(word);

        this.previousEvent = Event.WORD;
    }

    /**
     * Handles a macro occurrence during rendering and records it as the last completed event.
     *
     * @param id the macro identifier
     * @param parameters the macro parameters mapped by name
     * @param content the macro content or null if none
     * @param inline true when the macro is inline, false when standalone
     */
    @Override
    public void onMacro(String id, Map<String, String> parameters, String content, boolean inline)
    {
        super.onMacro(id, parameters, content, inline);

        this.previousEvent = Event.MACRO;
    }

    private static class ListState
    {
        public int listItemIndex = -1;
    }

    private static class DefinitionListState
    {
        public int definitionListItemIndex = -1;
    }
}
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

/**
 * Allow knowing if a container block (a block which can have children) has children or not.
 *
 * @version $Id$
 * @since 2.0M3
 */
public class EmptyBlockChainingListener extends AbstractChainingListener
{
    private Deque<Boolean> containerBlockStates = new ArrayDeque<Boolean>();

    /**
     * Create an EmptyBlockChainingListener and attach the given listener chain.
     *
     * @param listenerChain the listener chain that will receive forwarded events
     */
    public EmptyBlockChainingListener(ListenerChain listenerChain)
    {
        setListenerChain(listenerChain);
    }

    /**
     * Indicates whether the currently open container block has no children.
     *
     * @return `true` if the current container block has no children, `false` otherwise.
     */
    public boolean isCurrentContainerBlockEmpty()
    {
        return this.containerBlockStates.peek();
    }

    // Events

    /**
     * {@inheritDoc}
     *
     * @since 3.0M2
     */
    @Override
    public void beginDocument(MetaData metadata)
    {
        startContainerBlock();
        super.beginDocument(metadata);
    }

    /**
     * Start a definition description container and record that the current enclosing container contains content.
     */
    @Override
    public void beginDefinitionDescription()
    {
        markNotEmpty();
        startContainerBlock();
        super.beginDefinitionDescription();
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.0RC1
     */
    @Override
    public void beginDefinitionList(Map<String, String> parameters)
    {
        markNotEmpty();
        startContainerBlock();
        super.beginDefinitionList(parameters);
    }

    /**
     * Handle the start of a definition term: mark the enclosing container as having content,
     * begin a new nested container block for the term, and propagate the event to the listener chain.
     */
    @Override
    public void beginDefinitionTerm()
    {
        markNotEmpty();
        startContainerBlock();
        super.beginDefinitionTerm();
    }

    /**
     * Begins a group container and marks the enclosing container as containing content.
     *
     * @param parameters the group's parameters
     */
    @Override
    public void beginGroup(Map<String, String> parameters)
    {
        markNotEmpty();
        startContainerBlock();
        super.beginGroup(parameters);
    }

    /**
     * Begins a formatting block and updates the listener's container-empty state for the new block.
     *
     * @param format the formatting to begin
     * @param parameters optional parameters for the format, or null if none
     */
    @Override
    public void beginFormat(Format format, Map<String, String> parameters)
    {
        markNotEmpty();
        startContainerBlock();
        super.beginFormat(format, parameters);
    }

    /**
     * Marks the current container as containing content and begins a new link container block.
     *
     * @since 2.5RC1
     */
    @Override
    public void beginLink(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        markNotEmpty();
        startContainerBlock();
        super.beginLink(reference, freestanding, parameters);
    }

    /**
     * Handle the start of a list: mark the current enclosing container as containing content, begin a new
     * container scope for the list, and forward the begin-list event.
     *
     * @param type the list type (e.g., ordered, unordered)
     * @param parameters event-specific parameters for the list
     */
    @Override
    public void beginList(ListType type, Map<String, String> parameters)
    {
        markNotEmpty();
        startContainerBlock();
        super.beginList(type, parameters);
    }

    /**
     * Starts a new list item container and marks the enclosing container as having content.
     *
     * Delegates the begin-list-item event to the listener chain after updating container state.
     */
    @Override
    public void beginListItem()
    {
        markNotEmpty();
        startContainerBlock();
        super.beginListItem();
    }

    /**
     * Begins a list item, marks the enclosing container as containing content, and opens a new container scope
     * for the list item's children.
     *
     * @param parameters the attributes/parameters for the list item
     */
    @Override
    public void beginListItem(Map<String, String> parameters)
    {
        markNotEmpty();
        startContainerBlock();
        super.beginListItem(parameters);
    }

    /**
     * Begins a macro marker, marks the current container as having content, and starts a new container block for the macro.
     *
     * @param name the macro name
     * @param parameters the macro parameters
     * @param content the macro content, or {@code null} if none
     * @param isInline {@code true} if the macro is inline, {@code false} if it is block-level
     */
    @Override
    public void beginMacroMarker(String name, Map<String, String> parameters, String content, boolean isInline)
    {
        markNotEmpty();
        startContainerBlock();
        super.beginMacroMarker(name, parameters, content, isInline);
    }

    /**
     * Begins a paragraph container and marks the enclosing container as non-empty.
     *
     * @param parameters paragraph parameters (attributes); may be empty or null
     */
    @Override
    public void beginParagraph(Map<String, String> parameters)
    {
        markNotEmpty();
        startContainerBlock();
        super.beginParagraph(parameters);
    }

    /**
     * Begins a quotation block, records that the containing block has content, and starts a new container scope for the quotation.
     *
     * @param parameters additional attributes for the quotation block (may be null or empty)
     */
    @Override
    public void beginQuotation(Map<String, String> parameters)
    {
        markNotEmpty();
        startContainerBlock();
        super.beginQuotation(parameters);
    }

    /**
     * Begin a quotation line and update container-block emptiness tracking.
     *
     * Marks the current container as containing content (if applicable) and starts a new container block for the quotation line.
     */
    @Override
    public void beginQuotationLine()
    {
        markNotEmpty();
        startContainerBlock();
        super.beginQuotationLine();
    }

    /**
     * Start a header block and mark the current container as containing content.
     *
     * @param level the header level
     * @param id an optional header identifier, or {@code null} if none
     * @param parameters additional header parameters
     */
    @Override
    public void beginHeader(HeaderLevel level, String id, Map<String, String> parameters)
    {
        markNotEmpty();
        startContainerBlock();
        super.beginHeader(level, id, parameters);
    }

    /**
     * Handle the start of a table container.
     *
     * Marks the enclosing container as having content and begins a new nested container scope for the table.
     *
     * @param parameters table attributes and parameters (may be empty or null)
     */
    @Override
    public void beginTable(Map<String, String> parameters)
    {
        markNotEmpty();
        startContainerBlock();
        super.beginTable(parameters);
    }

    /**
     * Handle the start of a table row and begin tracking it as a new container block.
     *
     * @param parameters optional attributes for the table row (may be null or empty)
     */
    @Override
    public void beginTableRow(Map<String, String> parameters)
    {
        markNotEmpty();
        startContainerBlock();
        super.beginTableRow(parameters);
    }

    /**
     * Signals the start of a table cell and updates the listener's container-empty tracking.
     *
     * @param parameters optional attributes for the table cell (may be null)
     */
    @Override
    public void beginTableCell(Map<String, String> parameters)
    {
        markNotEmpty();
        startContainerBlock();
        super.beginTableCell(parameters);
    }

    /**
     * Notifies the listener chain of the start of a table header cell, marks the enclosing container as containing content,
     * and begins a new container scope for the header cell.
     *
     * @param parameters cell parameters or attributes; may be null or empty
     */
    @Override
    public void beginTableHeadCell(Map<String, String> parameters)
    {
        markNotEmpty();
        startContainerBlock();
        super.beginTableHeadCell(parameters);
    }

    /**
     * Begin a new section and start tracking whether the new section's container is empty.
     *
     * @param parameters the section's parameters (attributes), or {@code null} if none
     */
    @Override
    public void beginSection(Map<String, String> parameters)
    {
        markNotEmpty();
        startContainerBlock();
        super.beginSection(parameters);
    }

    /**
     * Begins a figure block, marks the current container as containing content, and starts a new container scope for the figure.
     *
     * @param parameters the figure's parameters/attributes (may be empty or null)
     */
    @Override
    public void beginFigure(Map<String, String> parameters)
    {
        markNotEmpty();
        startContainerBlock();
        super.beginFigure(parameters);
    }

    /**
     * Notifies the listener chain that a figure caption is starting, marks the enclosing container as containing content,
     * and begins a new container scope for the caption's content.
     *
     * @param parameters map of parameters associated with the figure caption
     */
    @Override
    public void beginFigureCaption(Map<String, String> parameters)
    {
        markNotEmpty();
        startContainerBlock();
        super.beginFigureCaption(parameters);
    }

    /**
         * Handles the end of the document and removes the current container block from the tracked stack.
         *
         * <p>Forwards the event to the chained listener and pops the top container block state.</p>
         *
         * @param metadata the document metadata
         * @since 3.0M2
         */
    @Override
    public void endDocument(MetaData metadata)
    {
        super.endDocument(metadata);
        stopContainerBlock();
    }

    /**
     * Signals the end of a definition description block and updates container block tracking.
     *
     * <p>Stops tracking the current container block so its empty/non-empty state is popped from the stack.</p>
     */
    @Override
    public void endDefinitionDescription()
    {
        super.endDefinitionDescription();
        stopContainerBlock();
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.0RC1
     */
    @Override
    public void endDefinitionList(Map<String, String> parameters)
    {
        super.endDefinitionList(parameters);
        stopContainerBlock();
    }

    /**
     * Ends the current definition term and updates the container-block emptiness state.
     */
    @Override
    public void endDefinitionTerm()
    {
        super.endDefinitionTerm();
        stopContainerBlock();
    }

    /**
     * Marks the end of the current formatted container and updates the container-empty state.
     *
     * @param format the format that is ending
     * @param parameters optional parameters associated with the format
     */
    @Override
    public void endFormat(Format format, Map<String, String> parameters)
    {
        super.endFormat(format, parameters);
        stopContainerBlock();
    }

    /**
     * Signals the end of a group container and updates the listener's container-empty tracking.
     *
     * @param parameters event parameters for the group
     */
    @Override
    public void endGroup(Map<String, String> parameters)
    {
        super.endGroup(parameters);
        stopContainerBlock();
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.5RC1
     */
    @Override
    public void endLink(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        super.endLink(reference, freestanding, parameters);
        stopContainerBlock();
    }

    /**
     * Handle the end of a list container and update the current container-block emptiness tracking.
     *
     * @param type the type of the list being ended
     * @param parameters optional parameters associated with the list
     */
    @Override
    public void endList(ListType type, Map<String, String> parameters)
    {
        super.endList(type, parameters);
        stopContainerBlock();
    }

    /**
     * Ends the current list item and stops tracking the corresponding container block's empty/non-empty state.
     */
    @Override
    public void endListItem()
    {
        super.endListItem();
        stopContainerBlock();
    }

    /**
     * Ends the current list item and updates the container-block emptiness stack to close the item's scope.
     *
     * @param parameters the rendering parameters for the list item
     */
    @Override
    public void endListItem(Map<String, String> parameters)
    {
        super.endListItem(parameters);
        stopContainerBlock();
    }
    
    /**
     * Handles the end of a macro marker container and updates the listener's container-empty tracking state.
     *
     * @param name the macro name
     * @param parameters macro parameters (may be empty)
     * @param content the macro's content, or {@code null} if none
     * @param isInline {@code true} if the macro was inline, {@code false} if it was block-level
     */
    @Override
    public void endMacroMarker(String name, Map<String, String> parameters, String content, boolean isInline)
    {
        super.endMacroMarker(name, parameters, content, isInline);
        stopContainerBlock();
    }

    /**
     * Ends the current paragraph container and updates the listener's container-empty tracking.
     *
     * @param parameters paragraph parameters
     */
    @Override
    public void endParagraph(Map<String, String> parameters)
    {
        super.endParagraph(parameters);
        stopContainerBlock();
    }

    /**
     * Signals the end of the current quotation container and updates the container-empty tracking state.
     *
     * @param parameters a map of quotation parameters (may be null)
     */
    @Override
    public void endQuotation(Map<String, String> parameters)
    {
        super.endQuotation(parameters);
        stopContainerBlock();
    }

    /**
     * Signals the end of the current quotation line and removes its container block state from the internal stack.
     */
    @Override
    public void endQuotationLine()
    {
        super.endQuotationLine();
        stopContainerBlock();
    }

    /**
     * Marks the end of the current section and removes its container-block state from the stack.
     */
    @Override
    public void endSection(Map<String, String> parameters)
    {
        super.endSection(parameters);
        stopContainerBlock();
    }

    /**
     * Signals the end of a header block and stops tracking the current container block's empty state.
     *
     * @param level the header level
     * @param id the header identifier, or {@code null} if none
     * @param parameters additional header parameters
     */
    @Override
    public void endHeader(HeaderLevel level, String id, Map<String, String> parameters)
    {
        super.endHeader(level, id, parameters);
        stopContainerBlock();
    }

    /**
     * Mark the end of the current table container block and update the container block state.
     *
     * @param parameters table parameters (may be empty or null)
     */
    @Override
    public void endTable(Map<String, String> parameters)
    {
        super.endTable(parameters);
        stopContainerBlock();
    }

    /**
     * Signals the end of the current table cell and removes its container block state from the internal stack.
     *
     * @param parameters map of table cell parameters/attributes (may be empty or null)
     */
    @Override
    public void endTableCell(Map<String, String> parameters)
    {
        super.endTableCell(parameters);
        stopContainerBlock();
    }

    /**
     * Handle the end of a table header cell and update the listener's container-block state.
     *
     * @param parameters parameters associated with the table header cell (may be empty)
     */
    @Override
    public void endTableHeadCell(Map<String, String> parameters)
    {
        super.endTableHeadCell(parameters);
        stopContainerBlock();
    }

    /**
     * Handles the end of a table row and removes the corresponding container-block state.
     *
     * @param parameters the table row parameters
     */
    @Override
    public void endTableRow(Map<String, String> parameters)
    {
        super.endTableRow(parameters);
        stopContainerBlock();
    }

    /**
     * Handles the end of a figure element and updates the current container-block emptiness tracking.
     *
     * @param parameters the parameters associated with the figure element
     */
    @Override
    public void endFigure(Map<String, String> parameters)
    {
        super.endFigure(parameters);
        stopContainerBlock();
    }

    /**
     * Signals the end of the current figure caption block and stops tracking that container's emptiness state.
     *
     * @param parameters parameters associated with the figure caption
     */
    @Override
    public void endFigureCaption(Map<String, String> parameters)
    {
        super.endFigureCaption(parameters);
        stopContainerBlock();
    }

    /**
     * Handles a raw text event and marks the current container block as having content.
     *
     * @param text the raw text content
     * @param syntax the syntax associated with the text
     */
    @Override
    public void onRawText(String text, Syntax syntax)
    {
        super.onRawText(text, syntax);
        markNotEmpty();
    }

    /**
     * Handle occurrence of consecutive empty lines within the current container block.
     *
     * @param count the number of consecutive empty lines encountered; processing these lines marks the current container block as having content
     */
    @Override
    public void onEmptyLines(int count)
    {
        super.onEmptyLines(count);
        markNotEmpty();
    }

    /**
     * Processes a horizontal line event and marks the current container block as having content.
     *
     * @param parameters a map of parameters (attributes) for the horizontal line, may be empty
     */
    @Override
    public void onHorizontalLine(Map<String, String> parameters)
    {
        super.onHorizontalLine(parameters);
        markNotEmpty();
    }

    /**
     * Handle an identifier token and mark the current container block as containing content.
     *
     * @param name the identifier name
     */
    @Override
    public void onId(String name)
    {
        super.onId(name);
        markNotEmpty();
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
        markNotEmpty();
    }

    /**
     * Handle an image with an explicit id and mark the current container block as containing content.
     *
     * @param reference   the target resource reference for the image
     * @param freestanding whether the image is freestanding (block) or inline
     * @param id          the explicit id associated with the image, or {@code null} if none
     * @param parameters  rendering parameters for the image
     */
    @Override
    public void onImage(ResourceReference reference, boolean freestanding, String id, Map<String, String> parameters)
    {
        super.onImage(reference, freestanding, id, parameters);
        markNotEmpty();
    }

    /**
     * Notifies the listener of a new-line event and records that the current container block contains content.
     */
    @Override
    public void onNewLine()
    {
        super.onNewLine();
        markNotEmpty();
    }

    /**
     * Handle a space character and mark the current container block as having content.
     */
    @Override
    public void onSpace()
    {
        super.onSpace();
        markNotEmpty();
    }

    /**
     * Notifies downstream listeners of a special symbol and marks the current container block as containing content.
     */
    @Override
    public void onSpecialSymbol(char symbol)
    {
        super.onSpecialSymbol(symbol);
        markNotEmpty();
    }

    /**
     * Handles a verbatim piece of content and marks the current container block as having content.
     *
     * @param content the verbatim text content
     * @param inline true if the verbatim content is inline, false if it is block-level
     * @param parameters additional parameters associated with the verbatim content
     */
    @Override
    public void onVerbatim(String content, boolean inline, Map<String, String> parameters)
    {
        super.onVerbatim(content, inline, parameters);
        markNotEmpty();
    }

    /**
     * Processes a word token and marks the current container block as containing content.
     *
     * @param word the word text encountered in the document
     */
    @Override
    public void onWord(String word)
    {
        super.onWord(word);
        markNotEmpty();
    }

    /**
     * Handle a macro event and record that the current container block contains content.
     *
     * @param id         the macro identifier
     * @param parameters the macro parameters, or null if none
     * @param content    the macro content, or null if none
     * @param inline     true if the macro is inline, false if block-level
     */
    @Override
    public void onMacro(String id, Map<String, String> parameters, String content, boolean inline)
    {
        super.onMacro(id, parameters, content, inline);
        markNotEmpty();
    }

    /**
     * Mark the start of a new container block by pushing an "empty" state onto the containerBlockStates stack.
     */
    private void startContainerBlock()
    {
        this.containerBlockStates.push(Boolean.TRUE);
    }

    /**
     * Ends the current container block by removing its tracked empty/non-empty state.
     *
     * <p>Removes the top value from {@code containerBlockStates}, ending tracking for the current container scope.
     */
    private void stopContainerBlock()
    {
        this.containerBlockStates.pop();
    }

    /**
     * Marks the current container block as containing content by setting the top stack value to {@code false}
     * when the stack is non-empty and the top value is currently {@code true}.
     */
    private void markNotEmpty()
    {
        if (!this.containerBlockStates.isEmpty() && this.containerBlockStates.peek()) {
            this.containerBlockStates.pop();
            this.containerBlockStates.push(Boolean.FALSE);
        }
    }

}
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.xwiki.rendering.listener.reference.ResourceReference;
import org.xwiki.rendering.syntax.Syntax;

/**
 * Send events to a list of {@link Listener}s.
 *
 * @version $Id$
 * @since 2.1M1
 */
public class CompositeListener implements Listener
{
    /**
     * The listeners.
     */
    private List<Listener> listeners = new ArrayList<Listener>();

    /**
     * Adds a listener to the composite so it will receive forwarded events.
     *
     * @param listener the listener to register
     */
    public void addListener(Listener listener)
    {
        this.listeners.add(listener);
    }

    /**
     * Retrieve the listener at the given index in the composite.
     *
     * @param i the zero-based index of the listener to return
     * @return the listener at the specified index
     */
    public Listener getListener(int i)
    {
        return this.listeners.get(i);
    }

    /**
     * Notifies all registered listeners that a definition description block has begun.
     */
    @Override
    public void beginDefinitionDescription()
    {
        for (Listener listener : this.listeners) {
            listener.beginDefinitionDescription();
        }
    }

    /**
     * Notifies all registered listeners that a definition list has started.
     *
     * @param parameters key/value parameters (attributes) associated with the definition list
     */
    @Override
    public void beginDefinitionList(Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.beginDefinitionList(parameters);
        }
    }

    /**
     * Signal the start of a definition term to all registered listeners.
     */
    @Override
    public void beginDefinitionTerm()
    {
        for (Listener listener : this.listeners) {
            listener.beginDefinitionTerm();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @since 3.0M2
     */
    @Override
    public void beginDocument(MetaData metadata)
    {
        for (Listener listener : this.listeners) {
            listener.beginDocument(metadata);
        }
    }

    /**
     * Signals the start of a formatting span to all registered listeners.
     *
     * @param format     the formatting type (e.g., bold, italic, code)
     * @param parameters additional attributes for the format element, may be empty
     */
    @Override
    public void beginFormat(Format format, Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.beginFormat(format, parameters);
        }
    }

    /**
     * Signals the start of a group element to the contained listeners.
     *
     * @param parameters rendering parameters associated with the group element
     */
    @Override
    public void beginGroup(Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.beginGroup(parameters);
        }
    }

    /**
     * Forwards a header-begin event to all registered listeners.
     *
     * @param level the header level
     * @param id the header identifier, or {@code null} if none
     * @param parameters additional header parameters as key/value pairs (may be empty)
     */
    @Override
    public void beginHeader(HeaderLevel level, String id, Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.beginHeader(level, id, parameters);
        }
    }

    /**
     * Forwards a "begin list" event to all registered listeners.
     *
     * @param type the type of the list to begin
     * @param parameters optional attributes for the list (may be {@code null} or empty)
     */
    @Override
    public void beginList(ListType type, Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.beginList(type, parameters);
        }
    }

    /**
     * Forwards a list-item begin event to all registered listeners.
     */
    @Override
    public void beginListItem()
    {
        for (Listener listener : this.listeners) {
            listener.beginListItem();
        }
    }

    /**
     * Notifies all registered listeners that a list item is beginning, providing the item's parameters.
     *
     * @param parameters a map of parameters for the list item (may be empty or null)
     */
    @Override
    public void beginListItem(Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.beginListItem(parameters);
        }
    }

    /**
     * Forwards a macro marker begin event to all registered listeners.
     *
     * @param name the macro marker name
     * @param macroParameters the macro's parameters (may be null or empty)
     * @param content the macro marker content (may be null)
     * @param isInline true if the macro marker is inline, false if block-level
     */
    @Override
    public void beginMacroMarker(String name, Map<String, String> macroParameters, String content, boolean isInline)
    {
        for (Listener listener : this.listeners) {
            listener.beginMacroMarker(name, macroParameters, content, isInline);
        }
    }

    /**
     * Signal the start of a paragraph.
     *
     * @param parameters a map of paragraph attributes (e.g. "id", "class", style or other renderer-specific parameters)
     */
    @Override
    public void beginParagraph(Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.beginParagraph(parameters);
        }
    }

    /**
     * Propagates a begin-quotation event to all registered listeners.
     *
     * @param parameters mapping of quotation parameter names to values (attributes associated with the quotation)
     */
    @Override
    public void beginQuotation(Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.beginQuotation(parameters);
        }
    }

    /**
     * Dispatches a beginQuotationLine event to all registered listeners.
     */
    @Override
    public void beginQuotationLine()
    {
        for (Listener listener : this.listeners) {
            listener.beginQuotationLine();
        }
    }

    /**
     * Signals the start of a section.
     *
     * @param parameters a map of section parameter names to their values (may be empty)
     */
    @Override
    public void beginSection(Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.beginSection(parameters);
        }
    }

    /**
     * Forwards a table-begin event to all registered listeners.
     *
     * @param parameters a map of table parameters/attributes to pass to each listener (may be null)
     */
    @Override
    public void beginTable(Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.beginTable(parameters);
        }
    }

    /**
     * Forwards a "begin table cell" event to all registered listeners.
     *
     * @param parameters a map of parameters associated with the table cell (e.g., attributes or rendering hints)
     */
    @Override
    public void beginTableCell(Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.beginTableCell(parameters);
        }
    }

    /**
     * Notifies all registered listeners that a table header cell has begun.
     *
     * @param parameters mapping of parameters/attributes for the table head cell
     */
    @Override
    public void beginTableHeadCell(Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.beginTableHeadCell(parameters);
        }
    }

    /**
     * Forwards a "begin table row" event to all registered listeners.
     *
     * @param parameters a map of parameters associated with the table row
     */
    @Override
    public void beginTableRow(Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.beginTableRow(parameters);
        }
    }

    /**
     * Signals the end of a definition description to all contained listeners.
     */
    @Override
    public void endDefinitionDescription()
    {
        for (Listener listener : this.listeners) {
            listener.endDefinitionDescription();
        }
    }

    /**
     * Signals the end of a definition list.
     *
     * @param parameters parameters associated with the definition list
     */
    @Override
    public void endDefinitionList(Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.endDefinitionList(parameters);
        }
    }

    /**
     * Forwards the end-of-definition-term event to all registered listeners.
     */
    @Override
    public void endDefinitionTerm()
    {
        for (Listener listener : this.listeners) {
            listener.endDefinitionTerm();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @since 3.0M2
     */
    @Override
    public void endDocument(MetaData metadata)
    {
        for (Listener listener : this.listeners) {
            listener.endDocument(metadata);
        }
    }

    /**
     * Dispatches an end-of-format event to all registered listeners.
     *
     * @param format the format that is ending
     * @param parameters additional attributes associated with the format
     */
    @Override
    public void endFormat(Format format, Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.endFormat(format, parameters);
        }
    }

    /**
     * Forwards the end-of-group event to all registered listeners.
     *
     * @param parameters a map of attributes associated with the group, or {@code null} if none
     */
    @Override
    public void endGroup(Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.endGroup(parameters);
        }
    }

    /**
     * Notifies all registered listeners that a header has ended.
     *
     * @param level the header level that ended
     * @param id the identifier associated with the header, or {@code null} if none
     * @param parameters additional header attributes, may be empty
     */
    @Override
    public void endHeader(HeaderLevel level, String id, Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.endHeader(level, id, parameters);
        }
    }

    /**
     * Notifies all registered listeners that a list has ended.
     *
     * @param type the type of the list that ended
     * @param parameters additional parameters associated with the list
     */
    @Override
    public void endList(ListType type, Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.endList(type, parameters);
        }
    }

    /**
     * Notifies each registered listener that the current list item has ended.
     */
    @Override
    public void endListItem()
    {
        for (Listener listener : this.listeners) {
            listener.endListItem();
        }
    }

    /**
     * Notifies all registered listeners that the current list item has ended.
     *
     * @param parameters attributes associated with the list item
     */
    @Override
    public void endListItem(Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.endListItem(parameters);
        }
    }

    /**
     * Notifies all registered listeners that a macro marker has ended.
     *
     * @param name the macro marker name
     * @param macroParameters the macro's parameters
     * @param content the macro's content
     * @param isInline `true` if the macro marker was inline, `false` otherwise
     */
    @Override
    public void endMacroMarker(String name, Map<String, String> macroParameters, String content, boolean isInline)
    {
        for (Listener listener : this.listeners) {
            listener.endMacroMarker(name, macroParameters, content, isInline);
        }
    }

    /**
     * Notifies all registered listeners that the current paragraph has ended.
     *
     * @param parameters a map of paragraph parameters (attributes), or {@code null} if none
     */
    @Override
    public void endParagraph(Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.endParagraph(parameters);
        }
    }

    /**
     * Forwards the end of a quotation element to all registered listeners.
     *
     * @param parameters parameters associated with the quotation element
     */
    @Override
    public void endQuotation(Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.endQuotation(parameters);
        }
    }

    /**
     * Signals the end of a quotation line to each registered listener.
     */
    @Override
    public void endQuotationLine()
    {
        for (Listener listener : this.listeners) {
            listener.endQuotationLine();
        }
    }

    /**
     * Notify all contained listeners that a figure element is beginning.
     *
     * @param parameters a map of parameters for the figure (for example attributes like id, class, title); may be empty
     */
    @Override
    public void beginFigure(Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.beginFigure(parameters);
        }
    }

    /**
     * Signals the end of a figure element.
     *
     * @param parameters a map of parameters associated with the figure (may be empty)
     */
    @Override
    public void endFigure(Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.endFigure(parameters);
        }
    }

    /**
     * Forwards a begin-figure-caption event to all registered listeners.
     *
     * @param parameters element parameters (attributes) associated with the figure caption
     */
    @Override
    public void beginFigureCaption(Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.beginFigureCaption(parameters);
        }
    }

    /**
     * Notifies all registered listeners that a figure caption has ended.
     *
     * @param parameters a map of figure caption attributes (may be empty or null)
     */
    @Override
    public void endFigureCaption(Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.endFigureCaption(parameters);
        }
    }

    /**
     * Signals the end of a section to each contained listener.
     *
     * @param parameters parameters associated with the section
     */
    @Override
    public void endSection(Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.endSection(parameters);
        }
    }

    /**
     * Signals the end of a table block to all registered listeners.
     *
     * @param parameters rendering parameters or attributes associated with the table (may be empty)
     */
    @Override
    public void endTable(Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.endTable(parameters);
        }
    }

    /**
     * Signals the end of a table cell.
     *
     * @param parameters the attributes associated with the table cell, or an empty map if none
     */
    @Override
    public void endTableCell(Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.endTableCell(parameters);
        }
    }

    /**
     * Indicates the end of a table header cell to all registered listeners.
     *
     * @param parameters additional parameters associated with the table header cell, or an empty map if none
     */
    @Override
    public void endTableHeadCell(Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.endTableHeadCell(parameters);
        }
    }

    /**
     * Notifies all registered listeners that a table row has ended.
     *
     * @param parameters map of attributes for the table row
     */
    @Override
    public void endTableRow(Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.endTableRow(parameters);
        }
    }

    /**
     * Notify registered listeners about a sequence of empty lines in the input.
     *
     * @param count the number of consecutive empty lines encountered
     */
    @Override
    public void onEmptyLines(int count)
    {
        for (Listener listener : this.listeners) {
            listener.onEmptyLines(count);
        }
    }

    /**
     * Forwards a horizontal-line event to all registered listeners.
     *
     * @param parameters a map of attributes for the horizontal line, keyed by attribute name (may be empty)
     */
    @Override
    public void onHorizontalLine(Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.onHorizontalLine(parameters);
        }
    }

    /**
     * Dispatches an identifier event to each registered listener.
     *
     * @param name the identifier name encountered in the document
     */
    @Override
    public void onId(String name)
    {
        for (Listener listener : this.listeners) {
            listener.onId(name);
        }
    }

    /**
     * Forwards a macro event to all registered listeners.
     *
     * Invokes each contained listener with the provided macro identifier, parameters, content, and inline flag.
     *
     * @param id the macro identifier
     * @param parameters a mapping of macro parameter names to values
     * @param content the macro body/content
     * @param inline {@code true} if the macro is inline, {@code false} if it is block-level
     */
    @Override
    public void onMacro(String id, Map<String, String> parameters, String content, boolean inline)
    {
        for (Listener listener : this.listeners) {
            listener.onMacro(id, parameters, content, inline);
        }
    }

    /**
     * Notify all registered listeners that a new line has been encountered.
     */
    @Override
    public void onNewLine()
    {
        for (Listener listener : this.listeners) {
            listener.onNewLine();
        }
    }

    /**
     * Dispatches raw text content to all registered listeners.
     *
     * @param text the raw text content to deliver
     * @param syntax the syntax associated with the text
     */
    @Override
    public void onRawText(String text, Syntax syntax)
    {
        for (Listener listener : this.listeners) {
            listener.onRawText(text, syntax);
        }
    }

    /**
     * Forwards a space event to all registered listeners.
     */
    @Override
    public void onSpace()
    {
        for (Listener listener : this.listeners) {
            listener.onSpace();
        }
    }

    /**
     * Forwards a special-symbol event to all registered listeners.
     *
     * @param symbol the special symbol character encountered
     */
    @Override
    public void onSpecialSymbol(char symbol)
    {
        for (Listener listener : this.listeners) {
            listener.onSpecialSymbol(symbol);
        }
    }

    /**
     * Forwards a verbatim text event to all registered listeners.
     *
     * @param content    the verbatim text content
     * @param inline     {@code true} when the verbatim content is inline, {@code false} when it is a block
     * @param parameters additional attributes for the verbatim element as a map of names to values
     */
    @Override
    public void onVerbatim(String content, boolean inline, Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.onVerbatim(content, inline, parameters);
        }
    }

    /**
     * Dispatches a word event to all registered listeners.
     *
     * @param word the word text to deliver to each listener
     */
    @Override
    public void onWord(String word)
    {
        for (Listener listener : this.listeners) {
            listener.onWord(word);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.5RC1
     */
    @Override
    public void beginLink(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.beginLink(reference, freestanding, parameters);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.5RC1
     */
    @Override
    public void endLink(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.endLink(reference, freestanding, parameters);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.5RC1
     */
    @Override
    public void onImage(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.onImage(reference, freestanding, parameters);
        }
    }

    /**
     * Forwards an image event to all registered listeners.
     *
     * @param reference  the image resource reference
     * @param freestanding  {@code true} if the image is freestanding (not inline), {@code false} otherwise
     * @param id  optional image identifier, or {@code null} if none
     * @param parameters  additional image parameters (may be empty)
     */
    @Override
    public void onImage(ResourceReference reference, boolean freestanding, String id, Map<String, String> parameters)
    {
        for (Listener listener : this.listeners) {
            listener.onImage(reference, freestanding, id, parameters);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @since 3.0M2
     */
    @Override
    public void beginMetaData(MetaData metadata)
    {
        for (Listener listener : this.listeners) {
            listener.beginMetaData(metadata);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @since 3.0M2
     */
    @Override
    public void endMetaData(MetaData metadata)
    {
        for (Listener listener : this.listeners) {
            listener.endMetaData(metadata);
        }
    }
}
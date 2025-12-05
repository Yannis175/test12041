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

import org.xwiki.rendering.listener.reference.ResourceReference;
import org.xwiki.rendering.syntax.Syntax;

/**
 * A Listener wrapping another Listener.
 *
 * @version $Id$
 */
public class WrappingListener implements Listener
{
    /**
     * The Listener to wrap.
     */
    private Listener listener;

    /**
     * Sets the wrapped listener that will receive delegated events.
     *
     * @param listener the Listener to delegate events to; may be {@code null} to disable delegation
     */
    public void setWrappedListener(Listener listener)
    {
        this.listener = listener;
    }

    /**
     * Retrieve the currently wrapped Listener.
     *
     * @return the currently wrapped Listener, or {@code null} if no listener has been set
     */
    public Listener getWrappedListener()
    {
        return this.listener;
    }

    /**
     * Signal the start of a document and provide its associated metadata.
     *
     * @param metadata the metadata describing the document
     */
    @Override
    public void beginDocument(MetaData metadata)
    {
        if (this.listener != null) {
            this.listener.beginDocument(metadata);
        }
    }

    /**
     * Signals the start of a figure element with the provided parameters.
     *
     * @param parameters attributes for the figure (key/value pairs describing properties of the figure)
     */
    @Override
    public void beginFigure(Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.beginFigure(parameters);
        }
    }

    /**
     * Forwards a "begin figure caption" event to the wrapped listener if one is set.
     *
     * @param parameters additional attributes for the figure caption (may be {@code null})
     */
    @Override
    public void beginFigureCaption(Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.beginFigureCaption(parameters);
        }
    }

    /**
     * Signals the end of the document to the wrapped listener, if one is set.
     *
     * @param metadata document metadata associated with the end-document event
     */
    @Override
    public void endDocument(MetaData metadata)
    {
        if (this.listener != null) {
            this.listener.endDocument(metadata);
        }
    }

    /**
     * Signals the end of a figure block.
     *
     * @param parameters a map of parameters for the figure, or null if none
     */
    @Override
    public void endFigure(Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.endFigure(parameters);
        }
    }

    /**
     * Signals the end of the current figure caption.
     *
     * @param parameters optional attributes for the figure caption (may be {@code null})
     */
    @Override
    public void endFigureCaption(Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.endFigureCaption(parameters);
        }
    }

    /**
     * Begin a group element using the provided parameters.
     *
     * @param parameters a map of attribute names to values for the group, or {@code null} if there are no parameters
     */
    @Override
    public void beginGroup(Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.beginGroup(parameters);
        }
    }

    /**
     * Signals the end of the current group block.
     *
     * @param parameters optional parameters or attributes for the group, or {@code null} if none
     */
    @Override
    public void endGroup(Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.endGroup(parameters);
        }
    }

    /**
     * Signals the start of a formatting span with the given format and optional parameters.
     *
     * @param format the format to start
     * @param parameters optional attributes controlling the format, or {@code null} if none
     */
    @Override
    public void beginFormat(Format format, Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.beginFormat(format, parameters);
        }
    }

    /**
     * Signals the start of a list using the specified list type and rendering parameters.
     *
     * @param type the kind of list to begin (for example ordered or unordered)
     * @param parameters optional rendering parameters for the list; may be {@code null}
     */
    @Override
    public void beginList(ListType type, Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.beginList(type, parameters);
        }
    }

    /**
     * Signals the start of a list item.
     */
    @Override
    public void beginListItem()
    {
        if (this.listener != null) {
            this.listener.beginListItem();
        }
    }

    /**
     * Signals the start of a list item using the provided parameters.
     *
     * @param parameters additional attributes for the list item (e.g., styling or identifiers); may be {@code null}
     */
    @Override
    public void beginListItem(Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.beginListItem(parameters);
        }
    }

    /**
     * Signals the start of a macro marker.
     *
     * @param name the macro's name
     * @param parameters a map of macro parameters (may be null)
     * @param content the macro content (may be null)
     * @param isInline true if the macro marker is inline, false if block-level
     */
    @Override
    public void beginMacroMarker(String name, Map<String, String> parameters, String content, boolean isInline)
    {
        if (this.listener != null) {
            this.listener.beginMacroMarker(name, parameters, content, isInline);
        }
    }

    /**
     * Signals the start of a paragraph.
     *
     * @param parameters optional attributes for the paragraph (may be null)
     */
    @Override
    public void beginParagraph(Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.beginParagraph(parameters);
        }
    }

    /**
     * Signals the start of a section with the given parameters.
     *
     * @param parameters a map of section parameters (for example attributes or options); may be {@code null}
     */
    @Override
    public void beginSection(Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.beginSection(parameters);
        }
    }

    /**
     * Begin a header block with the specified level, identifier, and parameters.
     *
     * If no wrapped listener is set, this call has no effect.
     *
     * @param level the header level
     * @param id an optional identifier for the header (may be null)
     * @param parameters optional parameters associated with the header (may be null)
     */
    @Override
    public void beginHeader(HeaderLevel level, String id, Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.beginHeader(level, id, parameters);
        }
    }

    /**
     * Signals the end of a formatting element.
     *
     * @param format the format that is ending
     * @param parameters additional parameters associated with the format, or {@code null} if none
     */
    @Override
    public void endFormat(Format format, Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.endFormat(format, parameters);
        }
    }

    /**
     * Delegates the end of a list event to the wrapped listener when one is set.
     *
     * @param type the list type being ended
     * @param parameters optional parameters for the list, or {@code null} if none
     */
    @Override
    public void endList(ListType type, Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.endList(type, parameters);
        }
    }

    /**
     * Signal the end of the current list item to the wrapped listener, if one is set.
     */
    @Override
    public void endListItem()
    {
        if (this.listener != null) {
            this.listener.endListItem();
        }
    }

    /**
     * Notifies the wrapped listener that the current list item has ended.
     *
     * @param parameters a map of parameters associated with the list item, or {@code null} if there are no parameters
     */
    @Override
    public void endListItem(Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.endListItem(parameters);
        }
    }

    /**
     * Signal the end of a macro marker.
     *
     * @param name the macro identifier
     * @param parameters the macro parameters map
     * @param content the macro content, or {@code null} if none
     * @param isInline {@code true} if the macro is inline, {@code false} if it is block-level
     */
    @Override
    public void endMacroMarker(String name, Map<String, String> parameters, String content, boolean isInline)
    {
        if (this.listener != null) {
            this.listener.endMacroMarker(name, parameters, content, isInline);
        }
    }

    /**
     * Notifies the listener that a paragraph has ended.
     *
     * @param parameters optional parameters associated with the paragraph (keys and values are implementation-defined)
     */
    @Override
    public void endParagraph(Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.endParagraph(parameters);
        }
    }

    /**
     * Signals the end of a section.
     *
     * @param parameters a map of section parameters (string keys and values), or {@code null} if none
     */
    @Override
    public void endSection(Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.endSection(parameters);
        }
    }

    /**
     * Notifies the wrapped listener that a header has ended with the specified level, identifier, and parameters.
     *
     * @param level the header level
     * @param id the header identifier, or {@code null} if none
     * @param parameters additional header parameters, or {@code null} if none
     */
    @Override
    public void endHeader(HeaderLevel level, String id, Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.endHeader(level, id, parameters);
        }
    }

    /**
     * Begins a link element.
     *
     * If a wrapped listener is set, the begin-link event is forwarded with the given reference, freestanding flag, and parameters.
     *
     * @param reference  the link target reference
     * @param freestanding  true if the link is freestanding (appears as a standalone element), false if inline
     * @param parameters  optional attributes for the link (may be null)
     */
    @Override
    public void beginLink(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.beginLink(reference, freestanding, parameters);
        }
    }

    /**
     * Forwards the end-of-link event to the wrapped listener if one is set.
     *
     * @param reference   the target reference of the link
     * @param freestanding whether the link is freestanding
     * @param parameters  optional parameters associated with the link
     */
    @Override
    public void endLink(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.endLink(reference, freestanding, parameters);
        }
    }

    /**
     * Handle a macro occurrence with the given identifier, parameters and content.
     *
     * @param id the macro identifier
     * @param parameters a map of parameter names to values for the macro; may be null or empty
     * @param content the macro content (body) or null if none
     * @param inline {@code true} if the macro is inline, {@code false} if it is block-level
     */
    @Override
    public void onMacro(String id, Map<String, String> parameters, String content, boolean inline)
    {
        if (this.listener != null) {
            this.listener.onMacro(id, parameters, content, inline);
        }
    }

    /**
     * Signals that a new line has been encountered to the wrapped listener.
     *
     * If no wrapped listener is set, the call is a no-op.
     */
    @Override
    public void onNewLine()
    {
        if (this.listener != null) {
            this.listener.onNewLine();
        }
    }

    /**
     * Signals that a space character was encountered.
     *
     * If no wrapped listener is configured, the call has no effect.
     */
    @Override
    public void onSpace()
    {
        if (this.listener != null) {
            this.listener.onSpace();
        }
    }

    /**
     * Notifies that a special symbol character was encountered.
     *
     * @param symbol the special symbol character encountered
     */
    @Override
    public void onSpecialSymbol(char symbol)
    {
        if (this.listener != null) {
            this.listener.onSpecialSymbol(symbol);
        }
    }

    /**
     * Forward a word token to the wrapped listener, if one is set.
     *
     * @param word the word text encountered by the parser
     */
    @Override
    public void onWord(String word)
    {
        if (this.listener != null) {
            this.listener.onWord(word);
        }
    }

    /**
     * Notifies the wrapped listener of an identifier token.
     *
     * @param name the identifier text
     */
    @Override
    public void onId(String name)
    {
        if (this.listener != null) {
            this.listener.onId(name);
        }
    }

    /**
     * Notifies the wrapped listener of a horizontal line element, forwarding the provided parameters.
     *
     * @param parameters a map of attributes describing the horizontal line (may be null)
     */
    @Override
    public void onHorizontalLine(Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.onHorizontalLine(parameters);
        }
    }

    /**
     * Signal that a run of empty lines was encountered.
     *
     * @param count the number of consecutive empty lines
     */
    @Override
    public void onEmptyLines(int count)
    {
        if (this.listener != null) {
            this.listener.onEmptyLines(count);
        }
    }

    /**
     * Forwards a verbatim text event to the wrapped listener when one is set.
     *
     * @param content    the verbatim text content
     * @param inline     `true` if the verbatim content is inline, `false` if it is a block
     * @param parameters optional parameters/attributes for the verbatim content, may be `null`
     */
    @Override
    public void onVerbatim(String content, boolean inline, Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.onVerbatim(content, inline, parameters);
        }
    }

    /**
     * Delivers a raw-text event to the wrapped listener.
     *
     * @param text the raw text content
     * @param syntax the syntax associated with the raw text
     */
    @Override
    public void onRawText(String text, Syntax syntax)
    {
        if (this.listener != null) {
            this.listener.onRawText(text, syntax);
        }
    }

    /**
     * Begin a definition list.
     *
     * @param parameters a map of attributes for the definition list, or {@code null} if none
     */
    @Override
    public void beginDefinitionList(Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.beginDefinitionList(parameters);
        }
    }

    /**
     * Signals the end of a definition list.
     *
     * @param parameters additional parameters for the definition list, or {@code null} if none
     */
    @Override
    public void endDefinitionList(Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.endDefinitionList(parameters);
        }
    }

    /**
     * Signal the start of a definition term.
     *
     * <p>If a wrapped listener is installed, this forwards the event to it; otherwise this method is a no-op.</p>
     */
    @Override
    public void beginDefinitionTerm()
    {
        if (this.listener != null) {
            this.listener.beginDefinitionTerm();
        }
    }

    /**
     * Signals the start of a definition description block.
     *
     * If a wrapped listener is installed, the event is forwarded; otherwise this method is a no-op.
     */
    @Override
    public void beginDefinitionDescription()
    {
        if (this.listener != null) {
            this.listener.beginDefinitionDescription();
        }
    }

    /**
     * Signals the end of a definition term.
     *
     * <p>No action is taken if no wrapped listener is installed.</p>
     */
    @Override
    public void endDefinitionTerm()
    {
        if (this.listener != null) {
            this.listener.endDefinitionTerm();
        }
    }

    /**
     * Signals the end of the current definition description block.
     *
     * If a wrapped listener is installed, forwards this event to it; otherwise the call is a no-op.
     */
    @Override
    public void endDefinitionDescription()
    {
        if (this.listener != null) {
            this.listener.endDefinitionDescription();
        }
    }

    /**
     * Signals the start of a quotation block with the given parameters.
     *
     * @param parameters a map of quotation parameters (attributes); may be {@code null}
     */
    @Override
    public void beginQuotation(Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.beginQuotation(parameters);
        }
    }

    /**
     * Signals the end of a quotation block.
     *
     * @param parameters a map of parameters associated with the quotation, or {@code null} if none
     */
    @Override
    public void endQuotation(Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.endQuotation(parameters);
        }
    }

    /**
     * Signals the start of a quotation line.
     *
     * If a wrapped listener is installed, forwards the event to it; otherwise does nothing.
     */
    @Override
    public void beginQuotationLine()
    {
        if (this.listener != null) {
            this.listener.beginQuotationLine();
        }
    }

    /**
     * Signals the end of a line inside a quotation block.
     *
     * If a wrapped listener is installed, the event is forwarded to it; otherwise this call is a no-op.
     */
    @Override
    public void endQuotationLine()
    {
        if (this.listener != null) {
            this.listener.endQuotationLine();
        }
    }

    /**
     * Notifies the wrapped listener that a table is starting.
     *
     * @param parameters a map of parameters describing the table (may be {@code null})
     */
    @Override
    public void beginTable(Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.beginTable(parameters);
        }
    }

    /**
     * Signals the start of a table cell with the given parameters.
     *
     * If no wrapped listener is set, the call has no effect.
     *
     * @param parameters mapping of parameter names to values for the table cell; may be {@code null}
     */
    @Override
    public void beginTableCell(Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.beginTableCell(parameters);
        }
    }

    /**
     * Signals the start of a table header cell.
     *
     * @param parameters a map of parameters for the table header cell, or {@code null} if none
     */
    @Override
    public void beginTableHeadCell(Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.beginTableHeadCell(parameters);
        }
    }

    /**
     * Signals the start of a table row.
     *
     * @param parameters a map of attributes for the table row (attribute name -> attribute value); may be null if no attributes
     */
    @Override
    public void beginTableRow(Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.beginTableRow(parameters);
        }
    }

    /**
     * Signals the end of a table block.
     *
     * @param parameters a map of parameters (attributes) associated with the table, or {@code null} if none
     */
    @Override
    public void endTable(Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.endTable(parameters);
        }
    }

    /**
     * Signal the end of the current table cell.
     *
     * @param parameters optional attributes for the table cell (may be null)
     */
    @Override
    public void endTableCell(Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.endTableCell(parameters);
        }
    }

    /**
     * Signals the end of a table header cell.
     *
     * @param parameters a map of cell parameters/attributes (may be {@code null})
     */
    @Override
    public void endTableHeadCell(Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.endTableHeadCell(parameters);
        }
    }

    /**
     * Signals the end of a table row.
     *
     * @param parameters optional parameters associated with the table row (may be {@code null})
     */
    @Override
    public void endTableRow(Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.endTableRow(parameters);
        }
    }

    /**
     * Notify about an image using its resource reference, freestanding status, and associated parameters.
     *
     * @param reference  the image's resource reference
     * @param freestanding  `true` if the image is freestanding, `false` otherwise
     * @param parameters  optional image parameters (may be null)
     */
    @Override
    public void onImage(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.onImage(reference, freestanding, parameters);
        }
    }

    /**
     * Signals an image element with an explicit id to the wrapped listener.
     *
     * @param reference   the resource reference pointing to the image
     * @param freestanding true if the image is a freestanding block (not inline), false if inline
     * @param id          an explicit identifier for the image, or null if none
     * @param parameters  additional image parameters (attributes) or null if none
     */
    @Override
    public void onImage(ResourceReference reference, boolean freestanding, String id, Map<String, String> parameters)
    {
        if (this.listener != null) {
            this.listener.onImage(reference, freestanding, id, parameters);
        }
    }

    /**
     * Forwards the start-of-metadata event to the wrapped listener, if one is set.
     *
     * @param metadata the metadata associated with the document being started
     */
    @Override
    public void beginMetaData(MetaData metadata)
    {
        if (this.listener != null) {
            this.listener.beginMetaData(metadata);
        }
    }

    /**
     * Signals the end of metadata processing for the provided metadata.
     *
     * @param metadata the metadata that has completed processing
     */
    @Override
    public void endMetaData(MetaData metadata)
    {
        if (this.listener != null) {
            this.listener.endMetaData(metadata);
        }
    }
}
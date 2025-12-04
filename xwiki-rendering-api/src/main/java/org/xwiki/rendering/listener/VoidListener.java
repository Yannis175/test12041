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
 * A Listener that does nothing.
 *
 * @version $Id$
 * @since 3.2RC1
 */
public class VoidListener implements Listener
{
    /**
     * Signal the start of a definition description.
     *
     * <p>No-op implementation; does nothing.</p>
     */
    @Override
    public void beginDefinitionDescription()
    {
        // Do nothing.
    }

    /**
     * Invoked when a definition list begins.
     *
     * @param parameters rendering parameters for the definition list, or null if none
     */
    @Override
    public void beginDefinitionList(Map<String, String> parameters)
    {
        // Do nothing.
    }

    /**
     * Handles the start of a definition term event without producing any side effects.
     */
    @Override
    public void beginDefinitionTerm()
    {
        // Do nothing.
    }

    /**
     * Notified when a document starts; this implementation ignores the event.
     *
     * @param metadata the document metadata
     */
    @Override
    public void beginDocument(MetaData metadata)
    {
        // Do nothing.
    }

    /**
     * Handle the start of a formatting block without performing any action.
     *
     * @param format the format being started (e.g., bold, italic)
     * @param parameters optional parameters associated with the format, or {@code null} if none
     */
    @Override
    public void beginFormat(Format format, Map<String, String> parameters)
    {
        // Do nothing.
    }

    /**
     * Signals the start of a group block in the rendering stream.
     *
     * @param parameters additional attributes for the group, if any (may be empty)
     */
    @Override
    public void beginGroup(Map<String, String> parameters)
    {
        // Do nothing.
    }

    /**
     * Called at the start of a header element; this implementation performs no action.
     *
     * @param level the header level (e.g., H1, H2)
     * @param id the optional identifier for the header, or {@code null} if none
     * @param parameters additional rendering parameters for the header
     */
    @Override
    public void beginHeader(HeaderLevel level, String id, Map<String, String> parameters)
    {
        // Do nothing.
    }

    /**
     * Signals the start of a list.
     *
     * @param type the kind of list (for example ordered, unordered, or definition)
     * @param parameters optional attributes for the list (e.g., style or identifier)
     */
    @Override
    public void beginList(ListType type, Map<String, String> parameters)
    {
        // Do nothing.
    }

    /**
     * Signals the start of a list item.
     *
     * <p>This implementation performs no action.</p>
     */
    @Override
    public void beginListItem()
    {
        // Do nothing.
    }

    /**
     * Invoked at the start of a list item.
     *
     * @param parameters parameters for the list item, or {@code null} if none
     */
    @Override
    public void beginListItem(Map<String, String> parameters)
    {
        // Do nothing.
    }

    /**
     * Called when a macro marker starts in the parsed document.
     *
     * @param name the macro marker name
     * @param macroParameters the macro parameters (may be empty)
     * @param content the macro content as a string (may be null or empty)
     * @param isInline true if the macro marker is inline, false if it is block-level
     */
    @Override
    public void beginMacroMarker(String name, Map<String, String> macroParameters, String content, boolean isInline)
    {
        // Do nothing.
    }

    /**
     * Signal the start of a metadata block.
     *
     * This implementation performs no action.
     *
     * @param metadata the metadata associated with the document
     */
    @Override
    public void beginMetaData(MetaData metadata)
    {
        // Do nothing.
    }

    /**
     * Invoked when a paragraph starts.
     *
     * <p>This implementation does nothing.</p>
     *
     * @param parameters a map of paragraph parameters (may be {@code null})
     */
    @Override
    public void beginParagraph(Map<String, String> parameters)
    {
        // Do nothing.
    }

    /**
     * Notifies that a quotation block is starting.
     *
     * @param parameters optional parameters for the quotation block, or {@code null} if there are none
     */
    @Override
    public void beginQuotation(Map<String, String> parameters)
    {
        // Do nothing.
    }

    /**
     * Signals the start of a quotation line.
     */
    @Override
    public void beginQuotationLine()
    {
        // Do nothing.
    }

    /**
     * Signals the start of a section in the rendering event stream.
     *
     * @param parameters optional parameters associated with the section, or {@code null} if none
     */
    @Override
    public void beginSection(Map<String, String> parameters)
    {
        // Do nothing.
    }

    /**
     * Notified that a table element is beginning.
     *
     * @param parameters a map of table parameters (attribute name → value), may be empty if there are no parameters
     */
    @Override
    public void beginTable(Map<String, String> parameters)
    {
        // Do nothing.
    }

    /**
     * Notifies the listener that a table cell begins.
     *
     * @param parameters additional key-value parameters for the table cell
     */
    @Override
    public void beginTableCell(Map<String, String> parameters)
    {
        // Do nothing.
    }

    /**
     * Notified at the start of a table header cell; this implementation ignores the event.
     *
     * @param parameters a map of parameters associated with the table header cell
     */
    @Override
    public void beginTableHeadCell(Map<String, String> parameters)
    {
        // Do nothing.
    }

    /**
     * Signals the start of a table row.
     *
     * @param parameters parameters for the table row, such as attributes or style hints
     */
    @Override
    public void beginTableRow(Map<String, String> parameters)
    {
        // Do nothing.
    }

    /**
     * Handle the end of a definition description without performing any action.
     */
    @Override
    public void endDefinitionDescription()
    {
        // Do nothing.
    }

    /**
     * Called when a definition list ends; this implementation performs no action.
     *
     * @param parameters the attributes/parameters associated with the definition list, or null if none
     */
    @Override
    public void endDefinitionList(Map<String, String> parameters)
    {
        // Do nothing.
    }

    /**
     * Invoked when a definition term ends; this implementation performs no action.
     */
    @Override
    public void endDefinitionTerm()
    {
        // Do nothing.
    }

    /**
     * Notified when document processing ends; this implementation ignores the event.
     *
     * @param metadata the metadata associated with the document end event
     */
    @Override
    public void endDocument(MetaData metadata)
    {
        // Do nothing.
    }

    /**
     * Handle the end of a formatting span.
     *
     * @param format the formatting type that ends (for example bold, italic, code)
     * @param parameters additional format parameters, or {@code null} if none
     */
    @Override
    public void endFormat(Format format, Map<String, String> parameters)
    {
        // Do nothing.
    }

    /**
     * Marks the end of a group element.
     *
     * @param parameters map of parameters associated with the group element
     */
    @Override
    public void endGroup(Map<String, String> parameters)
    {
        // Do nothing.
    }

    /**
     * No-op callback invoked when a header block is closed.
     *
     * @param level the header level being ended
     * @param id the identifier (anchor) associated with the header, or {@code null} if none
     * @param parameters additional header parameters or attributes, may be empty or {@code null}
     */
    @Override
    public void endHeader(HeaderLevel level, String id, Map<String, String> parameters)
    {
        // Do nothing.
    }

    /**
     * Signal that a list has ended.
     *
     * @param type the kind of list that ended (for example: ordered, unordered, definition)
     * @param parameters rendering parameters associated with the list, or an empty map if none
     */
    @Override
    public void endList(ListType type, Map<String, String> parameters)
    {
        // Do nothing.
    }

    /**
     * Handles the end of a list item event without performing any action.
     */
    @Override
    public void endListItem()
    {
        // Do nothing.
    }

    /**
     * Handles the end of a macro marker.
     *
     * @param name the macro marker name
     * @param macroParameters a map of macro parameters, may be empty
     * @param content the macro content, if any
     * @param isInline {@code true} when the macro marker is inline, {@code false} otherwise
     */
    @Override
    public void endMacroMarker(String name, Map<String, String> macroParameters, String content, boolean isInline)
    {
        // Do nothing.
    }

    /**
     * Signal the end of metadata section for the current document.
     *
     * @param metadata the document metadata that has finished being processed
     */
    @Override
    public void endMetaData(MetaData metadata)
    {
        // Do nothing.
    }

    /**
     * No-op invoked when a paragraph ends.
     *
     * @param parameters additional parameters associated with the paragraph, or {@code null} if there are none
     */
    @Override
    public void endParagraph(Map<String, String> parameters)
    {
        // Do nothing.
    }

    /**
     * Marks the end of a quotation block.
     *
     * @param parameters additional parameters associated with the quotation block (for example attributes or styling)
     */
    @Override
    public void endQuotation(Map<String, String> parameters)
    {
        // Do nothing.
    }

    /**
     * Called when a quotation line ends.
     *
     * <p>No-op implementation.</p>
     */
    @Override
    public void endQuotationLine()
    {
        // Do nothing.
    }

    /**
     * Invoked when the current section ends.
     *
     * @param parameters the section's parameters
     */
    @Override
    public void endSection(Map<String, String> parameters)
    {
        // Do nothing.
    }

    /**
     * Handle the end of a table without performing any action.
     *
     * @param parameters the table's parameters or attributes (rendering-specific key/value pairs)
     */
    @Override
    public void endTable(Map<String, String> parameters)
    {
        // Do nothing.
    }

    /**
     * Handles the end of a table cell event; this implementation performs no action.
     *
     * @param parameters parameters associated with the table cell (may be {@code null})
     */
    @Override
    public void endTableCell(Map<String, String> parameters)
    {
        // Do nothing.
    }

    /**
     * Signals the end of a table header cell event to the listener; this implementation performs no action.
     *
     * @param parameters optional parameters associated with the header cell (may be empty or null)
     */
    @Override
    public void endTableHeadCell(Map<String, String> parameters)
    {
        // Do nothing.
    }

    /**
     * Called when a table row finishes; this implementation does nothing.
     *
     * @param parameters optional rendering parameters associated with the table row, or {@code null} if none
     */
    @Override
    public void endTableRow(Map<String, String> parameters)
    {
        // Do nothing.
    }

    /**
     * Called when the renderer encounters one or more consecutive empty lines.
     *
     * @param count the number of consecutive empty lines
     */
    @Override
    public void onEmptyLines(int count)
    {
        // Do nothing.
    }

    /**
     * Handles a horizontal line event emitted by the renderer.
     *
     * This implementation ignores the event and performs no action.
     *
     * @param parameters rendering parameters for the horizontal line; may be {@code null} or empty
     */
    @Override
    public void onHorizontalLine(Map<String, String> parameters)
    {
        // Do nothing.
    }

    /**
     * Handle an identifier event without performing any action.
     *
     * @param name the identifier name provided by the event
     */
    @Override
    public void onId(String name)
    {
        // Do nothing.
    }

    /**
     * Receives a macro event and ignores it.
     *
     * @param id the macro identifier
     * @param parameters the macro parameters, or {@code null} if none
     * @param content the macro content, or {@code null} if none
     * @param inline {@code true} if the macro is inline, {@code false} if it is block-level
     */
    @Override
    public void onMacro(String id, Map<String, String> parameters, String content, boolean inline)
    {
        // Do nothing.
    }

    /**
     * Signals that a newline was encountered.
     *
     * This implementation performs no action.
     */
    @Override
    public void onNewLine()
    {
        // Do nothing.
    }

    /**
     * Handle a raw text event containing content and its syntax.
     *
     * @param content the raw text content
     * @param syntax the syntax that applies to the content
     */
    @Override
    public void onRawText(String content, Syntax syntax)
    {
        // Do nothing.
    }

    /**
     * Handle a single space character encountered in the rendering stream.
     *
     * This implementation does nothing.
     */
    @Override
    public void onSpace()
    {
        // Do nothing.
    }

    /**
     * Handles a special symbol encountered during rendering.
     *
     * @param symbol the special symbol character encountered
     */
    @Override
    public void onSpecialSymbol(char symbol)
    {
        // Do nothing.
    }

    /**
     * Handles a verbatim (preformatted) text segment.
     *
     * @param content the verbatim text content
     * @param inline  {@code true} when the verbatim content is inline, {@code false} when it is a block
     * @param parameters optional rendering parameters for the verbatim element (may be empty or {@code null})
     */
    @Override
    public void onVerbatim(String content, boolean inline, Map<String, String> parameters)
    {
        // Do nothing.
    }

    /**
     * Called when a word token is encountered during rendering.
     *
     * @param word the textual content of the word token
     */
    @Override
    public void onWord(String word)
    {
        // Do nothing.
    }

    /**
     * Ignores an image rendering event.
     *
     * @param reference the image resource reference
     * @param freestanding {@code true} if the image is freestanding (block), {@code false} if inline
     * @param parameters additional image parameters provided by the renderer
     */
    @Override
    public void onImage(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        // Do nothing.
    }

    /**
     * Receives an image event and ignores it.
     *
     * @param reference the resource reference identifying the image
     * @param freestanding {@code true} if the image is freestanding (not inline), {@code false} otherwise
     * @param id an optional identifier for the image, or {@code null} if none
     * @param parameters additional image parameters (for example attributes like alt, width), or {@code null}
     */
    @Override
    public void onImage(ResourceReference reference, boolean freestanding, String id, Map<String, String> parameters)
    {
        // Do nothing.
    }

    /**
     * Signals the start of a link element; this implementation performs no action.
     *
     * @param reference the target resource of the link
     * @param freestanding {@code true} if the link is freestanding (occupies its own block), {@code false} otherwise
     * @param parameters optional parameters or attributes associated with the link
     */
    @Override
    public void beginLink(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        // Do nothing.
    }

    /**
     * Called when a link element ends.
     *
     * @param reference the resource reference targeted by the link
     * @param freestanding whether the link is freestanding (not part of surrounding text)
     * @param parameters additional parameters associated with the link, or {@code null} if none
     */
    @Override
    public void endLink(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        // Do nothing.
    }

    /**
     * Signals the end of the current list item.
     *
     * @param parameters rendering parameters associated with the list item, or {@code null} if none
     */
    @Override
    public void endListItem(Map<String, String> parameters)
    {
        // Do nothing.
    }
}
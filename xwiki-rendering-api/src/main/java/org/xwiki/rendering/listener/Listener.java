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

import java.util.Collections;
import java.util.Map;

import org.xwiki.filter.annotation.Default;
import org.xwiki.filter.annotation.Name;
import org.xwiki.rendering.syntax.Syntax;

/**
 * Contains callback events called when a document has been parsed and when it needs to be modified or rendered. More
 * specifically when a document is parsed it generates an {@link org.xwiki.rendering.block.XDOM} object. That object has
 * a {@link org.xwiki.rendering.block.XDOM#traverse(Listener)} method that accepts a {@link Listener} object. For each
 * {@link org.xwiki.rendering.block.Block} element found in the document its
 * {@link org.xwiki.rendering.block.Block#traverse} method is called leading to the generation of events from this
 * interface.
 * <p>
 * Here's an example of usage:
 * </p>
 *
 * <pre>
 * &lt;code&gt;
 *   XDOM dom = parser.parse(source);
 *   MyListener listener = new MyListener(...);
 *   dom.traverse(listener);
 *   // At this stage all events have been sent to MyListener.
 * &lt;/code&gt;
 * </pre>
 *
 * @version $Id$
 * @since 1.5M2
 */
public interface Listener extends LinkListener, ImageListener
{
    /**
     * To use when there is no parameters.
     */
    Map<String, String> EMPTY_PARAMETERS = Collections.emptyMap();

    /**
 * Signal the start of a document and associate metadata with subsequent listener events.
 *
 * @param metadata the metadata to associate with the following events; see {@link MetaData}
 * @since 3.0M2
 */
    void beginDocument(MetaData metadata);

    /**
 * Signals the end of the document and supplies the document's metadata.
 *
 * @param metadata the metadata associated with the document
 * @since 3.0M2
 */
    void endDocument(MetaData metadata);

    /**
 * Signals the start of a metadata block associated with the document.
 *
 * @param metadata metadata to associate with subsequent rendering events
 * @since 3.0M2
 */
    void beginMetaData(MetaData metadata);

    /**
 * Signals the end of a metadata block.
 *
 * @param metadata the metadata associated with the ended block
 * @since 3.0M2
 */
    void endMetaData(MetaData metadata);

    /**
 * Signals the start of a group of standalone elements, used to allow standalone elements inside list items,
 * table cells, sections, etc., and to attach parameters to that group.
 *
 * @param parameters mapping of parameter names to values (may be empty). Example: style="background-color: blue"
 * @since 1.8.3
 */
    void beginGroup(@Default("") Map<String, String> parameters);

    /**
 * Signals the end of a grouped element.
 *
 * @param parameters map of attributes for the group (e.g., style="background-color: blue")
 * @since 1.8.3
 */
    void endGroup(@Default("") Map<String, String> parameters);

    /**
 * Signals the start of a text formatting block (for example, bold or italic).
 *
 * @param format the formatting to apply
 * @param parameters optional attributes for the formatting (for example, style="background-color: blue")
 * @see Format
 */
    void beginFormat(@Default("NONE") Format format, @Default("") Map<String, String> parameters);

    /**
     * End of a text formatting block.
     *
     * @param format the formatting type (bold, italic, etc)
     * @param parameters a generic list of parameters. Example: style="background-color: blue"
     * @see Format
     */
    void endFormat(@Default("NONE") Format format, @Default("") Map<String, String> parameters);

    /**
 * Signals the start of a paragraph.
 *
 * @param parameters map of paragraph attributes (for example, {@code style="background-color: blue"}); may be empty
 */
    void beginParagraph(@Default("") Map<String, String> parameters);

    /**
 * Signal the end of a paragraph.
 *
 * @param parameters map of paragraph attributes (e.g., style="background-color: blue")
 */
    void endParagraph(@Default("") Map<String, String> parameters);

    /**
 * Signals the start of a list.
 *
 * @param type the list type (for example BULLETED or NUMBERED)
 * @param parameters map of list attributes (for example {@code "style"} -> {@code "background-color: blue"})
 * @see ListType
 */
    void beginList(@Default("BULLETED") ListType type, @Default("") Map<String, String> parameters);

    /**
     * Signals the start of a definition list (equivalent to an HTML `<dl>`).
     *
     * @param parameters a map of list parameters (attribute name -> value); may be empty
     * @since 2.0RC1
     */
    void beginDefinitionList(@Default("") Map<String, String> parameters);

    /**
 * Signals the end of a list.
 *
 * @param type the list type (for example, BULLETED or NUMBERED)
 * @param parameters map of parameters for the list (for example {@code "style" -> "background-color: blue"})
 * @see ListType
 */
    void endList(@Default("BULLETED") ListType type, @Default("") Map<String, String> parameters);

    /**
 * Signals the end of a definition list.
 *
 * @param parameters optional attributes for the list (for example, style entries like {@code background-color: blue})
 * @since 2.0RC1
 */
    void endDefinitionList(@Default("") Map<String, String> parameters);

    /**
 * Signals the start of a list item.
 */
    void beginListItem();

    /**
     * Signals the start of a list item using the given parameters.
     *
     * @param parameters map of parameters for the list item (for example, "style" -> "background-color: blue")
     * @since 10.0
     */
    default void beginListItem(@Default("") Map<String, String> parameters)
    {
        beginListItem();
    }

    /**
 * Signals the start of a definition list term (for example, the HTML <dt> element).
 *
 * @since 1.6M2
 */
    void beginDefinitionTerm();

    /**
     * Start of a definition list description. For example in HTML this is the equivalent of &lt;dd&gt;.
     *
     * @since 1.6M2
     */
    void beginDefinitionDescription();

    /**
     * End of a list item.
     */
    void endListItem();

    /**
     * Signals the end of a list item.
     *
     * @param parameters a map of parameters for the list item (e.g., "style" -> "background-color: blue")
     * @since 10.0
     */
    default void endListItem(@Default("") Map<String, String> parameters)
    {
        endListItem();
    }

    /**
 * Signals the end of a definition list term.
 *
 * @since 1.6M2
 */
    void endDefinitionTerm();

    /**
     * End of a definition list description. For example in HTML this is the equivalent of &lt;/dd&gt;.
     *
     * @since 1.6M2
     */
    void endDefinitionDescription();

    /**
 * Signals the start of a table.
 *
 * @param parameters map of table parameters (attributes); may be empty
 * @since 1.6M2
 */
    void beginTable(@Default("") Map<String, String> parameters);

    /**
 * Signals the start of a table row.
 *
 * @param parameters map of row attributes (key → value); may be empty
 * @since 1.6M2
 */
    void beginTableRow(@Default("") Map<String, String> parameters);

    /**
 * Signals the start of a table cell.
 *
 * @param parameters a map of cell attributes (for example styling or span information); may be empty
 * @since 1.6M2
 */
    void beginTableCell(@Default("") Map<String, String> parameters);

    /**
 * Signals the start of a table header cell.
 *
 * @param parameters optional attributes for the header cell (for example styling, colspan, rowspan)
 * @since 1.6M2
 */
    void beginTableHeadCell(@Default("") Map<String, String> parameters);

    /**
 * Signals the end of a table element.
 *
 * @param parameters a map of table parameters (for example styling or other attributes)
 * @since 1.6M2
 */
    void endTable(@Default("") Map<String, String> parameters);

    /**
 * Signals the end of a table row.
 *
 * @param parameters map of attributes for the table row (may be empty)
 * @since 1.6M2
 */
    void endTableRow(@Default("") Map<String, String> parameters);

    /**
 * Signals the end of a table cell.
 *
 * @param parameters additional attributes for the table cell (for example styling or alignment); may be empty
 * @since 1.6M2
 */
    void endTableCell(@Default("") Map<String, String> parameters);

    /**
 * Signals the end of a table header cell.
 *
 * @param parameters a map of attributes for the table header cell (for example alignment, colspan, rowspan, or style)
 * @since 1.6M2
 */
    void endTableHeadCell(@Default("") Map<String, String> parameters);

    /**
 * Signals the start of a section.
 *
 * @param parameters map of attributes for the section (for example: style="background-color: blue")
 * @see org.xwiki.rendering.listener.HeaderLevel
 */
    void beginSection(@Default("") Map<String, String> parameters);

    /**
 * Signals the end of a section.
 *
 * @param parameters optional parameters for the section (for example, style="background-color: blue")
 * @see org.xwiki.rendering.listener.HeaderLevel
 */
    void endSection(@Default("") Map<String, String> parameters);

    /**
 * Signals the start of a header element.
 *
 * @param level the header level (1 = highest, 2 = next, ...)
 * @param id the header's unique identifier (anchor name) or null if none
 * @param parameters optional attributes for the header; e.g. style="background-color: blue"
 * @see org.xwiki.rendering.listener.HeaderLevel
 * @since 1.9M1
 */
    void beginHeader(@Default("1") HeaderLevel level, String id, @Default("") Map<String, String> parameters);

    /**
 * Signal the end of a header block.
 *
 * @param level the header level (1 = top-level, larger numbers indicate lower-level headers)
 * @param id the header unique identifier (may be null)
 * @param parameters additional header attributes (e.g., style properties); use {@link #EMPTY_PARAMETERS} when none
 * @see org.xwiki.rendering.listener.HeaderLevel
 * @since 1.9M1
 */
    void endHeader(@Default("1") HeaderLevel level, String id, @Default("") Map<String, String> parameters);

    /**
 * Signals the start of a macro marker that preserves a macro's original syntax and metadata so the marker can be
 * used to reconstruct the macro definition after execution.
 *
 * @param name the macro name
 * @param parameters the macro parameters (may be empty)
 * @param content the macro content
 * @param inline true if the macro appears inline (for example inside a paragraph), false if it is block-level
 */
    void beginMacroMarker(String name, @Default("") Map<String, String> parameters, String content, boolean inline);

    /**
 * Signals the end of a macro marker.
 *
 * @param name the macro identifier
 * @param parameters the macro's parameters (may be an empty map)
 * @param content the macro's content as it appeared in the source
 * @param inline true if the macro was located in inline content (for example inside a paragraph), false otherwise
 * @see #beginMacroMarker(String, java.util.Map, String, boolean)
 */
    void endMacroMarker(String name, @Default("") Map<String, String> parameters, String content, boolean inline);

    /**
 * Signals the start of a quotation block.
 *
 * @param parameters a map of parameters for the quotation (for example, "style" -> "background-color: blue")
 */
    void beginQuotation(@Default("") Map<String, String> parameters);

    /**
 * Signals the end of a quotation block.
 *
 * @param parameters map of attributes for the quotation (e.g., "style" -> "background-color: blue")
 */
    void endQuotation(@Default("") Map<String, String> parameters);

    /**
 * Signals the start of a line within a quotation block.
 */
    void beginQuotationLine();

    /**
     * End of a quotation line.
     */
    void endQuotationLine();

    /**
     * Signals the start of a figure block.
     *
     * @param parameters attributes for the figure (may be empty)
     */
    default void beginFigure(Map<String, String> parameters)
    {
        // No default implementation, needs to be implemented by Listener wanting to handle this event
    }

    /**
     * Signals the end of a figure block.
     *
     * @param parameters attributes associated with the figure; may be {@link #EMPTY_PARAMETERS} when none are provided
     */
    default void endFigure(Map<String, String> parameters)
    {
        // No default implementation, needs to be implemented by Listener wanting to handle this event
    }

    /**
     * Signals the start of a figure caption.
     *
     * @param parameters caption parameters (e.g., styling or attributes); may be empty
     */
    default void beginFigureCaption(Map<String, String> parameters)
    {
        // No default implementation, needs to be implemented by Listener wanting to handle this event
    }

    /**
     * Signals the end of a figure caption.
     *
     * @param parameters map of caption attributes (for example style, id, or other metadata); may be empty
     */
    default void endFigureCaption(Map<String, String> parameters)
    {
        // No default implementation, needs to be implemented by Listener wanting to handle this event
    }

    /**
 * Signals a new line or line break in the document.
 *
 * Implementations choose whether this is rendered as a literal newline or as a syntax-specific line break.
 */
    void onNewLine();

    /**
         * Signals the occurrence of a macro in the document.
         *
         * @param id the macro identifier (e.g., "toc")
         * @param parameters a map of macro parameters (name → value)
         * @param content the macro content, if any
         * @param inline true if the macro appears inside inline content (for example within a paragraph)
         * @since 1.6M2
         */
    void onMacro(String id, @Default("") Map<String, String> parameters, String content,
        @Name("inline") boolean inline);

    /**
 * Signals that a word token was encountered.
 *
 * @param word the text content of the word token
 */
    void onWord(String word);

    /**
 * Signals a space token encountered in the document.
 */
    void onSpace();

    /**
 * Signals that a non-alphanumeric symbol (for example: '*', '<', '>', '=', '"', ''' ) was encountered.
 *
 * @param symbol the symbol character encountered
 */
    void onSpecialSymbol(char symbol);

    /**
 * Signals a named reference (anchor) location within the document.
 *
 * <p>This marks a location that can be targeted by links; names are typically generated by macros.</p>
 *
 * @param name the reference name (anchor identifier)
 * @since 1.6M1
 */
    void onId(String name);

    /**
 * Signals a horizontal line (horizontal rule) element in the document.
 *
 * @param parameters map of optional parameters for the horizontal line (for example, style="background-color: blue")
 * @since 1.6M1
 */
    void onHorizontalLine(@Default("") Map<String, String> parameters);

    /**
 * Signal one or more empty lines between standalone blocks.
 *
 * Standalone blocks are blocks not contained within another block (for example: paragraph, standalone macro, list, table).
 *
 * @param count the number of consecutive empty lines between standalone blocks
 */
    void onEmptyLines(@Default("1") int count);

    /**
 * Signals a verbatim text segment that must not be parsed or interpreted.
 *
 * @param content the verbatim text content
 * @param inline  true when the content appears inline (for example inside a paragraph), false when standalone
 * @param parameters optional parameters (for example styling attributes) associated with the verbatim segment
 */
    void onVerbatim(@Name("content") String content, boolean inline, @Default("") Map<String, String> parameters);

    /**
 * Injects raw text into the listener output without parsing it.
 *
 * The provided text is delivered as-is; listener implementations may handle or ignore it. The
 * `syntax` parameter indicates the writing syntax of the raw text so implementations can decide
 * whether they can process the injected content.
 *
 * @param content the raw text to inject
 * @param syntax the syntax of the provided content
 */
    void onRawText(@Name("content") String content, Syntax syntax);
}
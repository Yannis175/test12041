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
import org.xwiki.rendering.listener.Listener;
import org.xwiki.rendering.listener.MetaData;
import org.xwiki.rendering.listener.reference.ResourceReference;
import org.xwiki.rendering.syntax.Syntax;

/**
 * Stores event types and offers a way to call a stored event.
 *
 * @version $Id$
 * @since 1.8RC1
 */
public enum EventType
{
    /**
     * @see Listener#beginDocument(org.xwiki.rendering.listener.MetaData)
     */
    BEGIN_DOCUMENT {
        /**
         * Dispatches a BEGIN_DOCUMENT event to the given listener.
         *
         * @param listener the listener to invoke
         * @param eventParameters expects a single element where {@code eventParameters[0]} is the {@link MetaData} for the document
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.beginDocument((MetaData) eventParameters[0]);
        }
    },
    /**
     * @see Listener#endDocument(org.xwiki.rendering.listener.MetaData)
     */
    END_DOCUMENT {
        /**
         * Invoke Listener#endDocument with the metadata supplied in the event parameters.
         *
         * @param eventParameters array whose element 0 is the {@code MetaData} to pass to {@code endDocument}
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.endDocument((MetaData) eventParameters[0]);
        }
    },
    /**
     * @see Listener#beginGroup(java.util.Map)
     */
    BEGIN_GROUP {
        /**
         * Dispatches a begin-group event to the given listener using the provided attributes.
         *
         * <p>Expects eventParameters[0] to be a Map&lt;String, String&gt; containing the group's attributes passed to
         * Listener.beginGroup.</p>
         *
         * @param listener the listener to notify
         * @param eventParameters varargs where the first element must be a Map&lt;String, String&gt; of group attributes
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.beginGroup((Map<String, String>) eventParameters[0]);
        }
    },
    /**
     * @see Listener#endGroup(java.util.Map)
     */
    END_GROUP {
        /**
         * Dispatches an end-group event to the specified listener using the first element of {@code eventParameters} as the group properties.
         *
         * @param listener the listener to notify
         * @param eventParameters varargs where the first element is a {@code Map<String, String>} containing group properties
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.endGroup((Map<String, String>) eventParameters[0]);
        }

        /**
         * Indicates the event marks the end of inline content.
         *
         * @return `true` if the event marks the end of inline content, `false` otherwise
         */
        @Override
        public boolean isInlineEnd()
        {
            return true;
        }
    },
    /**
     * @see Listener#beginParagraph(java.util.Map)
     */
    BEGIN_PARAGRAPH {
        /**
         * Dispatches a begin-paragraph event to the supplied listener.
         *
         * @param listener the listener that will receive the event
         * @param eventParameters varargs where index 0 is a Map<String, String> containing paragraph parameters
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.beginParagraph((Map<String, String>) eventParameters[0]);
        }
    },
    /**
     * @see Listener#endParagraph(java.util.Map)
     */
    END_PARAGRAPH {
        /**
         * Signal the end of a paragraph to the given listener.
         *
         * @param eventParameters an array whose first element is a Map&lt;String, String&gt; containing paragraph parameters
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.endParagraph((Map<String, String>) eventParameters[0]);
        }

        /**
         * Indicates the event marks the end of inline content.
         *
         * @return `true` if the event marks the end of inline content, `false` otherwise
         */
        @Override
        public boolean isInlineEnd()
        {
            return true;
        }
    },
    /**
     * @see Listener#beginDefinitionList(java.util.Map)
     */
    BEGIN_DEFINITION_LIST {
        /**
         * Dispatches a begin-definition-list event to the given listener.
         *
         * @param listener the listener that will receive the event
         * @param eventParameters expects the first element to be a {@code Map<String, String>} of parameters for the definition list
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.beginDefinitionList((Map<String, String>) eventParameters[0]);
        }

        /**
         * Indicates that this event ends inline mode for nested definition lists.
         *
         * @return {@code true} if the event should be treated as an inline-end (signals the end of inline content for nested
         *         definition lists), {@code false} otherwise
         */
        @Override
        public boolean isInlineEnd()
        {
            // This is because for nested definition lists, the event after a definition list item content is a new
            // definition list
            return true;
        }
    },
    /**
     * @see Listener#endDefinitionList(java.util.Map)
     */
    END_DEFINITION_LIST {
        /**
         * Signals the end of a definition list on the given listener.
         *
         * @param listener the listener to notify
         * @param eventParameters an array whose first element (index 0) is a Map<String, String> of parameters for the definition list
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.endDefinitionList((Map<String, String>) eventParameters[0]);
        }
    },
    /**
     * @see Listener#beginDefinitionTerm()
     */
    BEGIN_DEFINITION_TERM {
        /**
         * Signals the start of a definition term on the provided listener.
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.beginDefinitionTerm();
        }
    },
    /**
     * @see Listener#endDefinitionTerm()
     */
    END_DEFINITION_TERM {
        /**
         * Signals the end of a definition term to the provided listener.
         *
         * @param listener the listener that will receive the end-definition-term event
         * @param eventParameters ignored
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.endDefinitionTerm();
        }

        /**
         * Indicates the event marks the end of inline content.
         *
         * @return `true` if the event marks the end of inline content, `false` otherwise
         */
        @Override
        public boolean isInlineEnd()
        {
            return true;
        }
    },
    /**
     * @see Listener#beginDefinitionDescription()
     */
    BEGIN_DEFINITION_DESCRIPTION {
        /**
         * Starts a definition description event on the given listener.
         *
         * @param listener the listener to notify
         * @param eventParameters ignored
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.beginDefinitionDescription();
        }
    },
    /**
     * @see Listener#endDefinitionDescription()
     */
    END_DEFINITION_DESCRIPTION {
        /**
         * Signals the end of a definition description to the given listener.
         *
         * Invokes {@code listener.endDefinitionDescription()}; any provided {@code eventParameters} are ignored.
         *
         * @param listener the listener to notify
         * @param eventParameters optional parameters (ignored)
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.endDefinitionDescription();
        }

        /**
         * Indicates the event marks the end of inline content.
         *
         * @return `true` if the event marks the end of inline content, `false` otherwise
         */
        @Override
        public boolean isInlineEnd()
        {
            return true;
        }
    },
    /**
     * @see Listener#beginFormat(org.xwiki.rendering.listener.Format, java.util.Map)
     */
    BEGIN_FORMAT {
        /**
         * Dispatches a "begin format" event to the provided listener.
         *
         * @param listener the listener to notify
         * @param eventParameters event-specific arguments: index 0 — the {@link Format} to begin; index 1 — a {@link Map}
         *                        of parameters for the format
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.beginFormat((Format) eventParameters[0], (Map<String, String>) eventParameters[1]);
        }
    },
    /**
     * @see Listener#endFormat(org.xwiki.rendering.listener.Format, java.util.Map)
     */
    END_FORMAT {
        /**
         * Invokes the listener to signal the end of a format span.
         *
         * @param eventParameters the event arguments:
         *                        index 0 - the {@link Format} being ended;
         *                        index 1 - a {@link Map} of formatting parameters (String key/value)
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.endFormat((Format) eventParameters[0], (Map<String, String>) eventParameters[1]);
        }
    },
    /**
     * @see Listener#beginHeader(org.xwiki.rendering.listener.HeaderLevel, String, java.util.Map)
     */
    BEGIN_HEADER {
        /**
         * Dispatches a beginHeader event to the given listener using the provided parameters.
         *
         * @param listener the listener to notify
         * @param eventParameters an array containing the header parameters in order:
         *                        index 0 — the {@code HeaderLevel},
         *                        index 1 — the header title {@code String},
         *                        index 2 — a {@code Map<String, String>} of header parameters
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.beginHeader((HeaderLevel) eventParameters[0], (String) eventParameters[1],
                (Map<String, String>) eventParameters[2]);
        }
    },
    /**
     * @see Listener#endHeader(org.xwiki.rendering.listener.HeaderLevel, String, java.util.Map)
     */
    END_HEADER {
        /**
         * Dispatches an end-of-header event to the provided listener.
         *
         * @param eventParameters an array with the event arguments in order:
         *                        0 - the header level ({@code HeaderLevel}),
         *                        1 - the header identifier ({@code String}),
         *                        2 - the header parameters ({@code Map<String, String>})
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.endHeader((HeaderLevel) eventParameters[0], (String) eventParameters[1],
                (Map<String, String>) eventParameters[2]);
        }
    },
    /**
     * @see Listener#beginLink(org.xwiki.rendering.listener.reference.ResourceReference, boolean, java.util.Map)
     */
    BEGIN_LINK {
        /**
         * Dispatches a link-begin event to the provided listener.
         *
         * <p>Expected eventParameters (in order):</p>
         * <ol>
         *   <li>{@link org.xwiki.rendering.listener.chaining.ResourceReference} - the link target</li>
         *   <li>{@link java.lang.Boolean} - a boolean flag passed to the listener</li>
         *   <li>{@link java.util.Map}&lt;String, String&gt; - link parameters</li>
         * </ol>
         *
         * @param listener the listener that will receive the event
         * @param eventParameters ordered parameters required by the listener (see description)
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.beginLink((ResourceReference) eventParameters[0], (Boolean) eventParameters[1],
                (Map<String, String>) eventParameters[2]);
        }
    },
    /**
     * @see Listener#endLink(org.xwiki.rendering.listener.reference.ResourceReference, boolean, java.util.Map)
     */
    END_LINK {
        /**
         * Fires an end-link event on the given listener using the supplied parameters.
         *
         * @param listener the listener to notify
         * @param eventParameters an array containing event data in the following order:
         *        index 0 — the link's {@code ResourceReference};
         *        index 1 — a {@code Boolean} flag associated with the link;
         *        index 2 — a {@code Map<String, String>} of link parameters
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.endLink((ResourceReference) eventParameters[0], (Boolean) eventParameters[1],
                (Map<String, String>) eventParameters[2]);
        }
    },
    /**
     * @see Listener#beginList(org.xwiki.rendering.listener.ListType, java.util.Map)
     */
    BEGIN_LIST {
        /**
         * Dispatches a begin-list event to the given listener.
         *
         * Expects eventParameters to contain:
         * <ol>
         *   <li>index 0: a {@link ListType} identifying the list type</li>
         *   <li>index 1: a {@code Map<String, String>} of parameters for the list</li>
         * </ol>
         *
         * @param listener the listener to receive the event
         * @param eventParameters positional parameters for the event as described above
         * @throws ClassCastException if the elements of {@code eventParameters} are not of the expected types
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.beginList((ListType) eventParameters[0], (Map<String, String>) eventParameters[1]);
        }

        /**
         * Indicates that this event ends inline mode, which is required to handle nested lists.
         *
         * @return `true` to indicate the event ends inline mode
         */
        @Override
        public boolean isInlineEnd()
        {
            // This is because for nested lists, the event after list item content is a new list
            return true;
        }
    },
    /**
     * @see Listener#endList(org.xwiki.rendering.listener.ListType, java.util.Map)
     */
    END_LIST {
        /**
         * Dispatches an "end list" event to the given listener using the provided event parameters.
         *
         * @param eventParameters the event arguments where the first element is the {@link ListType} of the list and the
         *                        second element is a {@link Map}{@code <String, String>} of list parameters/attributes
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.endList((ListType) eventParameters[0], (Map<String, String>) eventParameters[1]);
        }
    },
    /**
     * @see org.xwiki.rendering.listener.Listener#beginListItem()
     */
    BEGIN_LIST_ITEM {
        /**
         * Dispatches a begin-list-item event to the provided listener, using an attributes map if present.
         *
         * @param eventParameters optional event parameters where the first element, if present, is a {@code Map<String,String>}
         *                        of attributes to pass to {@code listener.beginListItem(Map)}; if absent, {@code listener.beginListItem()}
         *                        is invoked instead
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            if (eventParameters.length > 0) {
                listener.beginListItem((Map<String, String>) eventParameters[0]);
            } else {
                listener.beginListItem();
            }
        }
    },
    /**
     * @see org.xwiki.rendering.listener.Listener#endListItem()
     */
    END_LIST_ITEM {
        /**
         * Dispatches an end-list-item event to the provided listener.
         *
         * <p>If an event parameter is provided, the first element is treated as a {@code Map<String, String>} and passed
         * to {@link Listener#endListItem(Map)}; otherwise {@link Listener#endListItem()} is invoked.</p>
         *
         * @param eventParameters optional parameters for the event; when present the first element must be a {@code Map<String,String>}
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            if (eventParameters.length > 0) {
                listener.endListItem((Map<String, String>) eventParameters[0]);
            } else {
                listener.endListItem();
            }
        }

        /**
         * Indicates the event marks the end of inline content.
         *
         * @return `true` if the event marks the end of inline content, `false` otherwise
         */
        @Override
        public boolean isInlineEnd()
        {
            return true;
        }
    },
    /**
     * @see Listener#beginMacroMarker(String, java.util.Map, String, boolean)
     */
    BEGIN_MACRO_MARKER {
        /**
         * Dispatches a begin-macro-marker event to the given listener.
         *
         * @param listener the listener to receive the event
         * @param eventParameters an array with the event parameters in order:
         *     0: the macro id/name as a {@link String};
         *     1: the macro parameters as a {@link Map}{@code <String, String>};
         *     2: the macro content as a {@link String};
         *     3: a {@link Boolean} indicating whether the macro is standalone
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.beginMacroMarker((String) eventParameters[0], (Map<String, String>) eventParameters[1],
                (String) eventParameters[2], (Boolean) eventParameters[3]);
        }
    },
    /**
     * @see Listener#endMacroMarker(String, java.util.Map, String, boolean)
     */
    END_MACRO_MARKER {
        /**
         * Dispatches an end-macro-marker event to the specified listener using the provided parameters.
         *
         * @param eventParameters an array with the expected elements in order:
         *        [0] the macro identifier (`String`),
         *        [1] the macro parameters (`Map<String, String>`),
         *        [2] the macro content (`String`),
         *        [3] a flag indicating inline mode (`Boolean`)
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.endMacroMarker((String) eventParameters[0], (Map<String, String>) eventParameters[1],
                (String) eventParameters[2], (Boolean) eventParameters[3]);
        }
    },
    /**
     * @see Listener#beginQuotation(java.util.Map)
     */
    BEGIN_QUOTATION {
        /**
         * Triggers a quotation start on the given listener using the attributes provided in the parameters.
         *
         * @param listener the listener to notify
         * @param eventParameters an array where {@code eventParameters[0]} is a {@code Map<String, String>} of quotation attributes
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.beginQuotation((Map<String, String>) eventParameters[0]);
        }

        /**
         * Indicates that this event ends inline mode for nested quotations.
         *
         * @return `true` if the event ends inline mode for the listener, `false` otherwise.
         */
        @Override
        public boolean isInlineEnd()
        {
            // This is because for nested quotations, the event after a quotation line is a new quotation
            return true;
        }
    },
    /**
     * @see Listener#endQuotation(java.util.Map)
     */
    END_QUOTATION {
        /**
         * Dispatches the END_QUOTATION event to the given listener by calling its {@code endQuotation} method.
         *
         * @param eventParameters an array where {@code eventParameters[0]} is a {@code Map<String, String>} of quotation
         *                        attributes
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.endQuotation((Map<String, String>) eventParameters[0]);
        }
    },
    /**
     * @see org.xwiki.rendering.listener.Listener#beginQuotationLine()
     */
    BEGIN_QUOTATION_LINE {
        /**
         * Dispatches a begin-quotation-line event to the provided listener.
         *
         * @param eventParameters ignored; this event does not expect any parameters
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.beginQuotationLine();
        }
    },
    /**
     * @see org.xwiki.rendering.listener.Listener#endQuotationLine()
     */
    END_QUOTATION_LINE {
        /**
         * Dispatches the end of a quotation line to the provided listener.
         *
         * @param listener the listener to notify
         * @param eventParameters ignored; this event does not accept parameters
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.endQuotationLine();
        }

        /**
         * Indicates the event marks the end of inline content.
         *
         * @return `true` if the event marks the end of inline content, `false` otherwise
         */
        @Override
        public boolean isInlineEnd()
        {
            return true;
        }
    },
    /**
     * @see Listener#beginSection(java.util.Map)
     */
    BEGIN_SECTION {
        /**
         * Dispatches a begin-section event to the provided listener.
         *
         * @param eventParameters an array where the first element is a {@code Map<String, String>} of section parameters
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.beginSection((Map<String, String>) eventParameters[0]);
        }
    },
    /**
     * @see Listener#endSection(java.util.Map)
     */
    END_SECTION {
        /**
         * Dispatches an end-of-section event to the given listener.
         *
         * @param listener the listener to receive the event
         * @param eventParameters the event parameters; expects a {@code Map<String, String>} at index 0 containing section parameters
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.endSection((Map<String, String>) eventParameters[0]);
        }
    },
    /**
     * @see Listener#beginTable(java.util.Map)
     */
    BEGIN_TABLE {
        /**
         * Dispatches a table-begin event to the provided listener.
         *
         * @param eventParameters first element must be a Map<String, String> containing table parameters passed to {@link Listener#beginTable(Map)}
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.beginTable((Map<String, String>) eventParameters[0]);
        }
    },
    /**
     * @see Listener#endTable(java.util.Map)
     */
    END_TABLE {
        /**
         * Invoke the listener to signal the end of a table.
         *
         * @param listener the listener to notify
         * @param eventParameters varargs where the first element is a Map&lt;String, String&gt; of table parameters/attributes
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.endTable((Map<String, String>) eventParameters[0]);
        }
    },
    /**
     * @see Listener#beginTableCell(java.util.Map)
     */
    BEGIN_TABLE_CELL {
        /**
         * Dispatches a begin-table-cell event to the provided listener.
         *
         * @param listener the listener that will receive the beginTableCell call
         * @param eventParameters expects at index 0 a {@code Map<String, String>} of cell parameters/attributes passed to the listener
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.beginTableCell((Map<String, String>) eventParameters[0]);
        }
    },
    /**
     * @see Listener#endTableCell(java.util.Map)
     */
    END_TABLE_CELL {
        /**
         * Dispatches an end-of-table-cell event to the given listener.
         *
         * @param listener the listener to notify
         * @param eventParameters the event parameters where index 0 is a {@code Map<String,String>} of the table cell's attributes
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.endTableCell((Map<String, String>) eventParameters[0]);
        }

        /**
         * Indicates the event marks the end of inline content.
         *
         * @return `true` if the event marks the end of inline content, `false` otherwise
         */
        @Override
        public boolean isInlineEnd()
        {
            return true;
        }
    },
    /**
     * @see Listener#beginTableHeadCell(java.util.Map)
     */
    BEGIN_TABLE_HEAD_CELL {
        /**
         * Dispatches a begin-table-head-cell event to the given listener.
         *
         * @param eventParameters the event parameters; element 0 must be a {@code Map<String, String>} of cell parameters/attributes
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.beginTableHeadCell((Map<String, String>) eventParameters[0]);
        }
    },
    /**
     * @see Listener#endTableHeadCell(java.util.Map)
     */
    END_TABLE_HEAD_CELL {
        /**
         * Dispatches an end-table-head-cell event to the given listener.
         *
         * @param eventParameters the event arguments; element 0 must be a {@code Map<String, String>} containing the
         *                        attributes for the table head cell
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.endTableHeadCell((Map<String, String>) eventParameters[0]);
        }

        /**
         * Indicates the event marks the end of inline content.
         *
         * @return `true` if the event marks the end of inline content, `false` otherwise
         */
        @Override
        public boolean isInlineEnd()
        {
            return true;
        }
    },
    /**
     * @see Listener#beginTableRow(java.util.Map)
     */
    BEGIN_TABLE_ROW {
        /**
         * Dispatches a beginTableRow event to the given listener.
         *
         * @param listener the listener that will receive the event
         * @param eventParameters eventParameters[0] must be a Map<String, String> containing the table row's parameters/attributes
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.beginTableRow((Map<String, String>) eventParameters[0]);
        }
    },
    /**
     * @see Listener#endTableRow(java.util.Map)
     */
    END_TABLE_ROW {
        /**
         * Calls {@code listener.endTableRow(...)} using the first element of {@code eventParameters} as the table row attributes.
         *
         * @param eventParameters the first element must be a {@code Map<String, String>} containing attributes for the table row
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.endTableRow((Map<String, String>) eventParameters[0]);
        }
    },
    /**
     * @see Listener#beginMetaData(org.xwiki.rendering.listener.MetaData)
     */
    BEGIN_METADATA {
        /**
         * Dispatches a begin-metadata event to the listener.
         *
         * @param eventParameters the event arguments; the first element must be a {@link MetaData} passed to the listener
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.beginMetaData((MetaData) eventParameters[0]);
        }
    },
    /**
     * @see Listener#endMetaData(org.xwiki.rendering.listener.MetaData)
     */
    END_METADATA {
        /**
         * Dispatches an end metadata event to the given listener.
         *
         * @param listener the listener that will receive the end metadata event
         * @param eventParameters varargs where the first element is the {@link MetaData} instance for the event
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.endMetaData((MetaData) eventParameters[0]);
        }
    },
    /**
     * @see Listener#onRawText(String, org.xwiki.rendering.syntax.Syntax)
     */
    ON_RAW_TEXT {
        /**
         * Dispatches a raw-text event to the listener.
         *
         * @param listener the listener that will receive the raw-text event
         * @param eventParameters expected to contain the raw text at index 0 (`String`) and the text `Syntax` at index 1
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.onRawText((String) eventParameters[0], (Syntax) eventParameters[1]);
        }
    },
    /**
     * @see Listener#onEmptyLines(int)
     */
    ON_EMPTY_LINES {
        /**
         * Dispatches an empty-lines event to the given listener.
         *
         * @param eventParameters the event arguments where {@code eventParameters[0]} is an {@link Integer} specifying
         *                        the number of consecutive empty lines; must contain at least one element
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.onEmptyLines((Integer) eventParameters[0]);
        }
    },
    /**
     * @see Listener#onHorizontalLine(java.util.Map)
     */
    ON_HORIZONTAL_LINE {
        /**
         * Dispatches a horizontal line event to the provided listener.
         *
         * <p>Expects eventParameters[0] to be a map of attributes for the horizontal line.</p>
         *
         * @param listener the listener to invoke
         * @param eventParameters an array where the first element is a {@code Map<String, String>} of horizontal-line attributes
         * @throws ClassCastException if the first event parameter is not a {@code Map<String, String>}
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.onHorizontalLine((Map<String, String>) eventParameters[0]);
        }
    },
    /**
     * @see Listener#onId(String)
     */
    ON_ID {
        /**
         * Dispatches an identifier event to the provided listener using the first element of {@code eventParameters} as the identifier.
         *
         * @param eventParameters the event arguments where {@code eventParameters[0]} is the identifier {@code String}
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.onId((String) eventParameters[0]);
        }
    },
    /**
     * @see Listener#onImage(org.xwiki.rendering.listener.reference.ResourceReference, boolean, java.util.Map)
     */
    ON_IMAGE {
        /**
         * Dispatches an image event to the given listener, supporting both image signatures with and without alt text.
         *
         * <p>If {@code eventParameters.length == 4} the parameters are interpreted as:
         * {@code (ResourceReference src, Boolean isFreeStanding, String altText, Map<String, String> parameters)}.
         * Otherwise they are interpreted as:
         * {@code (ResourceReference src, Boolean isFreeStanding, Map<String, String> parameters)}.
         *
         * @param listener the listener that will receive the image event
         * @param eventParameters the event arguments as described above (expected types and order must match one of the two forms)
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            if (eventParameters.length == 4) {
                listener.onImage((ResourceReference) eventParameters[0], (Boolean) eventParameters[1],
                    (String) eventParameters[2], (Map<String, String>) eventParameters[3]);
            } else {
                listener.onImage((ResourceReference) eventParameters[0], (Boolean) eventParameters[1],
                    (Map<String, String>) eventParameters[2]);
            }
        }
    },
    /**
     * @see Listener#onMacro(String, java.util.Map, String, boolean)
     */
    ON_MACRO {
        /**
         * Dispatches an onMacro event to the provided listener using the supplied event parameters.
         *
         * @param eventParameters ordered parameters:
         *        0 - macro id (`String`),
         *        1 - macro parameters (`Map<String, String>`),
         *        2 - macro content (`String`),
         *        3 - whether the macro is inline (`Boolean`)
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.onMacro((String) eventParameters[0], (Map<String, String>) eventParameters[1],
                (String) eventParameters[2], (Boolean) eventParameters[3]);
        }
    },
    /**
     * @see org.xwiki.rendering.listener.Listener#onNewLine()
     */
    ON_NEW_LINE {
        /**
         * Invoke the listener to signal a new line.
         *
         * @param eventParameters additional parameters; ignored by this event
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.onNewLine();
        }
    },
    /**
     * @see org.xwiki.rendering.listener.Listener#onSpace()
     */
    ON_SPACE {
        /**
         * Dispatches a space event to the given listener.
         *
         * Any provided eventParameters are ignored.
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.onSpace();
        }
    },
    /**
     * @see Listener#onSpecialSymbol(char)
     */
    ON_SPECIAL_SYMBOL {
        /**
         * Dispatches an onSpecialSymbol event to the given listener using the provided symbol.
         *
         * @param eventParameters the event arguments; the first element (eventParameters[0]) must be a {@link Character}
         *                        representing the special symbol to emit (any additional elements are ignored)
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.onSpecialSymbol((Character) eventParameters[0]);
        }
    },
    /**
     * @see Listener#onVerbatim(String, boolean, java.util.Map)
     */
    ON_VERBATIM {
        /**
         * Dispatches a verbatim text event to the given listener.
         *
         * @param eventParameters the event arguments:
         *                        index 0 — the verbatim text (`String`),
         *                        index 1 — `Boolean` indicating whether the verbatim is inline,
         *                        index 2 — a `Map<String, String>` of parameters
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.onVerbatim((String) eventParameters[0], (Boolean) eventParameters[1],
                (Map<String, String>) eventParameters[2]);
        }
    },
    /**
     * @see Listener#onWord(String)
     */
    ON_WORD {
        /**
         * Dispatches a word event to the provided Listener.
         *
         * @param eventParameters varargs where the first element must be the word (a `String`) passed to {@link Listener#onWord(String)}
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.onWord((String) eventParameters[0]);
        }
    },
    /**
     * @see Listener#beginFigure(Map)
     */
    BEGIN_FIGURE {
        /**
         * Dispatches a beginFigure event to the given listener.
         *
         * @param listener the listener that will receive the event
         * @param eventParameters first element must be a {@link java.util.Map}&lt;String, String&gt; containing the figure's parameters
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.beginFigure((Map<String, String>) eventParameters[0]);
        }
    },
    /**
     * @see Listener#beginFigureCaption(Map)
     */
    BEGIN_FIGURE_CAPTION {
        /**
         * Dispatches the BEGIN_FIGURE_CAPTION event to the given listener.
         *
         * @param eventParameters the event arguments where {@code eventParameters[0]} is a {@code Map<String, String>}
         *                        containing the figure caption's parameters
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.beginFigureCaption((Map<String, String>) eventParameters[0]);
        }
    },
    /**
     * @see Listener#endFigure(Map)
     */
    END_FIGURE {
        /**
         * Notify the listener that the current figure has ended.
         *
         * @param listener the listener to notify
         * @param eventParameters expects at index 0 a {@code Map<String, String>} of figure parameters passed to {@code listener.endFigure(...)}
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.endFigure((Map<String, String>) eventParameters[0]);
        }
    },
    /**
     * @see Listener#endFigureCaption(Map)
     */
    END_FIGURE_CAPTION {
        /**
         * Ends the current figure caption on the provided listener.
         *
         * @param eventParameters event parameters where index 0 is a Map&lt;String, String&gt; of caption parameters
         */
        @Override
        public void fireEvent(Listener listener, Object... eventParameters)
        {
            listener.endFigureCaption((Map<String, String>) eventParameters[0]);
        }
    };

    /**
 * Dispatches this event to the given listener using the provided parameters.
 *
 * Implementations invoke the corresponding Listener method; the required number, order and types of
 * elements in {@code eventParameters} depend on the specific EventType.
 *
 * @param listener the listener that will receive the event
 * @param eventParameters the parameters required by the event (their number, order and types vary by EventType)
 */
    public abstract void fireEvent(Listener listener, Object... eventParameters);

    /**
     * Indicates whether this event ends inline mode.
     *
     * @return `true` if the event ends inline mode, `false` otherwise
     */
    public boolean isInlineEnd()
    {
        return false;
    }
}
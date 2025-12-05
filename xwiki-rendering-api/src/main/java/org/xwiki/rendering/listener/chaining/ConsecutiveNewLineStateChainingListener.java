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
import org.xwiki.rendering.listener.reference.ResourceReference;
import org.xwiki.rendering.syntax.Syntax;

/**
 * Counts consecutive new lines.
 *
 * @version $Id; $
 * @since 1.8RC1
 */
public class ConsecutiveNewLineStateChainingListener extends AbstractChainingListener
    implements StackableChainingListener
{
    /**
     * Number of found new lines.
     */
    private int newLineCount;

    /**
     * Creates a chaining listener that delegates rendering events to the provided listener chain
     * and tracks consecutive new-line occurrences.
     *
     * @param listenerChain the listener chain to which events will be delegated
     */
    public ConsecutiveNewLineStateChainingListener(ListenerChain listenerChain)
    {
        setListenerChain(listenerChain);
    }

    /**
     * Create a new instance of this chaining listener that shares the current listener chain.
     *
     * @return a new ConsecutiveNewLineStateChainingListener initialized with the current listener chain
     */
    @Override
    public StackableChainingListener createChainingListenerInstance()
    {
        return new ConsecutiveNewLineStateChainingListener(getListenerChain());
    }

    /**
     * Report the number of consecutive new-line events encountered.
     *
     * @return the current count of consecutive new lines
     */
    public int getNewLineCount()
    {
        return this.newLineCount;
    }

    /**
     * Resets the consecutive new-line counter when a definition description ends.
     */
    @Override
    public void endDefinitionDescription()
    {
        this.newLineCount = 0;
        super.endDefinitionDescription();
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.0RC1
     */
    @Override
    public void endDefinitionList(Map<String, String> parameters)
    {
        this.newLineCount = 0;
        super.endDefinitionList(parameters);
    }

    /**
     * Signals the end of a definition list term and resets the internal consecutive new-line counter.
     *
     * <p>Resets {@code newLineCount} to 0 and forwards the end-of-term event to the chained listener.
     */
    @Override
    public void endDefinitionTerm()
    {
        this.newLineCount = 0;
        super.endDefinitionTerm();
    }

    /**
     * {@inheritDoc}
     *
     * @since 3.0M2
     */
    @Override
    public void endDocument(MetaData metadata)
    {
        this.newLineCount = 0;
        super.endDocument(metadata);
    }

    /**
     * Reset the consecutive new-line counter and end the current group.
     *
     * @param parameters the group's parameters (may be null or empty)
     */
    @Override
    public void endGroup(Map<String, String> parameters)
    {
        this.newLineCount = 0;
        super.endGroup(parameters);
    }

    /**
     * Reset the consecutive new-line counter when a formatting block ends.
     *
     * @param format the format that ended
     * @param parameters parameters associated with the format, may be empty
     */
    @Override
    public void endFormat(Format format, Map<String, String> parameters)
    {
        this.newLineCount = 0;
        super.endFormat(format, parameters);
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.5RC1
     */
    @Override
    public void endLink(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        this.newLineCount = 0;
        super.endLink(reference, freestanding, parameters);
    }

    /**
     * Handle the end of a list and reset the internal consecutive new-line counter.
     *
     * Resets the counter used to track consecutive new-line events so subsequent events are counted from zero.
     *
     * @param type the type of the list that ended
     * @param parameters optional parameters associated with the list event
     */
    @Override
    public void endList(ListType type, Map<String, String> parameters)
    {
        this.newLineCount = 0;
        super.endList(type, parameters);
    }

    /**
     * Signals the end of a list item and resets the consecutive new-line counter.
     */
    @Override
    public void endListItem()
    {
        this.newLineCount = 0;
        super.endListItem();
    }

    /**
     * Signal the end of a list item and reset the count of consecutive new lines.
     *
     * @param parameters map of parameters associated with the list item
     */
    @Override
    public void endListItem(Map<String, String> parameters)
    {
        this.newLineCount = 0;
        super.endListItem(parameters);
    }

    /**
     * Handles the end of a macro marker and resets the consecutive new-line counter.
     *
     * @param name the macro name
     * @param parameters the macro parameters
     * @param content the macro content, or null if none
     * @param isInline true if the macro is inline, false if it is a block macro
     */
    @Override
    public void endMacroMarker(String name, Map<String, String> parameters, String content, boolean isInline)
    {
        this.newLineCount = 0;
        super.endMacroMarker(name, parameters, content, isInline);
    }

    /**
     * Handle the end of a paragraph and reset the consecutive new-line counter.
     *
     * @param parameters paragraph parameters (attributes) associated with this paragraph, if any
     */
    @Override
    public void endParagraph(Map<String, String> parameters)
    {
        this.newLineCount = 0;
        super.endParagraph(parameters);
    }

    /**
     * Signals the end of a quotation block and resets the listener's consecutive-new-line counter.
     *
     * @param parameters the parameters associated with the quotation block, may be {@code null}
     */
    @Override
    public void endQuotation(Map<String, String> parameters)
    {
        this.newLineCount = 0;
        super.endQuotation(parameters);
    }

    /**
     * Resets the consecutive new-line counter when a quotation line ends.
     *
     * Delegates standard end-of-quotation-line processing to the superclass.
     */
    @Override
    public void endQuotationLine()
    {
        this.newLineCount = 0;
        super.endQuotationLine();
    }

    /**
     * Reset the consecutive new-line counter when a header ends.
     *
     * @param level the header level
     * @param id the header identifier, or {@code null} if none
     * @param parameters additional event parameters
     */
    @Override
    public void endHeader(HeaderLevel level, String id, Map<String, String> parameters)
    {
        this.newLineCount = 0;
        super.endHeader(level, id, parameters);
    }

    /**
     * Reset the consecutive new-line counter and handle the end of a table element.
     *
     * @param parameters rendering parameters for the table element (may be empty)
     */
    @Override
    public void endTable(Map<String, String> parameters)
    {
        this.newLineCount = 0;
        super.endTable(parameters);
    }

    /**
     * Reset the consecutive new-line counter and notify the listener chain that a table cell has ended.
     *
     * @param parameters rendering parameters associated with the table cell (may be null or empty)
     */
    @Override
    public void endTableCell(Map<String, String> parameters)
    {
        this.newLineCount = 0;
        super.endTableCell(parameters);
    }

    /**
     * Reset the consecutive new-line counter and mark the end of a table header cell.
     *
     * @param parameters additional parameters for the table header cell (may be empty)
     */
    @Override
    public void endTableHeadCell(Map<String, String> parameters)
    {
        this.newLineCount = 0;
        super.endTableHeadCell(parameters);
    }

    /**
     * Resets the consecutive-newline counter when a table row ends.
     *
     * @param parameters the parameters of the table row
     */
    @Override
    public void endTableRow(Map<String, String> parameters)
    {
        this.newLineCount = 0;
        super.endTableRow(parameters);
    }

    /**
     * Reset the consecutive new-line counter when a figure element ends.
     *
     * @param parameters the parameters associated with the figure, if any
     */
    @Override
    public void endFigure(Map<String, String> parameters)
    {
        this.newLineCount = 0;
        super.endFigure(parameters);
    }

    /**
     * Called when a figure caption ends and resets the internal consecutive new-line counter.
     *
     * @param parameters rendering parameters for the figure caption
     */
    @Override
    public void endFigureCaption(Map<String, String> parameters)
    {
        this.newLineCount = 0;
        super.endFigureCaption(parameters);
    }

    /**
     * Handles a raw text event and resets the consecutive new-line counter.
     *
     * @param text the raw text content
     * @param syntax the syntax associated with the text
     */
    @Override
    public void onRawText(String text, Syntax syntax)
    {
        this.newLineCount = 0;
        super.onRawText(text, syntax);
    }

    /**
     * Reset the consecutive new-line counter and handle a run of empty lines.
     *
     * @param count the number of consecutive empty lines encountered
     */
    @Override
    public void onEmptyLines(int count)
    {
        this.newLineCount = 0;
        super.onEmptyLines(count);
    }

    /**
     * Handles a horizontal line event and resets the consecutive-new-line counter.
     *
     * @param parameters event parameters for the horizontal line
     */
    @Override
    public void onHorizontalLine(Map<String, String> parameters)
    {
        this.newLineCount = 0;
        super.onHorizontalLine(parameters);
    }

    /**
     * Processes an identifier token and resets the consecutive new-line counter for this listener.
     *
     * @param name the identifier name
     */
    @Override
    public void onId(String name)
    {
        this.newLineCount = 0;
        super.onId(name);
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.5RC1
     */
    @Override
    public void onImage(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        this.newLineCount = 0;
        super.onImage(reference, freestanding, parameters);
    }

    /**
     * Reset the consecutive new-line counter and forward an image event to the chained listener.
     *
     * @param reference   the image resource reference
     * @param freestanding whether the image is freestanding (block) or inline
     * @param id          an optional identifier for the image (may be null)
     * @param parameters  additional image parameters
     */
    @Override
    public void onImage(ResourceReference reference, boolean freestanding, String id, Map<String, String> parameters)
    {
        this.newLineCount = 0;
        super.onImage(reference, freestanding, id, parameters);
    }

    /**
     * Records a new-line event by incrementing the consecutive new-line counter.
     *
     * <p>Forwards the new-line event to the chained listener after updating the counter.</p>
     */
    @Override
    public void onNewLine()
    {
        this.newLineCount++;
        super.onNewLine();
    }

    /**
     * Resets the consecutive new-line counter when a space is encountered.
     *
     * <p>Sets the internal consecutive new-line count to 0 and continues normal space handling.</p>
     */
    @Override
    public void onSpace()
    {
        this.newLineCount = 0;
        super.onSpace();
    }

    /**
     * Handles a special symbol rendering event and resets the consecutive new-line counter.
     *
     * @param symbol the special character encountered
     */
    @Override
    public void onSpecialSymbol(char symbol)
    {
        this.newLineCount = 0;
        super.onSpecialSymbol(symbol);
    }

    /**
     * Resets the consecutive new-line counter when a macro is encountered and forwards the macro event for normal handling.
     *
     * @param id the macro identifier
     * @param parameters the macro parameters, or {@code null} if none
     * @param content the macro content, or {@code null} if none
     * @param inline {@code true} if the macro is inline, {@code false} if it is block-level
     */
    @Override
    public void onMacro(String id, Map<String, String> parameters, String content, boolean inline)
    {
        this.newLineCount = 0;
        super.onMacro(id, parameters, content, inline);
    }

    /**
     * Handles a verbatim block by resetting the consecutive-new-line counter and delegating the event.
     *
     * @param content    the verbatim text content
     * @param inline     true if the verbatim content is inline, false if it is a block
     * @param parameters additional rendering parameters for the verbatim content
     */
    @Override
    public void onVerbatim(String content, boolean inline, Map<String, String> parameters)
    {
        this.newLineCount = 0;
        super.onVerbatim(content, inline, parameters);
    }

    /**
     * Processes a word rendering event and resets the consecutive new-line counter.
     *
     * @param word the word text encountered
     */
    @Override
    public void onWord(String word)
    {
        this.newLineCount = 0;
        super.onWord(word);
    }
}
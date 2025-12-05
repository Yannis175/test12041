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
package org.xwiki.rendering.internal.parser;

import java.util.Map;

import org.xwiki.rendering.block.BulletedListBlock;
import org.xwiki.rendering.block.DefinitionDescriptionBlock;
import org.xwiki.rendering.block.DefinitionListBlock;
import org.xwiki.rendering.block.DefinitionTermBlock;
import org.xwiki.rendering.block.EmptyLinesBlock;
import org.xwiki.rendering.block.FigureBlock;
import org.xwiki.rendering.block.FigureCaptionBlock;
import org.xwiki.rendering.block.FormatBlock;
import org.xwiki.rendering.block.GroupBlock;
import org.xwiki.rendering.block.HeaderBlock;
import org.xwiki.rendering.block.HorizontalLineBlock;
import org.xwiki.rendering.block.IdBlock;
import org.xwiki.rendering.block.ImageBlock;
import org.xwiki.rendering.block.LinkBlock;
import org.xwiki.rendering.block.ListItemBlock;
import org.xwiki.rendering.block.MacroBlock;
import org.xwiki.rendering.block.MacroMarkerBlock;
import org.xwiki.rendering.block.MetaDataBlock;
import org.xwiki.rendering.block.NewLineBlock;
import org.xwiki.rendering.block.NumberedListBlock;
import org.xwiki.rendering.block.ParagraphBlock;
import org.xwiki.rendering.block.QuotationBlock;
import org.xwiki.rendering.block.QuotationLineBlock;
import org.xwiki.rendering.block.RawBlock;
import org.xwiki.rendering.block.SectionBlock;
import org.xwiki.rendering.block.SpaceBlock;
import org.xwiki.rendering.block.SpecialSymbolBlock;
import org.xwiki.rendering.block.TableBlock;
import org.xwiki.rendering.block.TableCellBlock;
import org.xwiki.rendering.block.TableHeadCellBlock;
import org.xwiki.rendering.block.TableRowBlock;
import org.xwiki.rendering.block.VerbatimBlock;
import org.xwiki.rendering.block.WordBlock;
import org.xwiki.rendering.block.XDOM;
import org.xwiki.rendering.listener.Format;
import org.xwiki.rendering.listener.HeaderLevel;
import org.xwiki.rendering.listener.ListType;
import org.xwiki.rendering.listener.Listener;
import org.xwiki.rendering.listener.MetaData;
import org.xwiki.rendering.listener.reference.ResourceReference;
import org.xwiki.rendering.syntax.Syntax;

/**
 * Produce a {@link XDOM} based on events.
 *
 * @version $Id$
 * @since 2.1M1
 */
public class XDOMGeneratorListener implements Listener
{
    private XDOMBuilder builder = new XDOMBuilder();

    /**
     * Retrieve the XDOM produced by this listener.
     *
     * @return the generated {@link XDOM}
     */
    public XDOM getXDOM()
    {
        return this.builder.getXDOM();
    }

    /**
     * Begin a definition description block so subsequent events are collected as the description's content.
     *
     * This must be paired with {@link #endDefinitionDescription()} to finalize and add the description block.
     */
    @Override
    public void beginDefinitionDescription()
    {
        this.builder.startBlockList();
    }

    /**
     * Marks the start of a definition list in the event stream.
     *
     * @param parameters optional attributes for the definition list; may be null and will be applied when the list is closed
     */
    @Override
    public void beginDefinitionList(Map<String, String> parameters)
    {
        this.builder.startBlockList();
    }

    /**
     * Begins a new block scope to collect content for a definition term.
     */
    @Override
    public void beginDefinitionTerm()
    {
        this.builder.startBlockList();
    }

    /**
         * Start a new block list for the document's content.
         *
         * @param metadata the metadata associated with the document (may be null)
         * @since 3.0M2
         */
    @Override
    public void beginDocument(MetaData metadata)
    {
        this.builder.startBlockList();
    }

    /**
     * Marks the start of a figure block and begins a new block scope for the figure's content.
     *
     * @param parameters a map of figure parameters (e.g., attributes or options); may be {@code null}
     */
    @Override
    public void beginFigure(Map<String, String> parameters)
    {
        this.builder.startBlockList();
    }

    /**
     * Begin collecting content for a figure caption.
     *
     * Subsequent listener events will be accumulated as the caption's content until {@code endFigureCaption} is called.
     *
     * @param parameters optional parameters for the figure caption, may be {@code null}
     */
    @Override
    public void beginFigureCaption(Map<String, String> parameters)
    {
        this.builder.startBlockList();
    }

    /**
     * Start a new content scope for a formatted region.
     *
     * Subsequent listener events will be treated as the contents of this format until {@code endFormat} is called.
     *
     * @param format the format to apply to the upcoming content
     * @param parameters optional attributes for the format; may be {@code null}
     */
    @Override
    public void beginFormat(Format format, Map<String, String> parameters)
    {
        this.builder.startBlockList();
    }

    /**
     * Begins a group block used to contain nested blocks.
     *
     * @param parameters optional parameters for the group, or {@code null} if there are none
     */
    @Override
    public void beginGroup(Map<String, String> parameters)
    {
        this.builder.startBlockList();
    }

    /**
     * Signal the start of a header and begin accumulating its child blocks.
     *
     * @param level the header level (e.g., H1..H6)
     * @param id an optional identifier for the header, or {@code null} if none
     * @param parameters additional header parameters (attributes), or {@code null}
     */
    @Override
    public void beginHeader(HeaderLevel level, String id, Map<String, String> parameters)
    {
        this.builder.startBlockList();
    }

    /**
     * Begins a list block scope; subsequent events will populate the list's items.
     *
     * @param type the type of the list (for example, bulleted or numbered)
     * @param parameters optional list parameters; may be {@code null}
     */
    @Override
    public void beginList(ListType type, Map<String, String> parameters)
    {
        this.builder.startBlockList();
    }

    /**
     * Starts a new list item by opening a block list to collect the blocks belonging to that item.
     */
    @Override
    public void beginListItem()
    {
        this.builder.startBlockList();
    }

    /**
     * Signals the start of a new list item by beginning a fresh block list to collect the item's content.
     *
     * @param parameters a map of parameters for the list item (may be {@code null})
     */
    @Override
    public void beginListItem(Map<String, String> parameters)
    {
        this.builder.startBlockList();
    }

    /**
     * Begin a macro marker and prepare to collect the blocks that belong to it.
     *
     * @param name the macro marker name
     * @param macroParameters the macro's parameters (may be null)
     * @param content the macro marker content (may be null)
     * @param isInline {@code true} if the macro marker is inline, {@code false} if it is block-level
     */
    @Override
    public void beginMacroMarker(String name, Map<String, String> macroParameters, String content, boolean isInline)
    {
        this.builder.startBlockList();
    }

    /**
     * Start a new paragraph block scope to collect inline content for the paragraph.
     *
     * @param parameters optional paragraph parameters (e.g., attributes); may be {@code null}
     */
    @Override
    public void beginParagraph(Map<String, String> parameters)
    {
        this.builder.startBlockList();
    }

    /**
     * Starts a quotation block scope.
     *
     * @param parameters attributes for the quotation block; may be null
     */
    @Override
    public void beginQuotation(Map<String, String> parameters)
    {
        this.builder.startBlockList();
    }

    /**
     * Begin a new block list to accumulate content for a quotation line.
     */
    @Override
    public void beginQuotationLine()
    {
        this.builder.startBlockList();
    }

    /**
     * Begins a section, opening a new block scope to collect the section's content.
     *
     * @param parameters a map of section parameters (may be {@code null})
     */
    @Override
    public void beginSection(Map<String, String> parameters)
    {
        this.builder.startBlockList();
    }

    /**
     * Start a table element by beginning a new block list for the table's contents.
     *
     * @param parameters map of parameters for the table; may be {@code null}
     */
    @Override
    public void beginTable(Map<String, String> parameters)
    {
        this.builder.startBlockList();
    }

    /**
     * Marks the start of a table cell so subsequent parsing events form the cell's content.
     *
     * @param parameters table cell parameters (attributes), or null if none
     */
    @Override
    public void beginTableCell(Map<String, String> parameters)
    {
        this.builder.startBlockList();
    }

    /**
     * Signals the start of a table header cell and begins collecting child blocks that make up the cell content.
     *
     * @param parameters optional parameters/attributes for the table header cell; may be {@code null}
     */
    @Override
    public void beginTableHeadCell(Map<String, String> parameters)
    {
        this.builder.startBlockList();
    }

    /**
     * Begins a new block list to accumulate the row's child blocks for a table row.
     *
     * @param parameters table row parameters (e.g., attributes); may be {@code null}
     */
    @Override
    public void beginTableRow(Map<String, String> parameters)
    {
        this.builder.startBlockList();
    }

    /**
     * Begins a link construct and prepares to collect the link's inline content.
     *
     * @param reference the resource reference the link points to
     * @param freestanding true if the link is freestanding (not embedded in surrounding text), false if inline
     * @param parameters optional parameters for the link; may be null
     */
    @Override
    public void beginLink(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        this.builder.startBlockList();
    }

    /**
         * Begin a metadata block scope to accumulate content associated with the given metadata.
         *
         * @param metadata the metadata to attach to the accumulated content
         * @since 3.0M2
         */
    @Override
    public void beginMetaData(MetaData metadata)
    {
        this.builder.startBlockList();
    }

    /**
     * Ends a definition description and appends it to the current document structure as a DefinitionDescriptionBlock.
     *
     * This finalizes the content accumulated for the current definition description and adds it to the XDOM.
     */
    @Override
    public void endDefinitionDescription()
    {
        this.builder.addBlock(new DefinitionDescriptionBlock(this.builder.endBlockList()));
    }

    /**
     * Finalizes the current definition list and adds a {@link DefinitionListBlock} to the XDOM builder.
     *
     * @param parameters optional parameters for the created {@link DefinitionListBlock}; if {@code null},
     *                   {@link Listener#EMPTY_PARAMETERS} is used
     */
    @Override
    public void endDefinitionList(Map<String, String> parameters)
    {
        this.builder.addBlock(new DefinitionListBlock(this.builder.endBlockList(),
            parameters != null ? parameters : Listener.EMPTY_PARAMETERS));
    }

    /**
     * Finalizes the current definition term and appends it as a DefinitionTermBlock to the XDOM.
     */
    @Override
    public void endDefinitionTerm()
    {
        this.builder.addBlock(new DefinitionTermBlock(this.builder.endBlockList()));
    }

    /**
     * {@inheritDoc}
     *
     * @since 3.0M2
     */
    @Override
    public void endDocument(MetaData metadata)
    {
        this.builder.addBlock(new XDOM(this.builder.endBlockList(), metadata));
    }

    /**
     * Closes the current figure block scope and appends a FigureBlock containing the accumulated content.
     *
     * @param parameters a map of figure parameters/attributes to attach to the block, or {@code null} if none
     */
    @Override
    public void endFigure(Map<String, String> parameters)
    {
        this.builder.addBlock(new FigureBlock(this.builder.endBlockList(), parameters));
    }

    /**
     * Closes the current figure caption scope and appends a caption block built from the accumulated content.
     *
     * @param parameters a map of parameters/attributes for the caption (may be empty)
     */
    @Override
    public void endFigureCaption(Map<String, String> parameters)
    {
        this.builder.addBlock(new FigureCaptionBlock(this.builder.endBlockList(), parameters));
    }

    /**
     * Wraps the accumulated block list in a FormatBlock and adds it to the XDOM builder.
     *
     * Creates a FormatBlock from the current block list and the provided format and parameters,
     * using `Format.NONE` when `format` is null and `Listener.EMPTY_PARAMETERS` when `parameters` is null,
     * then adds that block to the internal builder.
     *
     * @param format     the text format to apply to the enclosed blocks; may be null to indicate no format
     * @param parameters additional parameters for the format; may be null
     */
    @Override
    public void endFormat(Format format, Map<String, String> parameters)
    {
        this.builder.addBlock(new FormatBlock(this.builder.endBlockList(), format != null ? format : Format.NONE,
            parameters != null ? parameters : Listener.EMPTY_PARAMETERS));
    }

    /**
     * Closes the current block scope as a group and appends a GroupBlock to the XDOM builder.
     *
     * @param parameters optional attributes for the group; if {@code null}, an empty parameter map is used
     */
    @Override
    public void endGroup(Map<String, String> parameters)
    {
        this.builder.addBlock(
            new GroupBlock(this.builder.endBlockList(), parameters != null ? parameters : Listener.EMPTY_PARAMETERS));
    }

    /**
     * Add a header block to the XDOM using the content accumulated since the matching begin event.
     *
     * @param level the header level
     * @param id an optional identifier for the header (may be {@code null})
     * @param parameters a map of header parameters; if {@code null}, an empty parameters map is used
     */
    @Override
    public void endHeader(HeaderLevel level, String id, Map<String, String> parameters)
    {
        this.builder.addBlock(new HeaderBlock(this.builder.endBlockList(), level,
            parameters != null ? parameters : Listener.EMPTY_PARAMETERS, id));
    }

    /**
     * Finalizes the current list scope into a list block and adds it to the XDOM builder.
     *
     * @param type the list kind; {@code BULLETED} produces a bulleted list block, other values produce a numbered list block
     * @param parameters optional parameters for the list; {@link Listener#EMPTY_PARAMETERS} is used when {@code null}
     */
    @Override
    public void endList(ListType type, Map<String, String> parameters)
    {
        if (type == ListType.BULLETED) {
            this.builder.addBlock(new BulletedListBlock(this.builder.endBlockList(),
                parameters != null ? parameters : Listener.EMPTY_PARAMETERS));
        } else {
            this.builder.addBlock(new NumberedListBlock(this.builder.endBlockList(),
                parameters != null ? parameters : Listener.EMPTY_PARAMETERS));
        }
    }

    /**
     * Finalizes the current list item by collecting its accumulated content into a ListItemBlock and adding it to the XDOM builder.
     */
    @Override
    public void endListItem()
    {
        this.builder.addBlock(new ListItemBlock(this.builder.endBlockList()));
    }

    /**
     * Finalizes the current block sequence as a list item and appends a new ListItemBlock to the document.
     *
     * @param parameters a map of attributes for the list item (may be null)
     */
    @Override
    public void endListItem(Map<String, String> parameters)
    {
        this.builder.addBlock(new ListItemBlock(this.builder.endBlockList(), parameters));
    }

    /**
     * Adds a macro marker block to the current XDOM being built, representing a macro invocation with its inner content.
     *
     * @param name the macro name
     * @param macroParameters the macro parameters, or {@link Listener#EMPTY_PARAMETERS} is used when {@code null}
     * @param content the raw macro content (may be empty)
     * @param isInline whether the macro marker is inline
     */
    @Override
    public void endMacroMarker(String name, Map<String, String> macroParameters, String content, boolean isInline)
    {
        this.builder
            .addBlock(new MacroMarkerBlock(name, macroParameters != null ? macroParameters : Listener.EMPTY_PARAMETERS,
                content, this.builder.endBlockList(), isInline));
    }

    /**
     * Wraps the accumulated block list into a ParagraphBlock and adds it to the internal XDOM builder.
     *
     * @param parameters a map of parameters for the paragraph; if {@code null}, {@link Listener#EMPTY_PARAMETERS} is used
     */
    @Override
    public void endParagraph(Map<String, String> parameters)
    {
        this.builder.addBlock(new ParagraphBlock(this.builder.endBlockList(),
            parameters != null ? parameters : Listener.EMPTY_PARAMETERS));
    }

    /**
     * Finalizes the current quotation, wraps the accumulated child blocks in a QuotationBlock, and adds it to the XDOM.
     *
     * @param parameters map of parameters for the quotation; if null, {@link Listener#EMPTY_PARAMETERS} is used
     */
    @Override
    public void endQuotation(Map<String, String> parameters)
    {
        this.builder.addBlock(new QuotationBlock(this.builder.endBlockList(),
            parameters != null ? parameters : Listener.EMPTY_PARAMETERS));
    }

    /**
     * Finalizes the current quotation line, creating a QuotationLineBlock from accumulated content and appending it to the XDOM.
     */
    @Override
    public void endQuotationLine()
    {
        this.builder.addBlock(new QuotationLineBlock(this.builder.endBlockList()));
    }

    /**
     * Wraps the current accumulated blocks in a SectionBlock and appends it to the XDOM being built.
     *
     * @param parameters a map of parameters for the section (e.g., attributes); may be {@code null}, in which case no parameters are applied
     */
    @Override
    public void endSection(Map<String, String> parameters)
    {
        this.builder.addBlock(
            new SectionBlock(this.builder.endBlockList(), parameters != null ? parameters : Listener.EMPTY_PARAMETERS));
    }

    /**
     * Finalizes the current table content into a TableBlock and adds it to the XDOM builder.
     *
     * @param parameters a map of table parameters (if null, an empty parameter map is used)
     */
    @Override
    public void endTable(Map<String, String> parameters)
    {
        this.builder.addBlock(
            new TableBlock(this.builder.endBlockList(), parameters != null ? parameters : Listener.EMPTY_PARAMETERS));
    }

    /**
     * Closes the current table cell and appends a TableCellBlock to the XDOM.
     *
     * @param parameters a map of parameters for the table cell; if {@code null}, an empty parameter map is used
     */
    @Override
    public void endTableCell(Map<String, String> parameters)
    {
        this.builder.addBlock(new TableCellBlock(this.builder.endBlockList(),
            parameters != null ? parameters : Listener.EMPTY_PARAMETERS));
    }

    /**
     * Ends the current table header cell and appends a TableHeadCellBlock built from the accumulated content.
     *
     * @param parameters cell attributes (may be null; null is treated as an empty parameter map)
     */
    @Override
    public void endTableHeadCell(Map<String, String> parameters)
    {
        this.builder.addBlock(new TableHeadCellBlock(this.builder.endBlockList(),
            parameters != null ? parameters : Listener.EMPTY_PARAMETERS));
    }

    /**
     * Finalizes the current table row and adds a TableRowBlock containing the accumulated row content and the
     * provided parameters to the XDOM.
     *
     * @param parameters a map of parameters for the table row; if {@code null}, an empty parameter map is used
     */
    @Override
    public void endTableRow(Map<String, String> parameters)
    {
        this.builder.addBlock(new TableRowBlock(this.builder.endBlockList(),
            parameters != null ? parameters : Listener.EMPTY_PARAMETERS));
    }

    /**
     * End the current link context and add a LinkBlock to the XDOM using the specified reference and options.
     *
     * @param reference the target resource reference for the link
     * @param freestanding {@code true} if the link is freestanding (not inline with surrounding text), {@code false} otherwise
     * @param parameters additional link parameters; if {@code null}, an empty parameter map is applied
     */
    @Override
    public void endLink(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        this.builder.addBlock(new LinkBlock(this.builder.endBlockList(), reference, freestanding,
            parameters != null ? parameters : Listener.EMPTY_PARAMETERS));
    }

    /**
     * Closes the current metadata section and adds a MetaDataBlock containing the enclosed blocks and the given metadata.
     *
     * @param metadata metadata to attach to the created block
     * @since 3.0M2
     */
    @Override
    public void endMetaData(MetaData metadata)
    {
        this.builder.addBlock(new MetaDataBlock(this.builder.endBlockList(), metadata));
    }

    /**
     * Records a run of consecutive empty lines in the current XDOM being built.
     *
     * @param count the number of consecutive empty lines
     */
    @Override
    public void onEmptyLines(int count)
    {
        this.builder.addBlock(new EmptyLinesBlock(count));
    }

    /**
     * Adds a horizontal line block to the current XDOM.
     *
     * @param parameters parameters for the horizontal line; if {@code null}, an empty parameter map is used
     */
    @Override
    public void onHorizontalLine(Map<String, String> parameters)
    {
        this.builder.addBlock(new HorizontalLineBlock(parameters != null ? parameters : Listener.EMPTY_PARAMETERS));
    }

    /**
     * Adds an identifier block with the specified name to the current document structure.
     *
     * @param name the identifier name to add to the document
     */
    @Override
    public void onId(String name)
    {
        this.builder.addBlock(new IdBlock(name));
    }

    /**
     * Adds a macro block to the current XDOM being built.
     *
     * @param id the macro identifier
     * @param parameters a map of macro parameters; if {@code null}, an empty parameter map is used
     * @param content the macro content (may be {@code null} for parameter-only macros)
     * @param inline {@code true} if the macro is inline, {@code false} if it's a block-level macro
     */
    @Override
    public void onMacro(String id, Map<String, String> parameters, String content, boolean inline)
    {
        this.builder
            .addBlock(new MacroBlock(id, parameters != null ? parameters : Listener.EMPTY_PARAMETERS, content, inline));
    }

    /**
     * Registers a new-line in the document by adding a NewLineBlock to the current block stream.
     */
    @Override
    public void onNewLine()
    {
        this.builder.addBlock(new NewLineBlock());
    }

    /**
     * Adds a raw text block with the given content and syntax to the generated XDOM.
     *
     * @param content the raw text content to add
     * @param syntax the syntax that applies to the raw content
     */
    @Override
    public void onRawText(String content, Syntax syntax)
    {
        this.builder.addBlock(new RawBlock(content, syntax));
    }

    /**
     * Adds a space block to the XDOM under construction.
     */
    @Override
    public void onSpace()
    {
        this.builder.addBlock(new SpaceBlock());
    }

    /**
     * Adds a SpecialSymbolBlock for the given character to the current XDOM being built.
     *
     * @param symbol the character to represent as a special symbol block
     */
    @Override
    public void onSpecialSymbol(char symbol)
    {
        this.builder.addBlock(new SpecialSymbolBlock(symbol));
    }

    /**
     * Adds a verbatim block containing the given content.
     *
     * @param content the verbatim text to include
     * @param inline  whether the verbatim content is inline (`true`) or block-level (`false`)
     * @param parameters optional parameters for the verbatim block; if `null`, `Listener.EMPTY_PARAMETERS` is used
     */
    @Override
    public void onVerbatim(String content, boolean inline, Map<String, String> parameters)
    {
        this.builder
            .addBlock(new VerbatimBlock(content, parameters != null ? parameters : Listener.EMPTY_PARAMETERS, inline));
    }

    /**
     * Appends a WordBlock containing the given word to the current block list being built.
     *
     * @param word the word to wrap in a WordBlock
     */
    @Override
    public void onWord(String word)
    {
        this.builder.addBlock(new WordBlock(word));
    }

    /**
     * Adds an image block to the current XDOM under construction.
     *
     * @param reference  the resource reference identifying the image
     * @param freestanding  whether the image is freestanding (occupies its own block) or inline
     * @param parameters  rendering parameters for the image; if {@code null}, empty parameters are used
     */
    @Override
    public void onImage(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        this.builder.addBlock(
            new ImageBlock(reference, freestanding, parameters != null ? parameters : Listener.EMPTY_PARAMETERS));
    }

    /**
     * Adds an image block to the current XDOM using the provided reference, freestanding flag, id, and parameters.
     *
     * @param reference   the resource reference for the image
     * @param freestanding whether the image stands alone (not inline)
     * @param id          an optional identifier for the image (may be null)
     * @param parameters  additional image parameters; if null, an empty parameter map is used
     */
    @Override
    public void onImage(ResourceReference reference, boolean freestanding, String id, Map<String, String> parameters)
    {
        this.builder.addBlock(
            new ImageBlock(reference, freestanding, id, parameters != null ? parameters : Listener.EMPTY_PARAMETERS));
    }
}
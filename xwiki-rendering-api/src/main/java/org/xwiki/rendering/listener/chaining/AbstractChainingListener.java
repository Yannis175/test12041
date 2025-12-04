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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.xwiki.rendering.listener.Format;
import org.xwiki.rendering.listener.HeaderLevel;
import org.xwiki.rendering.listener.ListType;
import org.xwiki.rendering.listener.MetaData;
import org.xwiki.rendering.listener.reference.ResourceReference;
import org.xwiki.rendering.syntax.Syntax;

/**
 * Default and basic implementation of a chaining listener that knows how to delegate event calls to the next listener
 * in the chain.
 *
 * @version $Id$
 * @since 1.8RC1
 */
public abstract class AbstractChainingListener implements ChainingListener
{
    /**
     * The chain to use to know the next listener to call on events.
     */
    private ListenerChain listenerChain;

    /**
     * True if {@link #beginListItem(Map)} should redirect to {@link #beginListItem()} for retro compatibility.
     * <p>
     * {@link #beginListItem(Map)} was added long after {@link #beginListItem()} and various renderers/listeners
     * override {@link #beginListItem()} only and won't be called in post 10.0 versions of XWiki without this retro
     * compatibility trick.
     * 
     * @since 10.11.10
     * @since 11.3.7
     * @since 11.10.2
     * @since 12.0RC1
     */
    private final boolean listItemRetroCompatibility;

    /**
     * True if {@link #onImage(ResourceReference, boolean, String, Map)} should redirect to
     * {@link #onImage(ResourceReference, boolean, Map)} for retro compatibility.
     *
     * The method {@link #onImage(ResourceReference, boolean, String, Map)} was added long after
     * {@link #onImage(ResourceReference, boolean, Map)} and renderers/listeners overriding the latter only won't be
     * called in versions 14.2RC1 and later without this compatibility mode.
     *
     * @since 14.2RC1
     */
    private final boolean imageRetroCompatibility;

    /**
     * Creates a new AbstractChainingListener and initializes retro-compatibility flags for legacy listener APIs.
     *
     * Initializes {@link #listItemRetroCompatibility} (whether the class hierarchy requires routing list-item
     * calls to the old zero-argument variant) and {@link #imageRetroCompatibility} (whether the class hierarchy
     * requires routing image calls to the old three-argument variant).
     */
    public AbstractChainingListener()
    {
        this.listItemRetroCompatibility = needsRetroCompatibility(method -> method.getName().endsWith("ListItem"), 0);
        this.imageRetroCompatibility = needsRetroCompatibility(method -> method.getName().equals("onImage"), 3);
    }

    /**
     * Determine whether retro-compatibility wrappers are required for methods matching the given filter.
     *
     * Inspects the class hierarchy from the concrete class up to AbstractChainingListener to detect a subclass
     * that declares the matched methods only with the legacy parameter count.
     *
     * @param methodFilter a predicate selecting the methods to inspect
     * @param oldParameterCount the parameter count of the legacy method variant to detect
     * @return `true` if a subclass declares only the legacy-parameter variant of the matched methods, `false` otherwise
     * @since 14.2RC1
     */
    private boolean needsRetroCompatibility(Predicate<Method> methodFilter, int oldParameterCount)
    {
        boolean result = false;

        for (Class<?> current = getClass(); current != AbstractChainingListener.class; current =
            current.getSuperclass()) {
            Set<Integer> parameterCounts = Arrays.stream(current.getDeclaredMethods())
                .filter(methodFilter)
                .map(Method::getParameterCount)
                .collect(Collectors.toSet());

            // If there is only the variant with the old parameter count, we need the compatibility wrapper.
            if (parameterCounts.size() == 1 && parameterCounts.contains(oldParameterCount)) {
                result = true;
            }

            // Do not continue looking once we found a class implementing one of the methods.
            if (!parameterCounts.isEmpty()) {
                break;
            }
        }

        return result;
    }

    /**
     * Sets the ListenerChain used to determine the next listener to invoke for events.
     *
     * @param listenerChain the ListenerChain to use for delegation, or {@code null} to clear the chain
     * @since 2.0M3
     */
    public void setListenerChain(ListenerChain listenerChain)
    {
        this.listenerChain = listenerChain;
    }

    /**
     * Retrieve the current listener chain used to obtain the next listener in the chain.
     *
     * @return the current {@link ListenerChain}, or {@code null} if no chain has been set
     */
    @Override
    public ListenerChain getListenerChain()
    {
        return this.listenerChain;
    }

    /**
     * Delegates the "begin definition description" event to the next listener in the chain.
     *
     * If there is no next listener, this method does nothing.
     */
    @Override
    public void beginDefinitionDescription()
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginDefinitionDescription();
        }
    }

    /**
     * Delegates the start of a definition list event to the next listener in the chain.
     *
     * @param parameters a map of parameters for the definition list, or {@code null} if there are none
     */
    @Override
    public void beginDefinitionList(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginDefinitionList(parameters);
        }
    }

    /**
     * Delegates the beginning of a definition term event to the next listener in the chain.
     */
    @Override
    public void beginDefinitionTerm()
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginDefinitionTerm();
        }
    }

    /**
     * Signals the start of document processing.
     *
     * @param metadata the document's metadata, or {@code null} if none
     */
    @Override
    public void beginDocument(MetaData metadata)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginDocument(metadata);
        }
    }

    /**
     * Forwards a "begin group" event to the next listener in the listener chain.
     *
     * @param parameters map of parameters associated with the group (may be {@code null})
     */
    @Override
    public void beginGroup(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginGroup(parameters);
        }
    }

    /**
     * Signals the start of a formatting span using the specified format and parameters.
     *
     * @param format the formatting type to begin
     * @param parameters optional parameters for the format; may be null
     */
    @Override
    public void beginFormat(Format format, Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginFormat(format, parameters);
        }
    }

    /**
     * Forwards the start-of-header event to the next listener in the listener chain.
     *
     * @param level the header level
     * @param id an optional header identifier, or {@code null} if none
     * @param parameters additional rendering parameters for the header
     */
    @Override
    public void beginHeader(HeaderLevel level, String id, Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginHeader(level, id, parameters);
        }
    }

    /**
     * Forwards a "begin link" event to the next listener in the chain.
     *
     * @param reference   the resource reference for the link
     * @param freestanding {@code true} if the link is freestanding (not inline), {@code false} otherwise
     * @param parameters  additional link parameters (may be {@code null})
     */
    @Override
    public void beginLink(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginLink(reference, freestanding, parameters);
        }
    }

    /**
     * Forwards a "begin list" event to the next listener in the chain.
     *
     * @param type the kind of list being started
     * @param parameters optional attributes for the list (may be {@code null})
     */
    @Override
    public void beginList(ListType type, Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginList(type, parameters);
        }
    }

    /**
     * Signal the start of a list item to the next listener in the chain.
     *
     * If no next listener is present, this method performs no action.
     */
    @Override
    public void beginListItem()
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginListItem();
        }
    }

    /**
     * Signals the start of a list item, delegating the event to the next listener in the chain or to the legacy
     * no-argument handler when retro compatibility is required.
     *
     * If retro compatibility is enabled, this invokes the no-argument beginListItem() on this instance; otherwise
     * the call is forwarded to the next listener if one exists.
     *
     * @param parameters a map of parameters associated with the list item (may be empty or null)
     */
    @Override
    public void beginListItem(Map<String, String> parameters)
    {
        // Make sure to call the old beginListItem() if it's the only thing implemented by the extending class since
        // this new one existed only after 10.10
        if (this.listItemRetroCompatibility) {
            beginListItem();
        } else {
            ChainingListener next = getListenerChain().getNextListener(getClass());
            if (next != null) {
                next.beginListItem(parameters);
            }
        }
    }

    /**
     * Forwards a macro marker begin event to the next listener in the chain.
     *
     * @param name the macro name
     * @param parameters a map of macro parameters (name -> value)
     * @param content the macro content, or {@code null} if none
     * @param isInline {@code true} if the macro is inline, {@code false} if it is block-level
     */
    @Override
    public void beginMacroMarker(String name, Map<String, String> parameters, String content, boolean isInline)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginMacroMarker(name, parameters, content, isInline);
        }
    }

    /**
     * Delegates the start-of-paragraph event to the next listener in the chain, if one exists.
     *
     * @param parameters paragraph parameters; may be null
     */
    @Override
    public void beginParagraph(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginParagraph(parameters);
        }
    }

    /**
     * Delegates the start of a quotation block to the next listener in the chain.
     *
     * @param parameters a map of parameters associated with the quotation block, or {@code null} if none
     */
    @Override
    public void beginQuotation(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginQuotation(parameters);
        }
    }

    /**
     * Signals the start of a quotation line.
     */
    @Override
    public void beginQuotationLine()
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginQuotationLine();
        }
    }

    /**
     * Forwards a section-begin event to the next listener in the chain.
     *
     * @param parameters a map of section parameters (attributes such as id or other properties)
     */
    @Override
    public void beginSection(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginSection(parameters);
        }
    }

    /**
     * Notifies the listener that a table is beginning with the provided parameters.
     *
     * @param parameters a map of table parameters/attributes (e.g., style, id) to apply to the table
     */
    @Override
    public void beginTable(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginTable(parameters);
        }
    }

    /**
     * Forwards the "begin table cell" event to the next listener in the chain when available.
     *
     * @param parameters a map of attributes for the table cell (may be null or empty)
     */
    @Override
    public void beginTableCell(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginTableCell(parameters);
        }
    }

    /**
     * Signals the start of a table header cell.
     *
     * @param parameters a map of parameters for the table header cell (may be null or empty)
     */
    @Override
    public void beginTableHeadCell(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginTableHeadCell(parameters);
        }
    }

    /**
     * Signals the start of a table row.
     *
     * @param parameters a map of parameters associated with the table row
     */
    @Override
    public void beginTableRow(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginTableRow(parameters);
        }
    }

    /**
     * Delegates the begin-metadata event to the next listener in the chain, if one exists.
     *
     * @param metadata the document metadata associated with the event
     */
    @Override
    public void beginMetaData(MetaData metadata)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginMetaData(metadata);
        }
    }

    /**
     * Delegates the start of a figure element to the next listener in the chain.
     *
     * @param parameters parameters associated with the figure element (may be {@code null})
     */
    @Override
    public void beginFigure(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginFigure(parameters);
        }
    }

    /**
     * Notify the listener chain that a figure caption has begun.
     *
     * @param parameters a map of attributes for the figure caption
     */
    @Override
    public void beginFigureCaption(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginFigureCaption(parameters);
        }
    }

    /**
     * Notifies the next listener in the chain that a definition description has ended.
     */
    @Override
    public void endDefinitionDescription()
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endDefinitionDescription();
        }
    }

    /**
     * Signals the end of a definition list to the next listener in the chain.
     *
     * @param parameters the parameters associated with the definition list
     */
    @Override
    public void endDefinitionList(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endDefinitionList(parameters);
        }
    }

    /**
     * Called when a definition term ends.
     *
     * Delegates the event to the next listener in the chain if one is available.
     */
    @Override
    public void endDefinitionTerm()
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endDefinitionTerm();
        }
    }

    /**
     * Notifies the next listener that document processing has finished.
     *
     * @param metadata the document's metadata
     */
    @Override
    public void endDocument(MetaData metadata)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endDocument(metadata);
        }
    }

    /**
     * Delegates the end of a group element to the next listener in the chain.
     *
     * @param parameters the parameters associated with the group element
     */
    @Override
    public void endGroup(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endGroup(parameters);
        }
    }

    /**
     * Signals the end of a formatting element to the listener chain.
     *
     * @param format the format that ended
     * @param parameters additional parameters associated with the format, or {@code null} if none
     */
    @Override
    public void endFormat(Format format, Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endFormat(format, parameters);
        }
    }

    /**
     * Notifies the listener chain that a header has ended by delegating the event to the next listener.
     *
     * @param level the header level that ended
     * @param id the header identifier, or {@code null} if none
     * @param parameters additional header parameters, may be empty or {@code null}
     */
    @Override
    public void endHeader(HeaderLevel level, String id, Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endHeader(level, id, parameters);
        }
    }

    /**
     * Notifies the listener chain that a link element has ended.
     *
     * @param reference   the target reference of the link
     * @param freestanding true if the link is freestanding (not surrounded by text), false otherwise
     * @param parameters  the link parameters (may be empty or null)
     */
    @Override
    public void endLink(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endLink(reference, freestanding, parameters);
        }
    }

    /**
     * Delegates the end-of-list event to the next listener in the listener chain.
     *
     * @param type       the type of the list being ended
     * @param parameters optional parameters associated with the list
     */
    @Override
    public void endList(ListType type, Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endList(type, parameters);
        }
    }

    /**
     * Signals the end of the current list item to the next listener in the chain.
     *
     * If no next listener is available this invocation has no effect.
     */
    @Override
    public void endListItem()
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endListItem();
        }
    }

    /**
     * Ends the current list item, honoring legacy implementations when needed.
     *
     * If this listener requires list-item retro-compatibility, invokes the no-argument {@code endListItem()} on this
     * instance; otherwise forwards the call with the provided parameters to the next listener in the chain if present.
     *
     * @param parameters parameters associated with the list item (may be empty or null)
     */
    @Override
    public void endListItem(Map<String, String> parameters)
    {
        // Make sure to call the old endListItem() if it's the only thing implemented by the extending class since
        // this new one existed only after 10.10.
        if (this.listItemRetroCompatibility) {
            endListItem();
        } else {
            ChainingListener next = getListenerChain().getNextListener(getClass());
            if (next != null) {
                next.endListItem(parameters);
            }
        }
    }

    /**
     * Delegates the end of a macro marker event to the next listener in the chain.
     *
     * @param name the macro name
     * @param parameters the macro parameters
     * @param content the macro content
     * @param isInline true if the macro marker is inline, false if it is block-level
     */
    @Override
    public void endMacroMarker(String name, Map<String, String> parameters, String content, boolean isInline)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endMacroMarker(name, parameters, content, isInline);
        }
    }

    /**
     * Delegates the end of a paragraph event to the next listener in the chain, if any.
     *
     * @param parameters the paragraph parameters/attributes, or {@code null} if none
     */
    @Override
    public void endParagraph(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endParagraph(parameters);
        }
    }

    /**
     * Signals the end of a quotation block to the next listener in the chain.
     *
     * @param parameters a map of quotation parameters (string keys and values), or {@code null} if none
     */
    @Override
    public void endQuotation(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endQuotation(parameters);
        }
    }

    /**
     * Delegates the end of a quotation line event to the next listener in the chain if one exists.
     */
    @Override
    public void endQuotationLine()
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endQuotationLine();
        }
    }

    /**
     * Signals the end of a section.
     *
     * @param parameters additional parameters associated with the section
     */
    @Override
    public void endSection(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endSection(parameters);
        }
    }

    /**
     * Delegates the end-of-table event to the next listener in the chain.
     *
     * @param parameters the table parameters, if any
     */
    @Override
    public void endTable(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endTable(parameters);
        }
    }

    /**
     * Notify that the current table cell has ended.
     *
     * Delegates the end-of-cell event to the next listener in the chain, if any.
     *
     * @param parameters a map of attributes associated with the table cell (may be empty or null)
     */
    @Override
    public void endTableCell(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endTableCell(parameters);
        }
    }

    /**
     * Signals the end of the current table header cell.
     *
     * @param parameters additional parameters associated with the table header cell
     */
    @Override
    public void endTableHeadCell(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endTableHeadCell(parameters);
        }
    }

    /**
     * Delegates the end-of-table-row event to the next listener in the chain, if any.
     *
     * @param parameters parameters associated with the table row
     */
    @Override
    public void endTableRow(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endTableRow(parameters);
        }
    }

    /**
     * Signal the end of document metadata.
     *
     * @param metadata the metadata for the document
     */
    @Override
    public void endMetaData(MetaData metadata)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endMetaData(metadata);
        }
    }

    /**
     * Signals the end of a figure element.
     *
     * @param parameters map of figure parameters (attribute name to value)
     */
    @Override
    public void endFigure(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endFigure(parameters);
        }
    }

    /**
     * Signals the end of a figure caption to the listener chain.
     *
     * @param parameters caption parameters (attributes) provided by the parser, or {@code null} if none
     */
    @Override
    public void endFigureCaption(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endFigureCaption(parameters);
        }
    }

    /**
     * Forwards an empty-lines event to the next listener in the chain.
     *
     * @param count the number of consecutive empty lines encountered
     */
    @Override
    public void onEmptyLines(int count)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.onEmptyLines(count);
        }
    }

    /**
     * Forwards a horizontal line event to the next listener in the chain, if one exists.
     *
     * @param parameters rendering parameters associated with the horizontal line
     */
    @Override
    public void onHorizontalLine(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.onHorizontalLine(parameters);
        }
    }

    /**
     * Delegates an identifier event to the next listener in the chain.
     *
     * @param name the identifier name
     */
    @Override
    public void onId(String name)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.onId(name);
        }
    }

    /**
     * Handles an image event by delegating it to the next listener in the chain.
     *
     * @param reference   the image resource reference
     * @param freestanding true if the image stands alone (not inline), false if inline
     * @param parameters  additional image parameters (may be empty or null)
     */
    @Override
    public void onImage(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.onImage(reference, freestanding, parameters);
        }
    }

    /**
     * Handle an image event by delegating to the next listener in the chain or, for legacy implementations, to the
     * older variant without an id.
     *
     * <p>If this listener class requires image retro-compatibility, the older `onImage(ResourceReference, boolean, Map)`
     * variant is invoked on this instance; otherwise the event is forwarded to the next listener's
     * `onImage(ResourceReference, boolean, String, Map)`.</p>
     *
     * @param reference   the resource reference for the image
     * @param freestanding whether the image is freestanding (block) or inline
     * @param id          the optional image id (may be null)
     * @param parameters  additional image parameters
     */
    @Override
    public void onImage(ResourceReference reference, boolean freestanding, String id, Map<String, String> parameters)
    {
        // Make sure to call the old method without id if the child class does not implement the new variant that has
        // been introduced in 14.2RC1.
        if (this.imageRetroCompatibility) {
            onImage(reference, freestanding, parameters);
        } else {
            ChainingListener next = getListenerChain().getNextListener(getClass());
            if (next != null) {
                next.onImage(reference, freestanding, id, parameters);
            }
        }
    }

    /**
     * Notifies the next listener in the chain that a macro has been encountered.
     *
     * @param id the macro name or identifier
     * @param parameters the macro's parameters (name → value)
     * @param content the macro content or body, or {@code null} if none
     * @param inline {@code true} if the macro is inline, {@code false} if it is block-level
     */
    @Override
    public void onMacro(String id, Map<String, String> parameters, String content, boolean inline)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.onMacro(id, parameters, content, inline);
        }
    }

    /**
     * Forward the new-line event to the next listener in the chain.
     *
     * If there is no next listener, this method does nothing.
     */
    @Override
    public void onNewLine()
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.onNewLine();
        }
    }

    /**
     * Delegates a space event to the next listener in the listener chain, if one exists.
     */
    @Override
    public void onSpace()
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.onSpace();
        }
    }

    /**
     * Forwards a special-symbol event to the next listener in the chain.
     *
     * @param symbol the special symbol character encountered (for example a punctuation or formatting marker)
     */
    @Override
    public void onSpecialSymbol(char symbol)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.onSpecialSymbol(symbol);
        }
    }

    /**
     * Forwards a verbatim block event to the next listener in the chain.
     *
     * @param content    the verbatim text content
     * @param inline     {@code true} if the verbatim content is inline, {@code false} if block-level
     * @param parameters additional parameters associated with the verbatim event
     */
    @Override
    public void onVerbatim(String content, boolean inline, Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.onVerbatim(content, inline, parameters);
        }
    }

    /**
     * Called when a word token is encountered.
     *
     * @param word the textual content of the word token
     */
    @Override
    public void onWord(String word)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.onWord(word);
        }
    }

    /**
     * Forwards a raw text event to the next listener in the chain if one is available.
     *
     * @param text   the raw text content
     * @param syntax the syntax of the raw text
     */
    @Override
    public void onRawText(String text, Syntax syntax)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.onRawText(text, syntax);
        }
    }
}
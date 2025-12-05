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
package org.xwiki.rendering.internal.parser.reference;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.commons.lang3.StringUtils;
import org.xwiki.component.annotation.Component;
import org.xwiki.component.manager.ComponentLookupException;
import org.xwiki.rendering.listener.reference.DocumentResourceReference;
import org.xwiki.rendering.listener.reference.InterWikiResourceReference;
import org.xwiki.rendering.listener.reference.ResourceReference;
import org.xwiki.rendering.listener.reference.ResourceType;
import org.xwiki.rendering.parser.ResourceReferenceParser;
import org.xwiki.rendering.parser.ResourceReferenceTypeParser;

/**
 * Each syntax should have its own resource reference parser. However while we wait for syntax specific parser to be
 * implemented this generic parser should provide a good approximation.
 *
 * @version $Id$
 * @since 2.5RC1
 */
@Component
@Named("default/link")
@Singleton
public class GenericLinkReferenceParser extends AbstractResourceReferenceParser
{
    /**
     * Interwiki separator.
     */
    public static final String SEPARATOR_INTERWIKI = "@";

    /**
     * Query String separator.
     */
    public static final String SEPARATOR_QUERYSTRING = "?";

    /**
     * Anchor separator.
     */
    public static final String SEPARATOR_ANCHOR = "#";

    /**
     * Escape character to allow "#", "@" and "?" characters in a reference's name.
     */
    public static final char ESCAPE_CHAR = '\\';

    /**
     * Escapes to remove from the document reference part when parsing the raw reference (i.e. excluding query string,
     * anchor and interwiki parts). Note that we don't remove the escaped escape char since this is how an escape char
     * is represented in an Entity Reference.
     */
    private static final String[] ESCAPES_REFERENCE = new String[] { ESCAPE_CHAR + SEPARATOR_QUERYSTRING,
        ESCAPE_CHAR + SEPARATOR_INTERWIKI, ESCAPE_CHAR + SEPARATOR_ANCHOR };

    /**
     * Escapes to remove from the query string, anchor and interwiki parts when parsing the raw reference.
     */
    private static final String[] ESCAPES_EXTRA = new String[] { ESCAPE_CHAR + SEPARATOR_QUERYSTRING,
        ESCAPE_CHAR + SEPARATOR_INTERWIKI, ESCAPE_CHAR + SEPARATOR_ANCHOR, String.valueOf(ESCAPE_CHAR) + ESCAPE_CHAR };

    /**
     * Escapes to remove the interwiki content.
     */
    private static final String[] ESCAPE_INTERWIKI =
        new String[] { String.valueOf(ESCAPE_CHAR) + ESCAPE_CHAR, String.valueOf(ESCAPE_CHAR) };

    /**
     * Replacement chars for the escapes to be removed from the reference part.
     */
    private static final String[] ESCAPE_REPLACEMENTS_REFERENCE = new String[] { SEPARATOR_QUERYSTRING,
        SEPARATOR_INTERWIKI, SEPARATOR_ANCHOR };

    /**
     * Replacement chars for the escapes to be removed from the query string, anchor and interwiki parts.
     */
    private static final String[] ESCAPE_REPLACEMENTS_EXTRA = new String[] { SEPARATOR_QUERYSTRING,
        SEPARATOR_INTERWIKI, SEPARATOR_ANCHOR, String.valueOf(ESCAPE_CHAR) };

    /**
     * Replacements chars for the escapes to be removed from the interwiki content.
     */
    private static final String[] ESCAPE_REPLACEMENTS_INTERWIKI = new String[] { String.valueOf(ESCAPE_CHAR), "" };

    /**
     * The list of recognized URL prefixes.
     */
    private static final List<String> URI_PREFIXES = Arrays.asList("mailto");

    /**
     * Parser to parse link references pointing to URLs.
     */
    @Inject
    @Named("url")
    private ResourceReferenceTypeParser urlResourceReferenceTypeParser;

    @Inject
    @Named("link/untyped")
    private ResourceReferenceParser untypedLinkReferenceParser;

    /**
     * URI prefixes recognized by this link parser.
     *
     * @return the list of recognized URI prefixes (for example, "mailto")
     */
    protected List<String> getAllowedURIPrefixes()
    {
        return URI_PREFIXES;
    }

    /**
     * Parses a raw link reference and returns a corresponding ResourceReference.
     *
     * The method recognizes URIs with supported schemes, InterWiki links, non-wiki URLs (when not in wiki mode),
     * or document links (including optional query string and anchor).
     *
     * @param rawReference the raw reference string to parse
     * @return the parsed ResourceReference, or `null` if the reference could not be parsed
     */
    @Override
    public ResourceReference parse(String rawReference)
    {
        // Step 1: Check if it's a known URI by looking for one of the known URI schemes. If not, check if it's a URL.
        ResourceReference resourceReference = parseURILinks(rawReference);
        if (resourceReference != null) {
            return resourceReference;
        }

        // Step 2: Look for an InterWiki link
        StringBuilder content = new StringBuilder(rawReference);
        resourceReference = parseInterWikiLinks(content);
        if (resourceReference != null) {
            return resourceReference;
        }

        // Step 3: If we're in non wiki mode, we consider the reference to be a URL.
        if (!isInWikiMode()) {
            resourceReference = new ResourceReference(rawReference, ResourceType.URL);
            resourceReference.setTyped(false);
            return resourceReference;
        }

        // Step 4: Consider that we have a reference to a document
        return parseDocumentLink(content);
    }

    /**
     * Create a document ResourceReference by parsing the provided content, extracting an optional query string and anchor.
     *
     * @param content the mutable raw document reference text (may contain escaped separators); parsed parts are removed from this builder
     * @return the parsed document ResourceReference, marked as untyped, with `QUERY_STRING` and `ANCHOR` parameters set when present
     */
    private ResourceReference parseDocumentLink(StringBuilder content)
    {
        // Extract any query string.
        String queryString = null;
        String text = parseElementAfterString(content, SEPARATOR_QUERYSTRING);
        if (text != null) {
            queryString = removeEscapesFromExtraParts(text);
        }

        // Extract any anchor.
        String anchor = null;
        text = parseElementAfterString(content, SEPARATOR_ANCHOR);
        if (text != null) {
            anchor = removeEscapesFromExtraParts(text);
        }

        // Make sure to unescape the remaining reference string.
        String unescapedReferenceString = removeEscapesFromReferencePart(content.toString());
        // Parse the string as an untyped link reference.
        ResourceReference reference = untypedLinkReferenceParser.parse(unescapedReferenceString);
        reference.setTyped(false);

        // Set any previously extracted parameters.
        if (StringUtils.isNotBlank(queryString)) {
            reference.setParameter(DocumentResourceReference.QUERY_STRING, queryString);
        }
        if (StringUtils.isNotBlank(anchor)) {
            reference.setParameter(DocumentResourceReference.ANCHOR, anchor);
        }

        return reference;
    }

    /**
     * Determine whether the input represents a URI or URL and parse it into a ResourceReference.
     *
     * @param rawLink the raw reference string to inspect and parse
     * @return a ResourceReference representing the parsed URI or URL, or `null` if the input is not a URI/URL or no suitable parser is available
     */
    private ResourceReference parseURILinks(String rawLink)
    {
        ResourceReference result = null;
        int uriSchemeDelimiterPos = rawLink.indexOf(':');
        if (uriSchemeDelimiterPos > -1) {
            String scheme = rawLink.substring(0, uriSchemeDelimiterPos);
            String reference = rawLink.substring(uriSchemeDelimiterPos + 1);
            if (getAllowedURIPrefixes().contains(scheme)) {
                try {
                    ResourceReferenceTypeParser parser =
                        this.componentManagerProvider.get().getInstance(ResourceReferenceTypeParser.class, scheme);
                    ResourceReference resourceReference = parser.parse(reference);
                    if (resourceReference != null) {
                        result = resourceReference;
                    }
                } catch (ComponentLookupException e) {
                    // Failed to lookup component, this shouldn't happen but ignore it.
                }
            } else {
                // Check if it's a URL
                ResourceReference resourceReference = this.urlResourceReferenceTypeParser.parse(rawLink);
                if (resourceReference != null) {
                    resourceReference.setTyped(false);
                    result = resourceReference;
                }
            }
        }
        return result;
    }

    /**
     * Detects an interwiki link in the given content.
     *
     * <p>If an interwiki separator is found, the corresponding InterWikiResourceReference is created with escapes
     * removed and the interwiki alias set.</p>
     *
     * @param content the original content to parse; this StringBuilder is modified to remove the parsed interwiki part
     * @return an {@link InterWikiResourceReference} for the detected interwiki link, or {@code null} if none was found
     */
    private ResourceReference parseInterWikiLinks(StringBuilder content)
    {
        ResourceReference result = null;
        String interWikiAlias = parseElementAfterString(content, SEPARATOR_INTERWIKI);
        if (interWikiAlias != null) {
            InterWikiResourceReference link = new InterWikiResourceReference(removeEscapes(content.toString()));
            link.setInterWikiAlias(removeEscapes(interWikiAlias));
            result = link;
        }
        return result;
    }

    /**
     * Extracts and returns the substring located to the right of the last unescaped occurrence of {@code separator}.
     *
     * The method removes the separator and everything to its right from the provided {@code content} buffer.
     *
     * @param content the string buffer to parse; the buffer will be modified to remove the parsed part when a separator is found
     * @param separator the separator string to locate
     * @return the parsed element (trimmed), or {@code null} if the separator string wasn't found unescaped
     */
    protected String parseElementAfterString(StringBuilder content, String separator)
    {
        String element = null;

        // Find the first non escaped separator (starting from the end of the content buffer).
        int index = content.lastIndexOf(separator);
        while (index != -1) {
            // Check if the element is found and it's not escaped.
            if (!shouldEscape(content, index)) {
                element = content.substring(index + separator.length()).trim();
                content.delete(index, content.length());
                break;
            }

            if (index > 0) {
                index = content.lastIndexOf(separator, index - 1);
            } else {
                break;
            }
        }

        return element;
    }

    /**
     * Determine whether the character at the given index is escaped by preceding escape characters.
     *
     * A character is considered escaped when it is immediately preceded by an odd number of escape characters (`ESCAPE_CHAR`).
     *
     * @param content the content in which to check for escapes
     * @param charPosition the index of the character to check within {@code content}
     * @return {@code true} if the character is escaped, {@code false} otherwise
     */
    private boolean shouldEscape(StringBuilder content, int charPosition)
    {
        int counter = 0;
        int pos = charPosition - 1;
        while (pos > -1 && content.charAt(pos) == ESCAPE_CHAR) {
            counter++;
            pos--;
        }
        return counter % 2 != 0;
    }

    /**
     * Remove escape sequences that apply to the main reference portion, restoring the original characters.
     *
     * @param text the reference text containing escape sequences
     * @return the reference text with reference-part escapes removed
     */
    private String removeEscapesFromReferencePart(String text)
    {
        return StringUtils.replaceEach(text, ESCAPES_REFERENCE, ESCAPE_REPLACEMENTS_REFERENCE);
    }

    /**
     * Remove escape sequences from query string, anchor, and interwiki parts of a reference.
     *
     * @param text the input substring (query, anchor, or interwiki alias) that may contain escape sequences
     * @return the input string with escape sequences replaced according to the ESCAPES_EXTRA → ESCAPE_REPLACEMENTS_EXTRA mappings
     */
    private String removeEscapesFromExtraParts(String text)
    {
        return StringUtils.replaceEach(text, ESCAPES_EXTRA, ESCAPE_REPLACEMENTS_EXTRA);
    }

    /**
     * Remove interwiki escape sequences from the given reference text.
     *
     * @param text the reference text that may contain interwiki escape sequences
     * @return the text with interwiki escape sequences replaced by their unescaped equivalents
     */
    private String removeEscapes(String text)
    {
        return StringUtils.replaceEach(text, ESCAPE_INTERWIKI, ESCAPE_REPLACEMENTS_INTERWIKI);
    }
}
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
package org.xwiki.rendering.listener.reference;

import org.apache.commons.lang3.StringUtils;

/**
 * Represents a reference to a Document.
 *
 * @version $Id$
 * @since 2.5RC1
 */
public class DocumentResourceReference extends ResourceReference
{
    /**
     * The name of the parameter representing the Query String.
     */
    public static final String QUERY_STRING = "queryString";

    /**
     * The name of the parameter representing the Anchor.
     */
    public static final String ANCHOR = "anchor";

    /**
     * Create a DocumentResourceReference for the given document reference string.
     *
     * @param reference the document reference string (the value returned by {@link #getReference()})
     */
    public DocumentResourceReference(String reference)
    {
        super(reference, ResourceType.DOCUMENT);
    }

    /**
     * Get the query string used when rendering the document URL.
     *
     * @return the query string to append to the URL, or {@code null} if none is set. Example: {@code mydata1=5&mydata2=Hello}
     */
    public String getQueryString()
    {
        return getParameter(QUERY_STRING);
    }

    /**
     * Set the query string parameter for this document reference.
     *
     * @param queryString the query string to associate with the reference; if null or empty the parameter is not set
     */
    public void setQueryString(String queryString)
    {
        if (!StringUtils.isEmpty(queryString)) {
            setParameter(QUERY_STRING, queryString);
        }
    }

    /**
     * Retrieves the anchor name within the referenced document.
     *
     * In XWiki, anchors are automatically created for titles (for example, "TableOfContentAnchor").
     *
     * @return the anchor name pointing to an anchor defined in the referenced document, or null if no anchor has been specified
     */
    public String getAnchor()
    {
        return getParameter(ANCHOR);
    }

    /**
     * Sets the anchor (fragment identifier) of the document reference.
     *
     * @param anchor the anchor (fragment identifier) to set; ignored if null or empty
     */
    public void setAnchor(String anchor)
    {
        if (!StringUtils.isEmpty(anchor)) {
            setParameter(ANCHOR, anchor);
        }
    }
}
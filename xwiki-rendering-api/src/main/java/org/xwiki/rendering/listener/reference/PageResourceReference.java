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
 * Represents a reference to a Page.
 *
 * @version $Id$
 * @since 10.6RC1
 */
public class PageResourceReference extends ResourceReference
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
     * Create a page resource reference from the given page reference string.
     *
     * @param reference the page reference string
     */
    public PageResourceReference(String reference)
    {
        super(reference, ResourceType.PAGE);
    }

    /**
     * Get the query string that will be appended to the rendered URL.
     *
     * @return the query string (for example {@code mydata1=5&mydata2=Hello}), or {@code null} if no query string has been specified.
     */
    public String getQueryString()
    {
        return getParameter(QUERY_STRING);
    }

    /**
     * Set the query string for this page reference.
     *
     * If the supplied value is null or empty, no parameter is set and the reference remains unchanged.
     *
     * @param queryString the query string to associate with the page reference
     */
    public void setQueryString(String queryString)
    {
        if (!StringUtils.isEmpty(queryString)) {
            setParameter(QUERY_STRING, queryString);
        }
    }

    /**
     * Get the anchor name within the referenced page.
     *
     * @return the anchor name (e.g., "TableOfContentAnchor"), or {@code null} if no anchor is specified (the reference points to the top of the page)
     */
    public String getAnchor()
    {
        return getParameter(ANCHOR);
    }

    /**
     * Sets the anchor (fragment identifier) for this page reference.
     *
     * @param anchor the fragment identifier to set; if {@code null} or empty the anchor is not changed
     */
    public void setAnchor(String anchor)
    {
        if (!StringUtils.isEmpty(anchor)) {
            setParameter(ANCHOR, anchor);
        }
    }
}
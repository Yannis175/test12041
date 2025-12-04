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
 * Represents a reference to an Attachment of a Page.
 *
 * @version $Id$
 * @since 13.10.5
 * @since 14.3RC1
 */
public class PageAttachmentResourceReference extends ResourceReference
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
     * Create a new reference that identifies a page attachment.
     *
     * @param reference the reference string that identifies the attachment resource (page and attachment)
     */
    public PageAttachmentResourceReference(String reference)
    {
        super(reference, ResourceType.PAGE_ATTACHMENT);
    }

    /**
     * Retrieve the query string used to build the rendered URL.
     *
     * @return the query string used for the rendered URL, or {@code null} if none was specified. Example: {@code mydata1=5&mydata2=Hello}
     */
    public String getQueryString()
    {
        return getParameter(QUERY_STRING);
    }

    /**
     * Set the query string to include in the attachment reference URL.
     *
     * If {@code queryString} is empty, the query string parameter is not stored.
     *
     * @param queryString the query string portion of the URL (without the leading '?'); ignored if empty
     */
    public void setQueryString(String queryString)
    {
        if (!StringUtils.isEmpty(queryString)) {
            setParameter(QUERY_STRING, queryString);
        }
    }

    /**
     * Get the anchor within the referenced attachment.
     *
     * @return the anchor name pointing to an anchor defined in the referenced attachment, or `null` if no anchor has been specified (in which case the reference points to the top of the attachment)
     */
    public String getAnchor()
    {
        return getParameter(ANCHOR);
    }

    /**
     * Set the anchor fragment for this page attachment reference.
     *
     * If `anchor` is null or empty, the anchor parameter is not set.
     *
     * @param anchor the anchor fragment to set; ignored if null or empty
     */
    public void setAnchor(String anchor)
    {
        if (!StringUtils.isEmpty(anchor)) {
            setParameter(ANCHOR, anchor);
        }
    }
}
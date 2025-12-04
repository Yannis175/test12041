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

/**
 * Represents a reference to an external wiki(<a href="http://en.wikipedia.org/wiki/InterWiki">Inter Wiki</a>).
 * An InterWiki reference is a shorthand notation to reference a set of external URL, all having a common prefix (eg
 * http://server/some/common/prefix/a1, http://server/some/common/prefix/a2). An InterWiki reference is made of an
 * InterWiki Alias which is a name corresponding to the common URL and an InterWiki Path which is the suffix to append
 * to the common URL part to make the full URL.
 *
 * @version $Id$
 * @since 2.5RC1
 */
public class InterWikiResourceReference extends ResourceReference
{
    /**
     * The name of the parameter representing the InterWiki Alias.
     */
    public static final String INTERWIKI_ALIAS = "interWikiAlias";

    /**
     * Create a new InterWikiResourceReference for the specified InterWiki reference.
     *
     * @param reference the InterWiki reference string, typically in the form "alias:page" (InterWiki shorthand)
     */
    public InterWikiResourceReference(String reference)
    {
        super(reference, ResourceType.INTERWIKI);
        setTyped(true);
    }

    /**
     * Stores the InterWiki alias used to resolve this reference to an external InterWiki URL.
     *
     * @param interWikiAlias the InterWiki alias (may be null to unset)
     */
    public void setInterWikiAlias(String interWikiAlias)
    {
        setParameter(INTERWIKI_ALIAS, interWikiAlias);
    }

    /**
     * The InterWiki alias that this resource references.
     *
     * Mappings between InterWiki aliases and external locations are defined in the InterWiki Map (for example: "wikipedia").
     *
     * @return the InterWiki alias, or {@code null} if not defined
     */
    public String getInterWikiAlias()
    {
        return getParameter(INTERWIKI_ALIAS);
    }
}
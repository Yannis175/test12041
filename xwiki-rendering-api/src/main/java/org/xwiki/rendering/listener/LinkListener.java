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

import java.util.Map;

import org.xwiki.filter.annotation.Default;
import org.xwiki.rendering.listener.reference.ResourceReference;

/**
 * Contains callback events for Links, called when a document has been parsed and when it needs to be modified or
 * rendered.
 *
 * @version $Id$
 * @since 1.8RC3
 * @see Listener
 */
public interface LinkListener
{
    /**
 * Invoked at the start of processing a link.
 *
 * @param reference the link resource reference
 * @param freestanding {@code true} if the link is a freestanding URI appearing directly in text
 * @param parameters additional link parameters (e.g., {@code style="background-color: blue"}); may be empty
 * @see ResourceReference
 * @since 2.5RC1
 */
    void beginLink(ResourceReference reference, boolean freestanding, @Default("") Map<String, String> parameters);

    /**
 * Signal the end of processing for a link.
 *
 * @param reference the link resource reference
 * @param freestanding {@code true} if the link is a freestanding URI directly in the text
 * @param parameters a map of additional parameters for the link (for example: {@code style="background-color: blue"})
 * @see ResourceReference
 * @since 2.5RC1
 */
    void endLink(ResourceReference reference, boolean freestanding, @Default("") Map<String, String> parameters);
}
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
import org.xwiki.stability.Unstable;

/**
 * Contains callback events for Images,called when a document has been parsed and when it needs to be modified or
 * rendered.
 *
 * @version $Id$
 * @since 1.8RC3
 * @see Listener
 */
public interface ImageListener
{
    /**
 * Handle an image element encountered during parsing or rendering.
 *
 * @param reference the image reference (location and related metadata)
 * @param freestanding true if the image is defined directly as a URI in the text
 * @param parameters a map of image parameters (for example: {@code style="background-color: blue"})
 */
    void onImage(ResourceReference reference, boolean freestanding, @Default("") Map<String, String> parameters);

    /**
     * Handle an image event that includes a generated image id.
     *
     * <p>Default implementation ignores the `id` parameter.</p>
     *
     * @param reference the image reference
     * @param freestanding true if the image is defined directly as a URI in the text
     * @param id the generated image id (may be empty)
     * @param parameters a map of image parameters, for example {@code style="background-color: blue"}
     * @since 14.2RC1
     */
    @Unstable
    default void onImage(ResourceReference reference, boolean freestanding, String id, @Default("") Map<String,
        String> parameters)
    {
        onImage(reference, freestanding, parameters);
    }
}
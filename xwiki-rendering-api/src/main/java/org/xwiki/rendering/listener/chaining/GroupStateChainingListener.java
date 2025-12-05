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

/**
 * Provides information on whether we're inside a group. Note that this listener is separated from the
 * {@link org.xwiki.rendering.listener.chaining.BlockStateChainingListener} class because we don't want this listener to
 * be stackable (since we need to create new instance of stackable listeners to reset states when we encounter a begin
 * group event but we also need to know we're inside a group).
 *
 * @version $Id$
 * @since 1.8.3
 */
public class GroupStateChainingListener extends AbstractChainingListener
{
    private int groupDepth;

    /**
     * Create a GroupStateChainingListener bound to the given listener chain.
     *
     * @param listenerChain the listener chain to which events will be delegated
     */
    public GroupStateChainingListener(ListenerChain listenerChain)
    {
        setListenerChain(listenerChain);
    }

    /**
     * Gets the current group nesting depth within the document.
     *
     * @return the current depth of nested groups; 0 when not inside any group
     */
    public int getDocumentDepth()
    {
        return this.groupDepth;
    }

    /**
     * Indicates whether the current position is inside a group.
     *
     * @return {@code true} if the current group nesting depth is greater than zero, {@code false} otherwise.
     */
    public boolean isInGroup()
    {
        return this.groupDepth > 0;
    }

    /**
     * Increments the internal group nesting depth and forwards a group-begin event to the chained listeners.
     *
     * @param parameters the parameters associated with the group, or {@code null} if none
     */

    @Override
    public void beginGroup(Map<String, String> parameters)
    {
        ++this.groupDepth;

        super.beginGroup(parameters);
    }

    /**
     * Signals the end of a group to the chained listener and updates the tracked group nesting depth.
     *
     * @param parameters the parameters associated with the group end event
     */
    @Override
    public void endGroup(Map<String, String> parameters)
    {
        super.endGroup(parameters);

        --this.groupDepth;
    }
}
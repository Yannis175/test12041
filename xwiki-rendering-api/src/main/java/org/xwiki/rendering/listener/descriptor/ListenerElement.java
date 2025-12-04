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
package org.xwiki.rendering.listener.descriptor;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * An element of the listener.
 * <p>
 * An element is defined by either an <code>on</code> event of a combination of <code>begin</code> and <code>end</code>
 * events.
 *
 * @version $Id$
 * @since 3.3M1
 */
public class ListenerElement
{
    /**
     * @see #getName()
     */
    private String name;

    /**
     * @see #getParameters()
     */
    private List<Type> parameters = new ArrayList<Type>();

    /**
     * @see #getBeginMethod()
     */
    private Method beginMethod;

    /**
     * @see #getEndMethod()
     */
    private Method endMethod;

    /**
     * @see #getOnMethod()
     */
    private Method onMethod;

    /**
     * Constructs a ListenerElement with the given name.
     *
     * @param name the element's name
     */
    public ListenerElement(String name)
    {
        this.name = name;
    }

    /**
     * Get the element's name.
     *
     * @return the name of the element
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Get the parameter types declared for this listener element.
     *
     * @return the list of parameter types for the element
     */
    public List<Type> getParameters()
    {
        return this.parameters;
    }

    /**
     * Method invoked at the element's begin event.
     *
     * @return the begin {@link Method}, or {@code null} if the element is defined as an {@code on}-event
     */
    public Method getBeginMethod()
    {
        return this.beginMethod;
    }

    /**
     * Set the method to invoke when the element's begin event occurs.
     *
     * @param beginMethod the begin event method, or {@code null} if the element is defined as an on-event
     */
    public void setBeginMethod(Method beginMethod)
    {
        this.beginMethod = beginMethod;
    }

    /**
     * The method invoked for the element's end event.
     *
     * @return the end {@link Method}, or {@code null} if the element uses an {@code on} event model
     */
    public Method getEndMethod()
    {
        return this.endMethod;
    }

    /**
     * Set the method to invoke for this element's end event.
     *
     * @param endMethod the method to invoke at the element's end event, or `null` to indicate the element uses an on-event model
     */
    public void setEndMethod(Method endMethod)
    {
        this.endMethod = endMethod;
    }

    /**
     * Method to invoke for an on-event element.
     *
     * @return the on-event {@link Method}, or {@code null} if this element is defined by begin/end events.
     */
    public Method getOnMethod()
    {
        return this.onMethod;
    }

    /**
     * Set the method to invoke for an on-event element.
     *
     * @param onMethod the Method to invoke for the on event; set to {@code null} if this element uses begin/end events
     */
    public void setOnMethod(Method onMethod)
    {
        this.onMethod = onMethod;
    }
}
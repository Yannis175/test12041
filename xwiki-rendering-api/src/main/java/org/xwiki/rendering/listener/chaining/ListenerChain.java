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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores information about the listeners in the chain and the order in which they need to be called. Also sports a
 * feature that allows pushing and popping listeners that are stackable. This feature is useful since listeners can hold
 * stateful information and sometimes you may need to push new versions of them to start with new state information. For
 * example this is used in the XWiki Syntax Renderer when group event is found to start the rendering for that group
 * using reset state information.
 *
 * @version $Id$
 * @since 1.8RC1
 */
public class ListenerChain
{
    /**
     * The full list of chaining listeners. For each of them we have a stack since the ones that implement the
     * {@link StackableChainingListener} interface can be stacked.
     */
    private Map<Class<? extends ChainingListener>, Deque<ChainingListener>> listeners = new HashMap<>();

    /**
     * The ordered list of listeners. We only allow one instance per listener class name so we just need to store the
     * class object and then the instance can be found in {@link #listeners}.
     */
    private List<Class<? extends ChainingListener>> nextListeners = new ArrayList<>();

    /**
     * Add a chaining listener to the chain.
     *
     * If a listener of the same class is already registered, the new instance is pushed onto that class's stack
     * so multiple instances of the same listener class can be stacked.
     *
     * @param listener the chaining listener to add; if an instance of that listener's class is already present the new
     *                 instance will be stacked on top of the existing instances
     */
    public void addListener(ChainingListener listener)
    {
        addListener(listener, -1);
    }

    /**
     * Remove the top instance of the specified listener class from the chain.
     *
     * If multiple instances of that listener class are stacked, only the top instance is popped.
     * When the class' stack becomes empty the listener class is removed from the chain ordering.
     *
     * @param listenerClass the listener class whose top instance should be removed
     * @throws NullPointerException if the listener class is not registered in the chain
     */
    public void removeListener(Class<? extends ChainingListener> listenerClass)
    {
        Deque<ChainingListener> stack = this.listeners.get(listenerClass);
        if (stack.size() > 0) {
            stack.pop();
        }
        if (stack.isEmpty()) {
            this.listeners.remove(listenerClass);
            this.nextListeners.remove(listenerClass);
        }
    }

    /**
     * Add a chaining listener into the chain at the given position, stacking additional instances of the same
     * listener class on top of the existing class-specific stack.
     *
     * If a stack for the listener's class does not yet exist, a new stack is created and the listener's class
     * is inserted into the chain at the specified index; if the index is negative or out of range the class is
     * appended at the end. The provided listener instance is pushed onto its class stack.
     *
     * @param listener the chaining listener to add; if other instances of the same listener class exist, this
     *                 instance is pushed on that class's stack
     * @param index the position in the chain where the listener's class should be inserted, or a negative/out-of-range
     *              value to append the class at the end
     * @since 10.5RC1
     */
    public void addListener(ChainingListener listener, int index)
    {
        // If there's already an entry for that listener then push it on the existing stack
        // and don't add the listener as an additional listener in the list (since it's already
        // in there). We need to take these steps since the push() methods below will create
        // new instances of listeners which will add themselves in the chain automatically.
        Deque<ChainingListener> stack = this.listeners.get(listener.getClass());
        if (stack == null) {
            stack = new ArrayDeque<>();
            this.listeners.put(listener.getClass(), stack);
            if (index > -1 && index < this.nextListeners.size()) {
                this.nextListeners.add(index, listener.getClass());
            } else {
                this.nextListeners.add(listener.getClass());
            }
        }
        stack.push(listener);
    }

    /**
     * Get the listener instance that follows the given listener class in the chain.
     *
     * @param listenerClass the listener class whose successor is requested
     * @return the next listener instance in the chain, or {@code null} if there is no successor
     */
    public ChainingListener getNextListener(Class<? extends ChainingListener> listenerClass)
    {
        ChainingListener next = null;
        int pos = indexOf(listenerClass);
        if (pos > -1 && this.nextListeners.size() > pos + 1) {
            next = this.listeners.get(this.nextListeners.get(pos + 1)).peek();
        }
        return next;
    }

    /**
     * Retrieve the current listener instance for the given listener class.
     *
     * If there is no exact stack registered for the requested class, searches for a registered listener class
     * that is assignable to the requested class and uses its current top instance. Returns the top (most recently
     * pushed) instance from the matching class-specific stack.
     *
     * @param listenerClass the listener class for which to locate the current instance
     * @return the top (most recently added) listener instance for the matching class, or {@code null} if none exists
     */
    public ChainingListener getListener(Class<? extends ChainingListener> listenerClass)
    {
        Deque<ChainingListener> result = this.listeners.get(listenerClass);
        if (result == null) {
            for (Class<? extends ChainingListener> listenerKey : this.listeners.keySet()) {
                if (listenerClass.isAssignableFrom(listenerKey)) {
                    result = this.listeners.get(listenerKey);
                    break;
                }
            }
        }

        return result != null ? result.peek() : null;
    }

    /**
     * Get the position of a listener class within the chain.
     *
     * @param listenerClass the listener class whose position to locate
     * @return the zero-based position of the class in the chain, or -1 if the class is not present
     */
    public int indexOf(Class<? extends ChainingListener> listenerClass)
    {
        return this.nextListeners.indexOf(listenerClass);
    }

    /**
     * Pushes a new instance for the given listener class onto its class-specific stack when the class is stackable.
     *
     * If the listener class implements StackableChainingListener and a stack for that class is present, a new
     * ChainingListener instance produced by the stack's current top element is pushed onto the stack. No action is
     * performed for non-stackable classes or when no stack is registered for the class.
     *
     * @param listenerClass the listener class for which to create and push a new instance if it is stackable
     */
    public void pushListener(Class<? extends ChainingListener> listenerClass)
    {
        if (StackableChainingListener.class.isAssignableFrom(listenerClass)) {
            Deque<ChainingListener> stack = this.listeners.get(listenerClass);
            stack.push(((StackableChainingListener) stack.peek()).createChainingListenerInstance());
        }
    }

    /**
     * Creates and pushes a fresh instance for every registered stackable chaining listener, establishing a clean state for subsequent sub-rendering operations.
     */
    public void pushAllStackableListeners()
    {
        for (Class<? extends ChainingListener> listenerClass : this.listeners.keySet()) {
            pushListener(listenerClass);
        }
    }

    /**
     * Restore the chain to the previous state by popping one instance for each registered stackable listener.
     *
     * <p>For each listener class currently registered in the chain, this method removes the top instance if the
     * listener class implements {@code StackableChainingListener}; non-stackable listener classes are left unchanged.</p>
     */
    public void popAllStackableListeners()
    {
        for (Class<? extends ChainingListener> listenerClass : this.listeners.keySet()) {
            popListener(listenerClass);
        }
    }

    /**
         * Pop the top instance for a stackable listener class to restore the previous listener state.
         *
         * @param listenerClass the class of the chaining listener to pop
         */
    public void popListener(Class<? extends ChainingListener> listenerClass)
    {
        if (StackableChainingListener.class.isAssignableFrom(listenerClass)) {
            this.listeners.get(listenerClass).pop();
        }
    }
}
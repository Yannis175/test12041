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
package org.xwiki.rendering.block;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.xwiki.rendering.listener.Listener;

/**
 * Represents a word.
 *
 * @version $Id$
 * @since 1.5M2
 */
public class WordBlock extends AbstractBlock
{
    /**
     * @see #getWord()
     */
    private String word;

    /**
     * Create a WordBlock that represents a single word in the rendering tree.
     *
     * @param word the word content; must be a single word — spaces or special symbols are represented by other blocks
     */
    public WordBlock(String word)
    {
        this.word = word;
    }

    /**
     * Emits this block's word to the provided listener.
     *
     * @param listener the listener to notify; receives the word via {@code Listener.onWord(String)}
     */
    @Override
    public void traverse(Listener listener)
    {
        listener.onWord(getWord());
    }

    /**
     * Retrieve the word represented by this block.
     *
     * @return the stored word text
     */
    public String getWord()
    {
        return this.word;
    }

    /**
     * Provide a string representation of this block using the contained word.
     *
     * @return the word contained by the block
     */
    @Override
    public String toString()
    {
        return getWord();
    }

    /**
     * Determines whether the specified object is equal to this WordBlock.
     *
     * Two WordBlock instances are equal if they are the same instance or if the other object is a {@code WordBlock},
     * the superclass considers them equal, and both have equal word values.
     *
     * @param obj the object to compare with this WordBlock
     * @return {@code true} if the specified object is equal to this WordBlock, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) {
            return true;
        }

        if (obj instanceof WordBlock && super.equals(obj)) {
            EqualsBuilder builder = new EqualsBuilder();

            builder.append(getWord(), ((WordBlock) obj).getWord());

            return builder.isEquals();
        }

        return false;
    }

    /**
     * Computes the hash code for this WordBlock.
     *
     * @return the hash code composed from the superclass hash and this block's word
     */
    @Override
    public int hashCode()
    {
        HashCodeBuilder builder = new HashCodeBuilder();

        builder.appendSuper(super.hashCode());
        builder.append(getWord());

        return builder.toHashCode();
    }
}
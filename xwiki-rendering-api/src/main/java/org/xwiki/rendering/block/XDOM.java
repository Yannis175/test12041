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

import java.util.Collections;
import java.util.List;

import org.xwiki.rendering.listener.Listener;
import org.xwiki.rendering.listener.MetaData;
import org.xwiki.rendering.util.IdGenerator;

/**
 * Contains the full tree of {@link Block} that represent a XWiki Document's content.
 *
 * @version $Id$
 * @since 1.5M2
 */
public class XDOM extends MetaDataBlock
{
    /**
     * Constructs an empty XDOM. Useful for example when calling a macro that doesn't use the XDOM parameter passed to
     * it.
     */
    public static final XDOM EMPTY = new XDOM(Collections.<Block>emptyList());

    /**
     * Stateful id generator for this document. We store it in the XDOM because it is the only object which remains the
     * same between parsing, transformation and rendering, and we need to generate ids during parsing and during
     * transformation.
     */
    private transient IdGenerator idGenerator;

    /**
     * Create an XDOM containing the given child blocks using a fresh IdGenerator and empty metadata.
     *
     * @param childBlocks the child blocks that compose the document
     */
    public XDOM(List<? extends Block> childBlocks)
    {
        this(childBlocks, new IdGenerator(), MetaData.EMPTY);
    }

    /**
     * Create an XDOM initialized with the given child blocks and metadata.
     *
     * @param childBlocks the child blocks that form the document's content
     * @param metaData the metadata to attach to the document
     * @see AbstractBlock#AbstractBlock(List)
     */
    public XDOM(List<? extends Block> childBlocks, MetaData metaData)
    {
        this(childBlocks, new IdGenerator(), metaData);
    }

    /**
     * Create an XDOM containing the given child blocks and using the provided document-wide IdGenerator.
     *
     * The XDOM is initialized with empty metadata.
     *
     * @param childBlocks the child blocks that form the document's content
     * @param idGenerator a stateful IdGenerator to produce stable identifiers within this document
     */
    public XDOM(List<? extends Block> childBlocks, IdGenerator idGenerator)
    {
        this(childBlocks, idGenerator, MetaData.EMPTY);
    }

    /**
     * Create an XDOM containing the given child blocks, using the provided document-wide id generator and metadata.
     *
     * @param childBlocks the child blocks that form the document tree
     * @param idGenerator a stateful IdGenerator to produce document-scoped identifiers
     * @param metaData metadata to attach to the document
     * @see AbstractBlock#AbstractBlock(List)
     */
    public XDOM(List<? extends Block> childBlocks, IdGenerator idGenerator, MetaData metaData)
    {
        super(childBlocks, metaData);
        this.idGenerator = idGenerator;
    }

    /**
     * Access the document-scoped IdGenerator used to assign identifiers to blocks within this XDOM.
     *
     * @return the stateful IdGenerator for this document
     */
    public IdGenerator getIdGenerator()
    {
        return this.idGenerator;
    }

    /**
     * Assigns the document-wide IdGenerator used to produce stable identifiers for blocks within this XDOM.
     *
     * @param idGenerator the stateful id generator to use for this document; may be {@code null} to remove any generator
     * @since 2.1M1
     */
    public void setIdGenerator(IdGenerator idGenerator)
    {
        this.idGenerator = idGenerator;
    }

    /**
     * Notify the given listener that document processing is starting using this XDOM's metadata.
     *
     * @param listener the listener to notify
     */
    @Override
    public void before(Listener listener)
    {
        listener.beginDocument(getMetaData());
    }

    /**
     * Signals the end of this document to the given listener using this document's metadata.
     *
     * @param listener the listener to notify of the document end
     */
    @Override
    public void after(Listener listener)
    {
        listener.endDocument(getMetaData());
    }

    /**
     * Create a shallow copy of this XDOM with an independent IdGenerator when present.
     *
     * @return a shallow clone of this XDOM; if this XDOM has an `IdGenerator`, the clone receives a copied `IdGenerator`
     */
    @Override
    public XDOM clone()
    {
        XDOM clone = (XDOM) super.clone();

        // The cloned XDOM should not increment the current id generator
        if (this.idGenerator != null) {
            clone.idGenerator = new IdGenerator(this.idGenerator);
        }

        return clone;
    }
}
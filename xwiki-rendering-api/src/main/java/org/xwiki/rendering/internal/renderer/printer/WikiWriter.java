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
package org.xwiki.rendering.internal.renderer.printer;

import java.io.IOException;
import java.io.Writer;

import org.xwiki.rendering.renderer.printer.WikiPrinter;

/**
 * Bridge so that {@link WikiPrinter} can be used in a tools supporting {@link Writer} api.
 *
 * @version $Id$
 */
public class WikiWriter extends Writer
{
    /**
     * Create a Writer that delegates all output to the given WikiPrinter.
     *
     * @param printer the WikiPrinter to receive printed output and to use as the Writer's synchronization lock
     */
    public WikiWriter(WikiPrinter printer)
    {
        super(printer);
    }

    /**
     * Sets the underlying WikiPrinter used by this writer.
     *
     * @param printer the WikiPrinter to delegate output to and to use as this writer's lock
     */
    public void setWikiPrinter(WikiPrinter printer)
    {
        this.lock = printer;
    }

    /**
     * Retrieve the underlying WikiPrinter used as this writer's lock/target.
     *
     * @return the underlying {@link WikiPrinter} instance
     */
    public WikiPrinter getWikiPrinter()
    {
        return (WikiPrinter) this.lock;
    }

    /**
     * Performs no action when closing this Writer because the underlying {@link org.xwiki.rendering.renderer.printer.WikiPrinter}
     * does not support being closed.
     */
    @Override
    public void close() throws IOException
    {
        // WikiPrinter does not support stream close
    }

    /**
     * No-op flush; the underlying {@link org.xwiki.rendering.renderer.printer.WikiPrinter} does not support flush.
     */
    @Override
    public void flush() throws IOException
    {
        // WikiPrinter does not support stream flush
    }

    /**
     * Writes a portion of a character array to the underlying {@link org.xwiki.rendering.renderer.printer.WikiPrinter}.
     *
     * @param cbuf the source character array
     * @param off the start offset in the array
     * @param len the number of characters to write
     */
    @Override
    public void write(char[] cbuf, int off, int len) throws IOException
    {
        getWikiPrinter().print(String.valueOf(cbuf, off, len));
    }

    /**
     * Send the specified string to the underlying WikiPrinter.
     *
     * @param str the string to print
     * @throws IOException if an I/O error occurs while printing
     */
    @Override
    public void write(String str) throws IOException
    {
        getWikiPrinter().print(str);
    }

}
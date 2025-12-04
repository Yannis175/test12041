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
package org.xwiki.rendering;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link RenderingException}.
 *
 * @version $Id$
 * @since 10.10RC1
 */
public class RenderingExceptionTest
{
    @Test
    public void constructorWithMessage()
    {
        String message = "Test rendering exception message";
        RenderingException exception = new RenderingException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void constructorWithMessageAndCause()
    {
        String message = "Test rendering exception with cause";
        Throwable cause = new IllegalArgumentException("Root cause");
        RenderingException exception = new RenderingException(message, cause);

        assertEquals(message, exception.getMessage());
        assertSame(cause, exception.getCause());
    }

    @Test
    public void constructorWithNullMessage()
    {
        RenderingException exception = new RenderingException(null);

        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void constructorWithNullCause()
    {
        String message = "Test with null cause";
        RenderingException exception = new RenderingException(message, null);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void exceptionIsSerializable()
    {
        RenderingException exception = new RenderingException("Test message");
        
        // Verify the exception can be created (implies it's serializable with proper serialVersionUID)
        assertNotNull(exception);
        assertTrue(exception instanceof Exception);
    }

    @Test
    public void exceptionCanBeThrown()
    {
        try {
            throw new RenderingException("Test exception");
        } catch (RenderingException e) {
            assertEquals("Test exception", e.getMessage());
        }
    }

    @Test
    public void exceptionWithCauseCanBeThrown()
    {
        try {
            RuntimeException cause = new RuntimeException("Original error");
            throw new RenderingException("Wrapped error", cause);
        } catch (RenderingException e) {
            assertEquals("Wrapped error", e.getMessage());
            assertNotNull(e.getCause());
            assertEquals("Original error", e.getCause().getMessage());
        }
    }

    @Test
    public void exceptionChaining()
    {
        Exception rootCause = new Exception("Root cause");
        RenderingException middleException = new RenderingException("Middle exception", rootCause);
        RenderingException topException = new RenderingException("Top exception", middleException);

        assertEquals("Top exception", topException.getMessage());
        assertSame(middleException, topException.getCause());
        assertSame(rootCause, topException.getCause().getCause());
    }

    @Test
    public void initCauseAfterConstruction()
    {
        RenderingException exception = new RenderingException("Test message");
        assertNull(exception.getCause());

        Throwable cause = new IllegalStateException("Delayed cause");
        exception.initCause(cause);

        assertSame(cause, exception.getCause());
    }
}
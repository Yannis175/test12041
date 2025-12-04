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
package org.xwiki.rendering.internal.renderer;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Generates syntax for a parameters group like macros and links.
 *
 * @version $Id$
 * @since 1.9RC2
 */
public class ParametersPrinter
{
    /**
     * Quote character.
     */
    private static final String QUOTE = "\"";

    private final String escapedStrings;

    private char escapeChar;

    private Pattern escaped;

    private String replacement;

    /**
     * Creates a ParametersPrinter configured to escape only the double-quote character.
     *
     * @deprecated since 7.4.5 and 8.2RC1; use {@link #ParametersPrinter(char, String...)} instead.
     */
    @Deprecated
    public ParametersPrinter()
    {
        this.escapedStrings = Pattern.quote(QUOTE);
    }

    /**
     * Create a ParametersPrinter configured with the given escape character and additional strings to escape.
     *
     * <p>Always treats the double-quote character (") as escapable in addition to the provided strings.</p>
     *
     * @param escapeChar the character used to prefix escaped tokens in parameter values
     * @param escapedStrings additional literal strings that must be escaped when present in parameter values
     * @since 7.4.5
     * @since 8.2RC1
     */
    public ParametersPrinter(char escapeChar, String... escapedStrings)
    {
        StringBuilder builder = new StringBuilder();

        builder.append(Pattern.quote(QUOTE));
        for (String str : escapedStrings) {
            builder.append('|');
            builder.append(Pattern.quote(str));
        }

        this.escapedStrings = builder.toString();

        setEscapeChar(escapeChar);
    }

    /**
     * Configure the character used to escape special tokens and rebuild the corresponding matcher and replacement.
     *
     * This sets the instance's escape character and prepares:
     * - {@code replacement}: a string that prefixes matched tokens with the escape character (using a backreference).
     * - {@code escaped}: a compiled {@link java.util.regex.Pattern} that matches either the escape character itself or
     *   any of the tokens listed in {@link #escapedStrings}.
     *
     * @param escapeChar the character to use as the escape prefix for matched tokens
     */
    private void setEscapeChar(char escapeChar)
    {
        this.escapeChar = escapeChar;

        StringBuilder replacementBuilder = new StringBuilder();
        replacementBuilder.append(Matcher.quoteReplacement(String.valueOf(escapeChar)));
        replacementBuilder.append("$0");
        this.replacement = replacementBuilder.toString();

        this.escaped = Pattern.compile(Pattern.quote(String.valueOf(this.escapeChar)) + '|' + this.escapedStrings);
    }

    /**
     * Render a map of parameters into a single string where each entry is formatted as name="value".
     *
     * Values are escaped using the provided escape character.
     *
     * @param parameters the parameters to render; entries with null keys or values are skipped
     * @param escapeChar the character used to prefix any escapable token inside parameter values
     * @return the rendered parameters as a space-separated sequence of name="escapedValue" entries
     * @deprecated since 7.4.5 and 8.2RC1, use {@link #print(Map)} instead
     */
    @Deprecated
    public String print(Map<String, String> parameters, char escapeChar)
    {
        setEscapeChar(escapeChar);

        return print(parameters);
    }

    /**
     * Render the given parameters as a single string of space-separated name="value" pairs.
     *
     * @param parameters the map of parameter names to values; entries with a null key or null value are skipped
     * @return the parameters formatted as space-separated name="escapedValue" pairs, or an empty string if none
     * @since 7.4.5
     * @since 8.2RC1
     */
    public String print(Map<String, String> parameters)
    {
        StringBuilder builder = new StringBuilder();

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String value = entry.getValue();
            String key = entry.getKey();

            if (key != null && value != null) {
                if (builder.length() > 0) {
                    builder.append(' ');
                }
                builder.append(print(key, value));
            }
        }

        return builder.toString();
    }

    /**
     * Format a single parameter as name="value", escaping characters according to the provided escape character.
     *
     * @param escapeChar the character prefixed to any matched token when escaping parameter values
     * @return the formatted parameter, e.g. {@code name="escapedValue"}
     * @deprecated since 7.4.5 and 8.2RC1; use {@link #print(String, String)} instead
     */
    @Deprecated
    public String print(String parameterName, String parameterValue, char escapeChar)
    {
        setEscapeChar(escapeChar);

        return print(parameterName, parameterValue);
    }

    /**
     * Format a parameter into the form name="value", applying the configured escaping to the value.
     *
     * @param parameterName the parameter name
     * @param parameterValue the parameter value; escapable characters in this value will be escaped
     * @return the formatted parameter as {@code name="escapedValue"}
     * @since 7.4.5
     * @since 8.2RC1
     */
    public String print(String parameterName, String parameterValue)
    {
        // escape meaningfull strings
        String value = this.escaped.matcher(parameterValue).replaceAll(this.replacement);

        return parameterName + "=" + QUOTE + value + QUOTE;
    }
}
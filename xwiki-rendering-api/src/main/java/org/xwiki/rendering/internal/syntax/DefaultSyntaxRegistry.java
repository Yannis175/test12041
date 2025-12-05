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
package org.xwiki.rendering.internal.syntax;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.rendering.parser.ParseException;
import org.xwiki.rendering.syntax.Syntax;
import org.xwiki.rendering.syntax.SyntaxRegistry;
import org.xwiki.rendering.syntax.SyntaxType;

/**
 * Default implementation of the Syntax Registry, storing the known syntaxes in memory.
 *
 * @version $Id$
 * @since 13.2RC1
 */
@Component
@Singleton
public class DefaultSyntaxRegistry implements SyntaxRegistry
{
    /**
     * Used to cut the syntax identifier into syntax name and syntax version.
     */
    private static final Pattern SYNTAX_PATTERN = Pattern.compile("(.*)/(.*)");

    private Map<String, Syntax> syntaxes = new HashMap<>();

    /**
     * Registers one or more Syntax instances in the registry.
     *
     * @param syntaxes the Syntax instances to add to the registry
     */
    @Override
    public void registerSyntaxes(Syntax... syntaxes)
    {
        for (Syntax syntax : syntaxes) {
            registerSyntax(syntax);
        }
    }

    /**
     * Unregisters the given syntaxes from this registry.
     *
     * @param syntaxes the syntaxes to remove
     */
    @Override
    public void unregisterSyntaxes(Syntax... syntaxes)
    {
        for (Syntax syntax : syntaxes) {
            unregisterSyntax(syntax);
        }
    }

    /**
     * Provides an unmodifiable map of registered syntaxes keyed by their id.
     *
     * @return an unmodifiable map whose keys are syntax id strings and whose values are the corresponding {@link Syntax} instances
     */
    @Override
    public Map<String, Syntax> getSyntaxes()
    {
        return Collections.unmodifiableMap(this.syntaxes);
    }

    /**
     * Retrieve a registered Syntax by its identifier.
     *
     * @param syntaxId the syntax identifier (as returned by {@code Syntax#toIdString()})
     * @return an {@link Optional} containing the matching {@link Syntax} if present, otherwise an empty {@link Optional}
     */
    @Override
    public Optional<Syntax> getSyntax(String syntaxId)
    {
        return Optional.ofNullable(this.syntaxes.get(syntaxId));
    }

    /**
     * Resolve a Syntax for the given identifier by returning a registered entry or constructing one from the identifier.
     *
     * <p>If no registered syntax matches the provided id, the id is parsed using the format "name/version" to build a new
     * Syntax instance; the resulting Syntax's type will use the parsed name (or a known SyntaxType matching that name).
     *
     * @param syntaxId the syntax identifier to resolve, expected in the form "name/version"
     * @return the registered Syntax matching the id, or a newly constructed Syntax parsed from the id
     * @throws ParseException if {@code syntaxId} is {@code null} or does not follow the required "name/version" format
     */
    @Override
    public Syntax resolveSyntax(String syntaxId) throws ParseException
    {
        // Try to find the syntax in the registered list and if not there, fallback to parsing the syntax id string.
        // However note that this means that the returned syntax's name type will default to the syntax id type.
        return getSyntax(syntaxId).orElse(valueOf(syntaxId));
    }

    /**
     * Parse a syntax identifier string and return the corresponding {@link Syntax}.
     *
     * <p>The input must be in the form "name/version". If a known {@link SyntaxType} for the parsed name exists, that
     * type is used; otherwise a new {@link SyntaxType} is created with the parsed name used as both id and human-readable
     * name.</p>
     *
     * @param syntaxIdAsString the syntax identifier string, formatted as "name/version"
     * @return the parsed {@link Syntax} combining the resolved {@link SyntaxType} and the parsed version
     * @throws ParseException if {@code syntaxIdAsString} is {@code null} or does not match the required "name/version"
     *         format
     */
    private Syntax valueOf(String syntaxIdAsString) throws ParseException
    {
        if (syntaxIdAsString == null) {
            throw new ParseException("The passed Syntax cannot be NULL");
        }

        Matcher matcher = SYNTAX_PATTERN.matcher(syntaxIdAsString);
        if (!matcher.matches()) {
            throw new ParseException(String.format("Invalid Syntax format [%s]", syntaxIdAsString));
        }

        String syntaxId = matcher.group(1);
        String version = matcher.group(2);

        // For well-known syntaxes, get the Syntax Name from the registered SyntaxType, otherwise use the id as both
        // the human readable name and the technical id (since the syntax string doesn't contain any information about
        // the pretty name of a syntax type).
        SyntaxType syntaxType = SyntaxType.getSyntaxTypes().get(syntaxId);
        if (syntaxType == null) {
            syntaxType = new SyntaxType(syntaxId, syntaxId);
        }

        return new Syntax(syntaxType, version);
    }

    /**
     * Register the given syntax in the registry.
     *
     * @param syntax the Syntax to register; it is stored keyed by its id string (returned by {@code syntax.toIdString()})
     */
    private void registerSyntax(Syntax syntax)
    {
        this.syntaxes.put(syntax.toIdString(), syntax);
    }

    /**
     * Remove the given syntax from the registry.
     *
     * @param syntax the Syntax to unregister; the entry with the same id string will be removed if present
     */
    private void unregisterSyntax(Syntax syntax)
    {
        this.syntaxes.remove(syntax.toIdString());
    }
}
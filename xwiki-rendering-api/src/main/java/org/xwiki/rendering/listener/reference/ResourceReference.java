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
package org.xwiki.rendering.listener.reference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.xwiki.text.XWikiToStringStyle;

/**
 * Represents a reference to a Resource (document, image, attachment, mail, etc).
 * Note that this representation is independent of any wiki syntax.
 *
 * @version $Id$
 * @since 2.5RC1
 */
public class ResourceReference implements Cloneable
{
    /**
     * @see #isTyped()
     */
    private boolean typed = true;

    /**
     * @see #getReference()
     *
     * Note that the reason we store the reference as a String and not as an Entity Reference is because we want
     * the Rendering module independent of the XWiki Model so that it can be used independently of XWiki.
     */
    private String reference;

    /**
     * @see #getBaseReferences()
     *
     * Note that the reason we store the base reference as a String and not as an Entity Reference is because we want
     * the Rendering module independent of the XWiki Model so that it can be used independently of XWiki.
     */
    private List<String> baseReferences;

    /**
     * @see #getType()
     */
    private ResourceType type;

    /**
     * @see #getParameter(String).
     */
    private Map<String, String> parameters = new LinkedHashMap<>();

    /**
     * Create a ResourceReference with the given reference and resource type.
     *
     * @param reference the reference string of the resource
     * @param type the resource's type
     */
    public ResourceReference(String reference, ResourceType type)
    {
        setReference(reference);
        setType(type);
    }

    /**
     * Set whether the resource type was explicitly provided.
     *
     * @param typed `true` if the resource type was explicitly provided, `false` otherwise
     */
    public void setTyped(boolean typed)
    {
        this.typed = typed;
    }

    /**
     * Indicates whether the resource type was explicitly provided (for example, "type:reference").
     *
     * @return {@code true} if the resource type was explicitly provided, {@code false} otherwise.
     */
    public boolean isTyped()
    {
        return this.typed;
    }

    /**
     * Set the resource reference string.
     *
     * @param reference the reference identifying the resource, or {@code null} to unset it
     */
    public void setReference(String reference)
    {
        this.reference = reference;
    }

    /**
     * Get the resource's reference string.
     *
     * The reference identifies the resource and may be a document name, a URI, a URL, an InterWiki reference, etc.
     *
     * @return the reference string (for example "wiki:space.page", "mailto:john@doe.com", a URL, or an InterWiki reference)
     * @see #getType()
     */
    public String getReference()
    {
        return this.reference;
    }

    /**
     * Adds a base reference to the list of base references used to resolve this resource.
     *
     * @param baseReference the base reference to add to the resolution chain
     */
    public void addBaseReference(String baseReference)
    {
        if (this.baseReferences == null) {
            this.baseReferences = new ArrayList<String>();
        }
        this.baseReferences.add(baseReference);
    }

    /**
     * Adds multiple base references to this resource.
     *
     * Each element in the provided list is added as a base reference in iteration order.
     *
     * @param baseReferences the list of base reference strings to add
     */
    public void addBaseReferences(List<String> baseReferences)
    {
        for (String baseReference : baseReferences) {
            addBaseReference(baseReference);
        }
    }

    /**
     * Get base references used to resolve non-absolute resource references.
     *
     * @return an unmodifiable list of base reference strings in resolution order (first to last); empty list if no base
     *         references are set
     */
    public List<String> getBaseReferences()
    {
        List<String> result;
        if (this.baseReferences == null) {
            result = Collections.emptyList();
        } else {
            result = Collections.unmodifiableList(this.baseReferences);
        }
        return result;
    }

    /**
     * Gets the resource's type.
     *
     * @return the resource type
     * @see ResourceType
     */
    public ResourceType getType()
    {
        return this.type;
    }

    /**
     * Sets the type of this resource reference.
     *
     * @param type the resource type to assign to this reference, or {@code null} to unset it
     * @see ResourceType
     */
    public void setType(ResourceType type)
    {
        this.type = type;
    }

    /**
     * Adds or updates a named parameter on this resource reference.
     *
     * @param name  the parameter key
     * @param value the parameter value; if a parameter with the same name already exists its value is replaced
     */
    public void setParameter(String name, String value)
    {
        this.parameters.put(name, value);
    }

    /**
     * Merge the provided parameter entries into this reference's parameter map.
     *
     * @param parameters the parameter entries to add; entries with the same name replace existing ones
     * @throws NullPointerException if {@code parameters} is {@code null}
     */
    public void setParameters(Map<String, String> parameters)
    {
        this.parameters.putAll(parameters);
    }

    /**
     * Remove the parameter with the given name from this reference's parameter map.
     *
     * @param name the parameter name to remove; if no parameter exists with this name the method does nothing
     */
    public void removeParameter(String name)
    {
        this.parameters.remove(name);
    }

    /**
     * Retrieve an extension parameter value for this resource reference.
     *
     * <p>Supported parameter names depend on the target renderer/syntax (for example, XWiki Syntax 2.1 supports
     * {@code "queryString"} and {@code "anchor"}).</p>
     *
     * @param name the parameter name
     * @return the parameter value, or {@code null} if no such parameter is set
     */
    public String getParameter(String name)
    {
        return this.parameters.get(name);
    }

    /**
     * Get the parameters map preserving the order in which parameters were added.
     *
     * @return an unmodifiable map of parameter names to parameter values in insertion order
     */
    public Map<String, String> getParameters()
    {
        return Collections.unmodifiableMap(this.parameters);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The output is syntax independent since this class is used for all syntaxes. Specific syntaxes should extend this
     * class and override this method to perform syntax-dependent formatting.</p>
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        // TODO: This needs to be changed but it involves changing a lot of unit tests
        XWikiToStringStyle style = new XWikiToStringStyle();
        style.setSeparator("");
        ToStringBuilder builder = new ToStringBuilder(this, style);

        builder = builder.append("Typed", isTyped()).append("Type", getType());

        if (getReference() != null) {
            builder = builder.append("Reference", getReference());
        }

        if (!getBaseReferences().isEmpty()) {
            builder = builder.append("Base References", getBaseReferences());
        }

        Map<String, String> params = getParameters();
        if (!params.isEmpty()) {
            builder = builder.append("Parameters", params);
        }

        return builder.toString();
    }

    /**
     * Create a copy of this ResourceReference with its mutable collections duplicated.
     *
     * The returned instance is a clone whose baseReferences and parameters are independent copies of the original's
     * collections.
     *
     * @return a cloned ResourceReference with cloned mutable fields
     * @throws RuntimeException if the object cannot be cloned (wraps CloneNotSupportedException)
     */
    @Override
    public ResourceReference clone()
    {
        ResourceReference clone;
        try {
            clone = (ResourceReference) super.clone();
        } catch (CloneNotSupportedException e) {
            // Should never happen
            throw new RuntimeException("Failed to clone object", e);
        }

        // Really clone the mutable fields
        if (this.baseReferences != null) {
            this.baseReferences = new ArrayList<>(this.baseReferences);
        }
        this.parameters = new LinkedHashMap<>(this.parameters);

        return clone;
    }

    /**
     * Compute a hash code based on the resource's type, typed flag, reference, base references, and parameters.
     *
     * @return the hash code value computed from the resource's type, typed flag, reference, base references, and parameters
     */
    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(1, 9)
            .append(getType())
            .append(isTyped())
            .append(getReference())
            .append(getBaseReferences())
            .append(getParameters())
            .toHashCode();
    }

    /**
     * Determine whether the given object is equal to this ResourceReference.
     *
     * @param object the object to compare with this ResourceReference
     * @return `true` if the object is a ResourceReference with the same type, typed flag, reference, base references,
     *         and parameters; `false` otherwise
     */
    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof ResourceReference)) {
            return false;
        }
        if (object == this) {
            return true;
        }
        ResourceReference rhs = (ResourceReference) object;
        return new EqualsBuilder()
            .append(getType(), rhs.getType())
            .append(isTyped(), rhs.isTyped())
            .append(getReference(), rhs.getReference())
            .append(getBaseReferences(), rhs.getBaseReferences())
            .append(getParameters(), rhs.getParameters())
            .isEquals();
    }
}
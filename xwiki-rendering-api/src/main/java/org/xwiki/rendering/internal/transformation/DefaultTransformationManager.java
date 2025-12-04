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
package org.xwiki.rendering.internal.transformation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.xwiki.component.annotation.Component;
import org.xwiki.component.manager.ComponentLookupException;
import org.xwiki.component.manager.ComponentManager;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.configuration.RenderingConfiguration;
import org.xwiki.rendering.transformation.RenderingContext;
import org.xwiki.rendering.transformation.Transformation;
import org.xwiki.rendering.transformation.TransformationContext;
import org.xwiki.rendering.transformation.TransformationException;
import org.xwiki.rendering.transformation.TransformationManager;

/**
 * Calls all existing transformations (executed by priority) on an existing XDOM object to generate a new transformed
 * XDOM.
 *
 * @version $Id$
 * @since 1.5M2
 */
@Component
@Singleton
public class DefaultTransformationManager implements TransformationManager
{
    /**
     * Used to get the ordered list of transformations to execute.
     */
    @Inject
    protected RenderingConfiguration configuration;

    /**
     * Used to updated the rendering context.
     */
    @Inject
    private RenderingContext renderingContext;

    /**
     * The logger to log.
     */
    @Inject
    private Logger logger;

    /**
     * Used to look up transformations at runtime.
     */
    @Inject
    @Named("context")
    private Provider<ComponentManager> componentManagerProvider;

    /**
     * Execute all configured Transformations, in order, on the given XDOM block within the provided transformation context.
     *
     * @param block the XDOM block to transform
     * @param context the transformation context that provides contextual data for the transformations
     * @throws TransformationException if one or more transformations fail; the exception message aggregates each failed
     *         transformation's class name and its stack trace
     */
    @Override
    public void performTransformations(Block block, TransformationContext context) throws TransformationException
    {
        Map<String, String> transformationsInError = null;
        for (Transformation transformation : getTransformations()) {
            try {
                ((MutableRenderingContext) this.renderingContext).transformInContext(transformation, context, block);
            } catch (Exception e) {
                // Continue running the other transformations
                if (transformationsInError == null) {
                    transformationsInError = new HashMap<>();
                }
                transformationsInError.put(transformation.getClass().getName(),
                    ExceptionUtils.getStackTrace(e));
            }
        }
        if (transformationsInError != null) {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, String> entry : transformationsInError.entrySet()) {
                builder.append(String.format("- Transformation: [%s]\n", entry.getKey()));
                builder.append(entry.getValue());
            }
            throw new TransformationException(String.format("The following transformations failed to execute "
                + "properly: [\n%s]", builder.toString()));
        }
    }

    /**
     * Obtain the configured Transformation implementations, ordered by their execution priority.
     *
     * @return the ordered list of Transformation instances to execute
     */
    public List<Transformation> getTransformations()
    {
        return getTransformations(this.configuration.getTransformationNames());
    }

    /**
     * Build the list of Transformation instances corresponding to the provided ordered list of transformation hints.
     *
     * Unresolvable hints are ignored (a warning is logged) and do not abort resolution; the resulting list is
     * sorted by each transformation's priority before being returned.
     *
     * @param transformationNames the ordered list of transformation names (hints) to resolve
     * @return the list of resolved Transformations, ordered by priority
     */
    protected List<Transformation> getTransformations(List<String> transformationNames)
    {
        List<Transformation> transformations = new ArrayList<>();
        for (String hint : transformationNames) {
            try {
                transformations.add(this.componentManagerProvider.get().getInstance(Transformation.class, hint));
            } catch (ComponentLookupException e) {
                this.logger.warn("Failed to locate transformation with hint [{}], ignoring it. "
                    + "Root reason [{}]", hint, ExceptionUtils.getRootCauseMessage(e));
            }
        }
        Collections.sort(transformations);
        return transformations;
    }
}
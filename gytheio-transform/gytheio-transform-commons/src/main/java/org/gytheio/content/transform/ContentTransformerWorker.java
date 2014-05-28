/*
 * Copyright (C) 2005-2014 Alfresco Software Limited.
 *
 * This file is part of Gytheio
 *
 * Gytheio is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Gytheio is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Gytheio. If not, see <http://www.gnu.org/licenses/>.
 */
package org.gytheio.content.transform;

import java.util.List;

import org.gytheio.content.ContentReference;
import org.gytheio.content.ContentWorkResult;
import org.gytheio.content.ContentWorker;
import org.gytheio.content.transform.options.TransformationOptions;

/**
 * Defines the methods responsible for doing the work of transformation of a content reference
 * 
 * @author Ray Gauss II
 */
public interface ContentTransformerWorker extends ContentWorker
{
    /**
     * Transforms the given source content references into the given target content references
     * with the given options, reporting back via the given progress reporter.
     * <p>
     * Depending on the transformation being requests, multiple sources may be merged into a single target,
     * a single source may be split into multiple targets, multiple sources may be transformed to 
     * multiple targets, etc.  It is up to the caller to know what type of targets to expect.
     * <p>
     * Additionally, some implementations may not be able to transform into the targets specified or
     * the number of targets may not be known before hand so implementations must return the 
     * final target references.
     * 
     * @param sources the list of source content references
     * @param targets the list of target content references which may be null indicating targets should be created
     * @param options
     * @param progressReporter
     * @return the final target references
     * @throws Exception
     */
    public List<ContentWorkResult> transform(
            List<ContentReference> sources, 
            List<ContentReference> targets,
            TransformationOptions options,
            ContentTransformerWorkerProgressReporter progressReporter) throws Exception;
}

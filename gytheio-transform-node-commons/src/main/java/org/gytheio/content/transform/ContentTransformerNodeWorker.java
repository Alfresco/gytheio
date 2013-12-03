/*
 * Copyright (C) 2005-2013 Alfresco Software Limited.
 *
 * This file is part of an Alfresco messaging investigation
 *
 * The Alfresco messaging investigation is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Alfresco messaging investigation is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the Alfresco messaging investigation. If not, see <http://www.gnu.org/licenses/>.
 */
package org.gytheio.content.transform;

import org.gytheio.content.ContentReference;
import org.gytheio.content.transform.options.TransformationOptions;

/**
 * Defines the methods responsible for doing the work of transformation of a content reference
 * 
 * @author Ray Gauss II
 */
public interface ContentTransformerNodeWorker
{
    public void transform(
            ContentReference source, 
            ContentReference target,
            TransformationOptions options,
            ContentTransformerNodeWorkerProgressReporter progressReporter) throws Exception;
}

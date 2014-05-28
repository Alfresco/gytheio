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
package org.gytheio.content.node;

import java.util.Properties;

import org.gytheio.content.AbstractComponent;
import org.gytheio.content.handler.ContentReferenceHandler;
import org.gytheio.content.hash.AbstractContentHashWorker;
import org.gytheio.content.hash.BaseContentHashComponent;

/**
 * Bootraps a hash component
 * 
 * @author Ray Gauss II
 *
 * @param <W>
 */
public class HashComponentBootstrapFromProperties<W extends AbstractContentHashWorker> extends 
        AbstractComponentBootstrapFromProperties<W>
{
    public HashComponentBootstrapFromProperties(Properties properties, W worker)
    {
        super(properties, worker);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected AbstractComponent createComponent()
    {
        return new BaseContentHashComponent();
    }
    
    protected void initWorker()
    {
        ContentReferenceHandler sourceHandler = createContentReferenceHandler(
                PROP_WORKER_CONTENT_REF_HANDLER_SOURCE_PREFIX);
        worker.setSourceContentReferenceHandler(sourceHandler);
        worker.initialize();
    }

}

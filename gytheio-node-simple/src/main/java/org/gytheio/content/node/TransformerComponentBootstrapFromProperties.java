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
import org.gytheio.content.transform.AbstractContentTransformerWorker;
import org.gytheio.content.transform.BaseContentTransformerComponent;

/**
 * Bootstraps a transformer component
 * 
 * @author Ray Gauss II
 *
 * @param <W>
 */
public class TransformerComponentBootstrapFromProperties<W extends AbstractContentTransformerWorker> extends 
        AbstractComponentBootstrapFromProperties<W>
{
    protected static final String PROP_WORKER_CONTENT_REF_HANDLER_TARGET_PREFIX = 
            "gytheio.worker.contentrefhandler.target";
    
    public TransformerComponentBootstrapFromProperties(Properties properties, W worker)
    {
        super(properties, worker);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected AbstractComponent createComponent()
    {
        return new BaseContentTransformerComponent();
    }
    
    protected void initWorker()
    {
        ContentReferenceHandler sourceHandler = createContentReferenceHandler(
                PROP_WORKER_CONTENT_REF_HANDLER_SOURCE_PREFIX);
        worker.setSourceContentReferenceHandler(sourceHandler);
        
        ContentReferenceHandler targetHandler = createContentReferenceHandler(
                PROP_WORKER_CONTENT_REF_HANDLER_TARGET_PREFIX);
        worker.setTargetContentReferenceHandler(targetHandler);
        
        worker.initialize();
    }

}

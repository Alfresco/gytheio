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
package org.gytheio.health.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.gytheio.health.ComponentUnavailableExceptionHandler;

/**
 * Camel Processor and standard bean which provide methods to pass the exception
 * to the specified handler.
 */
public class ExceptionProcessor implements Processor
{
    private ComponentUnavailableExceptionHandler handler;

    public void setHandler(ComponentUnavailableExceptionHandler handler)
    {
        this.handler = handler;
    }

    @Override
    public void process(Exchange exchange) throws Exception
    {
        Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        handler.handle(cause);
    }
    
    public void onReceive(Object body)
    {
        // Handler can only deal with Throwables
        if (body instanceof Throwable)
        {
            handler.handle((Throwable) body);
        }
    }

}

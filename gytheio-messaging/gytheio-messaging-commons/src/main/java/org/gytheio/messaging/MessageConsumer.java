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
package org.gytheio.messaging;

/**
 * Defines methods for handling messages.  A separate message listener is 
 * responsible for pulling messages off a queue and passing them to the consumer.
 * 
 * @author Ray Gauss II
 */
public interface MessageConsumer
{

    /**
     * Performs any processing required upon receiving the given POJO message
     * 
     * @param message
     */
    public void onReceive(Object message);
    
    /**
     * The class of POJO messages expected
     * 
     * @return the POJO message class
     */
    public Class<?> getConsumingMessageBodyClass();
    
}

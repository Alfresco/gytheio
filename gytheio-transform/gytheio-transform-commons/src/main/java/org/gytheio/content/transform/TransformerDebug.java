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

import org.gytheio.content.transform.options.TransformationOptionPair;

/**
 * Generates logging for transformers.
 * <p>
 * Currently contains the minimum contract required by {@link TransformationOptionPair}.
 * 
 * @author Alan Davis
 * @author Ray Gauss II
 *
 */
public interface TransformerDebug
{
    public boolean isEnabled();
    
    public <T extends Throwable> T setCause(T t);
    
    public void debug(String message);
    
    public void debug(String message, Throwable t);
}

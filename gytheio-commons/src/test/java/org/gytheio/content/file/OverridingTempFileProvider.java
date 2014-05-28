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
package org.gytheio.content.file;

/**
 * Class which overrides TempFileProvider's temp file dir for testing
 * 
 * @author Ray Gauss II
 */
public class OverridingTempFileProvider extends TempFileProvider
{
    public static final String OVERRIDE_TEMP_FILE_DIR = "GytheioTest";

    protected static String getApplicationTempFileDir()
    {
        return OVERRIDE_TEMP_FILE_DIR;
    }
    
}

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
package org.gytheio.content.handler.tempfile;

import org.gytheio.content.file.FileProviderImpl;
import org.gytheio.content.file.CleaningTempFileProvider;
import org.gytheio.content.handler.FileContentReferenceHandlerImpl;

/**
 * A convenience FileContentReferenceHandlerImpl extension which creates a file
 * provider with a directory path of the {@link CleaningTempFileProvider}'s temp dir.
 * 
 * @author Ray Gauss II
 */
public class TempFileContentReferenceHandlerImpl extends FileContentReferenceHandlerImpl
{
    public TempFileContentReferenceHandlerImpl()
    {
        super();
        FileProviderImpl fileProvider = new FileProviderImpl();
        fileProvider.setDirectoryPath(CleaningTempFileProvider.getTempDir().getPath());
        setFileProvider(fileProvider);
    }
}

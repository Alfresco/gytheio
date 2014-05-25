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
package org.gytheio.content.handler;

import java.io.File;

import org.gytheio.content.ContentIOException;
import org.gytheio.content.ContentReference;

/**
 * Adds file handling to the ContentReferenceHandler interface.
 */
public interface FileContentReferenceHandler extends ContentReferenceHandler
{

    /**
     * Gets a File object for the given content reference, optionally waiting for the
     * file to be available and match the expected file size.
     * <p>
     * This is useful for implementations that already use a file-based implementation
     * and can prevent workers from unnecessarily copying I/O streams.
     * 
     * @param contentReference
     * @param waitForTransfer
     * @return the File for the content reference
     * @throws ContentIOException
     * @throws InterruptedException
     */
    public File getFile(ContentReference contentReference, boolean waitForTransfer) throws ContentIOException, InterruptedException;
}

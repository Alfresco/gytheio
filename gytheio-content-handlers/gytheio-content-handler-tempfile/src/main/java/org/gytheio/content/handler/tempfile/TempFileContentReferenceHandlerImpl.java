package org.gytheio.content.handler.tempfile;

import org.gytheio.content.file.FileProviderImpl;
import org.gytheio.content.file.CleaningTempFileProvider;
import org.gytheio.content.handler.FileContentReferenceHandlerImpl;

/**
 * A convenience FileContentReferenceHandlerImpl extension which creates a file
 * provider with a directory path of the {@link CleaningTempFileProvider}'s temp dir.
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

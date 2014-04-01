package org.gytheio.content.handler;

import org.apache.commons.lang.StringUtils;
import org.gytheio.content.ContentReference;
import org.gytheio.content.file.FileProvider;
import org.gytheio.content.file.TempFileProvider;
import org.gytheio.content.handler.ContentReferenceHandler;
import org.gytheio.content.handler.FileContentReferenceHandlerImpl;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

public class FileContentReferenceHandlerImplTest
{
    private ContentReferenceHandler handler;
    
    @Before
    public void setUp()
    {
        FileProvider fileProvider = new TempFileProvider();
        handler = new FileContentReferenceHandlerImpl();
        ((FileContentReferenceHandlerImpl) handler).setFileProvider(fileProvider);
    }
    
    protected void checkReference(String fileName, String mediaType)
    {
        ContentReference reference = handler.createContentReference(fileName, mediaType);
        assertEquals(mediaType, reference.getMediaType());
        
        String uri = reference.getUri();
        String createdFileName = uri.split("\\/")[uri.split("\\/").length-1];
        
        String origPrefix = fileName.substring(0, StringUtils.lastIndexOf(fileName, "."));
        String origSuffix = fileName.substring(StringUtils.lastIndexOf(fileName, "."), fileName.length());
        assertTrue("ContentReference file name '" + createdFileName + 
                "' did not contain original file name prefix '" + origPrefix + "'", 
                createdFileName.contains(origPrefix));
        assertTrue("ContentReference file name '" + createdFileName + 
                "' did not contain original file name suffix '" + origPrefix + "'", 
                createdFileName.contains(origSuffix));
    }
    
    @Test
    public void testSimpleReference()
    {
        checkReference("myfile.txt", "text/plain");
    }
    
    @Test
    public void testPathReference()
    {
        checkReference("my.file.txt", "text/plain");
    }

}

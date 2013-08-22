package org.alfresco.content.handler;

import org.alfresco.content.ContentReference;
import org.alfresco.content.ContentReferenceUriImpl;
import org.alfresco.content.handler.ContentReferenceHandler;
import org.alfresco.content.handler.FileContentReferenceHandlerImpl;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

public class FileContentReferenceHandlerImplTest
{
    private ContentReferenceHandler handler;
    
    @Before
    public void setUp()
    {
        handler = new FileContentReferenceHandlerImpl();
    }
    
    protected void checkReference(String fileName, String mediaType)
    {
        ContentReference reference = handler.createContentReference(fileName, mediaType);
        assertEquals(mediaType, reference.getMediaType());
        
        String uri = ((ContentReferenceUriImpl) reference).getUri();
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

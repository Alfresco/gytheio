package org.alfresco.content.handler;

import java.io.File;
import java.io.InputStream;

import org.alfresco.content.ContentReference;
import org.alfresco.service.cmr.repository.ContentIOException;

public interface ContentReferenceHandler
{
    
    public boolean isContentReferenceSupported(ContentReference contentReference);
    
    public ContentReference createContentReference(String fileName, String mediaType) throws ContentIOException;
    
    public File getFile(ContentReference contentReference) throws ContentIOException;
    
    public void putFile(File sourceFile, ContentReference targetContentReference) throws ContentIOException;
    
    public InputStream getInputStream(ContentReference contentReference) throws ContentIOException;
    
    public void putInputStream(InputStream sourceInputStream, ContentReference targetContentReference) throws ContentIOException;
    
    public void delete(ContentReference contentReference) throws ContentIOException;
    
    public boolean isAvailable();
    
}

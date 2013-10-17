package org.alfresco.messaging.content.transport;

import java.io.InputStream;
import java.util.UUID;

import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.MimetypeService;
import org.gytheio.content.ContentReference;
import org.gytheio.content.handler.ContentReferenceHandler;

public class ContentTransportImpl implements ContentTransport
{
    
    private MimetypeService mimetypeService;
    private ContentReferenceHandler contentReferenceHandler;

    public void setMimetypeService(MimetypeService mimetypeService)
    {
        this.mimetypeService = mimetypeService;
    }

    public void setContentReferenceHandler(ContentReferenceHandler contentReferenceHandler)
    {
        this.contentReferenceHandler = contentReferenceHandler;
    }

    @Override
    public void write(ContentReader contentReader, ContentReference contentReference) throws Exception
    {
        InputStream inputStream = contentReader.getContentInputStream();
        contentReferenceHandler.putInputStream(inputStream, contentReference);
        inputStream.close();
    }

    @Override
    public void read(ContentReference contentReference, ContentWriter contentWriter) throws Exception
    {
        InputStream inputStream = contentReferenceHandler.getInputStream(contentReference);
        contentWriter.putContent(inputStream);
        inputStream.close();
    }

    @Override
    public ContentReference createContentReference(String mediaType) throws Exception
    {
        String tempExtenstion = mimetypeService.getExtension(mediaType);
        
        String fileName = this.getClass().getSimpleName() + "-" + UUID.randomUUID().toString() + "." + tempExtenstion;
        
        return contentReferenceHandler.createContentReference(fileName, mediaType);
    }

    @Override
    public boolean isAvailable()
    {
        // TODO Auto-generated method stub
        return false;
    }

}

package org.alfresco.content.transform;

import java.io.File;

import org.alfresco.content.ContentReference;
import org.alfresco.content.handler.ContentReferenceHandler;
import org.alfresco.content.transform.options.TransformationOptions;

public abstract class AbstractContentTransformerWorker implements ContentTransformerNodeWorker
{
    
    protected ContentReferenceHandler contentReferenceHandler;
    
    public void setContentReferenceHandler(ContentReferenceHandler contentReferenceFileHandler)
    {
        this.contentReferenceHandler = contentReferenceFileHandler;
    }

    public void transform(
            ContentReference source, 
            ContentReference target,
            TransformationOptions options,
            ContentTransformerNodeWorkerProgressReporter progressReporter) throws Exception
    {
        transformInternal(
                contentReferenceHandler.getFile(source), source.getMediaType(), 
                contentReferenceHandler.getFile(target), target.getMediaType(), 
                options,
                progressReporter);
    }
    
    protected abstract void transformInternal(
            File sourceFile, String sourceMimetype, 
            File targetFile, String targetMimetype,
            TransformationOptions options,
            ContentTransformerNodeWorkerProgressReporter progressReporter) throws Exception;

}

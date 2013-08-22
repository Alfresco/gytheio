package org.alfresco.repo.content.transform;

import org.alfresco.service.cmr.repository.ContentIOException;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.TransformationOptions;

public interface AsyncContentTransformer extends ContentTransformer
{
    
    /**
     * Transforms the content provided by the reader and source mimetype
     * to the writer and target mimetype with the provided transformation options.
     * <p>
     * The transformation viability can be determined by an up front call
     * to {@link #isTransformable(String, String, TransformationOptions)}.
     * <p>
     * The source and target mimetypes <b>must</b> be available on the
     * {@link org.alfresco.service.cmr.repository.ContentAccessor#getMimetype()} methods of
     * both the reader and the writer.
     * <p>
     * Both reader and writer will be closed after the transformation completes.
     * <p>
     * The provided options can be null.
     * 
     * @param  reader               the source of the content
     * @param  contentWriter        the destination of the transformed content    
     * @param  options              transformation options, these can be null
     * @param executeAsynchronously whether or not to execute asynchronously
     * @return                      the ID for the running transformation
     * @throws ContentIOException   if an IO exception occurs
     */
    public String transform(ContentReader reader, ContentWriter contentWriter, TransformationOptions options, boolean executeAsynchronously) 
        throws ContentIOException;

    public Float getProgress(String runningTransformationId);
}

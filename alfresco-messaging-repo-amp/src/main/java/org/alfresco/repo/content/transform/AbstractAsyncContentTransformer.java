package org.alfresco.repo.content.transform;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.alfresco.service.cmr.repository.ContentIOException;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentServiceTransientException;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.TransformationOptions;

public abstract class AbstractAsyncContentTransformer extends AbstractContentTransformer2 implements AsyncContentTransformer
{
    private static final Log logger = LogFactory.getLog(AbstractAsyncContentTransformer.class);

    public final String transform(ContentReader reader, ContentWriter writer, TransformationOptions options, boolean executeAsynchronously)
            throws ContentIOException
        {
            String transformationId = "";
            try
            {
                // depth.set(depth.get()+1);
                
                // begin timing
                long before = System.currentTimeMillis();
                
                String sourceMimetype = reader.getMimetype();
                String targetMimetype = writer.getMimetype();

                // check options map
                if (options == null)
                {
                    options = new TransformationOptions();
                }
                
                try
                {
                    if (transformerDebug.isEnabled())
                    {
                        transformerDebug.pushTransform(this, reader.getContentUrl(), sourceMimetype,
                                targetMimetype, reader.getSize(), options);
                    }
                    
                    // Check the transformability
                    checkTransformable(reader, writer, options);
                    
                    // Pass on any limits to the reader
                    setReaderLimits(reader, writer, options);

                    // Transform
                    transformationId = transformInternal(reader, writer, options, executeAsynchronously);
                    
                    // record time
                    long after = System.currentTimeMillis();
                    recordTime(sourceMimetype, targetMimetype, after - before);
                }
                catch (ContentServiceTransientException cste)
                {
                    // A transient failure has occurred within the content transformer.
                    // This should not be interpreted as a failure and therefore we should not
                    // update the transformer's average time.
                    if (logger.isDebugEnabled())
                    {
                        logger.debug("Transformation has been transiently declined: \n" +
                                "   reader: " + reader + "\n" +
                                "   writer: " + writer + "\n" +
                                "   options: " + options + "\n" +
                                "   transformer: " + this);
                    }
                    // the finally block below will still perform tidyup. Otherwise we're done.
                    // We rethrow the exception
                    throw cste;
                }
                catch (UnsupportedTransformationException e)
                {
                    // Don't record an error or even the time, as this is normal in compound transformations.
                    transformerDebug.debug("          Failed", e);
                    throw e;
                }
                catch (Throwable e)
                {
                    // Make sure that this transformation gets set back i.t.o. time taken.
                    // This will ensure that transformers that compete for the same transformation
                    // will be prejudiced against transformers that tend to fail
                    long after = System.currentTimeMillis();
                    recordError(sourceMimetype, targetMimetype, after - before);
                    
                    // Ask Tika to detect the document, and report back on if
                    //  the current mime type is plausible
                    String differentType = getMimetypeService().getMimetypeIfNotMatches(reader.getReader());
            
                    // Report the error
                    if(differentType == null)
                    {
                    transformerDebug.debug("          Failed", e);
                        throw new ContentIOException("Content conversion failed: \n" +
                               "   reader: " + reader + "\n" +
                               "   writer: " + writer + "\n" +
                               "   options: " + options.toString(false) + "\n" +
                               "   limits: " + getLimits(reader, writer, options),
                               e);
                    }
                    else
                    {
                   transformerDebug.debug("          Failed: Mime type was '"+differentType+"'", e);
                       throw new ContentIOException("Content conversion failed: \n" +
                             "   reader: " + reader + "\n" +
                             "   writer: " + writer + "\n" +
                             "   options: " + options.toString(false) + "\n" +
                             "   limits: " + getLimits(reader, writer, options) + "\n" +
                             "   claimed mime type: " + reader.getMimetype() + "\n" +
                             "   detected mime type: " + differentType,
                             e);
                    }
                }
                finally
                {
                    transformerDebug.popTransform();
                    
                    // check that the reader and writer are both closed
                    if (reader.isChannelOpen())
                    {
                        logger.error("Content reader not closed by transformer: \n" +
                                "   reader: " + reader + "\n" +
                                "   transformer: " + this);
                    }
                    if (writer.isChannelOpen())
                    {
                        logger.error("Content writer not closed by transformer: \n" +
                                "   writer: " + writer + "\n" +
                                "   transformer: " + this);
                    }
                }
                
                // done
                if (logger.isDebugEnabled())
                {
                    logger.debug("Completed transformation: \n" +
                            "   reader: " + reader + "\n" +
                            "   writer: " + writer + "\n" +
                            "   options: " + options + "\n" +
                            "   transformer: " + this);
                }
            }
            finally
            {
                // depth.set(depth.get()-1);
            }
            return transformationId;
        }
    
    protected abstract String transformInternal(ContentReader reader, ContentWriter writer, 
            TransformationOptions options, boolean executeAsynchronously) throws Exception;

}

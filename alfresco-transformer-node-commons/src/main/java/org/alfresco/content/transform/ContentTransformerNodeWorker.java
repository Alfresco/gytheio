package org.alfresco.content.transform;

import org.alfresco.content.ContentReference;
import org.alfresco.content.transform.options.TransformationOptions;

public interface ContentTransformerNodeWorker
{
    public void transform(
            ContentReference source, 
            ContentReference target,
            TransformationOptions options,
            ContentTransformerNodeWorkerProgressReporter progressReporter) throws Exception;
}

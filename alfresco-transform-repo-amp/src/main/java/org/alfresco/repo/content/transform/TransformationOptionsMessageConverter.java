package org.alfresco.repo.content.transform;

public interface TransformationOptionsMessageConverter
{

    public org.gytheio.content.transform.options.TransformationOptions 
        convert(org.alfresco.service.cmr.repository.TransformationOptions options);
    
}

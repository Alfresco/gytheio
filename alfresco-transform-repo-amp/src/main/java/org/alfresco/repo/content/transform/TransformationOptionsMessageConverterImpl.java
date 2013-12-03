package org.alfresco.repo.content.transform;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.alfresco.service.cmr.repository.TransformationSourceOptions;

public class TransformationOptionsMessageConverterImpl implements TransformationOptionsMessageConverter
{
    private static final Log logger = LogFactory.getLog(TransformationOptionsMessageConverterImpl.class);

    public org.gytheio.content.transform.options.TransformationOptions
            convert(org.alfresco.service.cmr.repository.TransformationOptions options)
    {
        org.gytheio.content.transform.options.TransformationOptionsImpl requestOptions = null;
        
        if (options instanceof org.alfresco.repo.content.transform.magick.ImageTransformationOptions)
        {
            requestOptions = 
                    new org.gytheio.content.transform.options.ImageTransformationOptions();
            ((org.gytheio.content.transform.options.ImageTransformationOptions) requestOptions).setResizeOptions(
                    ((org.alfresco.repo.content.transform.magick.ImageTransformationOptions) options).getResizeOptions());
            ((org.gytheio.content.transform.options.ImageTransformationOptions) requestOptions).setCommandOptions(
                    ((org.alfresco.repo.content.transform.magick.ImageTransformationOptions) options).getCommandOptions());
        }
        else
        {
            requestOptions = 
                    new org.gytheio.content.transform.options.TransformationOptionsImpl();
        }
        
        Map<String, Map<String, Serializable>> requestSourceOptions = 
                new HashMap<String, Map<String,Serializable>>();
        if (options.getSourceOptionsList() != null)
        {
            for (TransformationSourceOptions sourceOptions : options.getSourceOptionsList())
            {
                Map<String, Serializable> sourceOptionsParams = new HashMap<String, Serializable>();
                sourceOptions.getSerializer().serialize(sourceOptions, sourceOptionsParams);
                requestSourceOptions.put(sourceOptions.getClass().getCanonicalName(), sourceOptionsParams);
            }
        }
        
        requestOptions.setSourceOptionsList(options.getSourceOptionsList());
        
        if (logger.isDebugEnabled())
        {
            logger.debug("converted " + options.toString() + " to " + requestOptions.toString());
        }
        
        return requestOptions;
    }

}
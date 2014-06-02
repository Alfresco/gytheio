/*
 * Copyright (C) 2005-2014 Alfresco Software Limited.
 *
 * This file is part of Gytheio
 *
 * Gytheio is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Gytheio is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Gytheio. If not, see <http://www.gnu.org/licenses/>.
 */
package org.gytheio.content.transform.options;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Defines options and demarcations needed to describe the details of how
 * the source should be transformed, independent of the target requirements.
 * <p>
 * See {@link PagedSourceOptions} for an example implementation that
 * describes the page number that should be used from the source content.
 * 
 * @author Ray Gauss II
 */
public interface TransformationSourceOptions
{
    
    /**
     * Gets the list of applicable media types (mimetypes)
     * 
     * @return the applicable media types
     */
    public List<String> getApplicableMediaTypes();

    /**
     * Gets whether or not these transformation source options apply for the
     * given media type (mimetype)
     * 
     * @param mediaType the media type of the source
     * @return if these transformation source options apply
     */
    public boolean isApplicableForMediaType(String mediaType);
    
    /**
     * Creates a new <code>TransformationSourceOptions</code> object from this
     * one, merging any non-null overriding fields in the given
     * <code>overridingOptions</code>
     * 
     * @param overridingOptions
     * @return a merged <code>TransformationSourceOptions</code> object
     */
    public TransformationSourceOptions mergedOptions(TransformationSourceOptions overridingOptions);
    
    /**
     * Gets the serializer for the source options.
     * 
     * @return the serializer
     */
    public TransformationSourceOptionsSerializer getSerializer();
    
    /**
     * Defines methods for serializing the source options into a parameter map and
     * deserializing from a serialized options accessor.
     * <p>
     * This is primarily used when interacting with the {@link RenditionService}
     * with {@link AbstractRenderingEngine}'s RenderContext being an implementer
     * of this interface.
     */
    public interface TransformationSourceOptionsSerializer
    {
        
        /**
         * Serializes the given transformation source options into the given parameter map.
         * 
         * @param transformationSourceOptions
         * @param parameters
         */
        public void serialize(TransformationSourceOptions transformationSourceOptions, Map<String, Serializable> parameters);
        
        /**
         * Gets the parameters from the serialized options accessor and builds a source options object.
         * 
         * @param serializedOptions
         * @return the deserialized source options
         */
        public TransformationSourceOptions deserialize(SerializedTransformationOptionsAccessor serializedOptions);
        
    }

}



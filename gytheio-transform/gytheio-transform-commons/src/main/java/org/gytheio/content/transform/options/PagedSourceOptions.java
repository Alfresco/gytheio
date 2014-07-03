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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.gytheio.content.mediatype.FileMediaType;
import org.gytheio.content.transform.options.AbstractTransformationSourceOptions;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Paged content conversion options to specify a page number range.
 * <p>
 * The page numbering index starts with 1.
 * <p>
 * If only the start page number is specified transformers should attempt
 * a page range from that page number to the end if possible.
 * <p>
 * If only an end page number is specified transformers should attempt
 * a page range from the start to that page if possible.
 * 
 * @author Ray Gauss II
 */
public class PagedSourceOptions extends AbstractTransformationSourceOptions
{
    private static final long serialVersionUID = 1272460998965414717L;

    public static final Integer PAGE_1 = new Integer(1);

    /** The start of the page range in the source document */
    private Integer startPageNumber;
    
    /** The end of the page range in the source document */
    private Integer endPageNumber;
    
    protected static List<String> getDefaultApplicableMimetypes()
    {
        List<String> defaults = new ArrayList<String>(17);
        defaults.add(FileMediaType.PDF.getMediaType());
        defaults.add(FileMediaType.WORD.getMediaType());
        defaults.add(FileMediaType.PPT.getMediaType());
        defaults.add(FileMediaType.IMAGE_TIFF.getMediaType());
        defaults.add(FileMediaType.OPENDOCUMENT_PRESENTATION.getMediaType());
        defaults.add(FileMediaType.OPENDOCUMENT_PRESENTATION_TEMPLATE.getMediaType());
        defaults.add(FileMediaType.OPENDOCUMENT_TEXT_TEMPLATE.getMediaType());
        defaults.add(FileMediaType.OPENOFFICE1_WRITER.getMediaType());
        defaults.add(FileMediaType.OPENOFFICE1_IMPRESS.getMediaType());
        defaults.add(FileMediaType.OPENXML_PRESENTATION.getMediaType());
        defaults.add(FileMediaType.OPENXML_WORDPROCESSING.getMediaType());
        defaults.add(FileMediaType.STAROFFICE5_IMPRESS.getMediaType());
        defaults.add(FileMediaType.STAROFFICE5_IMPRESS_PACKED.getMediaType());
        defaults.add(FileMediaType.STAROFFICE5_WRITER.getMediaType());
        defaults.add(FileMediaType.STAROFFICE5_WRITER_GLOBAL.getMediaType());
        defaults.add(FileMediaType.IWORK_KEYNOTE.getMediaType());
        defaults.add(FileMediaType.IWORK_PAGES.getMediaType());
        defaults.add(FileMediaType.WORDPERFECT.getMediaType());
        return defaults;
    }
    
    public PagedSourceOptions()
    {
        super();
        setApplicableMediaTypes(PagedSourceOptions.getDefaultApplicableMimetypes());
    }
    
    /**
     * Gets the page number to start from in the source document
     * 
     * @return the start page number
     */
    @ToStringProperty
    public Integer getStartPageNumber()
    {
        return startPageNumber;
    }
    
    /**
     * Sets the page number to start from in the source document
     * 
     * @param startPageNumber the start page number
     */
    public void setStartPageNumber(Integer startPageNumber)
    {
        this.startPageNumber = startPageNumber;
    }

    /**
     * Gets the page number to end at in the source document
     * 
     * @return the start page number
     */
    @ToStringProperty
    public Integer getEndPageNumber()
    {
        return endPageNumber;
    }

    /**
     * Sets the page number to end at in the source document
     * 
     * @param endPageNumber the end page number
     */
    public void setEndPageNumber(Integer endPageNumber)
    {
        this.endPageNumber = endPageNumber;
    }

    @Override
    public TransformationSourceOptions mergedOptions(TransformationSourceOptions overridingOptions)
    {
        if (overridingOptions instanceof PagedSourceOptions)
        {
            PagedSourceOptions mergedOptions = (PagedSourceOptions) super.mergedOptions(overridingOptions);

            if (((PagedSourceOptions) overridingOptions).getStartPageNumber() != null)
            {
                mergedOptions.setStartPageNumber(((PagedSourceOptions) overridingOptions).getStartPageNumber());
            }
            if (((PagedSourceOptions) overridingOptions).getEndPageNumber() != null)
            {
                mergedOptions.setEndPageNumber(((PagedSourceOptions) overridingOptions).getEndPageNumber());
            }
            return mergedOptions;
        }
        return null;
    }
    
    /**
     * Gets paged source options which specify just the first page.
     * 
     * @return the page one source options
     */
    public static PagedSourceOptions getPage1Instance() {
        PagedSourceOptions sourceOptions = new PagedSourceOptions();
        sourceOptions.setStartPageNumber(PAGE_1);
        sourceOptions.setEndPageNumber(PAGE_1);
        return sourceOptions;
    }
    
    @Override
    @JsonIgnore
    public TransformationSourceOptionsSerializer getSerializer()
    {
        return PagedSourceOptions.createSerializerInstance();
    }
    
    /**
     * Creates an instance of the options serializer
     * 
     * @return the options serializer
     */
    public static TransformationSourceOptionsSerializer createSerializerInstance()
    {
        return (new PagedSourceOptions()).new PagedSourceOptionsSerializer();
    }
    
    /**
     * Serializer for paged source options
     */
    public class PagedSourceOptionsSerializer implements TransformationSourceOptionsSerializer
    {
        public static final String PARAM_SOURCE_START_PAGE = "source_start_page";
        public static final String PARAM_SOURCE_END_PAGE = "source_end_page";
        
//        @Override
        public TransformationSourceOptions deserialize(SerializedTransformationOptionsAccessor serializedOptions)
        {
            int startPageNumber = serializedOptions.getIntegerParam(PARAM_SOURCE_START_PAGE, 1);
            int endPageNumber = serializedOptions.getIntegerParam(PARAM_SOURCE_END_PAGE, 1);
            
            PagedSourceOptions sourceOptions = new PagedSourceOptions();
            sourceOptions.setStartPageNumber(startPageNumber);
            sourceOptions.setEndPageNumber(endPageNumber);
            return sourceOptions;
        }

//        @Override
        public void serialize(TransformationSourceOptions sourceOptions, 
                Map<String, Serializable> parameters)
        {
            if (parameters == null || sourceOptions == null)
            {
                return;
            }
            PagedSourceOptions pagedSourceOptions = (PagedSourceOptions) sourceOptions;
            parameters.put(PARAM_SOURCE_START_PAGE, pagedSourceOptions.getStartPageNumber());
            parameters.put(PARAM_SOURCE_END_PAGE, pagedSourceOptions.getEndPageNumber());
        }
    }
}

package org.alfresco.messaging.marshalling;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.io.Writer;

import org.alfresco.content.ContentReferenceUriImpl;
import org.alfresco.content.transform.TransformationRequest;
import org.alfresco.content.transform.options.ImageTransformationOptions;
import org.alfresco.messaging.jackson.ObjectMapperFactory;
import org.alfresco.repo.content.transform.magick.ImageResizeOptions;
import org.alfresco.service.cmr.repository.PagedSourceOptions;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonObjectMarshallerImplTest
{
    private static final Integer EXPECTED_SOURCE_OPTION_VALUE_END_PAGE = new Integer(7);

    private static final int EXPECTED_OPTION_VALUE_WIDTH = 640;
    private static final int EXPECTED_OPTION_VALUE_HEIGHT = 480;
    
    private ObjectMapper objectMapper;

    @Before
    public void setUp()
    {
        objectMapper = ObjectMapperFactory.createInstance();
    }
    
    @Test
    public void testTransformationRequestMarshalling() throws Exception
    {
        PagedSourceOptions sourceOptions = new PagedSourceOptions();
        sourceOptions.setEndPageNumber(EXPECTED_SOURCE_OPTION_VALUE_END_PAGE);
        
        ImageResizeOptions resizeOptions = new ImageResizeOptions();
        resizeOptions.setWidth(EXPECTED_OPTION_VALUE_WIDTH);
        resizeOptions.setHeight(EXPECTED_OPTION_VALUE_HEIGHT);
        
        ImageTransformationOptions options = new ImageTransformationOptions();
        
        options.addSourceOptions(sourceOptions);
        
        options.setResizeOptions(resizeOptions);
        
        ContentReferenceUriImpl source = new ContentReferenceUriImpl(
                "file:/tmp/Alfresco/TempFileContentTransportImpl-88e9011d-6ecf-4a68-8f6c-342d0cd30fee6858578792784676782.mov", 
                "video/quicktime");
        ContentReferenceUriImpl target = new ContentReferenceUriImpl(
                "file:/tmp/Alfresco/TempFileContentTransportImpl-be08c51b-4282-4040-8a58-a1e4cd1df1472999488129563166381.png", 
                "image/png");
        TransformationRequest request = 
                new TransformationRequest(source, target, options);
        
        
        Writer strWriter = new StringWriter();
        objectMapper.writeValue(strWriter, request);
        String requestString = strWriter.toString();
        
        assertNotNull(requestString);
//        System.out.println("requestString=" + requestString);
        assertTrue("Marshalled PagedSourceOptions did not contain the expected endPageNumber",
                requestString.contains("\"endPageNumber\":" + EXPECTED_SOURCE_OPTION_VALUE_END_PAGE));
    
        TransformationRequest unmarshalledRequest =
                objectMapper.readValue(requestString, TransformationRequest.class);
        
        assertNotNull("Request was null", unmarshalledRequest);
        assertNotNull("Transformation options were null", unmarshalledRequest.getOptions());
        
        ImageTransformationOptions unmarshalledOptions = (ImageTransformationOptions) unmarshalledRequest.getOptions();
       
        assertNotNull("Transformation resize options were null", unmarshalledOptions.getResizeOptions());
        
        String unmarshalledImproperlyMessage = "Transformation source options were not unmarshalled properly";
        assertEquals(unmarshalledImproperlyMessage,
                EXPECTED_OPTION_VALUE_WIDTH, unmarshalledOptions.getResizeOptions().getWidth());
        assertEquals(unmarshalledImproperlyMessage,
                EXPECTED_OPTION_VALUE_HEIGHT, unmarshalledOptions.getResizeOptions().getHeight());
        
        PagedSourceOptions unmarshalledSourceOptions =
                unmarshalledRequest.getOptions().getSourceOptions(PagedSourceOptions.class);
        assertNotNull("Transformation source options were null", unmarshalledSourceOptions);
        
        assertEquals(unmarshalledImproperlyMessage,
                EXPECTED_SOURCE_OPTION_VALUE_END_PAGE, unmarshalledSourceOptions.getEndPageNumber());
    }

}

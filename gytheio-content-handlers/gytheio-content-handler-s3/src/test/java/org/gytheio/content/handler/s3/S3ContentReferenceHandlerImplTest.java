/*
 * Copyright (C) 2005-2018 Alfresco Software Limited.
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

package org.gytheio.content.handler.s3;

import com.amazonaws.ClientConfiguration;
import org.apache.commons.lang.StringUtils;
import org.gytheio.content.ContentReference;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the s3 content reference handler
 * 
 * @author janv
 *  
 * @see {@link S3ContentReferenceHandlerImpl}
 */
public class S3ContentReferenceHandlerImplTest
{
    private static S3ContentReferenceHandlerImpl handler;

    //
    // If s3AccessKey / s3SecretKey are not overridden 
    // then use DefaultAWSCredentialsProviderChain which searches for credentials in this order:
    //
    // - Environment Variables - AWS_ACCESS_KEY_ID and AWS_SECRET_KEY
    // - Java System Properties - aws.accessKeyId and aws.secretKey
    // - Credential profiles file at the default location (~/.aws/credentials) shared by all AWS SDKs and the AWS CLI
    // - Credentials delivered through the Amazon EC2 container service if AWS_CONTAINER_CREDENTIALS_RELATIVE_URI env var is set
    //   and security manager has permission to access the var,
    // - Instance profile credentials delivered through the Amazon EC2 metadata service
    //
    private static String s3AccessKey = null; 
    private static String s3SecretKey = null;

    private static String s3BucketName = "alf-gytheio-s3-test-"+UUID.randomUUID().toString();

    private static String s3BucketRegion = null;

    // note: bucket must be empty
    private static boolean deleteTestBucket = true;

    @BeforeClass
    public static void setUp()
    {
        handler = new S3ContentReferenceHandlerImpl();

        handler.setS3AccessKey(s3AccessKey);
        handler.setS3SecretKey(s3SecretKey);

        handler.setS3BucketName(s3BucketName);
        handler.setS3BucketRegion(s3BucketRegion);

        // note: will attempt to create bucket if it does not exist
        handler.init();

        assertTrue("S3 not available !", handler.isAvailable());
    }

    @AfterClass
    public static void tearDown()
    {
    	if (deleteTestBucket)
    	{
	        try
	        {
	        	AmazonS3 s3 = S3ContentReferenceHandlerImpl.initClient(s3AccessKey, s3SecretKey, s3BucketRegion);
	            s3.deleteBucket(s3BucketName);
	        }
	        catch (AmazonClientException e)
	        {
	            throw e;
	        }
    	}
    }

    protected void checkReference(String fileName, String mediaType)
    {
        ContentReference reference = handler.createContentReference(fileName, mediaType);
        assertEquals(mediaType, reference.getMediaType());

        String uri = reference.getUri();
        String createdFileName = uri.split("\\/")[uri.split("\\/").length-1];

        String origPrefix = fileName.substring(0, StringUtils.lastIndexOf(fileName, "."));
        String origSuffix = fileName.substring(StringUtils.lastIndexOf(fileName, "."), fileName.length());
        assertTrue("ContentReference file name '" + createdFileName + 
                "' did not contain original file name prefix '" + origPrefix + "'", 
                createdFileName.contains(origPrefix));
        assertTrue("ContentReference file name '" + createdFileName + 
                "' did not contain original file name suffix '" + origPrefix + "'", 
                createdFileName.contains(origSuffix));
    }

    @Test
    public void testSimpleReference()
    {
        checkReference("myfile.txt", "text/plain");
    }

    @Test
    public void testPathReference()
    {
        checkReference("my.file.txt", "text/plain");
    }

    @Test
    public void testFileOps()
    {
        int LOOP_COUNT = ClientConfiguration.DEFAULT_MAX_CONNECTIONS + 5; // MM-682
        
        for (int i = 0; i < LOOP_COUNT; i++)
        {
            testFileOpsImpl();
        }
    }
    
    private void testFileOpsImpl()
    {
    	String uuid = UUID.randomUUID().toString();
        String fileName = "test-" + uuid + ".txt";

        ContentReference reference = handler.createContentReference(fileName, "text/plain");

        assertFalse(handler.isContentReferenceExists(reference));

        // create the S3 object

        byte[] dataIn = new byte[1024]; // 1 KiB
        new Random().nextBytes(dataIn);

        int contentLen = dataIn.length;
        try 
        {
        	reference.setSize((long)contentLen); // prevents AmazonS3Client warn

	    	ByteArrayInputStream bais = new ByteArrayInputStream(dataIn);
	        handler.putInputStream(bais, reference);

	        bais.close();
        }
        catch (IOException ioe)
        {
        	System.err.println(ioe.getMessage());
        }

        assertTrue(handler.isContentReferenceExists(reference));

        // get the S3 object
        try
        {
	        InputStream is = handler.getInputStream(reference, false);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1)
            {
                baos.write(buffer, 0, length);
            }

			baos.close();
			is.close();

            byte[] dataOut = baos.toByteArray();

			// check bytes
			assertTrue(Arrays.equals(dataOut, dataIn));
        }
        catch (IOException ioe)
        {
        	System.err.println(ioe.getMessage());
        }

        // delete the S3 object
        handler.delete(reference);

        assertFalse(handler.isContentReferenceExists(reference));
    }
}

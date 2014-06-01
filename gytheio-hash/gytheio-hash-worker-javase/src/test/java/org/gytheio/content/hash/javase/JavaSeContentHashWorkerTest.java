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
package org.gytheio.content.hash.javase;

import static junit.framework.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.gytheio.content.ContentReference;
import org.gytheio.content.ContentWorkResult;
import org.gytheio.content.handler.FileContentReferenceHandlerImpl;
import org.gytheio.content.hash.ContentHashWorker;
import org.gytheio.content.hash.javase.JavaSeContentHashWorker;
import org.junit.Before;
import org.junit.Test;

/**
 * Test of the Java SE content hash worker
 * 
 * @see {@link JavaSeContentHashWorker}
 * @author Ray Gauss II
 */
public class JavaSeContentHashWorkerTest
{
    
    private static final String EXPECTED_VALUE_MD5 = "fe974bb7f67f392239f077b61d649405";
    private static final String EXPECTED_VALUE_SHA_256 = "29d834b3b1623e55d88f9f61f53ca30f7f6f6d5233cda4c723a40b0237f577b";
    private static final String EXPECTED_VALUE_SHA_512 = "8f65c82745b7b54bcb0ad87d7540e82b" +
            "2616c27c632b9269fa6ce71c91e73b69b466cf356883e18a4993624449209693c429b79677c8713110aff5f8c2ed1aa3";
    
    private ContentHashWorker worker;
    
    @Before
    public void setUp() throws Exception {
        worker = new JavaSeContentHashWorker();
        ((JavaSeContentHashWorker) worker).setSourceContentReferenceHandler(
                new FileContentReferenceHandlerImpl());
        ((JavaSeContentHashWorker) worker).initialize();
    }
    
    @Test
    public void testVersion() throws Exception
    {
        assertTrue(worker.getVersionString().contains("Gytheio JavaSE Content Hash Worker"));
        assertTrue(worker.getVersionDetailsString().contains("JVM: 1"));
    }
    
    protected void testHash(String hashAlgorithm, String expectedValue) throws Exception
    {
        ContentReference source = new ContentReference(
                this.getClass().getResource("/quick/quick.mpg").toURI().toString(), "video/mpeg");
        
        List<ContentWorkResult> results = worker.generateHashes(Arrays.asList(source), hashAlgorithm);
        
        assertNotNull("Hash values was null", results);
        assertFalse("Hash values was empty", results.size() == 0);
        ContentWorkResult foundResult = null;
        for (ContentWorkResult result : results)
        {
            if (result.getContentReference().equals(source))
            {
                foundResult = result;
                break;
            }
        }
        assertNotNull("Hash values was null", foundResult);
        assertEquals(expectedValue, 
                foundResult.getDetails().get(ContentHashWorker.RESULT_DETAIL_HEX_ENCODED_VALUE));
    }
    
    @Test
    public void testMd5() throws Exception
    {
        testHash(ContentHashWorker.HASH_ALGORITHM_MD5, EXPECTED_VALUE_MD5);
    }
    
    @Test
    public void testSha256() throws Exception
    {
        testHash(ContentHashWorker.HASH_ALGORITHM_SHA_256, EXPECTED_VALUE_SHA_256);
    }
    
    @Test
    public void testSha512() throws Exception
    {
        testHash(ContentHashWorker.HASH_ALGORITHM_SHA_512, EXPECTED_VALUE_SHA_512);
    }
    
}

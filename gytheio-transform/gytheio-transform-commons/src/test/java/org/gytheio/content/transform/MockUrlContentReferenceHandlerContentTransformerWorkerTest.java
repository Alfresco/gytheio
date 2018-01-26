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
package org.gytheio.content.transform;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

import org.gytheio.content.ContentIOException;
import org.gytheio.content.ContentReference;
import org.gytheio.content.file.FileProvider;
import org.gytheio.content.file.FileProviderImpl;
import org.gytheio.content.file.TempFileProvider;
import org.gytheio.content.handler.ContentReferenceHandler;
import org.gytheio.content.handler.FileContentReferenceHandlerImpl;
import org.gytheio.content.mediatype.FileMediaType;
import org.gytheio.content.transform.options.TransformationOptions;
import org.junit.Before;
import org.junit.Test;

public class MockUrlContentReferenceHandlerContentTransformerWorkerTest extends AbstractContentTransformerWorkerTest
{
    protected static final String TEST_RESOURCE_CLASSPATH = "/quick/quick.mpg";
    protected static final FileMediaType TEST_RESOURCE_MEDIATYPE = FileMediaType.VIDEO_MPG;
    protected static final String EXCEPTION_MESSAGE_NULL_SIZE = "Size matters";

    private ContentTransformerWorker transformerWorker;
    private ContentReferenceHandler contentReferenceHandler;
    private File sourceFile;
    private ContentReference source;

    @Before
    public void setUp() throws Exception {
        sourceFile = new File(this.getClass().getResource(TEST_RESOURCE_CLASSPATH).toURI());
        source = new ContentReference(
                sourceFile.toURI().toString(), TEST_RESOURCE_MEDIATYPE.getMediaType(), sourceFile.length());

        FileProvider fileProvider = new FileProviderImpl(TempFileProvider.getTempDir().getPath());
        ContentReferenceHandler delegateContentReferenceHandler = new FileContentReferenceHandlerImpl();
        ((FileContentReferenceHandlerImpl) delegateContentReferenceHandler).setFileProvider(fileProvider);

        contentReferenceHandler = new TargetSizeRequiredContentReferenceHandler();
        ((TargetSizeRequiredContentReferenceHandler) contentReferenceHandler).setDelegateContentReferenceHandler(
                delegateContentReferenceHandler);

        transformerWorker = new MockCopyingContentTransformerWorker();
        ((MockCopyingContentTransformerWorker) transformerWorker).setSourceContentReferenceHandler(contentReferenceHandler);
        ((MockCopyingContentTransformerWorker) transformerWorker).setTargetContentReferenceHandler(contentReferenceHandler);
        ((MockCopyingContentTransformerWorker) transformerWorker).initialize();
    }

    @Test
    public void testTransformTargetSize() throws Exception
    {
        // make a writer for the target file
        File targetFile = TempFileProvider.createTempFile(
                getClass().getSimpleName() + "_quick_" + TEST_RESOURCE_MEDIATYPE.getExtension() + "_",
                "." + TEST_RESOURCE_MEDIATYPE.getExtension());

        ContentReference target = new ContentReference(
                targetFile.toURI().toString(), TEST_RESOURCE_MEDIATYPE.getMediaType());

        try
        {
            transformerWorker.transform(
                    Arrays.asList(source),
                    Arrays.asList(target),
                    null,
                    null);
        }
        catch(IllegalArgumentException e)
        {
            if (EXCEPTION_MESSAGE_NULL_SIZE.equals(e.getMessage()))
            {
                fail("Target size was null");
            }
            else
            {
                // some other exception, we should still throw it
                throw e;
            }
        }
    }

    /**
     * Content reference handler that throws an {@link IllegalArgumentException} if the target
     * content reference does not contain a size.
     */
    public class TargetSizeRequiredContentReferenceHandler implements ContentReferenceHandler
    {
        private ContentReferenceHandler delegateContentReferenceHandler;

        public void setDelegateContentReferenceHandler(ContentReferenceHandler delegateContentReferenceHandler)
        {
            this.delegateContentReferenceHandler = delegateContentReferenceHandler;
        }

        @Override
        public boolean isContentReferenceSupported(ContentReference contentReference)
        {
            return delegateContentReferenceHandler.isContentReferenceSupported(contentReference);
        }

        @Override
        public boolean isContentReferenceExists(ContentReference contentReference)
        {
            return delegateContentReferenceHandler.isContentReferenceExists(contentReference);
        }

        @Override
        public ContentReference createContentReference(String fileName, String mediaType) throws ContentIOException
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public InputStream getInputStream(ContentReference contentReference, boolean waitForAvailability)
                throws ContentIOException, InterruptedException
        {
            return delegateContentReferenceHandler.getInputStream(contentReference, waitForAvailability);
        }

        @Override
        public long putInputStream(InputStream sourceInputStream, ContentReference targetContentReference)
                throws ContentIOException
        {
            if (targetContentReference.getSize() == null)
            {
                throw new IllegalArgumentException(EXCEPTION_MESSAGE_NULL_SIZE);
            }
            return delegateContentReferenceHandler.putInputStream(sourceInputStream, targetContentReference);
        }

        @Override
        public void delete(ContentReference contentReference) throws ContentIOException
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isAvailable()
        {
            return delegateContentReferenceHandler.isAvailable();
        }
    }

    /**
     * Content transformer worker that just copied the first source to the first target
     */
    public class MockCopyingContentTransformerWorker extends AbstractFileContentTransformerWorker
    {
        @Override
        public boolean isTransformable(List<String> sourceMediaTypes, String targetMediaType,
                TransformationOptions options)
        {
            return true;
        }

        @Override
        protected List<File> transformInternal(List<FileContentReferencePair> sources,
                List<FileContentReferencePair> targets, TransformationOptions options,
                ContentTransformerWorkerProgressReporter progressReporter) throws Exception
        {
            // copy the source to the target
            Files.copy(sources.iterator().next().getFile().toPath(), targets.iterator().next().getFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
            return Arrays.asList(targets.iterator().next().getFile());
        }
    }
}
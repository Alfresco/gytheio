package org.alfresco.service.cmr.repository.hash;

import java.util.concurrent.Future;

import org.alfresco.service.cmr.repository.ContentIOException;
import org.alfresco.service.cmr.repository.ContentReader;

public interface HashService
{
    
    public Future<String> generateHashAsync(ContentReader reader, String hashAlgorithm) throws ContentIOException;
    
    public String generateHash(ContentReader reader, String hashAlgorithm) throws ContentIOException;

}

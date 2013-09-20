package org.alfresco.repo.content.hash;

import org.alfresco.service.cmr.repository.ContentIOException;
import org.alfresco.service.cmr.repository.ContentReader;

public interface HashService
{
    
    public String hash(ContentReader reader) throws ContentIOException;

}

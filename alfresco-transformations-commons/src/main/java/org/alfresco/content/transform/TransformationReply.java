/*
 * Copyright (C) 2005-2013 Alfresco Software Limited.
 *
 * This file is part of an Alfresco messaging investigation
 *
 * The Alfresco messaging investigation is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Alfresco messaging investigation is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the Alfresco messaging investigation. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.content.transform;

import org.alfresco.content.ContentReference;

/**
 * Represents a reply from a content transformer on the status of a transformation request.
 */
public class TransformationReply
{
    public static final String STATUS_IN_PROGRESS = "in-progress";
    public static final String STATUS_COMPLETE = "complete";
    public static final String STATUS_ERROR = "error";
    
    private String transformationRequestId;
    private ContentReference targetContentReference;
    private String status;
    private Float progress;
    private String detail;
    
    public TransformationReply() {
        super();
    }
    
    public TransformationReply(TransformationRequest request)
    {
        super();
        this.transformationRequestId = request.getTransformationRequestId();
        this.targetContentReference = request.getTargetContentReference();
    }

    /**
     * Gets the UUID for the original transformation request
     * 
     * @return the transformation request ID
     */
    public String getTransformationRequestId()
    {
        return transformationRequestId;
    }

    /**
     * Sets the UUID for the original transformation request
     * 
     * @param transformationRequestId
     */
    public void setTransformationRequestId(String transformationRequestId)
    {
        this.transformationRequestId = transformationRequestId;
    }

    /**
     * Gets the target content reference object.
     * <p>
     * This is usually the same as the content reference provided in the original
     * transformation request.
     * 
     * @return the target content reference
     */
    public ContentReference getTargetContentReference()
    {
        return targetContentReference;
    }

    /**
     * Sets the target content reference object.
     * <p>
     * This is usually the same as the content reference provided in the original
     * transformation request.
     * 
     * @param targetContentReference
     */
    public void setTargetContentReference(ContentReference targetContentReference)
    {
        this.targetContentReference = targetContentReference;
    }

    /**
     * Gets the status of the transformation
     * 
     * @return the status
     */
    public String getStatus()
    {
        return status;
    }

    /**
     * Sets the status of the transformation
     * 
     * @param status
     */
    public void setStatus(String status)
    {
        this.status = status;
    }

    /**
     * Gets the percentage progress of the transformation
     * 
     * @return the transformation progress
     */
    public Float getProgress()
    {
        return progress;
    }

    /**
     * Sets the percentage progress of the transformation
     * 
     * @param progress
     */
    public void setProgress(Float progress)
    {
        this.progress = progress;
    }

    /**
     * Gets further detail on the status of the transformation, possibly messages relating to an error
     * 
     * @return the transformation status detail
     */
    public String getDetail()
    {
        return detail;
    }

    /**
     * Sets further detail on the status of the transformation, possibly messages relating to an error
     * 
     * @param detail
     */
    public void setDetail(String detail)
    {
        this.detail = detail;
    }

}

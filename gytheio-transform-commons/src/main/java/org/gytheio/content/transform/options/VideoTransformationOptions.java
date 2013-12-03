/*
 * Copyright (C) 2005-2013 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */
package org.gytheio.content.transform.options;

/**
 * Options relating to video transformations
 * 
 * @author Ray Gauss II
 */
public class VideoTransformationOptions extends AudioTransformationOptions
{
    
    public static final String VIDEO_CODEC_H264 = "h264";
    public static final String VIDEO_CODEC_MPEG4 = "mpeg4";
    public static final String VIDEO_CODEC_THEORA = "theora";
    public static final String VIDEO_CODEC_VP6 = "vp6";
    public static final String VIDEO_CODEC_VP7 = "vp7";
    public static final String VIDEO_CODEC_VP8 = "vp8";
    public static final String VIDEO_CODEC_WMV = "wmv";

    private String targetVideoCodec;
    private Integer targetVideoBitrate;
    
    /**
     * Gets the video codec to use for the target of the transformation
     * 
     * @return the target video codec
     */
    public String getTargetVideoCodec()
    {
        return targetVideoCodec;
    }
    
    /**
     * Sets the video codec to use for the target of the transformation
     * 
     * @param targetVideoCodec
     */
    public void setTargetVideoCodec(String targetVideoCodec)
    {
        this.targetVideoCodec = targetVideoCodec;
    }
    
    /**
     * Gets the video bitrate to use for the target of the transformation
     * 
     * @return the target video bitrate
     */
    public Integer getTargetVideoBitrate()
    {
        return targetVideoBitrate;
    }
    
    /**
     * Sets the video bitrate to use for the target of the transformation
     * 
     * @param targetVideoBitrate
     */
    public void setTargetVideoBitrate(Integer targetVideoBitrate)
    {
        this.targetVideoBitrate = targetVideoBitrate;
    }
    
}

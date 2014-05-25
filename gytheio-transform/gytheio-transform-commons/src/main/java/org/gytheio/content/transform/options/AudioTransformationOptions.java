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

/**
 * Options relating to audio transformations
 * 
 * @author Ray Gauss II
 *
 */
public class AudioTransformationOptions extends TransformationOptionsImpl
{

    public static final String AUDIO_CODEC_AAC = "aac";
    public static final String AUDIO_CODEC_MP3 = "mp3";
    public static final String AUDIO_CODEC_VORBIS = "vorbis";
    public static final String AUDIO_CODEC_WMA = "wma";
    
    private String targetAudioCodec;
    private Integer targetAudioBitrate;
    private Integer targetAudioSamplingrate;
    private Integer targetAudioChannels;

    /**
     * Gets the audio codec to use for the target of the transformation
     * 
     * @return the target audio codec
     */
    public String getTargetAudioCodec()
    {
        return targetAudioCodec;
    }

    /**
     * Sets the audio codec to use for the target of the transformation
     * 
     * @param targetAudioCodec
     */
    public void setTargetAudioCodec(String targetAudioCodec)
    {
        this.targetAudioCodec = targetAudioCodec;
    }

    /**
     * Gets the audio bitrate to use for the target of the transformation
     * 
     * @return the target audio bitrate
     */
    public Integer getTargetAudioBitrate()
    {
        return targetAudioBitrate;
    }

    /**
     * Sets the audio bitrate to use for the target of the transformation
     * 
     * @param targetAudioBitrate
     */
    public void setTargetAudioBitrate(Integer targetAudioBitrate)
    {
        this.targetAudioBitrate = targetAudioBitrate;
    }

    /**
     * Gets the audio sampling rate to use for the target of the transformation
     * 
     * @return the target audio sampling rate
     */
    public Integer getTargetAudioSamplingrate()
    {
        return targetAudioSamplingrate;
    }

    /**
     * Sets the audio sampling rate to use for the target of the transformation
     * 
     * @param targetAudioSamplingrate
     */
    public void setTargetAudioSamplingrate(Integer targetAudioSamplingrate)
    {
        this.targetAudioSamplingrate = targetAudioSamplingrate;
    }

    /**
     * Gets the number of audio channels to use for the target of the transformation 
     * 
     * @return the number of target audio channels
     */
    public Integer getTargetAudioChannels()
    {
        return targetAudioChannels;
    }

    /**
     * Sets the number of audio channels to use for the target of the transformation 
     * 
     * @param targetAudioChannels
     */
    public void setTargetAudioChannels(Integer targetAudioChannels)
    {
        this.targetAudioChannels = targetAudioChannels;
    }
    
}

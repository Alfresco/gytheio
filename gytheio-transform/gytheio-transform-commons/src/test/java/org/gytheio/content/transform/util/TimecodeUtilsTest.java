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
package org.gytheio.content.transform.util;

import org.junit.Test;

import static junit.framework.Assert.*;

public class TimecodeUtilsTest
{

    @Test
    public void testSecondsToTimecode()
    {
        float seconds = 50f;
        assertEquals("00:00:50.000", TimecodeUtils.convertSecondsToTimecode(seconds));
        
        seconds = 70f;
        assertEquals("00:01:10.000", TimecodeUtils.convertSecondsToTimecode(seconds));
        
        seconds = 5f;
        assertEquals("00:00:05.000", TimecodeUtils.convertSecondsToTimecode(seconds));
        
        seconds = 5.011f;
        assertEquals("00:00:05.011", TimecodeUtils.convertSecondsToTimecode(seconds));
        
        seconds = (60*60*2) + (60*11) + 22.123f;
        assertEquals("02:11:22.123", TimecodeUtils.convertSecondsToTimecode(seconds));
        
        seconds = 5f;
        assertEquals("00:00:05.0", TimecodeUtils.convertSecondsToTimecode(seconds, 1));
    }
    
    @Test
    public void testTimecodeToSeconds()
    {
        String timecode = "00:00:50.000";
        assertEquals(50f, TimecodeUtils.convertTimecodeToSeconds(timecode));
        
        timecode = "00:01:10.000";
        assertEquals(70f, TimecodeUtils.convertTimecodeToSeconds(timecode));
        
        timecode = "00:00:05.000";
        assertEquals(5f, TimecodeUtils.convertTimecodeToSeconds(timecode));
        
        timecode = "00:00:05.011";
        assertEquals(5.011f, TimecodeUtils.convertTimecodeToSeconds(timecode));
        
        timecode = "00:00:11.07";
        assertEquals(11.07f, TimecodeUtils.convertTimecodeToSeconds(timecode));
        
        timecode = "02:11:22.123";
        assertEquals((60*60*2) + (60*11) + 22.123f, TimecodeUtils.convertTimecodeToSeconds(timecode));
    }

}

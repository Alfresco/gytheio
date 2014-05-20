package org.gytheio.content.file;

public class OverridingTempFileProvider extends TempFileProvider
{
    public static final String OVERRIDE_TEMP_FILE_DIR = "GytheioTest";

    protected static String getApplicationTempFileDir()
    {
        return OVERRIDE_TEMP_FILE_DIR;
    }
    
}

/*
 * Copyright (C) 2005-2017 Alfresco Software Limited.
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
package org.gytheio.messaging.camel.dataformat;

import java.util.HashMap;

public class SimplePojo
{
    public enum EnumValue { VALUE_1, VALUE_2 }
    
    private String field1;
    private Integer field2;
    private EnumValue field3;
    private HashMap<Class<?>, String> field4;
    private HashMap<String, String> field5;
    
    public SimplePojo()
    {
    }
    
    public SimplePojo(String field1, Integer field2, EnumValue field3)
    {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
    }
    
    public String getField1()
    {
        return field1;
    }
    public void setField1(String field1)
    {
        this.field1 = field1;
    }
    public Integer getField2()
    {
        return field2;
    }
    public void setField2(Integer field2)
    {
        this.field2 = field2;
    }
    public EnumValue getField3()
    {
        return field3;
    }
    public void setField3(EnumValue field3)
    {
        this.field3 = field3;
    }
    public HashMap<Class<?>, String> getField4()
    {
        return field4;
    }
    public void setField4(HashMap<Class<?>, String> field4)
    {
        this.field4 = field4;
    }
    public HashMap<String, String> getField5()
    {
        return field5;
    }
    public void setField5(HashMap<String, String> field5)
    {
        this.field5 = field5;
    }
}

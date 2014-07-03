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

import java.beans.Introspector;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Concrete implementation of transformation options
 * 
 * @author Ray Gauss II
 */
public class TransformationOptionsImpl implements TransformationOptions
{
    private static final long serialVersionUID = -4466824587314591239L;
    
    protected static final String TO_STR_KEY_VAL = ": ";
    protected static final String TO_STR_OBJ_START = "{ ";
    protected static final String TO_STR_OBJ_END = " }";
    protected static final String TO_STR_SET_START = "[ ";
    protected static final String TO_STR_SET_END = " ]";
    protected static final String TO_STR_DEL = ", ";

    /** Source options based on its mimetype */
    private Map<Class<? extends TransformationSourceOptions>, TransformationSourceOptions> sourceOptionsMap;
    
    /** The include embedded resources yes/no */
    private Boolean includeEmbedded;
    
    private Map<String, Serializable> additionalOptions;
    
    /** Time, KBytes and page limits */
    private TransformationOptionLimits limits = new TransformationOptionLimits();

    @Override
    public Map<Class<? extends TransformationSourceOptions>, TransformationSourceOptions> getSourceOptionsMap()
    {
        return sourceOptionsMap;
    }

    @Override
    public void setSourceOptionsMap(
            Map<Class<? extends TransformationSourceOptions>, TransformationSourceOptions> sourceOptionsMap)
    {
        this.sourceOptionsMap = sourceOptionsMap;
    }
    
    @Override
    public void setSourceOptionsList(Collection<TransformationSourceOptions> sourceOptionsList)
    {
        if (sourceOptionsList != null)
        {
            for (TransformationSourceOptions sourceOptions : sourceOptionsList)
            {
                addSourceOptions(sourceOptions);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends TransformationSourceOptions> T getSourceOptions(Class<T> clazz)
    {
        if (sourceOptionsMap == null)
            return null;
        return (T) sourceOptionsMap.get(clazz);
    }
    
    @Override
    public void addSourceOptions(TransformationSourceOptions sourceOptions)
    {
        if (sourceOptionsMap == null)
        {
            sourceOptionsMap = new HashMap<Class<? extends TransformationSourceOptions>, TransformationSourceOptions>(1);
        }
        TransformationSourceOptions newOptions = sourceOptions;
        TransformationSourceOptions existingOptions = sourceOptionsMap.get(sourceOptions.getClass());
        if (existingOptions != null)
        {
            newOptions = existingOptions.mergedOptions(sourceOptions);
        }
        sourceOptionsMap.put(sourceOptions.getClass(), newOptions);
    }
    
    @Override
    public long getTimeoutMs()
    {
        return limits.getTimeoutMs();
    }

    @Override
    public void setTimeoutMs(long timeoutMs)
    {
        limits.setTimeoutMs(timeoutMs);
    }
    
    @Override
    public int getPageLimit()
    {
        return limits.getPageLimit();
    }
    
    @Override
    public void setIncludeEmbedded(Boolean includeEmbedded) 
    {
       this.includeEmbedded = includeEmbedded;
    }

    @Override
    public Boolean getIncludeEmbedded() 
    {
        return includeEmbedded;
    }
    
    @Override
    public void setPageLimit(int pageLimit)
    {
        limits.setPageLimit(pageLimit);
    }

    @Override
    public Map<String, Serializable> getAdditionalOptions()
    {
        return additionalOptions;
    }

    @Override
    public void setAdditionalOptions(Map<String, Serializable> additionalOptions)
    {
        this.additionalOptions = additionalOptions;
    }
    
    /**
     * Builds the source options to string.
     * <p>
     * We can't rely on a framework here because we need full serialization
     * for messaging.
     * 
     * @return the source options toString
     */
    protected String toStringSourceOptions()
    {
        StringBuilder output = new StringBuilder();
        output.append("\"sourceOptions\"").append(TO_STR_KEY_VAL).append(TO_STR_OBJ_START);
        if (sourceOptionsMap != null)
        {
            for (Iterator<TransformationSourceOptions> iterator = sourceOptionsMap.values().iterator(); iterator.hasNext();)
            {
                TransformationSourceOptions sourceOptions = (TransformationSourceOptions) iterator.next();
                output.append("\"").
                    append(Introspector.decapitalize(sourceOptions.getClass().getSimpleName())).
                    append("\"").append(TO_STR_KEY_VAL);
                output.append(TO_STR_OBJ_START).append(sourceOptions.toString()).append(TO_STR_OBJ_END);
                if (iterator.hasNext())
                {
                    output.append(TO_STR_DEL);
                }
            }
        }
        output.append(TO_STR_OBJ_END);
        return output.toString();
    }
    
    @Override
    public String toString()
    {
        StringBuilder output = new StringBuilder();
        output.append(TO_STR_OBJ_START);
        output.append(toStringSourceOptions());
        output.append(TO_STR_OBJ_END);
        return output.toString();
    }
    
    /**
     * Builds the JSON toString of the given object through methods
     * annotated with {@link ToStringProperty}.
     * <p>
     * We can't rely on a framework here because we need full serialization
     * for messaging.
     * 
     * @param object
     * @return the toString representation of the object
     */
    protected static String toString(Object object)
    {
        if (object == null)
        {
            return "null";
        }
        StringBuilder builder = new StringBuilder();
        Method[] methods = object.getClass().getMethods();
        ArrayList<Method> annotatedMethods = new ArrayList<Method>();
        for (int i = 0; i < methods.length; i++)
        {
            Method method = methods[i];
            if (method.isAnnotationPresent(ToStringProperty.class))
            {
                annotatedMethods.add(method);
            }
        }
        for (Iterator<Method> iterator = annotatedMethods.iterator(); iterator.hasNext();)
        {
            Method method = (Method) iterator.next();
            
            Object value = "*error*";
            try
            {
                value = method.invoke(object);
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
            {
            }
            String methodName = Introspector.decapitalize(method.getName().startsWith("get") ? 
                    method.getName().replaceFirst("get",  "") : method.getName());
            
            builder.append("\"").append(methodName).append("\"").append(TransformationOptionsImpl.TO_STR_KEY_VAL);
            if (value instanceof String)
            {
                builder.append("\"").append(value).append("\"");
            }
            else
            {
                builder.append(value);
            }
            if (iterator.hasNext())
            {
                builder.append(TO_STR_DEL);
            }
        }
        return builder.toString();
    }
}

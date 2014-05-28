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
package org.gytheio.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to denote a class or method as part
 * of the set of stable, unchanging elements.
 * <p>
 * When a class or method 
 * is so designated it will not change within a minor release,
 * i.e. 1.1 -> 1.2, in a way that would make it no longer backwardly compatible 
 * with an earlier version within the release.
 * <p>
 * Classes or methods with the annotation may still change in major releases,
 * i.e. 1.x -> 2.0.
 * 
 * @author Greg Melahn
 * @author Ray Gauss II
 *
 */
@Target( {ElementType.TYPE,ElementType.METHOD} )
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StableApi
{
}

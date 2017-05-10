/*
 * Copyright (c) 2011, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.sun.javafx.test;

import java.lang.reflect.Method;
import com.sun.javafx.test.binding.ReflectionHelper;

public final class BuilderProxy {
    private final Class<?> builderClass;
    private final Method createMethod;
    private final Method applyToMethod;

    private BuilderProxy(final Class<?> builderClass,
                         final Method createMethod,
                         final Method applyToMethod) {
        this.builderClass = builderClass;
        this.createMethod = createMethod;
        this.applyToMethod = applyToMethod;
    }

    public Object createBuilder() {
        return ReflectionHelper.invokeMethod(null, createMethod);
    }

    public PropertyReference createPropertyReference(
            final String propertyName,
            final Class<?> propertyValueType) {
        return createForBuilder(builderClass,
                                propertyName,
                                propertyValueType);
    }

    public void applyTo(final Object builder, final Object bean) {
        ReflectionHelper.invokeMethod(builder, applyToMethod, bean);
    }

    public static BuilderProxy createForBean(final Class<?> beanClass) {
        final String builderClassName = beanClass.getName() + "Builder";

        Class<?> builderClass;
        try {
            builderClass = Class.forName(builderClassName);
        } catch (final ClassNotFoundException e) {
            // if there is no builder for the bean, we return null
            return null;
        }

        return new BuilderProxy(
                           builderClass, 
                           ReflectionHelper.getMethod(builderClass, "create"),
                           ReflectionHelper.getMethod(builderClass,
                                                      "applyTo",
                                                      beanClass));
    }

    public static PropertyReference createForBuilder(
            final Class<?> builderClass,
            final String propertyName,
            final Class<?> propertyValueType) {
        try {
            final Method propertySetterMethod =
                    ReflectionHelper.getMethod(
                            builderClass,
                            propertyName,
                            propertyValueType);

            return new PropertyReference(
                               propertyName,
                               propertyValueType,
                               null,
                               propertySetterMethod);
        } catch (final RuntimeException e) {
            throw new RuntimeException("Failed to obtain setter for "
                                           + propertyName + "!");
        }
    }

}

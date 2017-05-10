/*
 * Copyright (c) 2010, 2013, Oracle and/or its affiliates. All rights reserved.
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

package com.sun.javafx.fxml;

/**
 * Load listener interface.
 */
public interface LoadListener {
    /**
     * Called when the loader has read an import processing instruction.
     *
     * @param target
     */
    public void readImportProcessingInstruction(String target);

    /**
     * Called when the loader has read a language processing instruction.
     *
     * @param language
     */
    public void readLanguageProcessingInstruction(String language);

    /**
     * Called when the loader has read a comment.
     *
     * @param comment
     */
    public void readComment(String comment);

    /**
     * Called when the loader has begun reading an instance declaration
     * element.
     *
     * @param type
     */
    public void beginInstanceDeclarationElement(Class<?> type);

    /**
     * Called when the loader has begun reading an instance declaration
     * element for an unknown type.
     *
     * @param name
     */
    public void beginUnknownTypeElement(String name);

    /**
     * Called when the loader has begun reading an include element.
     */
    public void beginIncludeElement();

    /**
     * Called when the loader has begun reading a reference element.
     */
    public void beginReferenceElement();

    /**
     * Called when the loader has begun reading a copy element.
     */
    public void beginCopyElement();

    /**
     * Called when the loader has begun reading a root element.
     */
    public void beginRootElement();

    /**
     * Called when the loader has begun reading a property element.
     *
     * @param name
     * @param sourceType
     */
    public void beginPropertyElement(String name, Class<?> sourceType);

    /**
     * Called when the loader has begun reading a static property element
     * defined by an unknown type.
     *
     * @param name
     * @param sourceType
     */
    public void beginUnknownStaticPropertyElement(String name);

    /**
     * Called when the loader has begun reading a script element.
     */
    public void beginScriptElement();

    /**
     * Called when the loader has begun reading a define element.
     */
    public void beginDefineElement();

    /**
     * Called when the loader has read an internal attribute.
     *
     * @param name
     * @param value
     */
    public void readInternalAttribute(String name, String value);

    /**
     * Called when the loader has read a property attribute.
     *
     * @param name
     * @param sourceType
     * @param value
     */
    public void readPropertyAttribute(String name, Class<?> sourceType, String value);

    /**
     * Called when the loader has read an unknown static property attribute.
     */
    public void readUnknownStaticPropertyAttribute(String name, String value);

    /**
     * Called when the loader has read an event handler attribute.
     *
     * @param name
     * @param value
     */
    public void readEventHandlerAttribute(String name, String value);

    /**
     * Called when the loader has finished reading an element.
     *
     * @param value
     */
    public void endElement(Object value);
}

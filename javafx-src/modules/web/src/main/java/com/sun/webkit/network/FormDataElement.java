/*
 * Copyright (c) 2011, 2013, Oracle and/or its affiliates. All rights reserved.
 */
package com.sun.webkit.network;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A form data element, such as a byte array or a local file.
 */
abstract class FormDataElement {
    
    /**
     * The input stream from which the content of this element
     * can be read.
     */
    private InputStream inputStream;


    /**
     * Opens this element and makes its size and content available
     * for retrieval.
     */
    void open() throws IOException {
        inputStream = createInputStream();
    }

    /**
     * Returns the size of this element's content in bytes.
     */
    long getSize() {
        if (inputStream == null) {
            throw new IllegalStateException();
        }
        return doGetSize();
    }
    
    /**
     * Returns the input stream from which the content of this element
     * can be read.
     */
    InputStream getInputStream() {
        if (inputStream == null) {
            throw new IllegalStateException();
        }
        return inputStream;
    }

    /**
     * Closes this element and releases all resources associated with it.
     * This method makes the element's size and content unavailable
     * for retrieval.
     * This method is a no op if the element is already closed.
     */
    void close() throws IOException {
        if (inputStream != null) {
            inputStream.close();
            inputStream = null;
        }
    }

    /**
     * Creates the input stream from which the content of this element
     * can be read.
     */
    protected abstract InputStream createInputStream() throws IOException;

    /**
     * Returns the size of this element's content in bytes.
     */
    protected abstract long doGetSize();


    /**
     * Creates a new FormDataElement from a byte array.
     */
    private static FormDataElement fwkCreateFromByteArray(byte[] byteArray) {
        return new ByteArrayElement(byteArray);
    }

    /**
     * Creates a new FormDataElement from a file.
     */
    private static FormDataElement fwkCreateFromFile(String fileName) {
        return new FileElement(fileName);
    }

    /**
     * A form data element based on a byte array.
     */
    private static final class ByteArrayElement extends FormDataElement {

        private final byte[] byteArray;


        private ByteArrayElement(byte[] byteArray) {
            this.byteArray = byteArray;
        }


        @Override
        protected InputStream createInputStream() {
            return new ByteArrayInputStream(byteArray);
        }

        @Override
        protected long doGetSize() {
            return byteArray.length;
        }
    }

    /**
     * A form data element based on a file.
     */
    private static final class FileElement extends FormDataElement {

        private final File file;


        private FileElement(String filename) {
            file = new File(filename);
        }


        @Override
        protected InputStream createInputStream() throws IOException {
            return new BufferedInputStream(new FileInputStream(file));
        }

        @Override
        protected long doGetSize() {
            return file.length();
        }
    }
}

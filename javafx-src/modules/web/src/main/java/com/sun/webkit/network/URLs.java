/*
 * Copyright (c) 2011, 2013, Oracle and/or its affiliates. All rights reserved.
 */
package com.sun.webkit.network;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A collection of static methods for URL creation.
 */
public final class URLs {

    /**
     * The mapping between WebPane-specific protocol names and their
     * respective handlers.
     */
    private static final Map<String,URLStreamHandler> handlerMap;
    static {
        Map<String,URLStreamHandler> map =
                new HashMap<String,URLStreamHandler>(2);
        map.put("about", new com.sun.webkit.network.about.Handler());
        map.put("data", new com.sun.webkit.network.data.Handler());
        handlerMap = Collections.unmodifiableMap(map);
    }


    /**
     * The private default constructor. Ensures non-instantiability.
     */
    private URLs() {
        throw new AssertionError();
    }


    /**
     * Creates a {@code URL} object from the {@code String} representation.
     * This method is equivalent to the {@link URL#URL(String)} constructor
     * with the additional support for WebPane-specific protocol handlers.
     * @param spec the {@code String} to parse as a {@code URL}.
     * @throws MalformedURLException if the string specifies an unknown
     *         protocol.
     */
    public static URL newURL(String spec) throws MalformedURLException {
        return newURL(null, spec);
    }

    /**
     * Creates a URL by parsing the given spec within a specified context.
     * This method is equivalent to the {@link URL#URL(URL,String)}
     * constructor with the additional support for WebPane-specific protocol
     * handlers.
     * @param context the context in which to parse the specification.
     * @param spec the {@code String} to parse as a {@code URL}.
     * @throws MalformedURLException if no protocol is specified, or an
     *         unknown protocol is found.
     */
    public static URL newURL(URL context, String spec)
        throws MalformedURLException
    {
        try {
            // Try the standard protocol handler selection procedure
            return new URL(context, spec);
        } catch (MalformedURLException ex) {
            // Try WebPane-specific protocol handler, if any
            URLStreamHandler handler = null;
            int colonPosition = spec.indexOf(':');
            if (colonPosition != -1) {
                handler = handlerMap.get(
                        spec.substring(0, colonPosition).toLowerCase());
            }
            if (handler == null) {
                throw ex;
            }
            return new URL(context, spec, handler);
        }
    }
}

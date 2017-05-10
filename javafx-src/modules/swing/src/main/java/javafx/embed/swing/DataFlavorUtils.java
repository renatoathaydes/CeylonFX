/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
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

package javafx.embed.swing;

import javafx.scene.input.DataFormat;

import java.io.ByteArrayOutputStream;
import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collections;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;


final class DataFlavorUtils {

    static String getFxMimeType(final DataFlavor flavor) {
        return flavor.getPrimaryType() + "/" + flavor.getSubType();
    }

    static Object adjustFxData(final DataFlavor flavor, final Object fxData)
            throws UnsupportedEncodingException
    {
        // TBD: Handle more data types!!!
        if (fxData instanceof String) {
            if (flavor.isRepresentationClassInputStream()) {
                final String encoding = flavor.getParameter("charset");
                return new ByteArrayInputStream(encoding != null
                        ? ((String) fxData).getBytes(encoding)
                        : ((String) fxData).getBytes());
            }
            if (flavor.isRepresentationClassByteBuffer()) {
                // ...
            }
        }
        return fxData;
    }

    static Object adjustSwingData(final DataFlavor flavor,
                                  final String mimeType,
                                  final Object swingData)
    {
        if (flavor.isFlavorJavaFileListType()) {
            // RT-12663
            final List<File> fileList = (List<File>)swingData;
            final String[] paths = new String[fileList.size()];
            int i = 0;
            for (File f : fileList) {
                paths[i++] = f.getPath();
            }
            return paths;
        }
        DataFormat dataFormat = DataFormat.lookupMimeType(mimeType);
        if (DataFormat.PLAIN_TEXT.equals(dataFormat)) {
            if (flavor.isFlavorTextType()) {
                if (swingData instanceof InputStream) {
                    InputStream in = (InputStream)swingData;
                    // TBD: charset
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] bb = new byte[64];
                    try {
                        int len = in.read(bb);
                        while (len != -1) {
                            out.write(bb, 0, len);
                            len = in.read(bb);
                        }
                        out.close();
                        return new String(out.toByteArray());
                    } catch (Exception z) {
                        // ignore
                    }
                }
            } else if (swingData != null) {
                return swingData.toString();
            }
        }
        return swingData;
    }

    static Map<String, DataFlavor> adjustSwingDataFlavors(final DataFlavor[] flavors) {
        // Group data flavors by FX mime type.
        final Map<String, Set<DataFlavor>> mimeType2Flavors =
                new HashMap<>(flavors.length);
        for (DataFlavor flavor : flavors) {
            final String mimeType = getFxMimeType(flavor);
            if (mimeType2Flavors.containsKey(mimeType)) {
                final Set<DataFlavor> mimeTypeFlavors = mimeType2Flavors.get(
                        mimeType);
                try {
                    mimeTypeFlavors.add(flavor);
                } catch (UnsupportedOperationException e) {
                    // List of data flavors corresponding to FX mime
                    // type has been finalized already.
                }
            } else {
                Set<DataFlavor> mimeTypeFlavors = new HashSet<DataFlavor>();

                // If this is text data flavor use DataFlavor representing
                // a Java Unicode String class. This is what FX expects from
                // clipboard.
                if (flavor.isFlavorTextType()) {
                    mimeTypeFlavors.add(DataFlavor.stringFlavor);
                    mimeTypeFlavors = Collections.unmodifiableSet(
                            mimeTypeFlavors);
                } else {
                    mimeTypeFlavors.add(flavor);
                }

                mimeType2Flavors.put(mimeType, mimeTypeFlavors);
            }
        }

        // Choose the best data flavor corresponding to the given FX mime type
        final Map<String, DataFlavor> mimeType2Flavor = new HashMap<>();
        for (String mimeType : mimeType2Flavors.keySet()) {
            final DataFlavor[] mimeTypeFlavors = mimeType2Flavors.get(mimeType).
                    toArray(new DataFlavor[0]);
            if (mimeTypeFlavors.length == 1) {
                mimeType2Flavor.put(mimeType, mimeTypeFlavors[0]);
            } else {
                // TBD: something better!!!
                mimeType2Flavor.put(mimeType, mimeTypeFlavors[0]);
            }
        }

        return mimeType2Flavor;
    }

    private static Object readData(final Transferable t, final DataFlavor flavor) {
        Object obj = null;
        try {
            obj = t.getTransferData(flavor);
        } catch (UnsupportedFlavorException ex) {
            // FIXME: report error
            ex.printStackTrace(System.err);
        } catch (IOException ex) {
            // FIXME: report error
            ex.printStackTrace(System.err);
        }
        return obj;
    }

    static Map<String, Object> readAllData(final Transferable t,
                                           final Map<String, DataFlavor> fxMimeType2DataFlavor)
    {
        final Map<String, Object> fxMimeType2Data = new HashMap<>();
        for (DataFlavor flavor : t.getTransferDataFlavors()) {
            Object obj = readData(t, flavor);
            if (obj != null) {
                String mimeType = getFxMimeType(flavor);
                obj = adjustSwingData(flavor, mimeType, obj);
                fxMimeType2Data.put(mimeType, obj);
            }
        }
        for (Map.Entry<String, DataFlavor> e: fxMimeType2DataFlavor.entrySet()) {
            String mimeType = e.getKey();
            DataFlavor flavor = e.getValue();
            Object obj = readData(t, flavor);
            if (obj != null) {
                obj = adjustSwingData(flavor, mimeType, obj);
                fxMimeType2Data.put(e.getKey(), obj);
            }
        }
        return fxMimeType2Data;
    }
}

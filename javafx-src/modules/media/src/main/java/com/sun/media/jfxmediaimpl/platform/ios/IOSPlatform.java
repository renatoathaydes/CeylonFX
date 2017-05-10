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

package com.sun.media.jfxmediaimpl.platform.ios;

import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.MediaPlayer;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.HostUtils;
import com.sun.media.jfxmediaimpl.platform.Platform;

/**
 * iOS Platform implementation.
 */
public final class IOSPlatform extends Platform {

    /**
     * The MIME types of all supported media.
     */
    private static final String[] CONTENT_TYPES = {
        "video/mp4",
        "audio/x-m4a",
        "video/x-m4v",
        "application/vnd.apple.mpegurl",
        "audio/mpegurl",
        "audio/mpeg",
        "audio/mp3",
        "audio/x-wav",
        "video/quicktime",
        "video/x-quicktime",
        "audio/x-aiff"
    };

    private static final class IOSPlatformInitializer {
        private static final IOSPlatform globalInstance = new IOSPlatform();
    }

    public static Platform getPlatformInstance() {
        return IOSPlatformInitializer.globalInstance;
    }

    private IOSPlatform() {
    }

    @Override
    public void preloadPlatform() {}

    /**
     * @return false if the platform cannot be loaded
     */
    @Override
    public boolean loadPlatform() {
        if (!HostUtils.isIOS()) {
            return false;
        }

        try {
            iosPlatformInit();
        } catch (UnsatisfiedLinkError ule) {
            if (Logger.canLog(Logger.DEBUG)) {
                Logger.logMsg(Logger.DEBUG, "Unable to load iOS platform.");
            }
            //MediaUtils.nativeError(IOSPlatform.class, MediaError.ERROR_MANAGER_ENGINEINIT_FAIL);
            return false;
        }
        return true;
    }

    @Override
    public String[] getSupportedContentTypes() {
        String[] contentTypesCopy = new String[CONTENT_TYPES.length];
        System.arraycopy(CONTENT_TYPES, 0, contentTypesCopy, 0, CONTENT_TYPES.length);
        return contentTypesCopy;
    }

    @Override
    public Media createMedia(Locator source) {
        return new IOSMedia(source);
    }

    @Override
    public Object prerollMediaPlayer(Locator source) {
        // attempt the actual player creation, then preroll here
        // on success we return a reference to the native player as the cookie
        return new IOSMediaPlayer(source);
    }

    @Override
    public MediaPlayer createMediaPlayer(Locator source, Object cookie) {
        if (cookie == null) {
            throw new NullPointerException("null player!");
        }
        else if (!(cookie instanceof IOSMediaPlayer)) {
            throw new IllegalArgumentException("cookie is not an instance of IOSMediaPlayer");
        }

        return (MediaPlayer) cookie;
    }

    private static native void iosPlatformInit();
}

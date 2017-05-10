/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
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
#ifndef ANDROIDLENS_H
#define	ANDROIDLENS_H

#include <android/native_window.h>

#ifdef	__cplusplus
extern "C" {
#endif

    jboolean lens_input_initialize(JNIEnv *env);

    void lens_input_shutdown();
    
    void notifyWindowEvent_resize(
        ANativeWindow *window,
        int eventType,
        int width,
        int height);
    
    void notifyTouchEvent(
        int  state,
        int  id,
        int  sendAlsoButtonEvent,
        int  xabs,
        int  yabs);

    void notifyMultiTouchEvent(
        int count,
        int *states,
        int *ids,
        int *xs,
        int *ys);
    
    void notifyMotionEvent(
        int mousePosX,
        int mousePosY,
        int isTouch,
        int touchId);
    
    void notifyButtonEvent(
        int pressed,
        int button,
        int xabs, int yabs);
    
    void notifyKeyEvent(
        int eventType,
        int platformKeycode,
        int isRepeatEvent);
    
#ifdef	__cplusplus
}
#endif

#endif	/* ANDROIDLENS_H */


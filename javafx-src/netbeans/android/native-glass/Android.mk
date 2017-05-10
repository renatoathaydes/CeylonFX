#
 # Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
 # DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 #
 # This code is free software; you can redistribute it and/or modify it
 # under the terms of the GNU General Public License version 2 only, as
 # published by the Free Software Foundation.  Oracle designates this
 # particular file as subject to the "Classpath" exception as provided
 # by Oracle in the LICENSE file that accompanied this code.
 #
 # This code is distributed in the hope that it will be useful, but WITHOUT
 # ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 # FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 # version 2 for more details (a copy is included in the LICENSE file that
 # accompanied this code).
 #
 # You should have received a copy of the GNU General Public License version
 # 2 along with this work; if not, write to the Free Software Foundation,
 # Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 #
 # Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 # or visit www.oracle.com if you need additional information or have any
 # questions.
 #
LOCAL_PATH := $(call my-dir)/../../../modules/graphics/src/main/native-glass/lens
include $(CLEAR_VARS)
LOCAL_MODULE := glass-lens-eglfb

LOCAL_SRC_FILES := LensApplication.c LensCursor.c LensCursorImages.c \
LensInputEvents.c LensLogger.c LensPixels.c LensRobot.c LensScreen.c LensView.c LensWindow.c
LOCAL_SRC_FILES += wm/LensWindowManager.c wm/robot.c
LOCAL_SRC_FILES += wm/screen/androidScreen.c
LOCAL_SRC_FILES += cursor/nullCursor/nullCursor.c
LOCAL_SRC_FILES += input/android/androidInput.c input/android/androidLens.c
LOCAL_C_INCLUDES := $(LOCAL_PATH) $(LOCAL_PATH)/input/android $(LOCAL_PATH)/../../../../build/generated-src/headers/glass/android
LOCAL_CFLAGS := -DANDROID_NDK -DDEBUG -std=c99
LOCAL_LDLIBS := -llog -landroid -ldl
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := glass-lens-android
LOCAL_SRC_FILES := android/android.c
LOCAL_C_INCLUDES := $(LOCAL_PATH)/android $(LOCAL_PATH)/../../../../build/generated-src/headers/glass/android
LOCAL_CFLAGS := -DANDROID_NDK -DDEBUG -std=c99
LOCAL_LDLIBS := -llog -landroid -ldl
include $(BUILD_SHARED_LIBRARY)	

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

#import "common.h"
#import "com_sun_glass_ui_Screen.h"

#import "GlassMacros.h"
#import "GlassScreen.h"
#import "GlassTimer.h"

//#define VERBOSE
#ifndef VERBOSE
    #define LOG(MSG, ...)
#else
    #define LOG(MSG, ...) GLASS_LOG(MSG, ## __VA_ARGS__);
#endif

#define MAX_DISPLAY_COUNT 1024

CGFloat GetScreenScaleFactor(NSScreen *screen)
{
    if ([screen respondsToSelector:@selector(backingScaleFactor)]) {
        return [screen backingScaleFactor];
    } else {
        return [screen userSpaceScaleFactor];
    }
}

jobject createJavaScreen(JNIEnv *env, NSScreen* screen)
{
    jobject jscreen = NULL;

    if (screen != nil)
    {
        jmethodID screenInit = (*env)->GetMethodID(env, jScreenClass,
                                                   "<init>",
                                                   "(JIIIIIIIIIIIF)V");
        GLASS_CHECK_EXCEPTION(env);

        NSValue *resolutionValue = [[screen deviceDescription] valueForKey:NSDeviceResolution];
        NSSize resolution = [resolutionValue sizeValue];

        jscreen = (jobject)(*env)->NewObject(env, jScreenClass, screenInit,
                                             ptr_to_jlong(screen),

                                             (jint)NSBitsPerPixelFromDepth([screen depth]),

                                             (jint)[screen frame].origin.x,
                                             (jint)[screen frame].origin.y,
                                             (jint)[screen frame].size.width,
                                             (jint)[screen frame].size.height,

                                             (jint)[screen visibleFrame].origin.x,
                                             (jint)([screen frame].size.height - [screen visibleFrame].size.height - [screen visibleFrame].origin.y),
                                             (jint)[screen visibleFrame].size.width,
                                             (jint)[screen visibleFrame].size.height,


                                             (jint)resolution.width,
                                             (jint)resolution.height,
                                             (jfloat)GetScreenScaleFactor(screen));
        
        GLASS_CHECK_EXCEPTION(env);
    }
    
    return jscreen;
}

jobjectArray createJavaScreens(JNIEnv* env) {
    //Update the Java notion of screens[]
    NSArray* screens = [NSScreen screens];

    if (jScreenClass == NULL)
    {
        jScreenClass = (*env)->NewGlobalRef(env, (*env)->FindClass(env, "com/sun/glass/ui/Screen"));
    }

    jobjectArray screenArray = (*env)->NewObjectArray(env,
                                                      [screens count],
                                                      jScreenClass,
                                                      NULL);

    for (NSUInteger index = 0; index < [screens count]; index++) {
        jobject javaScreen = createJavaScreen(env, [screens objectAtIndex:index]);
        (*env)->SetObjectArrayElement(env, screenArray, index, javaScreen);
    }

    return screenArray;
}

void GlassScreenDidChangeScreenParameters(JNIEnv *env)
{
    if (jScreenNotifySettingsChanged == NULL) 
    {
        jScreenNotifySettingsChanged = (*env)->GetStaticMethodID(env, jScreenClass, "notifySettingsChanged", "()V");
    }
    
    (*env)->CallStaticVoidMethod(env, jScreenClass, jScreenNotifySettingsChanged);
}

@implementation NSScreen (FullscreenAdditions)

- (CGDirectDisplayID)enterFullscreenAndHideCursor:(BOOL)hide
{
    CGDirectDisplayID displayID = 0;
    
    CGDisplayCount displayCount = 0;
    CGDirectDisplayID activeDisplays[MAX_DISPLAY_COUNT];
    CGDisplayErr err = CGGetActiveDisplayList(MAX_DISPLAY_COUNT, activeDisplays, &displayCount);
    if (err != kCGErrorSuccess)
    {
        NSLog(@"CGGetActiveDisplayList returned error: %d", err);
    }
    else
    {
        NSRect nsrect = [self frame];
        
        for (CGDisplayCount i=0; i<displayCount; i++)
        {
            CGRect cgrect = CGDisplayBounds(activeDisplays[i]);
            if ((nsrect.origin.x == cgrect.origin.x) && (nsrect.origin.y == cgrect.origin.y)
                && (nsrect.size.width == cgrect.size.width) && (nsrect.size.height == cgrect.size.height))
            {
                displayID = activeDisplays[i];
                break;
            }
        }
        
#if 0
        err = CGDisplayCapture(displayID);
#endif
        if (displayID == kCGDirectMainDisplay)
        {
            [NSMenu setMenuBarVisible:NO];
        }
        
        if (err != kCGErrorSuccess)
        {
            NSLog(@"CGDisplayCapture returned error: %d", err);
            displayID = 0;
        }
        else
        {
            if (hide == YES)
            {
                CGDisplayHideCursor(displayID);
            }
        }
    }
    
    return displayID;
}

- (void)exitFullscreen:(CGDirectDisplayID)displayID
{
    if (displayID != 0)
    {
        if (displayID == kCGDirectMainDisplay)
        {
            [NSMenu setMenuBarVisible:YES];
        }
#if 0
        CGDisplayRelease(displayID);
#endif
        CGDisplayShowCursor(displayID);
    }
}

@end


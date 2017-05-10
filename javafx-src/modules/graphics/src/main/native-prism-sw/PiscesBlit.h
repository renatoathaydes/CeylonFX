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

#ifndef PISCES_BLIT_H
#define PISCES_BLIT_H

#include <PiscesDefs.h>
#include <PiscesRenderer.h>

#define MIN_ALPHA 0
#define MAX_ALPHA 255

void initGammaArrays(jfloat gamma);

void genLinearGradientPaint(Renderer *rdr, jint height);
void genRadialGradientPaint(Renderer *rdr, jint height);
void genTexturePaint(Renderer *rdr, jint height);
void genTexturePaint565NoAlpha(Renderer *rdr, jint height);
void genTexturePaint565WithAlpha(Renderer *rdr, jint height);

void blitSrc8888_pre(Renderer *rdr, jint height);
void blitSrc8888(Renderer *rdr, jint height);

void blitSrcMask8888_pre(Renderer *rdr, jint height);

void blitPTSrc8888(Renderer *rdr, jint height);
void blitPTSrc8888_pre(Renderer *rdr, jint height);

void blitPTSrcMask8888_pre(Renderer *rdr, jint height);

void blitSrcOver8888(Renderer *rdr, jint height);
void blitSrcOver8888_pre(Renderer *rdr, jint height);

void blitSrcOverMask8888_pre(Renderer *rdr, jint height);

void blitSrcOverLCDMask8888_pre(Renderer *rdr, jint height);

void blitPTSrcOver8888(Renderer *rdr, jint height);
void blitPTSrcOver8888_pre(Renderer *rdr, jint height);

void blitPTSrcOverMask8888_pre(Renderer *rdr, jint height);

void clearRect8888(Renderer *rdr, jint x, jint y, jint w, jint h);

void emitLineSource8888(Renderer *rdr, jint height, jint frac);
void emitLinePTSource8888(Renderer *rdr, jint height, jint frac);
void emitLineSourceOver8888(Renderer *rdr, jint height, jint frac);
void emitLinePTSourceOver8888(Renderer *rdr, jint height, jint frac);

void emitLineSource8888_pre(Renderer *rdr, jint height, jint frac);
void emitLinePTSource8888_pre(Renderer *rdr, jint height, jint frac);
void emitLineSourceOver8888_pre(Renderer *rdr, jint height, jint frac);
void emitLinePTSourceOver8888_pre(Renderer *rdr, jint height, jint frac);

#endif

/*
 * Copyright (c) 2011, 2013, Oracle and/or its affiliates. All rights reserved.
 */
package com.sun.webkit.graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Logger;

public final class GraphicsDecoder  {
    public final static int FILLRECT_FFFFI         = 0;
    public final static int SETFILLCOLOR           = 1;
    public final static int SETSTROKESTYLE         = 2;
    public final static int SETSTROKECOLOR         = 3;
    public final static int SETSTROKEWIDTH         = 4;
    public final static int DRAWPOLYGON            = 6;
    public final static int DRAWLINE               = 7;
    public final static int DRAWIMAGE              = 8;
    public final static int DRAWICON               = 9;
    public final static int DRAWPATTERN            = 10;
    public final static int TRANSLATE              = 11;
    public final static int SAVESTATE              = 12;
    public final static int RESTORESTATE           = 13;
    public final static int CLIP_PATH              = 14;
    public final static int SETCLIP_IIII           = 15;
    public final static int DRAWRECT               = 16;
    public final static int SETCOMPOSITE           = 17;
    public final static int STROKEARC              = 18;
    public final static int DRAWELLIPSE            = 19;
    public final static int DRAWFOCUSRING          = 20;
    public final static int SETALPHA               = 21;
    public final static int BEGINTRANSPARENCYLAYER = 22;
    public final static int ENDTRANSPARENCYLAYER   = 23;
    public final static int STROKE_PATH            = 24;
    public final static int FILL_PATH              = 25;
    public final static int GETIMAGE               = 26;
    public final static int SCALE                  = 27;
    public final static int SETSHADOW              = 28;
    public final static int DRAWSTRING             = 29;
    public final static int DRAWSTRING_FAST        = 31;
    public final static int DRAWWIDGET             = 33;
    public final static int DRAWSCROLLBAR          = 34;
    public final static int CLEARRECT_FFFF         = 36;
    public final static int STROKERECT_FFFFF       = 37;
    public final static int RENDERMEDIAPLAYER      = 38;
    public final static int CONCATTRANSFORM_FFFFFF = 39;
    public final static int COPYREGION             = 40;
    public final static int DECODERQ               = 41;
    public final static int SET_TRANSFORM          = 42;
    public final static int ROTATE                 = 43;
    public final static int RENDERMEDIACONTROL     = 44;
    public final static int RENDERMEDIA_TIMETRACK  = 45;
    public final static int RENDERMEDIA_VOLUMETRACK = 46;
    public final static int FILLRECT_FFFF          = 47;
    public final static int FILL_ROUNDED_RECT      = 48;
    public final static int SET_FILL_GRADIENT      = 49;
    public final static int SET_STROKE_GRADIENT    = 50;
    public final static int SET_LINE_DASH          = 51;
    public final static int SET_LINE_CAP           = 52;
    public final static int SET_LINE_JOIN          = 53;
    public final static int SET_MITER_LIMIT        = 54;
    public final static int SET_TEXT_MODE          = 55;

    private final static Logger log =
        Logger.getLogger(GraphicsDecoder.class.getName());

    static void decode(WCGraphicsManager gm, WCGraphicsContext gc, BufferData bdata) {
        if (gc == null) {
            return;
        }
        ByteBuffer buf = bdata.getBuffer();
        buf.order(ByteOrder.nativeOrder());
        while (buf.remaining() > 0) {
            int op = buf.getInt();
            switch(op) {
                case FILLRECT_FFFF:
                    gc.fillRect(
                        buf.getFloat(),
                        buf.getFloat(),
                        buf.getFloat(),
                        buf.getFloat(),
                        null);
                    break;
                case FILLRECT_FFFFI:
                    gc.fillRect(
                        buf.getFloat(),
                        buf.getFloat(),
                        buf.getFloat(),
                        buf.getFloat(),
                        buf.getInt());
                    break;
                case FILL_ROUNDED_RECT:
                    gc.fillRoundedRect(
                        // base rectangle
                        buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(),
                        // top corners w/h
                        buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(),
                        // bottom corners w/h
                        buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(),
                        buf.getInt());
                    break;
                case CLEARRECT_FFFF:
                    gc.clearRect(
                        buf.getFloat(),
                        buf.getFloat(),
                        buf.getFloat(),
                        buf.getFloat());
                    break;
                case STROKERECT_FFFFF:
                    gc.strokeRect(
                        buf.getFloat(),
                        buf.getFloat(),
                        buf.getFloat(),
                        buf.getFloat(),
                        buf.getFloat());
                    break;
                case SETFILLCOLOR:
                    gc.setFillColor(buf.getInt());
                    break;
                case SET_TEXT_MODE:
                    gc.setTextMode(getBoolean(buf), getBoolean(buf), getBoolean(buf));
                    break;
                case SETSTROKESTYLE:
                    gc.setStrokeStyle(buf.getInt());
                    break;
                case SETSTROKECOLOR:
                    gc.setStrokeColor(buf.getInt());
                    break;
                case SETSTROKEWIDTH:
                    gc.setStrokeWidth(buf.getFloat());
                    break;
                case SET_FILL_GRADIENT:
                    gc.setFillGradient(getGradient(gc, buf));
                    break;
                case SET_STROKE_GRADIENT:
                    gc.setStrokeGradient(getGradient(gc, buf));
                    break;
                case SET_LINE_DASH:
                    gc.setLineDash(buf.getFloat(), getFloatArray(buf));
                    break;
                case SET_LINE_CAP:
                    gc.setLineCap(buf.getInt());
                    break;
                case SET_LINE_JOIN:
                    gc.setLineJoin(buf.getInt());
                    break;
                case SET_MITER_LIMIT:
                    gc.setMiterLimit(buf.getFloat());
                    break;
                case DRAWPOLYGON:
                    boolean shouldAA = buf.getInt() == -1;
                    int n = buf.getInt();
                    float pnts[] = new float[n];
                    buf.asFloatBuffer().get(pnts);
                    buf.position(buf.position() + n*4);
                    gc.drawPolygon(pnts, shouldAA);
                    break;
                case DRAWLINE:
                    gc.drawLine(
                        buf.getInt(),
                        buf.getInt(),
                        buf.getInt(),
                        buf.getInt());
                    break;
                case DRAWIMAGE:
                    drawImage(gc,
                        gm.getRef(buf.getInt()),
                        //dest React
                        buf.getFloat(),
                        buf.getFloat(),
                        buf.getFloat(),
                        buf.getFloat(),
                        //src Rect
                        buf.getFloat(),
                        buf.getFloat(),
                        buf.getFloat(),
                        buf.getFloat());
                    break;
                case DRAWICON:
                    gc.drawIcon((WCIcon)gm.getRef(buf.getInt()),
                        buf.getInt(),
                        buf.getInt());
                    break;
                case DRAWPATTERN:
                    drawPattern(gc,
                        gm.getRef(buf.getInt()),
                        getRectangle(buf),
                        (WCTransform)gm.getRef(buf.getInt()),
                        getPoint(buf),
                        getRectangle(buf));
                    break;
                case TRANSLATE:
                    gc.translate(buf.getFloat(), buf.getFloat());
                    break;
                case SCALE:
                    gc.scale(buf.getFloat(), buf.getFloat());
                    break;
                case SAVESTATE:
                    gc.saveState();
                    break;
                case RESTORESTATE:
                    gc.restoreState();
                    break;
                case CLIP_PATH:
                    gc.setClip(
                        getPath(gm, buf),
                        buf.getInt()>0);
                    break;
                case SETCLIP_IIII:
                    gc.setClip(
                        buf.getInt(),
                        buf.getInt(),
                        buf.getInt(),
                        buf.getInt());
                    break;
                case DRAWRECT:
                    gc.drawRect(
                        buf.getInt(),
                        buf.getInt(),
                        buf.getInt(),
                        buf.getInt());
                    break;
                case SETCOMPOSITE:
                    gc.setComposite(buf.getInt());
                    break;
                case STROKEARC:
                    gc.strokeArc(
                        buf.getInt(),
                        buf.getInt(),
                        buf.getInt(),
                        buf.getInt(),
                        buf.getInt(),
                        buf.getInt());
                    break;
                case DRAWELLIPSE:
                    gc.drawEllipse(
                        buf.getInt(),
                        buf.getInt(),
                        buf.getInt(),
                        buf.getInt());
                    break;
                case DRAWFOCUSRING:
                    gc.drawFocusRing(
                        buf.getInt(),
                        buf.getInt(),
                        buf.getInt(),
                        buf.getInt(),
                        buf.getInt());
                    break;
                case SETALPHA:
                    gc.setAlpha(buf.getFloat());
                    break;
                case BEGINTRANSPARENCYLAYER:
                    gc.beginTransparencyLayer(buf.getFloat());
                    break;
                case ENDTRANSPARENCYLAYER:
                    gc.endTransparencyLayer();
                    break;
                case STROKE_PATH:
                    gc.strokePath(getPath(gm, buf));
                    break;
                case FILL_PATH:
                    gc.fillPath(getPath(gm, buf));
                    break;
                case SETSHADOW:
                    gc.setShadow(
                        buf.getFloat(),
                        buf.getFloat(),
                        buf.getFloat(),
                        buf.getInt());
                    break;
                case DRAWSTRING:
                    gc.drawString(
                        (WCFont) gm.getRef(buf.getInt()),
                        bdata.getString(buf.getInt()),
                        (buf.getInt() == -1),           // rtl flag
                        buf.getInt(), buf.getInt(),     // from and to positions
                        buf.getFloat(), buf.getFloat());// (x,y) position
                    break;
                case DRAWSTRING_FAST:
                    gc.drawString(
                        (WCFont) gm.getRef(buf.getInt()),
                        bdata.getIntArray(buf.getInt()), //glyphs
                        bdata.getFloatArray(buf.getInt()), //offsets
                        buf.getFloat(),
                        buf.getFloat());
                    break;
                case DRAWWIDGET:
                    gc.drawWidget((RenderTheme)(gm.getRef(buf.getInt())),
                        gm.getRef(buf.getInt()), buf.getInt(), buf.getInt());
                    break;
                case DRAWSCROLLBAR:
                    gc.drawScrollbar((ScrollBarTheme)(gm.getRef(buf.getInt())),
                        gm.getRef(buf.getInt()), buf.getInt(), buf.getInt(),
                        buf.getInt(), buf.getInt());
                    break;
                case RENDERMEDIAPLAYER:
                    WCMediaPlayer mp = (WCMediaPlayer)gm.getRef(buf.getInt());
                    mp.render(gc,
                            buf.getInt(),   // x
                            buf.getInt(),   // y
                            buf.getInt(),   // width
                            buf.getInt());  // height
                    break;
                case CONCATTRANSFORM_FFFFFF:
                    gc.concatTransform(new WCTransform(
                            buf.getFloat(), buf.getFloat(), buf.getFloat(),
                            buf.getFloat(), buf.getFloat(), buf.getFloat()));
                    break;
                case SET_TRANSFORM:
                    gc.setTransform(new WCTransform(
                            buf.getFloat(), buf.getFloat(), buf.getFloat(),
                            buf.getFloat(), buf.getFloat(), buf.getFloat()));
                    break;
                case COPYREGION:
                    WCPageBackBuffer buffer = (WCPageBackBuffer)gm.getRef(buf.getInt());
                    buffer.copyArea(buf.getInt(), buf.getInt(), buf.getInt(), buf.getInt(),
                                    buf.getInt(), buf.getInt());
                    break;
                case DECODERQ:
                    WCRenderQueue _rq = (WCRenderQueue)gm.getRef(buf.getInt());
                    _rq.decode(gc.getFontSmoothingType());
                    break;
                case ROTATE:
                    gc.rotate(buf.getFloat());
                    break;
                case RENDERMEDIACONTROL:
                    RenderMediaControls.paintControl(gc,
                            buf.getInt(),   // control type
                            buf.getInt(),   // x
                            buf.getInt(),   // y
                            buf.getInt(),   // width
                            buf.getInt());  // height
                    break;
                case RENDERMEDIA_TIMETRACK:
                    n = buf.getInt();   // number of timeRange pairs
                    float[] buffered = new float[n*2];
                    buf.asFloatBuffer().get(buffered);
                    buf.position(buf.position() + n*4 *2);
                    RenderMediaControls.paintTimeSliderTrack(gc,
                            buf.getFloat(), // duration
                            buf.getFloat(), // currentTime
                            buffered,       // buffered() timeRanges
                            buf.getInt(),   // x
                            buf.getInt(),   // y
                            buf.getInt(),   // width
                            buf.getInt());  // height
                     break;
                case RENDERMEDIA_VOLUMETRACK:
                    RenderMediaControls.paintVolumeTrack(gc,
                            buf.getFloat(), // curVolume
                            buf.getInt() != 0,  // muted
                            buf.getInt(),   // x
                            buf.getInt(),   // y
                            buf.getInt(),   // width
                            buf.getInt());  // height
                    break;
                default:
                    log.fine("ERROR. Unknown primitive found");
                    break;
            }
        }
    }


    private static void drawPattern(
            WCGraphicsContext gc,
            Object imgFrame,
            WCRectangle srcRect,
            WCTransform patternTransform,
            WCPoint phase,
            WCRectangle destRect)
    {
        WCImage img = WCImage.getImage(imgFrame);
        if (img != null) {
            // RT-10059: drawImage() may have to create the texture
            // lazily, and may fail with an OutOfMemory error
            // if the texture is too large. This is a legitimate
            // situation that should be handled gracefully. It should
            // not cause us to quit painting other page components.
            try {
                gc.drawPattern(
                    img,
                    srcRect,
                    patternTransform,
                    phase,
                    destRect);
            } catch (OutOfMemoryError error) {
                error.printStackTrace();
            }
        }
    }

    private static void drawImage(
            WCGraphicsContext gc,
            Object imgFrame,
            float dstx, float dsty, float dstw, float dsth,
            float srcx, float srcy, float srcw, float srch)
    {
        WCImage img = WCImage.getImage(imgFrame);
        if (img != null) {
            // RT-10059: drawImage() may have to create the texture
            // lazily, and may fail with an OutOfMemory error
            // if the texture is too large. This is a legitimate
            // situation that should be handled gracefully. It should
            // not cause us to quit painting other page components.
            try {
                gc.drawImage(
                    img,
                    dstx, dsty, dstw, dsth,
                    srcx, srcy, srcw, srch);
            } catch (OutOfMemoryError error) {
                error.printStackTrace();
            }
        }
    }

    private static boolean getBoolean(ByteBuffer buf) {
        return 0 != buf.getInt();
    }

    private static float[] getFloatArray(ByteBuffer buf) {
        float[] array = new float[buf.getInt()];
        for (int i = 0; i < array.length; i++) {
            array[i] = buf.getFloat();
        }
        return array;
    }

    private static WCPath getPath(WCGraphicsManager gm, ByteBuffer buf) {
        WCPath path = (WCPath) gm.getRef(buf.getInt());
        path.setWindingRule(buf.getInt());
        return path;
    }

    private static WCPoint getPoint(ByteBuffer buf) {
        return new WCPoint(buf.getFloat(),
                           buf.getFloat());
    }

    private static WCRectangle getRectangle(ByteBuffer buf) {
        return new WCRectangle(buf.getFloat(),
                               buf.getFloat(),
                               buf.getFloat(),
                               buf.getFloat());
    }

    private static WCGradient getGradient(WCGraphicsContext gc, ByteBuffer buf) {
        WCPoint p1 = getPoint(buf);
        WCPoint p2 = getPoint(buf);
        WCGradient gradient = getBoolean(buf)
                ? gc.createRadialGradient(p1, buf.getFloat(), p2, buf.getFloat())
                : gc.createLinearGradient(p1, p2);

        boolean proportional = getBoolean(buf);
        int spreadMethod = buf.getInt();
        if (gradient != null) {
            gradient.setProportional(proportional);
            gradient.setSpreadMethod(spreadMethod);
        }
        int count = buf.getInt();
        for (int i = 0; i < count; i++) {
            int color = buf.getInt();
            float offset = buf.getFloat();
            if (gradient != null) {
                gradient.addStop(color, offset);
            }
        }
        return gradient;
    }
}

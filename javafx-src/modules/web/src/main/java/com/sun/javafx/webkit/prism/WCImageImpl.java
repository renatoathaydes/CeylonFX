/*
 * Copyright (c) 2011, 2013, Oracle and/or its affiliates. All rights reserved.
 */
package com.sun.javafx.webkit.prism;

import com.sun.javafx.iio.ImageFrame;
import com.sun.prism.Graphics;
import com.sun.prism.Image;
import com.sun.prism.PrinterGraphics;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.image.CompoundCoords;
import com.sun.prism.image.CompoundTexture;
import com.sun.prism.image.Coords;
import com.sun.prism.image.ViewPort;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.PixelFormat;

/**
 * @author Alexey.Ushakov
 */
final class WCImageImpl extends PrismImage {
    private final static Logger log =
        Logger.getLogger(WCImageImpl.class.getName());

    private final Image img;
    private Texture texture;
    private CompoundTexture compoundTexture;


    WCImageImpl(int w, int h) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "Creating empty image({0},{1})",
                    new Object[] {w, h});
        }
        img = Image.fromIntArgbPreData(new int[w*h], w, h);
    }

    WCImageImpl(int[] buffer, int w, int h) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "Creating image({0},{1}) from buffer",
                    new Object[] {w, h});
        }
        img = Image.fromIntArgbPreData(buffer, w, h);
    }

    WCImageImpl(ImageFrame frame) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "Creating image {0}x{1} of type {2} from buffer",
                    new Object[]{frame.getWidth(), frame.getHeight(), frame.getImageType()});
        }
        img = Image.convertImageFrame(frame);
    }

    Image getImage() {
        return img;
    }

    @Override
    Graphics getGraphics() {
        return null;
    }

    @Override
    void draw(Graphics g,
            int dstx1, int dsty1, int dstx2, int dsty2,
            int srcx1, int srcy1, int srcx2, int srcy2)
    {
        if (g instanceof PrinterGraphics) {
            // We're printing. Wrap [img] into a J2DTexture and draw it.
            Texture t = g.getResourceFactory().createTexture(
                    img, Texture.Usage.STATIC, Texture.WrapMode.CLAMP_NOT_NEEDED);
            g.drawTexture(t,
                    dstx1, dsty1, dstx2, dsty2,
                    srcx1, srcy1, srcx2, srcy2);
            t.dispose();
            return;
        }
        
        if (texture != null) {
            texture.lock();
            if (texture.isSurfaceLost()) {
                texture = null;
            }
        }
        if (texture == null && compoundTexture == null) {
            ResourceFactory resourceFactory = g.getResourceFactory();
            int maxSize = resourceFactory.getMaximumTextureSize();
            if (img.getWidth() <= maxSize && img.getHeight() <= maxSize) {
                texture = resourceFactory.createTexture(img, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_TO_EDGE);
                assert texture != null;
            } else {
                compoundTexture = new CompoundTexture(img, maxSize);
            }
        }

        if (texture != null) {
            assert compoundTexture == null;
            g.drawTexture(
                    texture,
                    dstx1, dsty1, dstx2, dsty2,
                    srcx1, srcy1, srcx2, srcy2);
            texture.unlock();
        } else {
            assert compoundTexture != null;
            ViewPort viewPort = new ViewPort(
                    srcx1,
                    srcy1,
                    srcx2 - srcx1,
                    srcy2 - srcy1);
            Coords coords = new Coords(dstx2 - dstx1, dsty2 - dsty1, viewPort);
            CompoundCoords compoundCoords = new CompoundCoords(
                    compoundTexture,
                    coords);
            compoundCoords.draw(g, compoundTexture, dstx1, dsty1);
        }
    }

    @Override
    void dispose() {
        if (texture != null) {
            texture.dispose();
            texture = null;
        }
        if (compoundTexture != null) {
            compoundTexture.dispose();
            compoundTexture = null;
        }
    }

    @Override
    public int getWidth() {
        return img.getWidth();
    }

    @Override
    public int getHeight() {
        return img.getHeight();
    }

    @Override
    public ByteBuffer getPixelBuffer() {
        int w = img.getWidth();
        int h = img.getHeight();
        int s = w*4;
        ByteBuffer pixels = ByteBuffer.allocate(s * h);
        img.getPixels(0, 0, w, h,
            PixelFormat.getByteBgraInstance(),
            pixels, s);
        return pixels;
    }
}

/*
 * Copyright (c) 2011, 2013, Oracle and/or its affiliates. All rights reserved.
 */
package com.sun.javafx.sg.prism;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Graphics;
import com.sun.prism.PrinterGraphics;
import com.sun.webkit.WebPage;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCGraphicsManager;
import com.sun.webkit.graphics.WCRectangle;

/**
 * A scene graph node that renders a web resource
 *
 * @author Alexey Ushakov
 */
public final class NGWebView extends NGGroup {

    private final static Logger log =
        Logger.getLogger(NGWebView.class.getName());
    private volatile WebPage page;
    private volatile float width, height;

    public void setPage(WebPage page) {
        this.page = page;
    }

    public void resize(float w, float h) {
        if (width != w || height != h) {
            width = w;
            height = h;
            geometryChanged();
            if (page != null) {
                page.setBounds(0, 0, (int)w, (int)h);
            }
        }
    }

    // Invoked on JavaFX User Thread.
    public void update() {
        if (page != null) {
            BaseBounds clip = getClippedBounds(new RectBounds(), BaseTransform.IDENTITY_TRANSFORM);
            if (!clip.isEmpty()) {
                log.log(Level.FINEST, "updating rectangle: {0}", clip);
                page.updateContent(new WCRectangle(clip.getMinX(), clip.getMinY(),
                                                   clip.getWidth(), clip.getHeight()));
            }
        }
    }

    public void requestRender() {
        visualsChanged();
    }

    // Invoked on Render Thread.
    @Override protected void renderContent(Graphics g) {
        log.log(Level.FINEST, "rendering into {0}", g);
        if (g == null || page == null || width <= 0 || height <= 0)
            return;

        WCGraphicsContext gc =
                WCGraphicsManager.getGraphicsManager().createGraphicsContext(g);
        try {
            if (g instanceof PrinterGraphics) {
                page.print(gc, 0, 0, (int) width, (int) height);
            } else {
                page.paint(gc, 0, 0, (int) width, (int) height);
            }
        } finally {
            gc.dispose();
        }

    }

    @Override public boolean hasOverlappingContents() {
        return false;
    }
    
    @Override protected boolean hasVisuals() {
        return true;
    }
}

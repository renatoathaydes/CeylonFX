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

package javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Ellipse2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGEllipse;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGShape;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.scene.paint.Paint;


/**
 * The {@code Ellipse} class creates a new ellipse
 * with the specified size and location in pixels
 *
<PRE>
import javafx.scene.shape.*;

Ellipse ellipse = new Ellipse(); {
ellipse.setCenterX(50.0f);
ellipse.setCenterY(50.0f);
ellipse.setRadiusX(50.0f);
ellipse.setRadiusY(25.0f);
</PRE>
 * @since JavaFX 2.0
 */
public class Ellipse extends Shape {

    private final Ellipse2D shape = new Ellipse2D();

    private static final int NON_RECTILINEAR_TYPE_MASK = ~(
            BaseTransform.TYPE_TRANSLATION |
            BaseTransform.TYPE_QUADRANT_ROTATION |
            BaseTransform.TYPE_MASK_SCALE |
            BaseTransform.TYPE_FLIP);


    /**
     * Creates an empty instance of Ellipse.
     */
    public Ellipse() {
    }

    /**
     * Creates an instance of Ellipse of the given size.
     * @param radiusX the horizontal radius of the ellipse in pixels
     * @param radiusY the vertical radius of the ellipse in pixels
     */
    public Ellipse(double radiusX, double radiusY) {
        setRadiusX(radiusX);
        setRadiusY(radiusY);
    }

    /**
     * Creates an instance of Ellipse of the given position and size.
     * @param centerX the horizontal position of the center of the ellipse in pixels
     * @param centerY the vertical position of the center of the ellipse in pixels
     * @param radiusX the horizontal radius of the ellipse in pixels
     * @param radiusY the vertical radius of the ellipse in pixels
     */
    public Ellipse(double centerX, double centerY, double radiusX, double radiusY) {
        this(radiusX, radiusY);
        setCenterX(centerX);
        setCenterY(centerY);
    }

    /**
     * Defines the horizontal position of the center of the ellipse in pixels.
     *
     * @defaultValue 0.0
     */
    private DoubleProperty centerX;

    public final void setCenterX(double value) {
        if (centerX != null || value != 0.0) {
            centerXProperty().set(value);
        }
    }

    public final double getCenterX() {
        return centerX == null ? 0.0 : centerX.get();
    }

    public final DoubleProperty centerXProperty() {
        if (centerX == null) {
            centerX = new DoublePropertyBase() {

                @Override
                public void invalidated() {
                    impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    impl_geomChanged();
                }

                @Override
                public Object getBean() {
                    return Ellipse.this;
                }

                @Override
                public String getName() {
                    return "centerX";
                }
            };
        }
        return centerX;
    }

    /**
     * Defines the vertical position of the center of the ellipse in pixels.
     *
     * @defaultValue 0.0
     */
    private DoubleProperty centerY;

    public final void setCenterY(double value) {
        if (centerY != null || value != 0.0) {
            centerYProperty().set(value);
        }
    }

    public final double getCenterY() {
        return centerY == null ? 0.0 : centerY.get();
    }

    public final DoubleProperty centerYProperty() {
        if (centerY == null) {
            centerY = new DoublePropertyBase() {

                @Override
                public void invalidated() {
                    impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    impl_geomChanged();
                }

                @Override
                public Object getBean() {
                    return Ellipse.this;
                }

                @Override
                public String getName() {
                    return "centerY";
                }
            };
        }
        return centerY;
    }

    /**
     * Defines the width of the ellipse in pixels.
     *
     * @defaultValue 0.0
     */
    private final DoubleProperty radiusX = new DoublePropertyBase() {

        @Override
        public void invalidated() {
            impl_markDirty(DirtyBits.NODE_GEOMETRY);
            impl_geomChanged();
        }

        @Override
        public Object getBean() {
            return Ellipse.this;
        }

        @Override
        public String getName() {
            return "radiusX";
        }
    };

    public final void setRadiusX(double value) {
        radiusX.set(value);
    }

    public final double getRadiusX() {
        return radiusX.get();
    }

    public final DoubleProperty radiusXProperty() {
        return radiusX;
    }

    /**
     * Defines the height of the ellipse in pixels.
     *
     * @defaultValue 0.0
     */
    private final DoubleProperty radiusY = new DoublePropertyBase() {

        @Override
        public void invalidated() {
            impl_markDirty(DirtyBits.NODE_GEOMETRY);
            impl_geomChanged();
        }

        @Override
        public Object getBean() {
            return Ellipse.this;
        }

        @Override
        public String getName() {
            return "radiusY";
        }
    };

    public final void setRadiusY(double value) {
        radiusY.set(value);
    }

    public final double getRadiusY() {
        return radiusY.get();
    }

    public final DoubleProperty radiusYProperty() {
        return radiusY;
    }

    /**
     * @treatAsPrivate implementation detail
     * @deprecated This is an internal API that is not intended for use and will be removed in the next version
     */
    @Deprecated
    @Override
    protected NGNode impl_createPeer() {
        return new NGEllipse();
    }

    /**
     */
    @Override StrokeLineJoin convertLineJoin(StrokeLineJoin t) {
        // The MITER join style can produce anomalous results for very thin or
        // very wide ellipses when the bezier curves that approximate the arcs
        // become so distorted that they shoot out MITER-like extensions.  This
        // effect complicates matters because it makes the ellipses very non-round,
        // and also because it means we might have to pad the bounds to account
        // for this rare and unpredictable circumstance.
        // To avoid the problem, we set the Join style to BEVEL for any
        // ellipse.  The BEVEL join style is more predictable for
        // anomalous angles and is the simplest join style to compute in
        // the stroking code.
        return StrokeLineJoin.BEVEL;
    }

    /**
     * @treatAsPrivate implementation detail
     * @deprecated This is an internal API that is not intended for use and will be removed in the next version
     */
    @Deprecated
    @Override
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        // if there is no fill or stroke, then there are no bounds. The bounds
        // must be marked empty in this case to distinguish it from 0,0,0,0
        // which would actually contribute to the bounds of a group.
        if (impl_mode == NGShape.Mode.EMPTY) {
            return bounds.makeEmpty();
        }
        if ((tx.getType() & NON_RECTILINEAR_TYPE_MASK) != 0) {
            return computeShapeBounds(bounds, tx, impl_configShape());
        }

        // compute the x, y, width and height of the ellipse
        final double x = getCenterX() - getRadiusX();
        final double y = getCenterY() - getRadiusY();
        final double width = 2.0f * getRadiusX();
        final double height = 2.0f * getRadiusY();
        double upad;
        double dpad;
        if (impl_mode == NGShape.Mode.FILL || getStrokeType() == StrokeType.INSIDE) {
            upad = dpad = 0.0f;
        } else {
            upad = getStrokeWidth();
            if (getStrokeType() == StrokeType.CENTERED) {
                upad /= 2.0f;
            }
            dpad = 0.0f;
        }
        return computeBounds(bounds, tx, upad, dpad, x, y, width, height);
    }

    /**
     * @treatAsPrivate implementation detail
     * @deprecated This is an internal API that is not intended for use and will be removed in the next version
     */
    @Deprecated
    @Override
	public Ellipse2D impl_configShape() {
        shape.setFrame(
            (float)(getCenterX() - getRadiusX()), // x
            (float)(getCenterY() - getRadiusY()), // y
            (float)(getRadiusX() * 2.0), // w
            (float)(getRadiusY() * 2.0)); // h
        return shape;
    }

    /**
     * @treatAsPrivate implementation detail
     * @deprecated This is an internal API that is not intended for use and will be removed in the next version
     */
    @Deprecated
    @Override
    public void impl_updatePeer() {
        super.impl_updatePeer();

        if (impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
            NGEllipse peer = impl_getPeer();
            peer.updateEllipse((float)getCenterX(),
                (float)getCenterY(),
                (float)getRadiusX(),
                (float)getRadiusY());
        }
    }

    /**
     * Returns a string representation of this {@code Ellipse} object.
     * @return a string representation of this {@code Ellipse} object.
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Ellipse[");

        String id = getId();
        if (id != null) {
            sb.append("id=").append(id).append(", ");
        }

        sb.append("centerX=").append(getCenterX());
        sb.append(", centerY=").append(getCenterY());
        sb.append(", radiusX=").append(getRadiusX());
        sb.append(", radiusY=").append(getRadiusY());

        sb.append(", fill=").append(getFill());

        Paint stroke = getStroke();
        if (stroke != null) {
            sb.append(", stroke=").append(stroke);
            sb.append(", strokeWidth=").append(getStrokeWidth());
        }

        return sb.append("]").toString();
    }
}

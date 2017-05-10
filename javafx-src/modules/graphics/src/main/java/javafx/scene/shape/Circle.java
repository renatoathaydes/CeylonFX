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
import com.sun.javafx.sg.prism.NGCircle;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGShape;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.scene.paint.Paint;

/**
 * The {@code Circle} class creates a new circle
 * with the specified radius and center location measured in pixels
 *
 * Example usage. The following code creates a circle with radius 50px centered
 * at (100,100)px.
 *
<PRE>
import javafx.scene.shape.*;

Circle circle = new Circle();
circle.setCenterX(100.0f);
circle.setCenterY(100.0f);
circle.setRadius(50.0f);
}
</PRE>
 * @since JavaFX 2.0
 */
public class Circle extends Shape {

    private final Ellipse2D shape = new Ellipse2D();

    /**
     * Creates a new instance of Circle with a specified radius.
     * @param radius the radius of the circle in pixels
     */
    public Circle(double radius) {
        setRadius(radius);
    }

    /**
     * Creates a new instance of Circle with a specified radius and fill.
     * @param radius the radius of the circle
     * @param fill determines how to fill the interior of the Circle
     */
    public Circle(double radius, Paint fill) {
        setRadius(radius);
        setFill(fill);
    }

    /**
     * Creates an empty instance of Circle.
     */
    public Circle() {
    }

    /**
     * Creates a new instance of Circle with a specified position and radius.
     * @param centerX the horizontal position of the center of the circle in pixels
     * @param centerY the vertical position of the center of the circle in pixels
     * @param radius the radius of the circle in pixels
     */
    public Circle(double centerX, double centerY, double radius) {
        setCenterX(centerX);
        setCenterY(centerY);
        setRadius(radius);
    }

    /**
     * Creates a new instance of Circle with a specified position, radius and fill.
     * @param centerX the horizontal position of the center of the circle in pixels
     * @param centerY the vertical position of the center of the circle in pixels
     * @param radius the radius of the circle in pixels
     * @param fill determines how to fill the interior of the Circle
     */
    public Circle(double centerX, double centerY, double radius, Paint fill) {
        setCenterX(centerX);
        setCenterY(centerY);
        setRadius(radius);
        setFill(fill);
    }

    /**
     * Defines the horizontal position of the center of the circle in pixels.
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
            centerX = new DoublePropertyBase(0.0) {

                @Override
                public void invalidated() {
                    impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    impl_geomChanged();
                }

                @Override
                public Object getBean() {
                    return Circle.this;
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
     * Defines the vertical position of the center of the circle in pixels.
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
            centerY = new DoublePropertyBase(0.0) {

                @Override
                public void invalidated() {
                    impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    impl_geomChanged();
                }

                @Override
                public Object getBean() {
                    return Circle.this;
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
     * Defines the radius of the circle in pixels.
     *
     * @defaultValue 0.0
     */
    private final DoubleProperty radius = new DoublePropertyBase() {

        @Override
        public void invalidated() {
            impl_markDirty(DirtyBits.NODE_GEOMETRY);
            impl_geomChanged();
        }

        @Override
        public Object getBean() {
            return Circle.this;
        }

        @Override
        public String getName() {
            return "radius";
        }
    };

    public final void setRadius(double value) {
        radius.set(value);
    }

    public final double getRadius() {
        return radius.get();
    }

    public final DoubleProperty radiusProperty() {
        return radius;
    }

    /**
     */
    @Override StrokeLineJoin convertLineJoin(StrokeLineJoin t) {
        // The MITER join style can produce anomalous results for very thin or
        // very wide ellipses when the bezier curves that approximate the arcs
        // become so distorted that they shoot out MITER-like extensions.  This
        // effect complicates matters because it makes the circles very non-round,
        // and also because it means we might have to pad the bounds to account
        // for this rare and unpredictable circumstance.
        // To avoid the problem, we set the Join style to BEVEL for any
        // circle.  The BEVEL join style is more predictable for
        // anomalous angles and is the simplest join style to compute in
        // the stroking code.
        // These problems do not necessarily happen for circles which have a
        // fixed and balanced aspect ratio, but why waste time computing a
        // conversion of a MITER join style when it has no advantage for
        // circles and technically requires more computation?
        return StrokeLineJoin.BEVEL;
    }

    /**
     * @treatAsPrivate implementation detail
     * @deprecated This is an internal API that is not intended for use and will be removed in the next version
     */
    @Deprecated
    @Override protected NGNode impl_createPeer() {
        return new NGCircle();
    }

    /**
     * @treatAsPrivate implementation detail
     * @deprecated This is an internal API that is not intended for use and will be removed in the next version
     */
    @Deprecated
    @Override public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        // if there is no fill or stroke, then there are no bounds. The bounds
        // must be marked empty in this case to distinguish it from 0,0,0,0
        // which would actually contribute to the bounds of a group.
        if (impl_mode == NGShape.Mode.EMPTY) {
            return bounds.makeEmpty();
        }

        final double cX = getCenterX();
        final double cY = getCenterY();

        if ((tx.getType() & ~(BaseTransform.TYPE_MASK_ROTATION | BaseTransform.TYPE_TRANSLATION)) == 0) {

            double tCX = cX * tx.getMxx() + cY * tx.getMxy() + tx.getMxt();
            double tCY = cX * tx.getMyx() + cY * tx.getMyy() + tx.getMyt();
            double r = getRadius();

            if (impl_mode != NGShape.Mode.FILL && getStrokeType() != StrokeType.INSIDE) {
                double upad = getStrokeWidth();
                if (getStrokeType() == StrokeType.CENTERED) {
                    upad /= 2.0f;
                }
                r += upad;
            }

            return bounds.deriveWithNewBounds((float) (tCX - r), (float) (tCY - r), 0,
                    (float) (tCX + r), (float) (tCY + r), 0);
        } else if ((tx.getType() & ~(BaseTransform.TYPE_MASK_SCALE | BaseTransform.TYPE_TRANSLATION | BaseTransform.TYPE_FLIP)) == 0) {
            final double r = getRadius();
            final double x = getCenterX() - r;
            final double y = getCenterY() - r;
            final double width = 2.0 * r;
            final double height = width;
            double upad;
            if (impl_mode == NGShape.Mode.FILL || getStrokeType() == StrokeType.INSIDE) {
                upad = 0.0f;
            } else {
                upad = getStrokeWidth();
            }
            return computeBounds(bounds, tx, upad, 0, x, y, width, height);
        }

        return computeShapeBounds(bounds, tx, impl_configShape());
    }

    /**
     * @treatAsPrivate implementation detail
     * @deprecated This is an internal API that is not intended for use and will be removed in the next version
     */
    @Deprecated
    @Override public Ellipse2D impl_configShape() {
        double r = getRadius();
        shape.setFrame(
            (float)(getCenterX() - r), // x
            (float)(getCenterY() - r), // y
            (float)(r * 2.0), // w
            (float)(r * 2.0)); // h
        return shape;
    }

    /**
     * @treatAsPrivate implementation detail
     * @deprecated This is an internal API that is not intended for use and will be removed in the next version
     */
    @Deprecated
    @Override public void impl_updatePeer() {
        super.impl_updatePeer();

        if (impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
            final NGCircle peer = impl_getPeer();
            peer.updateCircle((float)getCenterX(),
                (float)getCenterY(),
                (float)getRadius());
        }
    }

    /**
     * Returns a string representation of this {@code Circle} object.
     * @return a string representation of this {@code Circle} object.
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Circle[");

        String id = getId();
        if (id != null) {
            sb.append("id=").append(id).append(", ");
        }

        sb.append("centerX=").append(getCenterX());
        sb.append(", centerY=").append(getCenterY());
        sb.append(", radius=").append(getRadius());

        sb.append(", fill=").append(getFill());

        Paint stroke = getStroke();
        if (stroke != null) {
            sb.append(", stroke=").append(stroke);
            sb.append(", strokeWidth=").append(getStrokeWidth());
        }

        return sb.append("]").toString();
    }
}


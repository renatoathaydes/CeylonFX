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

package javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGSphere;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.input.PickResult;
import javafx.scene.transform.Rotate;

/**
 * The {@code Sphere} class defines a 3 dimensional sphere with the specified size.
 * A {@code Sphere} is a 3D geometry primitive created with a given radius.
 * It is centered at the origin.
 *
 * @since JavaFX 8.0
 */
public class Sphere extends Shape3D {

    static final int DEFAULT_DIVISIONS = 64;
    static final double DEFAULT_RADIUS = 1;
    private int divisions = DEFAULT_DIVISIONS;
    private TriangleMesh mesh;

    /**  
     * Creates a new instance of {@code Sphere} of radius of 1.0.
     * The resolution defaults to MID_RESOLUTION divisions along sphere's axes.
     */
    public Sphere() {
        this(DEFAULT_RADIUS, DEFAULT_DIVISIONS);
    }

    /**
     * Creates a new instance of {@code Sphere} of a given radius.
     * The resolution defaults to MID_RESOLUTION divisions along sphere's axes.
     *
     * @param radius Radius
     */
    public Sphere(double radius) {
        this(radius, DEFAULT_DIVISIONS);
    }

    /**  
     * Creates a new instance of {@code Sphere} of a given radius and number
     * of divisions.
     * The resolution is defined in terms of number of subdivisions along the
     * sphere's axes. More divisions lead to more finely tesselated objects.
     *
     * Note that divisions should be at least 1. Any value less than that will be
     * clamped to 1.
     * 
     * @param radius Radius
     * @param divisions Divisions     
     */
    public Sphere(double radius, int divisions) {
        this.divisions = divisions < 1 ? 1: divisions;
        setRadius(radius);
    }

    /**
     * Defines the radius of the Sphere.
     *
     * @defaultValue 1.0
     */
    private DoubleProperty radius;

    public final void setRadius(double value) {
        radiusProperty().set(value);
    }

    public final double getRadius() {
        return radius == null ? 1 : radius.get();
    }

    public final DoubleProperty radiusProperty() {
        if (radius == null) {
            radius = new SimpleDoubleProperty(Sphere.this, "radius", DEFAULT_RADIUS) {
                @Override
                public void invalidated() {
                    impl_markDirty(DirtyBits.MESH_GEOM);
                    manager.invalidateSphereMesh(key);
                    key = 0;
                    impl_geomChanged();
                }
            };
        }
        return radius;
    }

    /**
     * Retrieves the divisions attribute use to generate this sphere.
     *
     * @return the divisions attribute.
     */
    public int getDivisions() {
        return divisions;
    }

    /**
     * @treatAsPrivate implementation detail
     * @deprecated This is an internal API that is not intended for use and will be removed in the next version
     */
    @Deprecated
    @Override
    protected NGNode impl_createPeer() {
        return new NGSphere();
    }

    /**
     * @treatAsPrivate implementation detail
     * @deprecated This is an internal API that is not intended for use and will be removed in the next version
     */
    @Deprecated
    public void impl_updatePeer() {
        super.impl_updatePeer();
        if (impl_isDirty(DirtyBits.MESH_GEOM)) {
            final NGSphere pgSphere = impl_getPeer();
            final float r = (float) getRadius();
            if (r < 0) {
                pgSphere.updateMesh(null);
            } else {
                if (key == 0) {
                    key = generateKey(r, divisions);
                }
                mesh = manager.getSphereMesh(r, divisions, key);
                mesh.impl_updatePG();
                pgSphere.updateMesh(mesh.impl_getPGTriangleMesh());
            }
        }
    }

    /**
     * @treatAsPrivate implementation detail
     * @deprecated This is an internal API that is not intended for use and will be removed in the next version
     */
    @Deprecated
    @Override
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        final float r = (float) getRadius();
        
        if (r < 0) {
            return bounds.makeEmpty();
        }        
        
        bounds = bounds.deriveWithNewBounds(-r, -r, -r, r, r ,r);
        bounds = tx.transform(bounds, bounds);
        return bounds;
    }

    /**
     * @treatAsPrivate implementation detail
     * @deprecated This is an internal API that is not intended for use and will be removed in the next version
     */
    @Deprecated
    @Override
    protected boolean impl_computeContains(double localX, double localY) {
        double r = getRadius();
        double n2 = localX * localX + localY * localY;
        return n2 <= r * r;
    }

    /**
     * @treatAsPrivate implementation detail
     * @deprecated This is an internal API that is not intended for use and will be removed in the next version
     */
    @Deprecated
    @Override
    protected boolean impl_computeIntersects(PickRay pickRay, PickResultChooser pickResult) {

        final boolean exactPicking = divisions < DEFAULT_DIVISIONS && mesh != null;

        final double r = getRadius();
        final Vec3d dir = pickRay.getDirectionNoClone();
        final double dirX = dir.x;
        final double dirY = dir.y;
        final double dirZ = dir.z;
        final Vec3d origin = pickRay.getOriginNoClone();
        final double originX = origin.x;
        final double originY = origin.y;
        final double originZ = origin.z;

        // Coeficients of a quadratic equation desribing intersection with sphere
        final double a = dirX * dirX + dirY * dirY + dirZ * dirZ;
        final double b = 2 * (dirX * originX + dirY * originY + dirZ * originZ);
        final double c = originX * originX + originY * originY + originZ * originZ - r * r;

        final double discriminant = b * b - 4 * a * c;
        if (discriminant < 0) {
            // No real roots of the equation, missed the shape
            return false;
        }

        final double distSqrt = Math.sqrt(discriminant);
        final double q = (b < 0) ? (-b - distSqrt) / 2.0 : (-b + distSqrt) / 2.0;

        double t0 = q / a;
        double t1 = c / q;

        if (t0 > t1) {
            final double temp = t0;
            t0 = t1;
            t1 = temp;
        }

        final double minDistance = pickRay.getNearClip();
        final double maxDistance = pickRay.getFarClip();

        if (t1 < minDistance || t0 > maxDistance) {
            // the sphere is out of clipping planes
            return false;
        }

        double t = t0;
        final CullFace cullFace = getCullFace();
        if (t0 < minDistance || cullFace == CullFace.FRONT) {
            if (t1 <= maxDistance && getCullFace() != CullFace.BACK) {
                // picking the back wall
                t = t1;
            } else {
                // we are inside the sphere with the back wall culled, but the
                // exact picking still needs to be done because the front faced
                // triangles may still be in front of us
                if (!exactPicking) {
                    return false;
                }
            }
        }

        if (Double.isInfinite(t) || Double.isNaN(t)) {
            // We've got a nonsense pick ray or sphere size.
            return false;
        }

        if (exactPicking) {
            return mesh.impl_computeIntersects(pickRay, pickResult, this, cullFace, false);
        }

        if (pickResult != null && pickResult.isCloser(t)) {
            final Point3D point = PickResultChooser.computePoint(pickRay, t);

            // computing texture coords
            final Point3D proj = new Point3D(point.getX(), 0, point.getZ());
            final Point3D cross = proj.crossProduct(Rotate.Z_AXIS);
            double angle = proj.angle(Rotate.Z_AXIS);
            if (cross.getY() > 0) {
                angle = 360 - angle;
            }
            Point2D txtCoords = new Point2D(1 - angle / 360, 0.5 + point.getY() / (2 * r));

            pickResult.offer(this, t, PickResult.FACE_UNDEFINED, point, txtCoords);
        }
        return true;
    }

    private static int correctDivisions(int div) {
        return ((div + 3) / 4) * 4;
    }

    static TriangleMesh createMesh(int div, float r) {
        div = correctDivisions(div);

        // NOTE: still create mesh for degenerated sphere
        final int div2 = div / 2;

        final int nPoints = div * (div2 - 1) + 2;
        final int nTPoints = (div + 1) * (div2 - 1) + div * 2;
        final int nFaces = div * (div2 - 2) * 2 + div * 2;

        final float rDiv = 1.f / div;

        float points[] = new float[nPoints * 3];
        float tPoints[] = new float[nTPoints * 2];
        int faces[] = new int[nFaces * 6];

        int pPos = 0, tPos = 0;

        for (int y = 0; y < div2 - 1; ++y) {
            float va = rDiv * (y + 1 - div2 / 2) * 2 * (float) Math.PI;
            float sin_va = (float) Math.sin(va);
            float cos_va = (float) Math.cos(va);

            float ty = 0.5f + sin_va * 0.5f;
            for (int i = 0; i < div; ++i) {
                double a = rDiv * i * 2 * (float) Math.PI;
                float hSin = (float) Math.sin(a);
                float hCos = (float) Math.cos(a);
                points[pPos + 0] = hSin * cos_va * r;
                points[pPos + 2] = hCos * cos_va * r;
                points[pPos + 1] = sin_va * r;
                tPoints[tPos + 0] = 1 - rDiv * i;
                tPoints[tPos + 1] = ty;
                pPos += 3;
                tPos += 2;
            }
            tPoints[tPos + 0] = 0;
            tPoints[tPos + 1] = ty;
            tPos += 2;
        }

        points[pPos + 0] = 0;
        points[pPos + 1] = -r;
        points[pPos + 2] = 0;
        points[pPos + 3] = 0;
        points[pPos + 4] = r;
        points[pPos + 5] = 0;
        pPos += 6;

        int pS = (div2 - 1) * div;

        float textureDelta = 1.f / 256;
        for (int i = 0; i < div; ++i) {
            tPoints[tPos + 0] = rDiv * (0.5f + i);
            tPoints[tPos + 1] = textureDelta;
            tPos += 2;
        }

        for (int i = 0; i < div; ++i) {
            tPoints[tPos + 0] = rDiv * (0.5f + i);
            tPoints[tPos + 1] = 1 - textureDelta;
            tPos += 2;
        }

        int fIndex = 0;
        for (int y = 0; y < div2 - 2; ++y) {
            for (int x = 0; x < div; ++x) {
                int p0 = y * div + x;
                int p1 = p0 + 1;
                int p2 = p0 + div;
                int p3 = p1 + div;

                int t0 = p0 + y;
                int t1 = t0 + 1;
                int t2 = t0 + (div + 1);
                int t3 = t1 + (div + 1);

                // add p0, p1, p2
                faces[fIndex + 0] = p0;
                faces[fIndex + 1] = t0;
                faces[fIndex + 2] = p1 % div == 0 ? p1 - div : p1;
                faces[fIndex + 3] = t1;
                faces[fIndex + 4] = p2;
                faces[fIndex + 5] = t2;
                fIndex += 6;

                // add p3, p2, p1
                faces[fIndex + 0] = p3 % div == 0 ? p3 - div : p3;
                faces[fIndex + 1] = t3;
                faces[fIndex + 2] = p2;
                faces[fIndex + 3] = t2;
                faces[fIndex + 4] = p1 % div == 0 ? p1 - div : p1;
                faces[fIndex + 5] = t1;
                fIndex += 6;
            }
        }

        int p0 = pS;
        int tB = (div2 - 1) * (div + 1);
        for (int x = 0; x < div; ++x) {
            int p2 = x, p1 = x + 1, t0 = tB + x;
            faces[fIndex + 0] = p0;
            faces[fIndex + 1] = t0;
            faces[fIndex + 2] = p1 == div ? 0 : p1;
            faces[fIndex + 3] = p1;
            faces[fIndex + 4] = p2;
            faces[fIndex + 5] = p2;
            fIndex += 6;
        }

        p0 = p0 + 1;
        tB = tB + div;
        int pB = (div2 - 2) * div;

        for (int x = 0; x < div; ++x) {
            int p1 = pB + x, p2 = pB + x + 1, t0 = tB + x;
            int t1 = (div2 - 2) * (div + 1) + x, t2 = t1 + 1;
            faces[fIndex + 0] = p0;
            faces[fIndex + 1] = t0;
            faces[fIndex + 2] = p1;
            faces[fIndex + 3] = t1;
            faces[fIndex + 4] = p2 % div == 0 ? p2 - div : p2;
            faces[fIndex + 5] = t2;
            fIndex += 6;
        }

        TriangleMesh m = new TriangleMesh(true);
        m.getPoints().setAll(points);
        m.getTexCoords().setAll(tPoints);
        m.getFaces().setAll(faces);
        return m;
    }

    private static int generateKey(float r, int div) {
        int hash = 5;
        hash = 23 * hash + Float.floatToIntBits(r);
        hash = 23 * hash + div;
        return hash;
    }
}

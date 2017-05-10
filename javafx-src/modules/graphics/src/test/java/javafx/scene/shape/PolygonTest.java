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

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGPolygon;
import javafx.collections.ObservableList;
import javafx.scene.NodeTest;
import org.junit.Test;

import static com.sun.javafx.test.TestHelper.assertBoundsEqual;
import static com.sun.javafx.test.TestHelper.box;
import static org.junit.Assert.*;

public class PolygonTest {
    @Test public void testPropertyPropagation_emptyPoints() {
        final StubPolygon polygon = new StubPolygon();
        NodeTest.callSyncPGNode(polygon);
        assertPGPolygonPointsEquals(polygon, new double[0]);
    }

    @Test public void testPropertyPropagation_pointsEvenLength() {
        final double[] initialPoints = { 10, 20, 100, 200, 200, 100, 50, 10 };

        final StubPolygon polygon = new StubPolygon(initialPoints);
        NodeTest.callSyncPGNode(polygon);
        assertPGPolygonPointsEquals(polygon, initialPoints);

        final ObservableList<Double> polygonPoints = polygon.getPoints();
        polygonPoints.remove(1);
        polygonPoints.remove(2);

        NodeTest.callSyncPGNode(polygon);
        assertPGPolygonPointsEquals(polygon, 10, 100, 200, 100, 50, 10);
    }

    @Test public void testPropertyPropagation_pointsOddLength() {
        final double[] initialPoints = { 10, 20, 100, 200, 200 };

        final StubPolygon polygon = new StubPolygon(initialPoints);
        NodeTest.callSyncPGNode(polygon);
        assertPGPolygonPointsEquals(polygon, initialPoints);

        final ObservableList<Double> polygonPoints = polygon.getPoints();
        polygonPoints.add(100.0);
        polygonPoints.add(50.0);

        NodeTest.callSyncPGNode(polygon);
        assertPGPolygonPointsEquals(polygon, 10, 20, 100, 200, 200, 100, 50);
    }

    @Test public void testPropertyPropagation_visible() throws Exception {
        final StubPolygon polygon = new StubPolygon();
        NodeTest.testBooleanPropertyPropagation(polygon, "visible", false, true);
    }

    @Test public void testBounds_emptyPoints() {
        final StubPolygon polygon = new StubPolygon();
        assertBoundsEqual(box(0, 0, -1, -1), polygon.getBoundsInLocal());
    }

    @Test public void testBounds_evenPointsLength() {
        final double[] initialPoints = { 100, 100, 200, 100, 200, 200 };

        final StubPolygon polygon = new StubPolygon(initialPoints);
        assertBoundsEqual(box(100, 100, 100, 100), polygon.getBoundsInLocal());

        final ObservableList<Double> polygonPoints = polygon.getPoints();
        polygonPoints.add(150.0);
        polygonPoints.add(300.0);

        assertBoundsEqual(box(100, 100, 100, 200), polygon.getBoundsInLocal());
    }

    @Test public void testBounds_oddPointsLength() {
        final double[] initialPoints = {
            100, 100, 200, 100, 200, 200, 150, 300
        };

        final StubPolygon polygon = new StubPolygon(initialPoints);
        assertBoundsEqual(box(100, 100, 100, 200), polygon.getBoundsInLocal());

        final ObservableList<Double> polygonPoints = polygon.getPoints();
        polygonPoints.remove(6);

        assertBoundsEqual(box(100, 100, 100, 100), polygon.getBoundsInLocal());
    }
    
    @Test public void testConfigShape() throws Exception {
        final StubPolygon polygon =
                new StubPolygon(new double[] { 0, 0, 0, 1, 1, 1, 1, 0 });
        final Path2D path = polygon.impl_configShape();
        assertTrue(path.contains(0.5f, 0.5f));
        assertFalse(path.contains(0, 2));
    }

    private static void assertPGPolygonPointsEquals(
            final StubPolygon polygon,
            final double... expectedPoints) {
        final StubNGPolygon stubPolygon = polygon.impl_getPeer();
        final float[] pgPoints = stubPolygon.points;

        final int minLength = expectedPoints.length & ~1;
        final int maxLength = expectedPoints.length;

        assertTrue(pgPoints.length >= minLength);
        assertTrue(pgPoints.length <= maxLength);

        int i;

        for (i = 0; i < minLength; ++i) {
            assertEquals(expectedPoints[i], pgPoints[i], 0);
        }

        for (; i < pgPoints.length; ++i) {
            assertEquals(expectedPoints[i], pgPoints[i], 0);
        }
    }

    @Test public void toStringShouldReturnNonEmptyString() {
        String s = new Polygon().toString();
        assertNotNull(s);
        assertFalse(s.isEmpty());
    }

    private final class StubPolygon extends Polygon {
        public StubPolygon(double... initialPoints) {
            super(initialPoints);
        }

        public StubPolygon() {
            super();
        }

        @Override
        protected NGNode impl_createPeer() {
            return new StubNGPolygon();
        }
    }

    private final class StubNGPolygon extends NGPolygon {
        private float[] points;
        @Override
        public void updatePolygon(float[] points) {
            super.updatePolygon(points);
            this.points = points;
        }
    }
}

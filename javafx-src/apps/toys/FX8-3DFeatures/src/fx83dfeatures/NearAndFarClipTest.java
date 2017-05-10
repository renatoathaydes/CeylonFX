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
package fx83dfeatures;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class NearAndFarClipTest extends Application {

    private static final double OFFSET_PERCENT = 0.01; // 1 percent tolerance
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private static final double FOV = 30.0;
    private static final double NEAR = 0.1;
    private static final double FAR = 10.0;

    private Scene createClipPlanes(Stage stage) {
        stage.setTitle("Near and Far Clip Test");

        final double tanOfHalfFOV = Math.tan(Math.toRadians(FOV) / 2.0);
        final double halfHeight = HEIGHT / 2;
        final double focalLength = halfHeight / tanOfHalfFOV;
        final double eyePositionZ = -1.0 * focalLength;
        final double nearClipDistance = focalLength * NEAR + eyePositionZ;
        final double farClipDistance = focalLength * FAR + eyePositionZ;
        final double nearClipDistanceOffset = Math.abs(nearClipDistance * OFFSET_PERCENT);
        final double farClipDistanceOffset = Math.abs(farClipDistance * OFFSET_PERCENT);

        System.out.println("In scene coordinate: focalLength = " + focalLength
                + ", nearClipDistance = " + nearClipDistance
                + ", nearClipDistanceOffset = " + nearClipDistanceOffset
                + ", farClipDistance = " + farClipDistance
                + ", farClipDistanceOffset = " + farClipDistanceOffset);

        Rectangle insideRect = new Rectangle(220, 220, Color.GREEN);
        insideRect.setLayoutX(140);
        insideRect.setLayoutY(140);
        insideRect.setId("Green");

        Rectangle insideNearClip = new Rectangle(16, 16, Color.BLUE);
        insideNearClip.setLayoutX(242);
        insideNearClip.setLayoutY(242);
        insideNearClip.setTranslateZ(nearClipDistance + nearClipDistanceOffset);
        insideNearClip.setId("Blue");

        Rectangle outsideNearClip = new Rectangle(8, 8, Color.YELLOW);
        outsideNearClip.setLayoutX(246);
        outsideNearClip.setLayoutY(246);
        outsideNearClip.setTranslateZ(nearClipDistance - nearClipDistanceOffset);
        outsideNearClip.setId("Yellow");

        Rectangle insideFarClip = new Rectangle(3000, 3000, Color.RED);
        insideFarClip.setTranslateX(-1250);
        insideFarClip.setTranslateY(-1250);
        insideFarClip.setTranslateZ(farClipDistance - farClipDistanceOffset);
        insideFarClip.setId("Red");

        Rectangle outsideFarClip = new Rectangle(4000, 4000, Color.CYAN);
        outsideFarClip.setTranslateX(-1750);
        outsideFarClip.setTranslateY(-1750);
        outsideFarClip.setTranslateZ(farClipDistance + farClipDistanceOffset);
        outsideFarClip.setId("Cyan");

        Group root = new Group();

        // Render in painter order (far to near)
        root.getChildren().addAll(outsideFarClip, insideFarClip, insideRect, insideNearClip, outsideNearClip);
        Slider near = new Slider(NEAR*0.9, NEAR*1.1, NEAR);
        Slider far = new Slider(FAR*0.9, FAR*1.1, FAR);
        root.getChildren().addAll(new VBox(near, far));

        // Intentionally set depth buffer to false to reduce test complexity
        Scene scene = new Scene(root, WIDTH, HEIGHT, false);
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                System.out.println("Clicked: " + event.getTarget());
            }
        });


        PerspectiveCamera camera = new PerspectiveCamera();
        camera.setFieldOfView(FOV);
        camera.nearClipProperty().bind(near.valueProperty());
        camera.farClipProperty().bind(far.valueProperty());
        scene.setCamera(camera);
        return scene;
    }

    @Override public void start(Stage stage) {
        Scene scene = createClipPlanes(stage);
        scene.setFill(Color.GRAY);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();
        System.out.println("You should expect to see 3 overlapping squares in"
                + " the order of Blue is on top of Green and Green is on top Red.");
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}

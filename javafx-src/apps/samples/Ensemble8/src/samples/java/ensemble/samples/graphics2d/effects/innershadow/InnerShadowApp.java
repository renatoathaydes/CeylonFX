/*
 * Copyright (c) 2008, 2013 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle Corporation nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package ensemble.samples.graphics2d.effects.innershadow;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * A sample that demonstrates how an inner shadow effect is affected by various
 * settings.
 *
 * @sampleName Inner Shadow
 * @preview preview.png
 * @playground sample.fill (name="Text Fill")
 * @playground innerShadow.radius (name="Inner Shadow Radius", min=0, max=20)
 * @playground innerShadow.offsetX (name="Inner Shadow Offset X", min=-10, max=10)
 * @playground innerShadow.offsetY (name="Inner Shadow Offset Y", min=-10, max=10)
 * @playground innerShadow.color (name="Inner Shadow Color")
 * @see javafx.scene.effect.InnerShadow
 * @see javafx.scene.effect.Effect
 * @embedded
 */
public class InnerShadowApp extends Application {

    private InnerShadow innerShadow = new InnerShadow();
    private Text sample = new Text(0, 100, "Shadow");

    public Parent createContent() {
        StackPane root = new StackPane();
        sample.setFont(Font.font("Arial Black", 80));
        sample.setFill(Color.web("#BBBBBB"));
        innerShadow.setRadius(5d);
        innerShadow.setOffsetX(2);
        innerShadow.setOffsetY(2);
        sample.setEffect(innerShadow);
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(sample);
        return root;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    /**
     * Java main for when running without JavaFX launcher
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}

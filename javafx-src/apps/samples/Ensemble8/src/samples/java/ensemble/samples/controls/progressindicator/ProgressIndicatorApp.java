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
package ensemble.samples.controls.progressindicator;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * A sample that demonstrates the Progress Indicator control in various modes.
 *
 * @sampleName Progress Indicator
 * @preview preview.png
 * @see javafx.scene.control.ProgressIndicator
 * @related /Controls/Progress Bar
 * @embedded
 */
public class ProgressIndicatorApp extends Application {
    final Timeline timeline = new Timeline();
    
    public Parent createContent() {
        GridPane g = new GridPane();

        ProgressIndicator p1 = new ProgressIndicator();
        p1.setPrefSize(50, 50);

        ProgressIndicator p2 = new ProgressIndicator();
        p2.setPrefSize(50, 50);
        p2.setProgress(0.25F);

        ProgressIndicator p3 = new ProgressIndicator();
        p3.setPrefSize(50, 50);
        p3.setProgress(0.5F);

        ProgressIndicator p4 = new ProgressIndicator();
        p4.setPrefSize(50, 50);
        p4.setProgress(1.0F);
        
        // styled ProgressIndicator
        final ProgressIndicator p5 = new ProgressIndicator();
        p5.setPrefSize(100, 100);
        p5.progressProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue ov, Number oldVal, Number newVal) {
                if (p5.getProgress() < 0.25) {
                    p5.setStyle("-fx-progress-color: red;");
                } else if (p5.getProgress() < 0.5) {
                    p5.setStyle("-fx-progress-color: orange;");
                } else {
                    p5.setStyle("-fx-progress-color: green;");
                }
            }
        });
        // animate the styled ProgressIndicator
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        final KeyValue kv = new KeyValue(p5.progressProperty(), 1);
        final KeyFrame kf1 = new KeyFrame(Duration.millis(3000), kv);
        timeline.getKeyFrames().add(kf1);

        g.add(p1, 1, 0);
        g.add(p2, 0, 1);
        g.add(p3, 1, 1);
        g.add(p4, 2, 1);
        g.add(p5, 1, 2);

        g.setHgap(20);
        g.setVgap(20);
        g.setAlignment(Pos.CENTER);
        return g;
    }

    public void play() {
        timeline.play();
    }
    @Override
    public void stop() {
        timeline.stop();
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
        play();
    }

    /**
     * Java main for when running without JavaFX launcher
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}

/*
 * Copyright (c) 2012, 2014, Oracle and/or its affiliates.
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
package com.oracle.javafx.scenebuilder.app.preview;

import com.oracle.javafx.scenebuilder.app.i18n.I18N;
import com.oracle.javafx.scenebuilder.kit.editor.panel.util.dialog.AbstractModalDialog;
import com.oracle.javafx.scenebuilder.kit.util.control.paintpicker.PaintPicker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Window;

/**
 * Controller for the preview background color.
 */
public class BackgroundColorDialogController extends AbstractModalDialog {

    public BackgroundColorDialogController(Window owner) {
        super(BackgroundColorDialogController.class.getResource("BackgroundColor.fxml"), //NOI18N
                I18N.getBundle(), owner);
    }

    /*
     * AbstractModalDialog
     */
    @Override
    protected void controllerDidLoadFxml() {
        super.controllerDidLoadFxml();
        setActionButtonVisible(false);
        setDefaultButtonID(AbstractModalDialog.ButtonID.OK);
        setShowDefaultButton(true);
        // Update title
        final String title = I18N.getString("preview.background.color");
        getStage().setTitle(title);
    }

    @Override
    protected void controllerDidLoadContentFxml() {

        assert getContentRoot() instanceof Pane;
        final Pane contentRoot = (Pane) getContentRoot();

        final PaintPicker.Delegate delegate = new PaintPickerDelegate();
        final PaintPicker paintPicker = new PaintPicker(delegate);
        contentRoot.getChildren().add(paintPicker);

        final ChangeListener<Number> sizeListener = new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                getStage().sizeToScene();
            }
        };

        contentRoot.heightProperty().addListener(sizeListener);
        contentRoot.widthProperty().addListener(sizeListener);

        paintPicker.paintProperty().addListener(new PaintChangeListener<Paint>());
    }

    @Override
    protected void okButtonPressed(ActionEvent e) {
        closeWindow();
    }

    @Override
    protected void cancelButtonPressed(ActionEvent e) {
        closeWindow();
    }

    @Override
    protected void actionButtonPressed(ActionEvent e) {
        // Should not be called because button is hidden
        throw new IllegalStateException();
    }

    private static class PaintChangeListener<Paint> implements ChangeListener<Paint> {

        @Override
        public void changed(ObservableValue<? extends Paint> ov, Paint oldValue, Paint newValue) {
            // Missing update EditorController backgroundColor property
            System.err.println("Update Preview background color with " + newValue); //NOI18N
        }
    }
    
    private static class PaintPickerDelegate implements PaintPicker.Delegate {

        @Override
        public void handleError(String warningKey, Object... arguments) {
            // Log a warning in message bar
        }
    }
}

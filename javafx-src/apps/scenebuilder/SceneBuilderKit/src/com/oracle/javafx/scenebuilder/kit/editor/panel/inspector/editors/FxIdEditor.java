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
package com.oracle.javafx.scenebuilder.kit.editor.panel.inspector.editors;

import com.oracle.javafx.scenebuilder.kit.editor.EditorController;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMIndex;
import com.oracle.javafx.scenebuilder.kit.util.JavaLanguage;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * fx:id editor.
 *
 *
 */
public class FxIdEditor extends AutoSuggestEditor {

    private static final String PROPERTY_NAME = "fx:id";
    private static final String DEFAULT_VALUE = null;
    private EditorController editorController;

    @SuppressWarnings("LeakingThisInConstructor")
    public FxIdEditor(List<String> suggestedFxIds, EditorController editorController) {
        super(PROPERTY_NAME, DEFAULT_VALUE, suggestedFxIds); //NOI18N
        this.editorController = editorController;

        // text field events handling
        EventHandler<ActionEvent> onActionListener = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (isHandlingError()) {
                    // Event received because of focus lost due to error dialog
                    return;
                }
                String value = textField.getText();
                if (value != null && !value.isEmpty()) {
                    if (!JavaLanguage.isIdentifier(value)) {
//                        System.err.println(I18N.getString("log.warning.invalid.fxid", value));
                        handleInvalidValue(value);
                        return;
                    }
                    if (isValueChanged(value)) {
                        // Avoid multiple identical messages
                        if (getFxIdsInUse().contains(value)) {
                            editorController.getMessageLog().logWarningMessage(
                                    "log.warning.duplicate.fxid", value); //NOI18N
                        } else if ((getControllerClass() != null) && !getSuggestedList().contains(value)) {
                            editorController.getMessageLog().logWarningMessage(
                                    "log.warning.no.injectable.fxid", value); //NOI18N
                        }
                    }
                }
                userUpdateValueProperty((value == null || value.isEmpty()) ? null : value);
                textField.selectAll();
            }
        };
        setTextEditorBehavior(this, textField, onActionListener);
    }

    public void reset(List<String> suggestedFxIds, EditorController editorController) {
        reset(PROPERTY_NAME, DEFAULT_VALUE, suggestedFxIds);
        this.editorController = editorController;
    }

    private List<String> getFxIdsInUse() {
        FXOMIndex fxomIndex = new FXOMIndex(editorController.getFxomDocument());
        return new ArrayList<>(fxomIndex.getFxIds().keySet());
    }

    private String getControllerClass() {
        return editorController.getFxomDocument().getFxomRoot().getFxController();
    }

}

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

import com.oracle.javafx.scenebuilder.kit.editor.i18n.I18N;
import static com.oracle.javafx.scenebuilder.kit.editor.panel.inspector.editors.PropertyEditor.handleIndeterminate;
import com.oracle.javafx.scenebuilder.kit.metadata.property.ValuePropertyMetadata;
import com.oracle.javafx.scenebuilder.kit.metadata.property.value.CursorPropertyMetadata;
import java.util.Map;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

/**
 * Insets editor (for top/right/bottom/left fields).
 *
 *
 */
public class CursorEditor extends PropertyEditor {

    private final Parent root;

    @FXML
    private MenuButton cursorMb;
    @FXML
    private CheckMenuItem inheritedMi;
    @FXML
    private Label inheritedLb;

    private Cursor cursor = Cursor.DEFAULT;
    private String inheritedText, inheritedParentText;

    @SuppressWarnings("LeakingThisInConstructor")
    public CursorEditor(ValuePropertyMetadata propMeta, Set<Class<?>> selectedClasses) {
        super(propMeta, selectedClasses);
        root = EditorUtils.loadFxml("CursorEditor.fxml", this); //NOI18N
        initialize();
    }

    // Separate method to please FindBugs
    private void initialize() {
        int index = 0;
        Map<Cursor, String> predefinedCursors = CursorPropertyMetadata.getCursorMap();
        // Order the cursors
        Cursor[] cursorList = {Cursor.DEFAULT, Cursor.CLOSED_HAND, Cursor.OPEN_HAND, Cursor.HAND, Cursor.MOVE, Cursor.WAIT,
            Cursor.TEXT, Cursor.V_RESIZE, Cursor.H_RESIZE, Cursor.N_RESIZE, Cursor.NE_RESIZE, Cursor.E_RESIZE, Cursor.SE_RESIZE,
            Cursor.S_RESIZE, Cursor.SW_RESIZE, Cursor.W_RESIZE, Cursor.NW_RESIZE,
            Cursor.CROSSHAIR, Cursor.NONE, Cursor.DISAPPEAR};
        for (Cursor cursorObj : cursorList) {
            String cursorStr = predefinedCursors.get(cursorObj);
            final Label cursorLabel = new Label(cursorStr);
            cursorLabel.setCursor(cursorObj);
            CheckMenuItem menuItem = new CheckMenuItem();
            menuItem.setGraphic(cursorLabel);
            // add predefined cursors before "inherited" menu item
            cursorMb.getItems().add(index++, menuItem);
            menuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    selectCursor(cursorStr);
                    userUpdateValueProperty(getValue());
                }
            });
        }

        // "inherited" menu item
        inheritedText = I18N.getString("inspector.cursor.inherited");
        inheritedParentText = I18N.getString("inspector.cursor.inheritedparent");
        inheritedLb.setText(inheritedParentText);
    }

    @Override
    public Node getValueEditor() {
        return super.handleGenericModes(root);
    }

    @Override
    public Object getValue() {
        return cursor;
    }

    @Override
    public void setValue(Object value) {
        setValueGeneric(value);
        if (isSetValueDone()) {
            return;
        }

        if (value == null) {
            cursor = null;
            selectCursor(inheritedParentText);

        } else {
            assert value instanceof Cursor;
            if (value instanceof ImageCursor) {
                // Custom cursor
                selectCursor(""); //NOI18N
                cursorMb.setText(I18N.getString("inspector.cursor.custom"));
            } else {
                // predefined cursor
                // select the corresponding menu item
                selectCursor(((Cursor) value).toString());
            }
        }
    }

    @Override
    public void reset(ValuePropertyMetadata propMeta, Set<Class<?>> selectedClasses) {
        super.reset(propMeta, selectedClasses);
    }

    @Override
    protected void valueIsIndeterminate() {
        handleIndeterminate(cursorMb);
    }

    //
    // FXML methods
    //
    @FXML
    void inherited(ActionEvent event) {
        cursor = null;
        selectCursor(inheritedParentText);
        userUpdateValueProperty(getValue());
    }

    // Select the menu item corresponding to a cursor string.
    private void selectCursor(String cursorStr) {
        for (MenuItem menuItem : cursorMb.getItems()) {
            if (!(menuItem instanceof CheckMenuItem)) {
                // inherited action
                continue;
            }
            CheckMenuItem checkMenuItem = (CheckMenuItem) menuItem;
            assert checkMenuItem.getGraphic() instanceof Label;
            if (cursorStr.equals(((Label) checkMenuItem.getGraphic()).getText())) {
                checkMenuItem.setSelected(true);
                // set the menu button text
                if (cursorStr.equals(inheritedParentText)) {
                    // change the menu button text to be shorter in this case...
                    cursorMb.setText(inheritedText);
                } else {
                    cursorMb.setText(cursorStr);
                }
                cursor = checkMenuItem.getGraphic().getCursor();
            } else {
                checkMenuItem.setSelected(false);
            }
        }
    }

    @Override
    public void requestFocus() {
        EditorUtils.doNextFrame(new Runnable() {

            @Override
            public void run() {
                cursorMb.requestFocus();
            }
        });
    }
}

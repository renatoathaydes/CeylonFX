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

package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ComboBoxListViewBehavior;
import java.util.List;

import com.sun.javafx.scene.control.behavior.TextFieldBehavior;
import com.sun.javafx.scene.traversal.TraversalEngine;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class ComboBoxListViewSkin<T> extends ComboBoxPopupControl<T> {
    
    // By default we measure the width of all cells in the ListView. If this
    // is too burdensome, the developer may set a property in the ComboBox
    // properties map with this key to specify the number of rows to measure.
    // This may one day become a property on the ComboBox itself.
    private static final String COMBO_BOX_ROWS_TO_MEASURE_WIDTH_KEY = "comboBoxRowsToMeasureWidth";

    
    
    /***************************************************************************
     *                                                                         *
     * Private fields                                                          *
     *                                                                         *
     **************************************************************************/    
    
    private final ComboBox<T> comboBox;
    private ObservableList<T> comboBoxItems;
    
    private ListCell<T> buttonCell;
    private Callback<ListView<T>, ListCell<T>> cellFactory;
    private TextField textField;
    
    private final ListView<T> listView;
    private ObservableList<T> listViewItems;
    
    private boolean listSelectionLock = false;
    private boolean listViewSelectionDirty = false;

    
    /***************************************************************************
     *                                                                         *
     * Listeners                                                               *
     *                                                                         *
     **************************************************************************/
    
    private boolean itemCountDirty;
    private final ListChangeListener<T> listViewItemsListener = new ListChangeListener<T>() {
        @Override public void onChanged(ListChangeListener.Change<? extends T> c) {
            itemCountDirty = true;
            getSkinnable().requestLayout();
        }
    };
    
    private final WeakListChangeListener<T> weakListViewItemsListener =
            new WeakListChangeListener<T>(listViewItemsListener);
    
    
    
    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/   
    
    public ComboBoxListViewSkin(final ComboBox<T> comboBox) {
        super(comboBox, new ComboBoxListViewBehavior<T>(comboBox));
        this.comboBox = comboBox;
        updateComboBoxItems();
        
        // editable input node
        this.textField = comboBox.isEditable() ? getEditableInputNode() : null;
        
        // Fix for RT-29565. Without this the textField does not have a correct
        // pref width at startup, as it is not part of the scenegraph (and therefore
        // has no pref width until after the first measurements have been taken).
        if (this.textField != null) {
            getChildren().add(textField);
        }
        
        // listview for popup
        this.listView = createListView();
        
        // Fix for RT-21207. Additional code related to this bug is further below.
        this.listView.setManaged(false);
        getChildren().add(listView);
        // -- end of fix
                
        updateListViewItems();
        updateCellFactory();
        
        updateButtonCell();

        // move fake focus in to the textfield if the comboBox is editable
        comboBox.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean hasFocus) {
                if (comboBox.isEditable()) {
                    // Fix for the regression noted in a comment in RT-29885.
                    ((FakeFocusTextField)textField).setFakeFocus(hasFocus);
                }
            }
        });
        
        comboBox.addEventFilter(KeyEvent.ANY, new EventHandler<KeyEvent>() {
            @Override public void handle(KeyEvent ke) {
                if (textField == null) return;

                // This prevents a stack overflow from our rebroadcasting of the
                // event to the textfield that occurs in the final else statement
                // of the conditions below.
                if (ke.getTarget().equals(textField)) return;

                // When the user hits the enter or F4 keys, we respond before 
                // ever giving the event to the TextField.
                if (ke.getCode() == KeyCode.ENTER) {
                    setTextFromTextFieldIntoComboBoxValue();
                    /*
                    ** don't consume this if we're on an embedded
                    ** platform that supports 5-button navigation 
                    */
                    if (!Utils.isTwoLevelFocus()) {
                        ke.consume();
                    }
                    return;
                } else if (ke.getCode() == KeyCode.F4 && ke.getEventType() == KeyEvent.KEY_RELEASED) {
                    if (comboBox.isShowing()) comboBox.hide();
                    else comboBox.show();
                    ke.consume();
                    return;
                } else if (ke.getCode() == KeyCode.F10 || ke.getCode() == KeyCode.ESCAPE) {
                    // RT-23275: The TextField fires F10 and ESCAPE key events
                    // up to the parent, which are then fired back at the 
                    // TextField, and this ends up in an infinite loop until
                    // the stack overflows. So, here we consume these two
                    // events and stop them from going any further.
                    ke.consume();
                    return;
                } else {
                    // Fix for the regression noted in a comment in RT-29885.
                    // This forwards the event down into the TextField when
                    // the key event is actually received by the ComboBox.
                    textField.fireEvent(ke.copyFor(textField, textField));
                    ke.consume();
                }
            }
        });

        // Fix for RT-31093 - drag events from the textfield were not surfacing
        // properly for the ComboBox.
        if (textField != null) {
            textField.addEventFilter(MouseEvent.DRAG_DETECTED, new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent event) {
                    if (event.getTarget().equals(comboBox)) return;
                    comboBox.fireEvent(event.copyFor(comboBox, comboBox));
                    event.consume();
                }
            });
            textField.addEventFilter(DragEvent.ANY, new EventHandler<DragEvent>() {
                @Override public void handle(DragEvent event) {
                    if (event.getTarget().equals(comboBox)) return;
                        comboBox.fireEvent(event.copyFor(comboBox, comboBox));
                        event.consume();
                }
            });
        }
        
        // Fix for RT-19431 (also tested via ComboBoxListViewSkinTest)
        updateValue();
        
        registerChangeListener(comboBox.itemsProperty(), "ITEMS");
        registerChangeListener(comboBox.promptTextProperty(), "PROMPT_TEXT");
        registerChangeListener(comboBox.cellFactoryProperty(), "CELL_FACTORY");
        registerChangeListener(comboBox.visibleRowCountProperty(), "VISIBLE_ROW_COUNT");
        registerChangeListener(comboBox.converterProperty(), "CONVERTER");
        registerChangeListener(comboBox.buttonCellProperty(), "BUTTON_CELL");
        registerChangeListener(comboBox.valueProperty(), "VALUE");
    }
    
    
    
    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/  
    
    /** {@inheritDoc} */
    @Override protected void handleControlPropertyChanged(String p) {
        // Fix for RT-21207
        if ("SHOWING".equals(p)) {
            if (getSkinnable().isShowing()) {
                this.listView.setManaged(true);
            } else {
                this.listView.setManaged(false);
            }
        }
        // -- end of fix
        
        super.handleControlPropertyChanged(p);
        
        if ("ITEMS".equals(p)) {
            updateComboBoxItems();
            updateListViewItems();
        } else if ("PROMPT_TEXT".equals(p)) {
            updateDisplayNode();
        } else if ("CELL_FACTORY".equals(p)) {
            updateCellFactory();
        } else if ("VISIBLE_ROW_COUNT".equals(p)) {
            if (listView == null) return;
            listView.setPrefHeight(getListViewPrefHeight());
        } else if ("CONVERTER".equals(p)) {
            updateListViewItems();
        } else if ("EDITOR".equals(p)) {
            getEditableInputNode();
        } else if ("BUTTON_CELL".equals(p)) {
            updateButtonCell();
        } else if ("VALUE".equals(p)) {
            updateValue();
        }
    }
    
    /** {@inheritDoc} */
    @Override public Node getDisplayNode() {
        Node displayNode;
        if (comboBox.isEditable()) {
            displayNode = getEditableInputNode();
        } else {
            displayNode = buttonCell;
        }
        
        updateDisplayNode();
        
        return displayNode;
    }
    
    private void updateComboBoxItems() {
        comboBoxItems = comboBox.getItems();
        comboBoxItems = comboBoxItems == null ? FXCollections.<T>emptyObservableList() : comboBoxItems;
    }
    
    public void updateListViewItems() {
        if (listViewItems != null) {
            listViewItems.removeListener(weakListViewItemsListener);
        }

        this.listViewItems = comboBoxItems;
        listView.setItems(null);
        listView.setItems(listViewItems);

        if (listViewItems != null) {
            listViewItems.addListener(weakListViewItemsListener);
        }
        
        itemCountDirty = true;
        getSkinnable().requestLayout();
    }
    
    @Override public Node getPopupContent() {
        return listView;
    }
    
    @Override protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double superPrefWidth = super.computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
        double listViewWidth = listView.prefWidth(height);
        double pw = Math.max(superPrefWidth, listViewWidth);
        
        reconfigurePopup();
        
        return pw;
    }
    
    @Override protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return 50;
    }
    
    @Override protected void layoutChildren(final double x, final double y,
            final double w, final double h) {
        if (listViewSelectionDirty) {
            try {
                listSelectionLock = true;
                T item = comboBox.getSelectionModel().getSelectedItem();
                listView.getSelectionModel().clearSelection();
                listView.getSelectionModel().select(item);
            } finally {
                listSelectionLock = false;
                listViewSelectionDirty = false;
            }
        }
        
        super.layoutChildren(x,y,w,h);
    }

    // Added to allow subclasses to prevent the popup from hiding when the
    // ListView is clicked on (e.g when the list cells have checkboxes).
    protected boolean isHideOnClickEnabled() {
        return true;
    }

    
    
    /***************************************************************************
     *                                                                         *
     * Private methods                                                         *
     *                                                                         *
     **************************************************************************/    
    
    private void updateValue() {
        T newValue = comboBox.getValue();
        
        SelectionModel<T> listViewSM = listView.getSelectionModel();
        
        if (newValue == null) {
            listViewSM.clearSelection();
        } else {
            // RT-22386: We need to test to see if the value is in the comboBox
            // items list. If it isn't, then we should clear the listview 
            // selection
            int indexOfNewValue = getIndexOfComboBoxValueInItemsList();
            if (indexOfNewValue == -1) {
                listSelectionLock = true;
                listViewSM.clearSelection();
                listSelectionLock = false;
            } else {
                int index = comboBox.getSelectionModel().getSelectedIndex();
                if (index >= 0 && index < comboBoxItems.size()) {
                    T itemsObj = comboBoxItems.get(index);
                    if (itemsObj != null && itemsObj.equals(newValue)) {
                        listViewSM.select(index);
                    } else {
                        listViewSM.select(newValue);
                    }
                } else {
                    // just select the first instance of newValue in the list
                    int listViewIndex = comboBoxItems.indexOf(newValue);
                    if (listViewIndex == -1) {
                        // RT-21336 Show the ComboBox value even though it doesn't
                        // exist in the ComboBox items list (part one of fix)
                        updateDisplayNode();
                    } else {
                        listViewSM.select(listViewIndex);
                    }
                }
            }
        }
    }
    
    private String initialTextFieldValue = null;
    private TextField getEditableInputNode() {
        if (textField != null) return textField;
        
        textField = comboBox.getEditor();
        textField.setFocusTraversable(true);
        textField.promptTextProperty().bind(comboBox.promptTextProperty());
        textField.tooltipProperty().bind(comboBox.tooltipProperty());

        // Fix for RT-21406: ComboBox do not show initial text value
        initialTextFieldValue = textField.getText();
        // End of fix (see updateDisplayNode below for the related code)

        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean hasFocus) {
                if (! comboBox.isEditable()) return;

                // Fix for RT-29885
                comboBox.getProperties().put("FOCUSED", hasFocus);
                // --- end of RT-29885

                // RT-21454 starts here
                if (! hasFocus) {
                    setTextFromTextFieldIntoComboBoxValue();
                    pseudoClassStateChanged(CONTAINS_FOCUS_PSEUDOCLASS_STATE, false);
                } else {
                    pseudoClassStateChanged(CONTAINS_FOCUS_PSEUDOCLASS_STATE, true);
                }
                // --- end of RT-21454
            }
        });

        return textField;
    }
    
    private void updateDisplayNode() {
        StringConverter<T> c = comboBox.getConverter();
        if (c == null) return;
              
        T value = comboBox.getValue();
        if (comboBox.isEditable()) {
            if (initialTextFieldValue != null && ! initialTextFieldValue.isEmpty()) {
                // Remainder of fix for RT-21406: ComboBox do not show initial text value
                textField.setText(initialTextFieldValue);
                initialTextFieldValue = null;
                // end of fix
            } else {
                String stringValue = c.toString(value);
                if (value == null || stringValue == null) {
                    textField.setText("");
                } else if (! stringValue.equals(textField.getText())) {
                    textField.setText(stringValue);
                }
            }
        } else {
            int index = getIndexOfComboBoxValueInItemsList();
            if (index > -1) {
                buttonCell.setItem(null);
                buttonCell.updateIndex(index);
            } else {
                // RT-21336 Show the ComboBox value even though it doesn't
                // exist in the ComboBox items list (part two of fix)
                buttonCell.updateIndex(-1);
                boolean empty = updateDisplayText(buttonCell, value, false);
                
                // Note that empty boolean collected above. This is used to resolve
                // RT-27834, where we were getting different styling based on whether
                // the cell was updated via the updateIndex method above, or just
                // by directly updating the text. We fake the pseudoclass state
                // for empty, filled, and selected here.
                buttonCell.pseudoClassStateChanged(PSEUDO_CLASS_EMPTY,    empty);
                buttonCell.pseudoClassStateChanged(PSEUDO_CLASS_FILLED,   !empty);
                buttonCell.pseudoClassStateChanged(PSEUDO_CLASS_SELECTED, true);
            }
        }
    }
    
    // return a boolean to indicate that the cell is empty (and therefore not filled)
    private boolean updateDisplayText(ListCell<T> cell, T item, boolean empty) {
        if (empty) {
            if (cell == null) return true;
            cell.setGraphic(null);
            cell.setText(null);
            return true;
        } else if (item instanceof Node) {
            Node currentNode = cell.getGraphic();
            Node newNode = (Node) item;
            if (currentNode == null || ! currentNode.equals(newNode)) {
                cell.setText(null);
                cell.setGraphic(newNode);
            }
            return newNode == null;
        } else {
            // run item through StringConverter if it isn't null
            StringConverter<T> c = comboBox.getConverter();
            String s = item == null ? comboBox.getPromptText() : (c == null ? item.toString() : c.toString(item));
            cell.setText(s);
            cell.setGraphic(null);
            return s == null || s.isEmpty();
        }
    }
    
    private void setTextFromTextFieldIntoComboBoxValue() {
        if (! comboBox.isEditable()) return;
        
        StringConverter<T> c = comboBox.getConverter();
        if (c == null) return;
        
        T oldValue = comboBox.getValue();
        String text = textField.getText();
        
        // conditional check here added due to RT-28245
        T value = oldValue == null && (text == null || text.isEmpty()) ? null : c.fromString(textField.getText());
        
        if ((value == null && oldValue == null) || (value != null && value.equals(oldValue))) {
            // no point updating values needlessly (as they are the same)
            return;
        }
        
        comboBox.setValue(value);
    }
    
    private int getIndexOfComboBoxValueInItemsList() {
        T value = comboBox.getValue();
        int index = comboBoxItems.indexOf(value);
        return index;
    }
    
    private void updateButtonCell() {
        buttonCell = comboBox.getButtonCell() != null ? 
                comboBox.getButtonCell() : getDefaultCellFactory().call(listView);
        buttonCell.setMouseTransparent(true);
        buttonCell.updateListView(listView);
    } 

    private void updateCellFactory() {
        Callback<ListView<T>, ListCell<T>> cf = comboBox.getCellFactory();
        cellFactory = cf != null ? cf : getDefaultCellFactory();
        listView.setCellFactory(cellFactory);
    }
    
    private Callback<ListView<T>, ListCell<T>> getDefaultCellFactory() {
        return new Callback<ListView<T>, ListCell<T>>() {
            @Override public ListCell<T> call(ListView<T> listView) {
                return new ListCell<T>() {
                    @Override public void updateItem(T item, boolean empty) {
                        super.updateItem(item, empty);
                        updateDisplayText(this, item, empty);
                    }
                };
            }
        };
    }
    
    private ListView<T> createListView() {
        final ListView<T> _listView = new ListView<T>() {
            @Override protected double computeMinHeight(double width) {
                return 30;
            }
            
            @Override protected double computePrefWidth(double height) {
                double pw;
                if (getSkin() instanceof ListViewSkin) {
                    ListViewSkin<?> skin = (ListViewSkin<?>)getSkin();
                    if (itemCountDirty) {
                        skin.updateRowCount();
                        itemCountDirty = false;
                    }
                    
                    int rowsToMeasure = -1;
                    if (comboBox.getProperties().containsKey(COMBO_BOX_ROWS_TO_MEASURE_WIDTH_KEY)) {
                        rowsToMeasure = (Integer) comboBox.getProperties().get(COMBO_BOX_ROWS_TO_MEASURE_WIDTH_KEY);
                    }
                    
                    pw = Math.max(comboBox.getWidth(), skin.getMaxCellWidth(rowsToMeasure) + 30);
                } else {
                    pw = Math.max(100, comboBox.getWidth());
                }

                // need to check the ListView pref height in the case that the
                // placeholder node is showing
                if (getItems().isEmpty() && getPlaceholder() != null) {
                    pw = Math.max(super.computePrefWidth(height), pw);
                }

                return Math.max(50, pw);
            }

            @Override protected double computePrefHeight(double width) {
                return getListViewPrefHeight();
            }
        };

        _listView.setId("list-view");
        _listView.placeholderProperty().bind(comboBox.placeholderProperty());
        _listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        _listView.getSelectionModel().selectedIndexProperty().addListener(new InvalidationListener() {
             @Override public void invalidated(Observable o) {
                 if (listSelectionLock) return;
                 int index = listView.getSelectionModel().getSelectedIndex();
                 comboBox.getSelectionModel().select(index);
                 updateDisplayNode();
             }
         });
         
        comboBox.getSelectionModel().selectedItemProperty().addListener(new InvalidationListener() {
            @Override public void invalidated(Observable o) {
                listViewSelectionDirty = true;
            }
        });
        
        _listView.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent t) {
                // RT-18672: Without checking if the user is clicking in the 
                // scrollbar area of the ListView, the comboBox will hide. Therefore,
                // we add the check below to prevent this from happening.
                EventTarget target = t.getTarget();
                if (target instanceof Parent) {
                    List<String> s = ((Parent) target).getStyleClass();
                    if (s.contains("thumb") 
                            || s.contains("track") 
                            || s.contains("decrement-arrow") 
                            || s.contains("increment-arrow")) {
                        return;
                    }
                }
                
                if (isHideOnClickEnabled()) {
                    comboBox.hide();
                }
            }
        });

        _listView.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override public void handle(KeyEvent t) {
                // TODO move to behavior, when (or if) this class becomes a SkinBase
                if (t.getCode() == KeyCode.ENTER || 
                        t.getCode() == KeyCode.SPACE || 
                        t.getCode() == KeyCode.ESCAPE) {
                    comboBox.hide();
                }
            }
        });
        
        return _listView;
    }
    
    private double getListViewPrefHeight() {
        double ph;
        if (listView.getSkin() instanceof VirtualContainerBase) {
            int maxRows = comboBox.getVisibleRowCount();
            VirtualContainerBase<?,?,?> skin = (VirtualContainerBase<?,?,?>)listView.getSkin();
            ph = skin.getVirtualFlowPreferredHeight(maxRows);
        } else {
            double ch = comboBoxItems.size() * 25;
            ph = Math.min(ch, 200);
        }
        
        return ph;
    }


    
    /**************************************************************************
     * 
     * API for testing
     * 
     *************************************************************************/
    
    public ListView<T> getListView() {
        return listView;
    }
    
    
    

    /***************************************************************************
     *                                                                         *
     * Stylesheet Handling                                                     *
     *                                                                         *
     **************************************************************************/
    
    private static PseudoClass CONTAINS_FOCUS_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("contains-focus");    
    
    // These three pseudo class states are duplicated from Cell
    private static final PseudoClass PSEUDO_CLASS_SELECTED =
            PseudoClass.getPseudoClass("selected");
    private static final PseudoClass PSEUDO_CLASS_EMPTY =
            PseudoClass.getPseudoClass("empty");
    private static final PseudoClass PSEUDO_CLASS_FILLED =
            PseudoClass.getPseudoClass("filled");



    /***************************************************************************
     *                                                                         *
     * Support classes                                                         *
     *                                                                         *
     **************************************************************************/

    public static final class FakeFocusTextField extends TextField {

        public void setFakeFocus(boolean b) {
            setFocused(b);
        }
    }
}

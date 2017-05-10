/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
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

package com.sun.javafx.scene.control.behavior;

import static javafx.scene.input.KeyCode.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableFocusModel;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTablePosition;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;

public class TreeTableViewBehavior<T> extends TableViewBehaviorBase<TreeTableView<T>, TreeItem<T>, TreeTableColumn<T, ?>> {
    
    /**************************************************************************
     *                                                                        *
     * Setup key bindings                                                     *
     *                                                                        *  
     *************************************************************************/
    
    static {
        // Add these bindings at the front of the list, so they take precedence
        TABLE_VIEW_BINDINGS.add(0,new KeyBinding(LEFT, "CollapseRow"));
        TABLE_VIEW_BINDINGS.add(0,new KeyBinding(KP_LEFT, "CollapseRow"));
        TABLE_VIEW_BINDINGS.add(0,new KeyBinding(RIGHT, "ExpandRow"));
        TABLE_VIEW_BINDINGS.add(0,new KeyBinding(KP_RIGHT, "ExpandRow"));
        
        TABLE_VIEW_BINDINGS.add(0,new KeyBinding(MULTIPLY, "ExpandAll"));
        TABLE_VIEW_BINDINGS.add(0,new KeyBinding(ADD, "ExpandRow"));
        TABLE_VIEW_BINDINGS.add(0,new KeyBinding(SUBTRACT, "CollapseRow"));
    }

    @Override protected void callAction(String name) {
        if ("ExpandRow".equals(name)) rightArrowPressed();
        else if ("CollapseRow".equals(name)) leftArrowPressed();
        else if ("ExpandAll".equals(name)) expandAll();
        else super.callAction(name);
    }
    

    
    /**************************************************************************
     *                                                                        *
     * Listeners                                                              *
     *                                                                        *  
     *************************************************************************/
    
    private final ChangeListener<TreeTableView.TreeTableViewSelectionModel<T>> selectionModelListener = 
            new ChangeListener<TreeTableView.TreeTableViewSelectionModel<T>>() {
        @Override
        public void changed(ObservableValue<? extends TreeTableView.TreeTableViewSelectionModel<T>> observable, 
                    TreeTableView.TreeTableViewSelectionModel<T> oldValue, 
                    TreeTableView.TreeTableViewSelectionModel<T> newValue) {
            if (oldValue != null) {
                oldValue.getSelectedCells().removeListener(weakSelectedCellsListener);
            }
            if (newValue != null) {
                newValue.getSelectedCells().addListener(weakSelectedCellsListener);
            }
        }
    };
    
    private final WeakChangeListener<TreeTableView.TreeTableViewSelectionModel<T>> weakSelectionModelListener = 
            new WeakChangeListener<TreeTableView.TreeTableViewSelectionModel<T>>(selectionModelListener);
    
    
    
    /**************************************************************************
     *                                                                        *
     * Constructors                                                           *
     *                                                                        *  
     *************************************************************************/
    
    public TreeTableViewBehavior(TreeTableView<T> control) {
        super(control);
        
        // Fix for RT-16565
        control.selectionModelProperty().addListener(weakSelectionModelListener);
        if (getSelectionModel() != null) {
            control.getSelectionModel().getSelectedCells().addListener(selectedCellsListener);
        }
    }

    
    
    /**************************************************************************
     *                                                                        *
     * Implement TableViewBehaviorBase abstract methods                       *
     *                                                                        *  
     *************************************************************************/
    
    /** {@inheritDoc}  */
    @Override protected int getItemCount() {
        return getControl().getExpandedItemCount();
    }

    /** {@inheritDoc}  */
    @Override protected TableFocusModel getFocusModel() {
        return getControl().getFocusModel();
    }

    /** {@inheritDoc}  */
    @Override protected TableSelectionModel<TreeItem<T>> getSelectionModel() {
        return getControl().getSelectionModel();
    }

    /** {@inheritDoc}  */
    @Override protected ObservableList<TreeTablePosition<T,?>> getSelectedCells() {
        return getControl().getSelectionModel().getSelectedCells();
    }

    /** {@inheritDoc}  */
    @Override protected TablePositionBase getFocusedCell() {
        return getControl().getFocusModel().getFocusedCell();
    }

    /** {@inheritDoc}  */
    @Override protected int getVisibleLeafIndex(TableColumnBase tc) {
        return getControl().getVisibleLeafIndex((TreeTableColumn)tc);
    }

    /** {@inheritDoc}  */
    @Override protected TreeTableColumn getVisibleLeafColumn(int index) {
        return getControl().getVisibleLeafColumn(index);
    }

    /** {@inheritDoc}  */
    @Override protected void editCell(int row, TableColumnBase tc) {
        getControl().edit(row, (TreeTableColumn)tc);
    }

    /** {@inheritDoc}  */
    @Override protected ObservableList<TreeTableColumn<T,?>> getVisibleLeafColumns() {
        return getControl().getVisibleLeafColumns();
    }

    /** {@inheritDoc}  */
    @Override protected TablePositionBase<TreeTableColumn<T, ?>> 
            getTablePosition(int row, TableColumnBase<TreeItem<T>, ?> tc) {
        return new TreeTablePosition(getControl(), row, (TreeTableColumn)tc);
    }



    /**************************************************************************
     *                                                                        *
     * Modify TableViewBehaviorBase behavior                                  *
     *                                                                        *
     *************************************************************************/

    /** {@inheritDoc} */
    @Override protected void selectAllToFocus(boolean setAnchorToFocusIndex) {
        // Fix for RT-31241
        if (getControl().getEditingCell() != null) return;

        super.selectAllToFocus(setAnchorToFocusIndex);
    }

    /**************************************************************************
     *                                                                        *
     * Tree-related implementation                                            *
     *                                                                        *  
     *************************************************************************/
    
    /**
     * The next methods handle the left/right arrow input differently depending
     * on whether we are in row or cell selection.
     */
    private void rightArrowPressed() {
        if (getControl().getSelectionModel().isCellSelectionEnabled()) {
            selectRightCell();
        } else {
            expandRow();
        }
    }
    
    private void leftArrowPressed() {
        if (getControl().getSelectionModel().isCellSelectionEnabled()) {
            selectLeftCell();
        } else {
            collapseRow();
        }
    }
    
    private void expandRow() {
        Callback<TreeItem<T>, Integer> getIndex = new Callback<TreeItem<T>, Integer>() {
            @Override public Integer call(TreeItem<T> p) {
                return getControl().getRow(p);
            }
        };
        TreeViewBehavior.expandRow(getControl().getSelectionModel(), getIndex);
    }
    
    private void expandAll() {
        TreeViewBehavior.expandAll(getControl().getRoot());
    }
    
    private void collapseRow() {
        TreeTableView<T> control = getControl();
        TreeViewBehavior.collapseRow(control.getSelectionModel(), control.getRoot(), control.isShowRoot());
    }
}

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

package com.sun.javafx.scene.control.skin;


import java.util.*;

import com.sun.javafx.*;
import javafx.animation.FadeTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.TableColumnBase;
import javafx.util.Duration;

import com.sun.javafx.scene.control.behavior.CellBehaviorBase;
import com.sun.javafx.tk.Toolkit;

public abstract class TableRowSkinBase<T,
                                       C extends IndexedCell/*<T>*/,
                                       B extends CellBehaviorBase<C>,
                                       R extends IndexedCell> extends CellSkinBase<C,B> {

    /***************************************************************************
     *                                                                         *
     * Static Fields                                                           *
     *                                                                         *
     **************************************************************************/

    // There appears to be a memory leak when using the stub toolkit. Therefore,
    // to prevent tests from failing we disable the animations below when the
    // stub toolkit is being used.
    // Filed as RT-29163.
    private static boolean IS_STUB_TOOLKIT = Toolkit.getToolkit().toString().contains("StubToolkit");

    // lets save the CPU and not do animations when on embedded platforms
    private static boolean DO_ANIMATIONS = ! IS_STUB_TOOLKIT && ! PlatformUtil.isEmbedded();

    private static final Duration FADE_DURATION = Duration.millis(200);

    /*
     * This is rather hacky - but it is a quick workaround to resolve the
     * issue that we don't know maximum width of a disclosure node for a given
     * TreeView. If we don't know the maximum width, we have no way to ensure
     * consistent indentation for a given TreeView.
     *
     * To work around this, we create a single WeakHashMap to store a max
     * disclosureNode width per TreeView. We use WeakHashMap to help prevent
     * any memory leaks.
     */
    static final Map<Control, Double> maxDisclosureWidthMap = new WeakHashMap<Control, Double>();

    // Specifies the number of times we will call 'recreateCells()' before we blow
    // out the cellsMap structure and rebuild all cells. This helps to prevent
    // against memory leaks in certain extreme circumstances.
    private static final int DEFAULT_FULL_REFRESH_COUNTER = 100;



    /***************************************************************************
     *                                                                         *
     * Private Fields                                                          *
     *                                                                         *
     **************************************************************************/

    /*
     * A map that maps from TableColumn to TableCell (i.e. model to view).
     * This is recreated whenever the leaf columns change, however to increase
     * efficiency we create cells for all columns, even if they aren't visible,
     * and we only create new cells if we don't already have it cached in this
     * map.
     *
     * Note that this means that it is possible for this map to therefore be
     * a memory leak if an application uses TableView and is creating and removing
     * a large number of tableColumns. This is mitigated in the recreateCells()
     * function below - refer to that to learn more.
     */
    protected WeakHashMap<TableColumnBase, R> cellsMap;

    // This observableArrayList contains the currently visible table cells for this row.
    protected final List<R> cells = new ArrayList<R>();

    private int fullRefreshCounter = DEFAULT_FULL_REFRESH_COUNTER;

    protected boolean isDirty = false;
    protected boolean updateCells = false;

    private double fixedCellSize;
    private boolean fixedCellSizeEnabled;

    private int columnCount = 0;



    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    public TableRowSkinBase(C control, B behavior) {
        super(control, behavior);

        // init(control) should not be called here - it should be called by the
        // subclass after initialising itself. This is to prevent NPEs (for
        // example, getVisibleLeafColumns() throws a NPE as the control itself
        // is not yet set in subclasses).
    }

    // init isn't a constructor, but it is part of the initialisation routine
    protected void init(C control) {
        getSkinnable().setPickOnBounds(false);

        recreateCells();
        updateCells(true);

        // init bindings
        // watches for any change in the leaf columns observableArrayList - this will indicate
        // that the column order has changed and that we should update the row
        // such that the cells are in the new order
        getVisibleLeafColumns().addListener(weakVisibleLeafColumnsListener);
        // --- end init bindings

        registerChangeListener(control.itemProperty(), "ITEM");

        if (fixedCellSizeProperty() != null) {
            registerChangeListener(fixedCellSizeProperty(), "FIXED_CELL_SIZE");
            fixedCellSize = fixedCellSizeProperty().get();
            fixedCellSizeEnabled = fixedCellSize > 0;
        }
    }



    /***************************************************************************
     *                                                                         *
     * Listeners                                                               *
     *                                                                         *
     **************************************************************************/

    private ListChangeListener<TableColumnBase> visibleLeafColumnsListener = new ListChangeListener<TableColumnBase>() {
        @Override public void onChanged(Change<? extends TableColumnBase> c) {
            isDirty = true;
            getSkinnable().requestLayout();
        }
    };

    private WeakListChangeListener<TableColumnBase> weakVisibleLeafColumnsListener =
            new WeakListChangeListener<TableColumnBase>(visibleLeafColumnsListener);



    /***************************************************************************
     *                                                                         *
     * Abstract Methods                                                        *
     *                                                                         *
     **************************************************************************/

    /**
     * Returns the graphic to draw on the inside of the disclosure node. Null
     * is acceptable when no graphic should be shown. Commonly this is the
     * graphic associated with a TreeItem (i.e. treeItem.getGraphic()), rather
     * than a graphic associated with a cell.
     */
    protected abstract ObjectProperty<Node> graphicProperty();

    // return TableView / TreeTableView / etc
    protected abstract Control getVirtualFlowOwner();

    protected abstract ObservableList<? extends TableColumnBase/*<T,?>*/> getVisibleLeafColumns();

    // cell.updateTableRow(skinnable); (i.e cell.updateTableRow(row))
    protected abstract void updateCell(R cell, C row);

    protected abstract DoubleProperty fixedCellSizeProperty();

    protected abstract boolean isColumnPartiallyOrFullyVisible(TableColumnBase tc);

    protected abstract R getCell(TableColumnBase tc);

    protected abstract TableColumnBase<T,?> getTableColumnBase(R cell);



    /***************************************************************************
     *                                                                         *
     * Public Methods                                                          *
     *                                                                         *
     **************************************************************************/

    @Override protected void handleControlPropertyChanged(String p) {
        super.handleControlPropertyChanged(p);

        if ("ITEM".equals(p)) {
            updateCells = true;
            getSkinnable().requestLayout();

            // update the index of all children cells (RT-29849).
            // Note that we do this after the TableRow item has been updated,
            // rather than when the TableRow index has changed (as this will be
            // before the row has updated its item). This will result in the
            // issue highlighted in RT-33602, where the table cell had the correct
            // item whilst the row had the old item.
            final int newIndex = getSkinnable().getIndex();
            for (int i = 0, max = cells.size(); i < max; i++) {
                cells.get(i).updateIndex(newIndex);
            }
        } else if ("FIXED_CELL_SIZE".equals(p)) {
            fixedCellSize = fixedCellSizeProperty().get();
            fixedCellSizeEnabled = fixedCellSize > 0;
        }
    }

    @Override protected void layoutChildren(double x, final double y, final double w, final double h) {
        checkState(true);
        if (cellsMap.isEmpty()) return;

        ObservableList<? extends TableColumnBase> visibleLeafColumns = getVisibleLeafColumns();
        if (visibleLeafColumns.isEmpty()) {
            super.layoutChildren(x,y,w,h);
            return;
        }

        C control = getSkinnable();

        ///////////////////////////////////////////
        // indentation code starts here
        ///////////////////////////////////////////
        double leftMargin = 0;
        double disclosureWidth = 0;
        double graphicWidth = 0;
        boolean indentationRequired = isIndentationRequired();
        boolean disclosureVisible = isDisclosureNodeVisible();
        int indentationColumnIndex = 0;
        Node disclosureNode = null;
        if (indentationRequired) {
            // Determine the column in which we want to put the disclosure node.
            // By default it is null, which means the 0th column should be
            // where the indentation occurs.
            TableColumnBase<?,?> treeColumn = getTreeColumn();
            indentationColumnIndex = treeColumn == null ? 0 : visibleLeafColumns.indexOf(treeColumn);
            indentationColumnIndex = indentationColumnIndex < 0 ? 0 : indentationColumnIndex;

            int indentationLevel = getIndentationLevel(control);
            if (! isShowRoot()) indentationLevel--;
            final double indentationPerLevel = getIndentationPerLevel();
            leftMargin = indentationLevel * indentationPerLevel;

            // position the disclosure node so that it is at the proper indent
            Control c = getVirtualFlowOwner();
            final double defaultDisclosureWidth = maxDisclosureWidthMap.containsKey(c) ?
                maxDisclosureWidthMap.get(c) : 0;
            disclosureWidth = defaultDisclosureWidth;

            disclosureNode = getDisclosureNode();
            if (disclosureNode != null) {
                disclosureNode.setVisible(disclosureVisible);

                if (disclosureVisible) {
                    disclosureWidth = disclosureNode.prefWidth(h);
                    if (disclosureWidth > defaultDisclosureWidth) {
                        maxDisclosureWidthMap.put(c, disclosureWidth);
                    }
                }
            }
        }
        ///////////////////////////////////////////
        // indentation code ends here
        ///////////////////////////////////////////

        // layout the individual column cells
        double width;
        double height;

        final double verticalPadding = snappedTopInset() + snappedBottomInset();
        final double horizontalPadding = snappedLeftInset() + snappedRightInset();
        final double controlHeight = control.getHeight();

        /**
         * RT-26743:TreeTableView: Vertical Line looks unfinished.
         * We used to not do layout on cells whose row exceeded the number
         * of items, but now we do so as to ensure we get vertical lines
         * where expected in cases where the vertical height exceeds the
         * number of items.
         */
        int index = control.getIndex();
        if (index < 0/* || row >= itemsProperty().get().size()*/) return;

        for (int column = 0, max = cells.size(); column < max; column++) {
            R tableCell = cells.get(column);
            TableColumnBase<T, ?> tableColumn = getTableColumnBase(tableCell);

            width = snapSize(tableCell.prefWidth(-1)) - snapSize(horizontalPadding);

            boolean isVisible = true;
            if (fixedCellSizeEnabled) {
                // we determine if the cell is visible, and if not we have the
                // ability to take it out of the scenegraph to help improve
                // performance. However, we only do this when there is a
                // fixed cell length specified in the TableView. This is because
                // when we have a fixed cell length it is possible to know with
                // certainty the height of each TableCell - it is the fixed value
                // provided by the developer, and this means that we do not have
                // to concern ourselves with the possibility that the height
                // may be variable and / or dynamic.
                isVisible = isColumnPartiallyOrFullyVisible(tableColumn);

                height = fixedCellSize;
            } else {
                height = Math.max(controlHeight, tableCell.prefHeight(-1));
                height = snapSize(height) - snapSize(verticalPadding);
            }

            if (isVisible) {
                if (fixedCellSizeEnabled && tableCell.getParent() == null) {
                    getChildren().add(tableCell);
                }

                // Added for RT-32700, and then updated for RT-34074.
                // We change the alignment from CENTER_LEFT to TOP_LEFT if the
                // height of the row is greater than the default size, and if
                // the alignment is the default alignment.
                // What I would rather do is only change the alignment if the
                // alignment has not been manually changed, but for now this will
                // do.
                final boolean centreContent = h <= 24.0;
                if (! centreContent && tableCell.getAlignment() == Pos.CENTER_LEFT) {
                    tableCell.setAlignment(Pos.TOP_LEFT);
                }

                ///////////////////////////////////////////
                // further indentation code starts here
                ///////////////////////////////////////////
                if (indentationRequired && column == indentationColumnIndex) {
                    if (disclosureVisible) {
                        double ph = disclosureNode.prefHeight(disclosureWidth);

                        if (width < (disclosureWidth + leftMargin)) {
                            fadeOut(disclosureNode);
                        } else {
                            fadeIn(disclosureNode);
                            disclosureNode.resize(disclosureWidth, ph);

                            disclosureNode.relocate(x + leftMargin,
                                    centreContent ? (h / 2.0 - ph / 2.0) :
                                            (y + tableCell.getPadding().getTop()));
                            disclosureNode.toFront();
                        }
                    }

                    // determine starting point of the graphic or cell node, and the
                    // remaining width available to them
                    ObjectProperty<Node> graphicProperty = graphicProperty();
                    Node graphic = graphicProperty == null ? null : graphicProperty.get();

                    if (graphic != null) {
                        graphicWidth = graphic.prefWidth(-1) + 3;
                        double ph = graphic.prefHeight(graphicWidth);

                        if (width < disclosureWidth + leftMargin + graphicWidth) {
                            fadeOut(graphic);
                        } else {
                            fadeIn(graphic);

                            graphic.relocate(x + leftMargin + disclosureWidth,
                                    centreContent ? (h / 2.0 - ph / 2.0) :
                                            (y + tableCell.getPadding().getTop()));

                            graphic.toFront();
                        }
                    }
                }
                ///////////////////////////////////////////
                // further indentation code ends here
                ///////////////////////////////////////////

                tableCell.resize(width, height);
                tableCell.relocate(x, snappedTopInset());

                // Request layout is here as (partial) fix for RT-28684.
                // This does not appear to impact performance...
                tableCell.requestLayout();
            } else {
                if (fixedCellSizeEnabled) {
                    // we only add/remove to the scenegraph if the fixed cell
                    // length support is enabled - otherwise we keep all
                    // TableCells in the scenegraph
                    getChildren().remove(tableCell);
                }
            }

            x += width;
        }
    }

    protected int getIndentationLevel(C control) {
        return 0;
    }

    protected double getIndentationPerLevel() {
        return 0;
    }

    /**
     * Used to represent whether the current virtual flow owner is wanting
     * indentation to be used in this table row.
     */
    protected boolean isIndentationRequired() {
        return false;
    }

    /**
     * Returns the table column that should show the disclosure nodes and / or
     * a graphic. By default this is the left-most column.
     */
    protected TableColumnBase getTreeColumn() {
        return null;
    }

    protected Node getDisclosureNode() {
        return null;
    }

    /**
     * Used to represent whether a disclosure node is visible for _this_
     * table row. Not to be confused with isIndentationRequired(), which is the
     * more general API.
     */
    protected boolean isDisclosureNodeVisible() {
        return false;
    }

    protected boolean isShowRoot() {
        return true;
    }

    protected TableColumnBase<T,?> getVisibleLeafColumn(int column) {
        final List<? extends TableColumnBase/*<T,?>*/> visibleLeafColumns = getVisibleLeafColumns();
        if (column < 0 || column >= visibleLeafColumns.size()) return null;
        return visibleLeafColumns.get(column);
    }

    protected void updateCells(boolean resetChildren) {
        // if clear isn't called first, we can run into situations where the
        // cells aren't updated properly.
        final boolean cellsEmpty = cells.isEmpty();
        cells.clear();

        final C skinnable = getSkinnable();
        final int skinnableIndex = skinnable.getIndex();
        final List<? extends TableColumnBase/*<T,?>*/> visibleLeafColumns = getVisibleLeafColumns();

        for (int i = 0, max = visibleLeafColumns.size(); i < max; i++) {
            TableColumnBase<T,?> col = visibleLeafColumns.get(i);

            R cell = cellsMap.get(col);
            if (cell == null) {
                // if the cell is null it means we don't have it in cache and
                // need to create it
                cell = createCell(col);
            }

            updateCell(cell, skinnable);
            cell.updateIndex(skinnableIndex);
            cells.add(cell);
            if (resetChildren) {
                // RT-31084:When resetting children, we are already in the layout pass (with expection of one call during the init)
                // Since we are manipulating with cells here and cannot wait for the next pulse to process CSS for the new situation,
                // the CSS must be processed here
                cell.impl_processCSS(false);
            }
        }

        // update children of each row
        if (!fixedCellSizeEnabled && (resetChildren || cellsEmpty)) {
            getChildren().setAll(cells);
        }
    }

    @Override protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double prefWidth = 0.0;

        final List<? extends TableColumnBase/*<T,?>*/> visibleLeafColumns = getVisibleLeafColumns();
        for (int i = 0, max = visibleLeafColumns.size(); i < max; i++) {
            prefWidth += visibleLeafColumns.get(i).getWidth();
        }

        return prefWidth;
    }

    @Override protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (fixedCellSizeEnabled) {
            return fixedCellSize;
        }

        // fix for RT-29080
        checkState(false);

        // Support for RT-18467: making it easier to specify a height for
        // cells via CSS, where the desired height is less than the height
        // of the TableCells. Essentially, -fx-cell-size is given higher
        // precedence now
        if (getCellSize() < CellSkinBase.DEFAULT_CELL_SIZE) {
            return getCellSize();
        }

        // FIXME according to profiling, this method is slow and should
        // be optimised
        double prefHeight = 0.0f;
        final int count = cells.size();
        for (int i=0; i<count; i++) {
            final R tableCell = cells.get(i);
            prefHeight = Math.max(prefHeight, tableCell.prefHeight(-1));
        }
        double ph = Math.max(prefHeight, Math.max(getCellSize(), getSkinnable().minHeight(-1)));

        return ph;
    }

    @Override protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (fixedCellSizeEnabled) {
            return fixedCellSize;
        }

        // fix for RT-29080
        checkState(false);

        // Support for RT-18467: making it easier to specify a height for
        // cells via CSS, where the desired height is less than the height
        // of the TableCells. Essentially, -fx-cell-size is given higher
        // precedence now
        if (getCellSize() < CellSkinBase.DEFAULT_CELL_SIZE) {
            return getCellSize();
        }

        // FIXME according to profiling, this method is slow and should
        // be optimised
        double minHeight = 0.0f;
        final int count = cells.size();
        for (int i = 0; i < count; i++) {
            final R tableCell = cells.get(i);
            minHeight = Math.max(minHeight, tableCell.minHeight(-1));
        }
        return minHeight;
    }

    @Override protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (fixedCellSizeEnabled) {
            return fixedCellSize;
        }
        return super.computeMaxHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    // protected to allow subclasses to ensure a consistent state during layout
    protected final void checkState(boolean doRecreateIfNecessary) {
        if (isDirty) {
            // doRecreateIfNecessary was added to resolve RT-29382, which was
            // introduced by the fix for RT-29080 above in computePrefHeight
            if (doRecreateIfNecessary) {
                recreateCells();
            }
            updateCells(true);
            isDirty = false;
        } else if (updateCells) {
            updateCells(false);
            updateCells = false;
        }
    }



    /***************************************************************************
     *                                                                         *
     * Private Implementation                                                  *
     *                                                                         *
     **************************************************************************/

    private void recreateCells() {
        // This function is smart in the sense that we don't recreate all
        // TableCell instances every time this function is called. Instead we
        // only create TableCells for TableColumns we haven't already encountered.
        // To avoid a potential memory leak (when the TableColumns in the
        // TableView are created/inserted/removed/deleted, we have a 'refresh
        // counter' that when we reach 0 will delete all cells in this row
        // and recreate all of them.

        if (cellsMap != null) {
            Collection<R> cells = cellsMap.values();
            Iterator<R> cellsIter = cells.iterator();
            while (cellsIter.hasNext()) {
                R cell = cellsIter.next();
                cell.updateIndex(-1);
                cell.getSkin().dispose();
            }
            cellsMap.clear();
        }

        ObservableList<? extends TableColumnBase/*<T,?>*/> columns = getVisibleLeafColumns();

        if (columns.size() != columnCount || fullRefreshCounter == 0 || cellsMap == null) {
            cellsMap = new WeakHashMap<TableColumnBase, R>(columns.size());
            fullRefreshCounter = DEFAULT_FULL_REFRESH_COUNTER;
            getChildren().clear();
        }
        columnCount = columns.size();
        fullRefreshCounter--;

        for (TableColumnBase col : columns) {
            if (cellsMap.containsKey(col)) {
                continue;
            }

            // create a TableCell for this column and store it in the cellsMap
            // for future use
            createCell(col);
        }
    }

    private R createCell(TableColumnBase col) {
        // we must create a TableCell for this table column
        R cell = getCell(col);

        // and store this in our HashMap until needed
        cellsMap.put(col, cell);

        return cell;
    }

    private void fadeOut(final Node node) {
        if (node.getOpacity() < 1.0) return;

        if (! DO_ANIMATIONS) {
            node.setOpacity(0);
            return;
        }

        final FadeTransition fader = new FadeTransition(FADE_DURATION, node);
        fader.setToValue(0.0);
        fader.play();
    }

    private void fadeIn(final Node node) {
        if (node.getOpacity() > 0.0) return;

        if (! DO_ANIMATIONS) {
            node.setOpacity(1);
            return;
        }

        final FadeTransition fader = new FadeTransition(FADE_DURATION, node);
        fader.setToValue(1.0);
        fader.play();
    }
}

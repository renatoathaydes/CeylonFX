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

package javafx.scene.layout;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;

public class GridPaneDesignInfo extends PaneDesignInfo {
    public GridPaneDesignInfo() {
        super(GridPane.class);
    }

    protected GridPaneDesignInfo(Class type) {
        super(type);
    }

    public int getRowCount(GridPane pane) {
        int numRows = pane.getRowConstraints().size();
        for (int i = 0; i < pane.getChildren().size(); i++) {
            Node child = pane.getChildren().get(i);
            if (child.isManaged()) {
                int rowIndex = GridPane.getNodeRowIndex(child);
                int rowEnd = GridPane.getNodeRowEnd(child);
                numRows = Math.max(numRows, (rowEnd != GridPane.REMAINING? rowEnd : rowIndex) + 1);
            }
        }
        return numRows;
    }

    public int getColumnCount(GridPane pane) {
        int numColumns = pane.getColumnConstraints().size();
        for (int i = 0; i < pane.getChildren().size(); i++) {
            Node child = pane.getChildren().get(i);
            if (child.isManaged()) {
                int columnIndex = GridPane.getNodeColumnIndex(child);
                int columnEnd = GridPane.getNodeColumnEnd(child);
                numColumns = Math.max(numColumns, (columnEnd != GridPane.REMAINING? columnEnd : columnIndex) + 1);
            }
        }
        return numColumns;
    }

    public Bounds getCellBounds(GridPane pane, int columnIndex, int rowIndex) {
        final double snaphgap = pane.snapSpace(pane.getHgap());
        final double snapvgap = pane.snapSpace(pane.getVgap());
        final double top = pane.snapSpace(pane.getInsets().getTop());
        final double right = pane.snapSpace(pane.getInsets().getRight());
        final double bottom = pane.snapSpace(pane.getInsets().getBottom());
        final double left = pane.snapSpace(pane.getInsets().getLeft());
        final double gridPaneHeight = pane.snapSize(pane.getHeight()) - (top + bottom);
        final double gridPaneWidth = pane.snapSize(pane.getWidth()) - (left + right);

        // Compute grid. Result contains two double arrays, first for columns, second for rows
        double[] columnWidths;
        double[] rowHeights;

        double[][] grid = pane.getGrid();
        if (grid == null) {
            rowHeights = new double[] {0};
            rowIndex = 0;
            columnWidths = new double[] {0};
            columnIndex = 0;
        } else {
            columnWidths = grid[0];
            rowHeights = grid[1];
        }

        // Compute the total row height
        double rowTotal = 0;
        for (int i = 0; i < rowHeights.length; i++) {
            rowTotal += rowHeights[i];
        }
        rowTotal += ((rowHeights.length - 1) * snapvgap);

        // Adjust for alignment
        double minY = top + Region.computeYOffset(gridPaneHeight, rowTotal, pane.getAlignment().getVpos());
        double height = rowHeights[rowIndex];
        for (int j = 0; j < rowIndex; j++) {
            minY += rowHeights[j] + snapvgap;
        }

        // Compute the total column width
        double columnTotal = 0;
        for (int i = 0; i < columnWidths.length; i++) {
            columnTotal += columnWidths[i];
        }
        columnTotal += ((columnWidths.length - 1) * snaphgap);

        // Adjust for alignment
        double minX = left + Region.computeXOffset(gridPaneWidth, columnTotal, pane.getAlignment().getHpos());
        double width = columnWidths[columnIndex];
        for (int j = 0; j < columnIndex; j++) {
            minX += columnWidths[j] + snaphgap;
        }

        return new BoundingBox(minX, minY, width, height);
    }
}

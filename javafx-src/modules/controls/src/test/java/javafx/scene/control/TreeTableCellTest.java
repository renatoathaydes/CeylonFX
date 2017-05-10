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

package javafx.scene.control;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.util.Callback;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static com.sun.javafx.scene.control.infrastructure.ControlTestUtils.*;
import static org.junit.Assert.*;

public class TreeTableCellTest {
    private TreeTableCell<String, String> cell;
    private TreeTableView<String> tree;
    private TreeTableRow<String> row;

    private static final String ROOT = "Root";
    private static final String APPLES = "Apples";
    private static final String ORANGES = "Oranges";
    private static final String PEARS = "Pears";

    private TreeItem<String> root;
    private TreeItem<String> apples;
    private TreeItem<String> oranges;
    private TreeItem<String> pears;

    @Before public void setup() {
        cell = new TreeTableCell<String, String>();

        root = new TreeItem<>(ROOT);
        apples = new TreeItem<>(APPLES);
        oranges = new TreeItem<>(ORANGES);
        pears = new TreeItem<>(PEARS);
        root.getChildren().addAll(apples, oranges, pears);

        tree = new TreeTableView<String>(root);
        root.setExpanded(true);

        row = new TreeTableRow<>();
    }

    /*********************************************************************
     * Tests for the constructors                                        *
     ********************************************************************/

    @Test public void styleClassIs_tree_cell_byDefault() {
        assertStyleClassContains(cell, "tree-table-cell");
    }

    // The item should be null by default because the index is -1 by default
    @Test public void itemIsNullByDefault() {
        assertNull(cell.getItem());
    }

    /*********************************************************************
     * Tests for the item property. It should be updated whenever the    *
     * index, or treeView changes, including the treeView's items.       *
     ********************************************************************/

    @Ignore // TODO file bug!
    @Test public void itemMatchesIndexWithinTreeItems() {
        cell.updateIndex(0);
        cell.updateTreeTableView(tree);
        assertSame(ROOT, cell.getItem());
        assertSame(root, cell.getTreeTableRow().getTreeItem());
        cell.updateIndex(1);
        assertSame(APPLES, cell.getItem());
        assertSame(apples, cell.getTreeTableRow().getTreeItem());
    }

    @Ignore // TODO file bug!
    @Test public void itemMatchesIndexWithinTreeItems2() {
        cell.updateTreeTableView(tree);
        cell.updateIndex(0);
        assertSame(ROOT, cell.getItem());
        assertSame(root, cell.getTreeTableRow().getTreeItem());
        cell.updateIndex(1);
        assertSame(APPLES, cell.getItem());
        assertSame(apples, cell.getTreeTableRow().getTreeItem());
    }

    @Test public void itemIsNullWhenIndexIsOutOfRange() {
        cell.updateIndex(50);
        cell.updateTreeTableView(tree);
        assertNull(cell.getItem());
    }

    @Test public void treeItemIsNullWhenIndexIsOutOfRange() {
        cell.updateIndex(50);
        cell.updateTreeTableRow(row);
        cell.updateTreeTableView(tree);
        assertNull(cell.getTreeTableRow().getTreeItem());
    }

    @Test public void itemIsNullWhenIndexIsOutOfRange2() {
        cell.updateTreeTableView(tree);
        cell.updateIndex(50);
        assertNull(cell.getItem());
    }

    // Above were the simple tests. Now we check various circumstances
    // to make sure the item is updated correctly.

    @Ignore // TODO file bug!
    @Test public void itemIsUpdatedWhenItWasOutOfRangeButUpdatesToTreeTableViewItemsMakesItInRange() {
        cell.updateIndex(4);
        cell.updateTreeTableView(tree);
        root.getChildren().addAll(new TreeItem<String>("Pumpkin"), new TreeItem<>("Lemon"));
        assertSame("Pumpkin", cell.getItem());
    }

    @Ignore // TODO file bug!
    @Test public void itemIsUpdatedWhenItWasInRangeButUpdatesToTreeTableViewItemsMakesItOutOfRange() {
        cell.updateIndex(2);
        cell.updateTreeTableView(tree);
        assertSame(ORANGES, cell.getItem());
        root.getChildren().remove(oranges);
        assertNull(cell.getTreeTableRow().getTreeItem());
        assertNull(cell.getItem());
    }

    @Ignore // TODO file bug!
    @Test public void itemIsUpdatedWhenTreeTableViewItemsIsUpdated() {
        // set cell index to point to 'Apples'
        cell.updateIndex(1);
        cell.updateTreeTableView(tree);
        assertSame(APPLES, cell.getItem());
        assertSame(apples, cell.getTreeTableRow().getTreeItem());

        // then update the root children list so that the 1st item (including root),
        // is no longer 'Apples', but 'Lime'
        root.getChildren().set(0, new TreeItem<>("Lime"));
        assertEquals("Lime", cell.getItem());
    }

    @Ignore // TODO file bug!
    @Test public void itemIsUpdatedWhenTreeTableViewItemsHasNewItemInsertedBeforeIndex() {
        cell.updateIndex(2);
        cell.updateTreeTableView(tree);
        assertSame(ORANGES, cell.getItem());
        assertSame(oranges, cell.getTreeTableRow().getTreeItem());
        String previous = APPLES;
        root.getChildren().add(0, new TreeItem<>("Lime"));
        assertEquals(previous, cell.getItem());
    }

//    @Test public void itemIsUpdatedWhenTreeTableViewItemsHasItemRemovedBeforeIndex() {
//        cell.updateIndex(1);
//        cell.updateTreeTableView(tree);
//        assertSame(model.get(1), cell.getItem());
//        String other = model.get(2);
//        model.remove(0);
//        assertEquals(other, cell.getItem());
//    }

    @Ignore // TODO file bug!
    @Test public void itemIsUpdatedWhenTreeTableViewItemsIsReplaced() {
        cell.updateIndex(1);
        cell.updateTreeTableView(tree);
        root.getChildren().setAll(new TreeItem<>("Water"), new TreeItem<>("Juice"), new TreeItem<>("Soda"));
        assertEquals("Water", cell.getItem());
    }

    @Ignore // TODO file bug!
    @Test public void itemIsUpdatedWhenTreeTableViewIsReplaced() {
        cell.updateIndex(2);
        cell.updateTreeTableView(tree);
        TreeItem<String> newRoot = new TreeItem<>();
        newRoot.setExpanded(true);
        newRoot.getChildren().setAll(new TreeItem<>("Water"), new TreeItem<>("Juice"), new TreeItem<>("Soda"));
        TreeTableView<String> treeView2 = new TreeTableView<String>(newRoot);
        cell.updateTreeTableView(treeView2);
        assertEquals("Juice", cell.getItem());
    }

    @Test public void replaceItemsWithANull() {
        cell.updateIndex(0);
        cell.updateTreeTableView(tree);
        tree.setRoot(null);
        assertNull(cell.getItem());
    }

//    @Test public void replaceItemsWithANull_ListenersRemovedFromFormerList() {
//        cell.updateIndex(0);
//        cell.updateTreeTableView(tree);
//        ListChangeListener listener = getListChangeListener(cell, "weakItemsListener");
//        assertListenerListContains(model, listener);
//        tree.setRoot(null);
//        assertListenerListDoesNotContain(model, treeener);
//    }
//
@Ignore // TODO file bug!
    @Test public void replaceANullItemsWithNotNull() {
        cell.updateIndex(1);
        cell.updateTreeTableView(tree);
        tree.setRoot(null);

        TreeItem<String> newRoot = new TreeItem<>();
        newRoot.setExpanded(true);
        newRoot.getChildren().setAll(new TreeItem<>("Water"), new TreeItem<>("Juice"), new TreeItem<>("Soda"));
        tree.setRoot(newRoot);
        assertEquals("Water", cell.getItem());
    }


    /*********************************************************************
     * Tests for all things related to editing one of these guys         *
     ********************************************************************/

    // startEdit()
    @Ignore // TODO file bug!
    @Test public void editOnTreeTableViewResultsInEditingInCell() {
        tree.setEditable(true);
        cell.updateTreeTableView(tree);
        cell.updateIndex(1);
        tree.edit(1, null);
        assertTrue(cell.isEditing());
    }

    @Test public void editOnTreeTableViewResultsInNotEditingInCellWhenDifferentIndex() {
        tree.setEditable(true);
        cell.updateTreeTableView(tree);
        cell.updateIndex(1);
        tree.edit(0, null);
        assertFalse(cell.isEditing());
    }

    @Test public void editCellWithNullTreeTableViewResultsInNoExceptions() {
        cell.updateTreeTableView(tree);
        cell.updateIndex(1);
        cell.startEdit();
    }

    @Test public void editCellOnNonEditableTreeDoesNothing() {
        cell.updateIndex(1);
        cell.updateTreeTableView(tree);
        cell.startEdit();
        assertFalse(cell.isEditing());
        assertNull(tree.getEditingCell());
    }

    @Ignore // TODO file bug!
    @Test public void editCellWithTreeResultsInUpdatedEditingIndexProperty() {
        tree.setEditable(true);
        cell.updateTreeTableView(tree);
        cell.updateIndex(1);
        cell.startEdit();
        assertEquals(apples, tree.getEditingCell().getTreeItem());
    }

//    @Ignore // TODO file bug!
//    @Test public void editCellFiresEventOnTree() {
//        tree.setEditable(true);
//        cell.updateTreeTableView(tree);
//        cell.updateIndex(2);
//        final boolean[] called = new boolean[] { false };
//        tree.setOnEditStart(new EventHandler<TreeTableView.EditEvent<String>>() {
//            @Override public void handle(TreeTableView.EditEvent<String> event) {
//                called[0] = true;
//            }
//        });
//        cell.startEdit();
//        assertTrue(called[0]);
//    }

    // commitEdit()
    @Test public void commitWhenTreeIsNullIsOK() {
        cell.updateTreeTableView(tree);
        cell.updateIndex(1);
        cell.startEdit();
        cell.commitEdit("Watermelon");
    }

    @Ignore // TODO file bug!
    @Test public void commitWhenTreeIsNotNullWillUpdateTheItemsTree() {
        tree.setEditable(true);
        cell.updateTreeTableView(tree);
        cell.updateIndex(1);
        cell.startEdit();
        cell.commitEdit("Watermelon");
        assertEquals("Watermelon", tree.getRoot().getChildren().get(0).getValue());
    }

//    @Ignore // TODO file bug!
//    @Test public void commitSendsEventToTree() {
//        tree.setEditable(true);
//        cell.updateTreeTableView(tree);
//        cell.updateIndex(1);
//        cell.startEdit();
//        final boolean[] called = new boolean[] { false };
//        tree.setOnEditCommit(new EventHandler<TreeTableView.EditEvent<String>>() {
//            @Override public void handle(TreeTableView.EditEvent<String> event) {
//                called[0] = true;
//            }
//        });
//        cell.commitEdit("Watermelon");
//        assertTrue(called[0]);
//    }

    @Test public void afterCommitTreeTableViewEditingCellIsNull() {
        tree.setEditable(true);
        cell.updateTreeTableView(tree);
        cell.updateIndex(1);
        cell.startEdit();
        cell.commitEdit("Watermelon");
        assertNull(tree.getEditingCell());
        assertFalse(cell.isEditing());
    }

    // cancelEdit()
    @Test public void cancelEditCanBeCalledWhileTreeTableViewIsNull() {
        cell.updateTreeTableView(tree);
        cell.updateIndex(1);
        cell.startEdit();
        cell.cancelEdit();
    }

//    @Ignore // TODO file bug!
//    @Test public void cancelEditFiresChangeEvent() {
//        tree.setEditable(true);
//        cell.updateTreeTableView(tree);
//        cell.updateIndex(1);
//        cell.startEdit();
//        final boolean[] called = new boolean[] { false };
//        tree.setOnEditCancel(new EventHandler<TreeTableView.EditEvent<String>>() {
//            @Override public void handle(TreeTableView.EditEvent<String> event) {
//                called[0] = true;
//            }
//        });
//        cell.cancelEdit();
//        assertTrue(called[0]);
//    }

    @Test public void cancelSetsTreeTableViewEditingIndexToNegativeOne() {
        tree.setEditable(true);
        cell.updateTreeTableView(tree);
        cell.updateIndex(1);
        cell.startEdit();
        cell.cancelEdit();
        assertNull(tree.getEditingCell());
        assertFalse(cell.isEditing());
    }

    // When the tree view item's change and affects a cell that is editing, then what?
    // When the tree cell's index is changed while it is editing, then what?


    /*********************************************************************
     * Tests for the treeTableView property                              *
     ********************************************************************/

    @Test public void updateTreeTableViewUpdatesTreeTableView() {
        cell.updateTreeTableView(tree);
        assertSame(tree, cell.getTreeTableView());
        assertSame(tree, cell.treeTableViewProperty().get());
    }

    @Test public void canSetTreeTableViewBackToNull() {
        cell.updateTreeTableView(tree);
        cell.updateTreeTableView(null);
        assertNull(cell.getTreeTableView());
        assertNull(cell.treeTableViewProperty().get());
    }

    @Test public void treeTableViewPropertyReturnsCorrectBean() {
        assertSame(cell, cell.treeTableViewProperty().getBean());
    }

    @Test public void updateTreeTableViewWithNullFocusModelResultsInNoException() {
        cell.updateTreeTableView(tree);
        tree.setFocusModel(null);
        cell.updateTreeTableView(new TreeTableView());
    }

    @Test public void updateTreeTableViewWithNullFocusModelResultsInNoException2() {
        tree.setFocusModel(null);
        cell.updateTreeTableView(tree);
        cell.updateTreeTableView(new TreeTableView());
    }

    @Test public void updateTreeTableViewWithNullFocusModelResultsInNoException3() {
        cell.updateTreeTableView(tree);
        TreeTableView tree2 = new TreeTableView();
        tree2.setFocusModel(null);
        cell.updateTreeTableView(tree2);
    }

    @Test public void updateTreeTableViewWithNullSelectionModelResultsInNoException() {
        cell.updateTreeTableView(tree);
        tree.setSelectionModel(null);
        cell.updateTreeTableView(new TreeTableView());
    }

    @Test public void updateTreeTableViewWithNullSelectionModelResultsInNoException2() {
        tree.setSelectionModel(null);
        cell.updateTreeTableView(tree);
        cell.updateTreeTableView(new TreeTableView());
    }

    @Test public void updateTreeTableViewWithNullSelectionModelResultsInNoException3() {
        cell.updateTreeTableView(tree);
        TreeTableView tree2 = new TreeTableView();
        tree2.setSelectionModel(null);
        cell.updateTreeTableView(tree2);
    }

    @Test public void updateTreeTableViewWithNullItemsResultsInNoException() {
        cell.updateTreeTableView(tree);
        tree.setRoot(null);
        cell.updateTreeTableView(new TreeTableView());
    }

    @Test public void updateTreeTableViewWithNullItemsResultsInNoException2() {
        tree.setRoot(null);
        cell.updateTreeTableView(tree);
        cell.updateTreeTableView(new TreeTableView());
    }

    @Test public void updateTreeTableViewWithNullItemsResultsInNoException3() {
        cell.updateTreeTableView(tree);
        TreeTableView tree2 = new TreeTableView();
        tree2.setRoot(null);
        cell.updateTreeTableView(tree2);
    }

    @Test public void treeTableViewIsNullByDefault() {
        assertNull(cell.getTreeTableView());
        assertNull(cell.treeTableViewProperty().get());
    }

    @Test public void treeTableViewPropertyNameIs_treeTableView() {
        assertEquals("treeTableView", cell.treeTableViewProperty().getName());
    }

    private int rt_29923_count = 0;
    @Test public void test_rt_29923() {
        // setup test
        cell = new TreeTableCell<String,String>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                rt_29923_count++;
            }
        };
        TreeTableColumn col = new TreeTableColumn("TEST");
        col.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures, ObservableValue>() {
            @Override public ObservableValue call(TreeTableColumn.CellDataFeatures param) {
                return null;
            }
        });
        tree.getColumns().add(col);
        cell.updateTreeTableColumn(col);
        cell.updateTreeTableView(tree);

        // set index to 0, which results in the cell value factory returning
        // null, but because the number of items is 3, this is a valid value
        cell.updateIndex(0);
        assertNull(cell.getItem());
        assertFalse(cell.isEmpty());
        assertEquals(1, rt_29923_count);

        cell.updateIndex(1);
        assertNull(cell.getItem());
        assertFalse(cell.isEmpty());

        // This test used to be as shown below....but due to RT-33108, it changed
        // to the enabled code beneath. Refer to the first comment in RT-33108
        // for more detail, but in short we can't optimise and not call updateItem
        // when the new and old items are the same - doing so means we can end
        // up with bad bindings, etc in the individual cells (in other words,
        // even if their item has not changed, the rest of their state may have)
//        assertEquals(1, rt_29923_count);    // even though the index has changed,
//                                            // the item is the same, so we don't
//                                            // update the cell item.
        assertEquals(2, rt_29923_count);
    }

    @Test public void test_rt_33106() {
        cell.updateTreeTableView(tree);
        tree.setRoot(null);
        cell.updateIndex(1);
    }
}

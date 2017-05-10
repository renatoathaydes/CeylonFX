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

package javafx.scene.control;

import javafx.collections.WeakListChangeListener;
import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * An implementation of the {@link ComboBoxBase} abstract class for the most common
 * form of ComboBox, where a popup list is shown to users providing them with
 * a choice that they may select from. For more information around the general
 * concepts and API of ComboBox, refer to the {@link ComboBoxBase} class 
 * documentation.
 * 
 * <p>On top of ComboBoxBase, the ComboBox class introduces additional API. Most
 * importantly, it adds an {@link #itemsProperty() items} property that works in
 * much the same way as the ListView {@link ListView#itemsProperty() items}
 * property. In other words, it is the content of the items list that is displayed
 * to users when they click on the ComboBox button.
 * 
 * <p>By default, when the popup list is showing, the maximum number of rows
 * visible is 10, but this can be changed by modifying the 
 * {@link #visibleRowCountProperty() visibleRowCount} property. If the number of
 * items in the ComboBox is less than the value of <code>visibleRowCount</code>,
 * then the items size will be used instead so that the popup list is not
 * exceedingly long.
 * 
 * <p>As with ListView, it is possible to modify the 
 * {@link javafx.scene.control.SelectionModel selection model} that is used, 
 * although this is likely to be rarely changed. This is because the ComboBox
 * enforces the need for a {@link javafx.scene.control.SingleSelectionModel} 
 * instance, and it is not likely that there is much need for alternate 
 * implementations. Nonetheless, the option is there should use cases be found 
 * for switching the selection model.
 * 
 * <p>As the ComboBox internally renders content with a ListView, API exists in
 * the ComboBox class to allow for a custom cell factory to be set. For more
 * information on cell factories, refer to the {@link Cell} and {@link ListCell}
 * classes. It is important to note that if a cell factory is set on a ComboBox,
 * cells will only be used in the ListView that shows when the ComboBox is 
 * clicked. If you also want to customize the rendering of the 'button' area
 * of the ComboBox, you can set a custom {@link ListCell} instance in the 
 * {@link #buttonCellProperty() button cell} property. One way of doing this
 * is with the following code (note the use of {@code setButtonCell}:
 * 
 * <pre>
 * {@code
 * Callback<ListView<String>, ListCell<String>> cellFactory = ...;
 * ComboBox comboBox = new ComboBox();
 * comboBox.setItems(items);
 * comboBox.setButtonCell(cellFactory.call(null));
 * comboBox.setCellFactory(cellFactory);}</pre>
 * 
 * <p>Because a ComboBox can be {@link #editableProperty() editable}, and the
 * default means of allowing user input is via a {@link TextField}, a 
 * {@link #converterProperty() string converter} property is provided to allow
 * for developers to specify how to translate a users string into an object of
 * type T, such that the {@link #valueProperty() value} property may contain it.
 * By default the converter simply returns the String input as the user typed it,
 * which therefore assumes that the type of the editable ComboBox is String. If 
 * a different type is specified and the ComboBox is to be editable, it is 
 * necessary to specify a custom {@link StringConverter}.
 * 
 * <h3>A warning about inserting Nodes into the ComboBox items list</h3>
 * ComboBox allows for the items list to contain elements of any type, including 
 * {@link Node} instances. Putting nodes into 
 * the items list is <strong>strongly not recommended</strong>. This is because 
 * the default {@link #cellFactoryProperty() cell factory} simply inserts Node 
 * items directly into the cell, including in the ComboBox 'button' area too. 
 * Because the scenegraph only allows for Nodes to be in one place at a time, 
 * this means that when an item is selected it becomes removed from the ComboBox
 * list, and becomes visible in the button area. When selection changes the 
 * previously selected item returns to the list and the new selection is removed.
 * 
 * <p>The recommended approach, rather than inserting Node instances into the 
 * items list, is to put the relevant information into the ComboBox, and then
 * provide a custom {@link #cellFactoryProperty() cell factory}. For example,
 * rather than use the following code:
 * 
 * <pre>
 * {@code
 * ComboBox<Rectangle> cmb = new ComboBox<Rectangle>();
 * cmb.getItems().addAll(
 *     new Rectangle(10, 10, Color.RED), 
 *     new Rectangle(10, 10, Color.GREEN), 
 *     new Rectangle(10, 10, Color.BLUE));}</pre>
 * 
 * <p>You should do the following:</p>
 * 
 * <pre><code>
 * ComboBox&lt;Color&gt; cmb = new ComboBox&lt;Color&gt;();
 * cmb.getItems().addAll(
 *     Color.RED,
 *     Color.GREEN,
 *     Color.BLUE);
 *
 * cmb.setCellFactory(new Callback&lt;ListView&lt;Color&gt;, ListCell&lt;Color&gt;&gt;() {
 *     &#064;Override public ListCell&lt;Color&gt; call(ListView&lt;Color&gt; p) {
 *         return new ListCell&lt;Color&gt;() {
 *             private final Rectangle rectangle;
 *             { 
 *                 setContentDisplay(ContentDisplay.GRAPHIC_ONLY); 
 *                 rectangle = new Rectangle(10, 10);
 *             }
 *             
 *             &#064;Override protected void updateItem(Color item, boolean empty) {
 *                 super.updateItem(item, empty);
 *                 
 *                 if (item == null || empty) {
 *                     setGraphic(null);
 *                 } else {
 *                     rectangle.setFill(item);
 *                     setGraphic(rectangle);
 *                 }
 *            }
 *       };
 *   }
 *});</code></pre>
 * 
 * <p>Admittedly the above approach is far more verbose, but it offers the 
 * required functionality without encountering the scenegraph constraints.
 * 
 * @see ComboBoxBase
 * @see Cell
 * @see ListCell
 * @see StringConverter
 * @since JavaFX 2.1
 */
public class ComboBox<T> extends ComboBoxBase<T> {
    
    /***************************************************************************
     *                                                                         *
     * Static properties and methods                                           *
     *                                                                         *
     **************************************************************************/
    
    private static <T> StringConverter<T> defaultStringConverter() {
        return new StringConverter<T>() {
            @Override public String toString(T t) {
                return t == null ? null : t.toString();
            }

            @Override public T fromString(String string) {
                return (T) string;
            }
        };
    }
    
    
    
    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a default ComboBox instance with an empty 
     * {@link #itemsProperty() items} list and default 
     * {@link #selectionModelProperty() selection model}.
     */
    public ComboBox() {
        this(FXCollections.<T>observableArrayList());
    }
    
    /**
     * Creates a default ComboBox instance with the provided items list and
     * a default {@link #selectionModelProperty() selection model}.
     */
    public ComboBox(ObservableList<T> items) {
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        setItems(items);
        setSelectionModel(new ComboBoxSelectionModel<T>(this));
        
        // listen to the value property input by the user, and if the value is
        // set to something that exists in the items list, we should update the
        // selection model to indicate that this is the selected item
        valueProperty().addListener(new ChangeListener<T>() {
            @Override public void changed(ObservableValue<? extends T> ov, T t, T t1) {
                if (getItems() == null) return;
                
                SelectionModel<T> sm = getSelectionModel();
                int index = getItems().indexOf(t1);
                
                if (index == -1) {
                    sm.setSelectedItem(t1);
                } else {
                    // we must compare the value here with the currently selected
                    // item. If they are different, we overwrite the selection
                    // properties to reflect the new value.
                    // We do this as there can be circumstances where there are
                    // multiple instances of a value in the ComboBox items list,
                    // and if we don't check here we may change the selection
                    // mistakenly because the indexOf above will return the first
                    // instance always, and selection may be on the second or 
                    // later instances. This is RT-19227.
                    T selectedItem = sm.getSelectedItem();
                    if (selectedItem == null || ! selectedItem.equals(getValue())) {
                        sm.clearAndSelect(index);
                    }
                }
            }
        });
        
        editableProperty().addListener(new InvalidationListener() {
            @Override public void invalidated(Observable o) {
                // when editable changes, we reset the selection / value states
                getSelectionModel().clearSelection();
            }
        });
    }
    
 
    
    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/
    
    // --- items
    /**
     * The list of items to show within the ComboBox popup.
     */
    private ObjectProperty<ObservableList<T>> items = new SimpleObjectProperty<ObservableList<T>>(this, "items") {
        @Override protected void invalidated() {
            // FIXME temporary fix for RT-15793. This will need to be
            // properly fixed when time permits
            if (getSelectionModel() instanceof ComboBoxSelectionModel) {
                ((ComboBoxSelectionModel<T>)getSelectionModel()).updateItemsObserver(null, getItems());
            }
            if (getSkin() instanceof ComboBoxListViewSkin) {
                ComboBoxListViewSkin<?> skin = (ComboBoxListViewSkin<?>) getSkin();
                skin.updateListViewItems();
            }
        }
    };
    public final void setItems(ObservableList<T> value) { itemsProperty().set(value); }
    public final ObservableList<T> getItems() {return items.get(); }
    public ObjectProperty<ObservableList<T>> itemsProperty() { return items; }
    
    
    // --- string converter
    /**
     * Converts the user-typed input (when the ComboBox is 
     * {@link #editableProperty() editable}) to an object of type T, such that 
     * the input may be retrieved via the  {@link #valueProperty() value} property.
     */
    public ObjectProperty<StringConverter<T>> converterProperty() { return converter; }
    private ObjectProperty<StringConverter<T>> converter = 
            new SimpleObjectProperty<StringConverter<T>>(this, "converter", ComboBox.<T>defaultStringConverter());
    public final void setConverter(StringConverter<T> value) { converterProperty().set(value); }
    public final StringConverter<T> getConverter() {return converterProperty().get(); }
    
    
    // --- cell factory
    /**
     * Providing a custom cell factory allows for complete customization of the
     * rendering of items in the ComboBox. Refer to the {@link Cell} javadoc
     * for more information on cell factories.
     */
    private ObjectProperty<Callback<ListView<T>, ListCell<T>>> cellFactory = 
            new SimpleObjectProperty<Callback<ListView<T>, ListCell<T>>>(this, "cellFactory");
    public final void setCellFactory(Callback<ListView<T>, ListCell<T>> value) { cellFactoryProperty().set(value); }
    public final Callback<ListView<T>, ListCell<T>> getCellFactory() {return cellFactoryProperty().get(); }
    public ObjectProperty<Callback<ListView<T>, ListCell<T>>> cellFactoryProperty() { return cellFactory; }
    
    
    // --- button cell
    /**
     * The button cell is used to render what is shown in the ComboBox 'button'
     * area. If a cell is set here, it does not change the rendering of the
     * ComboBox popup list - that rendering is controlled via the 
     * {@link #cellFactoryProperty() cell factory} API.
     * @since JavaFX 2.2
     */
    public ObjectProperty<ListCell<T>> buttonCellProperty() { return buttonCell; }
    private ObjectProperty<ListCell<T>> buttonCell = 
            new SimpleObjectProperty<ListCell<T>>(this, "buttonCell");
    public final void setButtonCell(ListCell<T> value) { buttonCellProperty().set(value); }
    public final ListCell<T> getButtonCell() {return buttonCellProperty().get(); }
    
    
    // --- Selection Model
    /**
     * The selection model for the ComboBox. A ComboBox only supports
     * single selection.
     */
    private ObjectProperty<SingleSelectionModel<T>> selectionModel = new SimpleObjectProperty<SingleSelectionModel<T>>(this, "selectionModel") {
        private SingleSelectionModel<T> oldSM = null;
        @Override protected void invalidated() {
            if (oldSM != null) {
                oldSM.selectedItemProperty().removeListener(selectedItemListener);
            }
            SingleSelectionModel<T> sm = get();
            oldSM = sm;
            if (sm != null) {
                sm.selectedItemProperty().addListener(selectedItemListener);
            }
        }                
    };
    public final void setSelectionModel(SingleSelectionModel<T> value) { selectionModel.set(value); }
    public final SingleSelectionModel<T> getSelectionModel() { return selectionModel.get(); }
    public final ObjectProperty<SingleSelectionModel<T>> selectionModelProperty() { return selectionModel; }
    
    
    // --- Visible Row Count
    /**
     * The maximum number of rows to be visible in the ComboBox popup when it is
     * showing. By default this value is 10, but this can be changed to increase
     * or decrease the height of the popup.
     */
    private IntegerProperty visibleRowCount
            = new SimpleIntegerProperty(this, "visibleRowCount", 10);
    public final void setVisibleRowCount(int value) { visibleRowCount.set(value); }
    public final int getVisibleRowCount() { return visibleRowCount.get(); }
    public final IntegerProperty visibleRowCountProperty() { return visibleRowCount; }
    
    
    // --- Editor
    private TextField textField;
    /**
     * The editor for the ComboBox. The editor is null if the ComboBox is not
     * {@link #editableProperty() editable}.
     * @since JavaFX 2.2
     */
    private ReadOnlyObjectWrapper<TextField> editor;
    public final TextField getEditor() { 
        return editorProperty().get(); 
    }
    public final ReadOnlyObjectProperty<TextField> editorProperty() { 
        if (editor == null) {
            editor = new ReadOnlyObjectWrapper<TextField>(this, "editor");
            textField = new ComboBoxListViewSkin.FakeFocusTextField();
            editor.set(textField);
        }
        return editor.getReadOnlyProperty(); 
    }

    
    // --- Placeholder Node
    private ObjectProperty<Node> placeholder;
    /**
     * This Node is shown to the user when the ComboBox has no content to show.
     * The placeholder node is shown in the ComboBox popup area
     * when the items list is null or empty.
     * @since JavaFX 8.0
     */
    public final ObjectProperty<Node> placeholderProperty() {
        if (placeholder == null) {
            placeholder = new SimpleObjectProperty<Node>(this, "placeholder");
        }
        return placeholder;
    }
    public final void setPlaceholder(Node value) {
        placeholderProperty().set(value);
    }
    public final Node getPlaceholder() {
        return placeholder == null ? null : placeholder.get();
    }
    
    
    
    /***************************************************************************
     *                                                                         *
     * Methods                                                                 *
     *                                                                         *
     **************************************************************************/

    /** {@inheritDoc} */
    @Override protected Skin<?> createDefaultSkin() {
        return new ComboBoxListViewSkin<T>(this);
    }
    
    
    
    /***************************************************************************
     *                                                                         *
     * Callbacks and Events                                                    *
     *                                                                         *
     **************************************************************************/    
    
    // Listen to changes in the selectedItem property of the SelectionModel.
    // When it changes, set the selectedItem in the value property.
    private ChangeListener<T> selectedItemListener = new ChangeListener<T>() {
        @Override public void changed(ObservableValue<? extends T> ov, T t, T t1) {
            if (wasSetAllCalled && t1 == null) {
                // no-op: fix for RT-22572 where the developer was completely
                // replacing all items in the ComboBox, and expecting the 
                // selection (and ComboBox.value) to remain set. If this isn't
                // here, we would updateValue(null). 
                // Additional fix for RT-22937: adding the '&& t1 == null'. 
                // Without this, there would be circumstances where the user 
                // selecting a new value from the ComboBox would end up in here,
                // when we really should go into the updateValue(t1) call below.
                // We should only ever go into this clause if t1 is null.
                wasSetAllCalled = false;
            } else {
                updateValue(t1);
            }
        }
    };



    /***************************************************************************
     *                                                                         *
     * Private methods                                                         *
     *                                                                         *
     **************************************************************************/        

    private void updateValue(T newValue) {
        if (! valueProperty().isBound()) {
            setValue(newValue);
        }
    }
     

    
    
    /***************************************************************************
     *                                                                         *
     * Stylesheet Handling                                                     *
     *                                                                         *
     **************************************************************************/

    private static final String DEFAULT_STYLE_CLASS = "combo-box";
    
    private boolean wasSetAllCalled = false;
    private int previousItemCount = -1;
    
    // package for testing
    static class ComboBoxSelectionModel<T> extends SingleSelectionModel<T> {
        private final ComboBox<T> comboBox;

        public ComboBoxSelectionModel(final ComboBox<T> cb) {
            if (cb == null) {
                throw new NullPointerException("ComboBox can not be null");
            }
            this.comboBox = cb;
            
            selectedIndexProperty().addListener(new InvalidationListener() {
                @Override public void invalidated(Observable valueModel) {
                    // we used to lazily retrieve the selected item, but now we just
                    // do it when the selection changes.
                    setSelectedItem(getModelItem(getSelectedIndex()));
                }
            });

            /*
             * The following two listeners are used in conjunction with
             * SelectionModel.select(T obj) to allow for a developer to select
             * an item that is not actually in the data model. When this occurs,
             * we actively try to find an index that matches this object, going
             * so far as to actually watch for all changes to the items list,
             * rechecking each time.
             */

            this.comboBox.itemsProperty().addListener(weakItemsObserver);
            if (comboBox.getItems() != null) {
                this.comboBox.getItems().addListener(weakItemsContentObserver);
            }
        }
        
        // watching for changes to the items list content
        private final ListChangeListener<T> itemsContentObserver = new ListChangeListener<T>() {
            @Override public void onChanged(Change<? extends T> c) {
                if (comboBox.getItems() == null || comboBox.getItems().isEmpty()) {
                    setSelectedIndex(-1);
                } else if (getSelectedIndex() == -1 && getSelectedItem() != null) {
                    int newIndex = comboBox.getItems().indexOf(getSelectedItem());
                    if (newIndex != -1) {
                        setSelectedIndex(newIndex);
                    }
                }
                
                while (c.next()) {
                    comboBox.wasSetAllCalled = comboBox.previousItemCount == c.getRemovedSize();
                    
                    
                    if (c.getFrom() <= getSelectedIndex() && getSelectedIndex()!= -1 && (c.wasAdded() || c.wasRemoved())) {
                        int shift = c.wasAdded() ? c.getAddedSize() : -c.getRemovedSize();
                        clearAndSelect(getSelectedIndex() + shift);
                    }
                }
                
                comboBox.previousItemCount = getItemCount();
            }
        };
        
        // watching for changes to the items list
        private final ChangeListener<ObservableList<T>> itemsObserver = new ChangeListener<ObservableList<T>>() {
            @Override
            public void changed(ObservableValue<? extends ObservableList<T>> valueModel, 
                ObservableList<T> oldList, ObservableList<T> newList) {
                    updateItemsObserver(oldList, newList);
            }
        };
        
        private WeakListChangeListener<T> weakItemsContentObserver =
                new WeakListChangeListener<T>(itemsContentObserver);
        
        private WeakChangeListener<ObservableList<T>> weakItemsObserver = 
                new WeakChangeListener<ObservableList<T>>(itemsObserver);
        
        private void updateItemsObserver(ObservableList<T> oldList, ObservableList<T> newList) {
            // update listeners
            if (oldList != null) {
                oldList.removeListener(weakItemsContentObserver);
            }
            if (newList != null) {
                newList.addListener(weakItemsContentObserver);
            }

            // when the items list totally changes, we should clear out
            // the selection and focus
            setSelectedIndex(-1);
        }

        // API Implementation
        @Override protected T getModelItem(int index) {
            final ObservableList<T> items = comboBox.getItems();
            if (items == null) return null;
            if (index < 0 || index >= items.size()) return null;
            return items.get(index);
        }

        @Override protected int getItemCount() {
            final ObservableList<T> items = comboBox.getItems();
            return items == null ? 0 : items.size();
        }
    }
}

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

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;

import com.sun.javafx.Utils;
import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.scene.control.skin.ContextMenuSkin;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * <p>
 * A popup control containing an ObservableList of menu items. The {@link #items}
 * ObservableList allows for any {@link MenuItem} type to be inserted,
 * including its subclasses {@link Menu}, {@link MenuItem}, {@link RadioMenuItem}, {@link CheckMenuItem} and
 * {@link CustomMenuItem}. If an arbitrary Node needs to be
 * inserted into a menu, a CustomMenuItem can be used. One exception to this general rule is that
 * {@link SeparatorMenuItem} could be used for inserting a separator.
 * <p>
 * A common use case for this class is creating and showing context menus to
 * users. To create a context menu using ContextMenu you can do the
 * following:
<pre><code>
final ContextMenu contextMenu = new ContextMenu();
contextMenu.setOnShowing(new EventHandler&lt;WindowEvent&gt;() {
    public void handle(WindowEvent e) {
        System.out.println("showing");
    }
});
contextMenu.setOnShown(new EventHandler&lt;WindowEvent&gt;() {
    public void handle(WindowEvent e) {
        System.out.println("shown");
    }
});

MenuItem item1 = new MenuItem("About");
item1.setOnAction(new EventHandler&lt;ActionEvent&gt;() {
    public void handle(ActionEvent e) {
        System.out.println("About");
    }
});
MenuItem item2 = new MenuItem("Preferences");
item2.setOnAction(new EventHandler&lt;ActionEvent&gt;() {
    public void handle(ActionEvent e) {
        System.out.println("Preferences");
    }
});
contextMenu.getItems().addAll(item1, item2);

final TextField textField = new TextField("Type Something");
textField.setContextMenu(contextMenu);
</code></pre>
 *
 * <p>{@link Control#setContextMenu(javafx.scene.control.ContextMenu) } convenience
 * method can be used to set a context menu on on any control. The example above results in the
 * context menu being displayed on the right {@link javafx.geometry.Side Side}
 * of the TextField. Alternatively, an event handler can also be set on the control
 * to invoke the context menu as shown below.
 * <pre><code>
textField.setOnAction(new EventHandler&lt;ActionEvent&gt;() {
    public void handle(ActionEvent e) {
        contextMenu.show(textField, Side.BOTTOM, 0, 0);
    }
});
 
Group root = (Group) scene.getRoot();
root.getChildren().add(textField);
</code></pre>
 *
 * <p>In this example, the context menu is shown when the user clicks on the
 * {@link javafx.scene.control.Button Button} (of course, you should use the
 * {@link MenuButton} control to do this rather than doing the above).</p>
 *
 * <p>Note that the show function used in the code sample
 * above will result in the ContextMenu appearing directly beneath the
 * TextField. You can vary the {@link javafx.geometry.Side Side}  to get the results you expect.</p>
 *
 * @see MenuItem
 * @see Menu
 * @since JavaFX 2.0
 */
public class ContextMenu extends PopupControl {

    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    /**
     * Create a new ContextMenu
     */
    public ContextMenu() {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        setAutoHide(true);
    }

    /**
     * Create a new ContextMenu initialized with the given items
     */
    public ContextMenu(MenuItem... items) {
        this();
        this.items.addAll(items);
    }

    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * Callback function to be informed when an item contained within this
     * {@code ContextMenu} has been activated. The current implementation informs
     * all parent menus as well, so that it is not necessary to listen to all
     * sub menus for events.
     */
    private ObjectProperty<EventHandler<ActionEvent>> onAction = new ObjectPropertyBase<EventHandler<ActionEvent>>() {
        @Override protected void invalidated() {
            setEventHandler(ActionEvent.ACTION, get());
       }

        @Override
        public Object getBean() {
            return ContextMenu.this;
        }

        @Override
        public String getName() {
            return "onAction";
        }
    };
    public final void setOnAction(EventHandler<ActionEvent> value) { onActionProperty().set(value); }
    public final EventHandler<ActionEvent> getOnAction() { return onActionProperty().get(); }
    public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() { return onAction; }

    private final ObservableList<MenuItem> items = new TrackableObservableList<MenuItem>() {
        @Override protected void onChanged(Change<MenuItem> c) {
            while (c.next()) {
                for (MenuItem item : c.getRemoved()) {
                    item.setParentPopup(null);
                }
                for (MenuItem item : c.getAddedSubList()) {
                    item.setParentPopup(ContextMenu.this);
                }
            }
        }
    };
    /**
     * The menu items on the context menu. If this ObservableList is modified at
     * runtime, the ContextMenu will update as expected.
     * @see MenuItem
     */
    public final ObservableList<MenuItem> getItems() { return items; }

     /**
     * @treatAsPrivate implementation detail
     * @deprecated This is an internal API that is not intended for use and will be removed in the next version
     */
    @Deprecated
    private final BooleanProperty impl_showRelativeToWindow = new SimpleBooleanProperty(false);
     /**
      * @treatAsPrivate implementation detail
      */
    public final boolean isImpl_showRelativeToWindow() { return impl_showRelativeToWindow.get(); }
     /**
      * @treatAsPrivate implementation detail
      */
    public final void setImpl_showRelativeToWindow(boolean value) { impl_showRelativeToWindow.set(value); }
     /**
      * @treatAsPrivate implementation detail
      */
    public final BooleanProperty impl_showRelativeToWindowProperty() { return impl_showRelativeToWindow; }
    
    /***************************************************************************
     *                                                                         *
     * Methods                                                                 *
     *                                                                         *
     **************************************************************************/

    /**
     * Shows the {@code ContextMenu} relative to the given anchor node, on the side
     * specified by the {@code hpos} and {@code vpos} parameters, and offset
     * by the given {@code dx} and {@code dy} values for the x-axis and y-axis, respectively.
     * If there is not enough room, the menu is moved to the opposite side and
     * the offset is not applied.
     * <p>
     * To clarify the purpose of the {@code hpos} and {@code vpos} parameters,
     * consider that they are relative to the anchor node. As such, a {@code hpos}
     * and {@code vpos} of {@code CENTER} would mean that the ContextMenu appears
     * on top of the anchor, with the (0,0) position of the {@code ContextMenu}
     * positioned at (0,0) of the anchor. A {@code hpos} of right would then shift
     * the {@code ContextMenu} such that its top-left (0,0) position would be attached
     * to the top-right position of the anchor.
     * <p>
     * This function is useful for finely tuning the position of a menu,
     * relative to the parent node to ensure close alignment.
     */
    // TODO provide more detail
     public void show(Node anchor, Side side, double dx, double dy) {
        if (anchor == null) return;
        Event.fireEvent(this, new Event(Menu.ON_SHOWING));
        if (getItems().size() == 0) return;

        getScene().setNodeOrientation(anchor.getEffectiveNodeOrientation());
        //RT-27546 : The problem here is before the first show the content of the popup
        // is not initialized yet and hence the prefWidth & prefHeight remains 0
        // This leads to incorrect translation of anchor to screen coordinates.
        // A call to show initializes the content. Skin is null only the very first time.
        if(getSkin() == null) {
            hide();
            super.show(anchor, dx, dy);
        }
        // FIXME because Side is not yet in javafx.geometry, we have to convert
        // to the old HPos/VPos API here, as Utils can not refer to Side in the
        // charting API.
        HPos hpos = side == Side.LEFT ? HPos.LEFT : side == Side.RIGHT ? HPos.RIGHT : HPos.CENTER;
        VPos vpos = side == Side.TOP ? VPos.TOP : side == Side.BOTTOM ? VPos.BOTTOM : VPos.CENTER;

        // translate from anchor/hpos/vpos/dx/dy into screenX/screenY
        Point2D point = Utils.pointRelativeTo(anchor,
                prefWidth(-1), prefHeight(-1),
                hpos, vpos, dx, dy, true);
        doShow(anchor, point.getX(), point.getY());
    }

     /**
     * Shows the {@code ContextMenu} at the specified screen coordinates. If there
     * is not enough room at the specified location to show the {@code ContextMenu}
     * given its size requirements, the necessary adjustments are made to bring
     * the {@code ContextMenu} back back on screen. This also means that the
     * {@code ContextMenu} will not span multiple monitors.
     */
    public void show(Node anchor, double screenX, double screenY) {
        if (anchor == null) return;
        Event.fireEvent(this, new Event(Menu.ON_SHOWING));
        if (getItems().size() == 0) return;

        getScene().setNodeOrientation(anchor.getEffectiveNodeOrientation());
        doShow(anchor, screenX, screenY);
    }

    private void doShow(Node anchor, double screenX, double screenY) {
        if(isImpl_showRelativeToWindow()) {
            final Scene scene = (anchor == null) ? null : anchor.getScene();
            final Window win = (scene == null) ? null : scene.getWindow();
            if (win == null) return;
            super.show(win, screenX, screenY);
        } else {
            super.show(anchor, screenX, screenY);
        }
        Event.fireEvent(this, new Event(Menu.ON_SHOWN));
    }

    /**
     * Hides this {@code ContextMenu} and any visible submenus, assuming that when this function
     * is called that the {@code ContextMenu} was showing.
     * <p>
     * If this {@code ContextMenu} is not showing, then nothing happens.
     */
    @Override public void hide() {
        if (!isShowing()) return;
        Event.fireEvent(this, new Event(Menu.ON_HIDING));
        super.hide();
        Event.fireEvent(this, new Event(Menu.ON_HIDDEN));
    }

    /** {@inheritDoc} */
    @Override protected Skin<?> createDefaultSkin() {
        return new ContextMenuSkin(this);
    }

    /***************************************************************************
     *                                                                         *
     *                         Stylesheet Handling                             *
     *                                                                         *
     ***************************************************************************/

    private static final String DEFAULT_STYLE_CLASS = "context-menu";
}

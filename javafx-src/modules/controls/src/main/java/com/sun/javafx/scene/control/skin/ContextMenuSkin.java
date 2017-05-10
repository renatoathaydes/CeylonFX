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

import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.Skin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import com.sun.javafx.scene.control.behavior.TwoLevelFocusPopupBehavior;

/**
 * Default Skin implementation for PopupMenu. Several controls use PopupMenu in
 * order to display items in a drop down. It deals mostly with show hide logic for
 * Popup based controls, and specifically in this case for PopupMenu. This is
 * explained in the superclass {@link PopupControlSkin}.
 * <p>
 * Region/css based skin implementation for PopupMenu is the {@link PopupMenuContent}
 * class.
 */
public class ContextMenuSkin implements Skin<ContextMenu> {

    /* need to hold a reference to popupMenu here because getSkinnable() deliberately
     * returns null in PopupControlSkin. */
    private ContextMenu popupMenu;
    
    private final Region root;
    private TwoLevelFocusPopupBehavior tlFocus;
    
    // Fix for RT-18247
    private final EventHandler<KeyEvent> keyListener = new EventHandler<KeyEvent>() {
        @Override public void handle(KeyEvent event) {
            if (event.getEventType() != KeyEvent.KEY_PRESSED) return;
            
            // We only care if the root container still has focus
            if (! root.isFocused()) return;
            
            final KeyCode code = event.getCode();
            switch (code) {
                case ENTER: 
                case SPACE: popupMenu.hide(); return;
                default:    return;
            }
        }
    };

    /***/
    public ContextMenuSkin(final ContextMenu popupMenu) {
        this.popupMenu = popupMenu;
        
        // When a contextMenu is shown, requestFocus on its content to enable
        // keyboard navigation.
        popupMenu.addEventHandler(Menu.ON_SHOWN, new EventHandler<Event>() {
            @Override public void handle(Event event) {
                Node cmContent = popupMenu.getSkin().getNode();
                if (cmContent != null) cmContent.requestFocus();
                
                root.addEventHandler(KeyEvent.KEY_PRESSED, keyListener);
            }
        });
        popupMenu.addEventHandler(Menu.ON_HIDDEN, new EventHandler<Event>() {
            @Override public void handle(Event event) {
                Node cmContent = popupMenu.getSkin().getNode();
                if (cmContent != null) cmContent.requestFocus();
                
                root.removeEventHandler(KeyEvent.KEY_PRESSED, keyListener);
            }
        });

        if (BehaviorSkinBase.IS_TOUCH_SUPPORTED &&
            popupMenu.getStyleClass().contains("text-input-context-menu")) {
            root = new EmbeddedTextContextMenuContent(popupMenu);
        } else {
            root = new ContextMenuContent(popupMenu);
        }
        root.idProperty().bind(popupMenu.idProperty());
        root.styleProperty().bind(popupMenu.styleProperty());
        root.getStyleClass().addAll(popupMenu.getStyleClass()); // TODO needs to handle updates

        // Only add this if we're on an embedded platform that supports 5-button navigation
        if (Utils.isTwoLevelFocus()) {
            tlFocus = new TwoLevelFocusPopupBehavior(popupMenu); // needs to be last.
        }
    }

    @Override public ContextMenu getSkinnable() {
        return popupMenu;
    }

    @Override public Node getNode() {
        return root;
    }

    @Override public void dispose() {
        root.idProperty().unbind();
        root.styleProperty().unbind();
        if (tlFocus != null) tlFocus.dispose();
    }
}

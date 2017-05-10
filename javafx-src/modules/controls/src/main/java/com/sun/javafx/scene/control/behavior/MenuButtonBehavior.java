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

package com.sun.javafx.scene.control.behavior;

import javafx.scene.control.MenuButton;
import java.util.ArrayList;
import java.util.List;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.SPACE;
import static javafx.scene.input.KeyEvent.KEY_PRESSED;

/**
 * Behavior for MenuButton.
 */
public class MenuButtonBehavior extends MenuButtonBehaviorBase<MenuButton> {

    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a new MenuButtonBehavior for the given MenuButton.
     *
     * @param menuButton the MenuButton
     */
    public MenuButtonBehavior(final MenuButton menuButton) {
        super(menuButton, MENU_BUTTON_BINDINGS);
    }

    /***************************************************************************
     *                                                                         *
     * Key event handling                                                      *
     *                                                                         *
     **************************************************************************/

    /**
     * The key bindings for the MenuButton. Sets up the keys to open the menu.
     */
    protected static final List<KeyBinding> MENU_BUTTON_BINDINGS = new ArrayList<KeyBinding>();
    static {
        MENU_BUTTON_BINDINGS.addAll(BASE_MENU_BUTTON_BINDINGS);
        MENU_BUTTON_BINDINGS.add(new KeyBinding(SPACE, KEY_PRESSED, OPEN_ACTION));
        MENU_BUTTON_BINDINGS.add(new KeyBinding(ENTER, KEY_PRESSED, OPEN_ACTION));
    }
}

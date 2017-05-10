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

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import com.sun.javafx.scene.control.skin.TitledPaneSkin;
import com.sun.javafx.scene.traversal.Direction;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.SPACE;
import static javafx.scene.input.KeyCode.TAB;

public class TitledPaneBehavior extends BehaviorBase<TitledPane> {

    private TitledPane titledPane;

    public TitledPaneBehavior(TitledPane pane) {
        super(pane, TITLEDPANE_BINDINGS);
        this.titledPane = pane;
    }

    /***************************************************************************
     *                                                                         *
     * Key event handling                                                      *
     *                                                                         *
     **************************************************************************/

    private static final String PRESS_ACTION = "Press";

    protected static final List<KeyBinding> TITLEDPANE_BINDINGS = new ArrayList<KeyBinding>();
    static {
        TITLEDPANE_BINDINGS.add(new KeyBinding(TAB, "TraverseNext"));
        TITLEDPANE_BINDINGS.add(new KeyBinding(TAB, "TraversePrevious").shift());
        TITLEDPANE_BINDINGS.add(new KeyBinding(ENTER, PRESS_ACTION));
        TITLEDPANE_BINDINGS.add(new KeyBinding(SPACE, PRESS_ACTION));
    }

    @Override protected void callAction(String name) {
        switch (name) {
          case PRESS_ACTION:
            if (titledPane.isCollapsible() && titledPane.isFocused()) {
                titledPane.setExpanded(!titledPane.isExpanded());
                titledPane.requestFocus();
            }
            break;

        case "TraverseNext":
        case "TraversePrevious":
            TitledPaneSkin tps = (TitledPaneSkin)titledPane.getSkin();
            //tps.getContentRegion().getImpl_traversalEngine().getTopLeftFocusableNode();
            // Is there any focusable node in the TitledPane content
            if (titledPane.isExpanded() &&
                (!tps.getContentRegion().getImpl_traversalEngine().registeredNodes.isEmpty() ||
                 TabPaneBehavior.getFirstPopulatedInnerTraversalEngine(tps.getContentRegion().getChildren()) != null)) {
                /*
                ** if we have the focus owner then traverse from it, otherwise
                ** request focus in the top-left
                */
                List<Node> children = titledPane.getChildrenUnmodifiable();
                Node focusNode = titledPane.getScene().getFocusOwner();
                    
                if (focusNode != null && isChildFocused(focusNode, children)) {
                    // This happens if the child doesn't have the key binding
                    // for traversal and the event bubbled up to this class.
                    focusNode.impl_traverse("TraverseNext".equals(name) ? Direction.NEXT : Direction.PREVIOUS);
                } else if ("TraverseNext".equals(name)) {
                    focusFirstChild(children);
                } else {
                    super.callAction(name);
                }
            } else {
                super.callAction(name);
            }
            break;

          default:
            super.callAction(name);
        }
    }

    public static boolean isChildFocused(Node focusedNode, List<Node> children) {
        boolean answer = false;
        for(int i = 0; i < children.size(); i++) {
            if (children.get(i) == focusedNode) {
                answer = true;
                break;
            }
            if (children.get(i) instanceof Parent) {
                if (isChildFocused(focusedNode, ((Parent)children.get(i)).getChildrenUnmodifiable())) {
                    return true;
                }
            }
        }
        return answer;
    }

    public static boolean focusFirstChild(List<Node> children) {
        for(int i = 0; i < children.size(); i++) {
            Node n = children.get(i);
            if (n.isFocusTraversable() && n.impl_isTreeVisible() && !n.isDisabled()) {
                n.requestFocus();
                return true;
            }
            else if (n instanceof Parent) {
                if (focusFirstChild(((Parent)n).getChildrenUnmodifiable())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean focusLastChild(List<Node> children) {
        for(int i = children.size()-1 ; i > -1; i--) {
            Node n = children.get(i);
            if (n.isFocusTraversable() && n.impl_isTreeVisible() && !n.isDisabled()) {
                n.requestFocus();
                return true;
            }
            else if (n instanceof Parent) {
                if (focusFirstChild(((Parent)n).getChildrenUnmodifiable())) {
                    return true;
                }
            }
        }
        return false;
    }




    /***************************************************************************
     *                                                                         *
     * Mouse event handling                                                    *
     *                                                                         *
     **************************************************************************/

    @Override public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        TitledPane tp = getControl();
        tp.requestFocus();
    }

    /**************************************************************************
     *                         State and Functions                            *
     *************************************************************************/

    public void expand() {
        titledPane.setExpanded(true);
    }

    public void collapse() {
        titledPane.setExpanded(false);
    }

    public void toggle() {
        titledPane.setExpanded(!titledPane.isExpanded());
    }

}


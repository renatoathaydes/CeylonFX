/*
 * Copyright (c) 2012, 2014, Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle Corporation nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.oracle.javafx.scenebuilder.kit.editor.panel.content.mode;

import com.oracle.javafx.scenebuilder.kit.editor.panel.content.AbstractDecoration;
import com.oracle.javafx.scenebuilder.kit.editor.panel.content.ContentPanelController;
import com.oracle.javafx.scenebuilder.kit.editor.panel.content.util.Picker;
import com.oracle.javafx.scenebuilder.kit.editor.panel.content.util.RegionRectangle;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMObject;
import java.util.List;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.transform.Transform;

/**
 *
 */
public class HitNodeChrome extends AbstractDecoration<Object> {
    
    private final Point2D hitPoint;
    private final RegionRectangle chrome = new RegionRectangle();
    private Node closestNode;
    private Node hitNode;

    public HitNodeChrome(ContentPanelController contentPanelController, FXOMObject fxomObject, Point2D hitPoint) {
        super(contentPanelController, fxomObject, Object.class);
        this.hitPoint = hitPoint;
        this.closestNode = findClosestNode();
        this.hitNode = findHitNode();
        
        assert closestNode != null;
        assert hitNode != null;
        assert hitNode.getScene() != null;
        
        chrome.setMouseTransparent(true);
        chrome.getRegion().getStyleClass().add("css-pick-chrome"); //NOI18N
        getRootNode().getChildren().add(chrome);
    }

    public Point2D getHitPoint() {
        return hitPoint;
    }
    
    
    /*
     * AbstractDecoration
     */

    @Override
    public Bounds getSceneGraphObjectBounds() {
        return closestNode.getLayoutBounds();
    }

    @Override
    public Transform getSceneGraphToSceneTransform() {
        return closestNode.getLocalToSceneTransform();
    }

    @Override
    public Point2D sceneGraphObjectToScene(double x, double y) {
        return closestNode.localToScene(x, y);
    }

    @Override
    public Point2D sceneToSceneGraphObject(double x, double y) {
        return closestNode.sceneToLocal(x, y);
    }

    @Override
    protected void startListeningToSceneGraphObject() {
        startListeningToLayoutBounds(closestNode);
        startListeningToLocalToSceneTransform(closestNode);
        startListeningToBoundsInParent(hitNode);
    }

    @Override
    protected void stopListeningToSceneGraphObject() {
        stopListeningToLayoutBounds(closestNode);
        stopListeningToLocalToSceneTransform(closestNode);
        stopListeningToBoundsInParent(hitNode);
    }

    @Override
    protected void layoutDecoration() {
        assert chrome.getScene() != null;
        
        if ((hitNode == null) || (hitNode.getScene() == null)) {
            chrome.setVisible(false);
        } else {
            assert hitNode.getScene() != null;
            assert hitNode.getScene() == closestNode.getScene();
            
            final Transform t = getContentPanelController().computeSceneGraphToRudderLayerTransform(hitNode);
            chrome.getTransforms().clear();
            chrome.getTransforms().add(t);
            chrome.setLayoutBounds(hitNode.getLayoutBounds());
//            chrome.setLayoutBounds(new BoundingBox(0, 0, 20, 15));
            chrome.setVisible(true);
        }
    }

    @Override
    public State getState() {
        State result = super.getState();
        
        if (result == State.CLEAN) {
            final Node newClosestNode = findClosestNode();
            final Node newHitNode = findHitNode();
            if ((closestNode != newClosestNode) || (hitNode != newHitNode)) {
                result = State.NEEDS_RECONCILE;
            }
        }
        
        return result;
    }

    @Override
    public void reconcile() {
        super.reconcile();
        closestNode = findClosestNode();
        hitNode = findHitNode();
    }

    
    
    /*
     * Private
     */
    
    private Node findClosestNode() {
        final FXOMObject nodeObject = getFxomObject().getClosestNode();
        assert nodeObject != null; // At least the root is a Node
        assert nodeObject.getSceneGraphObject() instanceof Node;
        return (Node) nodeObject.getSceneGraphObject();
    }
    
    
    private Node findHitNode() {
        final Node result;
        
        if (hitPoint == null) {
            if (getFxomObject().isNode()) {
                result = (Node) getFxomObject().getSceneGraphObject();
            } else {
                result = null;
            }
        } else {
            assert closestNode != null;
            
            final Picker picker = new Picker();
            final List<Node> pick = picker.pickInLocal(closestNode, hitPoint.getX(), hitPoint.getY());
            if (pick == null) {
                result = null;
            } else {
                assert pick.isEmpty() == false;
                result = pick.get(0);
            }
        }
        
        return result;
    }
    
    
    
}

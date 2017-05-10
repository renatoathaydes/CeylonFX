/*
 * Copyright (c) 2014, Oracle and/or its affiliates.
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

package com.oracle.javafx.scenebuilder.kit.editor.panel.content;

import com.oracle.javafx.scenebuilder.kit.editor.panel.content.AbstractDecoration.State;
import com.oracle.javafx.scenebuilder.kit.editor.panel.content.driver.handles.AbstractGenericHandles;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMObject;
import javafx.geometry.Point2D;
import javafx.scene.transform.Transform;

/**
 *
 */
public abstract class AbstractResilientHandles<T> extends AbstractGenericHandles<T> {

    private boolean ready;
    
    public AbstractResilientHandles(ContentPanelController contentPanelController, 
            FXOMObject fxomObject, Class<T> sceneGraphClass) {
        super(contentPanelController, fxomObject, sceneGraphClass);
        getRootNode().setVisible(false);
    }
    
    public void setReady(boolean ready) {
        if (this.ready != ready) {
            this.ready = ready;
            readyDidChange();
        }
    }
    
    public boolean isReady() {
        return ready;
    }
    
    /*
     * AbstractDecoration
     */
    
    
    @Override
    public void reconcile() {
        assert getState() == State.NEEDS_RECONCILE;
        
        if (ready) {
            stopListeningToSceneGraphObject();
        }
        updateSceneGraphObject();
        if (ready) {
            startListeningToSceneGraphObject();
            layoutDecoration();
        }
    }
    
    @Override
    public Point2D sceneGraphObjectToDecoration(double x, double y, boolean snapToPixel) {
        assert ready;
        return super.sceneGraphObjectToDecoration(x, y, snapToPixel);
    }
    
    @Override
    public Transform getSceneGraphObjectToDecorationTransform() {
        assert ready;
        return super.getSceneGraphObjectToDecorationTransform();
    }
    
    @Override
    protected void rootNodeSceneDidChange() {
        if (ready) {
            if (getRootNode().getScene() == null) {
                // Transition  D -> C
                getRootNode().setVisible(false);
                stopListeningToSceneGraphObject();
            } else {
                // Transition C -> D
                layoutDecoration();
                startListeningToSceneGraphObject();
                getRootNode().setVisible(true);
            }
        } // else transitions A -> B or B -> A
    }
    
    
    /*
     * Private
     */
    
    /*
     * 
     *      \ rootNode.getScene() |      null      |   not null    |
     *   ready                    |                |               |
     *   -------------------------+----------------+---------------+
     *   false                    |        A       |       B       |
     *   -------------------------+----------------+---------------+
     *   true                     |        C       |       D       |
     *   -------------------------+----------------+---------------+
     * 
     *   On transitions A -> D, B -> D, C -> D
     *      => layoutDecoration()
     *      => startListeningToSceneGraphObject()
     *      => rootNode.setVisible(true)
     * 
     *   On transitions D -> A, D -> B, D -> C
     *      => rootNode.setVisible(false)
     *      => stopListeningToSceneGraphObject()
     */
    
    private void readyDidChange() {
        if (getRootNode().getScene() != null) {
            if (ready) {
                // Transition B -> D
                layoutDecoration();
                startListeningToSceneGraphObject();
                getRootNode().setVisible(true);
            } else {
                // Transition D -> B
                getRootNode().setVisible(false);
                stopListeningToSceneGraphObject();
            }
        } // Transitions A -> C or C -> A
    }
    
}

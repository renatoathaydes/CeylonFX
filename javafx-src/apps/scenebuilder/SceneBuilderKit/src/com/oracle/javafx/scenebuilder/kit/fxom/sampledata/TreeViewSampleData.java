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

package com.oracle.javafx.scenebuilder.kit.fxom.sampledata;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 *
 */
class TreeViewSampleData extends AbstractSampleData {
    
    private final TreeItem<String> sampleRoot;

    public TreeViewSampleData() {
        int i = 0;
        sampleRoot = new TreeItem<>(lorem(i++));
        sampleRoot.setExpanded(true);
        for (int j = 0; j<10; j++) {
            final Rectangle r = new Rectangle(10, 10);
            r.setFill(color(i));
            TreeItem<String> child = new TreeItem<>(lorem(i++));
            child.setExpanded(true);
            child.setGraphic(r);
            for (int k=0; k<3; k++) {
                final TreeItem<String> child2 = new TreeItem<>(lorem(i++));
                child2.setExpanded(true);
                final Circle c = new Circle(5);
                c.setFill(color(i));
                child2.setGraphic(c);
                child.getChildren().add(child2);
            }
            sampleRoot.getChildren().add(child);
        }
    }

    /*
     * AbstractSampleData
     */
    
    
    @Override
    public void applyTo(Object sceneGraphObject) {
        assert sceneGraphObject instanceof TreeView;
        @SuppressWarnings("unchecked")        
        final TreeView<String> treeView = (TreeView<String>) sceneGraphObject;
        treeView.setRoot(sampleRoot);
    }

    @Override
    public void removeFrom(Object sceneGraphObject) {
        assert sceneGraphObject instanceof TreeView;
        @SuppressWarnings("unchecked")        
        final TreeView<String> treeView = (TreeView<String>) sceneGraphObject;
        treeView.setRoot(null);
    }
    
}

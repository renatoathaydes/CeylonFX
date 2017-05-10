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
package com.oracle.javafx.scenebuilder.kit.editor.selection;

import com.oracle.javafx.scenebuilder.kit.editor.panel.content.util.Picker;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMDocument;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMNodes;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMObject;
import com.oracle.javafx.scenebuilder.kit.metadata.util.DesignHierarchyPath;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;

/**
 *
 * 
 */
public class ObjectSelectionGroup extends AbstractSelectionGroup {
    
    private final Set<FXOMObject> items = new HashSet<>();
    private final FXOMObject hitItem;
    private final Point2D hitPoint;
    
    ObjectSelectionGroup(FXOMObject fxomObject, Point2D hitPoint) {
        assert fxomObject != null;
        this.items.add(fxomObject);
        this.hitItem = fxomObject;
        this.hitPoint = hitPoint;
    }
    
    ObjectSelectionGroup(Collection<FXOMObject> fxomObjects, FXOMObject hitItem, Point2D hitPoint) {
        assert fxomObjects != null;
        assert hitItem != null;
        assert fxomObjects.contains(hitItem);
        this.items.addAll(fxomObjects);
        this.hitItem = hitItem;
        this.hitPoint = hitPoint;
    }
    
    ObjectSelectionGroup(FXOMObject fxomObject, Node hitNode) {
        assert fxomObject != null;
        assert (hitNode == null) || (hitNode.getScene() != null);
        
        this.items.add(fxomObject);
        this.hitItem = fxomObject;
        this.hitPoint = computeHitPoint(hitNode);
    }
    
    public Set<FXOMObject> getItems() {
        return Collections.unmodifiableSet(items);
    }

    public FXOMObject getHitItem() {
        return hitItem;
    }

    public Point2D getHitPoint() {
        return hitPoint;
    }
    
    public Set<FXOMObject> getFlattenItems() {
        return FXOMNodes.flatten(items);
    }
    
    public List<FXOMObject> getSortedItems() {
        return FXOMNodes.sort(items);
    }
    
    public boolean hasSingleParent() {
        final boolean result;
        
        if (items.size() == 1) {
            result = true;
        } else {
            final Set<FXOMObject> parents = new HashSet<>();
            for (FXOMObject i : items) {
                parents.add(i.getParentObject());
            }
            result = parents.size() == 1;
        }
        
        return result;
    }
    
    public Node findHitNode() {
        final Node result;
        
        if (hitPoint == null) {
            if (hitItem.isNode()) {
                result = (Node) hitItem.getSceneGraphObject();
            } else {
                result = null;
            }
        } else {
            final FXOMObject nodeObject = hitItem.getClosestNode();
            if (nodeObject == null) {
                result = null;
            } else {
                final Node closestNode = (Node)nodeObject.getSceneGraphObject();
                final Picker picker = new Picker();
                final List<Node> pick = picker.pickInLocal(closestNode, hitPoint.getX(), hitPoint.getY());
                if (pick == null) {
                    result = null;
                } else {
                    assert pick.isEmpty() == false;
                    result = pick.get(0);
                }
            }
        }
        
        return result;
    }
    
    /*
     * AbstractSelectionGroup
     */
    
    @Override
    public FXOMObject getAncestor() {
        final FXOMObject result;
        
        assert items.isEmpty() == false;
        
        switch(items.size()) {

            case 0:
                result = null;
                break;

            case 1:
                result = items.iterator().next().getParentObject();
                break;

            default:
                DesignHierarchyPath commonPath = null;
                for (FXOMObject i : items) {
                    final FXOMObject parent = i.getParentObject();
                    if (parent != null) {
                        final DesignHierarchyPath dph = new DesignHierarchyPath(parent);
                        if (commonPath == null) {
                            commonPath = dph;
                        } else {
                            commonPath = commonPath.getCommonPathWith(dph);
                        }
                    }
                }
                assert commonPath != null; // Else it would mean root is selected twice
                result = commonPath.getLeaf();
                break;
        }
        
        return result;
    }

    @Override
    public boolean isValid(FXOMDocument fxomDocument) {
        assert fxomDocument != null;
        
        boolean result;
        final FXOMObject fxomRoot = fxomDocument.getFxomRoot();
        if (fxomRoot == null) {
            result = false;
        } else {
            result = true;
            for (FXOMObject i : items) {
                final boolean ok = (i == fxomRoot) || i.isDescendantOf(fxomRoot);
                if (ok == false) {
                    result = false;
                    break;
                }
            }
        }
        
        return result;
    }
    
    
    /*
     * Cloneable
     */
    @Override
    public ObjectSelectionGroup clone() throws CloneNotSupportedException {
        return (ObjectSelectionGroup)super.clone();
    }

    
    /*
     * Object
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.items);
        hash = 41 * hash + Objects.hashCode(this.hitItem);
        hash = 41 * hash + Objects.hashCode(this.hitPoint);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ObjectSelectionGroup other = (ObjectSelectionGroup) obj;
        if (!Objects.equals(this.items, other.items)) {
            return false;
        }
        if (!Objects.equals(this.hitItem, other.hitItem)) {
            return false;
        }
        if (!Objects.equals(this.hitPoint, other.hitPoint)) {
            return false;
        }
        return true;
    }
    
    /*
     * Private
     */
    
    private Point2D computeHitPoint(Node hitNode) {
        final Point2D result;
        
        if (hitNode == null) {
            result = null;
        } else {
            final FXOMObject nodeObject = hitItem.getClosestNode();
            if (nodeObject == null) {
                result = null;
            } else {
                final Node closestNode = (Node)nodeObject.getSceneGraphObject();
                final Bounds hnb = hitNode.getLayoutBounds();
                final double midX = (hnb.getMinX() + hnb.getMaxX()) / 2.0;
                final double midY = (hnb.getMinY() + hnb.getMaxY()) / 2.0;
                if (hitNode == closestNode) {
                    result = new Point2D(midX, midY);
                } else {
                    result = closestNode.sceneToLocal(hitNode.localToScene(midX, midY));
                }
                assert findHitNode() == hitNode;
            }
        }
        
        return result;
    }
}

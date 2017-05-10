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
package com.oracle.javafx.scenebuilder.kit.editor.job;

import com.oracle.javafx.scenebuilder.kit.editor.EditorController;
import com.oracle.javafx.scenebuilder.kit.editor.i18n.I18N;
import com.oracle.javafx.scenebuilder.kit.editor.job.v2.ClearSelectionJob;
import com.oracle.javafx.scenebuilder.kit.editor.job.v2.CompositeJob;
import com.oracle.javafx.scenebuilder.kit.editor.job.v2.UpdateSelectionJob;
import com.oracle.javafx.scenebuilder.kit.editor.selection.AbstractSelectionGroup;
import com.oracle.javafx.scenebuilder.kit.editor.selection.ObjectSelectionGroup;
import com.oracle.javafx.scenebuilder.kit.editor.selection.Selection;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMCollection;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMDocument;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMInstance;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMNodes;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMObject;
import com.oracle.javafx.scenebuilder.kit.metadata.util.DesignHierarchyMask;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.Node;

/**
 *
 */
public class DuplicateSelectionJob extends CompositeJob {

    private final static double offset = 10;
    private int numOfDuplicatedObjects = 0;

    public DuplicateSelectionJob(EditorController editorController) {
        super(editorController);
    }

    /*
     * CompositeJob
     */
    @Override
    protected List<Job> makeSubJobs() {
        final List<Job> result = new ArrayList<>();

        if (canDuplicate()) { // (1)

            final Selection selection = getEditorController().getSelection();
            final AbstractSelectionGroup asg = selection.getGroup();
            assert asg instanceof ObjectSelectionGroup; // Because of (1)
            final ObjectSelectionGroup osg = (ObjectSelectionGroup) asg;
            assert osg.hasSingleParent() == true; // Because of (1)
            final FXOMObject targetObject = osg.getAncestor();
            assert targetObject != null; // Because of (1)
            final FXOMDocument targetDocument = getEditorController().getFxomDocument();
            final Map<FXOMObject, FXOMObject> newFxomObjects = new HashMap<>();
            for (FXOMObject selectedObject : osg.getItems()) {
                final FXOMDocument newDocument = FXOMNodes.newDocument(selectedObject);
                newDocument.getFxomRoot().moveToFxomDocument(targetDocument);
                final FXOMObject newObject = newDocument.getFxomRoot();
                newFxomObjects.put(selectedObject, newObject);
            }
            assert newFxomObjects.isEmpty() == false; // Because of (1)

            // Build InsertAsSubComponent jobs
            final DesignHierarchyMask targetMask = new DesignHierarchyMask(targetObject);
            if (targetMask.isAcceptingSubComponent(newFxomObjects.keySet())) {
                result.add(new ClearSelectionJob(getEditorController()));
                for (Map.Entry<FXOMObject, FXOMObject> entry : newFxomObjects.entrySet()) {
                    final FXOMObject selectedFxomObject = entry.getKey();
                    final FXOMObject newFxomObject = entry.getValue();
                    final InsertAsSubComponentJob insertSubJob = new InsertAsSubComponentJob(
                            newFxomObject,
                            targetObject,
                            targetMask.getSubComponentCount(),
                            getEditorController());
                    result.add(insertSubJob);
                    numOfDuplicatedObjects++;
                    final Object selectedSceneGraphObject = selectedFxomObject.getSceneGraphObject();
                    // Relocate duplicated objects if needed
                    if (selectedSceneGraphObject instanceof Node) {
                        final Node selectedNode = (Node) selectedSceneGraphObject;
                        final double newLayoutX = Math.round(selectedNode.getLayoutX() + offset);
                        final double newLayoutY = Math.round(selectedNode.getLayoutY() + offset);
                        assert newFxomObject instanceof FXOMInstance;
                        final RelocateNodeJob relocateSubJob = new RelocateNodeJob(
                                (FXOMInstance) newFxomObject,
                                newLayoutX,
                                newLayoutY,
                                getEditorController());
                        result.add(relocateSubJob);
                    }
                }
                result.add(new UpdateSelectionJob(newFxomObjects.values(), getEditorController()));
            }
        }
        return result;
    }

    @Override
    protected String makeDescription() {
        final String result;
        assert getSubJobs().isEmpty() == false;
        if (numOfDuplicatedObjects == 1) {
            result = makeSingleSelectionDescription();
        } else {
            result = makeMultipleSelectionDescription();
        }

        return result;
    }

    private boolean canDuplicate() {
        final FXOMDocument fxomDocument = getEditorController().getFxomDocument();
        if (fxomDocument == null) {
            return false;
        }
        final Selection selection = getEditorController().getSelection();
        if (selection.isEmpty()) {
            return false;
        }
        final FXOMObject rootObject = fxomDocument.getFxomRoot();
        if (selection.isSelected(rootObject)) {
            return false;
        }
        final AbstractSelectionGroup asg = selection.getGroup();
        if ((asg instanceof ObjectSelectionGroup) == false) {
            return false;
        }
        final ObjectSelectionGroup osg = (ObjectSelectionGroup) asg;
        return osg.hasSingleParent() == true;
    }

    private String makeSingleSelectionDescription() {
        final String result;

        final Job subJob0 = getSubJobs().get(1);// ClearSelectionJob + Insert + UpdateSelectionJob
        assert subJob0 instanceof InsertAsSubComponentJob;
        final InsertAsSubComponentJob insertJob = (InsertAsSubComponentJob) subJob0;
        final FXOMObject newObject = insertJob.getNewObject();
        if (newObject instanceof FXOMInstance) {
            final Object sceneGraphObject = newObject.getSceneGraphObject();
            if (sceneGraphObject != null) {
                result = I18N.getString("label.action.edit.duplicate.1", sceneGraphObject.getClass().getSimpleName());
            } else {
                result = I18N.getString("label.action.edit.duplicate.unresolved");
            }
        } else if (newObject instanceof FXOMCollection) {
            result = I18N.getString("label.action.edit.duplicate.collection");
        } else {
            assert false;
            result = I18N.getString("label.action.edit.duplicate.1", newObject.getClass().getSimpleName());
        }

        return result;
    }

    private String makeMultipleSelectionDescription() {
        return I18N.getString("label.action.edit.duplicate.n", numOfDuplicatedObjects);
    }
}

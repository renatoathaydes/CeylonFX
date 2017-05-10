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
import com.oracle.javafx.scenebuilder.kit.editor.selection.GridSelectionGroup;
import com.oracle.javafx.scenebuilder.kit.editor.selection.ObjectSelectionGroup;
import com.oracle.javafx.scenebuilder.kit.editor.selection.Selection;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMDocument;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMInstance;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMObject;
import com.oracle.javafx.scenebuilder.kit.metadata.util.DesignHierarchyMask;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class UseComputedSizesSelectionJob extends Job {

    private final List<UseComputedSizesObjectJob> subJobs = new ArrayList<>();
    private String description; // Final but constructed lazily

    public UseComputedSizesSelectionJob(EditorController editorController) {
        super(editorController);
        buildSubJobs();
    }

    /*
     * Job
     */
    @Override
    public boolean isExecutable() {
        return subJobs.isEmpty() == false;
    }

    @Override
    public void execute() {
        final FXOMDocument fxomDocument
                = getEditorController().getFxomDocument();
        fxomDocument.beginUpdate();
        for (UseComputedSizesObjectJob subJob : subJobs) {
            subJob.execute();
        }
        fxomDocument.endUpdate();
    }

    @Override
    public void undo() {
        final FXOMDocument fxomDocument
                = getEditorController().getFxomDocument();
        fxomDocument.beginUpdate();
        for (int i = subJobs.size() - 1; i >= 0; i--) {
            subJobs.get(i).undo();
        }
        fxomDocument.endUpdate();
    }

    @Override
    public void redo() {
        final FXOMDocument fxomDocument
                = getEditorController().getFxomDocument();
        fxomDocument.beginUpdate();
        for (UseComputedSizesObjectJob subJob : subJobs) {
            subJob.redo();
        }
        fxomDocument.endUpdate();
    }

    @Override
    public String getDescription() {
        if (description == null) {
            buildDescription();
        }

        return description;
    }

    /*
     * Private
     */
    private void buildSubJobs() {

        final Set<FXOMInstance> candidates = new HashSet<>();
        final Selection selection = getEditorController().getSelection();
        if (selection.getGroup() instanceof ObjectSelectionGroup) {
            final ObjectSelectionGroup osg = (ObjectSelectionGroup) selection.getGroup();
            for (FXOMObject fxomObject : osg.getItems()) {
                if (fxomObject instanceof FXOMInstance) {
                    candidates.add((FXOMInstance) fxomObject);
                }
            }
        } else if (selection.getGroup() instanceof GridSelectionGroup) {
            final GridSelectionGroup gsg = (GridSelectionGroup) selection.getGroup();
            final FXOMObject gridPane = gsg.getParentObject();
            final DesignHierarchyMask mask = new DesignHierarchyMask(gridPane);
            for (int index : gsg.getIndexes()) {
                final FXOMObject constraints;
                switch (gsg.getType()) {
                    case COLUMN:
                        constraints = mask.getColumnConstraintsAtIndex(index);
                        break;
                    case ROW:
                        constraints = mask.getRowConstraintsAtIndex(index);
                        break;
                    default:
                        assert false;
                        return;
                }
                assert constraints instanceof FXOMInstance;
                candidates.add((FXOMInstance) constraints);
            }
        } else {
            assert selection.getGroup() == null :
                    "Add implementation for " + selection.getGroup();
        }

        for (FXOMInstance candidate : candidates) {
            final UseComputedSizesObjectJob subJob
                    = new UseComputedSizesObjectJob(candidate, getEditorController());
            if (subJob.isExecutable()) {
                subJobs.add(subJob);
            }
        }
    }

    private void buildDescription() {
        switch (subJobs.size()) {
            case 0:
                description = "Unexecutable Use Computed Sizes"; // NO18N
                break;
            case 1:
                description = subJobs.get(0).getDescription();
                break;
            default:
                description = makeMultipleSelectionDescription();
                break;
        }
    }

    private String makeMultipleSelectionDescription() {
        final StringBuilder result = new StringBuilder();
        result.append("Use Computed Sizes on ");
        result.append(subJobs.size());
        result.append(" Objects");
        return result.toString();
    }
}

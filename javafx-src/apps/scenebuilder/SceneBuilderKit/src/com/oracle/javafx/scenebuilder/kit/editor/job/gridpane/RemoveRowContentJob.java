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
package com.oracle.javafx.scenebuilder.kit.editor.job.gridpane;

import com.oracle.javafx.scenebuilder.kit.editor.EditorController;
import com.oracle.javafx.scenebuilder.kit.editor.job.BatchJob;
import com.oracle.javafx.scenebuilder.kit.editor.job.DeleteObjectJob;
import com.oracle.javafx.scenebuilder.kit.editor.job.Job;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMDocument;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMInstance;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMObject;
import com.oracle.javafx.scenebuilder.kit.metadata.util.DesignHierarchyMask;
import java.util.List;

/**
 * Job invoked when removing row content.
 */
public class RemoveRowContentJob extends Job {

    private BatchJob subJob;
    private final FXOMObject targetGridPane;
    private final List<Integer> targetIndexes;

    public RemoveRowContentJob(
            final EditorController editorController,
            final FXOMObject targetGridPane,
            final List<Integer> targetIndexes) {
        super(editorController);

        assert targetGridPane != null;
        assert targetIndexes != null;
        this.targetGridPane = targetGridPane;
        this.targetIndexes = targetIndexes;
        buildSubJobs();
    }

    @Override
    public boolean isExecutable() {
        // Remove row content job may be empty 
        // (when removing a row with no content).
        // So we do not invoke subJob.isExecutable() here. 
        return subJob != null;
    }

    @Override
    public void execute() {
        assert isExecutable();
        final FXOMDocument fxomDocument = getEditorController().getFxomDocument();
        fxomDocument.beginUpdate();
        subJob.execute();
        fxomDocument.endUpdate();
    }

    @Override
    public void undo() {
        final FXOMDocument fxomDocument = getEditorController().getFxomDocument();
        fxomDocument.beginUpdate();
        subJob.undo();
        fxomDocument.endUpdate();
    }

    @Override
    public void redo() {
        final FXOMDocument fxomDocument = getEditorController().getFxomDocument();
        fxomDocument.beginUpdate();
        subJob.redo();
        fxomDocument.endUpdate();
    }

    @Override
    public String getDescription() {
        return "Remove Row Content"; //NOI18N
    }

    private void buildSubJobs() {

        // Create sub job
        subJob = new BatchJob(getEditorController(),
                true /* shouldRefreshSceneGraph */, null);

        assert targetGridPane instanceof FXOMInstance;
        assert targetIndexes.isEmpty() == false;
        final DesignHierarchyMask targetGridPaneMask
                = new DesignHierarchyMask(targetGridPane);

        for (int targetIndex : targetIndexes) {
            final List<FXOMObject> children
                    = targetGridPaneMask.getRowContentAtIndex(targetIndex);
            for (FXOMObject child : children) {
                final Job removeChildJob = new DeleteObjectJob(
                        child,
                        getEditorController());
                subJob.addSubJob(removeChildJob);
            }
        }
    }
}

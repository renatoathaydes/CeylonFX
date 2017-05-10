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
package com.oracle.javafx.scenebuilder.kit.editor.job.v2;

import com.oracle.javafx.scenebuilder.kit.editor.EditorController;
import com.oracle.javafx.scenebuilder.kit.editor.job.Job;
import com.oracle.javafx.scenebuilder.kit.editor.selection.Selection;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMDocument;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public abstract class CompositeJob extends Job {

    private final boolean shouldRefreshSceneGraph;
    private final boolean shouldUpdateSelection;
    private List<Job> subJobs;
    private String description;

    public CompositeJob(EditorController editorController) {
        this(editorController, true ,true);
    }
    
    public CompositeJob(EditorController editorController, 
            boolean shouldRefreshSceneGraph, boolean shouldUpdateSelection) {
        super(editorController);
        this.shouldRefreshSceneGraph = shouldRefreshSceneGraph;
        this.shouldUpdateSelection = shouldUpdateSelection;
    }
    
    public List<Job> getSubJobs() {
        if (subJobs == null) {
            subJobs = makeSubJobs();
            assert subJobs != null;
            subJobs = Collections.unmodifiableList(subJobs);
        }
        return subJobs;
    }
    
    /*
     * To be implemented by subclasses
     */
    
    protected abstract List<Job> makeSubJobs();
    protected abstract String makeDescription();
    
    /*
     * Job
     */
    
    @Override
    public boolean isExecutable() {
        return getSubJobs().isEmpty() == false;
    }

    @Override
    public void execute() {
        final Selection selection = getEditorController().getSelection();
        final FXOMDocument fxomDocument = getEditorController().getFxomDocument();
        if (shouldUpdateSelection) {
            selection.beginUpdate();
        }
        if (shouldRefreshSceneGraph) {
            fxomDocument.beginUpdate();
        }
        for (Job subJob : getSubJobs()) {
            subJob.execute();
        }
        if (shouldRefreshSceneGraph) {
            fxomDocument.endUpdate();
        }
        if (shouldUpdateSelection) {
            selection.endUpdate();
        }
    }

    @Override
    public void undo() {
        final Selection selection = getEditorController().getSelection();
        final FXOMDocument fxomDocument = getEditorController().getFxomDocument();
        if (shouldUpdateSelection) {
            selection.beginUpdate();
        }
        if (shouldRefreshSceneGraph) {
            fxomDocument.beginUpdate();
        }
        for (int i = getSubJobs().size()-1; i >= 0; i--) {
            getSubJobs().get(i).undo();
        }
        if (shouldRefreshSceneGraph) {
            fxomDocument.endUpdate();
        }
        if (shouldUpdateSelection) {
            selection.endUpdate();
        }
    }

    @Override
    public void redo() {
        final Selection selection = getEditorController().getSelection();
        final FXOMDocument fxomDocument = getEditorController().getFxomDocument();
        
        if (shouldUpdateSelection) {
            selection.beginUpdate();
        }
        if (shouldRefreshSceneGraph) {
            fxomDocument.beginUpdate();
        }
        for (Job subJob : getSubJobs()) {
            subJob.redo();
        }
        if (shouldRefreshSceneGraph) {
            fxomDocument.endUpdate();
        }
        if (shouldUpdateSelection) {
            selection.endUpdate();
        }
    }
    
    @Override
    public String getDescription() {
        if (description == null) {
            description = makeDescription();
            assert description != null;
        }
        return description;
    }
}

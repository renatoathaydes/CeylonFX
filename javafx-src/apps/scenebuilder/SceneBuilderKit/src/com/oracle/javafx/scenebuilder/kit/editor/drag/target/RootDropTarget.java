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

package com.oracle.javafx.scenebuilder.kit.editor.drag.target;

import com.oracle.javafx.scenebuilder.kit.editor.EditorController;
import com.oracle.javafx.scenebuilder.kit.editor.drag.source.AbstractDragSource;
import com.oracle.javafx.scenebuilder.kit.editor.drag.source.LibraryDragSource;
import com.oracle.javafx.scenebuilder.kit.editor.job.BatchJob;
import com.oracle.javafx.scenebuilder.kit.editor.job.Job;
import com.oracle.javafx.scenebuilder.kit.editor.job.SetDocumentRootJob;
import com.oracle.javafx.scenebuilder.kit.editor.job.UsePredefinedSizeJob;
import com.oracle.javafx.scenebuilder.kit.editor.job.v2.UpdateSelectionJob;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMObject;
import com.oracle.javafx.scenebuilder.kit.metadata.util.DesignHierarchyMask;

/**
 *
 */
public class RootDropTarget extends AbstractDropTarget {

    /*
     * AbstractDropTarget
     */
    
    @Override
    public FXOMObject getTargetObject() {
        return null;
    }

    @Override
    public boolean acceptDragSource(AbstractDragSource dragSource) {
        assert dragSource != null;
        return dragSource.getDraggedObjects().size() == 1;
    }

    @Override
    public Job makeDropJob(AbstractDragSource dragSource, EditorController editorController) {
        assert dragSource != null;
        assert dragSource.getDraggedObjects().size() == 1;
        
        final FXOMObject newRoot = dragSource.getDraggedObjects().get(0);
        
        /*
         * Containers coming from the library are automatically resized.
         */
        final UsePredefinedSizeJob resizeJob;
        if (dragSource instanceof LibraryDragSource) {
            final DesignHierarchyMask mask = new DesignHierarchyMask(newRoot);
            if (mask.needResizeWhenTopElement()) {
                resizeJob = new UsePredefinedSizeJob(editorController, 
                        EditorController.Size.SIZE_DEFAULT, newRoot);
            } else {
                resizeJob = null;
            }
        } else {
            resizeJob = null;
        }
        
        final BatchJob result = new BatchJob(editorController, true, dragSource.makeDropJobDescription());
        result.addSubJob(new SetDocumentRootJob(newRoot, editorController));
        if ((resizeJob != null) && resizeJob.isExecutable()) {
            result.addSubJob(resizeJob);
        }
        
        return result ;
    }
    
    @Override
    public boolean isSelectRequiredAfterDrop() {
        return true;
    }
    
}

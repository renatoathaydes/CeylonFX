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
package com.oracle.javafx.scenebuilder.kit.editor.job.wrap;

import com.oracle.javafx.scenebuilder.kit.editor.EditorController;
import com.oracle.javafx.scenebuilder.kit.editor.job.Job;
import com.oracle.javafx.scenebuilder.kit.editor.job.v2.AddPropertyJob;
import com.oracle.javafx.scenebuilder.kit.editor.job.v2.AddPropertyValueJob;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMInstance;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMObject;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMPropertyC;
import com.oracle.javafx.scenebuilder.kit.metadata.util.DesignHierarchyMask;
import com.oracle.javafx.scenebuilder.kit.metadata.util.DesignHierarchyMask.Accessory;
import com.oracle.javafx.scenebuilder.kit.metadata.util.PropertyName;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

/**
 * Job used to wrap selection in a TabPane.
 */
public class WrapInTabPaneJob extends AbstractWrapInJob {

    public WrapInTabPaneJob(EditorController editorController) {
        super(editorController);
        newContainerClass = TabPane.class;
    }

    @Override
    protected List<Job> wrapInJobs(final Set<FXOMObject> children) {

        final List<Job> jobs = new ArrayList<>();

        final DesignHierarchyMask newContainerMask
                = new DesignHierarchyMask(newContainer);
        assert newContainerMask.isAcceptingSubComponent();

        // Retrieve the new container property name to be used
        final PropertyName newContainerPropertyName
                = newContainerMask.getSubComponentPropertyName();
        // Create the new container property
        final FXOMPropertyC newContainerProperty = new FXOMPropertyC(
                newContainer.getFxomDocument(), newContainerPropertyName);

        // Create the Tab sub container
        final FXOMInstance tabContainer = makeContainerInstance(Tab.class);
        final DesignHierarchyMask tabContainerMask
                = new DesignHierarchyMask(tabContainer);
        assert tabContainerMask.isAcceptingAccessory(Accessory.CONTENT);

        // Retrieve the Tab sub container property name to be used
        final PropertyName tabContainerPropertyName
                = new PropertyName("content"); //NOI18N
        // Create the Tab sub container property
        final FXOMPropertyC tabContainerProperty = new FXOMPropertyC(
                tabContainer.getFxomDocument(), tabContainerPropertyName);

        // Add the Tab sub container to the new container
        final Job addTabValueJob = new AddPropertyValueJob(
                tabContainer,
                newContainerProperty,
                -1,
                getEditorController());
        jobs.add(addTabValueJob);

        //------------------------------------------------------------------
        // The selection is multiple :
        // - we set the tab container CONTENT property to a new AnchorPane sub container
        // - we update the children bounds and add them to the new AnchorPane sub container
        //------------------------------------------------------------------
        if (children.size() > 1) {

            // Create the AnchorPane sub container
            final FXOMInstance subContainer = makeContainerInstance(AnchorPane.class);
            final DesignHierarchyMask subContainerMask
                    = new DesignHierarchyMask(subContainer);

            // Retrieve the AnchorPane sub container property name to be used
            final PropertyName subContainerPropertyName
                    = subContainerMask.getSubComponentPropertyName();
            // Create the sub container property
            final FXOMPropertyC subContainerProperty = new FXOMPropertyC(
                    subContainer.getFxomDocument(), subContainerPropertyName);

            // Add the AnchorPane sub container to the Tab sub container
            final Job addValueJob = new AddPropertyValueJob(
                    subContainer,
                    tabContainerProperty,
                    -1,
                    getEditorController());
            jobs.add(addValueJob);

            // Update children bounds before adding them to the sub container
            assert subContainerMask.isFreeChildPositioning();
            final List<Job> modifyChildrenLayoutJobs
                    = modifyChildrenLayoutJobs(children);
            jobs.addAll(modifyChildrenLayoutJobs);

            // Add the children to the sub container
            final List<Job> addChildrenJobs
                    = addChildrenToPropertyJobs(subContainerProperty, children);
            jobs.addAll(addChildrenJobs);

            // Add the sub container property to the sub container instance
            assert subContainerProperty.getParentInstance() == null;
            final Job addPropertyJob = new AddPropertyJob(
                    subContainerProperty,
                    subContainer,
                    -1, getEditorController());
            jobs.add(addPropertyJob);
        }//
        //------------------------------------------------------------------
        // The selection is single :
        // - we set the tab container CONTENT property to the single child
        //------------------------------------------------------------------
        else {
            // Update children before adding them to the new container
            final List<Job> modifyChildrenJobs = modifyChildrenJobs(children);
            jobs.addAll(modifyChildrenJobs);

            // Add the children to the Tab sub container
            final List<Job> addChildrenJobs
                    = addChildrenToPropertyJobs(tabContainerProperty, children);
            jobs.addAll(addChildrenJobs);
        }

        // Add the Tab sub container property to the tab container instance
        assert tabContainerProperty.getParentInstance() == null;
        final Job addTabContainerPropertyJob = new AddPropertyJob(
                tabContainerProperty,
                tabContainer,
                -1, getEditorController());
        jobs.add(addTabContainerPropertyJob);

        // Add the new container property to the new container instance
        assert newContainerProperty.getParentInstance() == null;
        final Job addNewContainerPropertyJob = new AddPropertyJob(
                newContainerProperty,
                newContainer,
                -1, getEditorController());
        jobs.add(addNewContainerPropertyJob);

        return jobs;
    }

    @Override
    protected List<Job> modifyChildrenJobs(final Set<FXOMObject> children) {
        return Collections.emptyList();
    }

    @Override
    protected void modifyContainer(final Set<FXOMObject> children) {
    }
}

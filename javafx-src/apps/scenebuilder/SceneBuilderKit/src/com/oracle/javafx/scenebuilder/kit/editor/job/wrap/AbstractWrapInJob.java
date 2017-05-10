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
import com.oracle.javafx.scenebuilder.kit.editor.job.BatchJob;
import com.oracle.javafx.scenebuilder.kit.editor.job.DeleteObjectJob;
import com.oracle.javafx.scenebuilder.kit.editor.job.Job;
import com.oracle.javafx.scenebuilder.kit.editor.job.JobUtils;
import com.oracle.javafx.scenebuilder.kit.editor.job.ModifyFxControllerJob;
import com.oracle.javafx.scenebuilder.kit.editor.job.ModifyObjectJob;
import com.oracle.javafx.scenebuilder.kit.editor.job.SetDocumentRootJob;
import com.oracle.javafx.scenebuilder.kit.editor.job.ToggleFxRootJob;
import com.oracle.javafx.scenebuilder.kit.editor.job.v2.AddPropertyValueJob;
import com.oracle.javafx.scenebuilder.kit.editor.selection.AbstractSelectionGroup;
import com.oracle.javafx.scenebuilder.kit.editor.selection.ObjectSelectionGroup;
import com.oracle.javafx.scenebuilder.kit.editor.selection.Selection;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMDocument;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMInstance;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMObject;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMPropertyC;
import com.oracle.javafx.scenebuilder.kit.metadata.util.DesignHierarchyMask;
import com.oracle.javafx.scenebuilder.kit.metadata.util.PropertyName;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Region;

/**
 * Main class used for the wrap jobs.
 */
public abstract class AbstractWrapInJob extends Job {

    protected Class<?> newContainerClass;
    private BatchJob batchJob;
    private AbstractSelectionGroup selectionSnapshot;
    protected FXOMInstance oldContainer, newContainer;

    public AbstractWrapInJob(EditorController editorController) {
        super(editorController);
    }

    public static AbstractWrapInJob getWrapInJob(
            EditorController editorController,
            Class<? extends Parent> wrappingClass) {

        assert EditorController.getClassesSupportingWrapping().contains(wrappingClass);
        final AbstractWrapInJob job;
        if (wrappingClass == javafx.scene.layout.AnchorPane.class) {
            job = new WrapInAnchorPaneJob(editorController);
        } else if (wrappingClass == javafx.scene.layout.GridPane.class) {
            job = new WrapInGridPaneJob(editorController);
        } else if (wrappingClass == javafx.scene.Group.class) {
            job = new WrapInGroupJob(editorController);
        } else if (wrappingClass == javafx.scene.layout.HBox.class) {
            job = new WrapInHBoxJob(editorController);
        } else if (wrappingClass == javafx.scene.layout.Pane.class) {
            job = new WrapInPaneJob(editorController);
        } else if (wrappingClass == javafx.scene.control.ScrollPane.class) {
            job = new WrapInScrollPaneJob(editorController);
        } else if (wrappingClass == javafx.scene.control.SplitPane.class) {
            job = new WrapInSplitPaneJob(editorController);
        } else if (wrappingClass == javafx.scene.layout.StackPane.class) {
            job = new WrapInStackPaneJob(editorController);
        } else if (wrappingClass == javafx.scene.control.TabPane.class) {
            job = new WrapInTabPaneJob(editorController);
        } else if (wrappingClass == javafx.scene.control.TitledPane.class) {
            job = new WrapInTitledPaneJob(editorController);
        } else if (wrappingClass == javafx.scene.control.ToolBar.class) {
            job = new WrapInToolBarJob(editorController);
        } else {
            assert wrappingClass == javafx.scene.layout.VBox.class; // Because of (1)
            job = new WrapInVBoxJob(editorController);
        }
        return job;
    }

    @Override
    public boolean isExecutable() {
        return WrapJobUtils.canWrapIn(getEditorController());
    }

    @Override
    public void execute() {
        final FXOMDocument fxomDocument = getEditorController().getFxomDocument();
        final Selection selection = getEditorController().getSelection();

        assert isExecutable();
        buildSubJobs();

        try {
            selectionSnapshot = selection.getGroup().clone();
        } catch (CloneNotSupportedException x) {
            // Emergency code
            throw new RuntimeException(x);
        }
        selection.clear();
        selection.beginUpdate();
        fxomDocument.beginUpdate();
        batchJob.execute();
        fxomDocument.endUpdate();
        selection.select(newContainer);
        selection.endUpdate();
    }

    @Override
    public void undo() {
        assert batchJob != null;
        final FXOMDocument fxomDocument = getEditorController().getFxomDocument();
        final Selection selection = getEditorController().getSelection();

        selection.beginUpdate();
        fxomDocument.beginUpdate();
        batchJob.undo();
        fxomDocument.endUpdate();
        selection.select(selectionSnapshot);
        selection.endUpdate();
    }

    @Override
    public void redo() {
        assert batchJob != null;
        final FXOMDocument fxomDocument = getEditorController().getFxomDocument();
        final Selection selection = getEditorController().getSelection();

        selection.clear();
        selection.beginUpdate();
        fxomDocument.beginUpdate();
        batchJob.redo();
        fxomDocument.endUpdate();
        selection.select(newContainer);
        selection.endUpdate();
    }

    @Override
    public String getDescription() {
        return "Wrap in " + newContainerClass.getSimpleName();
    }

    protected void buildSubJobs() {

        assert isExecutable(); // (1)

        // Create batch job
        batchJob = new BatchJob(getEditorController());

        final Selection selection = getEditorController().getSelection();
        final AbstractSelectionGroup asg = selection.getGroup();
        assert asg instanceof ObjectSelectionGroup; // Because of (1)
        final ObjectSelectionGroup osg = (ObjectSelectionGroup) asg;

        // Retrieve the current container
        oldContainer = (FXOMInstance) osg.getAncestor();

        // Retrieve the children to be wrapped
        final Set<FXOMObject> children = osg.getItems();

        // Create the new container
        newContainer = makeContainerInstance();

        //==================================================================
        // STEP #1 UPDATE THE NEW CONTAINER
        //==================================================================
        // If the target object is NOT the FXOM root :
        // - we update the new container bounds and add it to the current container
        // - we remove the children from the current container
        //------------------------------------------------------------------
        if (oldContainer != null) {

            // Retrieve the current container property name in use
            final PropertyName oldContainerPropertyName
                    = WrapJobUtils.getContainerPropertyName(oldContainer, children);
            // Retrieve the current container property (already defined and not null)
            final FXOMPropertyC oldContainerProperty
                    = (FXOMPropertyC) oldContainer.getProperties().get(oldContainerPropertyName);
            assert oldContainerProperty != null
                    && oldContainerProperty.getParentInstance() != null;

            // Update the new container before adding it to the old container
            final DesignHierarchyMask oldContainerMask
                    = new DesignHierarchyMask(oldContainer);
            if (oldContainerMask.isFreeChildPositioning()
                    && Region.class.isAssignableFrom(newContainer.getDeclaredClass())) { // Do not include Group new container
                modifyContainerLayout(children);
            }
            modifyContainer(children);

            // Add the new container to the current container
            final int newContainerIndex = getIndex(oldContainer, children);
            final Job newContainerAddValueJob = new AddPropertyValueJob(
                    newContainer,
                    oldContainerProperty,
                    newContainerIndex, getEditorController());
            batchJob.addSubJob(newContainerAddValueJob);

            // Remove children from the current container
            final List<Job> deleteChildrenJobs = deleteChildrenJobs(children);
            batchJob.addSubJobs(deleteChildrenJobs);
        } //
        //------------------------------------------------------------------
        // If the target object is the FXOM root :
        // - we update the document root with the new container
        //------------------------------------------------------------------
        else {
            assert children.size() == 1; // Wrap the single root node
            final FXOMObject rootObject = children.iterator().next();
            assert rootObject instanceof FXOMInstance;
            boolean isFxRoot = ((FXOMInstance) rootObject).isFxRoot();
            final String fxController = rootObject.getFxController();
            // First remove the fx:controller/fx:root from the old root object
            if (isFxRoot) {
                final ToggleFxRootJob fxRootJob = new ToggleFxRootJob(getEditorController());
                batchJob.addSubJob(fxRootJob);
            }
            if (fxController != null) {
                final ModifyFxControllerJob fxControllerJob
                        = new ModifyFxControllerJob(rootObject, null, getEditorController());
                batchJob.addSubJob(fxControllerJob);
            }
            // Then set the new container as root object
            final Job setDocumentRoot = new SetDocumentRootJob(
                    newContainer, getEditorController());
            batchJob.addSubJob(setDocumentRoot);
            // Finally add the fx:controller/fx:root to the new root object
            if (isFxRoot) {
                final ToggleFxRootJob fxRootJob = new ToggleFxRootJob(getEditorController());
                batchJob.addSubJob(fxRootJob);
            }
            if (fxController != null) {
                final ModifyFxControllerJob fxControllerJob
                        = new ModifyFxControllerJob(newContainer, fxController, getEditorController());
                batchJob.addSubJob(fxControllerJob);
            }
        }

        //==================================================================
        // STEP #2 UPDATE THE NEW CONTAINER CHILDREN
        //==================================================================
        // This step depends on the new container property 
        // (either either the SUB COMPONENT or the CONTENT property)
        //------------------------------------------------------------------
        batchJob.addSubJobs(wrapInJobs(children));
    }

    protected List<Job> addChildrenToPropertyJobs(
            final FXOMPropertyC containerProperty,
            final Collection<FXOMObject> children) {

        final List<Job> jobs = new ArrayList<>();
        int index = 0;
        for (FXOMObject child : children) {
            assert child instanceof FXOMInstance;
            final Job addValueJob = new AddPropertyValueJob(
                    child,
                    containerProperty,
                    index++,
                    getEditorController());
            jobs.add(addValueJob);
        }
        return jobs;
    }

    protected List<Job> deleteChildrenJobs(final Set<FXOMObject> children) {

        final List<Job> jobs = new ArrayList<>();
        for (FXOMObject child : children) {
            assert child instanceof FXOMInstance;
            final Job deleteObjectJob = new DeleteObjectJob(
                    child, getEditorController());
            jobs.add(deleteObjectJob);
        }
        return jobs;
    }

    /**
     * Used to wrap the children in the new container.
     * May use either the SUB COMPONENT or the CONTENT property.
     *
     * @param children
     * @return
     */
    protected abstract List<Job> wrapInJobs(final Set<FXOMObject> children);

    /**
     * Used to modify the children before adding them to this container.
     *
     * @param children The children to be modified.
     * @return A list of jobs.
     */
    protected abstract List<Job> modifyChildrenJobs(final Set<FXOMObject> children);

    /**
     * Used to modify this container before adding it to the specified container.
     *
     * Note that unlike the modifyChildrenJobs method, we must not use any job
     * here but directly set the properties.
     * Indeed, the new container is not yet part of the model.
     *
     * @param children The children of this container.
     */
    protected abstract void modifyContainer(final Set<FXOMObject> children);

    /**
     * Used to modify the children layout properties.
     *
     * @param children The children.
     * @return
     */
    protected List<Job> modifyChildrenLayoutJobs(final Set<FXOMObject> children) {

        final List<Job> jobs = new ArrayList<>();
        final Bounds unionOfBounds = WrapJobUtils.getUnionOfBounds(children);

        for (FXOMObject child : children) {
            assert child.getSceneGraphObject() instanceof Node;
            final Node childNode = (Node) child.getSceneGraphObject();
            final Bounds childBounds = childNode.getLayoutBounds();

            final Point2D point = childNode.localToParent(
                    childBounds.getMinX(), childBounds.getMinY());
            double layoutX = point.getX() - unionOfBounds.getMinX();
            double layoutY = point.getY() - unionOfBounds.getMinY();
            final ModifyObjectJob modifyLayoutX = WrapJobUtils.modifyObjectJob(
                    (FXOMInstance) child, "layoutX", layoutX, getEditorController());
            jobs.add(modifyLayoutX);
            final ModifyObjectJob modifyLayoutY = WrapJobUtils.modifyObjectJob(
                    (FXOMInstance) child, "layoutY", layoutY, getEditorController());
            jobs.add(modifyLayoutY);
        }
        return jobs;
    }

    /**
     * Used to modify the container layout properties.
     *
     * @param children The children.
     */
    protected void modifyContainerLayout(final Set<FXOMObject> children) {

        final Bounds unionOfBounds = WrapJobUtils.getUnionOfBounds(children);
        JobUtils.setLayoutX(newContainer, Node.class, unionOfBounds.getMinX());
        JobUtils.setLayoutY(newContainer, Node.class, unionOfBounds.getMinY());
//            JobUtils.setMinHeight(newContainer, Region.class, unionOfBounds.getHeight());
//            JobUtils.setMinWidth(newContainer, Region.class, unionOfBounds.getMinY());
    }

    protected FXOMInstance makeContainerInstance(final Class<?> containerClass) {
        // Create new container instance
        final FXOMDocument newDocument = new FXOMDocument();
        final FXOMInstance result = new FXOMInstance(newDocument, containerClass);
        newDocument.setFxomRoot(result);
        result.moveToFxomDocument(getEditorController().getFxomDocument());

        return result;
    }

    private FXOMInstance makeContainerInstance() {
        return makeContainerInstance(newContainerClass);
    }

    /**
     * Returns the index to be used in order to add the new container
     * to the current container.
     *
     * @param container
     * @param fxomObjects
     * @return
     */
    private int getIndex(final FXOMInstance container, final Set<FXOMObject> fxomObjects) {
        final DesignHierarchyMask mask = new DesignHierarchyMask(container);
        if (mask.isAcceptingSubComponent() == false) {
            return -1;
        }
        // Use the smaller index of the specified FXOM objects
        final Iterator<FXOMObject> iterator = fxomObjects.iterator();
        assert iterator.hasNext();
        int result = iterator.next().getIndexInParentProperty();
        while (iterator.hasNext()) {
            int index = iterator.next().getIndexInParentProperty();
            if (index < result) {
                result = index;
            }
        }
        return result;
    }
}

/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package javafx.embed.swing;

import java.io.UnsupportedEncodingException;

import java.util.Collections;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.sun.javafx.embed.EmbeddedSceneDSInterface;
import com.sun.javafx.embed.HostDragStartListener;
import javafx.scene.input.TransferMode;

import com.sun.javafx.embed.EmbeddedSceneInterface;
import com.sun.javafx.embed.EmbeddedSceneDTInterface;
import com.sun.javafx.tk.Toolkit;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import java.awt.Point;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceAdapter;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * An utility class to connect DnD mechanism of Swing and FX.
 */
final class SwingDnD {

    private final Transferable dndTransferable = new DnDTransferable();

    private final DragSource dragSource;
    private final DragSourceListener dragSourceListener;

    // swingDragSource and fxDropTarget are used when DnD is initiated from
    // Swing or external process, i.e. this SwingDnD is used as a drop target
    private SwingDragSource swingDragSource;
    private EmbeddedSceneDTInterface fxDropTarget;

    // fxDragSource is used when DnD is initiated from FX, i.e. this
    // SwingDnD acts as a drag source
    private EmbeddedSceneDSInterface fxDragSource;

    private MouseEvent me;

    SwingDnD(final JComponent comp, final EmbeddedSceneInterface embeddedScene) {

        comp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                storeMouseEvent(me);
            }
            @Override
            public void mouseDragged(MouseEvent me) {
                storeMouseEvent(me);
            }
            @Override
            public void mousePressed(MouseEvent me) {
                storeMouseEvent(me);
            }
            @Override
            public void mouseReleased(MouseEvent me) {
                storeMouseEvent(me);
            }
        });

        dragSource = new DragSource();
        dragSourceListener = new DragSourceAdapter() {
            @Override
            public void dragDropEnd(final DragSourceDropEvent dsde) {
                assert fxDragSource != null;
                try {
                    fxDragSource.dragDropEnd(dropActionToTransferMode(dsde.getDropAction()));
                } finally {
                    fxDragSource = null;
                }
            }
        };

        DropTargetListener dtl = new DropTargetAdapter() {
            @Override
            public void dragEnter(final DropTargetDragEvent e) {
                // This is a temporary workaround for JDK-8027913
                if ((swingDragSource != null) || (fxDropTarget != null)) {
                    return;
                }

                assert swingDragSource == null;
                swingDragSource = new SwingDragSource();
                swingDragSource.updateContents(e);

                assert fxDropTarget == null;
                // Cache the Transferable data in advance, as it cannot be
                // queried from drop(). See comments in dragOver() and in
                // drop() below
                fxDropTarget = embeddedScene.createDropTarget();

                final Point orig = e.getLocation();
                final Point screen = new Point(orig);
                SwingUtilities.convertPointToScreen(screen, comp);
                final TransferMode dr = fxDropTarget.handleDragEnter(
                        orig.x, orig.y, screen.x, screen.y,
                        dropActionToTransferMode(e.getDropAction()), swingDragSource);
                applyDragResult(dr, e);
            }

            @Override
            public void dragExit(final DropTargetEvent e) {
                assert swingDragSource != null;
                assert fxDropTarget != null;
                try {
                    fxDropTarget.handleDragLeave();
                } finally {
                    endDnD();
                }
            }

            @Override
            public void dragOver(final DropTargetDragEvent e) {
                assert swingDragSource != null;
                // We cache Transferable data in advance, as we can't fetch
                // it from drop(), see comments in drop() below. However,
                // caching in every dragOver() is too expensive and also for
                // some reason has a weird side-effect: e.acceptDrag() is
                // ignored, and no drops are possible. So the workaround is
                // to cache all the data in dragEnter() only
                //
                // swingDragSource.updateContents(e);

                assert fxDropTarget != null;
                final Point orig = e.getLocation();
                final Point screen = new Point(orig);
                SwingUtilities.convertPointToScreen(screen, comp);
                final TransferMode dr = fxDropTarget.handleDragOver(
                        orig.x, orig.y, screen.x, screen.y,
                        dropActionToTransferMode(e.getDropAction()));
                applyDragResult(dr, e);
            }

            @Override
            public void drop(final DropTargetDropEvent e) {
                assert swingDragSource != null;
                // Don't call updateContents() from drop(). In AWT, it is possible to
                // get data from the Transferable object in drop() only after the drop
                // has been accepted. Here we first let FX handle drop(), then accept
                // or reject AWT drop based the result. So instead of querying the
                // Transferable object, we use data from swingDragSource, which was
                // cached in dragEnter(), but not in dragOver(), see comments in
                // dragOver() above
                //
                // swingDragSource.updateContents(e);

                final Point orig = e.getLocation();
                final Point screen = new Point(orig);
                SwingUtilities.convertPointToScreen(screen, comp);

                assert fxDropTarget != null;
                try {
                    final TransferMode dropResult = fxDropTarget.handleDragDrop(
                            orig.x, orig.y, screen.x, screen.y,
                            dropActionToTransferMode(e.getDropAction()));
                    applyDropResult(dropResult, e);
                    e.dropComplete(dropResult != null);
                } finally {
                    endDnD();
                }
            }
        };
        comp.setDropTarget(new DropTarget(comp,
                DnDConstants.ACTION_COPY | DnDConstants.ACTION_MOVE | DnDConstants.ACTION_LINK, dtl));

    }

    void addNotify() {
        dragSource.addDragSourceListener(dragSourceListener);
    }

    void removeNotify() {
        // RT-22049: Multi-JFrame/JFXPanel app leaks JFXPanels
        // Don't forget to unregister drag source listener!
        dragSource.removeDragSourceListener(dragSourceListener);
    }

    HostDragStartListener getDragStartListener() {
        return new HostDragStartListener() {
            @Override
            public void dragStarted(final EmbeddedSceneDSInterface dragSource,
                                    final TransferMode dragAction)
            {
                assert Toolkit.getToolkit().isFxUserThread();
                assert dragSource != null;
                
                // The method is called from FX Scene just before entering
                // nested event loop servicing DnD events.
                // It should initialize DnD in AWT EDT.
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        assert fxDragSource == null;
                        assert swingDragSource == null;
                        assert fxDropTarget == null;
                        
                        fxDragSource = dragSource;
                        startDrag(me, dndTransferable, dragSource.
                                getSupportedActions(), dragAction);
                    }
                });
            }
        };
    }

    private void startDrag(final MouseEvent e, final Transferable t,
                                  final Set<TransferMode> sa,
                                  final TransferMode dragAction)
    {
        assert sa.contains(dragAction);
        // This is a replacement for the default AWT drag gesture recognizer.
        // Not sure DragGestureRecognizer was ever supposed to be used this way.
        final class StubDragGestureRecognizer extends DragGestureRecognizer {
            StubDragGestureRecognizer(DragSource ds) {
                super(ds, e.getComponent());
                setSourceActions(transferModesToDropActions(sa));
                appendEvent(e);
            }
            @Override
            protected void registerListeners() {
            }
            @Override
            protected void unregisterListeners() {
            }
        }

        final Point pt = new Point(e.getX(), e.getY());
        final int action = transferModeToDropAction(dragAction);
        final DragGestureRecognizer dgs = new StubDragGestureRecognizer(dragSource);
        final List<InputEvent> events =
                Arrays.asList(new InputEvent[] { dgs.getTriggerEvent() });
        final DragGestureEvent dse = new DragGestureEvent(dgs, action, pt, events);
        dse.startDrag(null, t);
    }

    private void endDnD() {
        assert swingDragSource != null;
        assert fxDropTarget != null;
        fxDropTarget = null;
        swingDragSource = null;
    }

    private void storeMouseEvent(final MouseEvent me) {
        this.me = me;
    }

    private void applyDragResult(final TransferMode dragResult,
                                 final DropTargetDragEvent e)
    {
        if (dragResult == null) {
            e.rejectDrag();
        } else {
            e.acceptDrag(transferModeToDropAction(dragResult));
        }
    }

    private void applyDropResult(final TransferMode dropResult,
                                 final DropTargetDropEvent e)
    {
        if (dropResult == null) {
            e.rejectDrop();
        } else {
            e.acceptDrop(transferModeToDropAction(dropResult));
        }
    }

    static TransferMode dropActionToTransferMode(final int dropAction) {
        switch (dropAction) {
            case DnDConstants.ACTION_COPY:
                return TransferMode.COPY;
            case DnDConstants.ACTION_MOVE:
                return TransferMode.MOVE;
            case DnDConstants.ACTION_LINK:
                return TransferMode.LINK;
            case DnDConstants.ACTION_NONE:
                return null;
            default:
                throw new IllegalArgumentException();
        }
    }

    static int transferModeToDropAction(final TransferMode tm) {
        switch (tm) {
            case COPY:
                return DnDConstants.ACTION_COPY;
            case MOVE:
                return DnDConstants.ACTION_MOVE;
            case LINK:
                return DnDConstants.ACTION_LINK;
            default:
                throw new IllegalArgumentException();
        }
    }

    static Set<TransferMode> dropActionsToTransferModes(
            final int dropActions)
    {
        final Set<TransferMode> tms = EnumSet.noneOf(TransferMode.class);
        if ((dropActions & DnDConstants.ACTION_COPY) != 0) {
            tms.add(TransferMode.COPY);
        }
        if ((dropActions & DnDConstants.ACTION_MOVE) != 0) {
            tms.add(TransferMode.MOVE);
        }
        if ((dropActions & DnDConstants.ACTION_LINK) != 0) {
            tms.add(TransferMode.LINK);
        }
        return Collections.unmodifiableSet(tms);
    }

    static int transferModesToDropActions(final Set<TransferMode> tms) {
        int dropActions = DnDConstants.ACTION_NONE;
        for (TransferMode tm : tms) {
            dropActions |= transferModeToDropAction(tm);
        }
        return dropActions;
    }

    // Transferable wrapper over FX dragboard. All the calls are
    // forwarded to FX and executed on the FX event thread.
    private final class DnDTransferable implements Transferable {

        @Override
        public Object getTransferData(final DataFlavor flavor)
                throws UnsupportedEncodingException
        {
            assert fxDragSource != null;
            assert SwingUtilities.isEventDispatchThread();

            String mimeType = DataFlavorUtils.getFxMimeType(flavor);
            return DataFlavorUtils.adjustFxData(
                    flavor, fxDragSource.getData(mimeType));
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            assert fxDragSource != null;
            assert SwingUtilities.isEventDispatchThread();

            final String mimeTypes[] = fxDragSource.getMimeTypes();

            final ArrayList<DataFlavor> flavors =
                    new ArrayList<DataFlavor>(mimeTypes.length);
            for (String mime : mimeTypes) {
                DataFlavor flavor = null;
                try {
                    flavor = new DataFlavor(mime);
                } catch (ClassNotFoundException e) {
                    // FIXME: what to do?
                    continue;
                }
                flavors.add(flavor);
            }
            return flavors.toArray(new DataFlavor[0]);
        }

        @Override
        public boolean isDataFlavorSupported(final DataFlavor flavor) {
            assert fxDragSource != null;
            assert SwingUtilities.isEventDispatchThread();

            return fxDragSource.isMimeTypeAvailable(
                    DataFlavorUtils.getFxMimeType(flavor));
        }
    }
}

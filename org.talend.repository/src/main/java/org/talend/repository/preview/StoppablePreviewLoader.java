// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.repository.preview;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.internal.dialogs.EventLoopProgressMonitor;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.repository.i18n.Messages;
import org.talend.repository.preview.PreviewHandlerEvent.TYPE;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 * @param <R> result of preview
 */
public abstract class StoppablePreviewLoader<R> {

    private Label infoLabel;

    private IPreviewHandlerListener previewHandlerListener;

    private EventLoopProgressMonitor monitorWrap;

    protected boolean breakInfiniteLoop;

    private AsynchronousPreviewHandler<R> previewHandler;

    /**
     * DOC amaumont StoppablePreviewLoader constructor comment.
     * 
     * @param xmlFilePreview
     * @param infoLabel
     */
    public StoppablePreviewLoader(AsynchronousPreviewHandler<R> previewHandler, Label infoLabel) {
        this.infoLabel = infoLabel;
        this.previewHandler = previewHandler;
    }

    /**
     * DOC amaumont Comment method "load".
     * 
     * @param processDescription
     */
    public void load(final ProcessDescription processDescription) {

        if (previewHandler != null && previewHandlerListener == null) {
            previewHandlerListener = new IPreviewHandlerListener<R>() {

                public void handleEvent(final PreviewHandlerEvent<R> event) {

                    infoLabel.getDisplay().asyncExec(new Runnable() {

                        public void run() {

                            executePreviewHandlerEvent(event);

                        }
                    });

                }

            };
        }

        previewHandler.addListener(previewHandlerListener);

        infoLabel.getDisplay().syncExec(new Runnable() {

            public void run() {
                previewHandler.launchPreview(processDescription, "FILE_XML"); //$NON-NLS-1$
            }

        });

    }

    /**
     * DOC amaumont Comment method "executePreviewHandlerEvent".
     * 
     * @param event
     */
    private void executePreviewHandlerEvent(final PreviewHandlerEvent<R> event) {
        final PreviewHandlerEvent.TYPE eventType = event.getType();
        final AsynchronousPreviewHandler<R> source = event.getSource();

        if (eventType == PreviewHandlerEvent.TYPE.PREVIEW_STARTED) {

            final IRunnableWithProgress op = new IRunnableWithProgress() {

                public void run(final IProgressMonitor monitor) {
                    infoLabel.getDisplay().asyncExec(new Runnable() {

                        public void run() {

                            monitorWrap = new EventLoopProgressMonitor(monitor);

                            monitorWrap.beginTask("Loading preview...", IProgressMonitor.UNKNOWN); //$NON-NLS-1$

                            breakInfiniteLoop = false;

                            while (true) {
                                if (monitorWrap.isCanceled() || breakInfiniteLoop) {
                                    break;
                                }
                            }

                            if (monitorWrap.isCanceled()) {
                                source.stopPreviewProcess();
                            } else {
                                monitorWrap.done();
                            }

                        }
                    });
                }
            };

            infoLabel.getDisplay().syncExec(new Runnable() {

                public void run() {

                    try {
                        new ProgressMonitorDialog(infoLabel.getShell()).run(true, true, op);
                    } catch (InvocationTargetException e) {
                        ExceptionHandler.process(e);
                    } catch (InterruptedException e) {
                        ExceptionHandler.process(e);
                    }
                }

            });

        } else if (eventType == PreviewHandlerEvent.TYPE.PREVIEW_INTERRUPTED) {

            breakInfiniteLoop = true;

            infoLabel.setText("   " + "Preview interrupted");

        } else if (eventType == PreviewHandlerEvent.TYPE.PREVIEW_ENDED) {

            breakInfiniteLoop = true;

            infoLabel.setText("   " + Messages.getString("FileStep2.previewIsDone"));
            infoLabel.getParent().layout();
            R result = source.getResult();
            // refresh TablePreview on this step
            previewEnded(result);

        } else if (eventType == PreviewHandlerEvent.TYPE.PREVIEW_IN_ERROR) {

            breakInfiniteLoop = true;

            final CoreException e = event.getException();
            previewInError(e);

        }
    }

    /**
     * DOC amaumont Comment method "previewEnded".
     * 
     * @param xmlArrayAsynchronousPreviewHandler<XmlArray>
     */
    protected abstract void previewEnded(R result);

    /**
     * DOC amaumont Comment method "previewInError".
     * 
     * @param e
     */
    public abstract void previewInError(CoreException e);

}

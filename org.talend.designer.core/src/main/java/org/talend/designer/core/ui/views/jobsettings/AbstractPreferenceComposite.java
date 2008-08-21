// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.views.jobsettings;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElement;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.ui.AbstractMultiPageTalendEditor;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.editor.AbstractTalendEditor;
import org.talend.designer.core.ui.views.properties.MultipleThreadDynamicComposite;
import org.talend.designer.core.ui.views.statsandlogs.StatsAndLogsComposite;
import org.talend.designer.core.ui.views.statsandlogs.StatsAndLogsViewHelper;
import org.talend.designer.runprocess.ItemCacheManager;

/**
 * Add buttons for loading and saving values between preference page and job view.
 */
public abstract class AbstractPreferenceComposite extends MultipleThreadDynamicComposite {

    Button reloadBtn;

    Button saveBtn;

    String dialogTitle;

    Button applyToChildrenJob;

    private List<INode> tRunJobNodes;

    private static final String TRUN_JOB = "tRunJob"; //$NON-NLS-1$

    private static final String PROCESS = "PROCESS";

    /**
     * DOC chuang AbstractPreferenceComposite constructor comment.
     * 
     * @param parentComposite
     * @param styles
     * @param section
     * @param element
     * @param isCompactView
     */
    public AbstractPreferenceComposite(Composite parentComposite, int styles, EComponentCategory section, Element element,
            boolean isCompactView) {
        super(parentComposite, styles, section, element, isCompactView);
    }

    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

    protected abstract boolean needApplyToChildren();

    @Override
    public void addComponents(boolean forceRedraw, boolean reInitialize, int height) {
        if (forceRedraw || isNeedRedraw()) {
            disposeChildren();
            Composite topComposite = new Composite(getComposite(), SWT.NONE);

            if (hasRunJobNode(false) && needApplyToChildren()) {
                topComposite.setLayout(new GridLayout(3, false));
            } else {
                topComposite.setLayout(new GridLayout(2, false));
            }

            topComposite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
            reloadBtn = new Button(topComposite, SWT.PUSH);
            reloadBtn.setText(Messages.getString("StatsAndLogsComposite.Reload")); //$NON-NLS-1$
            reloadBtn.setToolTipText(Messages.getString("StatsAndLogsComposite.ReloadToolTipText")); //$NON-NLS-1$

            saveBtn = new Button(topComposite, SWT.PUSH);
            saveBtn.setText(Messages.getString("StatsAndLogsComposite.Save")); //$NON-NLS-1$
            saveBtn.setToolTipText(Messages.getString("StatsAndLogsComposite.SaveToolTipText")); //$NON-NLS-1$

            if (hasRunJobNode(false) && needApplyToChildren()) {
                applyToChildrenJob = new Button(topComposite, SWT.PUSH);
                applyToChildrenJob.setText("Apply to sub jobs");
                applyToChildrenJob.setToolTipText("Apply to sub jobs");
            }

            Point initialSize = topComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT);

            addButtonListeners();
            refresh();
            super.addComponents(true, false, initialSize.y + ITabbedPropertyConstants.VSPACE);
        }
    }

    /**
     * ftang Comment method "addButtonListeners".
     */
    private void addButtonListeners() {
        reloadBtn.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                onReloadButtonClick();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });

        saveBtn.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                onSaveButtonClick();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

        });

        if (hasRunJobNode(false) && needApplyToChildren()) {

            applyToChildrenJob.addSelectionListener(new SelectionListener() {

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
                 */
                public void widgetDefaultSelected(SelectionEvent e) {

                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
                 */
                public void widgetSelected(SelectionEvent e) {
                    for (INode runjobNode : tRunJobNodes) {

                        String id = (String) runjobNode.getElementParameter(EParameterName.PROCESS_TYPE_PROCESS.getName())
                                .getValue();
                        String version = (String) runjobNode.getElementParameter(EParameterName.PROCESS_TYPE_VERSION.getName())
                                .getValue();
                        if ("".equals(id) || id == null) {
                            MessageDialog.openWarning(getShell(), "Warning: " + runjobNode.getUniqueName(),
                                    "Child job should be assigned to " + runjobNode.getUniqueName());
                            return;
                        }
                        IEditorReference[] editorReferences = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                                .getActivePage().getEditorReferences();
                        IEditorPart activeEditorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                                .getActiveEditor();
                        for (IEditorReference editorReference : editorReferences) {
                            IEditorPart editorPart = editorReference.getEditor(false);
                            if ((editorPart != activeEditorPart) && (editorPart instanceof MultiPageTalendEditor)) {
                                IElement element = ((MultiPageTalendEditor) editorPart).getProcess();
                                StatsAndLogsViewHelper.applySettings(elem, element, AbstractPreferenceComposite.this);
                            }
                        }
                        applySettingsToSubJob(id, version);

                    }

                }

                /**
                 * yzhang Comment method "applySettingsToSubJob".
                 * 
                 * @param id
                 * @param version
                 */
                private void applySettingsToSubJob(String id, String version) {
                    ProcessType processType = ItemCacheManager.getProcessItem(id, version).getProcess();
                    EList<ElementParameterType> parameters = processType.getParameters().getElementParameter();
                    StatsAndLogsViewHelper.applySettings(parameters, elem);

                    List<NodeType> nodes = findRunJobNode(processType.getNode());
                    for (NodeType nodeType : nodes) {
                        EList<ElementParameterType> list = nodeType.getElementParameter();

                        ElementParameterType idParam = getElementParameterType(list, PROCESS + ":"
                                + EParameterName.PROCESS_TYPE_PROCESS.getName());
                        ElementParameterType versionParam = getElementParameterType(list, PROCESS + ":"
                                + EParameterName.PROCESS_TYPE_VERSION.getName());

                        String subId = idParam.getValue();
                        String subVersion = versionParam.getValue();

                        applySettingsToSubJob(subId, subVersion);
                    }
                }
            });
        }
    }

    /**
     * yzhang Comment method "getElementParameterType".
     * 
     * @param paraName
     * @return
     */
    private ElementParameterType getElementParameterType(EList<ElementParameterType> list, String paraName) {
        for (ElementParameterType parameter : list) {
            if (parameter.getName().equals(paraName)) {
                return parameter;
            }
        }
        return null;
    }

    /**
     * yzhang Comment method "hasRunJobNode".
     * 
     * @return
     */
    private boolean hasRunJobNode(boolean needRefresh) {
        if (tRunJobNodes == null || needRefresh) {
            tRunJobNodes = findRunJobNode();
        }
        return tRunJobNodes.size() > 0;
    }

    /**
     * yzhang Comment method "foundRunJobNode".
     * 
     * @return
     */
    private List<INode> findRunJobNode() {
        if (elem instanceof IProcess) {
            List<INode> matchingNodes = new ArrayList<INode>();
            for (INode node : (List<INode>) ((IProcess) elem).getGeneratingNodes()) {
                if (TRUN_JOB.equals(node.getComponent().getName())) {
                    matchingNodes.add(node);
                }
            }
            return matchingNodes;
        } else {
            return null;
        }
    }

    /**
     * yzhang Comment method "findRunJobNode".
     * 
     * @param nodes
     * @return
     */
    private List<NodeType> findRunJobNode(EList<NodeType> nodes) {
        List<NodeType> matchingNodes = new ArrayList<NodeType>();
        for (NodeType nodeType : nodes) {
            if (TRUN_JOB.equals(nodeType.getComponentName())) {
                matchingNodes.add(nodeType);
            }
        }
        return matchingNodes;
    }

    protected void onReloadButtonClick() {
        if (elem == null) {
            return;
        }
        boolean isOK = MessageDialog.openConfirm(Display.getDefault().getActiveShell(), dialogTitle, Messages
                .getString("StatsAndLogsComposite.ReloadMessages")); //$NON-NLS-1$
        if (isOK) {
            onReloadPreference();

            IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();

            if (activeEditor != null) {
                AbstractTalendEditor workbenchPart = ((AbstractMultiPageTalendEditor) activeEditor).getTalendEditor();
                workbenchPart.setDirty(true);
            }
        }
        addComponents(true);
        refresh();
    }

    /**
     * Override by subclass.
     */
    protected void onReloadPreference() {

    }

    protected void onSaveButtonClick() {
        if (elem == null) {
            return;
        }
        boolean isOK = MessageDialog.openConfirm(Display.getDefault().getActiveShell(), dialogTitle, Messages
                .getString("StatsAndLogsComposite.SavePreferenceMessages")); //$NON-NLS-1$
        if (isOK) {
            onSavePreference();
        }
    }

    /**
     * Override by subclass.
     */
    protected void onSavePreference() {
    }

    @Override
    public void refresh() {
        super.refresh();

        Element element = getElement();
        if (element != null && element instanceof IProcess) {
            IProcess process = (IProcess) element;
            if (reloadBtn != null && !reloadBtn.isDisposed()) {
                reloadBtn.setEnabled(!process.isReadOnly());
            }
            if (saveBtn != null && !saveBtn.isDisposed()) {
                saveBtn.setEnabled(!process.isReadOnly());
            }
        }
    }

}

// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
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
import org.eclipse.swt.widgets.Control;
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
import org.talend.designer.core.ui.views.statsandlogs.StatsAndLogsViewHelper;
import org.talend.designer.core.utils.DesignerUtilities;
import org.talend.designer.runprocess.ItemCacheManager;

/**
 * Add buttons for loading and saving values between preference page and job view.
 */
public abstract class AbstractPreferenceComposite extends MultipleThreadDynamicComposite {

    protected Button reloadBtn;

    protected Button saveBtn;

    private String dialogTitle;

    protected Button applyToChildrenJob;

    private List<INode> tRunJobNodes;

    private static final String PROCESS = "PROCESS"; //$NON-NLS-1$

    // achen added to fix 0005991 & 0005993
    protected boolean isUsingProjectSetting;

    protected Button useProjectSetting;

    protected Composite topComposite;

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
            topComposite = new Composite(getComposite(), SWT.NONE);

            if (hasRunJobNode(false) && needApplyToChildren() || isUsingProjectSetting) {
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
            // achen modify to fix 0005993
            if (isUsingProjectSetting) {
                saveBtn.setText(Messages.getString("SaveToProjectSettings")); //$NON-NLS-1$
                saveBtn.setToolTipText(Messages.getString("SaveToProjectSettingsToolTipText")); //$NON-NLS-1$

                reloadBtn.setText(Messages.getString("ReloadFromProjectSettings")); //$NON-NLS-1$
                reloadBtn.setToolTipText(Messages.getString("ReloadFromProjectSettingsToolTipText")); //$NON-NLS-1$

                // add useprojectsetting button
                useProjectSetting = new Button(topComposite, SWT.CHECK);
                useProjectSetting.setText(Messages.getString("StatsAndLogs.UseProjectSettings")); //$NON-NLS-1$
                useProjectSetting.setToolTipText(Messages.getString("StatsAndLogs.UseProjectSettings")); //$NON-NLS-1$
                useProjectSetting.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
            }
            // end
            if (hasRunJobNode(false) && needApplyToChildren()) {
                applyToChildrenJob = new Button(topComposite, SWT.PUSH);
                applyToChildrenJob.setText(Messages.getString("AbstractPreferenceComposite.textContent")); //$NON-NLS-1$
                applyToChildrenJob.setToolTipText(Messages.getString("AbstractPreferenceComposite.tipContent")); //$NON-NLS-1$
            }

            Point initialSize = topComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT);

            addButtonListeners();
            refresh();
            super.addComponents(true, false, initialSize.y + ITabbedPropertyConstants.VSPACE);

        }
    }

    /**
     * 
     * DOC aimingchen Comment method "setMainCompositeEnable".
     * 
     * @param enabled
     */
    protected void setMainCompositeEnable(boolean enabled) {
        Control[] controls = getComposite().getChildren();
        for (int i = 0; i < controls.length; i++) {
            if (controls[i] != topComposite) {
                controls[i].setEnabled(enabled);
            }
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
                        if ("".equals(id) || id == null) { //$NON-NLS-1$
                            MessageDialog.openWarning(getShell(), Messages.getString(
                                    "AbstractPreferenceComposite.warning", runjobNode.getUniqueName()), //$NON-NLS-1$
                                    Messages.getString("AbstractPreferenceComposite.jobAssigned", runjobNode.getUniqueName())); //$NON-NLS-1$
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

                        ElementParameterType idParam = getElementParameterType(list, PROCESS + ":" //$NON-NLS-1$
                                + EParameterName.PROCESS_TYPE_PROCESS.getName());
                        ElementParameterType versionParam = getElementParameterType(list, PROCESS + ":" //$NON-NLS-1$
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
     * 
     * DOC aimingchen Comment method "useProjectSetting".
     */
    protected void useProjectSetting() {
        if (elem == null) {
            return;
        }
        // achen modify to fix 0005991& 0005993
        onReloadPreference();

        IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();

        if (activeEditor != null) {
            AbstractTalendEditor workbenchPart = ((AbstractMultiPageTalendEditor) activeEditor).getTalendEditor();
            workbenchPart.setDirty(true);
        }

        addComponents(true);
        refresh();
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
        if (tRunJobNodes == null) {
            return false;
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
            return DesignerUtilities.getTRunjobs((IProcess) elem);
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
            if (DesignerUtilities.isTRunJobComponent(nodeType)) {
                matchingNodes.add(nodeType);
            }
        }
        return matchingNodes;
    }

    protected void onReloadButtonClick() {
        if (elem == null) {
            return;
        }
        // achen modify to fix 0005991& 0005993
        String message = ""; //$NON-NLS-1$
        if (!isUsingProjectSetting) {
            message = Messages.getString("StatsAndLogsComposite.ReloadMessages"); //$NON-NLS-1$
        } else {
            message = Messages.getString("ReloadFromProjectSettingsMessages"); //$NON-NLS-1$
        }
        boolean isOK = MessageDialog.openConfirm(Display.getDefault().getActiveShell(), dialogTitle, message); //$NON-NLS-1$
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
        // achen modify to fix 0005991& 0005993
        String message = ""; //$NON-NLS-1$
        if (!isUsingProjectSetting) {
            message = Messages.getString("StatsAndLogsComposite.SavePreferenceMessages"); //$NON-NLS-1$
        } else {
            message = Messages.getString("SaveToProjectSettingsMessage"); //$NON-NLS-1$
        }
        boolean isOK = MessageDialog.openConfirm(Display.getDefault().getActiveShell(), dialogTitle, message); //$NON-NLS-1$       
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
            if (applyToChildrenJob != null && !applyToChildrenJob.isDisposed()) {
                applyToChildrenJob.setEnabled(!process.isReadOnly());
            }
        }
    }

}

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
package org.talend.repository.ui.wizards.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.repository.RepositoryObject;
import org.talend.core.ui.images.ECoreImage;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.ui.wizards.PropertiesWizardPage;
import org.talend.repository.ui.wizards.RepositoryWizard;
import org.talend.repository.ui.wizards.metadata.connection.Step0WizardPage;

/**
 * FileWizard present the FileForm. Use to create a new connection to a DB.
 */

public class ContextWizard extends RepositoryWizard implements INewWizard {

    private static Logger log = Logger.getLogger(ContextWizard.class);

    private PropertiesWizardPage contextWizardPage0;

    private Property contextProperty;

    private ContextItem contextItem;

    private IContextManager contextManager;

    String oldSource;

    ProxyRepositoryFactory factory;

    /**
     * Constructor for FileWizard.
     * 
     * @param workbench
     * @param selection
     * @param strings
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public ContextWizard(IWorkbench workbench, boolean creation, ISelection selection, boolean forceReadOnly) {
        super(workbench, creation, forceReadOnly);
        factory = ProxyRepositoryFactory.getInstance();
        pathToSave = getPath(selection);

        setWindowTitle(""); //$NON-NLS-1$
        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(ECoreImage.CONTEXT_WIZ));
        setNeedsProgressMonitor(true);

        if (creation) {
            contextItem = PropertiesFactory.eINSTANCE.createContextItem();
            contextProperty = PropertiesFactory.eINSTANCE.createProperty();
            contextProperty.setAuthor(((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                    .getUser());
            contextProperty.setVersion(VersionUtils.DEFAULT_VERSION);
            contextProperty.setStatusCode(""); //$NON-NLS-1$

            contextItem.setProperty(contextProperty);

            contextManager = new JobContextManager();
        } else {
            RepositoryNode node = (RepositoryNode) ((IStructuredSelection) selection).getFirstElement();
            RepositoryObject object = (RepositoryObject) node.getObject();
            setRepositoryObject(object);
            isRepositoryObjectEditable();
            initLockStrategy();

            contextItem = (ContextItem) object.getProperty().getItem();
            oldSource = contextItem.getProperty().getLabel();

            contextProperty = contextItem.getProperty();
            contextManager = new JobContextManager(contextItem.getContext(), contextItem.getDefaultContext());

        }
        initLockStrategy();
    }

    /**
     * Adding the page to the wizard.
     */
    public void addPages() {
        setWindowTitle(Messages.getString("ContextWizard.Title")); //$NON-NLS-1$
        contextWizardPage0 = new Step0WizardPage(contextProperty, pathToSave, ERepositoryObjectType.CONTEXT,
                !isRepositoryObjectEditable(), creation);
        contextWizardPage0.setTitle(Messages.getString("ContextWizard.step0Title")); //$NON-NLS-1$
        contextWizardPage0.setDescription(Messages.getString("ContextWizard.step0Description")); //$NON-NLS-1$
        addPage(contextWizardPage0);
        if (creation) {
            contextWizardPage0.setPageComplete(false);
        }

        ContextPage contextPage = new ContextPage("test", contextManager, !isRepositoryObjectEditable()); //$NON-NLS-1$
        contextPage.setTitle(Messages.getString("ContextWizard.contextPageTitle")); //$NON-NLS-1$
        contextPage.setDescription(Messages.getString("ContextWizard.contextPageDescription")); //$NON-NLS-1$
        addPage(contextPage);
    }

    /**
     * This method determine if the 'Finish' button is enable This method is called when 'Finish' button is pressed in
     * the wizard. We will create an operation and run it using wizard as execution context.
     */
    public boolean performFinish() {
        // TimeMeasure.display = true;
        // TimeMeasure.measureActive = true;
        // TimeMeasure.begin("performFinish");
        boolean formIsPerformed = contextManager.getListContext().size() != 0;
        // if (delimitedFileWizardPage3 == null) {
        // formIsPerformed = delimitedFileWizardPage2.isPageComplete();
        // } else {
        // formIsPerformed = delimitedFileWizardPage3.isPageComplete();
        // }

        if (formIsPerformed) {
            // IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            try {
                if (creation) {
                    String nextId = factory.getNextId();
                    contextProperty.setId(nextId);
                    contextManager.saveToEmf(contextItem.getContext());
                    contextItem.setDefaultContext(contextManager.getDefaultContext().getName());
                    factory.create(contextItem, contextWizardPage0.getDestinationPath());

                } else {
                    contextItem.getContext().clear();
                    contextManager.saveToEmf(contextItem.getContext());
                    contextItem.setDefaultContext(contextManager.getDefaultContext().getName());
                    factory.save(contextItem);
                    List<IContext> newContext = contextManager.getListContext();
                    updateRelatedView(newContext);

                }
                closeLockStrategy();
                // TimeMeasure.end("performFinish");
            } catch (PersistenceException e) {
                String detailError = e.toString();
                new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("CommonWizard.persistenceException"), //$NON-NLS-1$
                        detailError);
                log.error(Messages.getString("CommonWizard.persistenceException") + "\n" + detailError); //$NON-NLS-1$ //$NON-NLS-2$
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * DOC bqian Comment method "updateProcessContextManager".
     */
    private void updateRelatedView(List<IContext> newContext) {

        this.onJobUpdateParameter(newContext);
        RepositoryPlugin.getDefault().getDesignerCoreService().switchToCurContextsView();
    }

    /**
     * We will accept the selection in the workbench to see if we can initialize from it.
     * 
     * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
     */
    public void init(final IWorkbench workbench, final IStructuredSelection selection2) {
        this.selection = selection2;
    }

    private IPath getPath(ISelection selection) {
        RepositoryNode node = (RepositoryNode) ((IStructuredSelection) selection).getFirstElement();

        IPath path;
        if (node.getType() == ENodeType.SIMPLE_FOLDER || node.getType() == ENodeType.SYSTEM_FOLDER) {
            path = RepositoryNodeUtilities.getPath(node);
        } else {
            path = new Path(""); //$NON-NLS-1$
        }
        return path;
    }

    public synchronized void onJobUpdateParameter(List<IContext> newContext) {
        List<String> contextList = new ArrayList<String>();
        IContext defaultContext = contextManager.getDefaultContext();

        for (IContext con : newContext) {
            // String str = con.getName();
            contextList.add(con.getName());// be optimized
        }

        for (IContext con : newContext) {
            List<IContextParameter> conp = con.getContextParameterList();
            String str = con.getName();
            for (IContextParameter cont : conp) {
                this.replaceJobContext(str, contextList, newContext);
                this.replaceViewContext(defaultContext, contextList, newContext);// be optimized
            }
        }

        // for (IContext con : newContext) {
        // List<IContextParameter> conp = con.getContextParameterList();
        // for (IContextParameter cont : conp) {
        // this.replaceViewContext(defaultContext, contextList, newContext);
        // }
        // }

    }

    private synchronized void replaceViewContext(IContext defaultContext, List<String> contextList, List<IContext> newContext) {
        boolean modified = false;
        boolean modify = false;
        List<String> newNameList = new ArrayList<String>();
        List<String> viewList = new ArrayList();

        IEditorReference[] reference = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
        List<IProcess> processes = RepositoryPlugin.getDefault().getDesignerCoreService().getOpenedProcess(reference);

        for (IEditorReference er : reference) {
            MultiPageEditorPart talendEditor = ((MultiPageEditorPart) (er.getPart(true)));

            Item item = RepositoryPlugin.getDefault().getDesignerCoreService().getProcessItem(talendEditor);

            for (IProcess process : processes) {
                viewList.clear();
                IContextManager cm = process.getContextManager();
                List<IContext> list = cm.getListContext();
                for (IContext context : newContext) {
                    for (IContext viewContext : list) {
                        viewList.add(viewContext.getName());
                        List<IContextParameter> viewContextParameter = viewContext.getContextParameterList();
                        for (IContextParameter cpar : viewContextParameter) {
                            if (oldSource.equals(cpar.getSource())) {
                                if (context.getName().equals(viewContext.getName())) {
                                    Map<String, String> nameMap = ((JobContextManager) contextManager).getNameMap();
                                    List<IContextParameter> newContextParameter = context.getContextParameterList();
                                    for (IContextParameter cp : newContextParameter) {
                                        if (nameMap.containsKey(cp.getName())) {
                                            String oldName = nameMap.get(cp.getName());
                                            if (oldName.equals(cpar.getName())) {
                                                this.updateViewContext(cpar, cp);
                                                newNameList.add(cp.getName());
                                                modified = true;
                                            }
                                        }
                                    }
                                    for (IContextParameter cp : newContextParameter) {
                                        if (newNameList.isEmpty()) {
                                            if (cpar.getName().equals(cp.getName())) {
                                                this.updateViewContext(cpar, cp);
                                                modified = true;
                                            }

                                        } else {
                                            for (String newName : newNameList) {
                                                if (!(newName.equals(cp.getName()))) {
                                                    if (cpar.getName().equals(cp.getName())) {
                                                        this.updateViewContext(cpar, cp);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                modify = this.findOtherContextName(list, defaultContext, contextList, viewList);
                if (modified || modify) {
                    try {
                        factory.save(item);
                    } catch (PersistenceException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private synchronized void replaceJobContext(String str, List<String> contextList, List<IContext> newContext) {
        // ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        List<String> jobList = new ArrayList<String>();

        List<IRepositoryObject> roList;
        try {
            roList = factory.getAll(ERepositoryObjectType.PROCESS, true);
            for (IRepositoryObject mainobject : roList) {
                List<IRepositoryObject> allVersion = factory.getAllVersion(mainobject.getId());
                for (IRepositoryObject object : allVersion) {
                    jobList.clear();
                    ProcessItem item = (ProcessItem) object.getProperty().getItem();

                    List<ContextType> contextl = item.getProcess().getContext();

                    boolean modified = false;
                    boolean modify = false;
                    for (ContextType context : contextl) {
                        List<ContextParameterType> paramList = context.getContextParameter();
                        jobList.add(context.getName());
                        for (ContextParameterType param : paramList) {
                            if (param.getRepositoryContextId() != null
                                    && param.getRepositoryContextId().equals(contextItem.getProperty().getId())) {
                                if (context.getName().equals(str)) {
                                    modified = this.compareName(newContext, param);
                                }
                            }
                        }
                    }
                    modify = this.findOtherName(contextl, contextList, jobList);
                    if (modified || modify) {
                        factory.save(item);
                    }
                }
            }
        } catch (PersistenceException e) {
            String detailError = e.toString();
            new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("CommonWizard.persistenceException"),
            //$NON-NLS-1$
                    detailError);
            log.error(Messages.getString("CommonWizard.persistenceException") + "\n" + detailError); //$NON-NLS-1$
            //$NON-NLS-2$
        }

    }

    private synchronized void updateJobContext(ContextParameterType param, IContextParameter cont) {
        param.setName(cont.getName());
        param.setType(cont.getType());
        param.setComment(cont.getComment());
        param.setPrompt(cont.getPrompt());
        param.setValue(cont.getValue());
        param.setPromptNeeded(cont.isPromptNeeded());
    }

    private synchronized void updateViewContext(IContextParameter cpar, IContextParameter cont) {
        cpar.setName(cont.getName());
        cpar.setType(cont.getType());
        cpar.setComment(cont.getComment());
        cpar.setPrompt(cont.getPrompt());
        cpar.setValue(cont.getValue());
        cpar.setPromptNeeded(cont.isPromptNeeded());
    }

    private synchronized boolean findOtherName(List<ContextType> contextl, List<String> contextList, List<String> jobList) {
        boolean modified = false;
        List<String> newNameList = new ArrayList<String>();
        ContextParameterType conParameter;
        List<ContextParameterType> jobContext;
        List<ContextType> defaultContextType = contextItem.getContext();
        List<ContextParameterType> defContextParameterType;
        Map<String, String> nameMap = ((JobContextManager) contextManager).getNameMap();
        jobList.removeAll(contextList);

        if (jobList == null) {
            return false;
        }
        for (String job : jobList) {
            for (ContextType context : contextl) {
                if (context.getName().equals(job)) {
                    jobContext = context.getContextParameter();
                    for (ContextParameterType jobContextParameterType : jobContext) {
                        if (jobContextParameterType.getRepositoryContextId() != null
                                && jobContextParameterType.getRepositoryContextId().equals(contextItem.getProperty().getId())) {
                            for (ContextType contextType : defaultContextType) {
                                defContextParameterType = (List<ContextParameterType>) contextType.getContextParameter();
                                for (ContextParameterType defaContextParameterType : defContextParameterType) {
                                    if (nameMap.containsKey(defaContextParameterType.getName())) {
                                        String oldconName = nameMap.get(defaContextParameterType.getName());
                                        if (jobContextParameterType.getName().equals(oldconName)) {
                                            jobContextParameterType.setName(defaContextParameterType.getName());
                                            jobContextParameterType.setType(defaContextParameterType.getType());
                                            jobContextParameterType.setComment(defaContextParameterType.getComment());
                                            jobContextParameterType.setPrompt(defaContextParameterType.getPrompt());
                                            jobContextParameterType.setValue(defaContextParameterType.getValue());
                                            jobContextParameterType.setPromptNeeded(defaContextParameterType.isPromptNeeded());
                                            newNameList.add(defaContextParameterType.getName());
                                            modified = true;
                                        }
                                    }

                                }
                                for (ContextParameterType defaContextParameterType : defContextParameterType) {
                                    if (newNameList.isEmpty()) {
                                        if (jobContextParameterType.getName().equals(defaContextParameterType.getName())) {
                                            jobContextParameterType.setName(defaContextParameterType.getName());
                                            jobContextParameterType.setType(defaContextParameterType.getType());
                                            jobContextParameterType.setComment(defaContextParameterType.getComment());
                                            jobContextParameterType.setPrompt(defaContextParameterType.getPrompt());
                                            jobContextParameterType.setValue(defaContextParameterType.getValue());
                                            jobContextParameterType.setPromptNeeded(defaContextParameterType.isPromptNeeded());
                                            modified = true;
                                        }

                                    } else {
                                        for (String newName : newNameList) {
                                            if (!(newName.equals(defaContextParameterType.getName()))) {
                                                if (jobContextParameterType.getName().equals(defaContextParameterType.getName())) {
                                                    jobContextParameterType.setName(defaContextParameterType.getName());
                                                    jobContextParameterType.setType(defaContextParameterType.getType());
                                                    jobContextParameterType.setComment(defaContextParameterType.getComment());
                                                    jobContextParameterType.setPrompt(defaContextParameterType.getPrompt());
                                                    jobContextParameterType.setValue(defaContextParameterType.getValue());
                                                    jobContextParameterType.setPromptNeeded(defaContextParameterType
                                                            .isPromptNeeded());
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }
                }

            }
        }
        return modified;
    }

    private synchronized boolean findOtherContextName(List<IContext> list, IContext defaultContext, List<String> contextList,
            List<String> viewList) {
        boolean modified = false;
        List<String> newNameList = new ArrayList<String>();
        IContextParameter contextParameter;
        List<IContextParameter> viewContext;
        List<IContextParameter> defContext = defaultContext.getContextParameterList();
        Map<String, String> nameMap = ((JobContextManager) contextManager).getNameMap();
        viewList.removeAll(contextList);

        if (viewList == null) {
            return false;
        }
        for (String job : viewList) {
            for (IContext context : list) {
                if (context.getName().equals(job)) {
                    viewContext = context.getContextParameterList();
                    for (IContextParameter viewContextParameter : viewContext) {
                        if (viewContextParameter.getSource().equals(oldSource)) {
                            for (IContextParameter defContextParameter : defContext) {
                                if (nameMap.containsKey(defContextParameter.getName())) {
                                    String oldcontName = nameMap.get(defContextParameter.getName());
                                    if (oldcontName.equals(viewContextParameter.getName())) {
                                        this.updateViewContext(viewContextParameter, defContextParameter);
                                        newNameList.add(defContextParameter.getName());
                                        modified = true;
                                    }
                                }
                            }
                            for (IContextParameter cp : defContext) {
                                if (newNameList.isEmpty()) {
                                    if (viewContextParameter.getName().equals(cp.getName())) {
                                        this.updateViewContext(viewContextParameter, cp);
                                        modified = true;
                                    }

                                } else {
                                    for (String newName : newNameList) {
                                        if (!(newName.equals(cp.getName()))) {
                                            if (viewContextParameter.getName().equals(cp.getName())) {
                                                this.updateViewContext(viewContextParameter, cp);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return modified;
    }

    private synchronized boolean compareName(List<IContext> newContext, ContextParameterType param) {

        boolean modified = false;

        List<String> newNameList = new ArrayList<String>();
        Map<String, String> nameMap = ((JobContextManager) contextManager).getNameMap();

        for (IContext context : newContext) {
            List<IContextParameter> contextParameter = context.getContextParameterList();
            for (IContextParameter cp : contextParameter) {
                if (nameMap.containsKey(cp.getName())) {
                    String oldName = nameMap.get(cp.getName());
                    if (oldName.equals(param.getName())) {
                        this.updateJobContext(param, cp);
                        newNameList.add(cp.getName());
                        modified = true;
                    }
                }
            }
            for (IContextParameter cp : contextParameter) {
                if (newNameList.isEmpty()) {
                    if (param.getName().equals(cp.getName())) {
                        this.updateJobContext(param, cp);
                        modified = true;
                    }

                } else {
                    for (String newName : newNameList) {
                        if (!(newName.equals(cp.getName()))) {
                            if (param.getName().equals(cp.getName())) {
                                this.updateJobContext(param, cp);
                            }
                        }
                    }
                }
            }
        }
        return modified;
    }

}

// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.preference;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.relationship.Relation;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.core.model.repository.DynaEnum;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.ui.token.AbstractTokenCollector;
import org.talend.core.ui.token.TokenInforUtil;
import org.talend.core.ui.token.TokenKey;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.repository.ProjectManager;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.model.IProxyRepositoryFactory;

import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;

/**
 * ggu class global comment. Detailled comment
 */
public class TosTokenCollector extends AbstractTokenCollector {

    private static final String PREF_TOS_JOBS_RECORDS = "TOS_Jobs_Records"; //$NON-NLS-1$

    private static final TokenKey TOS_COUNT_PROJECTS = new TokenKey("nb.projects"); //$NON-NLS-1$

    private static final TokenKey TOS_COUNT_JOBS_PER_PROJECT = new TokenKey("jobsPerProject"); //$NON-NLS-1$

    private static final TokenKey TOS_COUNT_COMPONENTS_PER_JOB = new TokenKey("tos.count.componentsPerJob"); //$NON-NLS-1$

    private static final TokenKey TOS_COUNT_CONTEXT_VARIABLES = new TokenKey("tos.count.contextVariables"); //$NON-NLS-1$

    private static final TokenKey TOS_COUNT_CONTEXT_VARIABLES_PER_JOB = new TokenKey("tos.count.contextVariablesPerJob"); //$NON-NLS-1$

    /**
     * ggu JobTokenCollector constructor comment.
     */
    public TosTokenCollector() {
    }

    @Override
    public void priorCollect() throws Exception {
        // for all projects
        JSONObject allProjectRecords = null;

        IPreferenceStore preferenceStore = RepositoryPlugin.getDefault().getPreferenceStore();
        String records = preferenceStore.getString(PREF_TOS_JOBS_RECORDS);
        try {
            // reset
            allProjectRecords = new JSONObject(records);
        } catch (Exception e) {
            // the value is not set, or is empty
            allProjectRecords = new JSONObject();
        }

        JSONObject currentProjectObject = collectProjectDetails();
        System.out.println("project=" + currentProjectObject.toString());

        Project currentProject = ProjectManager.getInstance().getCurrentProject();
        allProjectRecords.put(currentProject.getTechnicalLabel(), currentProjectObject);

        //
        preferenceStore.setValue(PREF_TOS_JOBS_RECORDS, allProjectRecords.toString());
    }

    private JSONObject collectProjectDetails() throws PersistenceException, JSONException {
        JSONObject jObject = new JSONObject();

        Project currentProject = ProjectManager.getInstance().getCurrentProject();
        final IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

        JSONObject repoStats = new JSONObject();
        // metadata
        for (DynaEnum type : ERepositoryObjectType.values()) {
            if (type instanceof ERepositoryObjectType) {
                List<IRepositoryViewObject> all = factory.getAll(currentProject, (ERepositoryObjectType) type);

                int nb = all.size();
                if (nb > 0) {
                    repoStats.put(type.getType(), nb);
                    if (ERepositoryObjectType.getAllTypesOfProcess().contains(type)) {
                        JSONObject jobDetails = new JSONObject();
                        collectJobDetails(all, jobDetails);
                        repoStats.put("details", jobDetails);
                    } else {
                        int nbUsed = 0;
                        for (IRepositoryViewObject object : all) {
                            List<Relation> relations = RelationshipItemBuilder.getInstance().getItemsHaveRelationWith(
                                    object.getId());
                            nbUsed = nbUsed + relations.size();
                        }
                        repoStats.put("nb.used", nbUsed);
                    }
                }
            }
        }
        jObject.put("repository", repoStats);
        jObject.put("type", ProjectManager.getInstance().getProjectType(currentProject));
        int nbRef = ProjectManager.getInstance().getAllReferencedProjects().size();
        if (nbRef > 0) {
            jObject.put("nb.refProjects", nbRef);
        }

        return jObject;
    }

    /**
     * DOC nrousseau Comment method "collectJobDetails".
     * 
     * @param all
     * @param jobDetails
     * @throws JSONException
     */
    private void collectJobDetails(List<IRepositoryViewObject> allRvo, JSONObject jobDetails) throws JSONException {
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        IWorkbenchWindow ww = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        IEditorReference[] reference = new IEditorReference[0];
        if (ww != null) {
            IWorkbenchPage page = ww.getActivePage();
            reference = page.getEditorReferences();
        }
        List<IProcess2> processes = RepositoryPlugin.getDefault().getDesignerCoreService().getOpenedProcess(reference);
        Set<String> idsOpened = new HashSet<String>();
        for (IProcess2 process : processes) {
            idsOpened.add(process.getId());
        }

        JSONObject components = new JSONObject();

        int contextVarsNum = 0;
        int nbComponentsUsed = 0;
        for (IRepositoryViewObject rvo : allRvo) {
            Item item = rvo.getProperty().getItem();
            if (item instanceof ProcessItem) {
                ProcessType processType = ((ProcessItem) item).getProcess();

                for (NodeType node : (List<NodeType>) processType.getNode()) {
                    String componentName = node.getComponentName();
                    int nbComp = 0;
                    if (components.has(componentName)) {
                        nbComp = components.getInt(componentName);
                    }
                    components.put(componentName, nbComp + 1);
                    nbComponentsUsed++;
                }
                // context variable per job
                EList contexts = processType.getContext();
                if (contexts.size() > 0) {
                    ContextType contextType = (ContextType) contexts.get(0);
                    contextVarsNum += contextType.getContextParameter().size();
                }

            }
            if (factory.getStatus(item) != ERepositoryStatus.LOCK_BY_USER && !idsOpened.contains(item.getProperty().getId())) {
                // job is not locked and not opened by editor, so we can unload.
                if (item.getParent() instanceof FolderItem) {
                    ((FolderItem) item.getParent()).getChildren().remove(item);
                    item.setParent(null);
                }
                item.eResource().unload();
            }
        }
        jobDetails.put("components", components);
        jobDetails.put("nb.contextVars", contextVarsNum);
        jobDetails.put("nb.components", nbComponentsUsed);
    }

    @Override
    protected void collectProperties(JSONObject propertiesObject) throws Exception {
        // number of projects
        int projectsNum = 0;
        
        JSONObject mergedData = new JSONObject();

        IPreferenceStore preferenceStore = RepositoryPlugin.getDefault().getPreferenceStore();
        String records = preferenceStore.getString(PREF_TOS_JOBS_RECORDS);
        JSONObject allProjectRecords = new JSONObject(records);
        Iterator<String> keys = allProjectRecords.keys();
        while (keys.hasNext()) {
            String projectName = keys.next();
             JSONObject object = (JSONObject) allProjectRecords.get(projectName); 
             if (object != null) {
                 TokenInforUtil.mergeJSON(object, mergedData);
             }
            projectsNum++;
        }
        // object.getInt(TOS_COUNT_JOBS.getKey()); businessModelsNum +=
        // object.getInt(TOS_COUNT_BUSINESS_MODELS.getKey()); generatedJobDocsNum +=
        // object.getInt(TOS_COUNT_GENERATED_JOB_DOCS.getKey()); metadatasNum +=
        // object.getInt(TOS_COUNT_METADATAS.getKey()); contextVarsNum +=
        // object.getInt(TOS_COUNT_CONTEXT_VARIABLES.getKey()); componentsNum +=
        // object.getInt(TOS_COUNT_COMPONENTS.getKey());
        //
        // JSONObject top20Components = (JSONObject) object.get(TOS_JOB_TOP20_COMPONENTS.getKey()); if (top20Components
        // != null && top20Components.length() > 0) { Iterator<String> componentKeys = top20Components.keys(); while
        // (componentKeys.hasNext()) { String componentName = componentKeys.next(); int num =
        // top20Components.getInt(componentName); if (numComponentMap.containsKey(componentName)) { Integer old =
        // numComponentMap.get(componentName); if (old != null) { num += old.intValue(); } }
        // numComponentMap.put(componentName, num); } } } } } catch (Exception e) { // the value is not set, or is empty
        // }

        // propertiesObject.put(TOS_COUNT_LOCALPROJECTS.getKey(), projectsNum);
        // propertiesObject.put(TOS_COUNT_JOBS.getKey(), jobsNum);
        // propertiesObject.put(TOS_COUNT_BUSINESS_MODELS.getKey(), businessModelsNum);
        // propertiesObject.put(TOS_COUNT_GENERATED_JOB_DOCS.getKey(), generatedJobDocsNum);
        // propertiesObject.put(TOS_COUNT_METADATAS.getKey(), metadatasNum);
        // propertiesObject.put(TOS_COUNT_JOBS_PER_PROJECT.getKey(), TokenInforUtil.calcAverageToStr(jobsNum,
        // projectsNum)); propertiesObject.put(TOS_COUNT_COMPONENTS_PER_JOB.getKey(),
        // TokenInforUtil.calcAverageToStr(componentsNum, jobsNum));
        // propertiesObject.put(TOS_COUNT_CONTEXT_VARIABLES_PER_JOB.getKey(),
        // TokenInforUtil.calcAverageToStr(contextVarsNum, jobsNum));
        //
        // // top20 propertiesObject.put(TOS_JOB_TOP20_COMPONENTS.getKey(),
        // TokenInforUtil.convertTopComponentsArray(numComponentMap, TOP_USED_COMPONENTS_MAX));

    }
}

// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.runprocess.maven.listener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.properties.RoutinesJarItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.routines.CodesJarInfo;
import org.talend.core.model.routines.RoutinesUtil;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.utils.XmiResourceManager;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.core.utils.CodesJarResourceCache;
import org.talend.designer.maven.tools.CodeM2CacheManager;
import org.talend.designer.maven.tools.CodesJarM2CacheManager;
import org.talend.designer.maven.utils.CodesJarMavenUtil;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.designer.runprocess.java.TalendJavaProjectManager;
import org.talend.repository.ProjectManager;
import org.talend.repository.documentation.ERepositoryActionName;

public class CodesJarChangeListener implements PropertyChangeListener {

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        String propertyName = event.getPropertyName();
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        try {
            if (propertyName.equals(ERepositoryActionName.PROPERTIES_CHANGE.getName())) {
                casePropertiesChange(oldValue, newValue);
            } else if (propertyName.equals(ERepositoryActionName.DELETE_FOREVER.getName())
                    || propertyName.equals(ERepositoryActionName.DELETE_TO_RECYCLE_BIN.getName())) {
                caseDelete(propertyName, newValue);
            } else if (propertyName.equals(ERepositoryActionName.SAVE.getName())
                    || propertyName.equals(ERepositoryActionName.CREATE.getName())) {
                caseCreateOrSave(newValue);
            } else if (propertyName.equals(ERepositoryActionName.IMPORT.getName())) {
                caseImport(propertyName, newValue);
            } else if (propertyName.equals(ERepositoryActionName.RESTORE.getName())) {
                caseRestore(newValue);
            } else if (propertyName.equals(ERepositoryActionName.COPY.getName())) {
                caseCopy(newValue);
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    private void casePropertiesChange(Object oldValue, Object newValue) throws Exception {
        if (oldValue instanceof String[] && newValue instanceof Property) {
            Property property = (Property) newValue;
            if (needUpdate(property.getItem())) {
                String[] oldFields = (String[]) oldValue;
                if (oldFields.length == 0) {
                    return;
                }
                String oldName = oldFields[0];
                String oldVersion = oldFields[1];
                CodesJarResourceCache.updateCache(null, oldName, oldVersion, property);
                ERepositoryObjectType type = ERepositoryObjectType.getItemType(property.getItem());
                TalendJavaProjectManager.deleteTalendCodesJarProject(type,
                        ProjectManager.getInstance().getProject(property).getTechnicalLabel(), oldName, true);
                boolean isLabelChanged = !property.getLabel().equals(oldName);
                if (isLabelChanged) {
                    CodesJarM2CacheManager.deleteCodesJarProjectCache(CodesJarInfo.create(property));
                }
                CodesJarM2CacheManager.updateCodesJarProject(CodesJarInfo.create(property), isLabelChanged);
            } else if (RoutinesUtil.isInnerCodes(property)) {
                updateModifiedDateForCodesJar(property.getItem());
            }
        }
    }

    private void caseDelete(String propertyName, Object newValue) throws Exception {
        if (newValue instanceof IRepositoryViewObject) {
            Property property = ((IRepositoryViewObject) newValue).getProperty();
            if (propertyName.equals(ERepositoryActionName.DELETE_FOREVER.getName())) {
                if (RoutinesUtil.isInnerCodes(property)) {
                    updateAndReSyncForInnerCode(property);
                    updateModifiedDateForCodesJar(property.getItem());
                } else if (needUpdate(property.getItem())) {
                    CodesJarResourceCache.removeCache(property);
                    CodesJarInfo info = CodesJarInfo.create(property);
                    CodesJarM2CacheManager.deleteCodesJarProjectCache(info);
                    TalendJavaProjectManager.deleteTalendCodesJarProject(info, true);
                }
            }
        }
    }

    private void caseCreateOrSave(Object newValue) throws Exception {
        if (newValue instanceof Item) {
            Item item = (Item) newValue;
            if (needUpdate(item)) {
                CodesJarResourceCache.addToCache(item.getProperty());
            } else if (RoutinesUtil.isInnerCodes(item.getProperty())) {
                updateModifiedDateForCodesJar(item);
            }
        }
    }

    private void caseImport(String propertyName, Object newValue) {
        if (newValue instanceof Set) {
            Set<Item> importItems = (Set<Item>) newValue;
            importItems.stream().filter(item -> needUpdate(item))
                    .forEach(item -> CodesJarResourceCache.addToCache(item.getProperty()));
        }
    }

    private void caseRestore(Object newValue) throws Exception {
        if (newValue instanceof IRepositoryViewObject) {
            IRepositoryViewObject object = (IRepositoryViewObject) newValue;
            Property property = object.getProperty();
            if (needUpdate(property.getItem())) {
                CodesJarResourceCache.addToCache(property);
            } else if (RoutinesUtil.isInnerCodes(property)) {
                updateModifiedDateForCodesJar(property.getItem());
            }
        }
    }

    private void caseCopy(Object newValue) throws Exception {
        if (newValue instanceof RoutineItem) {
            RoutineItem item = (RoutineItem) newValue;
            if (RoutinesUtil.isInnerCodes(item.getProperty())) {
                updateModifiedDateForCodesJar(item);
            }
            CodeM2CacheManager.updateCacheStatus(null, ERepositoryObjectType.getItemType(item), false);
        }
    }

    private boolean needUpdate(Item item) {
        return item instanceof RoutinesJarItem;
    }

    private void updateAndReSyncForInnerCode(Property property) throws Exception {
        if (!RoutinesUtil.isInnerCodes(property)) {
            return;
        }

        RoutineItem codeItem = (RoutineItem) property.getItem();
        CodesJarInfo info = CodesJarResourceCache.getCodesJarByInnerCode(codeItem);
        if (info == null) {
            return;
        }
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            IRunProcessService runProcessService = GlobalServiceRegister.getDefault().getService(IRunProcessService.class);
            if (runProcessService != null) {
                ITalendProcessJavaProject talendCodesJarJavaProject = runProcessService.getTalendCodesJarJavaProject(info);
                if (talendCodesJarJavaProject == null) {
                    return;
                }
                IFolder routineFolder = talendCodesJarJavaProject.getSrcSubFolder(null,
                        CodesJarMavenUtil.getCodesJarPackageByInnerCode(codeItem));
                IFile originalRoutineFile = routineFolder.getFile(property.getLabel() + JavaUtils.JAVA_EXTENSION);
                if (originalRoutineFile == null || !originalRoutineFile.exists()) {
                    return;
                }
                originalRoutineFile.delete(true, false, null);
            }
        }

        CodesJarM2CacheManager.updateCodesJarProject(info, false);
    }

    private void updateModifiedDateForCodesJar(Item item) throws Exception {
        if (item instanceof RoutineItem) {
            RoutineItem innerCodeItem = (RoutineItem) item;
            CodesJarInfo info = CodesJarResourceCache.getCodesJarByInnerCode(innerCodeItem);
            Project project = ProjectManager.getInstance().getProjectFromProjectTechLabel(info.getProjectTechName());
            IRepositoryViewObject obj = ProxyRepositoryFactory.getInstance().getLastVersion(project, info.getId());
            if (obj != null) {
                Property codesJarProperty = obj.getProperty();
                new XmiResourceManager().saveResource(codesJarProperty.eResource());
                CodesJarResourceCache.addToCache(codesJarProperty);
            }
        }
    }

    private void buildCodeProject(Item item) throws Exception {
        if (item instanceof RoutineItem) {
            RoutineItem routineItem = (RoutineItem) item;
            if (RoutinesUtil.isInnerCodes(routineItem.getProperty())) {
                CodesJarInfo info = CodesJarResourceCache.getCodesJarByInnerCode(routineItem);
                if (info != null) {
                    ITalendProcessJavaProject project = TalendJavaProjectManager.getTalendCodeJavaProject(info.getType(),
                            info.getProjectTechName());
                    project.buildModules(new NullProgressMonitor(), null, null);
                }
            } else {
                ERepositoryObjectType type = ERepositoryObjectType.getItemType(routineItem);
                if (type != null) {
                    ITalendProcessJavaProject project = TalendJavaProjectManager.getTalendCodeJavaProject(type);
                    project.buildModules(new NullProgressMonitor(), null, null);
                }
            }
        }
    }

}

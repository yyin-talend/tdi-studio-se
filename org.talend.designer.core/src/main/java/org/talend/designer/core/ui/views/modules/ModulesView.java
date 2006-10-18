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
package org.talend.designer.core.ui.views.modules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Level;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator.LAYOUT_MODE;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator.SHOW_SELECTION;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator.SORT;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.designer.core.model.components.ComponentImportNeeds;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.runprocess.Processor;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.repository.model.ComponentsFactoryProvider;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ModulesView extends ViewPart {

    public static final String VIEW_ID = "";

    private static final Bundle PERL_MODULE_PLUGIN = Platform.getBundle("org.talend.designer.codegen.perlmodule");

    private static final String CHECK_PERL_MODULE_RELATIVE_PATH = "perl/talend/check_modules.pl";

    private static final String MODULE_PARAM_KEY = "--module=";

    private static final String RESULT_SEPARATOR = " => ";

    private static final String RESULT_KEY_KO = "KO";

    private static final String RESULT_KEY_OK = "OK";

    protected static final String ID_STATUS = "status";

    private static List<ComponentImportNeeds> componentImportNeedsList;

    private static TableViewerCreator tableViewerCreator;

    private CheckAction checkAction;

    /**
     * DOC nrousseau ModulesView constructor comment.
     */
    public ModulesView() {
    }

    public static List<ComponentImportNeeds> getImports(String componentName) {
        List<ComponentImportNeeds> toReturn = new ArrayList<ComponentImportNeeds>();
        // check();
        for (ComponentImportNeeds current : getCompList()) {
            if (current.getComponentName().equals(componentName)) {
                toReturn.add(current);
            }
        }

        return toReturn;
    }

    private static List<ComponentImportNeeds> getCompList() {
        if (componentImportNeedsList == null) {
            componentImportNeedsList = getImportNeedsList();
            check();
        }
        return componentImportNeedsList;
    }

    public static void check() {
        // This map contains perl module name as keys and list of object using it as values :
        Map<String, List<ComponentImportNeeds>> componentsByModules = new HashMap<String, List<ComponentImportNeeds>>();

        String[] params = new String[] {};
        for (ComponentImportNeeds current : getCompList()) {
            String moduleName = current.getModuleName();
            List<ComponentImportNeeds> listForThisModule = componentsByModules.get(moduleName);
            if (listForThisModule == null) {
                // We have a new perl module to check :
                listForThisModule = new ArrayList<ComponentImportNeeds>();
                // Add it in the map :
                componentsByModules.put(moduleName, listForThisModule);
                // And in the params perl command line :
                params = (String[]) ArrayUtils.add(params, MODULE_PARAM_KEY + moduleName);
            }
            // Add this import in the perl module list :
            listForThisModule.add(current);

            // Set the status to unknow as after treatment, modules not in perl response are unknown
            current.setStatus(ComponentImportNeeds.UNKNOWN);
        }

        try {
            String checkPerlModuleAbsolutePath = FileLocator.toFileURL(
                    PERL_MODULE_PLUGIN.getEntry(CHECK_PERL_MODULE_RELATIVE_PATH)).getPath();

            StringBuffer out = new StringBuffer();
            StringBuffer err = new StringBuffer();

            Processor.exec(out, err, new Path(checkPerlModuleAbsolutePath), null, Level.DEBUG, "", "", -1, -1, params);

            analyzeResponse(out, componentsByModules);

            if (err.length() > 0) {
                throw new ProcessorException(err.toString());
            }

        } catch (IOException e) {
            ExceptionHandler.process(e);
        } catch (ProcessorException e) {
            MessageBoxExceptionHandler.process(e);
        }

    }

    public void refresh() {
        tableViewerCreator.getTableViewer().refresh();
    }

    /**
     * DOC smallet Comment method "analyzeResponse".
     * 
     * @param out
     */
    private static void analyzeResponse(StringBuffer buff, Map<String, List<ComponentImportNeeds>> componentsByModules) {

        String[] lines = buff.toString().split("\n");
        for (String line : lines) {
            if (line != null && line.length() > 0) {
                // Treat a perl response line :
                String[] elts = line.split(RESULT_SEPARATOR);

                List<ComponentImportNeeds> componentsToTreat = componentsByModules.get(elts[0]);

                if (componentsToTreat != null) {
                    // Define status regarding the perl response :
                    int status = ComponentImportNeeds.UNKNOWN;
                    if (elts[1].startsWith(RESULT_KEY_OK)) {
                        status = ComponentImportNeeds.INSTALLED;
                    } else if (elts[1].startsWith(RESULT_KEY_KO)) {
                        status = ComponentImportNeeds.NOT_INSTALLED;
                    }

                    // Step on objects using this module and set their status :
                    for (ComponentImportNeeds current : componentsToTreat) {
                        current.setStatus(status);
                    }
                }
            }

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent) {

        parent.setLayout(new FormLayout());
        FormData formData = new FormData();

        Composite rightPartComposite = new Composite(parent, SWT.NONE);
        formData = new FormData();
        formData.top = new FormAttachment(0);
        formData.left = new FormAttachment(0);
        formData.right = new FormAttachment(100);
        formData.bottom = new FormAttachment(100);
        rightPartComposite.setLayoutData(formData);
        rightPartComposite.setLayout(new FillLayout());

        tableViewerCreator = new TableViewerCreator(rightPartComposite);
        tableViewerCreator.setHeaderVisible(true);
        tableViewerCreator.setBorderVisible(true);
        tableViewerCreator.setLinesVisible(true);
        tableViewerCreator.setShowSelection(SHOW_SELECTION.FULL);
        tableViewerCreator.setVerticalScroll(true);
        tableViewerCreator.setCheckboxInFirstColumn(false);
        tableViewerCreator.setAllColumnsResizable(true);
        tableViewerCreator.setLayoutMode(LAYOUT_MODE.FILL_HORIZONTAL);
        tableViewerCreator.createTable();

        TableViewerCreatorColumn column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle("Status");
        column.setId(ID_STATUS);
        column.setSortable(true);
        column.setImageProvider(new StatusImageProvider());
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<ComponentImportNeeds, String>() {

            public String get(ComponentImportNeeds bean) {
                String str = null;
                switch (bean.getStatus()) {
                case ComponentImportNeeds.INSTALLED:
                    str = "Installed";
                    break;
                case ComponentImportNeeds.NOT_INSTALLED:
                    str = "Not installed";
                    break;
                default:
                    str = "Unknown";
                }
                return str;
            }

            public void set(ComponentImportNeeds bean, String value) {
            }
        });

        column.setWeight(3);
        column.setModifiable(false);

        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle("Component Name");
        column.setSortable(true);
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<ComponentImportNeeds, String>() {

            public String get(ComponentImportNeeds bean) {
                return bean.getComponentName();
            }

            public void set(ComponentImportNeeds bean, String value) {
            }
        });

        column.setModifiable(false);
        column.setWeight(3);

        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle("Module Name");
        column.setSortable(true);
        tableViewerCreator.setDefaultSort(column, SORT.ASC);
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<ComponentImportNeeds, String>() {

            public String get(ComponentImportNeeds bean) {
                return bean.getName();
            }

            public void set(ComponentImportNeeds bean, String value) {
            }
        });

        column.setModifiable(false);
        column.setWeight(3);

        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle("Required for");

        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<ComponentImportNeeds, String>() {

            public String get(ComponentImportNeeds bean) {
                return bean.getInformationMsg();
            }

            public void set(ComponentImportNeeds bean, String value) {
            }
        });

        column.setModifiable(true);
        column.setWeight(12);

        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle("Required");
        column.setImageProvider(new RequiredImageProvider());
        column.setSortable(true);
        column.setDisplayedValue("");
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<ComponentImportNeeds, String>() {

            public String get(ComponentImportNeeds bean) {
                return String.valueOf(bean.isRequired());
            }

            public void set(ComponentImportNeeds bean, String value) {
            }
        });

        column.setModifiable(false);
        column.setWeight(2);

        // check();
        tableViewerCreator.init(getCompList());

        makeActions();
        contributeToActionBars();
    }

    private void makeActions() {
        checkAction = new CheckAction(this);
    }

    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalToolBar(bars.getToolBarManager());
    }

    private void fillLocalToolBar(IToolBarManager manager) {
        manager.add(checkAction);
    }

    private static List<ComponentImportNeeds> getImportNeedsList() {
        List<ComponentImportNeeds> importNeedsList = new ArrayList<ComponentImportNeeds>();
        IComponentsFactory compFac = ComponentsFactoryProvider.getInstance();
        List<IComponent> componentList = compFac.getComponents();
        for (IComponent component : componentList) {
            if (component instanceof EmfComponent) {
                EmfComponent emfComponent = (EmfComponent) component;
                importNeedsList.addAll(emfComponent.getImportsNeeded());

            }
        }
        return importNeedsList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    @Override
    public void setFocus() {
        // TODO Auto-generated method stub

    }

}

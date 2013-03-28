// ============================================================================
//
// Copyright (C) 2006-2013 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.editor.properties.controllers;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.CorePlugin;
import org.talend.core.hadoop.version.custom.ECustomVersionGroup;
import org.talend.core.hadoop.version.custom.ECustomVersionType;
import org.talend.core.hadoop.version.custom.HadoopCustomVersionDefineDialog;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.properties.tab.IDynamicProperty;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.ui.editor.cmd.PropertyChangeCommand;

/**
 * DOC zwzhao class global comment. Detailled comment
 */
public class HadoopJarSetupController extends AbstractElementPropertySectionController {

    private String customJars;

    /**
     * DOC zwzhao HadoopJarSetupController constructor comment.
     * 
     * @param dp
     */
    public HadoopJarSetupController(IDynamicProperty dp) {
        super(dp);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent arg0) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.designer.core.ui.editor.properties.controllers.AbstractElementPropertySectionController#createControl
     * (org.eclipse.swt.widgets.Composite, org.talend.core.model.process.IElementParameter, int, int, int,
     * org.eclipse.swt.widgets.Control)
     */
    @Override
    public Control createControl(Composite subComposite, final IElementParameter param, int numInRow, int nbInRow, int top,
            Control lastControl) {
        final Composite container = subComposite;
        Button subButton = getWidgetFactory().createButton(container, "", SWT.PUSH);
        subButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                HadoopCustomVersionDefineDialog customVersionDialog = null;
                if (getCustomVersionMap().size() == 0) {
                    customVersionDialog = new HadoopCustomVersionDefineDialog(Display.getDefault().getActiveShell()) {

                        @Override
                        protected ECustomVersionType[] getDisplayTypes() {
                            return new ECustomVersionType[] { ECustomVersionType.OOZIE };
                        }
                    };
                } else {
                    customVersionDialog = new HadoopCustomVersionDefineDialog(Display.getDefault().getActiveShell(),
                            getCustomVersionMap()) {

                        @Override
                        protected ECustomVersionType[] getDisplayTypes() {
                            return new ECustomVersionType[] { ECustomVersionType.OOZIE };
                        }
                    };
                }
                if (customVersionDialog.open() == Window.OK) {
                    customJars = customVersionDialog.getLibListStr(ECustomVersionGroup.COMMON);
                    if (!customJars.isEmpty()) {
                        String[] array = customJars.split(";");
                        List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
                        for (int i = 0; i < array.length; i++) {
                            Map<String, Object> newLine = new HashMap<String, Object>();
                            newLine.put("JAR_NAME", array[i]);
                            values.add(newLine);
                        }
                        elem.setPropertyValue("CUSTOM_JARS", values);
                        elem.getElementParameter(EParameterName.UPDATE_COMPONENTS.getName()).setValue(true);
                    }
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {

            }

        });
        subButton.setImage(ImageProvider.getImage(CorePlugin.getImageDescriptor(DOTS_BUTTON)));
        Point s = subButton.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        FormData data = new FormData();
        data.left = new FormAttachment(lastControl, 0);
        data.right = new FormAttachment(lastControl, STANDARD_BUTTON_WIDTH, SWT.RIGHT);
        data.top = new FormAttachment(0, top);
        data.height = STANDARD_HEIGHT - 2;
        subButton.setLayoutData(data);

        return container;
    }

    private Map<String, Set<String>> getCustomVersionMap() {
        Map<String, Set<String>> map = new HashMap<String, Set<String>>();
        customJars = new String();
        List<Map<String, Object>> list = (List<Map<String, Object>>) elem.getPropertyValue("CUSTOM_JARS");
        for (Map<String, Object> mapJar : list) {
            String value = (String) mapJar.get("JAR_NAME");
            customJars += value + ";";
        }
        if (customJars != null && customJars.length() > 0) {
            customJars = customJars.substring(0, customJars.length() - 1);
        }
        if (StringUtils.isNotEmpty(customJars)) {
            Set<String> jarSet = new HashSet<String>();
            String[] jarArray = customJars.split(";"); //$NON-NLS-1$
            for (String jar : jarArray) {
                jarSet.add(jar);
            }
            map.put(ECustomVersionGroup.COMMON.getName(), jarSet);
        }

        return map;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.designer.core.ui.editor.properties.controllers.AbstractElementPropertySectionController#estimateRowSize
     * (org.eclipse.swt.widgets.Composite, org.talend.core.model.process.IElementParameter)
     */
    @Override
    public int estimateRowSize(Composite subComposite, IElementParameter param) {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.designer.core.ui.editor.properties.controllers.AbstractElementPropertySectionController#refresh(org
     * .talend.core.model.process.IElementParameter, boolean)
     */
    @Override
    public void refresh(IElementParameter param, boolean check) {
        // TODO Auto-generated method stub

    }

    class ModelChangeCommand extends Command {

        // private final IElementParameter param;

        private final String columnName;

        private final String[] value;

        private List<Map<String, Object>> values;

        public ModelChangeCommand(List<Map<String, Object>> values, String columnName, String[] value) {
            super();
            // this.param = param;
            this.columnName = columnName;
            this.value = value;
            this.values = values;
        }

        @Override
        public void execute() {
            for (int i = 0; i < values.size(); i++) {
                Map<String, Object> line = values.get(i);
                if (line != null) {
                    line.put(columnName, value[i]);
                }
            }
            // CorePlugin.getDefault().getLibrariesService().resetModulesNeeded();
        }

        @Override
        public void undo() {
            super.undo();
        }

    }

}

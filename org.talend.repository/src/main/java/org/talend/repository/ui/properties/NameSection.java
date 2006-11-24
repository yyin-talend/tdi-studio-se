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
package org.talend.repository.ui.properties;

import java.util.regex.Pattern;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.EImage;
import org.talend.core.ui.ImageProvider;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.views.RepositoryView;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class NameSection extends AbstractSection {

    private Text nameText;

    private CLabel statusText;

    @Override
    public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
        super.createControls(parent, aTabbedPropertySheetPage);

        Composite composite = getWidgetFactory().createFlatFormComposite(parent);
        FormData data;

        nameText = getWidgetFactory().createText(composite, ""); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(50, 0);
        data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
        nameText.setLayoutData(data);
        addFocusListener(nameText);
        nameText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                IStatus status = evaluateTextField();
                if (status.getSeverity() == IStatus.ERROR) {
                    statusText.setText(status.getMessage());
                    statusText.setVisible(true);
                } else {
                    statusText.setVisible(false);
                }
            }
        });

        CLabel labelLabel = getWidgetFactory().createCLabel(composite, "Name");
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(nameText, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(nameText, 0, SWT.CENTER);
        labelLabel.setLayoutData(data);

        statusText = getWidgetFactory().createCLabel(composite, "");
        data = new FormData();
        data.left = new FormAttachment(nameText, ITabbedPropertyConstants.HSPACE * 3);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(nameText, 0, SWT.CENTER);
        statusText.setLayoutData(data);
        statusText.setImage(ImageProvider.getImage(EImage.ERROR_ICON));
        statusText.setVisible(false);
    }

    protected IStatus evaluateTextField() {
        if (getObject().getType() == null || getObject().getType() == ERepositoryObjectType.FOLDER) {
            return createOkStatus();
        }

        if (nameText.getText().length() == 0) {
            return createStatus(IStatus.ERROR, "Name is empty.");
        } else if (!Pattern.matches(RepositoryConstants.FILE_PATTERN, nameText.getText())) {
            return createStatus(IStatus.ERROR, "Name contains incorrect characters.");
        } else if (!isValid(nameText.getText())) {
            return createStatus(IStatus.ERROR, "Item with the same name already exists.");
        } else {
            return createOkStatus();
        }
    }

    public boolean isValid(String itemName) {
        try {
            return getRepositoryFactory().isNameAvailable(getObject().getProperty().getItem(), itemName);
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
            return false;
        }
    }

    protected static IStatus createOkStatus() {
        return new Status(IStatus.OK, RepositoryPlugin.PLUGIN_ID, IStatus.OK, "", null); //$NON-NLS-1$
    }

    protected static IStatus createStatus(int severity, String message) {
        return new Status(severity, RepositoryPlugin.PLUGIN_ID, IStatus.OK, message, null);
    }

    @Override
    public void refresh() {
        nameText.setText(getName() != null ? getName().toString() : "");
    }

    protected String getName() {
        return getObject().getLabel();
    }

    @Override
    protected void beforeSave() {
        IStatus status = evaluateTextField();
        if (status.getSeverity() != IStatus.ERROR) {
            String text = nameText.getText();
            if (!text.equals(getObject().getLabel())) {
                if (getType() == ERepositoryObjectType.FOLDER) {
                    IPath path = RepositoryNodeUtilities.getPath(getNode());
                    try {
                        ERepositoryObjectType type = (ERepositoryObjectType) getNode().getProperties(EProperties.CONTENT_TYPE);
                        getRepositoryFactory().renameFolder(type, path, text);
                        RepositoryView view = (RepositoryView) getActivePage().findView(RepositoryView.VIEW_ID);
                        view.refresh();
                    } catch (PersistenceException e) {
                        e.printStackTrace();
                        return;
                    }

                }
                getObject().setLabel(text);
            }
        }
    }

    @Override
    protected void enableControl(boolean locked) {
        if (getType() == ERepositoryObjectType.FOLDER) {
            nameText.setEditable(true);
        } else {
            nameText.setEditable(locked);
        }
    }

    @Override
    protected void showControl(boolean visible) {
        nameText.getParent().setVisible(visible);
    }

}

/**
 * Copyright (C) 2006-2022 Talend Inc. - www.talend.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.talend.sdk.component.studio.ui.wizard.page;

import java.util.Set;

import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.metadata.managment.ui.model.IConnParamName;
import org.talend.metadata.managment.ui.utils.IRepositoryContextHandler;
import org.talend.metadata.managment.ui.utils.RepositoryContextManager;
import org.talend.metadata.managment.ui.wizard.AbstractForm;
import org.talend.sdk.component.studio.ui.composite.TaCoKitWizardComposite;

public class TaCokitForm extends AbstractForm {

    private TaCoKitWizardComposite composite;

    protected TaCokitForm(Composite parent, ConnectionItem connectionItem, boolean hasContextBtn, int style) {
        super(parent, style);
        setConnectionItem(connectionItem);
        setupForm(hasContextBtn);
        FormData data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.bottom = new FormAttachment(85, 0);
        setLayoutData(data);
    }

    public void setComposite(TaCoKitWizardComposite composite) {
        this.composite = composite;
    }

    @Override
    protected void exportAsContext() {

        collectConParameters();
        super.exportAsContext();
    }

    protected void collectConParameters() {

        Set<IConnParamName> parameters = null;

        for (IRepositoryContextHandler handler : RepositoryContextManager.getHandlers()) {
            if (connectionItem != null && handler.isRepositoryConType(connectionItem.getConnection())) {
                parameters = handler.collectConParameters(connectionItem.getConnection());
                break;
            }
        }
        if (parameters != null && parameters.size() > 0) {

            parameters.forEach(p -> {
                addContextParams(p, true);
            });
        }

    }

    @Override
    protected boolean checkFieldsValue() {
        return false;
    }

    @Override
    protected void initialize() {
        if (composite != null) {

            composite.updateParameter();

            composite.refresh();

        }

    }

    @Override
    protected void addFields() {

    }

    @Override
    protected void addFieldsListeners() {

    }

    @Override
    protected void adaptFormToReadOnly() {

    }

    @Override
    protected void addUtilsButtonListeners() {

    }

}

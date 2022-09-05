package org.talend.sdk.component.studio.ui.wizard.page;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.metadata.managment.ui.utils.IRepositoryContextHandler;
import org.talend.metadata.managment.ui.utils.RepositoryContextManager;
import org.talend.metadata.managment.ui.utils.ExtendedNodeConnectionContextUtils.EHadoopParamName;
import org.talend.metadata.managment.ui.utils.TaCoKitConnectionContextUtils.ETaCoKitParamName;
import org.talend.metadata.managment.ui.wizard.AbstractForm;
import org.talend.sdk.component.server.front.model.ConfigTypeNode;
import org.talend.sdk.component.studio.metadata.model.TaCoKitConfigurationModel;
import org.talend.sdk.component.studio.ui.composite.TaCoKitWizardComposite;

public class TaCokitForm extends AbstractForm {

	private TaCoKitWizardComposite composite;


	protected TaCokitForm(Composite parent, ConnectionItem connectionItem, int style) {
		super(parent, style);
		setConnectionItem(connectionItem);
		setupForm(true);
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
		
		Set<ETaCoKitParamName> parameters = null;

		for (IRepositoryContextHandler handler : RepositoryContextManager.getHandlers()) {
			if (connectionItem != null && handler.isRepositoryConType(connectionItem.getConnection())) {
				parameters = handler.collectConParameters();
				break;
			}
		}
		if (parameters != null && parameters.size()>0) {

			parameters.forEach(p -> {
				addContextParams(p, true);
			});
		}


	}

	@Override
	protected boolean checkFieldsValue() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void initialize() {
		if (composite != null) {

			composite.updateConfigurationModel(connectionItem.getConnection());

			composite.refresh();
		}

	}

	@Override
	protected void addUtilsButtonListeners() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void addFields() {

	}

	@Override
	protected void addFieldsListeners() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void adaptFormToReadOnly() {
		// TODO Auto-generated method stub

	}

}

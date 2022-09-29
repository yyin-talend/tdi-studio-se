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
		
		Set<IConnParamName> parameters = null;

		for (IRepositoryContextHandler handler : RepositoryContextManager.getHandlers()) {
			if (connectionItem != null && handler.isRepositoryConType(connectionItem.getConnection())) {
				parameters = handler.collectConParameters(connectionItem.getConnection());
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

			composite.updateParameter(connectionItem.getConnection());

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

package org.talend.sdk.component.studio.ui.wizard.page;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.metadata.managment.ui.utils.ExtendedNodeConnectionContextUtils.EHadoopParamName;
import org.talend.metadata.managment.ui.utils.TaCoKitConnectionContextUtils.ETaCoKitParamName;
import org.talend.metadata.managment.ui.wizard.AbstractForm;
import org.talend.sdk.component.studio.ui.composite.TaCoKitWizardComposite;

public class TaCokitForm extends AbstractForm{
	private TaCoKitWizardComposite composite;
	protected TaCokitForm(Composite parent,ConnectionItem connectionItem, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
		setConnectionItem(connectionItem);
		setupForm(true);
       
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
		addContextParams(ETaCoKitParamName.Account, true);
		addContextParams(ETaCoKitParamName.Email, true);
		addContextParams(ETaCoKitParamName.Password, true);
		addContextParams(ETaCoKitParamName.RoleId, true);
		addContextParams(ETaCoKitParamName.ApplicationId, true);

	}

	@Override
	protected boolean checkFieldsValue() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		if(composite!=null) {
			
			
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

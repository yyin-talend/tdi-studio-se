package org.talend.repository.model.migration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.components.ModifyComponentsAction;
import org.talend.core.model.components.conversions.IComponentConversion;
import org.talend.core.model.components.conversions.RemovePropertyComponentConversion;
import org.talend.core.model.components.filters.IComponentFilter;
import org.talend.core.model.components.filters.NameComponentFilter;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.migration.IProjectMigrationTask.ExecutionResult;
import org.talend.core.model.properties.ProcessItem;
import org.talend.designer.core.model.utils.emf.talendfile.ElementValueType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;

public class MoveExcelSheetnameToSheetlist extends AbstractJobMigrationTask {

	@Override
	public ExecutionResult executeOnProcess(ProcessItem item) {
        if (getProject().getLanguage() == ECodeLanguage.JAVA) {
            return ExecutionResult.NOTHING_TO_DO;
        }
		try {
			IComponentFilter filter1 = new NameComponentFilter(
					"tFileInputExcel"); //$NON-NLS-1$

			IComponentConversion addNewProperty = new IComponentConversion() {

				public void transform(NodeType node) {
					ComponentUtilities.addNodeProperty(node, "SHEETLIST",
							"TABLE");

					List<ElementValueType> values = new ArrayList<ElementValueType>();
					ElementValueType eValue = TalendFileFactory.eINSTANCE
							.createElementValueType();
					eValue.setElementRef("SHEETNAME");
					eValue.setValue(ComponentUtilities.getNodePropertyValue(
							node, "SHEETNAME"));
					values.add(eValue);

					ComponentUtilities.setNodeProperty(node, "SHEETLIST",
							values);
				}
			};
			IComponentConversion removeOldProperty = new RemovePropertyComponentConversion(
					"SHEETNAME"); //$NON-NLS-1$

			ModifyComponentsAction.searchAndModify(item, filter1, Arrays
					.<IComponentConversion> asList(addNewProperty,
							removeOldProperty));

			return ExecutionResult.SUCCESS_WITH_ALERT;
		} catch (Exception e) {
			ExceptionHandler.process(e);
			return ExecutionResult.FAILURE;
		}
	}

	public Date getOrder() {
		GregorianCalendar gc = new GregorianCalendar(2008, 2, 18, 10, 10, 0);
		return gc.getTime();
	}
}

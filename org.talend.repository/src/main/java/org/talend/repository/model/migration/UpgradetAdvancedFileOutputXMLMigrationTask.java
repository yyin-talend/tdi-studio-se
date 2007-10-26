package org.talend.repository.model.migration;

import java.util.Arrays;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.components.ModifyComponentsAction;
import org.talend.core.model.components.conversions.IComponentConversion;
import org.talend.core.model.components.conversions.UpdateMappingFortAdvancedXMLConversion;
import org.talend.core.model.components.filters.IComponentFilter;
import org.talend.core.model.components.filters.NameComponentFilter;
import org.talend.core.model.general.Project;
import org.talend.core.model.migration.AbstractMigrationTask;
import org.talend.core.model.migration.IProjectMigrationTask;

public class UpgradetAdvancedFileOutputXMLMigrationTask extends AbstractMigrationTask implements IProjectMigrationTask {

    public ExecutionResult execute(Project project) {
        if (project.getLanguage() != ECodeLanguage.JAVA) {
            return ExecutionResult.NOTHING_TO_DO;
        }
        try {
            IComponentFilter filter1 = new NameComponentFilter("tAdvancedFileOutputXML"); //$NON-NLS-1$

            IComponentConversion removeQuotes1 = new UpdateMappingFortAdvancedXMLConversion(); //$NON-NLS-1$
            ModifyComponentsAction.searchAndModify(filter1, Arrays.<IComponentConversion> asList(removeQuotes1));

            return ExecutionResult.SUCCESS_NO_ALERT;
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }
    }
}

package org.talend.designer.core.model.analysistask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.talend.analysistask.AbstractItemAnalysisTask;
import org.talend.analysistask.AnalysisReportRecorder;
import org.talend.analysistask.AnalysisReportRecorder.SeverityOption;
import org.talend.camel.core.model.camelProperties.CamelProcessItem;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.runtime.services.IGenericService;
import org.talend.core.ui.component.ComponentsFactoryProvider;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.model.utils.emf.talendfile.impl.NodeTypeImpl;

/**
 * Find if custom components are used in models and show warning message
 *
 */
public class CustomComponentsDepRiskAnalysisTask  extends AbstractItemAnalysisTask {

    @Override
    public List<AnalysisReportRecorder> execute(Item item) {

        Set<IComponent> components = ComponentsFactoryProvider.getInstance().getComponents();
        List<String> loadedCustomComponentsNameList = new ArrayList<String>();
        components.forEach(component ->  loadedCustomComponentsNameList.add(component.getName()));

        IGenericService genericService = GlobalServiceRegister.getDefault().getService(
                IGenericService.class);
        List<Map<String, String>> allLoadedomponentsInfo = genericService.getAllGenericComponentsInfo();
        List<String> allLoadedComponentsNameList = new ArrayList<String>();
        for(Map<String, String> component : allLoadedomponentsInfo) {
            allLoadedComponentsNameList.add(component.get("Name"));
        }

        ProcessType processType = ((CamelProcessItem) item).getProcess();
        EList<NodeTypeImpl> nodeList = processType.getNode();

        List<AnalysisReportRecorder> recordList = new ArrayList<AnalysisReportRecorder>();
        for(NodeTypeImpl node : nodeList) {
            String componentName = node.getComponentName();
            if(loadedCustomComponentsNameList.contains(componentName) 
                    || !allLoadedComponentsNameList.contains(componentName)) {
                
                recordList.add(new AnalysisReportRecorder(this, item, SeverityOption.CRITICAL,
                        Messages.getString("CustomComponent.updateComponentAndDependencies", 
                        componentName, componentName)));
            }
        }

        return recordList;
    }

    @Override
    public Set<ERepositoryObjectType> getRepositoryObjectTypeScope() {
        Set<ERepositoryObjectType> types = new HashSet<ERepositoryObjectType>();
        types.addAll(ERepositoryObjectType.getAllTypesOfCodes());
        return types;
    }
}

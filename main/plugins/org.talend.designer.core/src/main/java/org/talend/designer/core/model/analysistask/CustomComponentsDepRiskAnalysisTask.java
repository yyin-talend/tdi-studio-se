package org.talend.designer.core.model.analysistask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.talend.analysistask.AbstractItemAnalysisTask;
import org.talend.analysistask.AnalysisReportRecorder;
import org.talend.analysistask.AnalysisReportRecorder.SeverityOption;
import org.talend.core.i18n.Messages;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.component.ComponentsFactoryProvider;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;

/**
 * Find if custom components are used in models and show warning message
 *
 */
public class CustomComponentsDepRiskAnalysisTask  extends AbstractItemAnalysisTask {

    @Override
    public List<AnalysisReportRecorder> execute(Item item) {

        ProcessType processType = getProcessType(item);
        if (processType == null) {
            return null;
        }
        List<AnalysisReportRecorder> recordList = new ArrayList<AnalysisReportRecorder>();
        List<String> loadedCustomComponentsTypes = new ArrayList<String>();
        List<String> loadedAllComponentsTypes = new ArrayList<String>();

        Set<IComponent> components = ComponentsFactoryProvider.getInstance().getComponents();
        components.forEach(component ->  loadedAllComponentsTypes.add(component.getName()));

        List<IComponent> loadedCustomComponents = ComponentsFactoryProvider.getInstance().getCustomComponents();
        loadedCustomComponents.forEach(component ->  loadedCustomComponentsTypes.add(component.getName()));

        for(Object nodeObject : processType.getNode()) {
            NodeType nodeType = (NodeType) nodeObject;
            if(loadedCustomComponentsTypes.contains(nodeType.getComponentName())) {
                for (Object o : nodeType.getElementParameter()) {
                    ElementParameterType t = (ElementParameterType) o;
                    if ("UNIQUE_NAME".equals(t.getName())) {
                        String compnentNameVal = t.getValue();
                        if (compnentNameVal != null) {
                            recordList.add(new AnalysisReportRecorder(this, item, SeverityOption.WARNING,
                                    Messages.getString("CustomComponent.updateComponentAndDependencies",
                                            compnentNameVal, nodeType.getComponentName())));
                        }
                    }
                }
            }else if(!loadedAllComponentsTypes.contains(nodeType.getComponentName())){
                for (Object o : nodeType.getElementParameter()) {
                    ElementParameterType t = (ElementParameterType) o;
                    if ("UNIQUE_NAME".equals(t.getName())) {
                        String compnentNameVal = t.getValue();
                        if (compnentNameVal != null) {
                            recordList.add(new AnalysisReportRecorder(this, item, SeverityOption.WARNING,
                                    Messages.getString("CustomComponent.updateComponentAndDependencies",
                                            compnentNameVal, nodeType.getComponentName())));
                        }
                    }
                }
            }
        }
        return recordList;
    }

    @Override
    public Set<ERepositoryObjectType> getRepositoryObjectTypeScope() {
        Set<ERepositoryObjectType> types = new HashSet<ERepositoryObjectType>();
        types.addAll(ERepositoryObjectType.getAllTypesOfProcess());
        types.addAll(ERepositoryObjectType.getAllTypesOfBigDataProcess());
        types.addAll(ERepositoryObjectType.getAllTypesOfProcess2());
        types.addAll(ERepositoryObjectType.getAllTypesOfTestContainer());
        return types;
    }
}

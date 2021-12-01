package org.talend.designer.core.model.analysistask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.talend.analysistask.AbstractItemAnalysisTask;
import org.talend.analysistask.AnalysisReportRecorder;
import org.talend.analysistask.AnalysisReportRecorder.SeverityOption;
import org.talend.camel.core.model.camelProperties.CamelProcessItem;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;
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

        if(item instanceof CamelProcessItem) {
            Set<String> customComponents = new HashSet<String>();
            List<AnalysisReportRecorder> recordList = new ArrayList<AnalysisReportRecorder>();
            List<String> loadedCustomComponentsNameList = new ArrayList<String>();
            EList<NodeTypeImpl> nodeList = null;

            Set<IComponent> components = ComponentsFactoryProvider.getInstance().getComponents();
            components.forEach(component ->  loadedCustomComponentsNameList.add(component.getName()));

            List<IComponent> loadedCustomComponents = ComponentsFactoryProvider.getInstance().getCustomComponents();
            loadedCustomComponents.forEach(component ->  loadedCustomComponentsNameList.add(component.getName()));


            ProcessType processType = ((CamelProcessItem) item).getProcess();
            nodeList = processType.getNode();

            if(null != nodeList) {
                for(NodeTypeImpl node : nodeList) {
                    String componentName = node.getComponentName();
                    if(!loadedCustomComponentsNameList.contains(componentName)) {
                        customComponents.add(componentName);
                    }
                }
            }

            for(String customComponent : customComponents) {
                recordList.add(new AnalysisReportRecorder(this, item, SeverityOption.CRITICAL,
                        Messages.getString("CustomComponent.updateComponentAndDependencies", 
                                customComponent)));
            }

            return recordList;
        }
        return null;
    }

    @Override
    public Set<ERepositoryObjectType> getRepositoryObjectTypeScope() {
        Set<ERepositoryObjectType> types = new HashSet<ERepositoryObjectType>();
        types.addAll(ERepositoryObjectType.getAllTypesOfProcess());
        types.addAll(ERepositoryObjectType.getAllTypesOfProcess2());
        return types;
    }
}

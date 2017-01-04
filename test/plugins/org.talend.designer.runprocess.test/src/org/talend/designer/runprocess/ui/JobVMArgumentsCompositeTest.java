// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.runprocess.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.junit.Assert;
import org.junit.Test;
import org.talend.core.model.components.ComponentCategory;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.ui.component.ComponentsFactoryProvider;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainer;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.runprocess.ProcessMessage;
import org.talend.designer.runprocess.ProcessMessage.MsgType;
import org.talend.designer.runprocess.RunProcessContext;

/**
 * created by hwang on Dec 27, 2017 Detailled comment
 *
 */
public class JobVMArgumentsCompositeTest {

    /**
     * Test method for
     * {@link org.talend.designer.runprocess.ui.JobVMArgumentsComposite#readString(String stringList)}
     * .
     */
    @Test
    public void testReadString() {
        JobVMArgumentsComposite jobVM = new JobVMArgumentsComposite("VM", "VM", new Composite(new Shell(), SWT.NONE));
        String vm = null;
        Assert.assertTrue(jobVM.readString(vm).isEmpty());
        vm = "";
        Assert.assertTrue(jobVM.readString(vm).isEmpty());
        vm = "hhh jjjj kkkk";
        Assert.assertTrue(jobVM.readString(vm).isEmpty());
        String value = "{\"JOB_RUN_VM_ARGUMENTS\":[\"{[<,>]};:' \\\"/\\\\+\\\\t+\\\\b+\\\\f+\\\\n=\\\\r|\",\"String a = \\\\\\\\ + [array];\"]}";
        List<String> list = jobVM.readString(value);
        Assert.assertTrue(list.get(0).equals("{[<,>]};:' \"/\\+\\t+\\b+\\f+\\n=\\r|"));
        Assert.assertTrue(list.get(1).equals("String a = \\\\ + [array];"));
    }
    
    /**
     * Test method for
     * {@link org.talend.designer.runprocess.ui.JobVMArgumentsComposite#writeString(List<String> items)}
     * .
     */
    @Test
    public void testWriteString() {
        JobVMArgumentsComposite jobVM = new JobVMArgumentsComposite("VM", "VM", new Composite(new Shell(), SWT.NONE));
        List<String> list = new ArrayList<String>();
        String srg0 = "{[<,>]};:' \"/\\+\t+\b+\f+\n=\r|";
        list.add(srg0);
  
        String srg2 = "String a = \\\\ + [array];";
        list.add(srg2);
        
        String result = jobVM.writeString(list);
        String expectResult = "{\"JOB_RUN_VM_ARGUMENTS\":[\"{[<,>]};:' \\\"/\\\\+\\t+\\b+\\f+\\n=\\r|\",\"String a = \\\\\\\\ + [array];\"]}";
        Assert.assertTrue(result.equals(expectResult));
        
    }
}

// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.component.ui.wizard.internal;

import org.talend.components.api.properties.ComponentProperties;

/**
 * created by ycbai on 2015年11月27日 Detailled comment
 *
 */
public interface GenericComponentService {

    public abstract ComponentProperties validateProperty(String propName, ComponentProperties properties) throws Throwable;

    public abstract ComponentProperties beforePropertyActivate(String propName, ComponentProperties properties) throws Throwable;

    public abstract ComponentProperties beforePropertyPresent(String propName, ComponentProperties properties) throws Throwable;

    public abstract ComponentProperties afterProperty(String propName, ComponentProperties properties) throws Throwable;

    public abstract ComponentProperties beforeFormPresent(String formName, ComponentProperties properties) throws Throwable;

    public abstract ComponentProperties afterFormNext(String formName, ComponentProperties properties) throws Throwable;

    public abstract ComponentProperties afterFormBack(String formName, ComponentProperties properties) throws Throwable;

    public abstract ComponentProperties afterFormFinish(String formName, ComponentProperties properties) throws Throwable;

}
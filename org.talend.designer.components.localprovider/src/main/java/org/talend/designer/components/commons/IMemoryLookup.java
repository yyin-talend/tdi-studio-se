// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================

package org.talend.designer.components.commons;

import java.io.IOException;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * @param <K> K
 * @param <V> V
 */
public interface IMemoryLookup<K, V> {

    public void initPut();

    public V put(V bean);

    public void endPut();

    public void initGet();

    public void lookup(K key);

    public boolean hasNext();

    public V next();

    public void endGet();

}

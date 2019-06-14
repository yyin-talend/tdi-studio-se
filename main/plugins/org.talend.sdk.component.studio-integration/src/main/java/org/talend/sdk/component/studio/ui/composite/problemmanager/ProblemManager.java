// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.sdk.component.studio.ui.composite.problemmanager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.talend.core.model.process.IElementParameter;
import org.talend.sdk.component.studio.ui.composite.TaCoKitComposite;
import org.talend.sdk.component.studio.util.TaCoKitUtil;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class ProblemManager implements IProblemManager {

    private TaCoKitComposite tckComposite;

    private Map<IElementParameter, String> errMap = new HashMap<>();

    private Map<IElementParameter, String> warnMap = new HashMap<>();

    private Map<IElementParameter, String> infoMap = new HashMap<>();

    private Set<IElementParameter> unresolvedRequriedElems = new HashSet<>();

    @Override
    public TaCoKitComposite getTckComposite() {
        return tckComposite;
    }

    @Override
    public void setTckComposite(TaCoKitComposite tckComposite) {
        this.tckComposite = tckComposite;
    }

    @Override
    public void setError(IElementParameter ep, String errMsg) {
        if (TaCoKitUtil.isBlank(errMsg)) {
            errMap.remove(ep);
        } else {
            errMap.put(ep, errMsg);
        }
    }

    @Override
    public boolean hasError() {
        return !errMap.isEmpty();
    }

    @Override
    public void setWarn(IElementParameter ep, String warnMsg) {
        if (TaCoKitUtil.isBlank(warnMsg)) {
            warnMap.remove(ep);
        } else {
            warnMap.put(ep, warnMsg);
        }
    }

    @Override
    public boolean hasWarn() {
        return !warnMap.isEmpty();
    }

    @Override
    public void setInfo(IElementParameter ep, String infoMsg) {
        if (TaCoKitUtil.isBlank(infoMsg)) {
            infoMap.remove(ep);
        } else {
            infoMap.put(ep, infoMsg);
        }
    }

    @Override
    public boolean hasInfo() {
        return !infoMap.isEmpty();
    }

    @Override
    public void addUnresolvedRequriedElem(IElementParameter ep) {
        unresolvedRequriedElems.add(ep);
    }

    @Override
    public void removeUnresolvedRequriedElem(IElementParameter ep) {
        unresolvedRequriedElems.remove(ep);
    }

    @Override
    public boolean hasUnresolvedRequiredElem() {
        return !unresolvedRequriedElems.isEmpty();
    }
}

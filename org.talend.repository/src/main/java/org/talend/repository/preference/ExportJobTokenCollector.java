// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.preference;

import org.talend.core.token.AbstractTokenCollector;
import org.talend.core.token.PrefTokenKey;
import org.talend.repository.RepositoryPlugin;

import us.monoid.json.JSONObject;

/**
 * ggu class global comment. Detailled comment
 */
public class ExportJobTokenCollector extends AbstractTokenCollector {

    public static final PrefTokenKey NUM_JOB_EXPORT = new PrefTokenKey("jobExports", "times_job_export"); //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * ggu ExportJobTokenCollector constructor comment.
     */
    public ExportJobTokenCollector() {
    }

    @Override
    protected void collectProperties(JSONObject propertiesObject) throws Exception {
        int num = RepositoryPlugin.getDefault().getPreferenceStore().getInt(NUM_JOB_EXPORT.getPrefKey());
        propertiesObject.put(NUM_JOB_EXPORT.getKey(), num);
    }

}

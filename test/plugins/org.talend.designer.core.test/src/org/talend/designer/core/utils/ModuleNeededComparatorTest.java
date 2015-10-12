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
package org.talend.designer.core.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.talend.core.model.general.ModuleNeeded;

/**
 * Test class for the ModuleNeededComparator.
 *
 */
public class ModuleNeededComparatorTest {

    private final List<ModuleNeeded> neededModules = new ArrayList<>();

    private final List<ModuleNeeded> refNeededModules = new ArrayList<>();

    private static final String EMPTY = ""; //$NON-NLS-1$

    private static final String BUNDLE_NAME = "bundle1"; //$NON-NLS-1$

    private static final String MODULE1 = "module1", MODULE2 = "module2"; //$NON-NLS-1$ //$NON-NLS-2$

    @Before
    public void setUp() {

        ModuleNeeded m1 = new ModuleNeeded(EMPTY, MODULE1, EMPTY, false);
        m1.setBundleName(EMPTY);
        ModuleNeeded m2 = new ModuleNeeded(EMPTY, MODULE1, EMPTY, false);
        m2.setBundleName(BUNDLE_NAME);
        ModuleNeeded m3 = new ModuleNeeded(EMPTY, MODULE1, EMPTY, true);
        ModuleNeeded m4 = new ModuleNeeded(EMPTY, MODULE1, EMPTY, true);
        m4.setBundleName(BUNDLE_NAME);
        ModuleNeeded m5 = new ModuleNeeded(EMPTY, MODULE1, EMPTY, true);
        ModuleNeeded m6 = new ModuleNeeded(EMPTY, MODULE1, EMPTY, true);
        m6.setMrRequired(true);
        ModuleNeeded m11 = new ModuleNeeded(EMPTY, MODULE2, EMPTY, false);
        m11.setBundleName(EMPTY);
        ModuleNeeded m12 = new ModuleNeeded(EMPTY, MODULE2, EMPTY, false);
        m12.setBundleName(BUNDLE_NAME);
        ModuleNeeded m13 = new ModuleNeeded(EMPTY, MODULE2, EMPTY, true);
        ModuleNeeded m14 = new ModuleNeeded(EMPTY, MODULE2, EMPTY, false);
        m14.setBundleName(BUNDLE_NAME);
        ModuleNeeded m15 = new ModuleNeeded(EMPTY, MODULE2, EMPTY, true);
        ModuleNeeded m16 = new ModuleNeeded(EMPTY, MODULE2, EMPTY, true);
        m16.setMrRequired(true);

        neededModules.add(m11);
        neededModules.add(m3);
        neededModules.add(m2);
        neededModules.add(m12);
        neededModules.add(m16);
        neededModules.add(m5);
        neededModules.add(m4);
        neededModules.add(m1);
        neededModules.add(m15);
        neededModules.add(m13);
        neededModules.add(m6);
        neededModules.add(m14);

        refNeededModules.add(m4);
        refNeededModules.add(m2);
        refNeededModules.add(m1);
        refNeededModules.add(m6);
        refNeededModules.add(m3);
        refNeededModules.add(m5);
        refNeededModules.add(m14);
        refNeededModules.add(m12);
        refNeededModules.add(m11);
        refNeededModules.add(m16);
        refNeededModules.add(m13);
        refNeededModules.add(m15);
    }

    @Test
    public void compareTest() {
        Collections.sort(neededModules, new ModuleNeededComparator());
        assertEquals(neededModules, refNeededModules);
    }
}

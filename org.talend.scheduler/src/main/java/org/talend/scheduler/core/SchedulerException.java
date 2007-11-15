// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.scheduler.core;

/**
 * Detailled comment for this class. <br/> $Id$
 * 
 * @author phou
 * 
 */
public class SchedulerException extends Exception {

    private static final long serialVersionUID = 2657307347031538846L;

    public SchedulerException() {
        super();
    }

    public SchedulerException(String message) {
        super(message);
    }

    public SchedulerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SchedulerException(Throwable cause) {
        super(cause);
    }

}

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
package org.talend.spark.function;

import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;

public class DistinctFunction implements Function<JavaRDD<List<Object>>, JavaRDD<List<Object>>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JavaRDD<List<Object>> call(JavaRDD<List<Object>> rdd) throws Exception {
		return (JavaRDD<List<Object>>)rdd.distinct();
	}
}
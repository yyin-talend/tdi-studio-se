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

import java.util.Comparator;
import java.util.List;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.Function;

public class SortFunction implements Function<JavaPairRDD<List<Object>, List<Object>>, JavaPairRDD<List<Object>, List<Object>>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Comparator<List<Object>> comparator;

	public SortFunction(Comparator<List<Object>> comparator) {
		this.comparator = comparator;
	}

	public JavaPairRDD<List<Object>, List<Object>> call(JavaPairRDD<List<Object>, List<Object>> rdd) throws Exception {
		return (JavaPairRDD<List<Object>, List<Object>>)rdd.sortByKey(this.comparator);
	}
}

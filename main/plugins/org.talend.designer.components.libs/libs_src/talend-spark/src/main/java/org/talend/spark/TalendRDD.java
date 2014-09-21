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
package org.talend.spark;

import org.talend.spark.function.FilterColumnsFunction;
import org.talend.spark.function.FilterRowFunction;
import org.talend.spark.function.KeyByCompareColFunction;
import org.talend.spark.function.KeyByFunction;
import org.talend.spark.function.NormalizeFunction;
import org.talend.spark.function.RDDConverterFunction;
import org.talend.spark.function.StoreJavaRDDFunction;


public abstract class TalendRDD<T> {
	public abstract <K2,V2> TalendPairRDD<K2, V2> mapToPair(KeyByFunction func);
	public abstract <K2,V2> TalendPairRDD<K2, V2> mapToPair(KeyByCompareColFunction func);
	
	public abstract <R> TalendRDD<R> map(StoreJavaRDDFunction func);
	public abstract <R> TalendRDD<R> map(RDDConverterFunction func);
	public abstract <R> TalendRDD<R> map(FilterColumnsFunction func);
	
	public abstract TalendRDD<T> filter(FilterRowFunction func);
	public abstract <U> TalendRDD<T> sample(boolean isWithReplacement, double fraction, int seed);
	public abstract <U> TalendRDD<T> distinct();
	public abstract TalendRDD<T> union(TalendRDD<T> rdd);
	public abstract TalendRDD<T> getTalendRDD();
	
	public abstract void collect();
	public abstract void saveAsTextFile(String filename);
	public abstract <U> TalendRDD<U> flatMap(NormalizeFunction normalizeFunction);
}

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

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.talend.spark.function.DistinctFunction;
import org.talend.spark.function.FilterColumnsFunction;
import org.talend.spark.function.FilterRowFunction;
import org.talend.spark.function.KeyByCompareColFunction;
import org.talend.spark.function.KeyByFunction;
import org.talend.spark.function.NormalizeFunction;
import org.talend.spark.function.RDDConverterFunction;
import org.talend.spark.function.SampleFunction;
import org.talend.spark.function.StoreJavaRDDFunction;

public class TalendDStreamRDD<T> extends TalendRDD<T> {
	private JavaDStream<T> rdd;

	public TalendDStreamRDD(JavaDStream<T> rdd) {
		this.rdd = rdd;
	}

	public JavaDStream<T> getRdd() {
		return rdd;
	}

	@Override
	public TalendRDD<T> getTalendRDD() {
		return this;
	}

	public void setRdd(JavaDStream<T> rdd) {
		this.rdd = rdd;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K2,V2> TalendPairRDD<K2, V2> mapToPair(KeyByFunction func) {
		return new TalendDStreamPairRDD<K2, V2>(
				this.rdd.mapToPair((PairFunction<T, K2, V2>) func));
	}

	@Override
	public void saveAsTextFile(String filename) {
		this.rdd.dstream().saveAsTextFiles(filename, "");
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R> TalendRDD<R> map(StoreJavaRDDFunction func) {
		return new TalendDStreamRDD<R>(this.rdd.map((Function<T, R>) func));
	}

	@Override
	public void collect() {
		this.rdd.print();
	}

	@SuppressWarnings("unchecked")
	@Override
	public TalendRDD<T> filter(FilterRowFunction func) {
		return new TalendDStreamRDD<T>(
				this.rdd.filter((Function<T, Boolean>) func));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R> TalendRDD<R> map(FilterColumnsFunction func) {
		return new TalendDStreamRDD<R>(this.rdd.map((Function<T, R>) func));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K2,V2> TalendPairRDD<K2, V2> mapToPair(KeyByCompareColFunction func) {
		return new TalendDStreamPairRDD<K2, V2>(
				this.rdd.mapToPair((PairFunction<T, K2, V2>) func));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R> TalendRDD<R> map(RDDConverterFunction func) {
		return new TalendDStreamRDD<R>(this.rdd.map((Function<T, R>) func));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <U> TalendRDD<T> sample(boolean isWithReplacement, double fraction,
			int seed) {
		Function<U, U> func = (Function<U, U>) new SampleFunction(
				isWithReplacement, fraction, seed);
		return new TalendDStreamRDD<T>(
				rdd.transform((Function<JavaRDD<T>, JavaRDD<T>>) func));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <U> TalendRDD<T> distinct() {
		Function<U, U> func = (Function<U, U>) new DistinctFunction();
		return new TalendDStreamRDD<T>(
				rdd.transform((Function<JavaRDD<T>, JavaRDD<T>>) func));
	}

	@Override
	public TalendRDD<T> union(TalendRDD<T> rdd) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <U> TalendRDD<U> flatMap(NormalizeFunction normalizeFunction) {
		return new TalendDStreamRDD<U>(this.rdd.flatMap((FlatMapFunction<T, U>) normalizeFunction));
	}
}

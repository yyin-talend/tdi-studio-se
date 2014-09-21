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
import org.talend.spark.function.FilterColumnsFunction;
import org.talend.spark.function.FilterRowFunction;
import org.talend.spark.function.KeyByCompareColFunction;
import org.talend.spark.function.KeyByFunction;
import org.talend.spark.function.NormalizeFunction;
import org.talend.spark.function.RDDConverterFunction;
import org.talend.spark.function.StoreJavaRDDFunction;

public class TalendJavaRDD<T> extends TalendRDD<T> {
	private JavaRDD<T> rdd;

	public TalendJavaRDD(JavaRDD<T> rdd) {
		this.rdd = rdd;
	}

	public JavaRDD<T> getRdd() {
		return rdd;
	}

	public TalendRDD<T> getTalendRDD() {
		return this;
	}

	public void setRdd(JavaRDD<T> rdd) {
		this.rdd = rdd;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K2, V2> TalendPairRDD<K2, V2> mapToPair(KeyByFunction func) {
		return new TalendJavaPairRDD<K2, V2>(
				this.rdd.mapToPair((PairFunction<T, K2, V2>) func));
	}

	@Override
	public void saveAsTextFile(String filename) {
		this.rdd.saveAsTextFile(filename);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R> TalendRDD<R> map(StoreJavaRDDFunction func) {
		return new TalendJavaRDD<R>(this.rdd.map((Function<T, R>) func));
	}

	@Override
	public void collect() {
		for (T row : this.rdd.collect()) {
			System.out.println(row);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public TalendRDD<T> filter(FilterRowFunction func) {
		return new TalendJavaRDD<T>(
				this.rdd.filter((Function<T, Boolean>) func));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R> TalendRDD<R> map(FilterColumnsFunction func) {
		return new TalendJavaRDD<R>(this.rdd.map((Function<T, R>) func));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K2, V2> TalendPairRDD<K2, V2> mapToPair(KeyByCompareColFunction func) {
		return new TalendJavaPairRDD<K2, V2>(
				this.rdd.mapToPair((PairFunction<T, K2, V2>) func));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R> TalendRDD<R> map(RDDConverterFunction func) {
		return new TalendJavaRDD<R>(this.rdd.map((Function<T, R>) func));
	}

	@Override
	public TalendRDD<T> sample(boolean isWithReplacement, double fraction,
			int seed) {
		return new TalendJavaRDD<T>(this.rdd.sample(isWithReplacement,
				fraction, seed));
	}

	@Override
	public TalendRDD<T> distinct() {
		return new TalendJavaRDD<T>(this.rdd.distinct());
	}

	@Override
	public TalendRDD<T> union(TalendRDD<T> rdd) {
		return new TalendJavaRDD<T>(this.rdd.union(((TalendJavaRDD<T>) rdd)
				.getRdd()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <U> TalendRDD<U> flatMap(NormalizeFunction normalizeFunction) {
		return new TalendJavaRDD<U>(this.rdd.flatMap((FlatMapFunction<T, U>) normalizeFunction));
	}
}

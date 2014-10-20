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

import java.util.Comparator;
import java.util.List;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.talend.spark.function.AggregateFunction;
import org.talend.spark.function.CoGroupJoinFunction;
import org.talend.spark.function.InnerJoinFunction;
import org.talend.spark.function.LeftJoinFunction;
import org.talend.spark.utils.SortComparator;

import scala.Tuple2;

import com.google.common.base.Optional;

public class TalendJavaPairRDD<K, V> extends TalendPairRDD<K, V> {
	private JavaPairRDD<K, V> rdd;

	public TalendJavaPairRDD(JavaPairRDD<K, V> rdd) {
		this.rdd = rdd;
	}

	public JavaPairRDD<K, V> getRdd() {
		return rdd;
	}

	@Override
	public TalendPairRDD<K, V> getTalendRDD() {
		return this;
	}

	public void setRdd(JavaPairRDD<K, V> rdd) {
		this.rdd = rdd;
	}

	@Override
	public TalendPairRDD<K, Iterable<V>> groupByKey() {
		return new TalendJavaPairRDD<K, Iterable<V>>(this.rdd.groupByKey());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K2, V2> TalendPairRDD<K, V> sortByKey(SortComparator comparator) {
		return new TalendJavaPairRDD<K, V>(
				this.rdd.sortByKey((Comparator<K>) comparator));
	}

	@Override
	public TalendRDD<Tuple2<K, V>> toJavaRDD() {
		return new TalendJavaRDD<Tuple2<K, V>>(rdd.rdd().toJavaRDD());
	}

	@Override
	public <W> TalendPairRDD<K, Tuple2<V, Optional<W>>> leftOuterJoin(
			TalendPairRDD<K, W> dataM2) {
		return new TalendJavaPairRDD<K, Tuple2<V, Optional<W>>>(
				this.rdd.leftOuterJoin(((TalendJavaPairRDD<K, W>) dataM2)
						.getRdd()));
	}

	@Override
	public <W> TalendPairRDD<K, Tuple2<V, W>> join(TalendPairRDD<K, W> dataM2) {
		return new TalendJavaPairRDD<K, Tuple2<V, W>>(
				this.rdd.join(((TalendJavaPairRDD<K, W>) dataM2).getRdd()));
	}

	@SuppressWarnings("unchecked")
	public <K2, V2> TalendPairRDD<K2, V2> mapToPair(
			InnerJoinFunction innerJoinFunction) {
		JavaPairRDD<List<Object>, Tuple2<List<Object>, List<Object>>> pair = ((TalendJavaPairRDD<List<Object>, Tuple2<List<Object>, List<Object>>>) this).rdd;
		return new TalendJavaPairRDD<K2, V2>(
				pair.mapToPair((PairFunction<Tuple2<List<Object>, Tuple2<List<Object>, List<Object>>>, K2, V2>) innerJoinFunction));
	}

	@SuppressWarnings("unchecked")
	public <K2, V2> TalendPairRDD<K2, V2> mapToPair(
			LeftJoinFunction leftJoinFunction) {
		JavaPairRDD<List<Object>, Tuple2<List<Object>, Optional<List<Object>>>> pair = ((TalendJavaPairRDD<List<Object>, Tuple2<List<Object>, Optional<List<Object>>>>) this).rdd;
		return new TalendJavaPairRDD<K2, V2>(
				pair.mapToPair((PairFunction<Tuple2<List<Object>, Tuple2<List<Object>, Optional<List<Object>>>>, K2, V2>) leftJoinFunction));
	}

	@SuppressWarnings("unchecked")
	public <K2, V2> TalendPairRDD<K2, V2> mapToPair(
			CoGroupJoinFunction coGroupJoinFunction) {
		JavaPairRDD<List<Object>, Tuple2<Iterable<List<Object>>, Iterable<List<Object>>>> pair = ((TalendJavaPairRDD<List<Object>, Tuple2<Iterable<List<Object>>, Iterable<List<Object>>>>) this).rdd;
		return new TalendJavaPairRDD<K2, V2>(
				pair.mapToPair((PairFunction<Tuple2<List<Object>, Tuple2<Iterable<List<Object>>, Iterable<List<Object>>>>, K2, V2>) coGroupJoinFunction));
	}

	@Override
	public <W> TalendPairRDD<K, Tuple2<Iterable<V>, Iterable<W>>> cogroup(
			TalendPairRDD<K, W> dataM2) {
		return new TalendJavaPairRDD<K, Tuple2<Iterable<V>, Iterable<W>>>(
				this.rdd.cogroup(((TalendJavaPairRDD<K, W>) dataM2).getRdd()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public TalendPairRDD<K, V> reduceByKey(AggregateFunction aggregateFunction) {
		return new TalendJavaPairRDD<K, V>(
				this.rdd.reduceByKey((Function2<V, V, V>) aggregateFunction));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <U> TalendPairRDD<K, U> mapValues(AggregateFunction aggregateFunction) {
		return new TalendJavaPairRDD<K, U>(
				this.rdd.mapValues((Function<V, U>) aggregateFunction));
	}

	@Override
	public <R> TalendRDD<R> map(Function<Tuple2<K, V>, R> function) {
		return new TalendJavaRDD<R>(this.rdd.map(function));
	}
}

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

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.talend.spark.function.AggregateFunction;
import org.talend.spark.function.CoGroupJoinFunction;
import org.talend.spark.function.InnerJoinFunction;
import org.talend.spark.function.LeftJoinFunction;
import org.talend.spark.function.SortFunction;
import org.talend.spark.utils.SortComparator;

import scala.Tuple2;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.google.common.base.Optional;

public class TalendDStreamPairRDD<K, V> extends TalendPairRDD<K, V> {
	private JavaPairDStream<K, V> rdd;

	public TalendDStreamPairRDD(JavaPairDStream<K, V> rdd) {
		this.rdd = rdd;
	}

	public JavaPairDStream<K, V> getRdd() {
		return rdd;
	}
	
	@Override
	public TalendPairRDD<K, V> getTalendRDD() {
		return this;
	}

	public void setRdd(JavaPairDStream<K, V> rdd) {
		this.rdd = rdd;
	}

	@Override
	public TalendPairRDD<K, Iterable<V>> groupByKey() {
		return new TalendDStreamPairRDD<K, Iterable<V>>(this.rdd.groupByKey());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K2, V2> TalendPairRDD<K, V> sortByKey(SortComparator comparator) {
		Function<K2, V2> func = (Function<K2, V2>) new SortFunction(comparator);
		return new TalendDStreamPairRDD<K, V>(
				rdd.transformToPair((Function<JavaPairRDD<K, V>, JavaPairRDD<K, V>>) func));
	}

	@Override
	public TalendRDD<Tuple2<K, V>> toJavaRDD() {
		return new TalendDStreamRDD<Tuple2<K, V>>(rdd.toJavaDStream());
	}

	@Override
	public <W> TalendPairRDD<K, Tuple2<V, Optional<W>>> leftOuterJoin(
			TalendPairRDD<K, W> dataM2) {
		throw new NotImplementedException();
	}

	@Override
	public <W> TalendPairRDD<K, Tuple2<V, W>> join(TalendPairRDD<K, W> dataM2) {
		throw new NotImplementedException();
	}

	@Override
	public <K2, V2> TalendPairRDD<K2, V2> mapToPair(
			InnerJoinFunction innerJoinFunction) {
		throw new NotImplementedException();
	}

	@Override
	public <K2, V2> TalendPairRDD<K2, V2> mapToPair(
			LeftJoinFunction leftJoinFunction) {
		throw new NotImplementedException();
	}

	@Override
	public <K2, V2> TalendPairRDD<K2, V2> mapToPair(
			CoGroupJoinFunction coGroupJoinFunction) {
		throw new NotImplementedException();
	}

	@Override
	public <W> TalendPairRDD<K, Tuple2<Iterable<V>, Iterable<W>>> cogroup(
			TalendPairRDD<K, W> dataM2) {
		throw new NotImplementedException();
	}

	@SuppressWarnings("unchecked")
	@Override
	public TalendPairRDD<K, V> reduceByKey(AggregateFunction aggregateFunction) {
		return new TalendDStreamPairRDD<K, V>(this.rdd.reduceByKey((Function2<V, V, V>) aggregateFunction));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <U> TalendPairRDD<K, U> mapValues(AggregateFunction aggregateFunction) {
		return new TalendDStreamPairRDD<K, U>(
				this.rdd.mapValues((Function<V, U>) aggregateFunction));
	}

	@Override
	public <R> TalendRDD<R> map(Function<Tuple2<K, V>, R> function) {
		return new TalendDStreamRDD<R>(this.rdd.map(function));
	}
}

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

import org.apache.spark.api.java.function.Function;
import org.talend.spark.function.AggregateFunction;
import org.talend.spark.function.CoGroupJoinFunction;
import org.talend.spark.function.InnerJoinFunction;
import org.talend.spark.function.LeftJoinFunction;
import org.talend.spark.utils.SortComparator;

import scala.Tuple2;

import com.google.common.base.Optional;

public abstract class TalendPairRDD<K, V> {
	public abstract TalendPairRDD<K, V> getTalendRDD();
	public abstract TalendRDD<Tuple2<K, V>> toJavaRDD();
	
	public abstract <U> TalendPairRDD<K, U> mapValues(AggregateFunction aggregateFunction);
	public abstract <R> TalendRDD<R> map(Function<Tuple2<K, V>, R> function);
	
	public abstract TalendPairRDD<K, Iterable<V>> groupByKey();
	public abstract <K2, V2> TalendPairRDD<K, V> sortByKey(SortComparator comparator);
	public abstract TalendPairRDD<K, V> reduceByKey(AggregateFunction aggregateFunction);
	
	public abstract <W> TalendPairRDD<K, Tuple2<V, Optional<W>>> leftOuterJoin(TalendPairRDD<K, W> dataM2);
	public abstract <W> TalendPairRDD<K, Tuple2<V,W>> join(TalendPairRDD<K, W> dataM2);
	public abstract <W> TalendPairRDD<K, Tuple2<Iterable<V>,Iterable<W>>> cogroup(TalendPairRDD<K, W> dataM2);
		
	public abstract <K2,V2> TalendPairRDD<K2,V2> mapToPair(InnerJoinFunction innerJoinFunction);
	public abstract <K2,V2> TalendPairRDD<K2,V2> mapToPair(LeftJoinFunction leftJoinFunction);
	public abstract <K2,V2> TalendPairRDD<K2,V2> mapToPair(CoGroupJoinFunction coGroupJoinFunction);
}

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

import org.apache.spark.api.java.function.Function;
import org.talend.spark.utils.FilterObject;
import org.talend.spark.utils.Utils;

public class FilterRowFunction implements Function<List<Object>, Boolean> {
	private static final long serialVersionUID = 1L;
	private List<FilterObject> filters;

	public FilterRowFunction(List<FilterObject> list) {
		this.filters = list;
	}

	public Boolean call(List<Object> row) throws Exception {
		Boolean result = true;
		if (filters == null || filters.isEmpty()) {
			return result;
		}
		for (int i = 0; i < filters.size(); i++) {
			FilterObject filter = filters.get(i);
			boolean tempResult = true;
			if(row.get(filter.idCol)==null && !filter.isNullable) {
				throw new Exception("Null value found in a non-nullable column.");
			}
			switch (filter.op) {
			case EQUAL:
				tempResult = (Utils.compareTo(row.get(filter.idCol), filter.value, filter.javaType) == 0);
				break;
			case DIFF:
				tempResult = (Utils.compareTo(row.get(filter.idCol), filter.value, filter.javaType) != 0);
				break;
			case SUP:
				tempResult = (Utils.compareTo(row.get(filter.idCol), filter.value, filter.javaType) > 0);
				break;
			case SUP_EQUAL:
				tempResult = (Utils.compareTo(row.get(filter.idCol), filter.value, filter.javaType) >= 0);
				break;
			case INF:
				tempResult = (Utils.compareTo(row.get(filter.idCol), filter.value, filter.javaType) < 0);
				break;
			case INF_EQUAL:
				tempResult = (Utils.compareTo(row.get(filter.idCol), filter.value, filter.javaType) <= 0);
				break;
			}
			if (i == 0) {
				result = tempResult;
			} else {
				if (filter.logicOp == FilterObject.LogicOp.OR) {
					result = result || tempResult;
				} else {
					result = result && tempResult;
				}
			}

		}
		return result;
	}
}

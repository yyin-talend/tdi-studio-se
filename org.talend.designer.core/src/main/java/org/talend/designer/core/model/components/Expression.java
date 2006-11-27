// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.designer.core.model.components;

import java.util.List;

import org.talend.core.model.process.IElementParameter;

/**
 * This class will test an expression in the element parameters. <br>
 * The expression can be for example: <br>
 * ((VAR1 == 'value1' and VAR2 == 'value2') or (VAR3 != 'value3')) or (VAR4 ==
 * 'value4') <br>
 * With VAR1, VAR2, VAR3 & VAR4 as the name of differents parameters and
 * 'value1'.. the values to test. (values must be between quotes)<br>
 * 
 * $Id: Expression.java 301 2006-11-02 13:10:03 +0000 (jeu., 02 nov. 2006)
 * smallet $
 * 
 */
public final class Expression {

	private Expression leftExpression;

	private Expression rightExpression;

	private String condition; // and / or

	private String expressionString;

	private boolean valid;

	private static final String AND = "and";

	private static final String OR = "or";

	private static final String EQUALS = "==";

	private static final String NOT_EQUALS = "!=";

	private Expression(String expressionString) {
		this.expressionString = expressionString;
	}

	private String getExpressionString() {
		return this.expressionString;
	}

	private void setExpressionString(String value) {
		this.expressionString = value;
	}

	private String getCondition() {
		return this.condition;
	}

	private void setCondition(String condition) {
		this.condition = condition;
	}

	private Expression getLeftExpression() {
		return this.leftExpression;
	}

	private void setLeftExpression(Expression leftExpression) {
		this.leftExpression = leftExpression;
	}

	private Expression getRightExpression() {
		return this.rightExpression;
	}

	private void setRightExpression(Expression rightExpression) {
		this.rightExpression = rightExpression;
	}

	private boolean isValid() {
		return this.valid;
	}

	private void setValid(boolean value) {
		this.valid = value;
	}

	public static boolean evaluate(final String string,
			List<? extends IElementParameter> listParam) {
		if (string.contains("(")
				&& (isThereCondition(string, AND) || isThereCondition(string,
						OR))) {
			return evaluateExpression(new Expression(string), listParam)
					.isValid();
		} else {
			String newValue; // remove brackets
			newValue = string.replace("(", "");
			newValue = newValue.replace(")", "");
			return evaluateSimpleExpression(newValue, listParam);
		}

	}

	public static boolean isThereCondition(String expression, String condition) {
		// example for the reg exp: (.*)[')][ ]*or[ ]*[\w(](.*)
		if (expression.matches("(.*)[')][ ]*" + condition + "[ ]*[\\w(](.*)")) {
			return true;
		}
		if (expression.matches("(.*)[')][ ]*" + condition.toUpperCase()
				+ "[ ]*[\\w(](.*)")) {
			return true;
		}
		return false;
	}

	private static boolean evaluateSimpleExpression(String simpleExpression,
			List<? extends IElementParameter> listParam) {
		boolean showParameter = false;
		String test = null;
		if (simpleExpression.contains(EQUALS)) {
			test = EQUALS;
		} else {
			if (simpleExpression.contains(NOT_EQUALS)) {
				test = NOT_EQUALS;
			}
		}
		String[] strings = simpleExpression.split(test);

		String variableName = null, variableValue = null;

		for (int i = 0; i < strings.length; i++) {
			String string = strings[i].trim();
			if (string.contains("'")) { // value
				variableValue = string;
				variableValue = variableValue.substring(1, string
						.lastIndexOf("'"));
			} else {
				variableName = string;
			}
		}
		if ((variableName != null) && (variableValue != null)) {
			for (IElementParameter param : listParam) {
				if (param.getName().equals(variableName)) {
					boolean found = false;
					Object value = param.getValue();
					if (value instanceof String) {
						if (param.getListItemsValue() instanceof Object[]) {
                            Object[] values = (Object[]) param
									.getListItemsValue();
							for (int i = 0; i < values.length && !found; i++) {
								if (values[i].equals(param.getValue())) {
									String variableCode = param
											.getListItemsDisplayCodeName()[i];
									if (variableCode.equals(variableValue)) {
										found = true;
									}
								}
							}
						}
					} else {
						if (value instanceof Boolean) {
							Boolean tmpValue = new Boolean(variableValue);
							if (tmpValue.equals(value)) {
								found = true;
							}
						}
					}
					
					if (found) {
						if (test.equals(EQUALS)) {
							showParameter = true;
						}
					} else {
						if (test.equals(NOT_EQUALS)) {
							showParameter = true;
						}
					}

				}
			}
		}
		return showParameter;
	}

	private static Expression evaluateExpression(Expression expression,
			List<? extends IElementParameter> listParam) {
		int indexBegining = 0, indexEnd;
		int expressionLevel = 0;
		String string = expression.getExpressionString();
		boolean conditionFound = false;

		// if there's no braket then there should be only simple expression
		// or only one expression.
		for (int i = 0; i < string.length() && !conditionFound; i++) {
			if (string.charAt(i) == '(') {
				if (expressionLevel == 0) {
					indexBegining = i + 1;
				}
				expressionLevel++;
			} else if (string.charAt(i) == ')') {
				expressionLevel--;
				indexEnd = i;

				if (expressionLevel == 0) {
					if (isThereCondition(string, AND)
							|| isThereCondition(string, OR)) {
						String leftString = string.substring(indexBegining,
								indexEnd).trim();
						if (isThereCondition(leftString, AND)
								|| isThereCondition(leftString, OR)) {
							Expression leftExpression = new Expression(
									leftString);
							expression.setLeftExpression(leftExpression);
							evaluateExpression(leftExpression, listParam);
						} else {
							Expression leftExpression = new Expression(
									leftString);
							expression.setLeftExpression(leftExpression);
							leftExpression.setValid(evaluateSimpleExpression(
									leftString, listParam));
							// debug: System.out.println(leftString + " => " +
							// leftExpression.isValid());
						}
					} else {
						String newValue; // remove brackets
						newValue = string.replace("(", "");
						newValue = newValue.replace(")", "");
						expression.setExpressionString(newValue);
						expression.setValid(evaluateSimpleExpression(newValue,
								listParam));
					}
				}
			} else if (expressionLevel == 0) {
				if ((string.indexOf(AND, i) == i)
						|| (string.indexOf(AND.toUpperCase(), i) == i)) {
					String subStr = string.substring(i - 3, i + 5);
					if (isThereCondition(subStr, AND)) {
						expression.setCondition(AND);
						conditionFound = true;
					}
				} else if ((string.indexOf(OR, i) == i)
						|| (string.indexOf(OR.toUpperCase(), i) == i)) {
					String subStr = string.substring(i - 3, i + 5);
					if (isThereCondition(subStr, OR)) {
						expression.setCondition(OR);
						conditionFound = true;
					}
				}
			}
			if (conditionFound) {
				if (expression.getLeftExpression() == null) { // no bracket ==
																// evaluate
																// expression
					String leftString = string.substring(0, i - 1).trim();
					Expression leftExpression = new Expression(leftString);
					expression.setLeftExpression(leftExpression);
					leftExpression.setValid(evaluateSimpleExpression(
							leftString, listParam));
					// debug: System.out.println(leftString + " => " +
					// leftExpression.isValid());
				}
				String rightString = string.substring(i + 3, string.length())
						.trim();
				Expression rightExpression = new Expression(rightString);
				expression.setRightExpression(rightExpression);
				if (rightString.contains("(")
						|| isThereCondition(rightString, AND)
						|| isThereCondition(rightString, OR)) {
					evaluateExpression(rightExpression, listParam);
				} else { // no bracket == evaluate expression
					rightExpression.setValid(evaluateSimpleExpression(
							rightString, listParam));
					// debug: System.out.println(rightString + " => " +
					// rightExpression.isValid());
				}
				if (expression.getCondition().equals(AND)) {
					if (expression.getLeftExpression().isValid()
							&& expression.getRightExpression().isValid()) {
						expression.setValid(true);
					} else {
						expression.setValid(false);
					}
				} else if (expression.getCondition().equals(OR)) {
					if (expression.getLeftExpression().isValid()
							|| expression.getRightExpression().isValid()) {
						expression.setValid(true);
					} else {
						expression.setValid(false);
					}
				}
			}
		}
		// if after an expression between bracket there's no other expression,
		// then the validation of the expression
		// will depends on the "left" expression.
		if ((expression.getRightExpression() == null)
				&& (expression.getLeftExpression() != null)) {
			expression.setValid(expression.getLeftExpression().isValid());
		}
		// debug: System.out.println(expression.getExpressionString() + " => " +
		// expression.isValid());
		return expression;
	}
}

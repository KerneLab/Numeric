package org.kernelab.numeric.matrix;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;

import org.kernelab.basis.LinkedQueue;
import org.kernelab.basis.Relation;
import org.kernelab.basis.Tools;
import org.kernelab.numeric.Fraction;
import org.kernelab.numeric.Operator;
import org.kernelab.numeric.SyntaxErrorException;
import org.kernelab.numeric.matrix.FractionMatrix;
import org.kernelab.numeric.matrix.IllegalRangePatternException;
import org.kernelab.numeric.matrix.Matrix;
import org.kernelab.numeric.matrix.SizeDisagreeException;
import org.kernelab.numeric.util.GaussJordanForm;
import org.kernelab.numeric.util.IdentityMatrix;
import org.kernelab.numeric.util.LUDecomposition;

public class Parser
{
	public static final String										VERSION					= "1.3.9";

	public static final String										SENTENSE				= "$";

	public static final String										EVALUATE				= "=";

	public static final String										REFERENCE				= "@=";

	public static final String										BRACKET_BEGIN			= "(";

	public static final String										BRACKET_END				= ")";

	public static final String										OPERATOR_ADD			= "+";

	public static final String										OPERATOR_MINUS			= "-";

	public static final String										OPERATOR_CROSS_MULTIPLY	= "#";

	public static final String										OPERATOR_CROSS_DIVIDE	= "\\";

	public static final String										OPERATOR_MULTIPLY		= "*";

	public static final String										OPERATOR_DIVIDE			= "/";

	public static final String										OPERATOR_POWER			= "^";

	public static final String										OPERATOR_NEGATIVE		= "+-";

	public static final String[]									OPERATORS				= new String[] {
			OPERATOR_ADD, OPERATOR_MINUS, OPERATOR_CROSS_MULTIPLY, OPERATOR_CROSS_DIVIDE,
			OPERATOR_MULTIPLY, OPERATOR_DIVIDE												};

	public static final String[]									KEYWORDS				= new String[] {
			BRACKET_BEGIN, BRACKET_END, OPERATOR_ADD, OPERATOR_MINUS,
			OPERATOR_CROSS_MULTIPLY, OPERATOR_CROSS_DIVIDE, OPERATOR_MULTIPLY,
			OPERATOR_DIVIDE																};

	public static final Map<String, Integer>						OPERATOR_PRIORITY		= new HashMap<String, Integer>();

	public static final String										TRANSPOSE				= "'";

	public static final String										MATRIX_BEGIN			= "[";

	public static final String										MATRIX_END				= "]";

	public static final String										ROW_SPLIT				= ";";

	public static final String										COLUMN_SPLIT			= ",";

	public static final Map<String, Operator<FractionMatrix>>		Operators				= new Hashtable<String, Operator<FractionMatrix>>();

	public static final Map<String, Map<String, FractionMatrix>>	buffer					= new Hashtable<String, Map<String, FractionMatrix>>();

	public static final Map<String, FractionMatrix>					matrix					= new Hashtable<String, FractionMatrix>();

	public static Map<String, FractionMatrix>						cache;

	public static String											procedure				= "";

	static {
		Parser.configOperatorsPriority();
		Parser.loadFunctions();
	}

	public static final void checkSyntax(String string)
	{
		int begin = 0, end = 0;

		String s;

		for (int i = 0; i < string.length(); i++) {
			s = String.valueOf(string.charAt(i));
			if (s.equals(Parser.BRACKET_BEGIN)) {
				begin++;
			} else if (s.equals(Parser.BRACKET_END)) {
				end++;
			}
		}

		if (begin != end) {
			throw new SyntaxErrorException("The expression has " + begin + " \""
					+ Parser.BRACKET_BEGIN + "\" but has " + end + " \""
					+ Parser.BRACKET_END + "\"");
		}

	}

	public static final void configOperatorsPriority()
	{
		Parser.OPERATOR_PRIORITY.put(Parser.OPERATOR_ADD, 1);
		Parser.OPERATOR_PRIORITY.put(Parser.OPERATOR_MINUS, 1);
		Parser.OPERATOR_PRIORITY.put(Parser.OPERATOR_MULTIPLY, 2);
		Parser.OPERATOR_PRIORITY.put(Parser.OPERATOR_DIVIDE, 2);
		Parser.OPERATOR_PRIORITY.put(Parser.OPERATOR_CROSS_MULTIPLY, 2);
		Parser.OPERATOR_PRIORITY.put(Parser.OPERATOR_CROSS_DIVIDE, 2);
		Parser.OPERATOR_PRIORITY.put(Parser.OPERATOR_POWER, 3);
		Parser.OPERATOR_PRIORITY.put(Parser.OPERATOR_NEGATIVE, 4);
	}

	public static final boolean isKeyword(String kw)
	{
		return Tools.arrayHas(Parser.KEYWORDS, kw);
	}

	public static final boolean isNumber(String num)
	{
		boolean is = true;

		try {
			Integer.parseInt(num);
		} catch (NumberFormatException e) {
			is = false;
		}

		return is;
	}

	public static final boolean isOperator(String op)
	{
		return Parser.OPERATOR_PRIORITY.containsKey(op);
	}

	public static final void loadFunctions()
	{
		Parser.Operators.put("IDM", new IdentityMatrix());
		Parser.Operators.put("GJF", new GaussJordanForm());
		Parser.Operators.put("LUD", new LUDecomposition());
	}

	public static final void LocateCache(String ip)
	{
		Parser.cache = Parser.matrix;

		if (ip != null) {
			Parser.cache = Parser.buffer.get(ip);
			if (Parser.cache == null) {
				Parser.cache = new Hashtable<String, FractionMatrix>();
				Parser.buffer.put(ip, Parser.cache);
			}
		}

	}

	// public static final DoubleMatrix parseDoubleMatrix(String string)
	// {
	// DoubleMatrix matrix = Parser.cache.get(string);
	//
	// if (matrix == null) {
	//
	// if (string.indexOf("*") > 0) {
	// String[] m = string.split("\\*");
	//
	// try {
	// Double number = Double.valueOf(m[0]);
	// matrix = Parser.cache.get(m[1]).multiply(number);
	// } catch (NumberFormatException e) {
	// try {
	// Double number = Double.valueOf(m[1]);
	// matrix = Parser.cache.get(m[0]).multiply(number);
	// } catch (NumberFormatException e1) {
	// matrix = Parser.cache.get(m[0]).innerProduct(
	// Parser.cache.get(m[1]));
	// }
	//
	// }
	//
	// } else if (string.indexOf("+") > 0) {
	// String[] m = string.split("\\+");
	// matrix = Parser.parseMatrix(m[0]).add(Parser.parseMatrix(m[1]));
	// } else {
	// matrix = new DoubleMatrix(Parser.parseMatrixString(string));
	//
	// }
	//
	// }
	//
	// return matrix;
	// }

	// public static final Double[][] parseDoubleMatrixString(String string)
	// {
	// string = string.replaceAll("\\[", "");
	// string = string.replaceAll("\\]", "");
	// string = string.replaceAll(" ", "");
	// string = string.replaceAll("\n", "");
	// string = string.replaceAll("\r", "");
	//
	// String[] rows = string.split(Parser.ROW_SPLIT);
	// String[] columns = rows[Position.REAL_FIRST].split(Parser.COLUMN_SPLIT);
	//
	// Double[][] matrix = new Double[rows.length][columns.length];
	//
	// for (int row = 0; row < rows.length; row++) {
	// columns = rows[row].split(Parser.COLUMN_SPLIT);
	//
	// for (int column = 0; column < columns.length; column++) {
	// matrix[row][column] = Double.valueOf(columns[column]);
	// }
	//
	// }
	//
	// return matrix;
	// }

	// public static final DoubleMatrix parseDoubleMatrix(String string)
	// {
	// DoubleMatrix matrix = Parser.cache.get(string);
	//
	// if (matrix == null) {
	//
	// if (string.indexOf("*") > 0) {
	// String[] m = string.split("\\*");
	//
	// try {
	// Double number = Double.valueOf(m[0]);
	// matrix = Parser.cache.get(m[1]).multiply(number);
	// } catch (NumberFormatException e) {
	// try {
	// Double number = Double.valueOf(m[1]);
	// matrix = Parser.cache.get(m[0]).multiply(number);
	// } catch (NumberFormatException e1) {
	// matrix = Parser.cache.get(m[0]).innerProduct(
	// Parser.cache.get(m[1]));
	// }
	//
	// }
	//
	// } else if (string.indexOf("+") > 0) {
	// String[] m = string.split("\\+");
	// matrix = Parser.parseMatrix(m[0]).add(Parser.parseMatrix(m[1]));
	// } else {
	// matrix = new DoubleMatrix(Parser.parseMatrixString(string));
	//
	// }
	//
	// }
	//
	// return matrix;
	// }

	// public static final Double[][] parseDoubleMatrixString(String string)
	// {
	// string = string.replaceAll("\\[", "");
	// string = string.replaceAll("\\]", "");
	// string = string.replaceAll(" ", "");
	// string = string.replaceAll("\n", "");
	// string = string.replaceAll("\r", "");
	//
	// String[] rows = string.split(Parser.ROW_SPLIT);
	// String[] columns = rows[Position.REAL_FIRST].split(Parser.COLUMN_SPLIT);
	//
	// Double[][] matrix = new Double[rows.length][columns.length];
	//
	// for (int row = 0; row < rows.length; row++) {
	// columns = rows[row].split(Parser.COLUMN_SPLIT);
	//
	// for (int column = 0; column < columns.length; column++) {
	// matrix[row][column] = Double.valueOf(columns[column]);
	// }
	//
	// }
	//
	// return matrix;
	// }

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		String exp = "a=[1,2]$";

		Parser parser = new Parser();

		parser.parse(exp);
	}

	public static final FractionMatrix operate(String op, FractionMatrix a,
			FractionMatrix b)
	{
		FractionMatrix result = null;

		if (op.equals(Parser.OPERATOR_ADD)) {
			result = new FractionMatrix(FractionMatrix.Add(a, b, a.clone()));
		} else if (op.equals(Parser.OPERATOR_MINUS)) {
			result = new FractionMatrix(FractionMatrix.Minus(a, b, a.clone()));
		} else if (op.equals(Parser.OPERATOR_MULTIPLY)) {
			result = new FractionMatrix(FractionMatrix.Product(a, b, a.clone()));
		} else if (op.equals(Parser.OPERATOR_DIVIDE)) {
			result = new FractionMatrix(FractionMatrix.Divide(a, b, a.clone()));
		} else if (op.equals(Parser.OPERATOR_CROSS_MULTIPLY)) {
			result = new FractionMatrix(FractionMatrix.Multiply(a, b, a.clone()));
		} else if (op.equals(Parser.OPERATOR_CROSS_DIVIDE)) {
			result = new FractionMatrix(FractionMatrix.Divide(a, b, a.clone()));
		}

		return result;
	}

	public static final FractionMatrix parse(String string,
			org.kernelab.basis.Variable<Integer> index)
	{
		int from = index.value;

		FractionMatrix result = null;

		FractionMatrix temp = null;

		String op = "";

		String ch = "";

		String exp = "";

		do {
			ch = String.valueOf(string.charAt(index.value));

			index.value++;

			// Tools.debug(index.value + " -:- " + ch);

			if (ch.equals(Parser.BRACKET_BEGIN)) {

				if (index.value == 1
						|| Parser.isKeyword(String
								.valueOf(string.charAt(index.value - 2))))
				{
					// 优先计算的开始
					temp = Parser.parse(string, index);

					if (index.value == string.length()) {
						if (op.equals("")) {
							result = temp;
						} else {
							if (result == null && op.equals(Parser.OPERATOR_MINUS)) {
								// 作用于优先运算前的负号
								result = (FractionMatrix) temp.clone();
								result.multiply(new Fraction(-1));
							} else {
								result = Parser.operate(op, result, temp);
							}
						}
						temp = null;
					}

				} else {
					// 矩阵引用，视作一个单元
					exp += ch;
					for (int t = 1; index.value < string.length(); index.value++) {
						ch = String.valueOf(string.charAt(index.value));
						exp += ch;

						if (ch.equals(Parser.BRACKET_BEGIN)) {
							t++;
						} else if (ch.equals(Parser.BRACKET_END)) {
							t--;
						}

						if (t == 0) {
							if (index.value + 1 != string.length()) {
								index.value++;
							}
							break;
						}
					}
				}

			} else if (ch.equals(Parser.MATRIX_BEGIN)) {
				// 矩阵，视作一个单元
				exp += ch;
				for (int t = 1; index.value < string.length(); index.value++) {
					ch = String.valueOf(string.charAt(index.value));
					exp += ch;

					if (ch.equals(Parser.MATRIX_BEGIN)) {
						t++;
					} else if (ch.equals(Parser.MATRIX_END)) {
						t--;
					}

					if (t == 0) {
						if (index.value + 1 != string.length()) {
							index.value++;
						}
						break;
					}
				}

			} else if (Parser.isOperator(ch) || ch.equals(Parser.BRACKET_END)
					|| index.value == string.length())
			{
				// 运算符
				if (ch.equals(Parser.OPERATOR_MINUS) && index.value - from == 1
				// &&
						// !Parser.isKeyword(String.valueOf(string.charAt(index.value)))
						&& Parser.isNumber(String.valueOf(string.charAt(index.value))))
				{
					// 负号
					exp += ch;

				} else {
					// 运算
					if (!Parser.isKeyword(ch)) {
						exp += ch;
					}

					if (op.equals("")) {
						// 之前没有运算符
						if (exp.equals("")) {
							result = temp;
							temp = null;
						} else {
							result = Parser.parseFractionMatrix(exp);
						}

					} else {
						// 已存在运算符
						if (temp == null) {
							temp = Parser.parseFractionMatrix(exp);
						}

						if (result == null && op.equals(Parser.OPERATOR_MINUS)) {
							// 作用于优先运算前的负号
							result = (FractionMatrix) temp.clone();
							result.multiply(new Fraction(-1));
						} else {
							result = Parser.operate(op, result, temp);
						}

						temp = null;
					}

					if (ch.equals(Parser.BRACKET_END) || index.value == string.length()) {
						break;
					} else {
						op = ch;
						exp = "";
					}
				}

			} else {
				exp += ch;
			}

		} while (index.value < string.length());

		return result;
	}

	public static final String Parse(String string)
	{

		Parser.procedure = "";

		string = string.replaceAll(" ", "");
		string = string.replaceAll("\t", "");
		string = string.replaceAll("\n", "");
		string = string.replaceAll("\r", "");

		Tools.debug(string);
		String line = "";
		for (int i = 0; i < string.length(); i++) {
			line += "-";
		}
		Tools.debug(line);

		String[] exps = Tools.splitCharSequence(string, Parser.SENTENSE);

		for (int i = 0; i < exps.length - 1; i++) {
			exps[i] += Parser.SENTENSE;
		}

		String parse = "";

		for (String exp : exps) {

			if (!exp.equals("") && !exp.equals(Parser.SENTENSE)) {

				boolean isPrint = true;

				if (exp.endsWith(Parser.SENTENSE)) {
					exp = exp.substring(0, exp.length() - 1);
					isPrint = false;
				}

				Parser.checkSyntax(exp);

				exp = Parser.parsePriority(exp);

				String name = "";

				if (exp.indexOf(Parser.EVALUATE) == -1) {
					exp = "ans=" + exp;
				}

				if (exp.indexOf(Parser.EVALUATE) > 0) {

					int index = exp.indexOf(Parser.EVALUATE);

					if (exp.indexOf(Parser.REFERENCE) > 0) {
						name = exp.substring(0, index - 1);
					} else {
						name = exp.substring(0, index);
					}

					String expression = exp.substring(index + 1);

					if (name.indexOf(Parser.BRACKET_BEGIN) > 0
							&& name.indexOf(Parser.BRACKET_END) > 0)
					{
						// 引用现有矩阵中的一部分

						FractionMatrix matrix = Parser.parseFractionMatrix(name);

						if (matrix != null) {

							// String pattern = name.substring(
							// name.indexOf(Parser.BRACKET_BEGIN) + 1, name
							// .indexOf(Parser.BRACKET_END));
							//
							// matrix = new
							// FractionMatrix(matrix.quote(pattern));

							FractionMatrix result = Parser.parseCalculation(expression);

							if (matrix.getSize().equals(result.getSize())) {

								if (exp.indexOf(Parser.REFERENCE) > 0) {
									matrix.quote(result);
								} else {
									matrix.clone(result);
								}

								name = name.split("\\" + Parser.BRACKET_BEGIN)[0];

							} else {
								throw new SizeDisagreeException(matrix.getSize(), result
										.getSize());
							}

						} else {
							throw new IllegalRangePatternException(name.split("\\"
									+ Parser.BRACKET_BEGIN)[0]
									+ " doesn't exist.");
						}

					} else {
						FractionMatrix matrix = null;

						if (exp.indexOf(Parser.REFERENCE) > 0) {
							matrix = new FractionMatrix(Parser.parseCalculation(
									expression).quote());
						} else {
							matrix = new FractionMatrix(Parser.parseCalculation(
									expression).clone());
						}

						Parser.cache.put(name, matrix);
					}

					if (isPrint) {
						String s = Parser.procedure;

						s = s.replaceAll("<table class=\"matrix\">", "");
						s = s.replaceAll("</table>", "");
						s = s.replaceAll("<br>", "\n");
						s = s.replaceAll("<tr>", "");
						s = s.replaceAll("</tr>", "\n");
						s = s.replaceAll("<td>", "");
						s = s.replaceAll("</td>", "\t");
						s = s.replaceAll("<hr>", "-----------------------------\n");

						if (!s.equals("")) {
							Tools.debug(s);
						}

						Tools.debug(name + " = ");
						Tools.debug(Parser.cache.get(name));

					}

				}

				if (isPrint) {
					parse += name + " = <br>"
							+ ((Matrix<Fraction>) Parser.cache.get(name)).toHTML();

					parse = Parser.procedure + parse;
				}

			}

		}

		parse += "<br>";

		return parse;
	}

	// public static final FractionMatrix parseExpression(String string,
	// org.kernelstudio.basis.Variable<Integer> index, String endKey)
	// {
	// FractionMatrix result = null;
	//
	// String op = "";
	//
	// String exp = "";
	//
	// String ch = "";
	//
	// do {
	// ch = String.valueOf(string.charAt(index.getValue()));
	//
	// // Tools.debug(index.getValue() + " -:- " + ch);
	//
	// index.setValue(index.getValue() + 1);
	//
	// if (ch.equals(Parser.BRACKET_BEGIN) || ch.equals(Parser.MATRIX_BEGIN)) {
	//
	// String end;
	//
	// if (ch.equals(Parser.BRACKET_BEGIN)) {
	// end = Parser.BRACKET_END;
	// } else {
	// end = Parser.MATRIX_END;
	// }
	//
	// if (index.getValue() == 1
	// || Parser.isKeyword(String.valueOf(string
	// .charAt(index.getValue() - 2))))
	// {
	// // ..+(a-b)
	// if (op.equals("")) {
	// result = Parser.parseExpression(string, index, end);
	// } else {
	// result = Parser.operate(op, result, Parser.parseExpression(
	// string, index, end));
	// }
	//
	// } else {
	// // a(1,2) or func(b)
	// int from = index.getValue() - 1;
	// exp += string.substring(from, string.indexOf(end, from) + 1);
	//
	// index.setValue(string.indexOf(end, from) + 1);
	// if (index.getValue() == string.length()) {
	// index.value--;
	// ch = "";
	// }
	// }
	//
	// } else if (ch.equals(endKey) && ch.equals(Parser.MATRIX_END)) {
	//
	// result = Parser.parseFractionMatrix(exp);
	//
	// if (index.getValue() < string.length()
	// && String.valueOf(string.charAt(index.getValue())).equals(
	// Parser.TRANSPOSE))
	// {
	// result = new FractionMatrix(result.cloneTranspose());
	// index.value++;
	// }
	//
	// } else if (
	// // (ch.equals(Parser.BRACKET_BEGIN) && index.getValue() != 1 &&
	// // !Parser
	// // .isKeyword(String.valueOf(string.charAt(index.getValue() - 2))))
	// // ||
	// (endKey == null || endKey.equals("") ||
	// endKey.equals(Parser.BRACKET_END))
	// && (Parser.isKeyword(ch) || index.getValue() == string.length()))
	// {
	//
	// if (ch.equals(endKey) && !ch.equals(Parser.BRACKET_END)) {
	//
	// result = Parser.parseFractionMatrix(exp);
	//
	// } else {
	//
	// if (index.getValue() == string.length()
	// && !ch.equals(Parser.BRACKET_END))
	// {
	// exp += ch;
	// }
	//
	// FractionMatrix temp = null;
	//
	// if (exp.equals("")) {
	// temp = result;
	// } else {
	// temp = Parser.parseFractionMatrix(exp);
	// }
	//
	// if (op.equals("")) {
	//
	// result = temp;
	//
	// } else {
	//
	// result = Parser.operate(op, result, temp);
	//
	// }
	//
	// if (Parser.isOperator(ch)
	// && String.valueOf(string.charAt(index.getValue())).equals(
	// Parser.BRACKET_BEGIN))
	// {// 1+(2+3)
	// temp = Parser.parseExpression(string, index, "");
	// // result = Parser.parseFractionMatrix(exp);
	// op = ch;
	//
	// result = Parser.operate(op, result, temp);
	//
	// if (index.getValue() < string.length()) {
	// ch = String.valueOf(string.charAt(index.getValue()));
	// }
	// }
	//
	// if (ch.equals(Parser.OPERATOR_MINUS)
	// && (index.getValue() == 1 || index.getValue() > 1
	// && Parser.isKeyword(String.valueOf(string
	// .charAt(index.getValue() - 2)))
	// && !String.valueOf(
	// string.charAt(index.getValue() - 2)).equals(
	// Parser.BRACKET_END)))
	// {
	// // negative
	// exp += ch;
	// } else {
	// // minus
	// op = ch;
	// exp = "";
	// }
	//
	// }
	//
	// } else {
	// exp += ch;
	// }
	//
	// } while (!ch.equals(Parser.BRACKET_END) && index.getValue() <
	// string.length());
	//
	// return result;
	// }

	public static final FractionMatrix parseCalculation(String string)
	{
		// return Parser.parseCalculation(string,
		// org.kernelstudio.basis.Variable
		// .instance(string.length()));

		// return Parser.parseExpression(string,
		// org.kernelstudio.basis.Variable.instance(0),
		// "");

		return Parser.parse(string, org.kernelab.basis.Variable.newInstance(0));
	}

	// public static final FractionMatrix parseCalculation(String string,
	// org.kernelstudio.basis.Variable<Integer> index)
	// {
	// FractionMatrix matrix = Parser.cache.get(string);
	//
	// if (matrix == null) {
	// if (string.indexOf(Parser.MATRIX_BEGIN) >= 0
	// && string.indexOf(Parser.MATRIX_END) >= 0)
	// {
	//
	// matrix = Parser.parseFractionMatrix(string.substring(string
	// .lastIndexOf(Parser.MATRIX_BEGIN), string
	// .lastIndexOf(Parser.MATRIX_END) + 1));
	//
	// } else if (string.indexOf(Parser.OPERATOR_CROSS_MULTIPLY) > 0) {
	//
	// String a = string.substring(0, string
	// .lastIndexOf(Parser.OPERATOR_CROSS_MULTIPLY));
	//
	// String b = string.substring(string
	// .lastIndexOf(Parser.OPERATOR_CROSS_MULTIPLY)
	// + Parser.OPERATOR_CROSS_MULTIPLY.length());
	//
	// try {
	//
	// Fraction number = Fraction.valueOf(a);
	// matrix = Parser.parseFractionMatrix(b).multiply(number);
	//
	// } catch (NumberFormatException e) {
	//
	// try {
	//
	// Fraction number = Fraction.valueOf(b);
	// matrix = Parser.parseFractionMatrix(a).multiply(number);
	//
	// } catch (NumberFormatException e1) {
	//
	// matrix = Parser.parseCalculation(a).multiply(
	// Parser.parseFractionMatrix(b));
	//
	// }
	// }
	//
	// } else if (string.indexOf(Parser.OPERATOR_MULTIPLY) > 0) {
	//
	// // String[] m = Tools.splitString(string,
	// // Parser.OPERATOR_MULTIPLY);
	// //
	// // try {
	// // Fraction number = Fraction.valueOf(m[0]);
	// // matrix = Parser.parseFractionMatrix(m[1]).multiply(number);
	// // } catch (NumberFormatException e) {
	// // try {
	// // Fraction number = Fraction.valueOf(m[1]);
	// // matrix = Parser.parseFractionMatrix(m[0]).multiply(number);
	// // } catch (NumberFormatException e1) {
	// // matrix = Parser.parseFractionMatrix(m[0]).product(
	// // Parser.parseFractionMatrix(m[1]));
	// // }
	// //
	// // }
	//
	// String a = string.substring(0, string
	// .lastIndexOf(Parser.OPERATOR_MULTIPLY));
	//
	// String b = string.substring(string.lastIndexOf(Parser.OPERATOR_MULTIPLY)
	// + Parser.OPERATOR_MULTIPLY.length());
	//
	// try {
	//
	// Fraction number = Fraction.valueOf(a);
	// matrix = Parser.parseFractionMatrix(b).multiply(number);
	//
	// } catch (NumberFormatException e) {
	//
	// try {
	//
	// Fraction number = Fraction.valueOf(b);
	// matrix = Parser.parseFractionMatrix(a).multiply(number);
	//
	// } catch (NumberFormatException e1) {
	//
	// matrix = Parser.parseCalculation(a).product(
	// Parser.parseFractionMatrix(b));
	//
	// }
	//
	// }
	//
	// } else if (string.indexOf(Parser.OPERATOR_ADD) > 0) {
	//
	// String a = string.substring(0, string.lastIndexOf(Parser.OPERATOR_ADD));
	//
	// String b = string.substring(string.lastIndexOf(Parser.OPERATOR_ADD)
	// + Parser.OPERATOR_ADD.length());
	//
	// matrix = Parser.parseCalculation(a).add(Parser.parseFractionMatrix(b));
	//
	// } else {
	//
	// matrix = Parser.parseFractionMatrix(string);
	//
	// }
	//
	// }
	//
	// return matrix;
	// }

	public static final FractionMatrix parseFractionMatrix(String exp)
	{
		FractionMatrix matrix = null;

		String name = exp;

		if (exp.contains(Parser.TRANSPOSE)) {

			name = exp.substring(0, exp.indexOf(Parser.TRANSPOSE));

			matrix = Parser.parseFractionMatrix(name);

			matrix = new FractionMatrix(matrix.quoteTranspose());

			if (exp.indexOf(Parser.BRACKET_BEGIN, exp.indexOf(Parser.TRANSPOSE)) > 0
					&& exp.indexOf(Parser.BRACKET_END, exp.indexOf(Parser.TRANSPOSE)) > 0)
			{
				matrix = new FractionMatrix(matrix.quote(exp.substring(exp
						.indexOf(Parser.BRACKET_BEGIN) + 1, exp
						.indexOf(Parser.BRACKET_END))));
			}

		} else if (exp.contains(Parser.BRACKET_BEGIN) && exp.contains(Parser.BRACKET_END))
		{
			name = exp.substring(0, exp.indexOf(Parser.BRACKET_BEGIN));

			if (Parser.Operators.containsKey(name)) {
				matrix = Parser.Operators.get(name).operate(
						Parser.parseFractionMatrix(exp.substring(exp
								.indexOf(Parser.BRACKET_BEGIN) + 1, exp
								.indexOf(Parser.BRACKET_END))));
			} else {

				matrix = Parser.parseFractionMatrix(name);

				matrix = new FractionMatrix(matrix.quote(exp.substring(exp
						.indexOf(Parser.BRACKET_BEGIN) + 1, exp
						.indexOf(Parser.BRACKET_END))));
			}
		} else {
			if (Parser.cache != null) {
				matrix = Parser.cache.get(name);
			}
		}

		if (matrix == null) {

			matrix = FractionMatrix.Parse(exp);
		}

		return matrix;
	}

	public static final String parsePriority(String exp)
	{
		StringBuffer result = new StringBuffer(exp);

		String ch = "";

		LinkedQueue<Relation<Integer, Integer>> prior = new LinkedQueue<Relation<Integer, Integer>>();

		prior.push(new Relation<Integer, Integer>(0, -1));

		for (int i = 0; i < result.length(); i++) {

			ch = String.valueOf(result.charAt(i));

			if (ch.equals(Parser.BRACKET_BEGIN)) {

				int k = 1;
				String t = "";

				for (int j = i + 1; j < result.length(); j++) {

					t = String.valueOf(result.charAt(j));

					if (t.equals(Parser.BRACKET_END)) {
						k--;
						if (k == 0) {
							k = j;
							break;
						}
					} else if (t.equals(Parser.BRACKET_BEGIN)) {
						k++;
					}
				}

				String sub = result.substring(i + 1, k);

				sub = Parser.parsePriority(sub);

				result.delete(i + 1, k);

				result.insert(i + 1, sub);

				i += sub.length();

			} else if (Parser.isOperator(ch)) {

				int p = Parser.OPERATOR_PRIORITY.get(ch);

				int j = i == 0 ? i - 1 : i;

				if (ch.equals(Parser.OPERATOR_MINUS)) {
					if (i == 0 || Parser.isKeyword(String.valueOf(exp.charAt(i - 1)))) {
						p = Parser.OPERATOR_PRIORITY.get(Parser.OPERATOR_NEGATIVE);
						j = i - 1;
					}
				}

				if (prior.peek().getKey() < p) {
					prior.push(new Relation<Integer, Integer>(p, j));

				} else if (prior.peek().getKey() == p) {
					prior.peek().setValue(i);

				} else {
					do {
						prior.poll();

						result.insert(i, Parser.BRACKET_END);

						result.insert(prior.peek().getValue() + 1, Parser.BRACKET_BEGIN);

						i += 2;

					} while (prior.peek().getKey() > p);

				}
			}

		}

		Relation<Integer, Integer> temp;

		prior.poll();

		while (!prior.isEmpty()) {
			temp = prior.poll();
			if (temp.getKey() > 0) {
				result.insert(temp.getValue() + 1, Parser.BRACKET_BEGIN);
				result.append(Parser.BRACKET_END);
			}
		}

		return result.toString();
	}

	private String	input;

	private Scanner	scanner;

	private String	ip;

	public Parser()
	{
		this.setInput("");
		this.setIp(null);
	}

	public Parser(Scanner scanner)
	{
		this.setInput("");
		this.setScanner(new Scanner(System.in));
		this.setIp(null);
		this.input();
	}

	public Parser(String ip)
	{
		this.setInput("");
		this.setIp(ip);
	}

	public Matrix<Fraction> getFractionMatrix(String name)
	{
		return Parser.cache.get(name);
	}

	public String getInput()
	{
		return input;
	}

	public String getIp()
	{
		return ip;
	}

	public Scanner getScanner()
	{
		return scanner;
	}

	protected void input()
	{
		while (true) {

			this.setInput(this.getScanner().next());

			if (this.getInput().equals("end")) {
				break;
			}

			Parser.Parse(this.getInput());

		}
	}

	public String parse(String input)
	{
		this.setInput(input);
		return Parser.Parse(this.getInput());
	}

	public void setFractionMatrix(String name, FractionMatrix matrix)
	{
		Parser.cache.put(name, matrix);
	}

	public void setInput(String input)
	{
		this.input = input.replaceAll(" ", "");
		this.input = this.input.replaceAll("　", "");
		this.input = this.input.replaceAll("\n", "");
		this.input = this.input.replaceAll("\r", "");
		this.input = this.input.replaceAll("\t", "");
	}

	public void setIp(String ip)
	{
		this.ip = ip;
		Parser.LocateCache(ip);
	}

	public void setScanner(Scanner scanner)
	{
		this.scanner = scanner;
	}
}

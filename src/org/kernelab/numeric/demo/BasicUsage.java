package org.kernelab.numeric.demo;

import org.kernelab.basis.Tools;
import org.kernelab.numeric.matrix.Matrix;
import org.kernelab.numeric.matrix.Range;

public class BasicUsage
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// 创建一个4行3列的矩阵，该矩阵内可存放Double型元素
		Matrix<Double> matrix = new Matrix<Double>(4, 3);
		// 将所有元素赋值为0.0
		matrix.setAll(0.0);

		// 使用一个Double型二维数组作为构造的参数
		Double[][] data = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 }, { 10.0, 11.0, 12.0 } };
		matrix = new Matrix<Double>(data);
		Tools.debug(matrix.toString());
		/* 输出
		1.0	2.0	3.0
		4.0	5.0	6.0
		7.0	8.0	9.0
		10.0	11.0	12.0
		 */

		// 创建一个小的矩阵
		Matrix<Double> other = new Matrix<Double>(2, 2);
		// 引用上述矩阵中的一部分，第1行到第2行，第0列到第1列（注意这里的下标索引是从0开始的）
		other.quote(matrix, new Range(1, 2, 0, 1));

		// 修改引用中的某个元素
		other.set(other.get(0, 1) + 0.5, 0, 1);
		Tools.debug(other.toString());
		/* 矩阵other中的5.0已经变为5.5
		4.0	5.5
		7.0	8.0
		 */

		Tools.debug(matrix.toString());
		/* 原来的矩阵matrix中的对应位置上的元素亦发生了改变，但我们没有直接操作matrix
		1.0	2.0	3.0	
		4.0	5.5	6.0	
		7.0	8.0	9.0	
		10.0	11.0	12.0	
		 */
	}

}

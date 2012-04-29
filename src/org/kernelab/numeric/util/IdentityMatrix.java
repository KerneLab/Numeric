package org.kernelab.numeric.util;

import org.kernelab.basis.Tools;
import org.kernelab.numeric.Fraction;
import org.kernelab.numeric.Operator;
import org.kernelab.numeric.matrix.DoubleMatrix;
import org.kernelab.numeric.matrix.FractionMatrix;
import org.kernelab.numeric.matrix.Parser;
import org.kernelab.numeric.matrix.Position;
import org.kernelab.numeric.matrix.Size;

public class IdentityMatrix implements Operator<FractionMatrix>
{

	public static final IdentityMatrix	OPERATOR	= new IdentityMatrix();

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Tools.debug(new IdentityMatrix().operate(Parser.parseFractionMatrix("4")));
	}

	public static final DoubleMatrix makeIdentityMatrix(int order)
	{
		DoubleMatrix result = new DoubleMatrix(new Size(order, order));

		double number;
		for (int row = Position.FIRST; row < order; row++) {
			for (int column = Position.FIRST; column < order; column++) {
				if (row == column) {
					number = 1;
				} else {
					number = 0;
				}
				result.set(number, row, column);
			}
		}

		return result;
	}

	public FractionMatrix operate(FractionMatrix... n)
	{
		return this.operate(n[0].get().intValue());
	}

	public FractionMatrix operate(int order)
	{
		FractionMatrix result = new FractionMatrix(new Size(order, order));

		int number;
		for (int row = Position.FIRST; row < order; row++) {
			for (int column = Position.FIRST; column < order; column++) {
				if (row == column) {
					number = 1;
				} else {
					number = 0;
				}
				result.set(new Fraction(number), row, column);
			}
		}

		return result;
	}

}

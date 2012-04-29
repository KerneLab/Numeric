package org.kernelab.numeric.util;

import org.kernelab.basis.Tools;
import org.kernelab.numeric.Fraction;
import org.kernelab.numeric.Operator;
import org.kernelab.numeric.matrix.FractionMatrix;
import org.kernelab.numeric.matrix.Matrix;
import org.kernelab.numeric.matrix.Parser;
import org.kernelab.numeric.matrix.Position;
import org.kernelab.numeric.matrix.Range;
import org.kernelab.numeric.matrix.Size;

public class LUDecomposition implements Operator<FractionMatrix>
{

	public static final LUDecomposition	OPERATOR	= new LUDecomposition();

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		String s = "[1,2,3,4,5;1,2,4,-9,9;-2,-1,1,2,6;-1,-1,1,1,6]";
		FractionMatrix m = Parser.parseFractionMatrix(s);
		Tools.debug(m);
		Tools.debug(new LUDecomposition().operate(m));
		// s = "[1,0,0,0;-2,1,0,0;1,0,1,0;-1,1/3,5/3,1]";
		// m = Parser.parseFractionMatrix(s);
		// Tools.debug(m);
		// s = "[1,2,3,4;0,3,7,10;0,0,1,-13;0,0,0,70/3]";
		// FractionMatrix n = Parser.parseFractionMatrix(s);
		// Tools.debug(n);
		// Tools.debug(m.product(n));
	}

	public FractionMatrix operate(FractionMatrix... matrixs)
	{
		FractionMatrix matrix = matrixs[0];

		Parser.procedure = "";

		int order = matrix.getRows();

		FractionMatrix up = (FractionMatrix) matrix.clone(new Range(Position.FIRST,
				order - 1, Position.FIRST, order - 1));

		FractionMatrix result = new FractionMatrix(new Size(order, order));

		FractionMatrix low = IdentityMatrix.OPERATOR.operate(order);

		FractionMatrix gauss = null;

		FractionMatrix temp = null;

		Fraction diag = new Fraction(0);

		Range range = null;

		Position position = new Position();

		for (int column = Position.FIRST; column < order - 1; column++) {

			// The range which stands for the elements below the diagram.
			range = new Range(column + 1, order - 1, column, column);

			// Quote the elements below the diagram.
			temp = up.clone(range);

			position.setPosition(column, column);

			diag.multiply(up.get(position), new Fraction(-1));

			if (diag.equals(Fraction.Zero())) {
				// Exchange the row sequence to avoid the ZERO diagram element.
				boolean found = false;

				Position p = new Position();

				for (int row = column + 1; row < matrix.getRows(); row++) {

					p.setPosition(row, column);

					Fraction e = matrix.get(p);

					if (!e.equals(Fraction.Zero())) {
						// Operate the original matrix.
						Matrix<Fraction> t = matrix.cloneRow(column);

						matrix.quoteRow(column).clone(matrix.cloneRow(row));

						matrix.quoteRow(row).clone(t);

						// Using recursive method.
						return this.operate(matrix);
					}
				}
				if (!found) {
					break;
				}
			}

			// Rearrange the Gauss vector.
			temp.divide(diag);

			// Generate the Gauss matrix.
			gauss = IdentityMatrix.OPERATOR.operate(order);

			// Copy the Gauss vector into the Gauss matrix at the proper
			// column.
			gauss.quote(range).clone(temp);

			Parser.procedure += "M = <br>" + gauss.toHTML() + "<br>";

			// Copy the opposite Gauss vector into the L matrix at the proper
			// column.
			low.quote(range).clone(temp.multiply(new Fraction(-1)));

			// Recalculate the matrix NumericMatrix.
			// up.product(gauss, up);
			gauss.product(up);
			up.clone(gauss);

			Parser.procedure += "NumericMatrix = <br>" + up.toHTML() + "<hr>";
		}

		// Combine the result of L\U into one square matrix.
		result.quote(low);

		result.minus(IdentityMatrix.OPERATOR.operate(order));

		result.add(up);

		temp = result;

		// Add last column of original matrix to the result.

		result = (FractionMatrix) matrix.clone();

		// Copy the L\U combination to the result.
		result.quote(new Range(Position.FIRST, order - 1, Position.FIRST, order - 1))
				.clone(temp);

		return result;
	}
}

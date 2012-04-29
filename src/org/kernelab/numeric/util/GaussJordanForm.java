package org.kernelab.numeric.util;

import org.kernelab.basis.Tools;
import org.kernelab.numeric.Fraction;
import org.kernelab.numeric.Operator;
import org.kernelab.numeric.matrix.DoubleMatrix;
import org.kernelab.numeric.matrix.FractionMatrix;
import org.kernelab.numeric.matrix.Matrix;
import org.kernelab.numeric.matrix.Parser;
import org.kernelab.numeric.matrix.Position;
import org.kernelab.numeric.matrix.SizeDisagreeException;

public class GaussJordanForm implements Operator<FractionMatrix>
{

	public static final GaussJordanForm	OPERATOR	= new GaussJordanForm();

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		String s = "[3,3,-4,-3;0,6,1,1;5,4,2,1;2,3,3,2]";

		// FractionMatrix m = Parser.parseFractionMatrix(s);
		DoubleMatrix m = DoubleMatrix.Parse(s);

		Tools.debug(m);

		// DoubleMatrix n = ReverseOfDoubleMatrix(m);
		//
		// Tools.debug(n);
		// Tools.debug(m.product(n));

		// Tools.debug(GaussJordanForm.OperateFractionMatrix(m));
		// Tools.debug(GaussJordanForm.OperateDoubleMatrix(m));

		DoubleMatrix n = ReverseOfDoubleMatrix(m);

		Tools.debug(m.product(n));

	}

	public static DoubleMatrix OperateDoubleMatrix(DoubleMatrix matrix)
	{
		DoubleMatrix result = (DoubleMatrix) matrix.clone();

		Position position = new Position();

		int column = 0;

		// Forward
		for (column = Position.FIRST; column < result.getRows(); column++) {

			position.setPosition(column, column);

			Double diag = result.get(position);

			if (diag.equals(0.0)) {
				// Exchange row to avoid ZERO diagram element.
				boolean found = false;

				for (int row = column + 1; row < result.getRows(); row++) {

					Double e = result.get(row, column);

					if (!e.equals(0.0)) {

						Matrix<Double> temp = result.cloneRow(column);

						result.quoteRow(column).clone(result.cloneRow(row));

						result.quoteRow(row).clone(temp);

						diag = result.get(position);

						found = true;
						break;
					}
				}
				if (!found) {
					break;
				}
			}

			// Let the diagram element be ONE.
			if (!diag.equals(1.0)) {
				Matrix<Double> row = result.quoteRow(column);

				for (int r = Position.FIRST; r < row.getRows(); r++) {
					for (int c = Position.FIRST; c < row.getColumns(); c++) {
						row.set(row.get(r, c) / diag, r, c);
					}
				}
			}

			// Eliminate the elements below the diagram.
			for (int row = column + 1; row < result.getRows(); row++) {
				Double number = result.get(row, column);
				DoubleMatrix quoteRow = result.quoteRow(row);
				DoubleMatrix cloneColumn = result.cloneRow(column);
				quoteRow.minus(cloneColumn.multiply(number));
			}

			Parser.procedure += result.toHTML() + "<br>";

			if (column == result.getRows()) {
				break;
			}
		}

		Parser.procedure += "<hr>";

		// Backward
		for (column--; column > Position.FIRST; column--) {
			position.setPosition(column, column);
			Double diag = result.get(position);

			// Eliminate the elements above the NON-ZERO diagram element.
			if (!diag.equals(0.0)) {
				for (int row = column - 1; row >= Position.FIRST; row--) {
					Double number = result.get(row, column);
					DoubleMatrix quoteRow = result.quoteRow(row);
					DoubleMatrix cloneColumn = result.cloneRow(column);
					quoteRow.minus(cloneColumn.multiply(number));
				}
			}

			Parser.procedure += result.toHTML() + "<br>";
		}

		Parser.procedure += "<hr>";

		return result;
	}

	public static FractionMatrix OperateFractionMatrix(FractionMatrix matrix)
	{
		FractionMatrix result = new FractionMatrix(matrix.clone());

		Position position = new Position();

		int column = 0;

		// Forward
		for (column = Position.FIRST; column < result.getRows(); column++) {

			position.setPosition(column, column);

			Fraction diag = result.get(position);

			if (diag.equals(Fraction.Zero())) {
				// Exchange row to avoid ZERO diagram element.
				boolean found = false;

				for (int row = column + 1; row < result.getRows(); row++) {

					Fraction e = result.get(row, column);

					if (!e.equals(Fraction.Zero())) {

						Matrix<Fraction> temp = result.cloneRow(column);

						result.quoteRow(column).clone(result.cloneRow(row));

						result.quoteRow(row).clone(temp);

						diag = result.get(position);

						found = true;
						break;
					}
				}
				if (!found) {
					break;
				}
			}

			// Let the diagram element be ONE.
			if (!diag.equals(Fraction.One())) {
				Matrix<Fraction> row = result.quoteRow(column);

				for (int r = Position.FIRST; r < row.getRows(); r++) {
					for (int c = Position.FIRST; c < row.getColumns(); c++) {
						row.get(r, c).divide(diag);
					}
				}
			}

			// Eliminate the elements below the diagram.
			for (int row = column + 1; row < result.getRows(); row++) {
				Fraction number = result.get(row, column);
				FractionMatrix quoteRow = result.quoteRow(row);
				FractionMatrix cloneColumn = result.cloneRow(column);
				quoteRow.minus(cloneColumn.multiply(number));
			}

			Parser.procedure += result.toHTML() + "<br>";

			if (column == result.getRows()) {
				break;
			}
		}

		Parser.procedure += "<hr>";

		// Backward
		for (column--; column > Position.FIRST; column--) {
			position.setPosition(column, column);
			Fraction diag = result.get(position);

			// Eliminate the elements above the NON-ZERO diagram element.
			if (!diag.equals(Fraction.Zero())) {
				for (int row = column - 1; row >= Position.FIRST; row--) {
					Fraction number = result.get(row, column);
					FractionMatrix quoteRow = result.quoteRow(row);
					FractionMatrix cloneColumn = result.cloneRow(column);
					quoteRow.minus(cloneColumn.multiply(number));
				}
			}

			Parser.procedure += result.toHTML() + "<br>";
		}

		Parser.procedure += "<hr>";

		return result;
	}

	public static final DoubleMatrix ReverseOfDoubleMatrix(Matrix<Double> matrix)
	{
		if (matrix.getRows() != matrix.getColumns()) {
			throw new SizeDisagreeException("The matrix should be a SQUARE matrix.");
		}

		int order = matrix.getRows();

		matrix = matrix.clone(0, order - 1, 0, order - 1);

		matrix.insertColumns(IdentityMatrix.makeIdentityMatrix(order), order);

		DoubleMatrix mixed = new DoubleMatrix(matrix);

		DoubleMatrix reverse = GaussJordanForm.OperateDoubleMatrix(mixed);

		return reverse.clone(0, order - 1, order, 2 * order - 1);
	}

	public DoubleMatrix operate(DoubleMatrix matrix)
	{
		return OperateDoubleMatrix(matrix);
	}

	public FractionMatrix operate(FractionMatrix... matrixs)
	{
		return OperateFractionMatrix(matrixs[0]);
	}
}

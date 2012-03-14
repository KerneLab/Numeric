package org.kernelab.numeric;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.kernelab.basis.Function;
import org.kernelab.basis.Interval;
import org.kernelab.basis.Tools;
import org.kernelab.numeric.matrix.DoubleMatrix;
import org.kernelab.numeric.matrix.Size;
import org.kernelab.numeric.util.GaussJordanForm;

public class Numeric
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

	}

	public static Function PolynomialFitting(Map<Double, Double> data, int exponent)
	{
		int rows = data.size();

		int columns = exponent + 1;

		DoubleMatrix coeff = new DoubleMatrix(new Size(rows, columns));

		DoubleMatrix value = new DoubleMatrix(new Size(rows, 1));

		List<Double> dataList = new ArrayList<Double>(data.size());

		Tools.listOfCollection(dataList, data.keySet());

		for (int row = 0; row < rows; row++) {

			double c = 1;
			double x = dataList.get(row);

			value.set(data.get(x), row, 0);

			for (int column = 0; column < columns; column++) {
				coeff.set(c, row, column);
				c *= x;
			}
		}

		DoubleMatrix coeft = coeff.cloneTranspose();

		coeff.product(coeft, coeff);

		value.product(coeft, value);

		coeff = new DoubleMatrix(coeff.joinRows(value));

		GaussJordanForm gjf = new GaussJordanForm();

		DoubleMatrix result = gjf.operate(coeff);

		final List<Double> polynomial = result.getListOfColumn(result.getColumns() - 1);

		Function function = new Function() {

			/**
			 * 
			 */
			private static final long	serialVersionUID	= 8519460784032303825L;

			@Override
			public double valueAt(double x)
			{
				double value = 0.0;

				double coeff = 1.0;

				for (Double p : polynomial) {
					value += coeff * p;
					coeff *= x;
				}

				return value;
			}

		};

		function.getDomain().add(
				new Interval<Double>(Tools.min(dataList), Tools.max(dataList)));

		return function;
	}
}

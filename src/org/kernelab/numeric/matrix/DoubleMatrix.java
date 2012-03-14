package org.kernelab.numeric.matrix;

import org.kernelab.basis.Tools;
import org.kernelab.numeric.apps.Parser;

public class DoubleMatrix extends Matrix<Double> implements AlgebraicMatrix<Double>
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3689653499683725925L;

	/**
	 * Add matrix a and b, put the result into matrix c.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return Matrix c.
	 */
	public static final Matrix<Double> Add(Matrix<Double> a, Matrix<Double> b,
			Matrix<Double> c)
	{
		if (a.sameSizeOf(b)) {

			Matrix.VerifyLimits(c, a.getRows(), a.getColumns());

			for (int row = Position.FIRST; row < c.getRows(); row++) {
				for (int column = Position.FIRST; column < c.getColumns(); column++) {
					c.set(a.get(row, column) + b.get(row, column), row, column);
				}
			}

		} else {
			throw new SizeDisagreeException(a.getSize(), b.getSize());
		}

		return c;
	}

	/**
	 * Divide matrix a by Double b and put the result into matrix c.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return Matrix c.
	 */
	public static final Matrix<Double> Divide(Matrix<Double> a, Double b, Matrix<Double> c)
	{
		Matrix.VerifyLimits(c, a.getRows(), a.getColumns());

		for (int row = Position.FIRST; row < c.getRows(); row++) {
			for (int column = Position.FIRST; column < c.getColumns(); column++) {
				c.set(a.get(row, column) / b, row, column);
			}
		}

		return c;
	}

	/**
	 * Divide matrix a by b and put the result into matrix c.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return Matrix c.
	 */
	public static final Matrix<Double> Divide(Matrix<Double> a, Matrix<Double> b,
			Matrix<Double> c)
	{
		if (a.sameSizeOf(b)) {

			Matrix.VerifyLimits(c, a.getRows(), a.getColumns());

			for (int row = Position.FIRST; row < c.getRows(); row++) {
				for (int column = Position.FIRST; column < c.getColumns(); column++) {
					c.set(a.get(row, column) / b.get(row, column), row, column);
				}
			}

		} else if (b.isCell()) {
			Divide(a, b.get(), c);
		} else {
			throw new SizeDisagreeException(a.getSize(), b.getSize());
		}

		return c;
	}

	public static final DoubleMatrix InstanceFromFractionMatrix(FractionMatrix fMatrix)
	{
		DoubleMatrix dMatrix = new DoubleMatrix(fMatrix.getSize());

		for (int row = 0; row < dMatrix.getRows(); row++) {

			for (int column = 0; column < dMatrix.getColumns(); column++) {

				dMatrix.set(fMatrix.get(row, column).getValue(), row, column);
			}
		}

		return dMatrix;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		DoubleMatrix a = DoubleMatrix.Parse("[0.7,0.3;0.3,0.7]");
		Tools.debug(a);
		DoubleMatrix b = DoubleMatrix.Parse("[0.9,0;0,0.2]");
		Tools.debug(b);

		a.product(b);

		Tools.debug(a);
	}

	/**
	 * Matrix a minus b and put the result into matrix c.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return Matrix c.
	 */
	public static final Matrix<Double> Minus(Matrix<Double> a, Matrix<Double> b,
			Matrix<Double> c)
	{
		if (a.sameSizeOf(b)) {

			Matrix.VerifyLimits(c, a.getRows(), a.getColumns());

			for (int row = Position.FIRST; row < c.getRows(); row++) {
				for (int column = Position.FIRST; column < c.getColumns(); column++) {
					c.set(a.get(row, column) - b.get(row, column), row, column);
				}
			}

		} else {
			throw new SizeDisagreeException(a.getSize(), b.getSize());
		}

		return c;
	}

	/**
	 * Multiply matrix a by Double b and put the result into matrix c.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return Matrix c.
	 */
	public static final Matrix<Double> Multiply(Matrix<Double> a, Double b,
			Matrix<Double> c)
	{
		Matrix.VerifyLimits(c, a.getRows(), a.getColumns());

		for (int row = Position.FIRST; row < c.getRows(); row++) {
			for (int column = Position.FIRST; column < c.getColumns(); column++) {
				c.set(b * a.get(row, column), row, column);
			}
		}

		return c;
	}

	/**
	 * Multiply matrix a by b and put the result into matrix c.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return Matrix c.
	 */
	public static final Matrix<Double> Multiply(Matrix<Double> a, Matrix<Double> b,
			Matrix<Double> c)
	{
		if (a.isCell()) {

			Multiply(b, a.get(), c);

		} else if (b.isCell()) {

			Multiply(a, b.get(), c);

		} else if (a.sameSizeOf(b)) {

			Matrix.VerifyLimits(c, a.getRows(), a.getColumns());

			for (int row = Position.FIRST; row < c.getRows(); row++) {
				for (int column = Position.FIRST; column < c.getColumns(); column++) {
					c.set(a.get(row, column) * b.get(row, column), row, column);
				}
			}

		} else {
			throw new SizeDisagreeException(a.getSize(), b.getSize());
		}

		return c;
	}

	public static final DoubleMatrix Parse(String exp)
	{
		DoubleMatrix matrix = null;

		exp = exp.replaceAll("\\[", "");
		exp = exp.replaceAll("\\]", "");

		String[] rows = exp.split(Parser.ROW_SPLIT);
		String[] columns = rows[Position.FIRST].split(Parser.COLUMN_SPLIT);

		matrix = new DoubleMatrix(new Size(rows.length, columns.length));

		for (int row = Position.FIRST; row < rows.length; row++) {
			columns = rows[row].split(Parser.COLUMN_SPLIT);
			for (int column = Position.FIRST; column < columns.length; column++) {
				matrix.set(Double.valueOf(columns[column]), row, column);
			}
		}

		return matrix;
	}

	/**
	 * Product matrix a and b, put the result into matrix c. If matrix c refers
	 * to a (or b) then a (or b) will be cloned.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return Matrix c.
	 */
	public static final Matrix<Double> Product(Matrix<Double> a, Matrix<Double> b,
			Matrix<Double> c)
	{
		if (a.isCell() || b.isCell()) {

			Multiply(a, b, c);

		} else if (a.getColumns() == b.getRows()) {

			if (a == c) {
				a = a.clone();
			}
			if (b == c) {
				b = b.clone();
			}

			Matrix.VerifyLimits(c, a.getRows(), b.getColumns());

			int indices = a.getColumns();

			double sum = 0.0;
			for (int row = Position.FIRST; row < c.getRows(); row++) {
				for (int column = Position.FIRST; column < c.getColumns(); column++) {
					sum = 0;
					for (int index = Position.FIRST; index < indices; index++) {
						sum += a.get(row, index) * b.get(index, column);
					}
					c.set(sum, row, column);
				}
			}

		} else {
			throw new SizeDisagreeException(a.getSize(), b.getSize());
		}

		return c;
	}

	public DoubleMatrix()
	{
		super();
	}

	/**
	 * Clone
	 */
	public DoubleMatrix(Cell<Double>[][] matrix)
	{
		this(matrix, null);
	}

	/**
	 * Clone
	 */
	public DoubleMatrix(Cell<Double>[][] matrix, Range range)
	{
		super(matrix, range);
	}

	/**
	 * Clone
	 */
	public DoubleMatrix(Double[][] matrix)
	{
		this(matrix, null);
	}

	/**
	 * Clone
	 */
	public DoubleMatrix(Double[][] matrix, Range range)
	{
		super(matrix, range);
	}

	public DoubleMatrix(int rows, int columns)
	{
		super(rows, columns);
	}

	/**
	 * Quote
	 */
	public DoubleMatrix(Matrix<Double> matrix)
	{
		this(matrix, null);
	}

	/**
	 * Quote
	 */
	public DoubleMatrix(Matrix<Double> matrix, Range range)
	{
		super(matrix, range);
	}

	public DoubleMatrix(Sized size)
	{
		super(size);
	}

	public DoubleMatrix add(Matrix<Double> matrix)
	{
		return add(this, matrix);
	}

	public DoubleMatrix add(Matrix<Double> a, Matrix<Double> b)
	{
		return (DoubleMatrix) Add(a, b, this);
	}

	@Override
	public DoubleMatrix clone()
	{
		return this.clone(Range.Full(this));
	}

	@Override
	protected DoubleMatrix createClone(Matrix<Double> matrix, Range range)
	{
		return new DoubleMatrix(matrix.getMatrix(), range);
	}

	@Override
	protected DoubleMatrix createMatrix(Sized size)
	{
		return new DoubleMatrix(size);
	}

	@Override
	protected DoubleMatrix createQuote(Matrix<Double> matrix, Range range)
	{
		return new DoubleMatrix(matrix, range);
	}

	public DoubleMatrix divide(Double number)
	{
		return divide(this, number);
	}

	public DoubleMatrix divide(Matrix<Double> matrix)
	{
		return divide(this, matrix);
	}

	public DoubleMatrix divide(Matrix<Double> matrix, Double number)
	{
		return (DoubleMatrix) Divide(matrix, number, this);
	}

	public DoubleMatrix divide(Matrix<Double> a, Matrix<Double> b)
	{
		return (DoubleMatrix) Divide(a, b, this);
	}

	@Override
	public Cell<Double> getCell()
	{
		return this.getCell(Position.FIRST, Position.FIRST);
	}

	@Override
	public Cell<Double> getCell(int index)
	{
		return this.getCell(this.getRowOfIndex(index), this.getColumnOfIndex(index));
	}

	@Override
	public Cell<Double> getCell(int row, int column)
	{
		return this.getMatrix()[row][column];
	}

	@Override
	public Cell<Double> getCell(Position position)
	{
		return this.getCell(position.getRow(), position.getColumn());
	}

	public DoubleMatrix minus(Matrix<Double> matrix)
	{
		return minus(this, matrix);
	}

	public DoubleMatrix minus(Matrix<Double> a, Matrix<Double> b)
	{
		return (DoubleMatrix) Minus(a, b, this);
	}

	public DoubleMatrix multiply(Double number)
	{
		return multiply(this, number);
	}

	public DoubleMatrix multiply(Matrix<Double> matrix)
	{
		return multiply(this, matrix);
	}

	public DoubleMatrix multiply(Matrix<Double> matrix, Double number)
	{
		return (DoubleMatrix) Multiply(matrix, number, this);
	}

	public DoubleMatrix multiply(Matrix<Double> a, Matrix<Double> b)
	{
		return (DoubleMatrix) Multiply(a, b, this);
	}

	public DoubleMatrix product(Matrix<Double> matrix)
	{
		return product(this, matrix);
	}

	public DoubleMatrix product(Matrix<Double> a, Matrix<Double> b)
	{
		return (DoubleMatrix) Product(a, b, this);
	}
}

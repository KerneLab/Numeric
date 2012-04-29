package org.kernelab.numeric.matrix;

import org.kernelab.basis.Tools;

public class FloatMatrix extends Matrix<Float> implements AlgebraicMatrix<Float>
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -4328304678672017332L;

	/**
	 * Add matrix a and b, put the result into matrix c.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return Matrix c.
	 */
	public static final Matrix<Float> Add(Matrix<Float> a, Matrix<Float> b,
			Matrix<Float> c)
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
	 * Divide matrix a by Float b and put the result into matrix c.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return Matrix c.
	 */
	public static final Matrix<Float> Divide(Matrix<Float> a, Float b, Matrix<Float> c)
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
	public static final Matrix<Float> Divide(Matrix<Float> a, Matrix<Float> b,
			Matrix<Float> c)
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

	public static final FloatMatrix InstanceFromFractionMatrix(FractionMatrix fMatrix)
	{
		FloatMatrix dMatrix = new FloatMatrix(fMatrix.getSize());

		for (int row = 0; row < dMatrix.getRows(); row++) {

			for (int column = 0; column < dMatrix.getColumns(); column++) {

				dMatrix.set((float) fMatrix.get(row, column).getValue(), row, column);
			}
		}

		return dMatrix;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		FloatMatrix a = FloatMatrix.Parse("[0.7,0.3;0.3,0.7]");
		Tools.debug(a);
		FloatMatrix b = FloatMatrix.Parse("[0.9,0;0,0.2]");
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
	public static final Matrix<Float> Minus(Matrix<Float> a, Matrix<Float> b,
			Matrix<Float> c)
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
	 * Multiply matrix a by Float b and put the result into matrix c.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return Matrix c.
	 */
	public static final Matrix<Float> Multiply(Matrix<Float> a, Float b, Matrix<Float> c)
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
	public static final Matrix<Float> Multiply(Matrix<Float> a, Matrix<Float> b,
			Matrix<Float> c)
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

	public static final FloatMatrix Parse(String exp)
	{
		FloatMatrix matrix = null;

		exp = exp.replaceAll("\\[", "");
		exp = exp.replaceAll("\\]", "");

		String[] rows = exp.split(Parser.ROW_SPLIT);
		String[] columns = rows[Position.FIRST].split(Parser.COLUMN_SPLIT);

		matrix = new FloatMatrix(new Size(rows.length, columns.length));

		for (int row = Position.FIRST; row < rows.length; row++) {
			columns = rows[row].split(Parser.COLUMN_SPLIT);
			for (int column = Position.FIRST; column < columns.length; column++) {
				matrix.set(Float.valueOf(columns[column]), row, column);
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
	public static final Matrix<Float> Product(Matrix<Float> a, Matrix<Float> b,
			Matrix<Float> c)
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

			Float sum = 0.0f;
			for (int row = Position.FIRST; row < c.getRows(); row++) {
				for (int column = Position.FIRST; column < c.getColumns(); column++) {
					sum = 0f;
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

	public FloatMatrix()
	{
		super();
	}

	/**
	 * Clone
	 */
	public FloatMatrix(Cell<Float>[][] matrix)
	{
		this(matrix, null);
	}

	/**
	 * Clone
	 */
	public FloatMatrix(Cell<Float>[][] matrix, Range range)
	{
		super(matrix, range);
	}

	/**
	 * Clone
	 */
	public FloatMatrix(Float[][] matrix)
	{
		this(matrix, null);
	}

	/**
	 * Clone
	 */
	public FloatMatrix(Float[][] matrix, Range range)
	{
		super(matrix, range);
	}

	public FloatMatrix(int rows, int columns)
	{
		super(rows, columns);
	}

	/**
	 * Quote
	 */
	public FloatMatrix(Matrix<Float> matrix)
	{
		this(matrix, null);
	}

	/**
	 * Quote
	 */
	public FloatMatrix(Matrix<Float> matrix, Range range)
	{
		super(matrix, range);
	}

	public FloatMatrix(Sized size)
	{
		super(size);
	}

	public FloatMatrix add(Matrix<Float> matrix)
	{
		return add(this, matrix);
	}

	public FloatMatrix add(Matrix<Float> a, Matrix<Float> b)
	{
		return (FloatMatrix) Add(a, b, this);
	}

	@Override
	public FloatMatrix clone()
	{
		return this.clone(Range.Full(this));
	}

	@Override
	protected FloatMatrix createClone(Matrix<Float> matrix, Range range)
	{
		return new FloatMatrix(matrix.getMatrix(), range);
	}

	@Override
	protected FloatMatrix createMatrix(Sized size)
	{
		return new FloatMatrix(size);
	}

	@Override
	protected FloatMatrix createQuote(Matrix<Float> matrix, Range range)
	{
		return new FloatMatrix(matrix, range);
	}

	public FloatMatrix divide(Float number)
	{
		return divide(this, number);
	}

	public FloatMatrix divide(Matrix<Float> matrix)
	{
		return divide(this, matrix);
	}

	public FloatMatrix divide(Matrix<Float> matrix, Float number)
	{
		return (FloatMatrix) Divide(matrix, number, this);
	}

	public FloatMatrix divide(Matrix<Float> a, Matrix<Float> b)
	{
		return (FloatMatrix) Divide(a, b, this);
	}

	@Override
	public Cell<Float> getCell()
	{
		return this.getCell(Position.FIRST, Position.FIRST);
	}

	@Override
	public Cell<Float> getCell(int index)
	{
		return this.getCell(this.getRowOfIndex(index), this.getColumnOfIndex(index));
	}

	@Override
	public Cell<Float> getCell(int row, int column)
	{
		return this.getMatrix()[row][column];
	}

	@Override
	public Cell<Float> getCell(Position position)
	{
		return this.getCell(position.getRow(), position.getColumn());
	}

	public FloatMatrix minus(Matrix<Float> matrix)
	{
		return minus(this, matrix);
	}

	public FloatMatrix minus(Matrix<Float> a, Matrix<Float> b)
	{
		return (FloatMatrix) Minus(a, b, this);
	}

	public FloatMatrix multiply(Float number)
	{
		return multiply(this, number);
	}

	public FloatMatrix multiply(Matrix<Float> matrix)
	{
		return multiply(this, matrix);
	}

	public FloatMatrix multiply(Matrix<Float> matrix, Float number)
	{
		return (FloatMatrix) Multiply(matrix, number, this);
	}

	public FloatMatrix multiply(Matrix<Float> a, Matrix<Float> b)
	{
		return (FloatMatrix) Multiply(a, b, this);
	}

	public FloatMatrix product(Matrix<Float> matrix)
	{
		return product(this, matrix);
	}

	public FloatMatrix product(Matrix<Float> a, Matrix<Float> b)
	{
		return (FloatMatrix) Product(a, b, this);
	}
}

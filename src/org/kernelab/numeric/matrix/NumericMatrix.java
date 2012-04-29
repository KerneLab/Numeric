package org.kernelab.numeric.matrix;

import org.kernelab.numeric.AlgebraicNumber;

public class NumericMatrix<N extends AlgebraicNumber<N>> extends Matrix<N> implements
		AlgebraicMatrix<N>
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4487640558290248727L;

	/**
	 * Add matrix a and b, put the result into matrix c.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return Matrix c.
	 */
	public static final <N extends AlgebraicNumber<N>> Matrix<N> Add(Matrix<N> a,
			Matrix<N> b, Matrix<N> c)
	{
		if (a.sameSizeOf(b)) {

			Matrix.VerifyLimits(c, a.getRows(), a.getColumns());

			for (int row = Position.FIRST; row < c.getRows(); row++) {
				for (int column = Position.FIRST; column < c.getColumns(); column++) {
					N n = c.get(row, column);
					if (n == null) {
						n = a.get().zero();
						c.set(n, row, column);
					}
					n.add(a.get(row, column), b.get(row, column));
				}
			}

		} else {
			throw new SizeDisagreeException(a.getSize(), b.getSize());
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
	public static final <N extends AlgebraicNumber<N>> Matrix<N> Divide(Matrix<N> a,
			Matrix<N> b, Matrix<N> c)
	{
		if (a.sameSizeOf(b)) {

			Matrix.VerifyLimits(c, a.getRows(), a.getColumns());

			for (int row = Position.FIRST; row < c.getRows(); row++) {
				for (int column = Position.FIRST; column < c.getColumns(); column++) {
					N n = c.get(row, column);
					if (n == null) {
						n = a.get().zero();
						c.set(n, row, column);
					}
					n.divide(a.get(row, column), b.get(row, column));
				}
			}

		} else if (b.isCell()) {
			Divide(a, b.get(), c);
		} else {
			throw new SizeDisagreeException(a.getSize(), b.getSize());
		}

		return c;
	}

	/**
	 * Divide matrix a by number b and put the result into matrix c.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return Matrix c.
	 */
	public static final <N extends AlgebraicNumber<N>> Matrix<N> Divide(Matrix<N> a, N b,
			Matrix<N> c)
	{
		Matrix.VerifyLimits(c, a.getRows(), a.getColumns());

		for (int row = Position.FIRST; row < c.getRows(); row++) {
			for (int column = Position.FIRST; column < c.getColumns(); column++) {
				N n = c.get(row, column);
				if (n == null) {
					n = a.get().zero();
					c.set(n, row, column);
				}
				n.divide(a.get(row, column), b);
			}
		}

		return c;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

	}

	/**
	 * Matrix a minus b and put the result into matrix c.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return Matrix c.
	 */
	public static final <N extends AlgebraicNumber<N>> Matrix<N> Minus(Matrix<N> a,
			Matrix<N> b, Matrix<N> c)
	{
		if (a.sameSizeOf(b)) {

			Matrix.VerifyLimits(c, a.getRows(), a.getColumns());

			for (int row = Position.FIRST; row < c.getRows(); row++) {
				for (int column = Position.FIRST; column < c.getColumns(); column++) {
					N n = c.get(row, column);
					if (n == null) {
						n = a.get().zero();
						c.set(n, row, column);
					}
					n.minus(a.get(row, column), b.get(row, column));
				}
			}

		} else {
			throw new SizeDisagreeException(a.getSize(), b.getSize());
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
	public static final <N extends AlgebraicNumber<N>> Matrix<N> Multiply(Matrix<N> a,
			Matrix<N> b, Matrix<N> c)
	{
		if (a.isCell()) {

			Multiply(b, a.get(), c);

		} else if (b.isCell()) {

			Multiply(a, b.get(), c);

		} else if (a.sameSizeOf(b)) {

			Matrix.VerifyLimits(c, a.getRows(), a.getColumns());

			for (int row = Position.FIRST; row < c.getRows(); row++) {
				for (int column = Position.FIRST; column < c.getColumns(); column++) {
					N n = c.get(row, column);
					if (n == null) {
						n = a.get().zero();
						c.set(n, row, column);
					}
					n.multiply(a.get(row, column), b.get(row, column));
				}
			}

		} else {
			throw new SizeDisagreeException(a.getSize(), b.getSize());
		}

		return c;
	}

	/**
	 * Multiply matrix a by number b and put the result into matrix c.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return Matrix c.
	 */
	public static final <N extends AlgebraicNumber<N>> Matrix<N> Multiply(Matrix<N> a,
			N b, Matrix<N> c)
	{
		Matrix.VerifyLimits(c, a.getRows(), a.getColumns());

		for (int row = Position.FIRST; row < c.getRows(); row++) {
			for (int column = Position.FIRST; column < c.getColumns(); column++) {
				N n = c.get(row, column);
				if (n == null) {
					n = a.get().zero();
					c.set(n, row, column);
				}
				n.multiply(a.get(row, column), b);
			}
		}

		return c;
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
	public static final <N extends AlgebraicNumber<N>> Matrix<N> Product(Matrix<N> a,
			Matrix<N> b, Matrix<N> c)
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

			N sum = null;
			N mul = a.get().zero();
			for (int row = Position.FIRST; row < c.getRows(); row++) {
				for (int column = Position.FIRST; column < c.getColumns(); column++) {
					sum = a.get().zero();
					for (int index = Position.FIRST; index < indices; index++) {
						sum.add(mul.multiply(a.get(row, index), b.get(index, column)));
					}
					c.set(sum, row, column);
				}
			}

		} else {
			throw new SizeDisagreeException(a.getSize(), b.getSize());
		}

		return c;
	}

	public NumericMatrix()
	{
		super();
	}

	/**
	 * Clone
	 */
	public NumericMatrix(Cell<N>[][] matrix)
	{
		this(matrix, null);
	}

	/**
	 * Clone
	 */
	public NumericMatrix(Cell<N>[][] matrix, Range range)
	{
		super(matrix, range);
	}

	public NumericMatrix(int rows, int columns)
	{
		super(rows, columns);
	}

	/**
	 * Quote
	 */
	public NumericMatrix(Matrix<N> matrix)
	{
		this(matrix, null);
	}

	/**
	 * Quote
	 */
	public NumericMatrix(Matrix<N> matrix, Range range)
	{
		super(matrix, range);
	}

	/**
	 * Clone
	 */
	public NumericMatrix(N[][] matrix)
	{
		this(matrix, null);
	}

	/**
	 * Clone
	 */
	public NumericMatrix(N[][] matrix, Range range)
	{
		super(matrix, range);
	}

	public NumericMatrix(Sized size)
	{
		super(size);
	}

	public NumericMatrix<N> add(Matrix<N> matrix)
	{
		return add(this, matrix);
	}

	public NumericMatrix<N> add(Matrix<N> a, Matrix<N> b)
	{
		return (NumericMatrix<N>) Add(a, b, this);
	}

	@Override
	public NumericMatrix<N> clone()
	{
		return this.clone(Range.Full(this));
	}

	@Override
	protected NumericMatrix<N> createClone(Matrix<N> matrix, Range range)
	{
		return new NumericMatrix<N>(matrix.getMatrix(), range);
	}

	@Override
	protected NumericMatrix<N> createMatrix(Sized size)
	{
		return new NumericMatrix<N>(size);
	}

	@Override
	protected NumericMatrix<N> createQuote(Matrix<N> matrix, Range range)
	{
		return new NumericMatrix<N>(matrix, range);
	}

	public NumericMatrix<N> divide(Matrix<N> matrix)
	{
		return divide(this, matrix);
	}

	public NumericMatrix<N> divide(Matrix<N> a, Matrix<N> b)
	{
		return (NumericMatrix<N>) Divide(a, b, this);
	}

	public NumericMatrix<N> divide(Matrix<N> matrix, N number)
	{
		return (NumericMatrix<N>) Divide(matrix, number, this);
	}

	public NumericMatrix<N> divide(N number)
	{
		return divide(this, number);
	}

	public NumericMatrix<N> minus(Matrix<N> matrix)
	{
		return minus(this, matrix);
	}

	public NumericMatrix<N> minus(Matrix<N> a, Matrix<N> b)
	{
		return (NumericMatrix<N>) Minus(a, b, this);
	}

	public NumericMatrix<N> multiply(Matrix<N> matrix)
	{
		return multiply(this, matrix);
	}

	public NumericMatrix<N> multiply(Matrix<N> a, Matrix<N> b)
	{
		return (NumericMatrix<N>) Multiply(a, b, this);
	}

	public NumericMatrix<N> multiply(Matrix<N> matrix, N number)
	{
		return (NumericMatrix<N>) Multiply(matrix, number, this);
	}

	public NumericMatrix<N> multiply(N number)
	{
		return multiply(this, number);
	}

	public NumericMatrix<N> product(Matrix<N> matrix)
	{
		return product(this, matrix);
	}

	public NumericMatrix<N> product(Matrix<N> a, Matrix<N> b)
	{
		return (NumericMatrix<N>) Product(a, b, this);
	}
}

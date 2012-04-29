package org.kernelab.numeric.matrix;

import org.kernelab.numeric.Complex;

public class ComplexMatrix extends NumericMatrix<Complex>
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5737544235786533949L;

	// /**
	// * Add matrix a and b, put the result into matrix c.
	// *
	// * @param a
	// * @param b
	// * @param c
	// * @return Matrix c.
	// */
	// public static final Matrix<Complex> Add(Matrix<Complex> a,
	// Matrix<Complex> b,
	// Matrix<Complex> c)
	// {
	// if (a.sameSizeOf(b)) {
	//
	// Matrix.VerifyLimits(c, a.getRows(), a.getColumns());
	//
	// for (int row = Position.FIRST; row < c.getRows(); row++) {
	// for (int column = Position.FIRST; column < c.getColumns(); column++) {
	// Complex e = c.get(row, column);
	// if (e == null) {
	// e = new Complex(0);
	// c.set(e, row, column);
	// }
	// e.add(a.get(row, column), b.get(row, column));
	// }
	// }
	//
	// } else {
	// throw new SizeDisagreeException(a.getSize(), b.getSize());
	// }
	//
	// return c;
	// }
	//
	// /**
	// * Divide matrix a by Complex b and put the result into matrix c.
	// *
	// * @param a
	// * @param b
	// * @param c
	// * @return Matrix c.
	// */
	// public static final Matrix<Complex> Divide(Matrix<Complex> a, Complex b,
	// Matrix<Complex> c)
	// {
	// Matrix.VerifyLimits(c, a.getRows(), a.getColumns());
	//
	// for (int row = Position.FIRST; row < c.getRows(); row++) {
	// for (int column = Position.FIRST; column < c.getColumns(); column++) {
	// Complex e = c.get(row, column);
	// if (e == null) {
	// e = new Complex(0);
	// c.set(e, row, column);
	// }
	// e.divide(a.get(row, column), b);
	// }
	// }
	//
	// return c;
	// }
	//
	// /**
	// * Divide matrix a by b and put the result into matrix c.
	// *
	// * @param a
	// * @param b
	// * @param c
	// * @return Matrix c.
	// */
	// public static final Matrix<Complex> Divide(Matrix<Complex> a,
	// Matrix<Complex> b,
	// Matrix<Complex> c)
	// {
	// if (a.sameSizeOf(b)) {
	//
	// Matrix.VerifyLimits(c, a.getRows(), a.getColumns());
	//
	// for (int row = Position.FIRST; row < c.getRows(); row++) {
	// for (int column = Position.FIRST; column < c.getColumns(); column++) {
	// Complex e = c.get(row, column);
	// if (e == null) {
	// e = new Complex(0);
	// c.set(e, row, column);
	// }
	// e.divide(a.get(row, column), b.get(row, column));
	// }
	// }
	//
	// } else if (b.isCell()) {
	// Divide(a, b.get(), c);
	// } else {
	// throw new SizeDisagreeException(a.getSize(), b.getSize());
	// }
	//
	// return c;
	// }

	public static final ComplexMatrix InstanceFromDoubleMatrix(Matrix<Double> dMatrix)
	{
		ComplexMatrix cMatrix = new ComplexMatrix(dMatrix.getSize());

		for (int row = 0; row < dMatrix.getRows(); row++) {

			for (int column = 0; column < dMatrix.getColumns(); column++) {

				cMatrix.set(new Complex(dMatrix.get(row, column)), row, column);
			}
		}

		return cMatrix;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

	}

	// /**
	// * Matrix a minus b and put the result into matrix c.
	// *
	// * @param a
	// * @param b
	// * @param c
	// * @return Matrix c.
	// */
	// public static final Matrix<Complex> Minus(Matrix<Complex> a,
	// Matrix<Complex> b,
	// Matrix<Complex> c)
	// {
	// if (a.sameSizeOf(b)) {
	//
	// Matrix.VerifyLimits(c, a.getRows(), a.getColumns());
	//
	// for (int row = Position.FIRST; row < c.getRows(); row++) {
	// for (int column = Position.FIRST; column < c.getColumns(); column++) {
	// Complex e = c.get(row, column);
	// if (e == null) {
	// e = new Complex(0);
	// c.set(e, row, column);
	// }
	// e.minus(a.get(row, column), b.get(row, column));
	// }
	// }
	//
	// } else {
	// throw new SizeDisagreeException(a.getSize(), b.getSize());
	// }
	//
	// return c;
	// }
	//
	// /**
	// * Multiply matrix a by Complex b and put the result into matrix c.
	// *
	// * @param a
	// * @param b
	// * @param c
	// * @return Matrix c.
	// */
	// public static final Matrix<Complex> Multiply(Matrix<Complex> a, Complex
	// b,
	// Matrix<Complex> c)
	// {
	// Matrix.VerifyLimits(c, a.getRows(), a.getColumns());
	//
	// for (int row = Position.FIRST; row < c.getRows(); row++) {
	// for (int column = Position.FIRST; column < c.getColumns(); column++) {
	// Complex e = c.get(row, column);
	// if (e == null) {
	// e = new Complex(0);
	// c.set(e, row, column);
	// }
	// e.multiply(a.get(row, column), b);
	// }
	// }
	//
	// return c;
	// }
	//
	// /**
	// * Multiply matrix a by b and put the result into matrix c.
	// *
	// * @param a
	// * @param b
	// * @param c
	// * @return Matrix c.
	// */
	// public static final Matrix<Complex> Multiply(Matrix<Complex> a,
	// Matrix<Complex> b,
	// Matrix<Complex> c)
	// {
	// if (a.isCell()) {
	//
	// Multiply(b, a.get(), c);
	//
	// } else if (b.isCell()) {
	//
	// Multiply(a, b.get(), c);
	//
	// } else if (a.sameSizeOf(b)) {
	//
	// Matrix.VerifyLimits(c, a.getRows(), a.getColumns());
	//
	// for (int row = Position.FIRST; row < c.getRows(); row++) {
	// for (int column = Position.FIRST; column < c.getColumns(); column++) {
	// Complex e = c.get(row, column);
	// if (e == null) {
	// e = new Complex(0);
	// c.set(e, row, column);
	// }
	// e.multiply(a.get(row, column), b.get(row, column));
	// }
	// }
	//
	// } else {
	// throw new SizeDisagreeException(a.getSize(), b.getSize());
	// }
	//
	// return c;
	// }
	//
	// /**
	// * Product matrix a and b, put the result into matrix c. If matrix c
	// refers
	// * to a (or b) then a (or b) will be cloned.
	// *
	// * @param a
	// * @param b
	// * @param c
	// * @return Matrix c.
	// */
	// public static final Matrix<Complex> Product(Matrix<Complex> a,
	// Matrix<Complex> b,
	// Matrix<Complex> c)
	// {
	// if (a.isCell() || b.isCell()) {
	//
	// Multiply(a, b, c);
	//
	// } else if (a.getColumns() == b.getRows()) {
	//
	// if (a == c) {
	// a = a.clone();
	// }
	// if (b == c) {
	// b = b.clone();
	// }
	//
	// Matrix.VerifyLimits(c, a.getRows(), b.getColumns());
	//
	// int indices = a.getColumns();
	//
	// Complex sum = null;
	// Complex mul = new Complex(0);
	// for (int row = Position.FIRST; row < c.getRows(); row++) {
	// for (int column = Position.FIRST; column < c.getColumns(); column++) {
	// sum = new Complex(0);
	// for (int index = Position.FIRST; index < indices; index++) {
	// sum.add(mul.multiply(a.get(row, index), b.get(index, column)));
	// }
	// c.set(sum, row, column);
	// }
	// }
	//
	// } else {
	// throw new SizeDisagreeException(a.getSize(), b.getSize());
	// }
	//
	// return c;
	// }

	public ComplexMatrix()
	{
		super();
	}

	/**
	 * Clone
	 */
	public ComplexMatrix(Cell<Complex>[][] matrix)
	{
		this(matrix, null);
	}

	/**
	 * Clone
	 */
	public ComplexMatrix(Cell<Complex>[][] matrix, Range range)
	{
		super(matrix, range);
	}

	/**
	 * Clone
	 */
	public ComplexMatrix(Complex[][] matrix)
	{
		this(matrix, null);
	}

	/**
	 * Clone
	 */
	public ComplexMatrix(Complex[][] matrix, Range range)
	{
		super(matrix, range);
	}

	public ComplexMatrix(int rows, int columns)
	{
		super(rows, columns);
	}

	/**
	 * Quote
	 */
	public ComplexMatrix(Matrix<Complex> matrix)
	{
		this(matrix, null);
	}

	/**
	 * Quote
	 */
	public ComplexMatrix(Matrix<Complex> matrix, Range range)
	{
		super(matrix, range);
	}

	public ComplexMatrix(Sized size)
	{
		super(size);
	}

	// @Override
	// public ComplexMatrix add(Matrix<Complex> matrix)
	// {
	// return add(this, matrix);
	// }
	//
	// @Override
	// public ComplexMatrix add(Matrix<Complex> a, Matrix<Complex> b)
	// {
	// return (ComplexMatrix) Add(a, b, this);
	// }

	@Override
	public ComplexMatrix clone()
	{
		return this.clone(Range.Full(this));
	}

	@Override
	protected Complex cloneElement(Complex element)
	{
		return element == null ? element : element.clone();
	}

	@Override
	protected ComplexMatrix createClone(Matrix<Complex> matrix, Range range)
	{
		return new ComplexMatrix(matrix.getMatrix(), range);
	}

	@Override
	protected ComplexMatrix createMatrix(Sized size)
	{
		return new ComplexMatrix(size);
	}

	@Override
	protected ComplexMatrix createQuote(Matrix<Complex> matrix, Range range)
	{
		return new ComplexMatrix(matrix, range);
	}

	// @Override
	// public ComplexMatrix divide(Complex number)
	// {
	// return divide(this, number);
	// }
	//
	// @Override
	// public ComplexMatrix divide(Matrix<Complex> matrix)
	// {
	// return divide(this, matrix);
	// }
	//
	// @Override
	// public ComplexMatrix divide(Matrix<Complex> matrix, Complex number)
	// {
	// return (ComplexMatrix) Divide(matrix, number, this);
	// }
	//
	// @Override
	// public ComplexMatrix divide(Matrix<Complex> a, Matrix<Complex> b)
	// {
	// return (ComplexMatrix) Divide(a, b, this);
	// }
	//
	// @Override
	// public ComplexMatrix minus(Matrix<Complex> matrix)
	// {
	// return minus(this, matrix);
	// }
	//
	// @Override
	// public ComplexMatrix minus(Matrix<Complex> a, Matrix<Complex> b)
	// {
	// return (ComplexMatrix) Minus(a, b, this);
	// }
	//
	// @Override
	// public ComplexMatrix multiply(Complex number)
	// {
	// return multiply(this, number);
	// }
	//
	// @Override
	// public ComplexMatrix multiply(Matrix<Complex> matrix)
	// {
	// return multiply(this, matrix);
	// }
	//
	// @Override
	// public ComplexMatrix multiply(Matrix<Complex> matrix, Complex number)
	// {
	// return (ComplexMatrix) Multiply(matrix, number, this);
	// }
	//
	// @Override
	// public ComplexMatrix multiply(Matrix<Complex> a, Matrix<Complex> b)
	// {
	// return (ComplexMatrix) Multiply(a, b, this);
	// }
	//
	// @Override
	// public ComplexMatrix product(Matrix<Complex> matrix)
	// {
	// return product(this, matrix);
	// }
	//
	// @Override
	// public ComplexMatrix product(Matrix<Complex> a, Matrix<Complex> b)
	// {
	// return (ComplexMatrix) Product(a, b, this);
	// }

}

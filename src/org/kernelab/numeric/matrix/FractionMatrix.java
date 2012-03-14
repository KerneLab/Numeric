package org.kernelab.numeric.matrix;

import org.kernelab.basis.Tools;
import org.kernelab.numeric.Fraction;
import org.kernelab.numeric.apps.Parser;

public class FractionMatrix extends NumericMatrix<Fraction>
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8706513771443612275L;

	// /**
	// * Add matrix a and b, put the result into matrix c.
	// *
	// * @param a
	// * @param b
	// * @param c
	// * @return Matrix c.
	// */
	// public static final Matrix<Fraction> Add(Matrix<Fraction> a,
	// Matrix<Fraction> b,
	// Matrix<Fraction> c)
	// {
	// if (a.sameSizeOf(b)) {
	//
	// Matrix.VerifyLimits(c, a.getRows(), a.getColumns());
	//
	// for (int row = Position.FIRST; row < c.getRows(); row++) {
	// for (int column = Position.FIRST; column < c.getColumns(); column++) {
	// Fraction f = c.get(row, column);
	// if (f == null) {
	// f = new Fraction(0);
	// c.set(f, row, column);
	// }
	// f.add(a.get(row, column), b.get(row, column));
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
	// * Divide matrix a by Fraction b and put the result into matrix c.
	// *
	// * @param a
	// * @param b
	// * @param c
	// * @return Matrix c.
	// */
	// public static final Matrix<Fraction> Divide(Matrix<Fraction> a, Fraction
	// b,
	// Matrix<Fraction> c)
	// {
	// Matrix.VerifyLimits(c, a.getRows(), a.getColumns());
	//
	// for (int row = Position.FIRST; row < c.getRows(); row++) {
	// for (int column = Position.FIRST; column < c.getColumns(); column++) {
	// Fraction f = c.get(row, column);
	// if (f == null) {
	// f = new Fraction(0);
	// c.set(f, row, column);
	// }
	// f.divide(a.get(row, column), b);
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
	// public static final Matrix<Fraction> Divide(Matrix<Fraction> a,
	// Matrix<Fraction> b,
	// Matrix<Fraction> c)
	// {
	// if (a.sameSizeOf(b)) {
	//
	// Matrix.VerifyLimits(c, a.getRows(), a.getColumns());
	//
	// for (int row = Position.FIRST; row < c.getRows(); row++) {
	// for (int column = Position.FIRST; column < c.getColumns(); column++) {
	// Fraction f = c.get(row, column);
	// if (f == null) {
	// f = new Fraction(0);
	// c.set(f, row, column);
	// }
	// f.divide(a.get(row, column), b.get(row, column));
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

	public static final FractionMatrix InstanceFromDoubleMatrix(Matrix<Double> dMatrix)
	{
		FractionMatrix fMatrix = new FractionMatrix(dMatrix.getSize());

		for (int row = 0; row < dMatrix.getRows(); row++) {

			for (int column = 0; column < dMatrix.getColumns(); column++) {

				fMatrix.set(new Fraction(dMatrix.get(row, column)), row, column);
			}
		}

		return fMatrix;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		FractionMatrix a = FractionMatrix.Parse("[1/2,1/4,1/2;1/5,1/10,1/2]");
		Tools.debug(a);

		FractionMatrix b = FractionMatrix.Parse("[1,2,3;4,5,6]");
		Tools.debug(b);

		Tools.debug(a.minus(b));

		FractionMatrix c = a.clone();

		Tools.debug(c);
	}

	// /**
	// * Matrix a minus b and put the result into matrix c.
	// *
	// * @param a
	// * @param b
	// * @param c
	// * @return Matrix c.
	// */
	// public static final Matrix<Fraction> Minus(Matrix<Fraction> a,
	// Matrix<Fraction> b,
	// Matrix<Fraction> c)
	// {
	// if (a.sameSizeOf(b)) {
	//
	// Matrix.VerifyLimits(c, a.getRows(), a.getColumns());
	//
	// for (int row = Position.FIRST; row < c.getRows(); row++) {
	// for (int column = Position.FIRST; column < c.getColumns(); column++) {
	// Fraction f = c.get(row, column);
	// if (f == null) {
	// f = new Fraction(0);
	// c.set(f, row, column);
	// }
	// f.minus(a.get(row, column), b.get(row, column));
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
	// * Multiply matrix a by Fraction b and put the result into matrix c.
	// *
	// * @param a
	// * @param b
	// * @param c
	// * @return Matrix c.
	// */
	// public static final Matrix<Fraction> Multiply(Matrix<Fraction> a,
	// Fraction b,
	// Matrix<Fraction> c)
	// {
	// Matrix.VerifyLimits(c, a.getRows(), a.getColumns());
	//
	// for (int row = Position.FIRST; row < c.getRows(); row++) {
	// for (int column = Position.FIRST; column < c.getColumns(); column++) {
	// Fraction f = c.get(row, column);
	// if (f == null) {
	// f = new Fraction(0);
	// c.set(f, row, column);
	// }
	// f.multiply(a.get(row, column), b);
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
	// public static final Matrix<Fraction> Multiply(Matrix<Fraction> a,
	// Matrix<Fraction> b,
	// Matrix<Fraction> c)
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
	// Fraction f = c.get(row, column);
	// if (f == null) {
	// f = new Fraction(0);
	// c.set(f, row, column);
	// }
	// f.multiply(a.get(row, column), b.get(row, column));
	// }
	// }
	//
	// } else {
	// throw new SizeDisagreeException(a.getSize(), b.getSize());
	// }
	//
	// return c;
	// }

	public static final FractionMatrix Parse(String exp)
	{
		FractionMatrix matrix = null;

		exp = exp.replaceAll("\\[", "");
		exp = exp.replaceAll("\\]", "");

		String[] rows = exp.split(Parser.ROW_SPLIT);
		String[] columns = rows[Position.FIRST].split(Parser.COLUMN_SPLIT);

		matrix = new FractionMatrix(new Size(rows.length, columns.length));

		for (int row = Position.FIRST; row < rows.length; row++) {
			columns = rows[row].split(Parser.COLUMN_SPLIT);
			for (int column = Position.FIRST; column < columns.length; column++) {
				matrix.set(Fraction.valueOf(columns[column]), row, column);
			}
		}

		return matrix;
	}

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
	// public static final Matrix<Fraction> Product(Matrix<Fraction> a,
	// Matrix<Fraction> b,
	// Matrix<Fraction> c)
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
	// Fraction sum = null;
	// Fraction mul = new Fraction(0);
	// for (int row = Position.FIRST; row < c.getRows(); row++) {
	// for (int column = Position.FIRST; column < c.getColumns(); column++) {
	// sum = new Fraction(0);
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

	public FractionMatrix()
	{
		super();
	}

	/**
	 * Clone
	 */
	public FractionMatrix(Cell<Fraction>[][] matrix)
	{
		this(matrix, null);
	}

	/**
	 * Clone
	 */
	public FractionMatrix(Cell<Fraction>[][] matrix, Range range)
	{
		super(matrix, range);
	}

	/**
	 * Clone
	 */
	public FractionMatrix(Fraction[][] matrix)
	{
		this(matrix, null);
	}

	/**
	 * Clone
	 */
	public FractionMatrix(Fraction[][] matrix, Range range)
	{
		super(matrix, range);
	}

	public FractionMatrix(int rows, int columns)
	{
		super(rows, columns);
	}

	/**
	 * Quote
	 */
	public FractionMatrix(Matrix<Fraction> matrix)
	{
		this(matrix, null);
	}

	/**
	 * Quote
	 */
	public FractionMatrix(Matrix<Fraction> matrix, Range range)
	{
		super(matrix, range);
	}

	public FractionMatrix(Sized size)
	{
		super(size);
	}

	//
	// @Override
	// public FractionMatrix add(Matrix<Fraction> matrix)
	// {
	// return add(this, matrix);
	// }
	//
	// @Override
	// public FractionMatrix add(Matrix<Fraction> a, Matrix<Fraction> b)
	// {
	// return (FractionMatrix) Add(a, b, this);
	// }

	@Override
	public FractionMatrix clone()
	{
		return this.clone(Range.Full(this));
	}

	@Override
	protected Fraction cloneElement(Fraction element)
	{
		return element == null ? element : element.clone();
	}

	@Override
	protected FractionMatrix createClone(Matrix<Fraction> matrix, Range range)
	{
		return new FractionMatrix(matrix.getMatrix(), range);
	}

	@Override
	protected FractionMatrix createMatrix(Sized size)
	{
		return new FractionMatrix(size);
	}

	@Override
	protected FractionMatrix createQuote(Matrix<Fraction> matrix, Range range)
	{
		return new FractionMatrix(matrix, range);
	}

	// @Override
	// public FractionMatrix divide(Fraction number)
	// {
	// return divide(this, number);
	// }
	//
	// @Override
	// public FractionMatrix divide(Matrix<Fraction> matrix)
	// {
	// return divide(this, matrix);
	// }
	//
	// @Override
	// public FractionMatrix divide(Matrix<Fraction> matrix, Fraction number)
	// {
	// return (FractionMatrix) Divide(matrix, number, this);
	// }
	//
	// @Override
	// public FractionMatrix divide(Matrix<Fraction> a, Matrix<Fraction> b)
	// {
	// return (FractionMatrix) Divide(a, b, this);
	// }
	//
	// @Override
	// public FractionMatrix minus(Matrix<Fraction> matrix)
	// {
	// return minus(this, matrix);
	// }
	//
	// @Override
	// public FractionMatrix minus(Matrix<Fraction> a, Matrix<Fraction> b)
	// {
	// return (FractionMatrix) Minus(a, b, this);
	// }
	//
	// @Override
	// public FractionMatrix multiply(Fraction number)
	// {
	// return multiply(this, number);
	// }
	//
	// @Override
	// public FractionMatrix multiply(Matrix<Fraction> matrix)
	// {
	// return multiply(this, matrix);
	// }
	//
	// @Override
	// public FractionMatrix multiply(Matrix<Fraction> matrix, Fraction number)
	// {
	// return (FractionMatrix) Multiply(matrix, number, this);
	// }
	//
	// @Override
	// public FractionMatrix multiply(Matrix<Fraction> a, Matrix<Fraction> b)
	// {
	// return (FractionMatrix) Multiply(a, b, this);
	// }
	//
	// @Override
	// public FractionMatrix product(Matrix<Fraction> matrix)
	// {
	// return product(this, matrix);
	// }
	//
	// @Override
	// public FractionMatrix product(Matrix<Fraction> a, Matrix<Fraction> b)
	// {
	// return (FractionMatrix) Product(a, b, this);
	// }
}

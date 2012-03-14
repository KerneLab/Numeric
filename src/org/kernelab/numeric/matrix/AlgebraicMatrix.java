package org.kernelab.numeric.matrix;

public interface AlgebraicMatrix<E>
{
	public Matrix<E> add(Matrix<E> matrix);

	public Matrix<E> add(Matrix<E> a, Matrix<E> b);

	public Matrix<E> divide(E number);

	public Matrix<E> divide(Matrix<E> matrix);

	public Matrix<E> divide(Matrix<E> matrix, E number);

	public Matrix<E> divide(Matrix<E> a, Matrix<E> b);

	public Matrix<E> minus(Matrix<E> matrix);

	public Matrix<E> minus(Matrix<E> a, Matrix<E> b);

	public Matrix<E> multiply(E number);

	public Matrix<E> multiply(Matrix<E> matrix);

	public Matrix<E> multiply(Matrix<E> matrix, E number);

	public Matrix<E> multiply(Matrix<E> a, Matrix<E> b);

	public Matrix<E> product(Matrix<E> matrix);

	public Matrix<E> product(Matrix<E> a, Matrix<E> b);
}

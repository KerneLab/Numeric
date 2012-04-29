package org.kernelab.numeric.matrix;

/**
 * The interface which listen the element in a Matrix.
 * 
 * @author Dilly King
 * 
 * @param <E>
 */
public interface MatrixElementListener<E>
{

	/**
	 * Called when the element in the table is changed.
	 * 
	 * @param source
	 *            The source Matrix which modified the element. The source
	 *            Matrix might not be the same of this Matrix because this
	 *            Matrix quoted the element from the source Matrix.
	 * @param srcRow
	 *            The row index of change in the source Matrix.
	 * @param srcCol
	 *            The column index of change in the source Matrix.
	 * @param old
	 *            The old value of the element.
	 * @param matrix
	 *            The Matrix which holds the element.
	 * @param row
	 *            The row index of the element in the Matrix.
	 * @param column
	 *            The column index of the element in the Matrix.
	 * @param now
	 *            The new value of the element.
	 * @return true if stop calling any other Listener focus on this Position in
	 *         this Matrix.
	 */
	public boolean changed(Matrix<E> source, int srcRow, int srcCol, E old,
			Matrix<E> matrix, int row, int column, E now);
}

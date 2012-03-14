package org.kernelab.numeric.matrix;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import org.kernelab.basis.Copieable;
import org.kernelab.basis.Tools;
import org.kernelab.numeric.Tabulation;

/**
 * The Matrix.
 * 
 * @author Dilly King
 * @version 2011.10.29.2
 * @param <E>
 *            the generic type of element in the matrix.
 */
public class Matrix<E> implements Tabulation<E>, Sized, Iterable<E>,
		Copieable<Matrix<E>>, Serializable
{

	/**
	 * The Iterator of the Cells in Matrix.
	 * 
	 * @author Dilly King
	 * 
	 */
	private class MatrixCellIterator implements Iterator<Cell<E>>, Iterable<Cell<E>>
	{
		private int		priority;

		private int		row;

		private int		column;

		private Cell<E>	cell;

		public MatrixCellIterator(int priority)
		{
			this.priority = priority;
			row = column = Position.FIRST;
		}

		public boolean hasNext()
		{
			return hasPosition(row, column);
		}

		public Iterator<Cell<E>> iterator()
		{
			return this;
		}

		public Cell<E> next()
		{
			cell = matrix[row][column];

			switch (priority)
			{
				case Matrix.ROW_PRIORITY:
					column++;
					if (column == getColumns()) {
						column = Position.FIRST;
						row++;
					}
					break;

				case Matrix.COLUMN_PRIORITY:
					row++;
					if (row == getRows()) {
						row = Position.FIRST;
						column++;
					}
					break;
			}

			return cell;
		}

		public void remove()
		{
			matrix[row][column] = null;
		}
	}

	/**
	 * The Iterator for Matrix.
	 * 
	 * @author Dilly King
	 * 
	 */
	private class MatrixIterator implements Iterator<E>
	{
		private int		row;

		private int		column;

		private Cell<E>	cell;

		public MatrixIterator()
		{
			row = column = Position.FIRST;
		}

		public boolean hasNext()
		{
			return hasPosition(row, column);
		}

		public E next()
		{
			cell = matrix[row][column];

			switch (getPriority())
			{
				case Matrix.ROW_PRIORITY:
					column++;
					if (column == getColumns()) {
						column = Position.FIRST;
						row++;
					}
					break;

				case Matrix.COLUMN_PRIORITY:
					row++;
					if (row == getRows()) {
						row = Position.FIRST;
						column++;
					}
					break;
			}

			return cell.getElement();
		}

		public void remove()
		{
			cell.setElement(null);
		}
	}

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8175048497857273571L;

	public static final int		ROW_PRIORITY		= 0;

	public static final int		COLUMN_PRIORITY		= 1;

	/**
	 * To set the source elements in E[][] array within its given Range into the
	 * target Matrix begin at a certain offset Position.<br />
	 * Attention that some offset Position would make the assign result exceed
	 * the Size of the target Matrix. In this case, target Matrix would be
	 * expanded to accomplish the assign operation, meanwhile, some elements
	 * would be null unavoidably in the expanded target Matrix.
	 * 
	 * @param <E>
	 *            The generic type of the elements in the Matrix.
	 * @param <T>
	 *            The generic type of the matrix extends from Matrix<E>.
	 * @param source
	 *            The source E[][] array from which to assign the elements.
	 * @param range
	 *            The Range which indicates from where to where the elements in
	 *            the source array should be assigned. If null, then
	 *            Range.Full(matrix) would be taken.
	 * @param target
	 *            The target Matrix which holds the result of assign operation.
	 * @param offset
	 *            The offset Position that tells from where to fill the assigned
	 *            element into target Matrix. If null, then
	 *            Position.FirstPosition() would be used instead.
	 * @return The target Matrix.
	 */
	public static final <E, T extends Matrix<E>> T Assign(E[][] source, Range range,
			T target, Position offset)
	{
		if (range == null) {
			range = Range.Full(source);
		}

		if (offset == null) {
			offset = Position.FirstPosition();
		}

		int rows = range.getRows() + offset.getRow();
		int columns = range.getColumns() + offset.getColumn();

		if (rows > target.getRows() || columns > target.getColumns()) {
			Matrix.VerifyLimits(target, rows, columns);
		}

		Position from = range.getBegin();

		for (int row = Position.FIRST; row < range.getRows(); row++) {
			for (int column = Position.FIRST; column < range.getColumns(); column++) {
				target.set(source[row + from.row][column + from.column],
						row + offset.row, column + offset.column);
			}
		}

		return target;
	}

	/**
	 * To write the source Matrix's elements within its given Range into the
	 * target E[][] array begin at a certain offset Position.<br />
	 * Attention that some offset Position would make the assign result exceed
	 * the Size of the target E[][] array. In this case, a new Object[][] array
	 * would be set to the target array to accomplish the assign operation,
	 * meanwhile, some elements would be null unavoidably in the expanded target
	 * E[][] array.
	 * 
	 * @param <E>
	 *            The generic type of the elements in the Matrix.
	 * @param <T>
	 *            The generic type of the matrix extends from Matrix<E>.
	 * @param source
	 *            The source Matrix from which to assign the elements.
	 * @param range
	 *            The Range which indicates from where to where the elements in
	 *            the source matrix should be assigned. If null, then
	 *            Range.Full(matrix) would be taken.
	 * @param target
	 *            The target E[][] array which holds the result of assign
	 *            operation.
	 * @param offset
	 *            The offset Position that tells from where to fill the assigned
	 *            element into target array. If null, then
	 *            Position.FirstPosition() would be used instead.
	 * @return The target E[][] array.
	 */
	@SuppressWarnings("unchecked")
	public static final <E, T extends Matrix<E>> E[][] Assign(T source, Range range,
			E[][] target, Position offset)
	{
		if (range == null) {
			range = Range.Full(source);
		}

		if (offset == null) {
			offset = Position.FirstPosition();
		}

		int rows = range.getRows() + offset.getRow();
		int columns = range.getColumns() + offset.getColumn();

		if (target == null || rows > Matrix.RowsOfArray(target)
				|| columns > Matrix.ColumnsOfArray(target))
		{
			target = (E[][]) new Object[rows][columns];
		}

		Position from = range.getBegin();

		for (int row = Position.FIRST; row < range.getRows(); row++) {
			for (int column = Position.FIRST; column < range.getColumns(); column++) {
				target[row + offset.row][column + offset.column] = source.get(row
						+ from.row, column + from.column);
			}
		}

		return target;
	}

	/**
	 * To write the source Matrix's elements within its given Range into the
	 * target Matrix begin at a certain offset Position.<br />
	 * Attention that some offset Position would make the clone result exceed
	 * the Size of the target Matrix. In this case, target Matrix would be
	 * expanded to accomplish the assign operation, meanwhile, some elements
	 * would be null unavoidably in the expanded target Matrix.
	 * 
	 * @param <E>
	 *            The generic type of the elements in the Matrix.
	 * @param <T>
	 *            The generic type of the matrix extends from Matrix<E>.
	 * @param source
	 *            The source Matrix from which to assign the elements.
	 * @param range
	 *            The Range which indicates from where to where the elements in
	 *            the source matrix should be taken and assign into the target
	 *            matrix. If null, then Range.Full(matrix) would be taken.
	 * @param target
	 *            The target Matrix which holds the result of assign operation.
	 * @param offset
	 *            The offset Position that tells from where to fill the assigned
	 *            element into target Matrix. If null, then
	 *            Position.FirstPosition() would be used instead.
	 * @return The target Matrix.
	 */
	public static final <E, T extends Matrix<E>> T Assign(T source, Range range,
			T target, Position offset)
	{
		if (range == null) {
			range = Range.Full(source);
		}

		if (offset == null) {
			offset = Position.FirstPosition();
		}

		int rows = range.getRows() + offset.getRow();
		int columns = range.getColumns() + offset.getColumn();

		if (rows > target.getRows() || columns > target.getColumns()) {
			Matrix.VerifyLimits(target, rows, columns);
		}

		Position from = range.getBegin();

		for (int row = Position.FIRST; row < range.getRows(); row++) {
			for (int column = Position.FIRST; column < range.getColumns(); column++) {
				target.set(source.get(row + from.row, column + from.column), row
						+ offset.row, column + offset.column);
			}
		}

		return target;
	}

	@SuppressWarnings("unchecked")
	public static final <E, T extends Matrix<E>> T Clone(T matrix, Range range)
	{
		return (T) matrix.createClone(matrix, range);
	}

	/**
	 * To clone the source Matrix's elements within its given Range into the
	 * target Matrix begin at a certain offset Position.<br />
	 * Attention that some offset Position would make the clone result exceed
	 * the Size of the target Matrix. In this case, target Matrix would be
	 * expanded to accomplish the clone operation, meanwhile, some elements
	 * would be null unavoidably in the expanded target Matrix.
	 * 
	 * @param <E>
	 *            The generic type of the elements in the Matrix.
	 * @param <T>
	 *            The generic type of the matrix extends from Matrix<E>.
	 * @param source
	 *            The source Matrix from which to clone the elements.
	 * @param range
	 *            The Range which indicates from where to where the elements in
	 *            the source matrix should be cloned. If null, then
	 *            Range.Full(matrix) would be taken.
	 * @param target
	 *            The target Matrix which holds the result of clone operation.
	 * @param offset
	 *            The offset Position that tells from where to fill the cloned
	 *            element into target Matrix. If null, then
	 *            Position.FirstPosition() would be used instead.
	 * @return The target Matrix.
	 */
	public static final <E, T extends Matrix<E>> T Clone(T source, Range range, T target,
			Position offset)
	{
		if (range == null) {
			range = Range.Full(source);
		}

		if (offset == null) {
			offset = Position.FirstPosition();
		}

		int rows = range.getRows() + offset.getRow();
		int columns = range.getColumns() + offset.getColumn();

		if (rows > target.getRows() || columns > target.getColumns()) {
			Matrix.VerifyLimits(target, rows, columns);
		}

		Position from = range.getBegin();

		for (int row = Position.FIRST; row < range.getRows(); row++) {
			for (int column = Position.FIRST; column < range.getColumns(); column++) {
				target.setCell(target.createCell(target.cloneElement(source.get(row
						+ from.row, column + from.column))), row + offset.row, column
						+ offset.column);
			}
		}

		return target;
	}

	public static final <E, T extends Matrix<E>> T CloneColumn(T matrix, int column)
	{
		return Matrix.Clone(matrix, Range.Column(matrix, column));
	}

	public static final <E, T extends Matrix<E>> T CloneColumns(T matrix, int begin,
			int end)
	{
		return Matrix.Clone(matrix, Range.Columns(matrix, begin, end));
	}

	public static final <E, T extends Matrix<E>> T CloneRow(T matrix, int row)
	{
		return Matrix.Clone(matrix, Range.Row(matrix, row));
	}

	public static final <E, T extends Matrix<E>> T CloneRows(T matrix, int begin, int end)
	{
		return Matrix.Clone(matrix, Range.Rows(matrix, begin, end));
	}

	public static final <E, T extends Matrix<E>> T ColumnOfCollection(Collection<E> c, T m)
	{
		Matrix.VerifyLimits(m, c.size(), 1);

		int i = Position.FIRST;
		for (E e : c) {
			m.set(e, i, Position.FIRST);
			i++;
		}

		return m;
	}

	public static final <E> int ColumnsOfArray(E[][] array)
	{
		return array[Position.FIRST].length;
	}

	@SuppressWarnings("unchecked")
	public static <E> Cell<E>[][] CreateCellsArray(int... dimensions)
	{
		return (Cell<E>[][]) java.lang.reflect.Array.newInstance(Cell.class, dimensions);
	}

	/**
	 * Expand the size of a matrix. If the given rows (or columns) parameter is
	 * larger than the matrix current limit, then the new limit will be 1.5 x
	 * current limit. If still not so large as the given limit, then the limit
	 * will be set directly to the given one. During the expansion, the data
	 * will not be changed at all. The elements in the NEW cells are
	 * {@code null}.
	 * 
	 * @param <E>
	 *            The generic type of the elements in the matrix.
	 * @param matrix
	 *            The matrix.
	 * @param rows
	 *            The new rows limit.
	 * @param columns
	 *            The new columns limit.
	 */
	public static final <E> void ExpandLimits(Matrix<E> matrix, int rows, int columns)
	{
		int rowsLimit = matrix.getRowsLimit();
		int columnsLimit = matrix.getColumnsLimit();

		if (rows > rowsLimit || columns > columnsLimit) {

			if (rows > rowsLimit) {
				rowsLimit = rowsLimit * 3 / 2 + 1;
				if (rows > rowsLimit) {
					rowsLimit = rows;
				}
			}

			if (columns > columnsLimit) {
				columnsLimit = columnsLimit * 3 / 2 + 1;
				if (columns > columnsLimit) {
					columnsLimit = columns;
				}
			}

			Cell<E>[][] cells = matrix.matrix;

			matrix.matrix = matrix.createCellsArray(rowsLimit, columnsLimit);

			rows = matrix.getRows();
			columns = matrix.getColumns();

			for (int row = Position.FIRST; row < rowsLimit; row++) {
				for (int column = Position.FIRST; column < columnsLimit; column++) {
					if (row < rows && column < columns) {
						matrix.getMatrix()[row][column] = cells[row][column];
					} else {
						matrix.setCell(matrix.createCell(null), row, column);
					}
				}
			}
		}
	}

	/**
	 * Insert some columns in a matrix into another matrix. Attention that the
	 * default insert mode is "Quote", to insert in "Clone", just call this
	 * method with the cloned matrix insert.
	 * 
	 * @param <E>
	 * @param matrix
	 *            The object matrix to be inserted.
	 * @param insert
	 *            The columns which will be inserted into matrix. Attention that
	 *            the rows number should be the same as matrix.
	 * @param index
	 *            From where to insert the columns.
	 * @return matrix.
	 */
	@SuppressWarnings("unchecked")
	public static final <E, T extends Matrix<E>> T InsertColumnsToMatrix(
			Matrix<E> matrix, Matrix<E> insert, int index)
	{
		int rows = matrix.getRows();

		if (rows != insert.getRows()) {

			throw new SizeDisagreeException(matrix.getSize(), insert.getSize());

		} else {

			int matrixColumns = matrix.getColumns();

			if (index < 0 || index > matrixColumns) {

				throw new IndexOutOfBoundsException("Insert Index " + index + " beyond "
						+ matrixColumns + " Columns of The Matrix.");

			} else {

				int insertColumns = insert.getColumns();

				Matrix.VerifyLimits(matrix, rows, matrixColumns + insertColumns);

				// Move old columns after index
				for (int column = matrixColumns - 1; column >= index; column--) {
					for (int row = Position.FIRST; row < rows; row++) {

						Cell<E> cell = matrix.getCell(row, column);

						matrix.setCell(cell, row, column + insertColumns);
					}
				}

				// Copy new columns to index
				for (int column = Position.FIRST; column < insertColumns; column++) {
					for (int row = Position.FIRST; row < rows; row++) {

						matrix.setCell(insert.getCell(row, column), row, column + index);
					}
				}
			}
		}

		return (T) matrix;
	}

	/**
	 * Insert some rows in a matrix into another matrix. Attention that the
	 * default insert mode is "Quote", to insert in "Clone", just call this
	 * method with the cloned matrix insert.
	 * 
	 * @param <E>
	 * @param matrix
	 *            The object matrix to be inserted.
	 * @param insert
	 *            The rows which will be inserted into matrix. Attention that
	 *            the columns number should be the same as matrix.
	 * @param index
	 *            From where to insert the columns.
	 * @return matrix.
	 */
	@SuppressWarnings("unchecked")
	public static final <E, T extends Matrix<E>> T InsertRowsToMatrix(Matrix<E> matrix,
			Matrix<E> insert, int index)
	{
		int columns = matrix.getColumns();

		if (columns != insert.getColumns()) {

			throw new SizeDisagreeException(matrix.getSize(), insert.getSize());

		} else {

			int matrixRows = matrix.getRows();

			if (index < 0 || index > matrixRows) {

				throw new IndexOutOfBoundsException("Insert Index " + index + " beyond "
						+ matrixRows + " Rows of The Matrix.");

			} else {

				int insertRows = insert.getRows();

				Matrix.VerifyLimits(matrix, matrixRows + insertRows, columns);

				// Copy old rows before index
				// Which is implicitly done by ExpandMatrixLimit
				// for (int row = Position.REAL_FIRST; row < index; row++) {
				// for (int column = Position.REAL_FIRST; column < columns;
				// column++) {
				//
				// matrix.matrix[row][column] = matrix.matrix[row][column];
				// }
				// }

				// Move old rows after index
				for (int row = matrixRows - 1; row >= index; row--) {
					for (int column = Position.FIRST; column < columns; column++) {

						Cell<E> cell = matrix.getCell(row, column);

						matrix.setCell(cell, row + insertRows, column);
					}
				}

				// Copy new rows to index
				for (int row = Position.FIRST; row < insertRows; row++) {
					for (int column = Position.FIRST; column < columns; column++) {

						matrix.setCell(insert.getCell(row, column), row + index, column);
					}
				}
			}
		}

		return (T) matrix;
	}

	/**
	 * Join the columns in matrix b after the columns in matrix a, put the joint
	 * into matrix c. That means matrix a and b should have the same columns
	 * number. Attention that the default join mode is "Quote", to join in
	 * "Clone", just call this method with the cloned matrix a and/or b.
	 * 
	 * @param <E>
	 * @param a
	 * @param b
	 * @param c
	 * @return Matrix c.
	 */
	public static final <E, T extends Matrix<E>> T JoinColumns(T a, T b, T c)
	{
		if (a.getColumns() != b.getColumns()) {
			throw new SizeDisagreeException(a.getSize(), b.getSize());
		}

		int offset = a.getRows();

		Matrix.VerifyLimits(c, a.getRows() + b.getRows(), a.getColumns());

		for (int column = Position.FIRST; column < c.getColumns(); column++) {

			if (b != c) {

				for (int row = Position.FIRST; row < a.getRows(); row++) {
					c.setCell(a.getCell(row, column), row, column);
				}

				for (int row = Position.FIRST; row < b.getRows(); row++) {
					c.setCell(b.getCell(row, column), row + offset, column);
				}
			} else if (a != c) {

				for (int row = Position.FIRST; row < b.getRows(); row++) {
					c.setCell(b.getCell(row, column), row + offset, column);
				}
				for (int row = Position.FIRST; row < a.getRows(); row++) {
					c.setCell(a.getCell(row, column), row, column);
				}
			}
		}
		return c;
	}

	/**
	 * Join the rows in matrix b after the rows in matrix a, put the joint into
	 * matrix c. That means matrix a and b should have the same rows number.
	 * Attention that the default join mode is "Quote", to join in "Clone", just
	 * call this method with the cloned matrix a and/or b.
	 * 
	 * @param <E>
	 * @param a
	 * @param b
	 * @param c
	 * @return Matrix c.
	 */
	public static final <E, T extends Matrix<E>> T JoinRows(T a, T b, T c)
	{
		if (a.getRows() != b.getRows()) {
			throw new SizeDisagreeException(a.getSize(), b.getSize());
		}

		int offset = a.getColumns();

		Matrix.VerifyLimits(c, a.getRows(), a.getColumns() + b.getColumns());

		for (int row = Position.FIRST; row < c.getRows(); row++) {

			if (b != c) {

				for (int column = Position.FIRST; column < a.getColumns(); column++) {
					c.setCell(a.getCell(row, column), row, column);
				}

				for (int column = Position.FIRST; column < b.getColumns(); column++) {
					c.setCell(b.getCell(row, column), row, column + offset);
				}
			} else if (a != c) {

				for (int column = Position.FIRST; column < b.getColumns(); column++) {
					c.setCell(b.getCell(row, column), row, column + offset);
				}

				for (int column = Position.FIRST; column < a.getColumns(); column++) {
					c.setCell(a.getCell(row, column), row, column);
				}
			}
		}

		return c;
	}

	public static final <E> List<E> ListOfColumn(Matrix<E> matrix, int column,
			List<E> list)
	{
		for (int row = 0; row < matrix.getRows(); row++) {
			list.add(matrix.get(row, column));
		}

		return list;
	}

	public static final <E> List<E> ListOfRow(Matrix<E> matrix, int row, List<E> list)
	{
		for (int column = 0; column < matrix.getColumns(); column++) {
			list.add(matrix.get(row, column));
		}

		return list;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Double[][] am = new Double[][] { { 1.0, 2.0, 3.0, 7.3 }, { 2.1, 8.7, 5.5, 3.6 },
				{ 6.5, 1.6, 4.7, 8.4 } };

		Double[][] bm = new Double[][] { { 5.0, 6.0, 7.0, 8.3 },
				{ 11.1, 12.7, 13.5, 14.6 } };

		Matrix<Double> m = new Matrix<Double>(am);

		m.addElementListener(new MatrixElementListener<Double>() {

			public boolean changed(Matrix<Double> source, int srcRow, int srcCol,
					Double old, Matrix<Double> matrix, int row, int column, Double now)
			{
				if (source != matrix) {
					Tools.debug("矩阵m的" + row + "行" + column + "列, 来自n的 " + srcRow + "行"
							+ srcCol + "列");
				}
				return false;
			}

		});

		Tools.debug("m");
		Tools.debug(m.toString());

		Matrix<Double> n = new Matrix<Double>(bm);

		n.addElementListener(new MatrixElementListener<Double>() {

			public boolean changed(Matrix<Double> source, int srcRow, int srcCol,
					Double old, Matrix<Double> matrix, int row, int column, Double now)
			{
				if (source != matrix) {
					Tools.debug("矩阵n的" + row + "行" + column + "列");
				}
				return false;
			}

		});

		Tools.debug("n");
		Tools.debug(n.toString());

		m.joinColumns(n);

		Tools.debug("m 与 n 拼接");
		Tools.debug(m.toString());

		Tools.debug("更改 n 第0行 1列");
		n.put(1.2, 0, 1);

		Tools.debug("n");
		Tools.debug(n.toString());

		Tools.debug("m");
		Tools.debug(m.toString());

		m.put(n);
		Tools.debug(m.toString());

		// for (int row = 0; row < m.getRows(); row++) {
		// for (int col = 0; col < m.getColumns(); col++) {
		// Tools.mark(m.getCell(row, col).getMatrip().size());
		// Tools.mark("\t");
		// }
		// Tools.mark("\n");
		// }
	}

	@SuppressWarnings("unchecked")
	public static final <E> E[][] MatrixToArray(Matrix<E> matrix)
	{
		E[][] array = (E[][]) java.lang.reflect.Array.newInstance(
				matrix.get().getClass(),
				new int[] { matrix.getRows(), matrix.getColumns() });

		for (int row = Position.FIRST; row < matrix.getRows(); row++) {
			for (int column = Position.FIRST; column < matrix.getColumns(); column++) {
				array[row][column] = matrix.get(row, column);
			}
		}

		return array;
	}

	/**
	 * To put the source elements in E[][] array within its given Range into the
	 * target Matrix begin at a certain offset Position.<br />
	 * Attention that some offset Position would make the assign result exceed
	 * the Size of the target Matrix. In this case, target Matrix would be
	 * expanded to accomplish the assign operation, meanwhile, some elements
	 * would be null unavoidably in the expanded target Matrix.
	 * 
	 * @param <E>
	 *            The generic type of the elements in the Matrix.
	 * @param <T>
	 *            The generic type of the matrix extends from Matrix<E>.
	 * @param source
	 *            The source E[][] array from which to assign the elements.
	 * @param range
	 *            The Range which indicates from where to where the elements in
	 *            the source array should be assigned. If null, then
	 *            Range.Full(matrix) would be taken.
	 * @param target
	 *            The target Matrix which holds the result of assign operation.
	 * @param offset
	 *            The offset Position that tells from where to fill the assigned
	 *            element into target Matrix. If null, then
	 *            Position.FirstPosition() would be used instead.
	 * @return The target Matrix.
	 */
	public static final <E, T extends Matrix<E>> T Place(E[][] source, Range range,
			T target, Position offset)
	{
		if (range == null) {
			range = Range.Full(source);
		}

		if (offset == null) {
			offset = Position.FirstPosition();
		}

		int rows = range.getRows() + offset.getRow();
		int columns = range.getColumns() + offset.getColumn();

		if (rows > target.getRows() || columns > target.getColumns()) {
			Matrix.VerifyLimits(target, rows, columns);
		}

		Position from = range.getBegin();

		for (int row = Position.FIRST; row < range.getRows(); row++) {
			for (int column = Position.FIRST; column < range.getColumns(); column++) {
				target.put(source[row + from.row][column + from.column],
						row + offset.row, column + offset.column);
			}
		}

		return target;
	}

	/**
	 * To put the source Matrix's elements within its given Range into the
	 * target Matrix begin at a certain offset Position.<br />
	 * Attention that some offset Position would make the clone result exceed
	 * the Size of the target Matrix. In this case, target Matrix would be
	 * expanded to accomplish the place operation, meanwhile, some elements
	 * would be null unavoidably in the expanded target Matrix.
	 * 
	 * @param <E>
	 *            The generic type of the elements in the Matrix.
	 * @param <T>
	 *            The generic type of the matrix extends from Matrix<E>.
	 * @param source
	 *            The source Matrix from which to place the elements.
	 * @param range
	 *            The Range which indicates from where to where the elements in
	 *            the source matrix should be taken and place into the target
	 *            matrix. If null, then Range.Full(matrix) would be taken.
	 * @param target
	 *            The target Matrix which holds the result of place operation.
	 * @param offset
	 *            The offset Position that tells from where to fill the placed
	 *            element into target Matrix. If null, then
	 *            Position.FirstPosition() would be used instead.
	 * @return The target Matrix.
	 */
	public static final <E, T extends Matrix<E>> T Place(T source, Range range, T target,
			Position offset)
	{
		if (range == null) {
			range = Range.Full(source);
		}

		if (offset == null) {
			offset = Position.FirstPosition();
		}

		int rows = range.getRows() + offset.getRow();
		int columns = range.getColumns() + offset.getColumn();

		if (rows > target.getRows() || columns > target.getColumns()) {
			Matrix.VerifyLimits(target, rows, columns);
		}

		Position from = range.getBegin();

		for (int row = Position.FIRST; row < range.getRows(); row++) {
			for (int column = Position.FIRST; column < range.getColumns(); column++) {
				target.put(source.get(row + from.row, column + from.column), row
						+ offset.row, column + offset.column);
			}
		}

		return target;
	}

	@SuppressWarnings("unchecked")
	public static final <E, T extends Matrix<E>> T Quote(T matrix, Range range)
	{
		return (T) matrix.createQuote(matrix, range);
	}

	/**
	 * To quote the source Matrix's elements within its given Range into the
	 * target Matrix begin at a certain offset Position.<br />
	 * Attention that some offset Position would make the quote result exceed
	 * the Size of the target Matrix. In this case, target Matrix would be
	 * expanded to accomplish the quote operation, meanwhile, some elements
	 * would be null unavoidably in the expanded target Matrix.
	 * 
	 * @param <E>
	 *            The generic type of the elements in the Matrix.
	 * @param <T>
	 *            The generic type of the matrix extends from Matrix<E>.
	 * @param source
	 *            The source Matrix from which to quote the elements.
	 * @param range
	 *            The Range which indicates from where to where the elements in
	 *            the source matrix should be quoted. If null, then
	 *            Range.Full(matrix) would be taken.
	 * @param target
	 *            The target Matrix which holds the result of quote operation.
	 * @param offset
	 *            The offset Position that tells from where to fill the quoted
	 *            element into target Matrix. If null, then
	 *            Position.FirstPosition() would be used instead.
	 * @return The target Matrix.
	 */
	public static final <E, T extends Matrix<E>> T Quote(T source, Range range, T target,
			Position offset)
	{
		if (range == null) {
			range = Range.Full(source);
		}

		if (offset == null) {
			offset = Position.FirstPosition();
		}

		int rows = range.getRows() + offset.getRow();
		int columns = range.getColumns() + offset.getColumn();

		if (rows > target.getRows() || columns > target.getColumns()) {
			Matrix.VerifyLimits(target, rows, columns);
		}

		Position from = range.getBegin();

		for (int row = Position.FIRST; row < range.getRows(); row++) {
			for (int column = Position.FIRST; column < range.getColumns(); column++) {
				// target.matrix[row + offset.row][column + offset.column] =
				// source.matrix[row
				// + from.row][column + from.column];
				target.setCell(source.getCell(row + from.row, column + from.column), row
						+ offset.row, column + offset.column);
			}
		}

		return target;
	}

	public static final <E, T extends Matrix<E>> T QuoteColumn(T matrix, int column)
	{
		return Matrix.Quote(matrix, Range.Column(matrix, column));
	}

	public static final <E, T extends Matrix<E>> T QuoteColumns(T matrix, int begin,
			int end)
	{
		return Matrix.Quote(matrix, Range.Columns(matrix, begin, end));
	}

	public static final <E, T extends Matrix<E>> T QuoteRow(T matrix, int row)
	{
		return Matrix.Quote(matrix, Range.Row(matrix, row));
	}

	public static final <E, T extends Matrix<E>> T QuoteRows(T matrix, int begin, int end)
	{
		return Matrix.Quote(matrix, Range.Rows(matrix, begin, end));
	}

	public static final <E> void RemoveColumnsFromMatrix(Matrix<E> matrix, int from,
			int to)
	{
		if (from > to) {
			from += to;
			to = from - to;
			from -= to;
		}

		int columns = matrix.getColumns();

		int remove = to - from + 1;

		if (remove >= columns || from < Position.FIRST || from >= columns
				|| to < Position.FIRST || to >= columns)
		{
			throw new IndexOutOfBoundsException("Remove Index from " + from + " to " + to
					+ " beyond " + columns + " Columns of The Matrix.");
		} else {

			int rows = matrix.getRows();

			boolean needNull = false;

			for (int column = from; column < columns - remove; column++) {

				if (!needNull && column >= columns - 2 * remove) {
					needNull = true;
				}

				for (int row = Position.FIRST; row < rows; row++) {

					Cell<E> cell = matrix.getCell(row, column + remove);

					matrix.setCell(cell, row, column);

					if (needNull) {
						matrix.setCell(null, row, column + remove);
					}
				}
			}

			matrix.columns -= remove;
		}
	}

	public static final <E> void RemoveRowsFromMatrix(Matrix<E> matrix, int from, int to)
	{
		if (from > to) {
			from += to;
			to = from - to;
			from -= to;
		}

		int rows = matrix.getRows();

		int remove = to - from + 1;

		if (remove >= rows || from < Position.FIRST || from >= rows
				|| to < Position.FIRST || to >= rows)
		{
			throw new IndexOutOfBoundsException("Remove Index from " + from + " to " + to
					+ " beyond " + rows + " Rows of The Matrix.");
		} else {

			int columns = matrix.getColumns();

			boolean needNull = false;

			for (int row = from; row < rows - remove; row++) {

				if (!needNull && row >= rows - 2 * remove) {
					needNull = true;
				}

				for (int column = Position.FIRST; column < columns; column++) {

					Cell<E> cell = matrix.getCell(row + remove, column);

					matrix.setCell(cell, row, column);

					if (needNull) {
						matrix.setCell(null, row + remove, column);
					}
				}
			}

			matrix.rows -= remove;
		}
	}

	public static final <E, T extends Matrix<E>> T RowOfCollection(Collection<E> c, T m)
	{
		Matrix.VerifyLimits(m, 1, c.size());

		int i = Position.FIRST;
		for (E e : c) {
			m.set(e, Position.FIRST, i);
			i++;
		}

		return m;
	}

	public static final <E> int RowsOfArray(E[][] array)
	{
		return array.length;
	}

	public static final <E, T extends Matrix<E>> T TransposeClone(Matrix<E> matrix)
	{
		return Matrix.TransposeQuote(matrix.clone());
	}

	@SuppressWarnings("unchecked")
	public static final <E, T extends Matrix<E>> T TransposeQuote(Matrix<E> matrix)
	{
		Matrix<E> transpose = matrix.createMatrix(new Size(matrix.getColumns(), matrix
				.getRows()));

		for (int row = Position.FIRST; row < matrix.getRows(); row++) {
			for (int column = Position.FIRST; column < matrix.getColumns(); column++) {

				transpose.setCell(matrix.getCell(row, column), column, row);
			}
		}

		return (T) transpose;
	}

	/**
	 * Verify the size of a matrix. If the matrix is not large enough,
	 * {@link Matrix#ExpandLimits} will be called to expand the matrix.
	 * Attention that this method will reassign the size to the matrix that
	 * means the matrix size will be exactly the size according to the
	 * parameters.
	 * 
	 * @param <E>
	 *            The generic type of the elements in matrix.
	 * @param matrix
	 *            The matrix.
	 * @param rows
	 *            NumericMatrix positive number that the matrix rows number will
	 *            be.
	 * @param columns
	 *            NumericMatrix positive number that the matrix columns number
	 *            will be.
	 * @see Matrix#ExpandLimits(Matrix, int, int)
	 */
	public static final <E> void VerifyLimits(Matrix<E> matrix, int rows, int columns)
	{
		if (rows > 0 && columns > 0) {
			if (rows > matrix.getRowsLimit() || columns > matrix.getColumnsLimit()) {
				Matrix.ExpandLimits(matrix, rows, columns);
			}
			matrix.setRows(rows);
			matrix.setColumns(columns);
		}
	}

	public static final <E> void VerifyLimits(Matrix<E> matrix, Sized sized)
	{
		Matrix.VerifyLimits(matrix, sized.getRows(), sized.getColumns());
	}

	private Cell<E>[][]								matrix;

	private int										rows;

	private int										columns;

	private int										priority			= Matrix.ROW_PRIORITY;

	public boolean									listenElements		= true;

	private Collection<MatrixElementListener<E>>	elementListeners	= new LinkedHashSet<MatrixElementListener<E>>();

	/**
	 * To create an empty matrix which has only one cell.
	 */
	public Matrix()
	{
		this(Size.CELL_SIZE);
	}

	/**
	 * Clone
	 */
	public Matrix(Cell<E>[][] matrix)
	{
		this(matrix, null);
	}

	/**
	 * Clone
	 */
	public Matrix(Cell<E>[][] matrix, Range range)
	{
		if (range == null) {
			range = Range.Full(matrix);
		}

		rows = range.getRows();

		columns = range.getColumns();

		Position position = range.getBegin();

		Cell<E>[][] cells = this.createCellsArray(rows, columns);

		this.setMatrix(cells);

		for (int row = Position.FIRST; row < rows; row++) {
			for (int column = Position.FIRST; column < columns; column++) {
				this.setCell(
						this.createCell(this.cloneElement(matrix[row + position.getRow()][column
								+ position.getColumn()].getElement())), row, column);
			}
		}

	}

	/**
	 * Clone
	 */
	public Matrix(E[][] matrix)
	{
		this(matrix, null);
	}

	/**
	 * Clone
	 */
	public Matrix(E[][] matrix, Range range)
	{
		if (range == null) {
			range = Range.Full(matrix);
		}

		rows = range.getRows();

		columns = range.getColumns();

		Position position = range.getBegin();

		Cell<E>[][] cells = this.createCellsArray(rows, columns);

		this.setMatrix(cells);

		for (int row = Position.FIRST; row < rows; row++) {
			for (int column = Position.FIRST; column < columns; column++) {
				this.setCell(this.createCell(this.cloneElement(matrix[row
						+ position.getRow()][column + position.getColumn()])), row,
						column);
			}
		}
	}

	/**
	 * To create an empty matrix.
	 * 
	 * @param rows
	 *            The rows of the matrix.
	 * @param columns
	 *            The columns of the matrix.
	 */
	public Matrix(int rows, int columns)
	{
		this.rows = rows;
		this.columns = columns;

		this.setMatrix(this.createCellsArray(rows, columns));

		for (int row = Position.FIRST; row < rows; row++) {
			for (int column = Position.FIRST; column < columns; column++) {
				this.setCell(this.createCell(null), row, column);
			}
		}
	}

	/**
	 * Quote
	 */
	public Matrix(Matrix<E> matrix)
	{
		this(matrix, null);
	}

	/**
	 * Quote
	 */
	public Matrix(Matrix<E> matrix, Range range)
	{
		if (range == null) {
			range = Range.Full(matrix);
		}

		rows = range.getRows();

		columns = range.getColumns();

		Position position = range.getBegin();

		Cell<E>[][] cells = this.createCellsArray(rows, columns);

		this.setMatrix(cells);

		for (int row = Position.FIRST; row < rows; row++) {
			for (int column = Position.FIRST; column < columns; column++) {
				this.setCell(
						matrix.getCell(row + position.getRow(),
								column + position.getColumn()), row, column);
			}
		}

	}

	/**
	 * To create an empty matrix.
	 * 
	 * @param size
	 *            The size of the matrix.
	 */
	public Matrix(Sized size)
	{
		this(size.getRows(), size.getColumns());
	}

	public void addElementListener(MatrixElementListener<E> l)
	{
		if (l != null) {
			this.getElementListeners().add(l);
		}
	}

	public void broadcastElementChanged(Matrix<E> source, int srcRow, int srcCol, E old,
			int row, int column, E now)
	{
		if (this.isListenElements()) {
			for (MatrixElementListener<E> l : this.elementListeners) {
				if (l.changed(source, srcRow, srcCol, old, this, row, column, now))
					break;
			}
		}
	}

	public Iterable<Cell<E>> cells()
	{
		return cells(this.getPriority());
	}

	public Iterable<Cell<E>> cells(int priority)
	{
		return new MatrixCellIterator(priority);
	}

	/**
	 * Set all elements in the matrix to null.
	 */
	public void clear()
	{
		this.setAll(null);
	}

	public void clearElementListeners()
	{
		this.getElementListeners().clear();
	}

	/**
	 * This method is equal to calling clone(Range.Full(this)).
	 */
	@Override
	public Matrix<E> clone()
	{
		return this.clone(Range.Full(this));
	}

	@SuppressWarnings("unchecked")
	public <T extends Matrix<E>> T clone(int index)
	{
		return (T) this.clone(this.getRowOfIndex(index), this.getColumnOfIndex(index));
	}

	@SuppressWarnings("unchecked")
	public <T extends Matrix<E>> T clone(int row, int column)
	{
		return (T) this.clone(new Range(row, row, column, column));
	}

	@SuppressWarnings("unchecked")
	public <T extends Matrix<E>> T clone(int beginRow, int endRow, int beginColumn,
			int endColumn)
	{
		return (T) this.clone(new Range(beginRow, endRow, beginColumn, endColumn));
	}

	@SuppressWarnings("unchecked")
	public <T extends Matrix<E>> T clone(Range range)
	{
		return (T) Matrix.Clone(this, range);
	}

	public <T extends Matrix<E>> T clone(String pattern)
	{
		return this.clone(new Range(this, pattern));
	}

	public <T extends Matrix<E>> T clone(T matrix)
	{
		return this.clone(matrix, null, null);
	}

	public <T extends Matrix<E>> T clone(T matrix, Position offset)
	{
		return this.clone(matrix, null, offset);
	}

	public <T extends Matrix<E>> T clone(T matrix, Range range)
	{
		return this.clone(matrix, range, null);
	}

	@SuppressWarnings("unchecked")
	public <T extends Matrix<E>> T clone(T matrix, Range range, Position offset)
	{
		return (T) Matrix.Clone(matrix, range, this, offset);
	}

	protected Cell<E> cloneCell()
	{
		return this.cloneCell(Position.FIRST, Position.FIRST);
	}

	protected Cell<E> cloneCell(int index)
	{
		return this.cloneCell(this.getRowOfIndex(index), this.getColumnOfIndex(index));
	}

	protected Cell<E> cloneCell(int row, int column)
	{
		return this.getCell(row, column).clone(this);
	}

	protected Cell<E> cloneCell(Position position)
	{
		return this.cloneCell(position.getRow(), position.getColumn());
	}

	@SuppressWarnings("unchecked")
	public <T extends Matrix<E>> T cloneColumn(int column)
	{
		return (T) Matrix.CloneColumn(this, column);
	}

	@SuppressWarnings("unchecked")
	public <T extends Matrix<E>> T cloneColumns(int begin, int end)
	{
		return (T) Matrix.CloneColumns(this, begin, end);
	}

	/**
	 * Clone the element in the cell. This method will be called when a cell is
	 * being cloned.<br />
	 * If the elements in the cloned matrix are the exactly ones in the original
	 * matrix then this method should directly return the element parameter:<br />
	 * <br /> {@code return element;} <br />
	 * <br />
	 * Otherwise, return a new object which is the copy of element. Since the
	 * element parameter would be {@code null} for some case, the following
	 * statement is recommended:<br />
	 * <br /> {@code return element == null ? element : element.clone();}
	 * 
	 * @param element
	 *            The element in the cell.
	 * @return The clone of the element.
	 */
	protected E cloneElement(E element)
	{
		return element;
	}

	@SuppressWarnings("unchecked")
	public <T extends Matrix<E>> T cloneRow(int row)
	{
		return (T) Matrix.CloneRow(this, row);
	}

	@SuppressWarnings("unchecked")
	public <T extends Matrix<E>> T cloneRows(int begin, int end)
	{
		return (T) Matrix.CloneRows(this, begin, end);
	}

	public <T extends Matrix<E>> T cloneTranspose()
	{
		return Matrix.TransposeClone(this);
	}

	protected Cell<E> createCell(E element)
	{
		return new Cell<E>(element);
	}

	protected Cell<E>[][] createCellsArray(int... dimensions)
	{
		return Matrix.CreateCellsArray(dimensions);
	}

	protected Matrix<E> createClone(Matrix<E> matrix, Range range)
	{
		return new Matrix<E>(matrix.getMatrix(), range);
	}

	protected Matrix<E> createMatrix(Sized size)
	{
		return new Matrix<E>(size);
	}

	protected Matrix<E> createQuote(Matrix<E> matrix, Range range)
	{
		return new Matrix<E>(matrix, range);
	}

	@Override
	public boolean equals(Object o)
	{
		boolean equals = false;

		if (o instanceof Matrix<?>) {

			Matrix<?> m = (Matrix<?>) o;

			if (this.sameSizeOf(m)) {

				equals = true;

				for (int row = Position.FIRST; row < this.getRows() && equals; row++) {
					for (int column = Position.FIRST; column < this.getColumns(); column++)
					{
						if (!this.get(row, column).equals(m.get(row, column))) {
							equals = false;
							break;
						}
					}
				}
			}
		}

		return equals;
	}

	@Override
	protected void finalize() throws Throwable
	{
		int rowsLimit = this.getRowsLimit();
		int columnsLimit = this.getColumnsLimit();

		for (int row = Position.FIRST; row < rowsLimit; row++) {
			for (int column = Position.FIRST; column < columnsLimit; column++) {
				Cell<E> cell = this.getCell(row, column);
				if (cell != null) {
					this.setCell(null, row, column);
				}
			}
		}

		this.setMatrix(null);

		super.finalize();
	}

	public E get()
	{
		return this.get(Position.FIRST, Position.FIRST);
	}

	public E[][] get(E[][] matrix)
	{
		return this.get(matrix, null, null);
	}

	public E[][] get(E[][] matrix, Position offset)
	{
		return this.get(matrix, null, offset);
	}

	public E[][] get(E[][] matrix, Range range)
	{
		return this.get(matrix, range, null);
	}

	public E[][] get(E[][] matrix, Range range, Position offset)
	{
		return Matrix.Assign(this, range, matrix, offset);
	}

	public E get(int index)
	{
		return this.get(this.getRowOfIndex(index), this.getColumnOfIndex(index));
	}

	public E get(int row, int column)
	{
		return this.getCell(row, column).getElement();
	}

	public E get(Position position)
	{
		return this.get(position.getRow(), position.getColumn());
	}

	protected Cell<E> getCell()
	{
		return this.getCell(Position.FIRST, Position.FIRST);
	}

	protected Cell<E> getCell(int index)
	{
		return this.getCell(this.getRowOfIndex(index), this.getColumnOfIndex(index));
	}

	protected Cell<E> getCell(int row, int column)
	{
		return this.getMatrix()[row][column];
	}

	protected Cell<E> getCell(Position position)
	{
		return this.getCell(position.getRow(), position.getColumn());
	}

	public int getColumnOfIndex(int index)
	{
		int column = Position.FIRST;

		switch (this.getPriority())
		{
			case Matrix.ROW_PRIORITY:
				column = index % columns;
				break;

			case Matrix.COLUMN_PRIORITY:
				column = index / rows;
				break;
		}

		return column;
	}

	public int getColumns()
	{
		return columns;
	}

	protected int getColumnsLimit()
	{
		return Matrix.ColumnsOfArray(this.getMatrix());
	}

	protected Collection<MatrixElementListener<E>> getElementListeners()
	{
		return elementListeners;
	}

	public int getIndexOfPosition(int row, int column)
	{
		int index = Position.FIRST;

		switch (this.getPriority())
		{
			case Matrix.ROW_PRIORITY:
				index = row * columns + column;
				break;

			case Matrix.COLUMN_PRIORITY:
				index = column * rows + row;
				break;
		}

		return index;
	}

	public int getIndexOfPosition(Position position)
	{
		return this.getIndexOfPosition(position.row, position.column);
	}

	public List<E> getListOfColumn(int column)
	{
		return Matrix.ListOfColumn(this, column, new ArrayList<E>(this.getRows()));
	}

	public List<E> getListOfRow(int row)
	{
		return Matrix.ListOfRow(this, row, new ArrayList<E>(this.getColumns()));
	}

	protected Cell<E>[][] getMatrix()
	{
		return matrix;
	}

	public Position getPositionOfIndex(int index)
	{
		return this.getPositionOfIndex(index, null);
	}

	public Position getPositionOfIndex(int index, Position position)
	{
		if (position == null) {
			position = new Position();
		}

		position.setRow(this.getRowOfIndex(index));
		position.setColumn(this.getColumnOfIndex(index));

		return position;
	}

	public int getPriority()
	{
		return priority;
	}

	public int getRowOfIndex(int index)
	{
		int row = Position.FIRST;

		switch (this.getPriority())
		{
			case Matrix.ROW_PRIORITY:
				row = index / columns;
				break;

			case Matrix.COLUMN_PRIORITY:
				row = index % rows;
				break;
		}

		return row;
	}

	public int getRows()
	{
		return rows;
	}

	protected int getRowsLimit()
	{
		return Matrix.RowsOfArray(this.getMatrix());
	}

	public Size getSize()
	{
		return new Size(rows, columns);
	}

	public Size getSize(Size size)
	{
		return size.setSize(rows, columns);
	}

	public boolean hasPosition(int row, int column)
	{
		return row >= Position.FIRST && column >= Position.FIRST && row < this.getRows()
				&& column < this.getColumns();
	}

	public boolean hasPosition(Position position)
	{
		return this.hasPosition(position.getRow(), position.getColumn());
	}

	public <T extends Matrix<E>> T insertColumns(Matrix<E> columns, int columnIndex)
	{
		return Matrix.InsertColumnsToMatrix(this, columns, columnIndex);
	}

	public <T extends Matrix<E>> T insertRows(Matrix<E> rows, int rowIndex)
	{
		return Matrix.InsertRowsToMatrix(this, rows, rowIndex);
	}

	public boolean isCell()
	{
		boolean is = false;

		if (this.sameSizeOf(Size.CELL_SIZE)) {
			is = true;
		}

		return is;
	}

	public boolean isColumn()
	{
		return this.getColumns() == 1;
	}

	public boolean isListenElements()
	{
		return listenElements;
	}

	public boolean isRow()
	{
		return this.getRows() == 1;
	}

	/**
	 * {@inheritDoc}
	 */
	public Iterator<E> iterator()
	{
		return new MatrixIterator();
	}

	public <T extends Matrix<E>> T joinColumns(Matrix<E> matrix)
	{
		return this.joinColumns(this, matrix);
	}

	@SuppressWarnings("unchecked")
	public <T extends Matrix<E>> T joinColumns(Matrix<E> a, Matrix<E> b)
	{
		return (T) Matrix.JoinColumns(a, b, this);
	}

	public <T extends Matrix<E>> T joinRows(Matrix<E> matrix)
	{
		return this.joinRows(this, matrix);
	}

	@SuppressWarnings("unchecked")
	public <T extends Matrix<E>> T joinRows(Matrix<E> a, Matrix<E> b)
	{
		return (T) Matrix.JoinRows(a, b, this);
	}

	public void put(E element)
	{
		this.put(element, Position.FIRST, Position.FIRST);
	}

	public void put(E element, int index)
	{
		this.put(element, this.getRowOfIndex(index), this.getColumnOfIndex(index));
	}

	public void put(E element, int row, int column)
	{
		this.getCell(row, column).putElement(this, row, column, element);
	}

	public void put(E element, Position position)
	{
		this.put(element, position.getRow(), position.getColumn());
	}

	public void put(E element, Range range)
	{
		for (int row = range.getBegin().row; row < range.getRows(); row++) {
			for (int column = range.getBegin().column; column < range.getColumns(); column++)
			{
				put(element, row, column);
			}
		}
	}

	public void put(E element, String range)
	{
		this.put(element, new Range(this, range));
	}

	public void put(E[][] matrix)
	{
		this.put(matrix, null, null);
	}

	public void put(E[][] matrix, Position offset)
	{
		this.put(matrix, null, offset);
	}

	public void put(E[][] matrix, Range range)
	{
		this.put(matrix, range, null);
	}

	public void put(E[][] matrix, Range range, Position offset)
	{
		Matrix.Place(matrix, range, this, offset);
	}

	public void put(Matrix<E> matrix)
	{
		this.put(matrix, null, null);
	}

	public void put(Matrix<E> matrix, Position offset)
	{
		this.put(matrix, null, offset);
	}

	public void put(Matrix<E> matrix, Range range)
	{
		this.put(matrix, range, null);
	}

	public void put(Matrix<E> matrix, Range range, Position offset)
	{
		Matrix.Place(matrix, range, this, offset);
	}

	public void putAll(E element)
	{
		this.put(element, Range.Full(this));
	}

	@SuppressWarnings("unchecked")
	public <T extends Matrix<E>> T quote()
	{
		return (T) this.quote(Range.Full(this));
	}

	public <T extends Matrix<E>> T quote(int index)
	{
		return this.quote(this.getRowOfIndex(index), this.getColumnOfIndex(index));
	}

	@SuppressWarnings("unchecked")
	public <T extends Matrix<E>> T quote(int row, int column)
	{
		return (T) this.quote(new Range(row, row, column, column));
	}

	@SuppressWarnings("unchecked")
	public <T extends Matrix<E>> T quote(int beginRow, int endRow, int beginColumn,
			int endColumn)
	{
		return (T) this.quote(new Range(beginRow, endRow, beginColumn, endColumn));
	}

	@SuppressWarnings("unchecked")
	public <T extends Matrix<E>> T quote(Range range)
	{
		return (T) Matrix.Quote(this, range);
	}

	public <T extends Matrix<E>> T quote(String pattern)
	{
		return this.quote(new Range(this, pattern));
	}

	public <T extends Matrix<E>> T quote(T matrix)
	{
		return this.quote(matrix, null, null);
	}

	public <T extends Matrix<E>> T quote(T matrix, Position offset)
	{
		return this.quote(matrix, null, offset);
	}

	public <T extends Matrix<E>> T quote(T matrix, Range range)
	{
		return this.quote(matrix, range, null);
	}

	@SuppressWarnings("unchecked")
	public <T extends Matrix<E>> T quote(T matrix, Range range, Position offset)
	{
		return (T) Matrix.Quote(matrix, range, this, offset);
	}

	protected Cell<E> quoteCell()
	{
		return this.quoteCell(Position.FIRST, Position.FIRST);
	}

	protected Cell<E> quoteCell(int index)
	{
		return this.quoteCell(this.getRowOfIndex(index), this.getColumnOfIndex(index));
	}

	protected Cell<E> quoteCell(int row, int column)
	{
		return this.getMatrix()[row][column];
	}

	protected Cell<E> quoteCell(Position position)
	{
		return this.quoteCell(position.getRow(), position.getColumn());
	}

	@SuppressWarnings("unchecked")
	public <T extends Matrix<E>> T quoteColumn(int column)
	{
		return (T) Matrix.QuoteColumn(this, column);
	}

	@SuppressWarnings("unchecked")
	public <T extends Matrix<E>> T quoteColumns(int begin, int end)
	{
		return (T) Matrix.QuoteColumns(this, begin, end);
	}

	@SuppressWarnings("unchecked")
	public <T extends Matrix<E>> T quoteRow(int row)
	{
		return (T) Matrix.QuoteRow(this, row);
	}

	@SuppressWarnings("unchecked")
	public <T extends Matrix<E>> T quoteRows(int begin, int end)
	{
		return (T) Matrix.QuoteRows(this, begin, end);
	}

	public <T extends Matrix<E>> T quoteTranspose()
	{
		return Matrix.TransposeQuote(this);
	}

	public void removeColumn(int columnIndex)
	{
		this.removeColumns(columnIndex, columnIndex);
	}

	public void removeColumns(int fromIndex, int toIndex)
	{
		Matrix.RemoveColumnsFromMatrix(this, fromIndex, toIndex);
	}

	public void removeElementListener(MatrixElementListener<E> l)
	{
		if (l != null) {
			this.getElementListeners().remove(l);
		}
	}

	public void removeRow(int rowIndex)
	{
		this.removeRows(rowIndex, rowIndex);
	}

	public void removeRows(int fromIndex, int toIndex)
	{
		Matrix.RemoveRowsFromMatrix(this, fromIndex, toIndex);
	}

	public boolean sameSizeOf(Sized sized)
	{
		return Size.SameSize(this, sized);
	}

	public void set(E element)
	{
		this.set(element, Position.FIRST, Position.FIRST);
	}

	public void set(E element, int index)
	{
		this.set(element, this.getRowOfIndex(index), this.getColumnOfIndex(index));
	}

	public void set(E element, int row, int column)
	{
		this.getCell(row, column).setElement(element);
	}

	public void set(E element, Position position)
	{
		this.set(element, position.getRow(), position.getColumn());
	}

	public void set(E element, Range range)
	{
		for (int row = range.getBegin().row; row < range.getRows(); row++) {
			for (int column = range.getBegin().column; column < range.getColumns(); column++)
			{
				set(element, row, column);
			}
		}
	}

	public void set(E element, String range)
	{
		this.set(element, new Range(this, range));
	}

	public void set(E[][] matrix)
	{
		this.set(matrix, null, null);
	}

	public void set(E[][] matrix, Position offset)
	{
		this.set(matrix, null, offset);
	}

	public void set(E[][] matrix, Range range)
	{
		this.set(matrix, range, null);
	}

	public void set(E[][] matrix, Range range, Position offset)
	{
		Matrix.Assign(matrix, range, this, offset);
	}

	public void set(Matrix<E> matrix)
	{
		this.set(matrix, null, null);
	}

	public void set(Matrix<E> matrix, Position offset)
	{
		this.set(matrix, null, offset);
	}

	public void set(Matrix<E> matrix, Range range)
	{
		this.set(matrix, range, null);
	}

	public void set(Matrix<E> matrix, Range range, Position offset)
	{
		Matrix.Assign(matrix, range, this, offset);
	}

	public void setAll(E element)
	{
		this.set(element, Range.Full(this));
	}

	protected void setCell(Cell<E> cell)
	{
		this.setCell(cell, Position.FIRST, Position.FIRST);
	}

	protected void setCell(Cell<E> cell, int index)
	{
		this.setCell(cell, this.getRowOfIndex(index), this.getColumnOfIndex(index));
	}

	protected void setCell(Cell<E> cell, int row, int column)
	{
		Cell<E> old = this.getCell(row, column);

		this.getMatrix()[row][column] = cell;

		if (cell != null) {
			cell.addMatrixPosition(this, row, column);
		}

		if (old != null) {
			old.removeMatrixPosition(this, row, column);
		}
	}

	protected void setCell(Cell<E> cell, Position position)
	{
		this.setCell(cell, position.getRow(), position.getColumn());
	}

	protected void setColumns(int columns)
	{
		if (columns <= this.getColumnsLimit()) {
			this.columns = columns;
		}
	}

	protected void setElementListeners(
			Collection<MatrixElementListener<E>> elementListeners)
	{
		this.elementListeners = elementListeners;
	}

	public void setListenElements(boolean listenElements)
	{
		this.listenElements = listenElements;
	}

	protected void setMatrix(Cell<E>[][] matrix)
	{
		this.matrix = matrix;
	}

	public void setPriority(int priority)
	{
		this.priority = priority;
	}

	protected void setRows(int rows)
	{
		if (rows <= this.getRowsLimit()) {
			this.rows = rows;
		}
	}

	public int size()
	{
		return this.getRows() * this.getColumns();
	}

	public E[][] toArray()
	{
		return Matrix.MatrixToArray(this);
	}

	public String toHTML()
	{
		StringBuilder html = new StringBuilder("<table class=\"matrix\">");

		for (int row = Position.FIRST; row < this.getRows(); row++) {
			html.append("<tr>");
			for (int column = Position.FIRST; column < this.getColumns(); column++) {
				E element = get(row, column);
				html.append("<td>");
				html.append(element);
				html.append("</td>");
			}
			html.append("</tr>");
		}

		html.append("</table>");

		return html.toString();
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();

		for (int row = Position.FIRST; row < this.getRows(); row++) {
			for (int column = Position.FIRST; column < this.getColumns(); column++) {
				E element = get(row, column);
				builder.append(element);
				builder.append('\t');
			}
			builder.append('\n');
		}

		return builder.toString();
	}

	public void transpose()
	{
		int rows = this.rows;
		int columns = this.columns;

		Cell<E>[][] transpose = this.createCellsArray(columns, rows);

		for (int row = Position.FIRST; row < rows; row++) {

			for (int column = Position.FIRST; column < columns; column++) {

				transpose[column][row] = this.getCell(row, column);
			}
		}

		this.setMatrix(transpose);

		this.setRows(columns);
		this.setColumns(rows);
	}
}

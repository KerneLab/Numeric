package org.kernelab.numeric.matrix;

import java.io.Serializable;

import org.kernelab.basis.Copieable;

/**
 * The class to describe the Size of a matrix, including how many rows and
 * columns in the matrix. And the Size of a certain Range, even a row, column or
 * element, could also be expressed by this class.
 * 
 * @author Dilly King
 * @version 1.0.2
 * @update 2010-08-01
 * 
 */
public class Size implements Sized, Serializable, Copieable<Size>
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1770250292470284822L;

	public static final Size	CELL_SIZE			= new Size(1, 1);

	public static final <E> Size Column(Matrix<E> matrix)
	{
		return new Size(matrix.getRows(), 1);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

	}

	public static final <E> Size Row(Matrix<E> matrix)
	{
		return new Size(1, matrix.getColumns());
	}

	public static final boolean SameSize(Sized a, Sized b)
	{
		return a.getRows() == b.getRows() && a.getColumns() == b.getColumns();
	}

	private int	rows;

	private int	columns;

	public Size(int rows, int columns)
	{
		this.setSize(rows, columns);
	}

	protected Size(Size size)
	{
		this.setSize(size);
	}

	@Override
	public Size clone()
	{
		return new Size(this);
	}

	@Override
	public boolean equals(Object o)
	{
		boolean is = false;

		if (o instanceof Size) {
			Size s = (Size) o;
			if (this.rows == s.rows && this.columns == s.columns) {
				is = true;
			}
		}

		return is;
	}

	public int getColumns()
	{
		return columns;
	}

	public int getRows()
	{
		return rows;
	}

	@Override
	public int hashCode()
	{
		return this.toString().hashCode();
	}

	public boolean sameSizeOf(Sized sized)
	{
		return SameSize(this, sized);
	}

	public Size setColumns(int columns)
	{
		this.columns = columns;
		return this;
	}

	public Size setRows(int rows)
	{
		this.rows = rows;
		return this;
	}

	public Size setSize(int rows, int columns)
	{
		return this.setRows(rows).setColumns(columns);
	}

	public Size setSize(Size size)
	{
		return this.setSize(size.getRows(), size.getColumns());
	}

	@Override
	public String toString()
	{
		return this.getRows() + "," + this.getColumns();
	}

}

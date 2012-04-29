package org.kernelab.numeric.matrix;

import java.io.Serializable;

import org.kernelab.basis.Copieable;
import org.kernelab.basis.Tools;

/**
 * The class to describe a Range in a matrix from rowIndexA to rowIndexB and
 * from columnIndexA to columnIndexB.
 * 
 * @author Dilly King
 * @version 1.1.9
 * @update 2011-05-31
 * 
 */
public class Range implements Sized, Serializable, Copieable<Range>
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3343195821817271621L;

	public static final String	FULL_RANGE			= ":";

	public static final Range Cell(int row, int column)
	{
		return new Range(row, row, column, column);
	}

	public static final Range Cell(Position position)
	{
		return Cell(position.getRow(), position.getColumn());
	}

	public static final <E> Range Column(Matrix<E> matrix, int column)
	{
		return new Range(Position.FIRST, matrix.getRows() - 1, column, column);
	}

	public static final <E> Range Columns(Matrix<E> matrix, int begin, int end)
	{
		return new Range(Position.FIRST, matrix.getRows() - 1, begin, end);
	}

	public static final <E> Range Full(E[][] array)
	{
		return new Range(Position.FIRST, Matrix.RowsOfArray(array) - 1, Position.FIRST,
				Matrix.ColumnsOfArray(array) - 1);
	}

	public static final <E> Range Full(Sized sized)
	{
		return new Range(sized);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Range range = new Range(1, 3, 1, 4);
		Tools.debug(range);
	}

	public static final void Normalize(Range range)
	{
		int beginRow = range.getBegin().getRow();
		int endRow = range.getEnd().getRow();
		int beginColumn = range.getBegin().getColumn();
		int endColumn = range.getEnd().getColumn();

		if (beginRow > endRow) {
			range.getBegin().setRow(endRow);
			range.getEnd().setRow(beginRow);
		}

		if (beginColumn > endColumn) {
			range.getBegin().setColumn(endColumn);
			range.getEnd().setColumn(beginColumn);
		}
	}

	public static final <E> Range Row(Matrix<E> matrix, int row)
	{
		return new Range(row, row, Position.FIRST, matrix.getColumns() - 1);
	}

	public static final <E> Range Rows(Matrix<E> matrix, int begin, int end)
	{
		return new Range(begin, end, Position.FIRST, matrix.getColumns() - 1);
	}

	public static final Size Size(Range range)
	{
		return Size(range, new Size(0, 0));
	}

	public static final Size Size(Range range, Size size)
	{
		int rows = range.getEnd().getRow() - range.getBegin().getRow() + 1;

		int columns = range.getEnd().getColumn() - range.getBegin().getColumn() + 1;

		size.setSize(rows, columns);

		return size;
	}

	private Position	begin	= new Position();

	private Position	end		= new Position();

	public Range(int beginRow, int endRow, int beginColumn, int endColumn)
	{
		this.set(beginRow, endRow, beginColumn, endColumn);
	}

	public <E> Range(Matrix<E> matrix, String pattern)
	{
		pattern.replaceAll(" ", "");

		if (pattern.equals(FULL_RANGE)) {
			pattern = ":,:";
		}

		String[] strings = pattern.split(",");

		if (strings.length != 2) {
			throw new IllegalRangePatternException("Only has row or column pattern: "
					+ pattern);
		}

		String rowString = strings[0];
		String columnString = strings[1];

		if (rowString.equals(FULL_RANGE)) {
			rowString = "";
		}
		if (columnString.equals(FULL_RANGE)) {
			columnString = "";
		}

		String[] rows = Tools.splitCharSequence(rowString, FULL_RANGE);
		String[] columns = Tools.splitCharSequence(columnString, FULL_RANGE);

		int beginRow = Position.FIRST;
		int beginColumn = Position.FIRST;
		int endRow = matrix.getRows() - 1;
		int endColumn = matrix.getColumns() - 1;

		switch (rows.length)
		{
			case 1:
				if (!rows[0].equals("")) {
					beginRow = endRow = Integer.parseInt(rows[0]) - 1;
				}
				break;

			case 2:
				beginRow = Integer.parseInt(rows[0]) - 1;
				endRow = Integer.parseInt(rows[1]) - 1;
				break;
		}

		switch (columns.length)
		{
			case 1:
				if (!columns[0].equals("")) {
					beginColumn = endColumn = Integer.parseInt(columns[0]) - 1;
				}
				break;

			case 2:
				beginColumn = Integer.parseInt(columns[0]) - 1;
				endColumn = Integer.parseInt(columns[1]) - 1;
				break;
		}

		this.set(beginRow, endRow, beginColumn, endColumn);
	}

	public Range(Position begin, Position end)
	{
		this.set(begin, end);
	}

	protected Range(Range range)
	{
		this(range.getBegin(), range.getEnd());
	}

	public Range(Sized sized)
	{
		this(Position.FIRST, sized.getRows() - 1, Position.FIRST, sized.getColumns() - 1);
	}

	@Override
	public Range clone()
	{
		return new Range(this);
	}

	public boolean equals(int beginRow, int endRow, int beginColumn, int endColumn)
	{
		boolean is = false;

		is = Math.min(this.begin.row, this.end.row) == Math.min(beginRow, endRow)
				&& Math.max(this.begin.row, this.end.row) == Math.max(beginRow, endRow)
				&& Math.min(this.begin.column, this.end.column) == Math.min(beginColumn,
						endColumn)
				&& Math.max(this.begin.column, this.end.column) == Math.max(beginColumn,
						endColumn);

		return is;
	}

	@Override
	public boolean equals(Object o)
	{
		boolean is = false;

		if (o instanceof Range) {

			Range r = (Range) o;

			is = equals(r.getBeginRow(), r.getEndRow(), r.getBeginColumn(),
					r.getEndColumn());
		}

		return is;
	}

	public boolean equals(Position begin, Position end)
	{
		return this.equals(begin.getRow(), end.getRow(), begin.getColumn(),
				end.getColumn());
	}

	public Position getBegin()
	{
		return begin;
	}

	public int getBeginColumn()
	{
		return this.getBegin().getColumn();
	}

	public int getBeginRow()
	{
		return this.getBegin().getRow();
	}

	public int getColumns()
	{
		return Math.abs(end.column - begin.column) + 1;
	}

	public Position getEnd()
	{
		return end;
	}

	public int getEndColumn()
	{
		return this.getEnd().getColumn();
	}

	public int getEndRow()
	{
		return this.getEnd().getRow();
	}

	public int getRows()
	{
		return Math.abs(end.row - begin.row) + 1;
	}

	public Size getSize()
	{
		return Size(this);
	}

	public Size getSize(Size size)
	{
		return Size(this, size);
	}

	public Range normalize()
	{
		Normalize(this);
		return this;
	}

	public boolean sameSizeOf(Sized sized)
	{
		return Size.SameSize(this, sized);
	}

	public Range set(int beginRow, int endRow, int beginColumn, int endColumn)
	{
		return this.setRows(beginRow, endRow).setColumns(beginColumn, endColumn);
	}

	public Range set(Position begin, Position end)
	{
		return this.set(begin.row, end.row, begin.column, end.column);
	}

	public Range set(Range range)
	{
		return this.set(range.getBegin(), range.getEnd());
	}

	public Range setColumns(int begin, int end)
	{
		if (begin < end) {
			this.begin.column = begin;
			this.end.column = end;
		} else {
			this.begin.column = end;
			this.end.column = begin;
		}
		return this;
	}

	public Range setRows(int begin, int end)
	{
		if (begin < end) {
			this.begin.row = begin;
			this.end.row = end;
		} else {
			this.begin.row = end;
			this.end.row = begin;
		}
		return this;
	}

	@Override
	public String toString()
	{
		return (this.getBegin().getRow() + 1) + ":" + (this.getEnd().getRow() + 1) + ","
				+ (this.getBegin().getColumn() + 1) + ":"
				+ (this.getEnd().getColumn() + 1);
	}
}

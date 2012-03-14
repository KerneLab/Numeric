package org.kernelab.numeric.matrix;

import java.io.Serializable;

import org.kernelab.basis.Copieable;

/**
 * The class to describe the position of elements in a matrix.<br>
 * The value of row and column is Zero-Based. Only the constructor
 * Position(String) is One-Based.
 * 
 * @author Dilly King
 * @version 1.2.3
 * @update 2011-05-29
 * 
 */
public class Position implements Serializable, Copieable<Position>
{

	/**
	 * 
	 */
	private static final long		serialVersionUID	= 1468751405378281987L;

	public static final int			FIRST				= 0;

	// public static final int OLD_FIRST = 1;

	private static final Position	FIRST_POSITION		= new Position(FIRST, FIRST);

	public static final Position Column(int column)
	{
		return new Position(Position.FIRST, column);
	}

	public static final Position FirstPosition()
	{
		return Position.FIRST_POSITION.clone();
	}

	public static final Position New(int row, int column)
	{
		return new Position(row, column);
	}

	public static final Position Row(int row)
	{
		return new Position(row, Position.FIRST);
	}

	public int	row;

	public int	column;

	public Position()
	{
		this.row = Position.FIRST;
		this.column = Position.FIRST;
	}

	public Position(int row, int column)
	{
		this.row = row;
		this.column = column;
	}

	protected Position(Position position)
	{
		this.row = position.row;
		this.column = position.column;
	}

	@Override
	public Position clone()
	{
		return new Position(this);
	}

	@Override
	public boolean equals(Object o)
	{
		boolean is = false;

		if (o != null) {
			if (o instanceof Position) {
				Position p = (Position) o;
				if (this.row == p.row && this.column == p.column) {
					is = true;
				}
			}
		}

		return is;
	}

	public int getColumn()
	{
		return column;
	}

	// /*
	// * This method is One-Based.
	// */
	// public int getOldColumn()
	// {
	// return column + 1;
	// }
	//
	// /*
	// * This method is One-Based.
	// */
	// public int getOldRow()
	// {
	// return row + 1;
	// }

	public int getRow()
	{
		return row;
	}

	@Override
	public int hashCode()
	{
		return this.toString().hashCode();
	}

	public Position setColumn(int column)
	{
		this.column = column;
		return this;
	}

	// /*
	// * This method is One-Based.
	// */
	// public void setOldColumn(int column)
	// {
	// this.column = column - 1;
	// }
	//
	// /*
	// * This method is One-Based.
	// */
	// public void setOldPosition(int row, int column)
	// {
	// this.row = row - 1;
	// this.column = column - 1;
	// }
	//
	// /*
	// * This method is One-Based.
	// */
	// public void setOldRow(int row)
	// {
	// this.row = row - 1;
	// }

	public Position setPosition(int row, int column)
	{
		return this.setRow(row).setColumn(column);
	}

	public Position setPosition(Position position)
	{
		return this.setPosition(position.getRow(), position.getColumn());
	}

	public Position setRow(int row)
	{
		this.row = row;
		return this;
	}

	@Override
	public String toString()
	{
		return (row + 1) + "," + (column + 1);
	}

}

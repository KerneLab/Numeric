package org.kernelab.numeric;

/**
 * This is a basic interface for a table like object.
 * 
 * @author Dilly King
 * 
 * @param <E>
 *            the generic type of the element in the table.
 */
public interface Tabulation<E>
{
	public E get(int row, int column);

	public int getColumns();

	public int getRows();

	public void set(E element, int row, int column);
}

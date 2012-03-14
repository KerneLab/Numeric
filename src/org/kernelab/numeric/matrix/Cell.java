package org.kernelab.numeric.matrix;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.kernelab.basis.Copieable;
import org.kernelab.basis.Tools;

/**
 * The Cell in Matrix which contains element of type E.
 * 
 * @author Dilly King
 * @version 2011.10.29
 * @param <E>
 *            the generic type of element.
 */
public class Cell<E> implements Serializable, Copieable<Cell<E>>
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5991025193737567895L;

	@SuppressWarnings("unchecked")
	public static final <E> E[][] CellsToArray(Cell<E>[][] elements)
	{
		int rows = Matrix.RowsOfArray(elements);
		int columns = Matrix.ColumnsOfArray(elements);

		E[][] array = (E[][]) new Object[rows][columns];

		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				array[row][column] = elements[row][column].getElement();
			}
		}

		return array;
	}

	public E								element;

	private Map<Matrix<E>, List<Position>>	matrip;

	protected Cell(Cell<E> cell)
	{
		this(cell.element);
		this.matrip.putAll(cell.matrip);
	}

	public Cell(E element)
	{
		this(element, new LinkedHashMap<Matrix<E>, List<Position>>());
	}

	public Cell(E element, Map<Matrix<E>, List<Position>> matrip)
	{
		this.setElement(element);
		this.setMatrip(matrip);
	}

	protected boolean addMatrixPosition(Matrix<E> matrix, int row, int column)
	{
		return this.addMatrixPosition(matrix, Position.New(row, column));
	}

	protected boolean addMatrixPosition(Matrix<E> matrix, Position position)
	{
		if (matrix == null) {
			return false;
		} else {
			List<Position> positions = this.getMatrip().get(matrix);
			if (positions == null) {
				positions = this.createPositions();
				this.getMatrip().put(matrix, positions);
			}
			return positions.add(position);
		}
	}

	public boolean belongTo(Matrix<E> matrix)
	{
		return this.getMatrip().containsKey(matrix);
	}

	protected void clearMatrix(Matrix<E> matrix)
	{
		this.getMatrip().remove(matrix);
	}

	/**
	 * This method makes a new Cell contains the element. The new Cell doesn't
	 * belong to ANY Matrix as default which means it should be assigned to a
	 * certain Matrix explicit after cloned.
	 * 
	 * @return A new Cell.
	 */
	@Override
	public Cell<E> clone()
	{
		return new Cell<E>(element);
	}

	/**
	 * This method makes a new Cell contains the element. This method will also
	 * call matrix.cloneElement(Element) to make a clone of the element.The new
	 * Cell doesn't belong to ANY Matrix as default which means it should be
	 * assigned to a certain Matrix explicit after cloned.
	 * 
	 * @param <T>
	 * @param matrix
	 *            The matrix which decides how to make a clone of Element.
	 * @return A new Cell.
	 */
	public <T extends Matrix<E>> Cell<E> clone(Matrix<E> matrix)
	{
		return new Cell<E>(matrix.cloneElement(element));
	}

	protected List<Position> createPositions()
	{
		return new LinkedList<Position>();
	}

	@Override
	public boolean equals(Object o)
	{
		boolean equals = false;

		if (o != null) {
			if (o instanceof Cell<?>) {
				Cell<?> c = (Cell<?>) o;
				if (this.getElement() != null && c.getElement() != null) {
					equals = this.getElement().equals(c.getElement());
					if (equals) {
						equals = this.getMatrip().equals(c.getMatrip());
					}
				}
			}
		}

		return equals;
	}

	@Override
	protected void finalize() throws Throwable
	{
		this.matrip.clear();
		this.matrip = null;
		this.element = null;
		super.finalize();
	}

	public E getElement()
	{
		return element;
	}

	public Set<Matrix<E>> getMatrices()
	{
		return this.matrip.keySet();
	}

	protected Map<Matrix<E>, List<Position>> getMatrip()
	{
		return matrip;
	}

	public Matrix<E> getMatrix()
	{
		return Tools.getElementAt(this.getMatrices(), Position.FIRST);
	}

	public Position getPosition()
	{
		return this.getPosition(this.getMatrix());
	}

	public Position getPosition(Matrix<E> matrix)
	{
		return this.getPositions(matrix).get(Position.FIRST);
	}

	public List<Position> getPositions(Matrix<E> matrix)
	{
		return this.getMatrip().get(matrix);
	}

	public void putElement(Matrix<E> source, int row, int column, E element)
	{
		if (this.belongTo(source)) {

			E old = this.getElement();

			this.setElement(element);

			for (Entry<Matrix<E>, List<Position>> e : this.getMatrip().entrySet()) {

				Matrix<E> matrix = e.getKey();

				if (matrix.isListenElements()) {

					List<Position> positions = e.getValue();

					for (Position position : positions) {
						matrix.broadcastElementChanged(source, row, column, old,
								position.getRow(), position.getColumn(), element);
					}
				}
			}
		}
	}

	protected boolean removeMatrixPosition(Matrix<E> matrix, int row, int column)
	{
		return this.removeMatrixPosition(matrix, Position.New(row, column));
	}

	protected boolean removeMatrixPosition(Matrix<E> matrix, Position position)
	{
		if (matrix == null) {
			return false;
		} else {

			List<Position> positions = this.getMatrip().get(matrix);

			if (positions == null) {
				return false;
			} else {

				boolean r = positions.remove(position);

				if (positions.isEmpty()) {

					this.getMatrip().remove(matrix);

					if (this.getMatrip().isEmpty()) {
						try {
							this.finalize();
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				}

				return r;
			}
		}
	}

	public void setElement(E element)
	{
		this.element = element;
	}

	protected void setMatrip(Map<Matrix<E>, List<Position>> matrices)
	{
		this.matrip = matrices;
	}
}

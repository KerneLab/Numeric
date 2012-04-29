package org.kernelab.numeric.matrix;

import org.kernelab.basis.Tools;
import org.kernelab.numeric.Real;

public class RealMatrix extends NumericMatrix<Real>
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3466003301598750832L;

	public static final <T extends RealMatrix> T Assign(Double[][] source, Range range,
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
				target.getMatrix()[row + offset.row][column + offset.column]
						.setElement(new Real(source[row + from.row][column + from.column]));
			}
		}

		return target;
	}

	public static final <T extends RealMatrix> Double[][] Assign(T source, Range range,
			Double[][] target, Position offset)
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
			target = new Double[rows][columns];
		}

		Position from = range.getBegin();

		for (int row = Position.FIRST; row < range.getRows(); row++) {
			for (int column = Position.FIRST; column < range.getColumns(); column++) {
				target[row + offset.row][column + offset.column] = source.get(row
						+ from.row, column + from.column).value;
			}
		}

		return target;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Double[][] a = { { 1.0, 2.0 }, { 3.0, 4.0 } };
		RealMatrix m = new RealMatrix(a);
		Tools.debug(m);
	}

	public static final RealMatrix Parse(String exp)
	{
		RealMatrix matrix = null;

		exp = exp.replaceAll("\\[", "");
		exp = exp.replaceAll("\\]", "");

		String[] rows = exp.split(Parser.ROW_SPLIT);
		String[] columns = rows[Position.FIRST].split(Parser.COLUMN_SPLIT);

		matrix = new RealMatrix(new Size(rows.length, columns.length));

		for (int row = Position.FIRST; row < rows.length; row++) {
			columns = rows[row].split(Parser.COLUMN_SPLIT);
			for (int column = Position.FIRST; column < columns.length; column++) {
				matrix.set(Real.valueOf(columns[column]), row, column);
			}
		}

		return matrix;
	}

	public RealMatrix()
	{
		super();
	}

	/**
	 * Clone
	 */
	public RealMatrix(Cell<Real>[][] matrix)
	{
		this(matrix, null);
	}

	/**
	 * Clone
	 */
	public RealMatrix(Cell<Real>[][] matrix, Range range)
	{
		super(matrix, range);
	}

	/**
	 * Clone
	 */
	public RealMatrix(Double[][] matrix)
	{
		this(matrix, null);
	}

	/**
	 * Clone
	 */
	public RealMatrix(Double[][] matrix, Range range)
	{
		if (range == null) {
			range = Range.Full(matrix);
		}

		int rows = range.getRows();

		int columns = range.getColumns();

		Position position = range.getBegin();

		Cell<Real>[][] cells = this.createCellsArray(rows, columns);

		this.setMatrix(cells);

		Matrix.VerifyLimits(this, rows, columns);

		for (int row = Position.FIRST; row < rows; row++) {
			for (int column = Position.FIRST; column < columns; column++) {
				this.getMatrix()[row][column] = this.createCell(new Real(matrix[row
						+ position.getRow()][column + position.getColumn()]));
			}
		}
	}

	public RealMatrix(int rows, int columns)
	{
		super(rows, columns);
	}

	/**
	 * Quote
	 */
	public RealMatrix(Matrix<Real> matrix)
	{
		this(matrix, null);
	}

	/**
	 * Quote
	 */
	public RealMatrix(Matrix<Real> matrix, Range range)
	{
		super(matrix, range);
	}

	/**
	 * Clone
	 */
	public RealMatrix(Real[][] matrix)
	{
		this(matrix, null);
	}

	/**
	 * Clone
	 */
	public RealMatrix(Real[][] matrix, Range range)
	{
		super(matrix, range);
	}

	public RealMatrix(Sized size)
	{
		super(size);
	}

	@Override
	public RealMatrix clone()
	{
		return this.clone(Range.Full(this));
	}

	@Override
	protected Real cloneElement(Real element)
	{
		return element == null ? element : element.clone();
	}

	@Override
	protected RealMatrix createClone(Matrix<Real> matrix, Range range)
	{
		return new RealMatrix(matrix.getMatrix(), range);
	}

	@Override
	protected RealMatrix createMatrix(Sized size)
	{
		return new RealMatrix(size);
	}

	@Override
	protected RealMatrix createQuote(Matrix<Real> matrix, Range range)
	{
		return new RealMatrix(matrix, range);
	}

	public Double[][] get(Double[][] matrix)
	{
		return this.get(matrix, null, null);
	}

	public Double[][] get(Double[][] matrix, Position offset)
	{
		return this.get(matrix, null, offset);
	}

	public Double[][] get(Double[][] matrix, Range range)
	{
		return this.get(matrix, range, null);
	}

	public Double[][] get(Double[][] matrix, Range range, Position offset)
	{
		return Assign(this, range, matrix, offset);
	}

	public void set(double value, int row, int column)
	{
		Real real = this.get(row, column);
		if (real == null) {
			real = new Real(0);
			this.set(real, row, column);
		}
		real.value = value;
	}

	public <T extends RealMatrix> T set(Double[][] matrix)
	{
		return this.set(matrix, null, null);
	}

	public <T extends RealMatrix> T set(Double[][] matrix, Position offset)
	{
		return this.set(matrix, null, offset);
	}

	public <T extends RealMatrix> T set(Double[][] matrix, Range range)
	{
		return this.set(matrix, range, null);
	}

	@SuppressWarnings("unchecked")
	public <T extends RealMatrix> T set(Double[][] matrix, Range range, Position offset)
	{
		return (T) Assign(matrix, range, this, offset);
	}

}

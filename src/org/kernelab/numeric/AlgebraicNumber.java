package org.kernelab.numeric;

/**
 * The interface which defines the algebraic operations.
 * 
 * @author Dilly King
 * 
 * @param <N>
 *            The generic type of number.
 */
public interface AlgebraicNumber<N>
{
	/**
	 * Add a number to this self.
	 * 
	 * @param number
	 * @return This self.
	 */
	public N add(N number);

	/**
	 * Add number a and b, put the result into this self.
	 * 
	 * @param a
	 * @param b
	 * @return This self.
	 */
	public N add(N a, N b);

	/**
	 * Divide this self by a number.
	 * 
	 * @param number
	 * @return This self.
	 */
	public N divide(N number);

	/**
	 * Divide number a by b, put the result into this self.
	 * 
	 * @param a
	 * @param b
	 * @return This self.
	 */
	public N divide(N a, N b);

	/**
	 * Get the value of this number into the parameter.
	 * 
	 * @param number
	 *            The number which would holds the value of this number.
	 */
	public void get(N number);

	/**
	 * Minus this by a number.
	 * 
	 * @param number
	 * @return This self.
	 */
	public N minus(N number);

	/**
	 * Minus number a by b, put the result into this self.
	 * 
	 * @param a
	 * @param b
	 * @return This self.
	 */
	public N minus(N a, N b);

	/**
	 * Multiply a number to this self.
	 * 
	 * @param number
	 * @return This self.
	 */
	public N multiply(N number);

	/**
	 * Multiply number a and b, put the result into this self.
	 * 
	 * @param a
	 * @param b
	 * @return This self.
	 */
	public N multiply(N a, N b);

	/**
	 * Get the ONE element.
	 * 
	 * @return Element which means ONE.
	 */
	public N one();

	/**
	 * Set the value of this number to the value of given parameter.
	 * 
	 * @param number
	 *            The number according to which the value of this number would
	 *            be set to.
	 */
	public void set(N number);

	/**
	 * Get the ZERO element.
	 * 
	 * @return Element which means ZERO.
	 */
	public N zero();
}

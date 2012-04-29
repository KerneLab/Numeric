package org.kernelab.numeric;

import java.io.Serializable;

import org.kernelab.basis.Variable;

public class Real extends Variable<Double> implements AlgebraicNumber<Real>, Serializable
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5448290840972602470L;

	/**
	 * Add Real a and b, put the result into c.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return Real c.
	 */
	public static final Real Add(Real a, Real b, Real c)
	{
		c.value = a.value + b.value;
		return c;
	}

	/**
	 * Divide Real a by b, put the result into c.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return Real c.
	 */
	public static final Real Divide(Real a, Real b, Real c)
	{
		c.value = a.value / b.value;
		return c;
	}

	/**
	 * Minus Real a by b, put the result into c.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return Real c.
	 */
	public static final Real Minus(Real a, Real b, Real c)
	{
		c.value = a.value - b.value;
		return c;
	}

	/**
	 * Multiply Real a and b, put the result into c.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return Real c.
	 */
	public static final Real Multiply(Real a, Real b, Real c)
	{
		c.value = a.value * b.value;
		return c;
	}

	public static final Real One()
	{
		return new Real(1.0);
	}

	/**
	 * Put the opposite value of Real c into d.
	 * 
	 * @param c
	 * @param d
	 * @return Real d.
	 */
	public static final Real Opposite(Real c, Real d)
	{
		d.value = -c.value;
		return d;
	}

	@SuppressWarnings("unchecked")
	public static Real valueOf(String string)
	{
		return new Real(Double.valueOf(string));
	}

	public static final Real Zero()
	{
		return new Real(0.0);
	}

	public Real(double value)
	{
		super(value);
	}

	public Real(int value)
	{
		super((double) value);
	}

	protected Real(Real real)
	{
		super(real.value);
	}

	public Real add(Real number)
	{
		return Add(this, number, this);
	}

	public Real add(Real a, Real b)
	{
		return Add(a, b, this);
	}

	@Override
	public Real clone()
	{
		return new Real(this);
	}

	public Real divide(Real number)
	{
		return Divide(this, number, this);
	}

	public Real divide(Real a, Real b)
	{
		return Divide(a, b, this);
	}

	public void get(Real number)
	{
		super.get(number);
	}

	public Real minus(Real number)
	{
		return Minus(this, number, this);
	}

	public Real minus(Real a, Real b)
	{
		return Minus(a, b, this);
	}

	public Real multiply(Real number)
	{
		return Multiply(this, number, this);
	}

	public Real multiply(Real a, Real b)
	{
		return Multiply(a, b, this);
	}

	public Real one()
	{
		return One();
	}

	public void set(Real number)
	{
		super.set(number);
	}

	public Real zero()
	{
		return Zero();
	}

}

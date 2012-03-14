package org.kernelab.numeric;

import java.io.Serializable;

import org.kernelab.basis.Copieable;
import org.kernelab.basis.Tools;

public class Complex implements AlgebraicNumber<Complex>, Copieable<Complex>,
		Serializable
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -959268614367805436L;

	/**
	 * Add Complex a and b, put the result into c.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return Complex c.
	 */
	public static final Complex Add(Complex a, Complex b, Complex c)
	{
		c.real = a.real + b.real;
		c.imagin = a.imagin + b.imagin;
		return c;
	}

	/**
	 * Divide Complex a by b, put the result into c.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return Complex c.
	 */
	public static final Complex Divide(Complex a, Complex b, Complex c)
	{
		double d = Modulus2(b);
		double r = (a.real * b.real + a.imagin * b.imagin) / d;
		double i = (a.imagin * b.real - a.real * b.imagin) / d;
		c.real = r;
		c.imagin = i;
		return c;
	}

	/**
	 * Divide Complex a by number b, put the result into c.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return Complex c.
	 */
	public static final Complex Divide(Complex a, double b, Complex c)
	{
		c.real = a.real / b;
		c.imagin = a.imagin / b;
		return c;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Complex c = new Complex(0, 0);
		c.normalize();

		Tools.debug(c);
		Tools.debug(c.modulus());
	}

	/**
	 * Minus Complex a by b, put the result into c.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return Complex c.
	 */
	public static final Complex Minus(Complex a, Complex b, Complex c)
	{
		c.real = a.real - b.real;
		c.imagin = a.imagin - b.imagin;
		return c;
	}

	public static final double Modulus(Complex c)
	{
		return Math.sqrt(Modulus2(c));
	}

	public static final double Modulus2(Complex c)
	{
		return c.real * c.real + c.imagin * c.imagin;
	}

	/**
	 * Multiply Complex a and b, put the result into c.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return Complex c.
	 */
	public static final Complex Multiply(Complex a, Complex b, Complex c)
	{
		double r = a.real * b.real - a.imagin * b.imagin;
		double i = a.imagin * b.real + a.real * b.imagin;
		c.real = r;
		c.imagin = i;
		return c;
	}

	/**
	 * Multiply Complex a and double b, put the result into c.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return Complex c.
	 */
	public static final Complex Multiply(Complex a, double b, Complex c)
	{
		c.real = a.real * b;
		c.imagin = a.imagin * b;
		return c;
	}

	public static final Complex One()
	{
		return new Complex(1);
	}

	/**
	 * Put the opposite value of Complex c into d.
	 * 
	 * @param c
	 * @param d
	 * @return Complex d.
	 */
	public static final Complex Opposite(Complex c, Complex d)
	{
		d.real = -c.real;
		d.imagin = -c.imagin;
		return d;
	}

	/**
	 * To scale a Complex number, which means make its modulus to the given one.
	 * Attention that if the given modulus<=0, then the modulus of Complex c
	 * would be used. If still 0 then the result of scaling is 0+0i.
	 * 
	 * @param c
	 *            The Complex number to be normalized.
	 * @param modulus
	 *            The given modulus by which the Complex number would be scaled.
	 * @param d
	 *            The Complex number to hold the result of scaling.
	 * @return The result of scaling.
	 */
	public static final Complex Scale(Complex c, double modulus, Complex d)
	{
		if (modulus > 0) {
			modulus = c.modulus() / modulus;
		} else {
			modulus = c.modulus();
		}

		if (modulus == 0) {
			d.real = d.imagin = 0;
		} else {
			Divide(c, modulus, d);
		}

		return d;
	}

	public static final Complex Zero()
	{
		return new Complex(0);
	}

	public double	real;

	public double	imagin;

	protected Complex(Complex c)
	{
		this.real = c.real;
		this.imagin = c.imagin;
	}

	public Complex(double real)
	{
		this(real, 0);
	}

	public Complex(double real, double imagin)
	{
		this.real = real;
		this.imagin = imagin;
	}

	public Complex add(Complex c)
	{
		return Add(this, c, this);
	}

	public Complex add(Complex a, Complex b)
	{
		return Add(a, b, this);
	}

	@Override
	public Complex clone()
	{
		return new Complex(this);
	}

	public Complex divide(Complex c)
	{
		return Divide(this, c, this);
	}

	public Complex divide(Complex a, Complex b)
	{
		return Divide(a, b, this);
	}

	public Complex divide(Complex c, double d)
	{
		return Divide(c, d, this);
	}

	public Complex divide(double d)
	{
		return Divide(this, d, this);
	}

	@Override
	public boolean equals(Object o)
	{
		boolean is = false;

		if (o instanceof Complex) {
			Complex c = (Complex) o;
			if (this.real == c.real && this.imagin == c.imagin) {
				is = true;
			}
		}

		return is;
	}

	public void get(Complex number)
	{
		number.real = this.real;
		number.imagin = this.imagin;
	}

	public double getImagin()
	{
		return imagin;
	}

	public double getReal()
	{
		return real;
	}

	@Override
	public int hashCode()
	{
		return this.toString().hashCode();
	}

	public Complex minus(Complex c)
	{
		return Minus(this, c, this);
	}

	public Complex minus(Complex a, Complex b)
	{
		return Minus(a, b, this);
	}

	public double modulus()
	{
		return Modulus(this);
	}

	public double modulus2()
	{
		return Modulus2(this);
	}

	public Complex multiply(Complex c)
	{
		return Multiply(this, c, this);
	}

	public Complex multiply(Complex a, Complex b)
	{
		return Multiply(a, b, this);
	}

	public Complex multiply(Complex c, double d)
	{
		return Multiply(c, d, this);
	}

	public Complex multiply(double d)
	{
		return Multiply(this, d, this);
	}

	public Complex normalize()
	{
		return this.scale(1.0);
	}

	public Complex normalize(Complex c)
	{
		return this.scale(c, 1.0);
	}

	public Complex one()
	{
		return One();
	}

	public Complex opposite()
	{
		return Opposite(this, this);
	}

	public Complex opposite(Complex c)
	{
		return Opposite(c, this);
	}

	public Complex scale(Complex c, double modulus)
	{
		return Scale(c, modulus, this);
	}

	public Complex scale(double modulus)
	{
		return Scale(this, modulus, this);
	}

	public void set(Complex number)
	{
		this.real = number.real;
		this.imagin = number.imagin;
	}

	public Complex setImagin(double imagin)
	{
		this.imagin = imagin;
		return this;
	}

	public Complex setParts(double real, double imagin)
	{
		return this.setReal(real).setImagin(imagin);
	}

	public Complex setReal(double real)
	{
		this.real = real;
		return this;
	}

	@Override
	public String toString()
	{
		String string = "";

		if (real == 0 && imagin == 0) {
			string = "0";
		} else {

			if (real != 0) {
				string = String.valueOf(real);
				if (imagin > 0) {
					string += "+";
				}
			}

			if (imagin != 0) {
				string += imagin + "i";
			}

		}

		return string;
	}

	public Complex zero()
	{
		return Zero();
	}
}

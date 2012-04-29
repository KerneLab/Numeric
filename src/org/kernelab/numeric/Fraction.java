package org.kernelab.numeric;

import java.io.Serializable;

import org.kernelab.basis.Copieable;
import org.kernelab.basis.Tools;

public class Fraction extends java.lang.Number implements AlgebraicNumber<Fraction>,
		Comparable<Fraction>, Copieable<Fraction>, Serializable
{

	/**
	 * 
	 */
	private static final long		serialVersionUID	= -401665921564990795L;

	private static final Fraction	ZERO				= new Fraction(0);

	private static final Fraction	ONE					= new Fraction(1);

	public static final Fraction Add(Fraction a, Fraction b, Fraction c)
	{
		long denominator = a.getDenominator() * b.getDenominator();
		long numerator = a.getNumerator() * b.getDenominator() + a.getDenominator()
				* b.getNumerator();

		return ReduceToFraction(c, numerator, denominator);
	}

	public static final Fraction Divide(Fraction a, Fraction b, Fraction c)
	{
		long numerator = a.getNumerator() * b.getDenominator();
		long denominator = a.getDenominator() * b.getNumerator();

		return ReduceToFraction(c, numerator, denominator);
	}

	/**
	 * Determine whether a String is a Fraction.
	 * 
	 * <pre>
	 * Example:
	 * 
	 * the String &quot;1/2&quot; is a Fraction.
	 * the String &quot;.3/-1.7&quot; is a Fraction.
	 * the String &quot;-1.2&quot; is a Fraction.
	 * the String &quot;1/2/3&quot; is not a Fraction.
	 * </pre>
	 * 
	 * @param string
	 *            the Fraction string.
	 * @return <code>TRUE</code> if the string is Fraction otherwise
	 *         <code>FALSE</code>.
	 */
	public static final boolean isFraction(String string)
	{
		return string.matches("^-?\\d*\\.?\\d*(/-?\\d*\\.?\\d*)?$");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Fraction f1 = Fraction.valueOf("26065/9860");
		Fraction f2 = Fraction.valueOf("55141/24650");
		Tools.debug(f1.minus(f1, f2));
	}

	// /**
	// * Reduction of two Fractions to a common denominator. <br />
	// * <b>Attention!</b> This method would reduce these two fraction before
	// the
	// * rest operation.
	// *
	// * <pre>
	// * Example:
	// *
	// * a = 3/8
	// * b = 9/12
	// * =&gt;
	// * a = 3/8
	// * b = 3/4
	// * =&gt;
	// * a = 3/8
	// * b = 6/8
	// * </pre>
	// *
	// * @param a
	// * One of the Fractions.
	// * @param b
	// * One of the Fractions.
	// */
	// public static final void commonDenominator(Fraction a, Fraction b)
	// {
	// a.reduce();
	// b.reduce();
	//
	// if (a.getDenominator() != b.getDenominator()) {
	//
	// int denominator = Fraction.minimumCommonMultiple(a.getDenominator(), b
	// .getDenominator());
	//
	// a.setNumerator(a.getNumerator() * denominator / a.getDenominator());
	// b.setNumerator(b.getNumerator() * denominator / b.getDenominator());
	//
	// a.setDenominator(denominator);
	// b.setDenominator(denominator);
	// }
	// }

	/**
	 * Find the maximum common divisor(mcd) for two Integer number.
	 * 
	 * <pre>
	 * Example:
	 * 
	 * a = 16
	 * b = 24
	 * =&gt;
	 * mcd = 8
	 * </pre>
	 * 
	 * @param a
	 *            One of the two Integer number.
	 * @param b
	 *            One of the two Integer number.
	 * @return The maximum common divisor for these two Integer number.
	 */
	public static final long maximumCommonDivisor(long a, long b)
	{
		long max = 1;

		long range = Math.min(Math.abs(a), Math.abs(b));

		for (long divisor = range; divisor > 1; divisor--) {
			if (a % divisor == 0 && b % divisor == 0) {
				max = divisor;
				break;
			}
		}

		return max;
	}

	/**
	 * Find the minimum common multiple(mcm) for two Integer numbers.
	 * 
	 * <pre>
	 * Example:
	 * 
	 * a = 8
	 * b = 12
	 * =&gt;
	 * mcm = 24
	 * </pre>
	 * 
	 * @param a
	 *            One of the two Integer numbers.
	 * @param b
	 *            One of the two Integer numbers.
	 * @return The minimum common multiple for these two Integer numbers.
	 */
	public static final long minimumCommonMultiple(long a, long b)
	{
		// 5,7
		long min = a * b;

		long mcd = Fraction.maximumCommonDivisor(a, b);

		if (mcd != 1) {

			if (mcd == Math.min(a, b)) {
				// 4,16
				min = Math.max(a, b);

			} else {
				// 8,12
				long common = Math.min(a, b);
				int factor = 1;

				do {
					factor++;
					min = common * factor;
				} while (min % a != 0 || min % b != 0);

			}
		}

		return min;
	}

	public static final Fraction Minus(Fraction a, Fraction b, Fraction c)
	{
		long denominator = a.getDenominator() * b.getDenominator();
		long numerator = a.getNumerator() * b.getDenominator() - a.getDenominator()
				* b.getNumerator();

		return ReduceToFraction(c, numerator, denominator);
	}

	public static final Fraction Multiply(Fraction a, Fraction b, Fraction c)
	{
		long numerator = a.getNumerator() * b.getNumerator();
		long denominator = a.getDenominator() * b.getDenominator();

		return ReduceToFraction(c, numerator, denominator);
	}

	public static final Fraction newInstance(double number)
	{
		return new Fraction(number);
	}

	public static final Fraction newInstance(double numerator, double denominator)
	{
		return new Fraction(new Fraction(numerator), new Fraction(denominator));
	}

	public static final Fraction newInstance(Fraction number)
	{
		return new Fraction(number);
	}

	public static final Fraction newInstance(Fraction numerator, Fraction denominator)
	{
		return new Fraction(numerator, denominator);
	}

	public static final Fraction newInstance(int number)
	{
		return new Fraction(number);
	}

	public static final Fraction newInstance(int numerator, int denominator)
	{
		return new Fraction(numerator, denominator);
	}

	public static final Fraction newInstance(String string)
	{
		return Fraction.valueOf(string);
	}

	public static final Fraction One()
	{
		return new Fraction(ONE);
	}

	/**
	 * Put the opposite value of Fraction {@code in} into Fraction {@code out}.
	 * 
	 * @param in
	 *            The Fraction from which to get the opposite value.
	 * @param out
	 *            The Fraction into which to put the opposite value.
	 * @return Fraction out.
	 */
	public static final Fraction Opposite(Fraction in, Fraction out)
	{
		out.numerator = -in.numerator;
		out.denominator = in.denominator;
		return out;
	}

	/**
	 * Put the reciprocal value of Fraction {@code in} into Fraction {@code out}
	 * .
	 * 
	 * @param in
	 *            The Fraction from which to get the reciprocal value.
	 * @param out
	 *            The Fraction into which to put the reciprocal value.
	 * @return Fraction out.
	 */
	public static final Fraction Reciprocal(Fraction in, Fraction out)
	{
		int numerator = in.denominator;
		int denominator = in.numerator;
		out.numerator = numerator;
		out.denominator = denominator;
		return out;
	}

	/**
	 * Reduce a Fraction so that the maximum common divisor of the numerator and
	 * denominator is 1.
	 * 
	 * <pre>
	 * Example:
	 * 
	 * f = 12/27
	 * =&gt;
	 * f = 4/9
	 * </pre>
	 * 
	 * @param fraction
	 *            The Fraction to be reduced.
	 */
	public static final void Reduce(Fraction fraction)
	{
		int numerator = fraction.getNumerator();
		int denominator = fraction.getDenominator();

		if (numerator == 0) {

			denominator = 1;

		} else {

			long mcd = Fraction.maximumCommonDivisor(numerator, denominator);

			while (mcd != 1) {
				numerator /= mcd;
				denominator /= mcd;
				mcd = Fraction.maximumCommonDivisor(numerator, denominator);
			}
		}

		fraction.setNumerator(numerator);
		fraction.setDenominator(denominator);
	}

	public static final Fraction ReduceToFraction(Fraction fraction, long numerator,
			long denominator)
	{
		if (numerator == 0) {

			denominator = 1;

		} else {

			long mcd = Fraction.maximumCommonDivisor(numerator, denominator);

			while (mcd != 1) {
				numerator /= mcd;
				denominator /= mcd;
				mcd = Fraction.maximumCommonDivisor(numerator, denominator);
			}
		}

		fraction.numerator = (int) numerator;
		fraction.denominator = (int) denominator;

		return fraction;
	}

	/**
	 * Get the value of a fraction string.
	 * 
	 * @param string
	 *            The string of the fraction.
	 * @return The Fraction with certain value.
	 */
	public static final Fraction valueOf(String string)
	{
		Fraction fraction = null;

		string = string.replaceAll(" ", "");

		try {
			fraction = Fraction.newInstance(Double.parseDouble(string));

		} catch (NumberFormatException e) {

			if (string.contains("/")) {

				String[] value = string.split("/");

				fraction = Fraction.newInstance(Double.parseDouble(value[0]),
						Double.parseDouble(value[1]));

			} else {
				throw new NumberFormatException("For input string: " + string);
			}
		}

		return fraction;
	}

	public static final Fraction Zero()
	{
		return new Fraction(ZERO);
	}

	private int	numerator;

	private int	denominator;

	public Fraction(double number)
	{
		int base = 10;

		String string = String.valueOf(number);

		int position = string.length() - string.indexOf('.') - 1;

		denominator = 1;
		for (int i = 0; i < position; i++) {
			denominator *= base;
		}

		numerator = (int) (number * denominator);

		this.reduce();
	}

	protected Fraction(Fraction f)
	{
		this.numerator = f.numerator;
		this.denominator = f.denominator;
	}

	public Fraction(Fraction numerator, Fraction denominator)
	{
		this.setNumerator(numerator.getNumerator() * denominator.getDenominator());
		this.setDenominator(numerator.getDenominator() * denominator.getNumerator());
		this.reduce();
	}

	/**
	 * Create a Fraction of an Integer. <br/>
	 * 
	 * @param number
	 *            The Integer number.
	 */
	public Fraction(int number)
	{
		this.setNumerator(number);
		this.setDenominator(1);
	}

	/**
	 * Create a Fraction. <br />
	 * <b>Attention!</b> The denominator should always be positive. If it's
	 * negative, it would be alternated to positive and change the numerator to
	 * the opposite.
	 * 
	 * @param numerator
	 * @param denominator
	 */
	public Fraction(int numerator, int denominator)
	{
		this.setNumerator(numerator);
		this.setDenominator(denominator);
		this.reduce();
	}

	public Fraction add(Fraction f)
	{
		return add(this, f);
	}

	public Fraction add(Fraction a, Fraction b)
	{
		return Add(a, b, this);
	}

	@Override
	public Fraction clone()
	{
		return new Fraction(this);
	}

	public int compareTo(Fraction f)
	{
		int compare = 0;

		Fraction minus = new Fraction(0);

		minus.minus(this, f);

		if (minus.doubleValue() < 0) {
			compare = -1;
		} else if (minus.doubleValue() > 0) {
			compare = 1;
		}

		return compare;
	}

	public Fraction divide(Fraction f)
	{
		return divide(this, f);
	}

	public Fraction divide(Fraction a, Fraction b)
	{
		return Divide(a, b, this);
	}

	@Override
	public double doubleValue()
	{
		return 1.0 * numerator / denominator;
	}

	@Override
	public boolean equals(Object o)
	{
		boolean equals = false;

		if (o instanceof Fraction) {
			Fraction f = (Fraction) o;
			Fraction minus = new Fraction(0);
			minus.minus(this, f);
			if (minus.doubleValue() == 0.0) {
				equals = true;
			}
		}

		return equals;
	}

	@Override
	public float floatValue()
	{
		return (float) this.doubleValue();
	}

	public void get(Fraction number)
	{
		number.numerator = this.numerator;
		number.denominator = this.denominator;
	}

	public int getDenominator()
	{
		return denominator;
	}

	public int getNumerator()
	{
		return numerator;
	}

	public double getValue()
	{
		return this.doubleValue();
	}

	@Override
	public int intValue()
	{
		return (int) this.doubleValue();
	}

	public boolean isInfinite()
	{
		return Double.isInfinite(this.doubleValue());
	}

	@Override
	public long longValue()
	{
		return (long) this.doubleValue();
	}

	public Fraction minus(Fraction number)
	{
		return minus(this, number);
	}

	public Fraction minus(Fraction a, Fraction b)
	{
		return Minus(a, b, this);
	}

	public Fraction multiply(Fraction f)
	{
		return multiply(this, f);
	}

	public Fraction multiply(Fraction a, Fraction b)
	{
		return Multiply(a, b, this);
	}

	public Fraction one()
	{
		return One();
	}

	public Fraction opposite()
	{
		return opposite(this);
	}

	public Fraction opposite(Fraction fraction)
	{
		return Opposite(fraction, this);
	}

	public Fraction reciprocal()
	{
		return reciprocal(this);
	}

	public Fraction reciprocal(Fraction fraction)
	{
		return Reciprocal(fraction, this);
	}

	public void reduce()
	{
		Fraction.Reduce(this);
	}

	public void set(Fraction number)
	{
		this.numerator = number.numerator;
		this.denominator = number.denominator;
	}

	/**
	 * Set the denominator of the Fraction. <br />
	 * <b>Attention!</b>The denominator should always be positive. If it's
	 * negative, it would be alternated to positive and change the numerator to
	 * the opposite.
	 * 
	 * @param denominator
	 *            The denominator of the fraction.
	 * @return This Fraction.
	 */
	public Fraction setDenominator(int denominator)
	{
		this.denominator = denominator;
		if (this.denominator < 0) {
			this.denominator = -this.denominator;
			this.numerator = -this.numerator;
		}
		return this;
	}

	public Fraction setFraction(int numerator, int denominator)
	{
		return this.setNumerator(numerator).setDenominator(denominator);
	}

	public Fraction setNumerator(int numerator)
	{
		this.numerator = numerator;
		return this;
	}

	@Override
	public String toString()
	{
		this.reduce();

		String string = String.valueOf(numerator);

		if (denominator != 1) {
			string += "/" + denominator;
		}

		return string;
	}

	public Fraction zero()
	{
		return Zero();
	}
}

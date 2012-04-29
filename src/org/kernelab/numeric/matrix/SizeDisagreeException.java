package org.kernelab.numeric.matrix;

public class SizeDisagreeException extends RuntimeException
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5788767013201370221L;

	public SizeDisagreeException(Size a, Size b)
	{
		super("Size Disagree: " + a + " VS " + b);
	}

	public SizeDisagreeException(String msg)
	{
		super(msg);
	}

}

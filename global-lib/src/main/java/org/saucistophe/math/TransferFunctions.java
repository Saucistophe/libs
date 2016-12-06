package org.saucistophe.math;

/**
 Useful transfer functions for dynamysing or smoothing 0-1 values.
 */
public class TransferFunctions
{
	public static float smoothstep(float x)
	{
		x = Math.max(0, Math.min(1, x));
		return x * x * (3 - 2 * x);
	}
	
	public static float smootherstep(float x)
	{
		x = Math.max(0, Math.min(1, x));
		return  x*x*x*(x*(x*6 - 15) + 10);
	}
}

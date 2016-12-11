package org.saucistophe.math;

/**
 Additionnal math utils.
 */
public class MathUtils
{
	/**
	 Clamps the given value between the two bounds.

	 @param value The value to clamp
	 @param min   The min bound
	 @param max   The max bound
	 @return The clamped value.
	 */
	public static float clamp(float value, float min, float max)
	{
		assert (min < max);

		value = Math.max(value, min);
		value = Math.min(value, max);
		return value;
	}

	/**
	 Clamps the given value between the two bounds.

	 @param value The value to clamp
	 @param min   The min bound
	 @param max   The max bound
	 @return The clamped value.
	 */
	public static double clamp(double value, double min, double max)
	{
		assert (min < max);

		value = Math.max(value, min);
		value = Math.min(value, max);
		return value;
	}

	/**
	 Clamps the given value between the two bounds.

	 @param value The value to clamp
	 @param min   The min bound
	 @param max   The max bound
	 @return The clamped value.
	 */
	public static int clamp(int value, int min, int max)
	{
		assert (min < max);

		value = Math.max(value, min);
		value = Math.min(value, max);
		return value;
	}
}

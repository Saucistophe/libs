package org.saucistophe.stats;

import java.util.List;
import java.util.Random;
import org.saucistophe.geometry.twoDimensional.Point2F;

/**
 This class extends the basic Random class, with utility methods.

 @see Random
 */
public class ImprovedRandom extends Random
{
	private long seed;

	/**
	 A shared instance, for when replayability is not a factor.
	 */
	public static final ImprovedRandom instance = new ImprovedRandom();

	public ImprovedRandom()
	{
		super();
	}

	/**
	 Allows to init the generator with a String, for example to make the name
	 of an entity and its random values dependant.

	 @param stringSeed A seed in the form of a String.
	 */
	public ImprovedRandom(String stringSeed)
	{
		super(stringSeed.hashCode());
		this.seed = stringSeed.hashCode();
	}

	/**
	 Allows to init the generator with a long seed.
	 If the seed is null, the regular, truly random, constructor is used.

	 @param seed A seed in the form of a long.
	 */
	public ImprovedRandom(Long seed)
	{
		super();

		if (seed != null)
		{
			setSeed(seed);
		}
	}

	/**
	 @return The next Gaussian ("normally") distributed float value with mean
	 0.0 and standard deviation 1.0 from this random number generator's
	 sequence.

	 @see Random
	 */
	public float nextFloatGaussian()
	{
		return (float) super.nextGaussian();
	}

	/**
	 Returns a random ARGB value, with the alpha value specified.

	 @param alpha The alpha value needed.
	 @return a random ARGB value.
	 */
	public int nextARGB(int alpha)
	{
		return nextInt(0x00FFFFFF + 1) + (alpha << 24);
	}

	/**
	 Returns a random element in the given list.

	 @param <T> The list's element type.
	 @param list The list to take elements from.
	 @return a random element in this list, or null if empty.
	 */
	public <T> T nextListElement(List<T> list)
	{
		if (list.isEmpty())
		{
			return null;
		}
		return list.get(nextInt(list.size()));
	}

	/**
	 @return A point located in the unit square.
	 */
	public Point2F nextPoint2F()
	{
		return new Point2F(nextFloat(), nextFloat());
	}

	/**
	 Resets the random generator, so as to start the random sequence anew.
	 */
	public synchronized void reset()
	{
		setSeed(seed);
	}

	public float nextFloat(float maxValue)
	{
		return nextFloat() * maxValue;
	}

	/**
	 Returns a random RGBA array color, with the alpha specified.

	 @param alpha The alpha of the color to generate.
	 @return The random color, as a RGBA array.
	 */
	public int[] nextRgba(int alpha)
	{
		return new int[]
		{
			nextInt(256), nextInt(256), nextInt(256), alpha
		};
	}
	
	/**
	 Returns a random HSVA array color, with the alpha specified.

	 @param alpha The alpha of the color to generate.
	 @return The random color, as a HSVA array.
	 */
	public float[] nextHsva(float alpha)
	{
		return new float[]
		{
			nextFloat(), nextFloat(), nextFloat(), alpha
		};
	}

	/**
	 Generates a random string with lowercase letters.

	 @param length
	 @return
	 */
	public String nextString(int length)
	{
		String result = "";
		for (int i = 0; i < length; i++)
		{
			char c = (char) ('a' + nextInt('z'-'a'));
			result += c;
		}
		return result;
	}
}

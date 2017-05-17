package org.saucistophe.math;

import java.util.ArrayList;
import java.util.List;

/**
 Computes the best way to spread out N cannons on a machine gun. Currently practical for N lesser than 600.
 */
public class MachineGunAlgorithm
{
	/**
	 Returns the radius of a circle, made of n circles of radius 1.

	 @param n The number of circles.
	 @return The big radius.
	 */
	private static double radius(int n)
	{
		// Handle special cases and accelerate simple cases.
		if (n >= 3)
		{
			return 1 / Math.sin(Math.PI / n);
		} else
		{
			return (n - 1);
		}
	}

	/**
	 Indicates if two circles made of cirles of radius 1, can fit one in another.

	 @param exterior The number of small circles on the exterior circle.
	 @param interior The number of small circles on the interior circle.
	 @return true if they fit.
	 */
	private static boolean fits(int exterior, int interior)
	{
		double exteriorRadius = radius(exterior);
		double interiorRadius = radius(interior);

		return exteriorRadius - 1 >= interiorRadius + 1;
	}

	/**
	 Returns the best repartition of N circles.

	 @param n The number of circles to spread.
	 @return A list of numbers of circles. The Kth number indicates the number of circles on the Kth outer circle.
	 */
	public static List<Integer> bestRepartition(int n)
	{
		if (n <= 6)
		{
			List<Integer> result = new ArrayList<>();
			result.add(n);
			return result;
		}

		//int currentN = n - 1;
		int currentN = (int) ((Math.sqrt((n - 2) * 650 / 777)) * 4);
		int currentM = n - currentN;
		List<Integer> bestRepartition = null;

		do
		{
			List<Integer> candidateRepartition = bestRepartition(currentM);
			if (fits(currentN, candidateRepartition.get(0)))
			{
				bestRepartition = candidateRepartition;
			} else
			{
				bestRepartition.add(0, currentN + 1);
				return bestRepartition;
			}

			currentN--;
			currentM++;
		} while (true);
	}
}

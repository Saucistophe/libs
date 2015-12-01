package org.saucistophe.utils;

import java.util.ArrayList;
import java.util.List;

public class ArrayUtils
{
	/**
	 * Splits a List into as many Lists as needed to have no more than maxNumberOfElements per list.
	 */
	public static <T> List<List<T>> splitToMaxSize(List<T> list, int maxNumberOfElements)
	{
		// First determine the number of lists.
		int n = list.size() / maxNumberOfElements;
		if (list.size() % maxNumberOfElements != 0)
		{
			n++;
		}

		List<List<T>> result = new ArrayList<>();
		for (int i = 0; i < n; i++)
		{
			int maxBound = Math.min((i + 1) * maxNumberOfElements, list.size());
			ArrayList<T> subList = new ArrayList<>();
			subList.addAll(list.subList(i * maxNumberOfElements, maxBound));

			result.add(subList);
		}

		return result;
	}

	/**
	 * Splits a list into N lists.
	 */
	/* public static <T> List<List<T>> splitIntoNLists(List<T> list, int n)
	 * {
	 * return Lists.partition(list, n);
	 * } */
	public static <T> List<List<T>> splitIntoNLists(List<T> list, int n)
	{
		int packetSize = list.size() / n;
		List<List<T>> result = new ArrayList<>();

		for (int i = 0; i < n; i++)
		{
			int maxBound = Math.min((i + 1) * packetSize, list.size());
			ArrayList<T> subList = new ArrayList<>();
			subList.addAll(list.subList(i * packetSize, maxBound));

			result.add(subList);
		}

		return result;
	}
}

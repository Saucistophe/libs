package org.saucistophe.geometry.twoDimensional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.saucistophe.geometry.threeDimensional.Point3F;

/**
 A 2-D tree node implementation. Requires that all Xs and Ys are different between the mapped points.

 */
public class KdTreeNode
{
	/**
	 The point associated to a leaf of the tree, if applicable.
	 */
	public Point3F leafPoint = null;

	/**
	 The parent of the current node.
	 */
	public KdTreeNode parent = null;

	/**
	 The left subtree of the current node.
	 */
	public KdTreeNode leftSubTree;

	/**
	 The right subtree of the current node.
	 */
	public KdTreeNode rightSubTree;

	/**
	 The coordinate on which to split. If 0, x, 1 y and 2 z.
	 */
	public int splitCoordinate;

	/**
	 The median value used to split the current tree.
	 */
	public float medianValue;

	/**
	 Recursively builds a KD tree out of the given points.

	 @param points The points to represent in this tree.
	 @return The root node of the resulting tree.
	 */
	public static KdTreeNode buildTree(List<? extends Point3F> points)
	{
		// First check if the z-coordinate is relevant.
		boolean considerZs = false;
		float initialZ = points.get(0).z;
		for (Point3F point : points)
		{
			if (point.z != initialZ)
			{
				considerZs = true;
			}
		}

		return buildTree(0, points, considerZs);
	}

	/**
	 Recursively builds a KD tree out of the given points.

	 @param splitCoordinate The coordinate on which to split. If 0, x, 1 y and 2 z.
	 @param points The points to represent in this tree.
	 @param considerZs Boolean indicating if the Z value is relevant, or dismissed since it's constant.
	 @return The root node of the resulting tree.
	 */
	protected static KdTreeNode buildTree(int splitCoordinate, List<? extends Point3F> points, boolean considerZs)
	{
		assert (splitCoordinate >= 0 && splitCoordinate <= 2);

		// If there is no point left, bail out.
		{
			if (points.isEmpty())
			{
				return null;
			}
		}

		KdTreeNode result = new KdTreeNode();

		// If only one point is left, it's a leaf.
		if (points.size() == 1)
		{
			result.leafPoint = points.get(0);
			return result;
		}

		// If not, compute the median, according to the desired metric (x, y or z).
		Function<Point3F, Float> relevantMetric;
		if (splitCoordinate == 0)
		{
			relevantMetric = p -> p.x;
		} else if (splitCoordinate == 1)
		{
			relevantMetric = p -> p.y;
		} else
		{
			relevantMetric = p -> p.z;
		}

		// Get a list sorted using this metric.
		List<Point3F> sortedPoints = points.stream()
				.sorted((p1, p2) -> Float.compare(relevantMetric.apply(p1), relevantMetric.apply(p2)))
				.collect(Collectors.toCollection(ArrayList<Point3F>::new));

		// Get the median value.
		// If there is an uneven number of points:
		if (sortedPoints.size() % 2 == 1)
		{
			// The median value is the value of the middle point.
			Point3F middlePoint = sortedPoints.get((sortedPoints.size() + 1) / 2 - 1);
			result.medianValue = relevantMetric.apply(middlePoint);
		} else
		// If there is an even number of points:
		{
			// The median value is the average of the two middle points.
			Point3F lowerPoint = sortedPoints.get(sortedPoints.size() / 2 - 1);
			Point3F upperPoint = sortedPoints.get(sortedPoints.size() / 2);

			result.medianValue = (relevantMetric.apply(lowerPoint) + relevantMetric.apply(upperPoint)) / 2;
		}

		result.splitCoordinate = splitCoordinate;

		// Change the coordinate along which we split. Consider Zs only if relevant.
		if (considerZs)
		{
			splitCoordinate = (splitCoordinate + 1) % 3;
		} else
		{
			splitCoordinate = (splitCoordinate + 1) % 2;
		}

		// Create the subtrees with points below and above this metric.
		result.leftSubTree = buildTree(splitCoordinate, sortedPoints.subList(0, sortedPoints.size() / 2), considerZs);
		result.rightSubTree = buildTree(splitCoordinate, sortedPoints.subList(sortedPoints.size() / 2, sortedPoints.size()), considerZs);

		// Save the parent as such.
		result.leftSubTree.parent = result;
		result.rightSubTree.parent = result;

		return result;
	}

	/**
	 Finds the point in the diagram, closest to the given point. It corresponds to the deepest leaf found when browsing
	 the tree, not the actual closest point.

	 @param targetPoint The point we are looking for.
	 @return The closest point.
	 */
	protected KdTreeNode getNaiveClosestNode(Point3F targetPoint)
	{
		// If we're at a leaf, bingo.
		if (leafPoint != null)
		{
			assert (leftSubTree == null && rightSubTree == null);
			return this;
		}

		if (splitCoordinate == 0 && targetPoint.x < medianValue
				|| splitCoordinate == 1 && targetPoint.y < medianValue
				|| splitCoordinate == 2 && targetPoint.z < medianValue)
		{
			return leftSubTree.getNaiveClosestNode(targetPoint);
		} else
		{
			return rightSubTree.getNaiveClosestNode(targetPoint);
		}
	}

	/**
	 Finds the point of the tree closest to the given point.

	 @param targetPoint The targeted point.
	 @return The point of the graph that's closest to the target point.
	 */
	public Point3F getClosestPoint(Point3F targetPoint)
	{
		// Begin at the bottom of the tree, at the K-closest point.
		KdTreeNode currentNode = getNaiveClosestNode(targetPoint);
		Point3F bestPoint = currentNode.leafPoint;

		float bestSquaredDistance = currentNode.leafPoint.squaredDistanceTo(targetPoint);

		// While we can go upwards
		while (currentNode.parent != null)
		{
			currentNode = currentNode.parent;

			// Compute the distance of the target point, to the median line of the current node.
			float distanceToMedian;
			if (currentNode.splitCoordinate == 0)
			{
				distanceToMedian = currentNode.medianValue - targetPoint.x;
			} else if (currentNode.splitCoordinate == 1)
			{
				distanceToMedian = currentNode.medianValue - targetPoint.y;
			} else
			{
				distanceToMedian = currentNode.medianValue - targetPoint.z;
			}

			// If that distance is too big, the other side of the node won't be able to conceal a better
			// point; then bail out.
			if (distanceToMedian * distanceToMedian < bestSquaredDistance)
			{
				KdTreeNode otherNode;

				if (currentNode.splitCoordinate == 0 && targetPoint.x < currentNode.medianValue
						|| currentNode.splitCoordinate == 1 && targetPoint.y < currentNode.medianValue
						|| currentNode.splitCoordinate == 2 && targetPoint.z < currentNode.medianValue)
				{
					otherNode = currentNode.rightSubTree;
				} else
				{
					otherNode = currentNode.leftSubTree;
				}

				Point3F newBestPoint = otherNode.findLocalClosestPoint(targetPoint, bestPoint, bestSquaredDistance);
				if (newBestPoint != null)
				{
					float newSquaredDistance = newBestPoint.squaredDistanceTo(targetPoint);
					if (newSquaredDistance < bestSquaredDistance)
					{
						bestSquaredDistance = newSquaredDistance;
						bestPoint = newBestPoint;
					}
				}
			}
		}

		return bestPoint;
	}

	/**
	 Finds the points of the tree close enough to the given point.

	 @param targetPoint The targeted point.
	 @param squaredRange The square of the search range.
	 @return The points of the graph that are within the given range of the given point.
	 */
	public List<Point3F> rangeSearch(Point3F targetPoint, float squaredRange)
	{
		List<Point3F> result = new ArrayList<>();

		// If we're at a leaf, return the point if it's OK.
		if (leafPoint != null)
		{
			assert (leftSubTree == null && rightSubTree == null);
			if (leafPoint.squaredDistanceTo(targetPoint) < squaredRange)
			{
				result.add(leafPoint);
			}
			return result;
		}

		// If it's not a leaf, descend into the correct side, and the other if candidate.
		// TODO to a function, and use it in this whole file.
		boolean leftIsBest = (splitCoordinate == 0 && targetPoint.x < medianValue
				|| splitCoordinate == 1 && targetPoint.y < medianValue
				|| splitCoordinate == 2 && targetPoint.z < medianValue);

		if (leftIsBest)
		{
			result.addAll(leftSubTree.rangeSearch(targetPoint, squaredRange));
		} else
		{
			result.addAll(rightSubTree.rangeSearch(targetPoint, squaredRange));
		}

		float squaredDistanceToMedian;
		switch (splitCoordinate)
		{
			case 0:
				squaredDistanceToMedian = targetPoint.x - medianValue;
				break;
			case 1:
				squaredDistanceToMedian = targetPoint.y - medianValue;
				break;
			case 2:
				squaredDistanceToMedian = targetPoint.z - medianValue;
				break;
			default:
				squaredDistanceToMedian = 0f;
		}
		squaredDistanceToMedian *= squaredDistanceToMedian;

		if (squaredDistanceToMedian < squaredRange)
		{
			if (leftIsBest)
			{
				result.addAll(rightSubTree.rangeSearch(targetPoint, squaredRange));
			} else
			{
				result.addAll(leftSubTree.rangeSearch(targetPoint, squaredRange));
			}
		}

		return result;

	}

	/**
	 Finds the point of the diagram really closest to the target point, not just K-closest. Goes only downward.

	 @param targetPoint The target point.
	 @param bestPoint The best candidate so far.
	 @param bestSquaredDistance The distance of the current best point to the target point, squared.
	 @return The best match.
	 */
	private Point3F findLocalClosestPoint(Point3F targetPoint, Point3F bestPoint, float bestSquaredDistance)
	{
		// If we're at a leaf, check if this candidate is right and return it.
		if (leafPoint != null)
		{
			return leafPoint;
		}

		// For regular nodes; compute the distance of the target point to the median of the node.
		float distanceToMedian;
		if (splitCoordinate == 0)
		{
			distanceToMedian = medianValue - targetPoint.x;
		} else if (splitCoordinate == 1)
		{
			distanceToMedian = medianValue - targetPoint.y;
		} else
		{
			distanceToMedian = medianValue - targetPoint.z;
		}

		// If that distance is too big, the other side of the node won't be able to conceal a better
		// point:
		if (distanceToMedian * distanceToMedian > bestSquaredDistance)
		{
			// Return only the "right" side of the tree; the side where the target point can possibly lie.
			if (splitCoordinate == 0 && targetPoint.x < medianValue
					|| splitCoordinate == 1 && targetPoint.y < medianValue
					|| splitCoordinate == 2 && targetPoint.z < medianValue)
			{
				return leftSubTree.findLocalClosestPoint(targetPoint, bestPoint, bestSquaredDistance);
			} else
			{
				return rightSubTree.findLocalClosestPoint(targetPoint, bestPoint, bestSquaredDistance);
			}
		} // If the median is close enough to hide a good value beyond it:
		else
		{
			// Return the best of the two subtrees.
			Point3F leftCandidate = leftSubTree.findLocalClosestPoint(targetPoint, bestPoint, bestSquaredDistance);
			Point3F rightCandidate = rightSubTree.findLocalClosestPoint(targetPoint, bestPoint, bestSquaredDistance);

			// Return the best one, or null if none is better than the current value.
			if (leftCandidate == null)
			{
				return rightCandidate;
			} else if (rightCandidate == null)
			{
				return leftCandidate;
			} else
			{
				float leftSquaredDistance = leftCandidate.squaredDistanceTo(targetPoint);
				float rightSquaredDistance = rightCandidate.squaredDistanceTo(targetPoint);

				if (leftSquaredDistance < bestSquaredDistance && leftSquaredDistance < rightSquaredDistance)
				{
					return leftCandidate;
				} else if (rightSquaredDistance < bestSquaredDistance && rightSquaredDistance < leftSquaredDistance)
				{
					return rightCandidate;
				} else
				{
					return null;
				}
			}
		}
	}

	@Override
	public String toString()
	{
		if (leafPoint != null)
		{
			return leafPoint.toString();
		} else
		{
			return "[" + leftSubTree + ", " + rightSubTree + "]";
		}
	}
}

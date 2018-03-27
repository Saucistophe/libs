package org.saucistophe.geometry.threeDimensional;

import java.util.List;
import org.saucistophe.patterns.Pair;

/**

 @author Stophe
 */
public class Vector3i implements Cloneable
{
	public int x = 0;
	public int y = 0;
	public int z = 0;

	/**
	 Default constructor.
	 */
	public Vector3i()
	{
	}

	/**
	 Copy constructor.

	 @param original The vector to copy.
	 */
	public Vector3i(Vector3i original)
	{
		this.x = original.x;
		this.y = original.y;
		this.z = original.z;
	}

	public Vector3i(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 Returns the edge shared with the second triangle.

	 @param t2 An adjacent triangle.
	 @return The common edge *as expressed in this triangle*.
	 */
	public int[] getCommonEdge(Vector3i t2)
	{
		if (!t2.contains(x))
		{
			return new int[]
			{
				y, z
			};
		}
		else if (!t2.contains(y))
		{
			return new int[]
			{
				z, x
			};
		}
		else
		{
			return new int[]
			{
				x, y
			};
		}
	}

	/**
	 Takes another triangle, flips both, and returns the two new triangles.
	 The resulting triangles may not be topoligcally the same; especially, one may be inverted.

	 @param that The adjacent triangle to flip.
	 @return A pair of flipped triangles.
	 */
	public Pair<Vector3i, Vector3i> getFlippedTriangles(Vector3i that)
	{
		int[] sharedEdge = this.getCommonEdge(that);
		int oppositeVertex = that.getOppositeVertex(this);

		Vector3i flipped1 = this.replace(sharedEdge[0], oppositeVertex);
		Vector3i flipped2 = this.replace(sharedEdge[1], oppositeVertex);
		return new Pair<>(flipped1, flipped2);
	}

	/**
	 If the triangle contains the given point, it returns the opposite ones.
	 Else, it returns null.
	 */
	public int[] getOppositeEdge(int a)
	{
		if (x == a)
		{
			return new int[]
			{
				y, z
			};
		}
		else if (y == a)
		{
			return new int[]
			{
				x, z
			};
		}
		else if (z == a)
		{
			return new int[]
			{
				x, y
			};
		}
		return null;
	}

	/**
	 @param that The triangle sharing the common edge.
	 @return Among this triangle, the index of the vertex that is not on the common edge.
	 */
	public int getOppositeVertex(Vector3i that)
	{
		if ((this.x) != (that.x)
			&& (this.x) != (that.y)
			&& (this.x) != (that.z))
		{
			return this.x;
		}
		else if ((this.y) != (that.x)
			&& (this.y) != (that.y)
			&& (this.y) != (that.z))
		{
			return this.y;
		}
		else if ((this.z) != (that.x)
			&& (this.z) != (that.y)
			&& (this.z) != (that.z))
		{
			return this.z;
		}
		else
		{
			assert false : "" + this + ", " + that;
			return -1;
		}
	}

	/**
	 If the triangle contains the given points, it returns the third one.
	 Else, it returns -1.
	 */
	public int getThirdPoint(int a, int b)
	{
		if (x == a)
		{
			if (y == b)
			{
				return z;
			}
			else if (z == b)
			{
				return y;
			}
		}
		else if (y == a)
		{
			if (x == b)
			{
				return z;
			}
			else if (z == b)
			{
				return x;
			}
		}
		else if (z == a)
		{
			if (x == b)
			{
				return y;
			}
			else if (y == b)
			{
				return x;
			}
		}
		return -1;
	}

	/**
	 If the triangle contains the given points, it returns the third one.
	 Else, it returns -1.
	 */
	public int getThirdPoint(int edge[])
	{
		return getThirdPoint(edge[0], edge[1]);
	}

	/** Returns an array of all points. */
	public static int[] toArray(List<Vector3i> triangles)
	{
		int index = 0;
		int result[] = new int[triangles.size() * 3];
		for (Vector3i triangle : triangles)
		{
			result[index++] = triangle.x;
			result[index++] = triangle.y;
			result[index++] = triangle.z;
		}

		return result;
	}

	/**
	 Sets the values of this triangle to the taht of the given triangle.

	 @param that The triangle to copy.
	 @return this.
	 */
	public Vector3i set(Vector3i that)
	{
		this.x = that.x;
		this.y = that.y;
		this.z = that.z;

		return this;
	}

	public boolean contains(int i)
	{
		return x == i || y == i || z == i;
	}

	@Override
	public String toString()
	{
		return "[" + x + ',' + y + ',' + z + ']';
	}

	public Vector3i add(int offset)
	{
		return new Vector3i(this).addLocal(offset);
	}

	public Vector3i add(Vector3i offset)
	{
		return new Vector3i(this).addLocal(offset);
	}
		
	public Vector3i add(int i, int j, int k)
	{
		return new Vector3i(this).addLocal(i,j,k);
	}

	public Vector3i addLocal(int offset)
	{
		return addLocal(offset,offset,offset);
	}
	
	public Vector3i addLocal(Vector3i offset)
	{
		return addLocal(offset.x,offset.y,offset.z);
	}

	public Vector3i addLocal(int i, int j, int k)
	{
		x += i;
		y += j;
		z += k;

		return this;
	}
	
	public Vector3i subtract(int offset)
	{
		return new Vector3i(this).subtractLocal(offset);
	}

	public Vector3i subtract(Vector3i offset)
	{
		return new Vector3i(this).subtractLocal(offset);
	}
		
	public Vector3i subtract(int i, int j, int k)
	{
		return new Vector3i(this).subtractLocal(i,j,k);
	}

	public Vector3i subtractLocal(int offset)
	{
		return subtractLocal(offset,offset,offset);
	}
	
	public Vector3i subtractLocal(Vector3i offset)
	{
		return subtractLocal(offset.x,offset.y,offset.z);
	}

	public Vector3i subtractLocal(int i, int j, int k)
	{
		x -= i;
		y -= j;
		z -= k;

		return this;
	}

	public Vector3i multiply(int offset)
	{
		return new Vector3i(this).multiplyLocal(offset);
	}
		
	public Vector3i multiplyLocal(int offset)
	{
		x *= offset;
		y *= offset;
		z *= offset;
		return this;
	}
		
	/**
	 Replace all occurrences of i by newVertex, in a new vector.

	 @param i The index to replace.
	 @param newVertex Its replacement.
	 @return The new vector, with the changes made.
	 */
	public Vector3i replace(int i, int newVertex)
	{
		Vector3i result = new Vector3i(this);
		return result.replaceLocal(i, newVertex);
	}

	/**
	 Replace all occurrences of i by newVertex.

	 @param i The index to replace.
	 @param newVertex Its replacement.
	 @return This.
	 */
	public Vector3i replaceLocal(int i, int newVertex)
	{
		if (x == i)
		{
			x = newVertex;
		}
		if (y == i)
		{
			y = newVertex;
		}
		if (z == i)
		{
			z = newVertex;
		}

		return this;
	}

	/**
	 Puts the given vertex first.

	 @param firstVertex the vertex to put first (x).
	 @return A reordered copy of this triangle.
	 */
	public Vector3i reorder(int firstVertex)
	{
		Vector3i result = new Vector3i(this);
		result.reorderLocal(firstVertex);

		return result;
	}

	/**
	 Puts the given vertex first.

	 @param firstVertex the vertex to put first (x).
	 */
	public void reorderLocal(int firstVertex)
	{
		if (y == firstVertex)
		{
			y = z;
			z = x;
			x = firstVertex;
		}
		else if (z == firstVertex)
		{
			z = y;
			y = x;
			x = firstVertex;
		}
	}

	/**
	 Inverts the order of the integers. Will switch X Y Z to Z Y X.
     @return This.
	 */
	public Vector3i invertLocal()
	{
		int temp = x;
		x = z;
		z = temp;

		return this;
	}
    
	@Override
	public int hashCode()
	{
		int hash = 5;
		hash = 97 * hash + this.x;
		hash = 97 * hash + this.y;
		hash = 97 * hash + this.z;
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final Vector3i other = (Vector3i) obj;
		if (this.x != other.x)
		{
			return false;
		}
		if (this.y != other.y)
		{
			return false;
		}
		if (this.z != other.z)
		{
			return false;
		}
		return true;
	}
}

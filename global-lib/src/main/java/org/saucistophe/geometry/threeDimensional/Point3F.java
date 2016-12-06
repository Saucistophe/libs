package org.saucistophe.geometry.threeDimensional;

/**
 A class describing 3D float-precision points.
 */
public class Point3F
{
	public float x;
	public float y;
	public float z;

	/**
	 Default constructor.
	 */
	public Point3F()
	{
	}

	/**
	 Constructor using fields.

	 @param x The point's X value.
	 @param y The point's Y value.
	 @param z The point's Z value.
	 */
	public Point3F(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 Returns the squared length from this point to the given coordinates.

	 @param x The target point's x
	 @param y The target point's y
	 @param z The target point's z
	 @return The squared distance.
	 */
	public float squaredDistanceTo(float x, float y, float z)
	{
		return (x - this.x) * (x - this.x) + (y - this.y) * (y - this.y) + (z - this.z) * (z - this.z);
	}

	/**
	 Returns the squared length from this point to the given coordinates.

	 @param otherPoint The target point.
	 @return The squared distance between the two.
	 */
	public float squaredDistanceTo(Point3F otherPoint)
	{
		return squaredDistanceTo(otherPoint.x, otherPoint.y, otherPoint.z);
	}
	
	/**
	 Returns the length from this point to the given coordinates.

	 @param x The target point's x
	 @param y The target point's y
	 @param z The target point's z
	 @return The distance between the two.
	 */
	public float distanceTo(float x, float y, float z)
	{
		return (float) Math.sqrt(squaredDistanceTo(x, y, z));
	}
	
	/**
	 Returns the length from this point to the given coordinates.

	 @param otherPoint The target point.
	 @return The distance between the two.
	 */
	public float distanceTo(Point3F otherPoint)
	{
		return distanceTo(otherPoint.x, otherPoint.y, otherPoint.z);
	}

	/**
	 Returns the Manhattan length from this point to the given coordinates.

	 @param x The target point's x
	 @param y The target point's y
	 @param z The target point's z
	 @return The Manhattan distance.
	 */
	public float manhattanDistanceTo(float x, float y, float z)
	{
		return Math.abs(x - this.x) + Math.abs(y - this.y) + Math.abs(z - this.z);
	}

	/**
	 Returns the Manhattan length from this point to the given coordinates.

	 @param otherPoint The target point.
	 @return The Manhattan distance between the two.
	 */
	public float manhattanDistanceTo(Point3F otherPoint)
	{
		return manhattanDistanceTo(otherPoint.x, otherPoint.y, otherPoint.z);
	}

	@Override
	public String toString()
	{
		return "(" + x + ", " + y + ", " + z + ")";
	}

	public void addLocal(Point3F offset)
	{
		x += offset.x;
		y += offset.y;
		z += offset.z;
	}
}

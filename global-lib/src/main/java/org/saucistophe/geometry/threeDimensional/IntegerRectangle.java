package org.saucistophe.geometry.threeDimensional;

/**
 A rectangle with integer coordinates.
 */
public class IntegerRectangle implements Cloneable
{
	/**
	The origin of thie rectangle (The leftmost, bottom and back corner).
	*/
	public final Vector3i origin;
	
	/**
	The size of the rectangle, expressed in blocks.
	*/
	public final Vector3i size;

	public IntegerRectangle(Vector3i origin, Vector3i size)
	{
		this.origin = origin;
		this.size = size;
	}
}

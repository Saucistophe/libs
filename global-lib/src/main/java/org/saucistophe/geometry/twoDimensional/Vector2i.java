package org.saucistophe.geometry.twoDimensional;

public class Vector2i implements Cloneable
{
	public int x = 0;
	public int y = 0;

	/**
	 Default constructor.
	 */
	public Vector2i()
	{
	}

	/**
	 Copy constructor.

	 @param original The vector to copy.
	 */
	public Vector2i(Vector2i original)
	{
		this.x = original.x;
		this.y = original.y;
	}

	public Vector2i(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 Sets the values of this instance to that of the given instance.

	 @param that The value to copy.
	 @return this.
	 */
	public Vector2i set(Vector2i that)
	{
		this.x = that.x;
		this.y = that.y;

		return this;
	}

	public boolean contains(int i)
	{
		return x == i || y == i;
	}

	@Override
	public String toString()
	{
		return "[" + x + ',' + y + ']';
	}

	public Vector2i add(int offset)
	{
		return new Vector2i(x + offset, y + offset);
	}

	public Vector2i addLocal(int offset)
	{
		x += offset;
		y += offset;

		return this;
	}

	public Vector2i addLocal(int i, int j)
	{
		x += i;
		y += j;

		return this;
	}

	/**
	 Inverts the order of the integers. Will switch X Y to Y X.
     @return This.
	 */
	public Vector2i invertLocal()
	{
		int temp = x;
		x = y;
		y = temp;

		return this;
	}
    
	@Override
	public int hashCode()
	{
		int hash = 5;
		hash = 97 * hash + this.x;
		hash = 97 * hash + this.y;
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
		final Vector2i other = (Vector2i) obj;
		if (this.x != other.x)
		{
			return false;
		}
		if (this.y != other.y)
		{
			return false;
		}
		return true;
	}
}

package org.saucistophe.geometry.twoDimensional;

import java.util.function.BinaryOperator;

/**
 A class describing 2D float-precision points.
 */
public class Point2F
{
	public float x;
	public float y;

	/**
	 Default constructor.
	 */
	public Point2F()
	{
	}

	/**
	 Copy constructor.

	 @param otherPoint The point to copy.
	 */
	public Point2F(Point2F otherPoint)
	{
		this.x = otherPoint.x;
		this.y = otherPoint.y;
	}

	/**
	 Constructor using fields.

	 @param x The point's X value.
	 @param y The point's Y value.
	 */
	public Point2F(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 Returns the squared length from this point to the given coordinates.

	 @param x The target point's x
	 @param y The target point's y
	 @return The squared distance.
	 */
	public float squaredDistanceTo(float x, float y)
	{
		return (x - this.x) * (x - this.x) + (y - this.y) * (y - this.y);
	}

	/**
	 Returns the squared length from this point to the given coordinates.

	 @param otherPoint The target point.
	 @return The squared distance between the two.
	 */
	public float squaredDistanceTo(Point2F otherPoint)
	{
		return squaredDistanceTo(otherPoint.x, otherPoint.y);
	}

	/**
	 Applies an operator on this vector.

	 @param operator The operator to apply.
	 @param otherPoint The operand.
	 @return This.
	 */
	private Point2F operateLocal(BinaryOperator<Float> operator, Point2F otherPoint)
	{
		this.x = operator.apply(this.x, otherPoint.x);
		this.y = operator.apply(this.y, otherPoint.y);

		return this;
	}

	/**
	Applies an operator on this vector, but as a new instance.

	@param operator The operator to apply.
	@param otherPoint The operand.
	@return The created instance.
	*/
	private Point2F operate(BinaryOperator<Float> operator, Point2F otherPoint)
	{
		Point2F result = new Point2F(this);
		return result.operateLocal(operator, otherPoint);
	}

	public Point2F addLocal(Point2F otherPoint)
	{
		return operateLocal(Float::sum, otherPoint);
	}

	public Point2F subtractLocal(Point2F otherPoint)
	{
		return operateLocal((f1, f2) -> f1 - f2, otherPoint);
	}

	public Point2F subtract(Point2F otherPoint)
	{
		return operate((f1, f2) -> f1 - f2, otherPoint);
	}

	public Point2F multLocal(Point2F otherPoint)
	{
		return operateLocal((f1, f2) -> f1 * f2, otherPoint);
	}

	public Point2F mult(Point2F otherPoint)
	{
		return operate((f1, f2) -> f1 * f2, otherPoint);
	}

	public Point2F multLocal(float value)
	{
		this.x *= value;
		this.y *= value;

		return this;
	}

	public float getNorm()
	{
		return (float) Math.sqrt(squaredDistanceTo(0f, 0f));
	}

	public float dot(Point2F otherPoint)
	{
		return x * otherPoint.x + y * otherPoint.y;
	}

	public float cross(Point2F otherPoint)
	{
		return x * otherPoint.y - y * otherPoint.x;
	}

	@Override
	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}
}

package org.saucistophe.utils;

import java.awt.Color;
import java.util.List;
import org.apache.commons.math3.util.FastMath;
import org.saucistophe.geometry.twoDimensional.Point2F;

/**
 Utils class to process colors.
 */
public class ColorUtils
{
	/**
	 Transforms an ARGB value to a Color.

	 @param argb The ARGB value to transform.
	 @return The color.
	 */
	public static Color argbToColor(int argb)
	{
		int a = (argb & 0xFF000000) >>> 24;
		int r = (argb & 0x00FF0000) >>> 16;
		int g = (argb & 0x0000FF00) >>> 8;
		int b = (argb & 0x000000FF);

		return new Color(r, g, b, a);
	}

	/**
	 Transforms a [0,1] float value to a ARGB grey level value.

	 @param floatValue The value to convert.
	 @return A grey color between 0xFF000000 and 0xFFFFFFFF.
	 */
	public static int floatToArgbGrey(float floatValue)
	{
		int intValue = (int) (floatValue * 255) & 255;

		int result = 0xFF000000;
		result |= intValue;
		result |= intValue << 8;
		result |= intValue << 16;
		return result;
	}

	/**
	 Turns an HSVA color (as an array of floats) into a RGBA color.

	 @param hsva The HSVA color array, with each float 0-1.
	 @return The RGBA color.
	 */
	public static int[] hsvaToRgba(float[] hsva)
	{
		int[] rgba = new int[4];

		int rgbaInt = Color.HSBtoRGB(hsva[0], hsva[1], hsva[2]);

		rgba[0] = (rgbaInt >> 16) & 255;
		rgba[1] = (rgbaInt >> 8) & 255;
		rgba[2] = (rgbaInt) & 255;
		rgba[3] = (int) (hsva[3] * 255);

		return rgba;
	}

	/**
	 Turns an RGBA color (as an array of int) into a HSVA color.

	 @param rgba The RGBA color array, with each int 0-255.
	 @return The HSVA color.
	 */
	public static float[] rgbaToHsva(int[] rgba)
	{
		float[] hsva = new float[4];

		Color.RGBtoHSB(rgba[0], rgba[1], rgba[2], hsva);

		hsva[3] = rgba[3] / 255f;

		return hsva;
	}

	/**
	 Turns an RGBA color (as an hexadecimal int) into a HSVA color.

	 @param rgba The 0xRRGGBBAA color int.
	 @return The HSVA color.
	 */
	public static float[] rgbaToHsva(int rgba)
	{
		return rgbaToHsva(new int[]
		{
			(rgba >> 24) & 255, (rgba >> 16) & 255, (rgba >> 8) & 255, (rgba) & 255
		});
	}

	/**
	 Transforms a part of a vector (-1,1) to a normal map part (0-255).

	 @param vectorValue The [1,1] value to transform.
	 @return The [0-255] output.
	 */
	public static int vectorToNormal(double vectorValue)
	{
		return (int) ((vectorValue / 2 + 0.5) * 255);
	}

	/**
	 Surimpose a color on another, according to their alphas.

	 @param background The background HSVA color.
	 @param foreground The foreground HSVA color.
	 @return The surimposition of the foreground on the background.
	 */
	public static float[] surimposeHsvaColors(float[] background, float[] foreground)
	{
		float result[] = new float[4];

		float backgroundAlpha = background[3];
		float foregroundAlpha = foreground[3];

		int backgroundRgb = Color.HSBtoRGB(background[0], background[1], background[2]);
		int foregroundRgb = Color.HSBtoRGB(foreground[0], foreground[1], foreground[2]);

		int r = (int) (((backgroundRgb >> 16) & 255) * (1 - foregroundAlpha) * backgroundAlpha + ((foregroundRgb >> 16) & 255) * foregroundAlpha);
		int g = (int) (((backgroundRgb >> 8) & 255) * (1 - foregroundAlpha) * backgroundAlpha + ((foregroundRgb >> 8) & 255) * foregroundAlpha);
		int b = (int) (((backgroundRgb) & 255) * (1 - foregroundAlpha) * backgroundAlpha + ((foregroundRgb) & 255) * foregroundAlpha);

		Color.RGBtoHSB(r, g, b, result);

		float newAlpha = foregroundAlpha + backgroundAlpha * (1 - foregroundAlpha);
		result[3] = newAlpha;

		return result;
	}

	/**
	 Merges a list of HSVA colors, returning the average color.

	 @param colorsToMerge The list of colors to merge.
	 @return The average of the given colors.
	 */
	public static float[] mergeHsvColors(List<float[]> colorsToMerge)
	{
		//First off, if there's only one color, return it verbatim.
		if (colorsToMerge.size() == 1)
		{
			return colorsToMerge.get(0);
		}

		Point2F meanHsPoint = new Point2F(0, 0);

		float result[] =
		{
			0f, 0f, 0f, 0f
		};

		// Divide by the number of points to get the average.
		int numberOfPoints = colorsToMerge.size();

		for (float[] color : colorsToMerge)
		{
			// Compute average hue and saturation by interpolating vectors in HSV space.
			meanHsPoint.x += color[1] * FastMath.cos(color[0] * FastMath.PI * 2);
			meanHsPoint.y += color[1] * FastMath.sin(color[0] * FastMath.PI * 2);

			// Copy value and alpha as a simple average.
			//result[2] += color[2];
			result[2] = Math.max(result[2], color[2]);
			result[3] += color[3];
		}

		meanHsPoint.multLocal(1f / numberOfPoints);

		// Compute the average value.
		result[0] = (float) (Math.atan2(meanHsPoint.y, meanHsPoint.x) / FastMath.PI / 2);
		if (result[0] < 0)
		{
			result[0] = 1 + result[0];
		}
		result[1] = meanHsPoint.getNorm();
		//result[2] /= numberOfPoints;
		result[3] /= numberOfPoints;

		return result;
	}

	/**
	 Returns the actual (as perceived by the eye) luminance of the color.

	 @param rgba The RGBA color.
	 @return Its (0-255) luminance.
	 */
	public static int getLuminance(int[] rgba)
	{
		return (rgba[0] + rgba[0] + rgba[1] + rgba[1] + rgba[1] + rgba[2]) * rgba[3] / 255 / 6;
	}
}

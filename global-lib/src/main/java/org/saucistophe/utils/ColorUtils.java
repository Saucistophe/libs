package org.saucistophe.utils;

import java.awt.Color;
import java.util.List;
import org.apache.commons.math3.util.FastMath;
import org.saucistophe.geometry.twoDimensional.Point2F;
import org.saucistophe.math.MathUtils;

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
	 Note that the alpha value is optionnal; in which case a HSV value will turn to RGB.

	 @param hsva The HSVA color array, with each float 0-1.
	 @return The RGBA color.
	 */
	public static int[] hsvaToRgba(float[] hsva)
	{
		int[] rgba = new int[hsva.length];

		int rgbaInt = Color.HSBtoRGB(hsva[0], hsva[1], hsva[2]);

		rgba[0] = (rgbaInt >> 16) & 255;
		rgba[1] = (rgbaInt >> 8) & 255;
		rgba[2] = (rgbaInt) & 255;
		if (rgba.length >= 4)
			rgba[3] = (int) (hsva[3] * 255);

		return rgba;
	}

	/**
	 Turns an RGBA color (as an array of int) into a HSVA color.
	 Note that the alpha value is optionnal; in which case a RGB value will turn to HSV.

	 @param rgba The RGBA color array, with each int 0-255.
	 @return The HSVA color.
	 */
	public static float[] rgbaToHsva(int[] rgba)
	{
		float[] hsva = new float[rgba.length];

		Color.RGBtoHSB(rgba[0], rgba[1], rgba[2], hsva);

		if (hsva.length >= 4)
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
	 Each point's value will make it contribute more or less to the result color.
	
	 @param colorsToMerge The list of colors to merge.
	 @return The average of the given colors.
	 */
	public static float[] mergeHsvColors(List<float[]> colorsToMerge)
	{
		assert(!colorsToMerge.isEmpty());
		
		// First off, if there's only one color, return it verbatim.
		if (colorsToMerge.size() == 1)
		{
			return colorsToMerge.get(0);
		}

		Point2F meanHsPoint = new Point2F(0, 0);

		float result[] = new float[colorsToMerge.get(0).length];

		for (float[] color : colorsToMerge)
		{
			// Compute average hue and saturation by interpolating vectors in HSV space.
			meanHsPoint.x += color[2] * color[1] * FastMath.cos(color[0] * FastMath.PI * 2);
			meanHsPoint.y += color[2] * color[1] * FastMath.sin(color[0] * FastMath.PI * 2);

			// Copy value and alpha as a simple average.
			result[2] = Math.max(result[2], color[2]);
			if(result.length >= 4)
				result[3] += color[3];
		}

		// Divide by the number of points to get the average.
		meanHsPoint.multLocal(1f / colorsToMerge.size());

		// Compute the average value.
		result[0] = (float) (Math.atan2(meanHsPoint.y, meanHsPoint.x) / FastMath.PI / 2);
		if (result[0] < 0)
		{
			result[0] = 1 + result[0];
		}
		result[1] = meanHsPoint.getNorm();
		//result[2] /= numberOfPoints;
		if(result.length >= 4)
			result[3] /= colorsToMerge.size();

		return result;
	}
	
	/**
	 @param hsva1 The first color to interpolate.
	 @param hsva2 The second color to interpolate.
	 @param coeff The interpolation coefficient. 0 means using hsva1, 1 means using hsva2.
	 @return The interpolation of the two colors.
	 */
	public static float[] interpolate(float[] hsva1, float[] hsva2, float coeff)
	{
		assert (0 <= coeff);
		assert (coeff <= 1);
		assert hsva1.length == hsva2.length;
		float[] result = new float[hsva1.length];

		// Simply interpolate value and alpha.
		float coeff2 = 1 - coeff;
		result[2] = hsva1[2] * coeff2 + coeff * hsva2[2];
		if (hsva1.length >= 4)
			result[3] = hsva1[3] * coeff2 + coeff * hsva2[3];

		// Project both HS points to cartesian coordinates:
		double theta1 = hsva1[0] * Math.PI * 2;
		float x1 = (float) (hsva1[1] * Math.cos(theta1));
		float y1 = (float) (hsva1[1] * Math.sin(theta1));

		double theta2 = hsva2[0] * Math.PI * 2;
		float x2 = (float) (hsva2[1] * Math.cos(theta2));
		float y2 = (float) (hsva2[1] * Math.sin(theta2));

		// Interpolate the cartesian coordinates
		float newX = x1 * coeff2 + coeff * x2;
		float newY = y1 * coeff2 + coeff * y2;

		// Get the point back to HS space.
		double newRho = Math.sqrt(newX * newX + newY * newY);
		double newTheta = Math.atan2(newY, newX);
		if (newTheta < 0)
			newTheta += Math.PI * 2;
		
		result[0] = (float) (newTheta / Math.PI / 2);
		result[1] = (float) newRho;
				
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

	/**
	 A temperature to color conversion, inspired from a blogpost from PhotoDemon
	 (http://www.tannerhelland.com/4435/convert-temperature-rgb-algorithm-code/).

	 @param temperature The temperature of an ideal black body, in Kelvins;
	 @param alpha If true, the return value will be RGBA instead of RGB.
	 @return The corresponding RGB color.
	 */
	public static int[] getRgbFromTemperature(double temperature, boolean alpha)
	{
		// Temperature must fit between 1000 and 40000 degrees
		temperature = MathUtils.clamp(temperature,1000,40000);
		
		// All calculations require tmpKelvin \ 100, so only do the conversion once
		temperature /= 100;

		// Compute each color in turn.
		int red, green, blue;
		// First: red
		if (temperature <= 66)
			red = 255;
		else
		{
			// Note: the R-squared value for this approximation is .988
			red = (int) (329.698727446 * (Math.pow(temperature - 60, -0.1332047592)));
			red = MathUtils.clamp(red,0,255);
		}

		// Second: green
		if (temperature <= 66)
			// Note: the R-squared value for this approximation is .996
			green = (int) (99.4708025861 * Math.log(temperature) - 161.1195681661);
		else
			// Note: the R-squared value for this approximation is .987
			green = (int) (288.1221695283 * (Math.pow(temperature - 60, -0.0755148492)));

		green = MathUtils.clamp(green,0,255);

		// Third: blue
		if (temperature >= 66)
			blue = 255;
		else if (temperature <= 19)
			blue = 0;
		else
		{
			// Note: the R-squared value for this approximation is .998
			blue = (int) (138.5177312231 * Math.log(temperature - 10) - 305.0447927307);

			blue = MathUtils.clamp(blue,0,255);
		}

		if (alpha)
			return new int[]
			{
				red, green, blue, 255
			};
		else
			return new int[]
			{
				red, green, blue
			};
	}
}

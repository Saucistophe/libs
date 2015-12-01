package org.saucistophe.math;

/**
 A direct adaptation of Ken Perlin's noise.
 */
public class PerlinUtils
{
    private final static short data[] =
    {
        151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194,
        233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23,
        190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117,
        35, 11, 32, 57, 177, 33, 88, 237, 149, 56, 87, 174, 20, 125, 136, 171,
        168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166, 77, 146, 158, 231,
        83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245,
        40, 244, 102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132,
        187, 208, 89, 18, 169, 200, 196, 135, 130, 116, 188, 159, 86, 164, 100,
        109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123, 5, 202, 38,
        147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17,
        182, 189, 28, 42, 223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163,
        70, 221, 153, 101, 155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98, 108,
        110, 79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34,
        242, 193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249,
        14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, 176,
        115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222, 114, 67,
        29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180, 151, 160,
        137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36,
        103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23, 190, 6, 148, 247, 120,
        234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177,
        33, 88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74,
        165, 71, 134, 139, 48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122,
        60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245, 40, 244, 102, 143,
        54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18,
        169, 200, 196, 135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173,
        186, 3, 64, 52, 217, 226, 250, 124, 123, 5, 202, 38, 147, 118, 126,
        255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42,
        223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101,
        155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224,
        232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34, 242, 193, 238,
        210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107,
        49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, 176, 115, 121, 50,
        45, 127, 4, 150, 254, 138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243,
        141, 128, 195, 78, 66, 215, 61, 156, 180
    };

    private static float fade(float t)
    {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private static float lerp(float t, float a, float b)
    {
        return a + t * (b - a);
    }

    private static float grad(int hash, float x, float y, float z)
    {
        // CONVERT LO 4 BITS OF HASH CODE INTO 12 GRADIENT DIRECTIONS.
        int h = hash & 15;
        float u = h < 8 ? x : y;
        float v = h < 4 ? y : h == 12 || h == 14 ? x : z;

        return ((h & 1) != 0 ? -u : u) + ((h & 2) != 0 ? -v : v);
    }

    private static float noise(float x, float y, float z)
    {
        int X = (int) Math.floor(x) & 255; // FIND UNIT CUBE THAT
        int Y = (int) Math.floor(y) & 255; // CONTAINS POINT.
        int Z = (int) Math.floor(z) & 255;

        x -= Math.floor(x); // FIND RELATIVE X,Y,Z
        y -= Math.floor(y); // OF POINT IN CUBE.
        z -= Math.floor(z);

        float u = fade(x), // COMPUTE FADE CURVES
            v
            = fade(y), // FOR EACH OF X,Y,Z.
            w
            = fade(z);
        // HASH COORDINATES OF THE 8 CUBE CORNERS,
        int A = data[X] + Y;
        int AA = data[A] + Z;
        int AB = data[A + 1] + Z;
        int B = data[X + 1] + Y;
        int BA = data[B] + Z;
        int BB = data[B + 1] + Z;

        return lerp(w, lerp(v, lerp(u, grad(data[AA], x, y, z), // AND ADD
            grad(data[BA], x - 1, y, z)), // BLENDED
            lerp(u, grad(data[AB], x, y - 1, z), // RESULTS
                grad(data[BB], x - 1, y - 1, z))),// FROM  8
            lerp(v, lerp(u, grad(data[AA + 1], x, y, z - 1), // CORNERS
                    grad(data[BA + 1], x - 1, y, z - 1)), // OF CUBE
                lerp(u, grad(data[AB + 1], x, y - 1, z - 1), grad(data[BB + 1], x
                        - 1, y - 1, z - 1))));
    }

    /**
     Get a Noise value for the given x,y,z coordinates.
     @param x The X Perlin coordinate
     @param y The Y Perlin coordinate
     @param z The Z Perlin coordinate
     @return Numbers between 0 and 1. Plus, due to gaussian
     distribution never spreading outside [-0.707,0.707], I scaled the results
     and clipped away those theoretically inexistant issues.
     */
	static float min = 0;
	static float max = 0;
	static int count = 0;

    public static float normedNoise(float x, float y, float z)
    {
        float noise = noise(x, y, z) / 0.70710678118f;

        if (noise < -1)
        {
            noise = -1;
        }
        else if (noise > 1)
        {
            noise = 1;
        }

        return (noise + 1) / 2;
    }

    /**
     Get a Noise value for the given x,y,z coordinates.
     computes the height of a terrain pixel according to its position on a
     sphere. Includes 3 harmonics. Returns a float between 0 and 1.
     */
    public static float altitude(float x, float y, float z)
    {
        return (normedNoise(x, y, z) + normedNoise(x * 2, y * 2, z
            * 2) / 2 + normedNoise(x * 4, y * 4, z * 4) / 4) / 1.75f;
    }
}

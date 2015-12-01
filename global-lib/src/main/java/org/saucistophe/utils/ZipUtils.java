package org.saucistophe.utils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 A utility class to zip/unzip data, using the deflate algorithm.
 */
public class ZipUtils
{
    /**
     Compresses a byte array.

     @param data The bytes to compress.
     @return The corresponding bytes.
     */
    public static byte[] zip(byte[] data)
    {
        byte[] input = data;

        // Compress the bytes
        byte[] output = new byte[input.length];
        Deflater compresser = new Deflater();
        compresser.setInput(input);
        compresser.finish();
        int compressedDataLength = compresser.deflate(output);

        byte[] realOutput = Arrays.copyOfRange(output, 0, compressedDataLength);

        compresser.end();

        return realOutput;
    }

    /**
     Compresses a String.

     @param data The string to compress.
     @return The corresponding bytes.
     */
    public static byte[] zip(String data)
    {
        try
        {
            byte[] input = data.getBytes(Constants.ENCODING);
            return zip(input);
        } catch (UnsupportedEncodingException ex)
        {
            Logger.getLogger(ZipUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     Unzips into a String.

     @param data The bytes to decompress.
     @return The corresponding string.
     */
    public static String unZip(byte[] data)
    {
        try
        {
            Inflater decompresser = new Inflater();
            decompresser.setInput(data);

            byte[] result = new byte[Constants.Zip.BUFFER_SIZE];
            int resultLength = decompresser.inflate(result);
            byte[] output = Arrays.copyOfRange(result, 0, resultLength);
            decompresser.end();

            return new String(output, Constants.ENCODING);

        } catch (DataFormatException | UnsupportedEncodingException ex)
        {
            Logger.getLogger(ZipUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}

package org.saucistophe.utils;

import com.itextpdf.text.Font;

/**
 * A repository class for every constant.
 *
 * @author Christophe
 */
public class Constants
{
    public static final String ENCODING = "UTF-8";

    /**
     * Constants pertaining to file storage.
     */
    public interface File
    {
        public static final String XML_STORAGE_PATH = "xml";
    }

    /**
     * Constants pertaining to Authentification.
     */
    public interface Cipher
    {
        public static final String KEY_SEPARATOR = "///";
        public static final String ALGORITHM = "RSA";
        public static final int RADIX = 32;
        public static final String SEPARATOR = "@°o°@";
    }

    /**
     * Constants pertaining to Webservices.
     */
    public interface Services
    {
        public static final String AUTH_PARAM = "auth";
        public static final String KEY_PATH = "key";
    }

    /**
     * Constants pertaining to Compression.
     */
    public interface Zip
    {
        public static final int BUFFER_SIZE = 16384;
    }

    public interface Pdf
    {
        public static final Font.FontFamily font = Font.FontFamily.HELVETICA;
        public static final double margin = 0.03;
    }
}

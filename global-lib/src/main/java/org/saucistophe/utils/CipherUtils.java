package org.saucistophe.utils;

import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;

/**
 Utility class to cipher and decipher strings using the project's encryption
 algorithm.

 @author chcarpen
 */
public class CipherUtils
{
    /**
     Ciphers the given data.

     @param data The bytes to encrypt
     @param key  The key used to encrypt it
     @return The encrypted bytes.
     */
    public static byte[] cipher(byte[] data, Key key)
    {
        try
        {
            Cipher cipher = Cipher.getInstance(Constants.Cipher.ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] ciphertext = cipher.doFinal(data);

            return ciphertext;
        } catch (Exception ex)
        {
            Logger.getLogger(CipherUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     Deciphers the given data.

     @param data The bytes to ecrypt
     @param key  The key used to decrypt it
     @return The decrypted bytes.
     */
    public static byte[] decipher(byte[] data, Key key)
    {
        try
        {
            Cipher cipher = Cipher.getInstance(Constants.Cipher.ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypted = cipher.doFinal(data);

            return decrypted;
        } catch (Exception ex)
        {
            Logger.getLogger(CipherUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}

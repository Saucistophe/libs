package org.saucistophe.utils;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 An utility class to save/load beans as xml.
 */
public class XmlStore
{
    /**
     @param clazz
     @param name
     @return the file to store the bean, according to its class and name.
     */
    private static File getFileForBean(Class clazz, String name)
    {
        String className = clazz.getCanonicalName();

        // Add a _ in front of the name if it's not empty.
        if (name == null)
        {
            name = "";
        }
        else if (!name.equals(""))
        {
            name = "_" + name;
        }

        String fileName = className + name + ".xml";

        // Create an storage file.
        File storageFolder = new File(Constants.File.XML_STORAGE_PATH);
        storageFolder.mkdir();

        // Create the output file.
        File storageFile = new File(storageFolder, fileName);

        return storageFile;
    }

    /**
     Saves a bean, in the given xml file.
     */
    public static boolean saveBean(Object bean, File storageFile)
    {
        // Write XML
        try (XMLEncoder encoder = new XMLEncoder(new FileOutputStream(storageFile)))
        {
            encoder.writeObject(bean);
            encoder.flush();
        } catch (Exception ex)
        {
            Logger.getLogger(XmlStore.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     Saves a bean, in a xml file with the given name.
     */
    public static boolean saveBean(Object bean, String name)
    {
        return saveBean(bean, getFileForBean(bean.getClass(), name));
    }

    /**
     Loads a bean, of type T.

     @param <T> The type of object to return.
     @param clazz The class of the object to return.
     @param storageFile The file where the bean is stored.
     @return The deserialized object.
     */
    @SuppressWarnings("unchecked")
    public static <T> T loadBean(Class<T> clazz, File storageFile)
    {
        T t = null;
        try (XMLDecoder decoder = new XMLDecoder(new FileInputStream(storageFile)))
        {
            t = (T) decoder.readObject();
        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(XmlStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return t;
    }

    /**
     Loads a bean, of type T.

     @param <T> The type of object to return.
     @param clazz The class of the object to return.
     @param name The object's name.
     @return The deserialized object, obtained from the XML file corresponding to the name and class.
     */
    public static <T> T loadBean(Class<T> clazz, String name)
    {
        return loadBean(clazz, getFileForBean(clazz, name));
    }
}

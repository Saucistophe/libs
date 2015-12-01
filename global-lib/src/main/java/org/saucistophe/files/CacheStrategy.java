package org.saucistophe.files;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 Class describing a file caching strategy.
 */
public class CacheStrategy
{
	private static final Logger logger = Logger.getLogger(CacheStrategy.class.getName());

	/**
	 The root folder of this cache.
	 */
	public File rootFolderPath;

	/**
	 The (optionnal) extension to apply to all files cached.
	 */
	private String fileExtension = "";

	/**
	 A constructor with the root folder path specified.

	 @param rootPath The path to the cache's root folder.
	 */
	public CacheStrategy(String rootPath)
	{
		rootFolderPath = new File(rootPath);
		// Create the dir's to get the cache ready.
		rootFolderPath.mkdirs();

		logger.log(Level.INFO, "Created cache at {0}", rootPath);
	}

	/**
	 A constructor with the root folder path and file extension specified.

	 @param rootPath The path to the cache's root folder.
	 @param fileExtension The file extension of the files to
	 */
	public CacheStrategy(String rootPath, String fileExtension)
	{
		this(rootPath);
		this.fileExtension = fileExtension;

		// Add a point if needed.
		if (!fileExtension.startsWith("."))
		{
			this.fileExtension = "." + fileExtension;
		}
	}

	/**
	 Gets the file corresponding to the given attributes. If it doesn't already exists, creates it.

	 @param fileConsumer The method used to create the file.
	 @param attributes A list of attributes, in logical order, to identify this item. For example,
	 texture/normal/1024x104. At least one attribute must be specified.
	 @return The cached file.

	 @throws java.io.IOException In case of problem with the file creation or retrieval.
	 */
	public File getFile(Consumer<File> fileConsumer, Object... attributes) throws IOException
	{
		File resultFile;
		Path rootPath = rootFolderPath.toPath();

		if (attributes == null || attributes.length == 0)
		{
			throw new IllegalArgumentException("At least one attribute must be used in caches.");
		}

		// If all's ok, resolve the file's full path.
		Path currentPath = rootPath;

		for (int i = 0; i < attributes.length; i++)
		{
			String attribute;
			// Handle special case for conversion to string.
			if (attributes[i] instanceof Dimension)
			{
				Dimension dimensionAttribute = (Dimension) attributes[i];
				attribute = dimensionAttribute.width + "x" + dimensionAttribute.height;
			} else
			{
				// Default case: use the toString.
				attribute = attributes[i].toString();
			}

			// If the last attribute requires an extension.
			if (i == attributes.length - 1 && fileExtension != null && !fileExtension.isEmpty())
			{
				attribute += fileExtension;
			}
			currentPath = currentPath.resolve(attribute);
		}

		resultFile = currentPath.toFile();

		// If the file does not exist, create it.
		if (!resultFile.exists())
		{
			logger.log(Level.INFO, "File {0} not found in cache, generating...", resultFile.getPath());
			long currentTime = System.currentTimeMillis();

			resultFile.getParentFile().mkdirs();
			resultFile.createNewFile();
			fileConsumer.accept(resultFile);
			logger.log(Level.INFO, "Generated in {0}ms.", System.currentTimeMillis() - currentTime);
		} else
		{
			logger.log(Level.INFO, "Found cached file {0}", resultFile.getPath());
		}

		return resultFile;
	}

	/**
	 Clears the cache of all files.
	 */
	public void clear()
	{
		try
		{
			FileUtils.deleteDirectory(rootFolderPath);
		} catch (IOException ex)
		{
			Logger.getLogger(CacheStrategy.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}

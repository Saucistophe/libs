package org.saucistophe.swing;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.io.FilenameUtils;

/**
 Swing components for file choosing and manipulation.
 */
public class FileComponentsUtils
{
	/**
	 Prompts the user to choose a file, for the given extension.

	 @param fileExtension The file's wished extension. E.g. "pdf".
	 @param defaultLocation Optional, the folder in which to start browsing.
	 @return A file if one was chosen, null if the user cancelled.
	 */
	public static File chooseFile(String fileExtension, File defaultLocation)
	{
		File result = null;

		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileHidingEnabled(true);

		if (defaultLocation != null)
		{
			fileChooser.setCurrentDirectory(defaultLocation);
		}
		fileChooser.setFileFilter(new FileNameExtensionFilter(fileExtension + " files", fileExtension));

		int returnVal = fileChooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			result = fileChooser.getSelectedFile();

			// Add the extension if needed.
			String selectedFileExtension = FilenameUtils.getExtension(result.getAbsolutePath());
			if (selectedFileExtension.isEmpty())
			{
				result = new java.io.File(result.getAbsolutePath() + "." + fileExtension);
			}
		}

		return result;
	}
}

package org.saucistophe.swing;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;

/**
 An image, transferable via MIME thingies (clipboard, drag-n-drop...).
 */
public class TransferableImage implements Transferable
{
	/**
	 The image to transfer.
	 */
	private final Image imageToTransfer;

	public TransferableImage(Image imageToTransfer)
	{
		this.imageToTransfer = imageToTransfer;
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
	{
		// Check if the flavor is OK.
		if (flavor.equals(DataFlavor.imageFlavor))
		{
			return imageToTransfer;
		}
		else
		{
			throw new UnsupportedFlavorException(flavor);
		}
	}

	@Override
	public DataFlavor[] getTransferDataFlavors()
	{
		return new DataFlavor[]
		{
			DataFlavor.imageFlavor
		};
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		DataFlavor[] flavors = getTransferDataFlavors();
		return Arrays.asList(flavors).contains(flavor);
	}
}

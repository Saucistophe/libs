package org.saucistophe.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 */
public class PdfUtils
{
    public static PdfWriter createNewPdf(Document document)
    {
        // First ask the user for a file, and abort if he cancels.
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileHidingEnabled(true);
        // fileChooser.setCurrentDirectory();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Fichiers .PDF", "pdf"));

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION)
        {
            File file = fileChooser.getSelectedFile();
            // If not ending with ".pdf", rename it.
            if (!fileChooser.getSelectedFile().getPath().toLowerCase().endsWith(".pdf"))
            {
                file = new File(fileChooser.getSelectedFile().getPath() + ".pdf");
            }

            PdfWriter pdfWriter = null;
            try
            {
                pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));
            } catch (FileNotFoundException | DocumentException ex)
            {
                Logger.getLogger(PdfUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
            return pdfWriter;
        }

        return null;
    }
}

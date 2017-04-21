package es.uam.eps.bmi.search.parser;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
*
* @author  Francisco Andreu y Javier Hernandez
* @version 1.0
* @since   2017-02-10
*/
public class PDFTextParser {
    
   private PDFParser parser;
   private PDFTextStripper pdfStripper;
   private PDDocument pdDoc ;
   private COSDocument cosDoc ;
   
   private String Text ;
   private String filePath;
   private File file;

    public PDFTextParser(String collectionPath) {
        this.filePath = collectionPath;
    }
    
  /**
   * Metodo que devuelve la cadena parseada
   * @return String La cadena ya parseada
   */        
   public String ToText() throws IOException
   {
       this.pdfStripper = null;
       this.pdDoc = null;
       this.cosDoc = null;
       
       file = new File(filePath);
       parser = new PDFParser(new RandomAccessFile(file,"r")); // update for PDFBox V 2.0
       
       parser.parse();
       cosDoc = parser.getDocument();
       pdfStripper = new PDFTextStripper();
       pdDoc = new PDDocument(cosDoc);
       pdDoc.getNumberOfPages();
       pdfStripper.setStartPage(1);
        pdfStripper.setEndPage(pdDoc.getNumberOfPages());
       
       Text = pdfStripper.getText(pdDoc);
       return Text;
   }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
   
}
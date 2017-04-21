/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.parser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

/**
*
* @author  Francisco Andreu y Javier Hernandez
* @version 1.0
* @since   2017-02-10
*/
public class Parser {
    
  /**
   * Metodo estatico que devuelve la cadena parseada
   * @param name : El nombre del archivo
   * @param entry : El stream de datos de entrada a parsear
   * @return String La cadena yaparseada
   */    
    public static String parse(String name, InputStream entry) throws IOException{
        if(name.startsWith("http")){
            
           return Jsoup.connect(name).get().text();
            
            
            
        }else if(name.endsWith(".html")){
            if(entry!=null){
               
                return Jsoup.parse(entry, "UTF-8",name).text();
            }
            
        }else if(name.endsWith(".pdf")){
            PDFTextParser pdf = new PDFTextParser(name);
            return pdf.ToText();
            
        }else{
            return  new String(Files.readAllBytes(Paths.get(name)), StandardCharsets.UTF_8);
        }
        return null;
    }
    
}

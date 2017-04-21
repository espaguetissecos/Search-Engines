/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.index.impl;

import es.uam.eps.bmi.search.index.AbstractIndexBuilder;
import es.uam.eps.bmi.search.index.Config;
import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.index.structure.Posting;
import es.uam.eps.bmi.search.index.structure.impl.Position;
import es.uam.eps.bmi.search.index.structure.impl.PostingsListImpl;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 * @author Javiules
 */
public class DiskIndexBuilder  extends AbstractIndexBuilder{
    
    String indexFolder;
    HashMap<String, PostingsListImpl > dic;  //Diccionario term-lista posting
    HashMap<String, Position> dicPosition; //Diccionario term-lista posting
    int ordinal = 0;

    public void build(String collPath, String outputPath) throws IOException {
        indexFolder = outputPath;
        clear(indexFolder);
		
		dic = new HashMap<>();
        dicPosition = new HashMap<>();
        File f = new File(collPath);
        if (f.isDirectory()) indexFolder(f);                // A directory containing text files.
        else if (f.getName().endsWith(".zip")) indexZip(f); // A zip file containing compressed text files.
        else indexURLs(f);                                  // A file containing a list of URLs.
        
        RandomAccessFile postingFile = new RandomAccessFile(indexFolder + Config.postingsFileName, "rw");

        
        dic.forEach((k,v)->{
          
            try {
               
                Long position = postingFile.getFilePointer();
                String postingsListString = v.toStringAux();
                dicPosition.put(k, new Position(position, postingsListString.getBytes().length));
                postingFile.write(postingsListString.getBytes());
                
            } catch (IOException ex) {
                Logger.getLogger(DiskIndexBuilder.class.getName()).log(Level.SEVERE, null, ex);
            }
                
            
            
        });
        postingFile.close();
        PrintStream outAux = new PrintStream(this.indexFolder + Config.dictionaryFileName); 
        dicPosition.keySet().forEach((term) -> {
            outAux.println(term+" "+dicPosition.get(term).getPos()+" "+dicPosition.get(term).getSize());
        });
        outAux.close();
        saveDocNorms(indexFolder);

    }

    @Override
    protected void indexText(String text, String path) throws IOException {
       
        Pattern pattern = Pattern.compile("\\P{Alpha}+") ;
        String[] words = pattern.split(text);
        List<String> wordslist = Arrays.asList(words);
        wordslist.replaceAll(String::toLowerCase);
        
       
       
        wordslist.stream().collect(Collectors.groupingBy(s -> s)).forEach((term, v) -> {
            if (dic.get(term) == null)
                dic.put(term, new PostingsListImpl()); 
  
            dic.get(term).add(new Posting(ordinal, v.size()));
        });
        
        FileWriter fichero = new FileWriter(indexFolder  +Config.pathsFileName, true);
        try (PrintWriter pw = new PrintWriter(fichero)) {
            pw.println(path);
            pw.close();
            fichero.close();
        }
        ordinal++;
        
    }

    @Override
    protected Index getCoreIndex() throws IOException {
      
        
        return new DiskIndex(indexFolder);

    }
    
}
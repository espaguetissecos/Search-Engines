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
import es.uam.eps.bmi.search.index.structure.impl.PostingsListImpl;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 * @author root
 */
public class SerializedRAMIndexBuilder extends AbstractIndexBuilder{
    
    String indexFolder;
    HashMap<String, PostingsListImpl > dic;//Diccionario term-lista posting
    int ordinal = 0;

    
    @Override
    public void build(String collPath, String outputPath) throws IOException {
        indexFolder = outputPath;
        clear(indexFolder);
		dic = new HashMap<>();
        
        //  TODO ver si lo ponemos
        //        
        // IndexWriterConfig iwc = new IndexWriterConfig(new StandardAnalyzer()); //Tenemos que hacer nosotros el analyzer tb????
        //        
        //iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        
        

        File f = new File(collPath);
        if (f.isDirectory()) indexFolder(f);                // A directory containing text files.
        else if (f.getName().endsWith(".zip")) indexZip(f); // A zip file containing compressed text files.
        else indexURLs(f);                                  // A file containing a list of URLs.
        
        //TODO ¿Esto no iría en index Text? Aunque es lo mismo
        FileOutputStream fout = new FileOutputStream(indexFolder + "/" + Config.indexFileName);
        ObjectOutputStream oos = new ObjectOutputStream(fout);
        
        oos.writeObject(dic);  
        
        saveDocNorms(indexFolder);

        
    }

    @Override
    protected void indexText(String text, String path) throws IOException {
        
        Pattern pattern = Pattern.compile("\\P{Alpha}+") ;
        String[] words = pattern.split(text);
        List<String> wordslist = Arrays.asList(words);
        wordslist.replaceAll(String::toLowerCase);
        
       
        
        //Para cada termino, sacamos su frecuencia y lo añadimos al dic con su 
        //posting docID-Freq
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
        return new SerializedRAMIndex(indexFolder);
    }
    
}

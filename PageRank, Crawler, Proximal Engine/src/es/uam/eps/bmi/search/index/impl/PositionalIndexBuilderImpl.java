/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.index.impl;

import es.uam.eps.bmi.search.index.AbstractIndexBuilder;
import es.uam.eps.bmi.search.index.Config;
import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.index.structure.EditablePostingsList;
import es.uam.eps.bmi.search.index.structure.impl.PositionalPostingsListImpl;
import es.uam.eps.bmi.search.index.structure.impl.PostingsListImpl;
import es.uam.eps.bmi.search.index.structure.positional.PositionalPosting;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 * @author root
 */
public class PositionalIndexBuilderImpl extends AbstractIndexBuilder{
    
    String indexFolder;
    HashMap<String, PostingsListImpl > dic; //Diccionario term-lista posting
    int ordinal = 0;
    long lenText=0;
    
    @Override
    public void build(String collPath, String outputPath) throws IOException {
        indexFolder = outputPath;
        clear(indexFolder);
        dic = new HashMap<>();
        
      
        File f = new File(collPath);
        if (f.isDirectory()) indexFolder(f);                // A directory containing text files.
        else if (f.getName().endsWith(".zip")) indexZip(f); // A zip file containing compressed text files.
        else indexURLs(f);                                  // A file containing a list of URLs.
        
        FileOutputStream fout = new FileOutputStream(indexFolder + Config.indexFileName);
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
        HashMap<String, List<Integer>> pos = new HashMap<>();
        int i;
        
        
        /**
         * NUEVO
         */
        i = 0;
        for (String term : wordslist){
            if (pos.get(term) == null)
                pos.put(term, new ArrayList<>()); 
            pos.get(term).add(i);
            i++;
        }
        
        wordslist.stream().collect(Collectors.groupingBy(s -> s)).forEach((term, v) -> {
            if (dic.get(term) == null)
                dic.put(term, new PostingsListImpl()); 
         
            dic.get(term).add(new PositionalPosting(ordinal, v.size(), pos.get(term)));
        });
        
        
        FileWriter fichero = new FileWriter(indexFolder  +Config.pathsFileName, true);
        try (PrintWriter pw = new PrintWriter(fichero)) {
            pw.println(path);
            pw.close();
            fichero.close();
        }
        ordinal++;
    
        //Heap's law
         
        lenText += text.length();
        PrintStream heaplaw = new PrintStream(
        new FileOutputStream("HeapsLaw.txt", true)); 
        heaplaw.println(lenText + " " + dic.keySet().size());
        heaplaw.flush();
        heaplaw.close();
    }

    @Override
    protected Index getCoreIndex() throws IOException {
        return new PositionalIndexImpl(indexFolder);
    }
    
}
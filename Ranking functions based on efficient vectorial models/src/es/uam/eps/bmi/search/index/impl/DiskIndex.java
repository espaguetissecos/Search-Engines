/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.index.impl;

import es.uam.eps.bmi.search.index.AbstractIndex;
import es.uam.eps.bmi.search.index.Config;
import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.index.structure.Posting;
import es.uam.eps.bmi.search.index.structure.PostingsList;
import es.uam.eps.bmi.search.index.structure.impl.Position;
import es.uam.eps.bmi.search.index.structure.impl.PostingsListImpl;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javi
 */
public class DiskIndex extends AbstractIndex {
    //Para tenerlo ordenado
    
    
    HashMap<String, Position> dicPosition; 

   
    
    public DiskIndex(String string)  throws IOException {
        super(string);

    }
    
    @Override
    public int numDocs() {

    
        FileReader fileReader = null;
        int acc =0;

        try {
            fileReader = new FileReader(indexFolder + Config.pathsFileName);
            BufferedReader reader = new BufferedReader(fileReader);
            String linea;
            while((linea = reader.readLine())!=null){
                acc++;
            }  
        
        } catch (IOException ex) {
            Logger.getLogger(DiskIndex.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fileReader.close();
            } catch (IOException ex) {
                Logger.getLogger(DiskIndex.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
         return acc;
    
    }

    @Override

    public PostingsList getPostings(String term) throws IOException {
        PostingsListImpl list = new PostingsListImpl();
 
        String listString ;
        try (RandomAccessFile file = new RandomAccessFile(indexFolder + Config.postingsFileName, "rw")) {
            Position pos= this.dicPosition.get(term);
            file.seek(pos.getPos());
            byte [] byteStream = new byte[pos.getSize()];
            file.read(byteStream);
            listString = new String(byteStream );
        }
            return  list.stringToPostingsListImpl(listString);
               
          
           
    }
    
   

    @Override
    /* Todos los terms del diccionario */
    public Collection<String> getAllTerms() throws IOException {	
        return this.dicPosition.keySet();
        
    }

    @Override
    /* Ocurrencia del termino sobre todos los doc */
    public long getTotalFreq(String term) throws IOException {
        
            long counter = 0;
            PostingsList postingsList = getPostings(term);
            for(Posting p : postingsList){
                counter += p.getFreq();
            }
            return counter;
          
    }

    @Override
    public long getDocFreq(String term) throws IOException {
    
            //Access to postingsList indicated by the posiition of the term
            return getPostings(term).size();
 
    }

    @Override
    public void load(String path) throws IOException {
         File f = new File(path + Config.dictionaryFileName);
        if(!f.exists()) return;
        
        this.dicPosition = new HashMap<>();
        
      
        Scanner scn = new Scanner(f);
        String linea;
        while(scn.hasNext()){
            linea = scn.nextLine();
            //System.out.println(linea);
            String[] termpos = linea.split(" ");
            this.dicPosition.put(termpos[0], new Position(Long.parseLong(termpos[1]), Integer.parseInt(termpos[2])));
        }
        scn.close();
        loadNorms(indexFolder);


    }

    @Override
    public String getFolder() {
        return indexFolder;
    }

    /*  */
    @Override
    public String getDocPath(int docID) throws IOException {
        File archivo = new File(indexFolder + Config.pathsFileName);
        FileReader fr = new FileReader(archivo);
        BufferedReader br = new BufferedReader(fr);
        String linea;
        for (int i = 0; (linea = br.readLine()) != null; i++) 
            if (i == docID) 
                return linea;
        return null;

    }

    /*  */
    @Override
    public double getDocNorm(int docID) throws IOException {
        return this.docNorms[docID];
    }
    
}
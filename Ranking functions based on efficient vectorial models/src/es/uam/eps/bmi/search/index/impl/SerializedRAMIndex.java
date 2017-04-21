/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.index.impl;

import es.uam.eps.bmi.search.index.AbstractIndex;
import es.uam.eps.bmi.search.index.Config;
import es.uam.eps.bmi.search.index.structure.Posting;
import es.uam.eps.bmi.search.index.structure.PostingsList;
import es.uam.eps.bmi.search.index.structure.impl.PostingsListImpl;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class SerializedRAMIndex extends AbstractIndex {

    HashMap<String, PostingsListImpl> dic; //Diccionario term-lista posting

    public SerializedRAMIndex(String string) throws IOException {
        super(string);
    }

    @Override
    public int numDocs() {
        Set<Integer> set = new HashSet<>();
        dic.values().forEach((list) -> {
            list.forEach((p) -> {
                set.add(p.getDocID());
            });
        });
        return set.size();
    }

    @Override

    public PostingsListImpl getPostings(String term) throws IOException {
        return dic.get(term);
    }

    @Override
    /* Todos los terms del diccionario */
    public Collection<String> getAllTerms() throws IOException {
        return dic.keySet();
    }

    @Override
    /* Ocurrencia del termino sobre todos los doc */
    public long getTotalFreq(String term) throws IOException {
        long counter = 0;
        for (Posting p : dic.get(term))
            counter += p.getFreq();
        return counter;
    }

    @Override
    public long getDocFreq(String term) throws IOException {
        return dic.get(term).size();
    }

    @Override
    public void load(String path) throws IOException {
        InputStream file;
        file = new FileInputStream(path  + Config.indexFileName);
        InputStream buffer = new BufferedInputStream(file);
        ObjectInput input = new ObjectInputStream(buffer);
        try {
            this.dic = (HashMap<String, PostingsListImpl>) input.readObject();
           
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SerializedRAMIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
        loadNorms(path);


    }

    @Override
    public String getFolder() {
        return indexFolder;
    }

    /*  */
    @Override
    public String getDocPath(int docID) throws IOException {
        File archivo = new File(indexFolder  +Config.pathsFileName);
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

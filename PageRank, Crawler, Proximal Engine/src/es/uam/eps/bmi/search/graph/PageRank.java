/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.graph;

import es.uam.eps.bmi.search.AbstractEngine;
import es.uam.eps.bmi.search.SearchEngine;
import es.uam.eps.bmi.search.index.DocumentMap;
import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.ranking.impl.RankingImpl;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.DoubleStream;


/**
 *
 * @author root
 */
public class PageRank implements SearchEngine, DocumentMap{

    double r;
    int n;
    HashMap<String, Integer> correspondencia;
    String path;
    HashMap<Integer, List<Integer>> docs_to; 
    double[] P;
    RankingImpl ranking;
    private int nSortedDocs;
    
    public PageRank(String data, double r, int n) throws FileNotFoundException, IOException {
        this.r = r;
        this.n = n;
        this.correspondencia = new HashMap<>();
        this.path = data;
        /*Rellenamos HashMap con URLs del doc*/
        scanEntry(data);
 
        
        
                
    }

    @Override
    public SearchRanking search(String query, int cutoff) throws IOException {
        ranking = new RankingImpl(this, cutoff);

        return ranking;
    }

    @Override
    public DocumentMap getDocMap() {
        
        int i, j, k, l;
        double total = 0;
        l = 0;
        
        this.P = new double[nSortedDocs];

        for ( i = 0 ; i < nSortedDocs ; i++ )
            this.P[i] = (1.0/(double)nSortedDocs);
        
        double[] incremento_P = new double[nSortedDocs];
        
        
        while(l < this.n){
        
            for (i = 0 ; i < nSortedDocs ; i++)
                incremento_P[i] = (r);
             //incremento_P[i] = r/(double)this.n;
            
            for (int doc : correspondencia.values()){
                if (docs_to.get(doc) != null)
                    for (int to : docs_to.get(doc)){

                        i = doc; //from
                        j = to; //to

                        incremento_P[j] += (1 - this.r) * this.P[i]/((double)docs_to.get(doc).size());
                    }                
            }
            double sumidero = ((1.0 - Arrays.stream(incremento_P).sum())/nSortedDocs);
           
            for (i = 0 ; i < nSortedDocs ; i++)
                P[i] = incremento_P[i] + sumidero;
            
            
        l++;
        }
        
        
        for(i=0; i< nSortedDocs; i++){
            ranking.add(i, P[i]);
        }
        
        return this;
        
    }

    private void scanEntry(String data) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(data));
        String fields[];
        int docID = 0;
        this.docs_to = new HashMap<>();
        try {
            
           String line;
            while ( ( line = br.readLine()) != null) {
                fields = line.split("\t");
                if(fields.length == 2){
                    if (correspondencia.get(fields[0]) == null){
                        correspondencia.put(fields[0], docID);
                        docID++;
                    }
                    if (correspondencia.get(fields[1]) == null){
                        correspondencia.put(fields[1], docID);
                        docID++;
                    }     
                    int id_ori = correspondencia.get(fields[0]);
                    int id_dest = correspondencia.get(fields[1]);    
                
                    if (this.docs_to.get(id_ori) == null)
                        docs_to.put(id_ori,new ArrayList<>());
                
                     docs_to.get(id_ori).add(id_dest);
                }
                
            }
        } finally {
            this.nSortedDocs = docID;
            br.close();
        }    
    }

    @Override
    public String getDocPath(int docID) throws IOException {
        return getKeyByValue(this.correspondencia, docID);
    }

    @Override
    public double getDocNorm(int docID) throws IOException {
        throw new UnsupportedOperationException("No debe llegar"); 
    }

public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
    for (Entry<T, E> entry : map.entrySet()) {
        if (Objects.equals(value, entry.getValue())) {
            return entry.getKey();
        }
    }
    return null;
}
}


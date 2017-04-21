/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.index.structure.impl;

import es.uam.eps.bmi.search.index.structure.Posting;
import es.uam.eps.bmi.search.index.structure.PostingsList;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

/**
 *
 * @author root
 */
public class PostingsListImpl implements PostingsList, Serializable{


    
    private final ArrayList<Posting> postings;
    
    public PostingsListImpl(){
        postings = new ArrayList<>();
    }
    
    public  void convertPostingList(PostingsList postingsList) {
        
        Iterator it = postingsList.iterator();
        while(it.hasNext()){
            this.postings.add((Posting) it.next());
            
        }
        
    }
    
    @Override
    public int size() {
        return postings.size();
    }

    @Override
    public Iterator<Posting> iterator() {
        return postings.iterator();
    }
    
    private Posting get(int p){
       
        return postings.get(p);
    }
    public Posting getFirst(){
        return get(0);
    }
    
    
    
    public void add(Posting p){
        postings.add(p);
    }
    
    public void remove(Posting p){
        postings.remove(p);
    }
    
    private void remove(int i){
        postings.remove(i);
    }

    public void removeFirst() {
        remove(0);
    }
    public String toStringAux(){
        String linea = "";
        Iterator<Posting> iterator = this.postings.iterator();
        while(iterator.hasNext()){
            Posting posting = iterator.next();
            linea += posting.getDocID()+"-"+posting.getFreq();
            if(iterator.hasNext()){
                linea += "|"; 
            }
        }
        return linea;
    }
    
    public PostingsListImpl stringToPostingsListImpl(String string){
        PostingsListImpl list = new PostingsListImpl();

        String[] postings = string.split("\\|");
         for(String posting : postings ){
                    String[] docFreqs = posting.split("-");
                    //System.out.println(docFreqs[0]);
                    String doc="";
                    String freq ="";
                    for (int i = 0; i < docFreqs[0].length(); i++){
                           
                             int n = Character.getNumericValue(docFreqs[0].charAt(i));
                             if (n!=-1)
                                  doc+=n;

                    }
                     for (int i = 0; i < docFreqs[1].length(); i++){
                           
                             long n = Character.getNumericValue(docFreqs[1].charAt(i));
                             if (n!=-1)
                                  freq+=n;

                    }
                    
                    int docID = Integer.parseInt(doc);
                    long freq2 =  Long.parseLong(freq);
                    list.add(new Posting(docID,freq2));
              
        }
        return list;
        
    }
    
}
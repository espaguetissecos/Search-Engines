/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.index.structure.impl;

import es.uam.eps.bmi.search.index.structure.Posting;
import es.uam.eps.bmi.search.index.structure.PostingsList;
import es.uam.eps.bmi.search.index.structure.positional.PositionalPosting;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

/**
 *
 * @author root
 */
public class PositionalPostingsListImpl implements PostingsList, Serializable{

    
    private final ArrayList<Posting> postings;
    
    public PositionalPostingsListImpl(){
        postings = new ArrayList<>();
    }
    
    public  void convertPostingList(PostingsList postingsList) {
        
        Iterator it = postingsList.iterator();
        while(it.hasNext()){
            this.postings.add((PositionalPosting) it.next());
            
        }
        
    }
    
    public int size() {
        return postings.size();
    }

    public Iterator<Posting> iterator() {
        return postings.iterator();
    }
    
    private Posting get(int p){
       
        return postings.get(p);
    }
    public Posting getFirst(){
        return get(0);
    }
    
    
    
    public void add(PositionalPosting p){
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
    

    
}
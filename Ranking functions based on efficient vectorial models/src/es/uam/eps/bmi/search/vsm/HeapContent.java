/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.vsm;

import es.uam.eps.bmi.search.index.structure.Posting;
import es.uam.eps.bmi.search.index.structure.Posting;

/**
 *
 * @author root
 */
/*No tendría que ser lista de strings*/
public class HeapContent implements Comparable<HeapContent> {
    private String term;
    private Posting posting;

    public String getTerm() {
        return term;
    }

    public Posting getPosting() {
        return posting;
    }

    HeapContent(String term, Posting posting) {
        this.term = term;
        this.posting = posting;
    }
    
    @Override
    public boolean equals(Object e){
         
          if (e==this) return true;
          else if (!(e instanceof HeapContent)) return false;
          HeapContent p = (HeapContent)e;
          return posting.getDocID() == p.getPosting().getDocID();
    }   

    @Override
    public int compareTo(HeapContent heapContent) {
        //Compare by doc_id the term of the heap doesn't matter
        return this.posting.compareTo(heapContent.posting);
             
    }
    
    @Override 
    public int hashCode(){
        return posting.getDocID();
    }
    
    
}

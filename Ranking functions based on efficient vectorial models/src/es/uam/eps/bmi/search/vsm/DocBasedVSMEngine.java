/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.vsm;


import es.uam.eps.bmi.search.index.Index;

import es.uam.eps.bmi.search.index.structure.PostingsList;
import es.uam.eps.bmi.search.index.structure.PostingsListIterator;
import es.uam.eps.bmi.search.index.structure.impl.PostingsListImpl;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.ranking.impl.RankingImpl;
import java.io.IOException;
import java.util.HashMap;

import java.util.PriorityQueue;
import java.util.TreeMap;

/**
 *
 * @author Javi
 */
public class DocBasedVSMEngine extends AbstractVSMEngine {

    public DocBasedVSMEngine(Index index) {
        super(index);
    }

    @Override
    public SearchRanking search(String q, int cutoff) throws IOException {

        String query[] = q.split(" ");
        
        RankingImpl rankingImpl = new RankingImpl(index, cutoff);
        int actualID = -1;

        //Instanciamos Heap
        PriorityQueue<HeapContent> heap = new PriorityQueue<>(query.length);
        //Instanciamos HashMap de pares Termino-PostingList
        HashMap<String,PostingsListImpl> entry = new HashMap<>();
        /**
         * Instanciamos lista de pares DocID-ScoreFinal. TreeMap para que sea
         * ordenado según docID para recorrer mas facil el fichero de modulos
         */

        TreeMap<Integer, Double> finalScore = new TreeMap<>();

        //Definimos el HashMap termino-PostingList
        for (String term : query) {
            PostingsList postingList = index.getPostings(term);
            PostingsListImpl  postingsListImpl = new PostingsListImpl();
            postingsListImpl.convertPostingList(postingList);
            entry.put(term, postingsListImpl);

           // entry.put(term, (PostingsListIterator) postingList);
            //Y añadimos al heap el primer posting de cada Qi
            //Ver si con el next funciona
            
            // Ver si así
            //heap.add(new HeapContent(term, entry.get(term).get(0));
            // O así
            heap.add(new HeapContent(term, ((PostingsListImpl)entry.get(term)).getFirst()));
        }

        while (entry.isEmpty() == false) {
            HeapContent head = heap.poll();
            //Caso de que el acc sea de un nuevo DocID
            if(head !=null){
               
            
            if (actualID != head.getPosting().getDocID()) {
                actualID = head.getPosting().getDocID();
               
         
                finalScore.put(actualID, tfidf(head.getPosting().getFreq(),index.getDocFreq(head.getTerm()), index.numDocs()));
            } //Caso de que acumulemos el mismo DocID
            else {
                finalScore.put(actualID, finalScore.get(actualID) +tfidf(head.getPosting().getFreq(),index.getDocFreq(head.getTerm()), index.numDocs()));
            }
           
            
            entry.get(head.getTerm()).removeFirst();
           
            if (entry.get(head.getTerm()).size()!=0) {
                heap.add(new HeapContent(head.getTerm(), entry.get(head.getTerm()).getFirst()));
            }
            if(entry.get(head.getTerm()).size()==0){
                entry.remove(head.getTerm());
                
            }
           }
            
        }

        for (int docID : finalScore.keySet()) {
            rankingImpl.add(docID, (finalScore.get(docID) / index.getDocNorm(docID)));
        }
        return rankingImpl;

    }
}

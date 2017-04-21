/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.ranking.impl;

import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.index.structure.Posting;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.ranking.SearchRankingDoc;
import es.uam.eps.bmi.search.ranking.lucene.LuceneRankingIterator;
import es.uam.eps.bmi.search.vsm.HeapContent;
import es.uam.eps.bmi.search.vsm.VSMDoc;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import org.apache.lucene.search.ScoreDoc;

/**
*
* @author  Francisco Andreu y Javier Hernandez
* @version 1.0
* @since   2017-02-10
*/
public class RankingImpl implements SearchRanking{

    private final Index index;
    private PriorityQueue<VSMDoc> results;
    private int cutoff =0;
   
    public RankingImpl(Index index, int cutoff) {
        
        this.index = index;
        this.cutoff = cutoff;
        this.results = new PriorityQueue<>(this.index.numDocs());  

    }
    
  /**
   * Calcula el tamaño de los scores
   * @return int El tamaño del ranking
   */         
    @Override
    public int size() {
        return this.results.size();
    }

    @Override
    public Iterator<SearchRankingDoc> iterator() {
         VSMDoc[] resultsArray = (VSMDoc[]) this.results.toArray(new VSMDoc[this.results.size()]);
         Arrays.sort(resultsArray, Collections.reverseOrder());
         return new RankingIteratorImpl(index,  Arrays.copyOfRange(resultsArray, 0, resultsArray.length < cutoff ? resultsArray.length : cutoff));  
    }

    public void add(int doc, double score) {
        VSMDoc vsmDoc = new VSMDoc(doc, score);
        this.results.add(vsmDoc);
    }

   
}

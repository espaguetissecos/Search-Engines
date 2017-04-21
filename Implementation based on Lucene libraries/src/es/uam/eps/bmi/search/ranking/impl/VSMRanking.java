/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.ranking.impl;

import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.ranking.SearchRankingDoc;
import es.uam.eps.bmi.search.ranking.lucene.LuceneRankingIterator;
import es.uam.eps.bmi.search.vsm.VSMDoc;
import java.util.Iterator;
import org.apache.lucene.search.ScoreDoc;

/**
*
* @author  Francisco Andreu y Javier Hernandez
* @version 1.0
* @since   2017-02-10
*/
public class VSMRanking implements SearchRanking{

        private final Index index;
	private final VSMDoc[] results;		
    
    public VSMRanking(Index index, VSMDoc[] results) {
        this.index = index;
        this.results = results;
    }
    
  /**
   * Calcula el tamaño de los scores
   * @return int El tamaño del ranking
   */         
    @Override
    public int size() {
        return this.results.length;
        

    }

    @Override
    public Iterator<SearchRankingDoc> iterator() {
        return new VSMRankingIterator(this.index, this.results);         
    }
}

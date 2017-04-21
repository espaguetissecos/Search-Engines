/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.ranking.lucene;

import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.ranking.SearchRankingDoc;
import java.util.Iterator;
import org.apache.lucene.search.ScoreDoc;

/**
*
* @author  Francisco Andreu y Javier Hernandez
* @version 1.0
* @since   2017-02-10
*/
public class LuceneRanking implements SearchRanking {

    private final Index index;
    private final ScoreDoc[] results;
		

    
    public LuceneRanking(Index index, ScoreDoc[] results) {
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
        
        return new LuceneRankingIterator(this.index, this.results); 
        
        
    }
    
}
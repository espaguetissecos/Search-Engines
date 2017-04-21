/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.ranking.impl;

import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.ranking.SearchRankingDoc;
import es.uam.eps.bmi.search.ranking.SearchRankingIterator;
import es.uam.eps.bmi.search.ranking.lucene.LuceneRankingDoc;
import es.uam.eps.bmi.search.vsm.VSMDoc;
import org.apache.lucene.search.ScoreDoc;

/**
*
* @author  Francisco Andreu y Javier Hernandez
* @version 1.0
* @since   2017-02-10
*/
public class RankingIteratorImpl implements SearchRankingIterator {

    
    VSMDoc results[];
    Index index;
    int n = 0;

    public RankingIteratorImpl (Index idx, VSMDoc r[]) {
        index = idx;
        results = r;
    }
    
    // Empty result list
    public RankingIteratorImpl () {
        index = null;
        results = new VSMDoc[0];
    }
    
    public boolean hasNext() {
        return n < results.length;
    }

    public SearchRankingDoc next() {
        return new RankingDocImpl(index, results[n++]);
    }
    
}

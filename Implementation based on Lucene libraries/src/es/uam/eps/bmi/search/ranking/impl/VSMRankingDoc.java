/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.ranking.impl;

import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.ranking.SearchRankingDoc;
import es.uam.eps.bmi.search.vsm.VSMDoc;
import java.io.IOException;
import org.apache.lucene.search.ScoreDoc;

/**
*
* @author  Francisco Andreu y Javier Hernandez
* @version 1.0
* @since   2017-02-10
*/
public class VSMRankingDoc extends SearchRankingDoc {

    Index index;
    VSMDoc rankedDoc;
    
    VSMRankingDoc (Index idx, VSMDoc r) {
        index = idx;
        rankedDoc = r;
    }
    public double getScore() throws IOException {
        return rankedDoc.score;
    }

    public String getPath() throws IOException {
        return index.getDocPath(rankedDoc.doc);
    }
    
    
}

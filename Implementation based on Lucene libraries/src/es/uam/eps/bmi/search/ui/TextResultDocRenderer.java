/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.ui;

import es.uam.eps.bmi.search.ranking.SearchRankingDoc;
import es.uam.eps.bmi.search.ranking.lucene.LuceneRankingDoc;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class TextResultDocRenderer extends ResultsRenderer {
    SearchRankingDoc result;
    
    public TextResultDocRenderer(SearchRankingDoc result) {
        this.result= result;
    }

    @Override
    public String toString() {
        try{
            return result.getScore()+"  "+result.getPath();
        } catch (IOException ex) {
            Logger.getLogger(TextResultDocRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}

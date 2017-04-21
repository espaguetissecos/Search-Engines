/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search;

import es.uam.eps.bmi.search.index.DocumentMap;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import java.io.IOException;

/**
 *
 * @author root
 */
public class CombinedEngine implements SearchEngine {

    public CombinedEngine(SearchEngine[] searchEngine, double[] d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SearchRanking search(String query, int cutoff) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DocumentMap getDocMap() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

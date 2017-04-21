/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.ranking;

import es.uam.eps.bmi.search.index.Index;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.search.ScoreDoc;

/**
*
* @author  Francisco Andreu y Javier Hernandez
* @version 1.0
* @since   2017-02-10
*/
public abstract class SearchRankingDoc implements Comparable<SearchRankingDoc>{
    protected Index index;
    protected ScoreDoc rankedDoc;

  /**
   * Compara entre dos docs
   * @return int negativo si el arg es mayor que el objeto, 0 si es igual y
   * postivo en otro caso.
   */    
    @Override    
    public int compareTo(SearchRankingDoc o) {
        
        try {
            int score= (int) this.getScore() - (int) o.getScore();
            return  (score==0) ? this.getPath().compareTo(o.getPath()) : score;
        } catch (IOException ex) {
            return -2;
        }
    }

    public  double getScore()  throws IOException{
                return rankedDoc.score;
    }

    public  String getPath()  throws IOException{
        return index.getDocPath(rankedDoc.doc);

    };
   
    
}

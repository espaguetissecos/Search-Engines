/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.vsm;


import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.index.structure.Posting;
import es.uam.eps.bmi.search.index.structure.PostingsList;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.ranking.impl.RankingImpl;
import java.io.IOException;


/**
 *
 * @author root
 */
public class TermBasedVSMEngine extends AbstractVSMEngine {


    public TermBasedVSMEngine(Index idx) {
        super(idx);
    }
    

    @Override
    public SearchRanking search(String q, int cutoff) throws IOException {
        
        String query[] = q.split(" ");
        for (String w : query){
            w = w.toLowerCase();
        }
        
        RankingImpl rankingImpl = new RankingImpl(index, cutoff);
        int numDocs = index.numDocs();
                
        double[] scoreAcc = new double[numDocs];
        for (String term : query) {
            PostingsList postingList = index.getPostings(term);
            for(Posting posting : postingList){
                double tfid = 0;
                Long freq = posting.getFreq();
                Integer docId = posting.getDocID();
                if (freq > 0){
                     tfid = tfidf(freq, index.getDocFreq(term), numDocs);
                }
                scoreAcc[docId] +=tfid;          
            }      
        }
    
        //Insertamos en el ranking los resultados
        
        for(int i =0; i < numDocs; i++){
            
            if (scoreAcc[i] != 0) 
                rankingImpl.add(i, (scoreAcc[i]/index.getDocNorm(i)));
           
           //Si no hace falta tener los valores anteriores vamos sobreescribiendo el valor del score con el acc                rankingImpl.add(i, scoreAcc[i]);
        }
   
        return rankingImpl;
    }
        
}

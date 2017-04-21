/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.proximal;

import es.uam.eps.bmi.search.AbstractEngine;
import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.index.lucene.LucenePositionalIndex;
import es.uam.eps.bmi.search.index.structure.Posting;
import es.uam.eps.bmi.search.index.structure.PostingsList;
import es.uam.eps.bmi.search.index.structure.PostingsListIterator;
import es.uam.eps.bmi.search.index.structure.positional.PositionalPosting;
import es.uam.eps.bmi.search.index.structure.positional.PositionsIterator;
import es.uam.eps.bmi.search.index.structure.positional.PositionsList;
import es.uam.eps.bmi.search.index.structure.positional.lucene.LucenePositionalPostingsIterator;
import es.uam.eps.bmi.search.index.structure.positional.lucene.LucenePositionalPostingsList;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.ranking.impl.RankingImpl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author eps
 */
public class ProximalEngine extends AbstractEngine{
        RankingImpl rankingImpl;

    public ProximalEngine(Index index) {
        super(index);
    }

    @Override
    public SearchRanking search(String q, int cutoff) throws IOException {
        
   
        boolean flag_literal = false;
        
        rankingImpl =  new RankingImpl(index, cutoff);
        HashMap<Integer, List<PositionalPosting>> docPositionList = new HashMap<>();
        
        if (q.startsWith("\"") && q.endsWith("\"")){
            flag_literal = true;
            q = q.substring(1, q.length()-1);
        }
        
        
        String query[] = q.split(" ");
        int numTerms = query.length;
      

        for ( String term : query){
            PostingsList postingslist = (PostingsList) this.index.getPostings(term);
            List<PositionalPosting> list;

            for (Posting p : postingslist) {              
                if(!docPositionList.containsKey(p.getDocID())){

                    list = new ArrayList<>();
                    list.add((PositionalPosting) p);
                    if(list.size()==numTerms){
                        float score;
                        if(flag_literal==true){
                            score = positionalScoreLiteral(docPositionList.get(p.getDocID()));
                        }else{
                            score = positionalScore(docPositionList.get(p.getDocID()));
                            
                        }
                        if(score > 0.0){
                              rankingImpl.add(p.getDocID(), score);
                        }
                        
                    }else{
                        
                        docPositionList.put(p.getDocID(), list);
                    }
                }else{
                    List<PositionalPosting> it = docPositionList.get(p.getDocID());
                    
                    it.add((PositionalPosting) p);
                    if(it.size()==numTerms ){
                        float score;
                        if(flag_literal==true){
                            score = positionalScoreLiteral(docPositionList.get(p.getDocID()));
                        }else{
                            score = positionalScore(docPositionList.get(p.getDocID()));
                            
                        }
                        if(score > 0.0){
                              rankingImpl.add(p.getDocID(), score);
                        }
                        
                    }
                   
                }
            }
           
        }
        
    return rankingImpl;
    }

    private float positionalScore(List<PositionalPosting> docPositionList) {
        
        int a,b;
        a = Integer.MIN_VALUE;
        int valor_actual = 0;
        Iterator<Integer> itr;
        List<Integer> valores = new ArrayList<>();
        List<Integer> valores_aux = new ArrayList<>();
        int flag = 0;
        float score = 0;
        while (flag != 1){
            for (PositionalPosting posting : docPositionList){
                itr = posting.iterator();
                flag = 1;
                
                while(itr.hasNext()){
                    valor_actual = itr.next();
                    if ( valor_actual > a ){
                        valores.add(valor_actual);
                        flag = 0;
                        break;
                    }
                }            
                if (flag == 1)
                    break;

            }
            if (flag == 1)
                break;
            b = Collections.max(valores);
            valores.clear();
            for (PositionalPosting posting : docPositionList){
                itr = posting.iterator();
                valores_aux.clear();

                while(itr.hasNext()){
                    valor_actual = itr.next();
                    if (valor_actual <= b){
                        valores_aux.add(valor_actual);
                    }
                }
                if(!valores_aux.isEmpty()){
                    valores.add(Collections.max(valores_aux));

                }

            }
            if(!valores.isEmpty()){
                a = Collections.min(valores);
            }
            
             int denominator = b - a - Math.abs(docPositionList.size()) + 2;
             if(denominator !=0){
                score += (1.0 /denominator);
             }else{
                 score +=0.0;
             }
        }
        return score;
    }

    private float positionalScoreLiteral(List<PositionalPosting> docPositionList) {
        
        float score = 0;
        PositionalPosting firstPosting = docPositionList.get(0);
        docPositionList.remove(firstPosting);
        for (Integer pos : firstPosting){     
            boolean flag =false;
            for(PositionalPosting posting : docPositionList ){
                flag = ((PositionsIterator)posting.iterator()).contains(pos+1);
                pos += 1;
                   
            } if(flag){
                score += 1.0;
            }
               
        }
       return score;
    }
   
}


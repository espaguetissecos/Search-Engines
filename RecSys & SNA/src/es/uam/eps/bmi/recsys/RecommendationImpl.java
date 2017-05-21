/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.recsys;

import es.uam.eps.bmi.recsys.ranking.Ranking;
import es.uam.eps.bmi.recsys.ranking.RankingElement;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author eps
 */
public class RecommendationImpl implements Recommendation{

    private HashMap<Integer, Ranking> rankings;
  
    public RecommendationImpl(){
        this.rankings = new HashMap<>();
    }
    
    @Override
    public Set<Integer> getUsers() {
        return this.rankings.keySet();
    }

    @Override
    public Ranking getRecommendation(int user) {
        return this.rankings.get(user);
    }

    @Override
    public void add(int user, Ranking ranking) {
        this.rankings.put(user, ranking);
    }

    @Override
    public void print(PrintStream out) {
        rankings.entrySet().forEach((entry) -> {
            Ranking ranking = entry.getValue();
            Iterator<RankingElement> it = ranking.iterator();
            while(it.hasNext()){
                RankingElement element= it.next();
                out.println(entry.getKey()+" "+ element.getID()+" "+element.getScore());

            }
        });
       

    }

    @Override
    public void print(PrintStream out, int userCutoff, int itemCutoff) {
        int nItem,nUsers=0;
        for (Map.Entry<Integer, Ranking> entry : rankings.entrySet()) {
            if(nUsers < userCutoff){
                Ranking ranking = entry.getValue();
            Iterator<RankingElement> it = ranking.iterator();
            nItem=0;
            while(it.hasNext() && nItem < itemCutoff){
                RankingElement element= it.next();
                out.println(entry.getKey()+" "+ element.getID()+" "+element.getScore());
                nItem++;

            }
            nUsers++;
            }
            
        }
        
         
       
        
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.recsys.recommender;

import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.ranking.Ranking;
import es.uam.eps.bmi.recsys.ranking.RankingElementImpl;
import es.uam.eps.bmi.recsys.ranking.RankingImpl;
import es.uam.eps.bmi.recsys.recommender.similarity.CosineUserSimilarity;
import es.uam.eps.bmi.recsys.recommender.similarity.Similarity;
import es.uam.eps.bmi.recsys.structure.Pair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

/**
 *
 * @author Javi
 */
public class UserKNNRecommender extends AbstractRecommender {
    
    
    HashMap<Integer, HashMap<Integer, Double>> ratingSum;
    
    /*Mirar los usuarios que han valorado cada item que no haya valorado el usuario y se hace el sumatorio sim(u,v)*rating(v,i) */
    /*También se puede hacer el recorrido como en el coseno*/
    public UserKNNRecommender(Ratings ratings, Similarity similarity, int k) {
        super(ratings);
        
        //Todo falta hacer algo con k ¿K es el número dde vecinos?
        //Lo he probado con una collección menos pequeña y está bien asique lo que está mal debe ser lo de la k o alguna mierda de esa
        ratingSum = new HashMap<>();
        HashMap<Integer, HashMap<Integer, Double>> similarities = new HashMap<>();
        ratings.getUsers().forEach((u) -> {
            double sum = 0;

            HashMap<Integer, Double> itemScore = new HashMap<>();
            Set<Integer> notRated = new HashSet<>(ratings.getItems());
            notRated.removeAll(ratings.getItems(u));
            
            HashMap<Integer, Double> map  =new HashMap<>();

            ratings.getUsers().stream().filter((v) -> (!Objects.equals(u, v))).forEachOrdered((v) -> {
                map.put(v, similarity.sim(u, v));
            });
            similarities.put(u, map);

            
            
            RankingImpl r = new RankingImpl(k);
            
            similarities.get(u).keySet().forEach((key) -> {
                r.add(key,similarities.get(u).get(key));
            });
            
            notRated.forEach((i) -> {
                double sumAux =0.0;
                Iterator it = r.iterator();
                
                while(it.hasNext()){
                    RankingElementImpl ele = (RankingElementImpl) it.next();
                     if(ratings.getRating(ele.getID(), i)!=null){

                        sumAux += ele.getScore()*ratings.getRating(ele.getID(), i);
                     }
                }                    
                    itemScore.put(i, sumAux);
                }
            );            
           ratingSum.put(u, itemScore);
        });
    }

    @Override
    public double score(int user, int item) {
        if(ratingSum.containsKey(user) && ratingSum.get(user).containsKey(item)){
            return ratingSum.get(user).get(item);
        }
        return -1;
            
    }
    @Override
    public String toString() {
        return "kNN based on user";
    }    
}

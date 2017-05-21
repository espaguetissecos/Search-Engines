/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.recsys.recommender;

import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.ranking.RankingElementImpl;
import es.uam.eps.bmi.recsys.ranking.RankingImpl;
import es.uam.eps.bmi.recsys.recommender.similarity.Similarity;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Javi
 */
public class NormUserKNNRecommender extends AbstractRecommender {

    /*Revisar estructura*/
    HashMap<Integer, HashMap<Integer, Double>> ratingSum;

    /* recorrer con un solo bucle for(i=0;i <20; i++)*/
    public NormUserKNNRecommender(Ratings ratings, Similarity similarity, int k, int i) {

        super(ratings);
        ratingSum = new HashMap<>();

        HashMap<Integer, HashMap<Integer, Double>> similarities = new HashMap<>();
        ratings.getUsers().forEach((u) -> {
            double sum = 0;

            HashMap<Integer, Double> itemScore = new HashMap<>();
            Set<Integer> notRated = new HashSet<>(ratings.getItems());
            notRated.removeAll(ratings.getItems(u));

            HashMap<Integer, Double> map = new HashMap<>();

            ratings.getUsers().stream().filter((v) -> (!Objects.equals(u, v))).forEachOrdered((v) -> {
                map.put(v, similarity.sim(u, v));
            });
            similarities.put(u, map);

            RankingImpl r = new RankingImpl(k);

            
            similarities.get(u).keySet().forEach((v) -> {
                if (ratings.getItems(v).size() > i) 
                    r.add(v, similarities.get(u).get(v));
                
            });

            for( int item : notRated){
                double sumAux = 0.0;
                double norm = 0.0;

                Iterator it = r.iterator();
                while (it.hasNext()) {
                    RankingElementImpl ele = (RankingElementImpl) it.next();
                    if (ratings.getRating(ele.getID(), item) != null) {
                        norm += Math.abs(ele.getScore());
                        sumAux += ele.getScore() * ratings.getRating(ele.getID(), item);
                    }
                }
                double score = sumAux / norm;
                if (Double.isNaN(score))
                    itemScore.put(item, 0.0);
                else
                    itemScore.put(item, score);
            }
            
            ratingSum.put(u, itemScore);
        });
        
      

    }

    @Override
    public double score(int user, int item) {
        if (ratingSum.containsKey(user) && ratingSum.get(user).containsKey(item)) {
            
            return ratingSum.get(user).get(item);
        }
        return -1;//To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String toString() {
        return "Normalized kNN based on user ";
    }  
}

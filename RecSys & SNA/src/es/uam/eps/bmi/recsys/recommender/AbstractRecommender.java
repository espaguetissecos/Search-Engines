/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.recsys.recommender;

import es.uam.eps.bmi.recsys.Recommendation;
import es.uam.eps.bmi.recsys.RecommendationImpl;
import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.ranking.RankingImpl;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author e303736
 */
public abstract class AbstractRecommender implements Recommender{

    
    protected Ratings ratings;
    
    public AbstractRecommender(Ratings ratings) {
        this.ratings = ratings;
    }
   
    public abstract double score (int user, int item);
    
    public Recommendation recommend(int cutoff){
        RecommendationImpl rec = new RecommendationImpl();
        for(Integer user : ratings.getUsers()){
            RankingImpl ranking = new RankingImpl(cutoff);
            List<Integer> itemsNotRated = new ArrayList<>(ratings.getItems());
            itemsNotRated.removeAll(ratings.getItems(user));
            for(Integer item : itemsNotRated){
                ranking.add(item, this.score(user, item));
            }
            rec.add(user, ranking);
            
        }
        return rec;
    }

    
    
    
        
}

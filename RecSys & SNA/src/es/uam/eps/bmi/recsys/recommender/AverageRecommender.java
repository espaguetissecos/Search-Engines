/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.recsys.recommender;

import es.uam.eps.bmi.recsys.data.Ratings;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author eps
 */
public class AverageRecommender extends AbstractRecommender  {
     
    Map<Integer,Double> ratingSum;

    Integer minimumNumberRatings;
    

    public AverageRecommender(Ratings ratings, int i) {
        super(ratings);
        int number;
        minimumNumberRatings = i;
        ratingSum = new HashMap<>();
        for (int item : ratings.getItems()) {
            number =0;
            double sum = 0;
            for (int u : ratings.getUsers(item)){
                sum += ratings.getRating(u, item);
                number++;
            }
            if(number >= minimumNumberRatings)
                ratingSum.put(item, sum);
        }
    }

    @Override
    public double score(int user, int item) {
        int userThatHaveRatedTheItem = this.ratings.getUsers(item).size();
        if(ratingSum.containsKey(item)){
            return ratingSum.get(item)/userThatHaveRatedTheItem;
        }else
            //Todo ver
            return -1;
    }
    
    public String toString() {
        return "average";
    }
    
}

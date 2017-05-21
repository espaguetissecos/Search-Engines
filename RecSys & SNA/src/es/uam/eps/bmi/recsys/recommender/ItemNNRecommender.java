/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.recsys.recommender;

import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.recommender.similarity.Similarity;
import java.util.HashMap;
import java.util.HashSet;

import java.util.Set;

/**
 *
 * @author Javi
 */
public class ItemNNRecommender extends AbstractRecommender{
    HashMap<Integer, HashMap<Integer, Double>> ratingSum;

    public ItemNNRecommender(Ratings ratings, Similarity similarity) {
        super(ratings);
               
        //Todo falta hacer algo con k ¿K es el número dde vecinos?
        //Lo he probado con una collección menos pequeña y está bien asique lo que está mal debe ser lo de la k o alguna mierda de esa
        ratingSum = new HashMap<>();
        ratings.getUsers().forEach((u) -> {
                HashMap<Integer, Double> itemScore = new HashMap<>();
                Set<Integer> notRated = new HashSet<>(ratings.getItems());
                notRated.removeAll(ratings.getItems(u));
                for (int i : notRated) {
                    double sum = 0;
                    sum = ratings.getItems(u).stream().map((j) -> similarity.sim(i, j)*ratings.getRating(u, j)).reduce(sum, (accumulator, _item) -> accumulator + _item);
                    itemScore.put(i, sum);
                    //System.out.println(u + "    "+i +" "+ sum);
                }
            
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
    public String toString() {
        return "kNN based on item";
    }
}

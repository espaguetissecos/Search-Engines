/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.recsys.recommender;

import es.uam.eps.bmi.recsys.data.Features;
import es.uam.eps.bmi.recsys.data.FeaturesImpl;
import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.recommender.similarity.FeatureSimilarity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Javi
 */
public class CentroidRecommender<F> extends AbstractRecommender{

        HashMap<Integer, HashMap<Integer, Double>> ratingSum;

    public CentroidRecommender(Ratings ratings) {
        super(ratings);
    }

    public CentroidRecommender(Ratings ratings, FeatureSimilarity sim) {
        super(ratings);
        Features fItems = sim.getFeatures();
        ratingSum = new HashMap<>();
        for (int u : ratings.getUsers()){
            
            
            HashMap<Integer, Double> itemScore = new HashMap<>();
            Set<Integer> notRated = new HashSet<>(ratings.getItems());
            notRated.removeAll(ratings.getItems(u));
            Set<Integer> itemlist = ratings.getItems(u);
            int size = itemlist.size();
            
            Features f = new FeaturesImpl();
            
           
            
            HashMap<Object, Double> matrix = new HashMap<>();
            
            for (int item : itemlist){
                if(fItems.getFeatures(item)==null)
                    continue;
                
                Iterator<F>it = fItems.getFeatures(item).iterator();
                
                while(it.hasNext()){
                    F feat = it.next();
                    if(matrix.containsKey(feat)){
                      matrix.put(feat,matrix.get(feat)+(fItems.getFeature(item, feat) * ratings.getRating(u, item)));

                    }else{
                    matrix.put(feat, (fItems.getFeature(item, feat) * ratings.getRating(u, item)));
                    }
                }
                
                
               
                
            }
            for (Object key: matrix.keySet()){
                f.setFeature(u, key, matrix.get(key)/size);
            }
            sim.setXFeatures(f);
            for (int i : notRated){
                if(sim.sim(u, i)!=-1){
                    itemScore.put(i, sim.sim(u,i));

                }
            }
            this.ratingSum.put(u, itemScore);
            
            
        }
    }

    @Override
    public String toString() {
        return "Centroid Based ";
    }  
 

    @Override
    public double score(int user, int item) {
        if(this.ratingSum.containsKey(user) && this.ratingSum.get(user).containsKey(item)){
            return this.ratingSum.get(user).get(item);
        }
        return -1;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.recsys.recommender.similarity;

import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.structure.Pair;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Javi
 */
public class CosineItemSimilarity  implements Similarity{
    HashMap<Integer, Double> listSum;
    Ratings ratings;

    public CosineItemSimilarity(Ratings ratings) {
        this.ratings = ratings;
        listSum = new HashMap<>();
        ratings.getItems().forEach((i) -> {
            listSum.put(i, ratings.getUsers(i).stream().mapToDouble((user) -> Math.pow(ratings.getRating(user, i), 2)).sum());
           
        });    

}

    @Override
    public double sim(int x, int y) {
        double sumI = listSum.get(x);
        double sumJ = listSum.get(y);
        double sumNum =0;
        if (!Objects.equals(x, y)) { 
                for(int u: ratings.getUsers(x)){
                    sumNum = ratings.getUsers(y).stream().filter((v) -> (u==v)).map((_item) -> ratings.getRating(u, x)*ratings.getRating(u, y)).reduce(sumNum, (accumulator, _item) -> accumulator + _item);
                }
                         
        }
        return (sumNum)/((Math.sqrt(sumI))*Math.sqrt(sumJ));

    }
    
}

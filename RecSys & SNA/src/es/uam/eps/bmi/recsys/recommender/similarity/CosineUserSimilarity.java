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
public class CosineUserSimilarity  implements Similarity{
    HashMap<Pair<Integer, Integer>, Double> listSim;
    
    //Todo triple cambiar todo recorrer con un solo bucle for(i=0;i <20; i++)
    public CosineUserSimilarity(Ratings ratings) {
        listSim = new HashMap<>();
        /*Todo también se puede recorrer como se hace en el kNNRecommender recorrer users, recorrer los items que le gustan al u y comprobar coincidan*/
        ratings.getUsers().forEach((u) -> {
            double sumU = ratings.getItems(u).stream().mapToDouble((item) -> Math.pow(ratings.getRating(u, item), 2)).sum();

            ratings.getUsers().forEach((v) -> {
                Pair<Integer, Integer> pair= new Pair<>(u,v);
                if (!listSim.containsKey(pair)) {
                    if (!Objects.equals(u, v)) {
                        double sumNum =0;
                        double sumV = ratings.getItems(v).stream().mapToDouble((item) -> Math.pow(ratings.getRating(v, item), 2)).sum();
                        Set<Integer> common = new HashSet<>(ratings.getItems(u));
                        common.retainAll(ratings.getItems(v));
                        sumNum = common.stream().map((comm) -> ratings.getRating(u, comm)*ratings.getRating(v, comm)).reduce(sumNum, (accumulator, _item) -> accumulator + _item);
              
                        listSim.put(new Pair(u,v),sumNum/((Math.sqrt(sumU))*Math.sqrt(sumV)));
                    }
                }
            });
        });
        
    }

    @Override
    public double sim(int x, int y) {

        return listSim.get(new Pair(x,y));
    }
    
}

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
public class PearsonSimilarity  implements Similarity{
    HashMap<Pair<Integer, Integer>, Double> listSim;
    
    //Todo triple cambiar todo recorrer con un solo bucle for(i=0;i <20; i++)
    public PearsonSimilarity(Ratings ratings) {
        listSim = new HashMap<>();
        /*Todo también se puede recorrer como se hace en el kNNRecommender recorrer users, recorrer los items que le gustan al u y comprobar coincidan*/
        ratings.getUsers().forEach((Integer u) -> {
            ratings.getUsers().forEach((Integer v) -> {
                Pair<Integer, Integer> pair= new Pair<>(u,v);
                if (!listSim.containsKey(pair)) {
                    if (!Objects.equals(u, v)) {
                        double sumNum =0;
                        Double avgU;
                        Double avgV;
                        double sumUAVG =0;
                        double sumVAVG =0;
                        sumUAVG= ratings.getItems(u).stream().map((item) -> ratings.getRating(u, item)).reduce(sumUAVG, (accumulator, _item) -> accumulator + _item);
                        avgU= sumUAVG/ratings.getItems(u).size();
                        sumVAVG= ratings.getItems(v).stream().map((item) -> ratings.getRating(v, item)).reduce(sumVAVG, (accumulator, _item) -> accumulator + _item);
                        avgV= sumVAVG/ratings.getItems(v).size();
                        double sumU = ratings.getItems(u).stream().mapToDouble((item) -> Math.pow(ratings.getRating(u, item)-avgU, 2)).sum();
                        double sumV = ratings.getItems(v).stream().mapToDouble((item) -> Math.pow(ratings.getRating(v, item)-avgV, 2)).sum();
                        Set<Integer> common = new HashSet<>(ratings.getItems(u));
                        common.retainAll(ratings.getItems(v));
                        sumNum = common.stream().map((comm) -> (ratings.getRating(u, comm)-avgU)*(ratings.getRating(v, comm)-avgV)).reduce(sumNum, (accumulator, _item) -> accumulator + _item);
                        
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
   


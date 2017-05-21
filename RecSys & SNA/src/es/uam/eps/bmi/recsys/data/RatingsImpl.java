/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.recsys.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author e303736
 */
public class RatingsImpl implements Ratings{
    
    private HashMap<Integer, HashMap<Integer, Double>> userratings;

    
    private HashMap<Integer, HashMap<Integer, Double>> itemratings;

    
    
    public RatingsImpl() {
        this.userratings = new HashMap<>();
        this.itemratings = new HashMap<>();
        
    }
     public RatingsImpl(HashMap<Integer, HashMap<Integer, Double>> userratings, HashMap<Integer, HashMap<Integer, Double>> itemratings) {
        this.userratings = userratings;
        this.itemratings = itemratings;
    }

    public RatingsImpl(String ratingsFile, String separator) {
        this.userratings = new HashMap<>();
        this.itemratings = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ratingsFile))) {
            String sCurrentLine;
            Integer user;
            Integer item;
            Double rating;
            String[] elements;
            while ((sCurrentLine = br.readLine()) != null) {
                elements = sCurrentLine.split(separator);
                try{
                    user = Integer.parseInt(elements[0]);
                    item = Integer.parseInt(elements[1]);
                    rating = Double.parseDouble(elements[2]);
                    rateAux(user, item, rating);
                }catch(NumberFormatException ex){
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(RatingsImpl.class.getName()).log(Level.SEVERE, null, ex);
        } 

        
        
    }
    

    @Override
    public void rate(int user, int item, Double rating) {
        if(this.userratings.containsKey(user)){
            this.userratings.get(user).put(item, rating);    
        }else{
            HashMap<Integer, Double> scores = new HashMap<>();
            scores.put(item, rating);
            this.userratings.put(user, scores);
        }
        if(this.itemratings.containsKey(item)){
            this.itemratings.get(item).put(user, rating);    
        }else{
            HashMap<Integer, Double> scores = new HashMap<>();
            scores.put(user, rating);
            this.itemratings.put(item, scores);
        }
        
        
    }

    @Override
    public Double getRating(int user, int item) {
        if(this.userratings.containsKey(user))
            if(this.userratings.get(user).containsKey(item))  
                 return this.userratings.get(user).get(item);
        
        return null;
    }

    @Override
    public Set<Integer> getUsers(int item) {
        if(this.itemratings.containsKey(item))
            return this.itemratings.get(item).keySet();
        
        return null;
    }

    @Override
    public Set<Integer> getItems(int user) {
        if(this.userratings.containsKey(user))
            return userratings.get(user).keySet();
        return null;
        
    }

    @Override
    public Set<Integer> getUsers() {
        return this.userratings.keySet();
    }

    @Override
    public Set<Integer> getItems() {
       return this.itemratings.keySet();
    }

    @Override
    public int nRatings() {
        int cont=0;
        cont = this.userratings.values().stream().map((map) -> map.values().size()).reduce(cont, Integer::sum);
        return cont;
    }
     
    /*Todo revisar*/
    @Override
    public Ratings[] randomSplit(double ratio) {
       
        HashMap<Integer, HashMap<Integer, Double>> []userRatings = new HashMap[]{new HashMap<>(), new HashMap<>()};
        userratings.entrySet().stream().forEach((entry) -> {
            double numero = (Math.random() * (0-1) + 1);
            if(numero > ratio){
                userRatings[1].put(entry.getKey(), entry.getValue());
            }else {
                userRatings[0].put(entry.getKey(), entry.getValue());
            }
        });

        HashMap<Integer, HashMap<Integer, Double>> []itemsRatings = new HashMap[]{ new HashMap<>(),new HashMap<>() };        
         itemratings.entrySet().stream().forEach((entry) -> {
            double numero = (Math.random() * (0-1) + 1);
            if(numero > ratio){
                userRatings[1].put(entry.getKey(), entry.getValue());
            }else {
                userRatings[0].put(entry.getKey(), entry.getValue());
            }
        });
        
        return new Ratings [] { new RatingsImpl(userRatings[0], itemsRatings[0]), new RatingsImpl(userRatings[1], itemsRatings[1]) };
 
    }

    private void rateAux(Integer user, Integer item, Double rating) {
        rate(user, item, rating);
    }

   
}

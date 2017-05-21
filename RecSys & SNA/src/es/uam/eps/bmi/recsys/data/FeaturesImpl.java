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
 * @author eps
 * @param <F>
 */
public class FeaturesImpl<F> implements Features<F>{
    
    private HashMap<Integer, HashMap<F, Double>> features;
    
    public FeaturesImpl (){
        this.features = new HashMap<>();
    }
    
    public FeaturesImpl(String featuresFile, String separator, Parser<F> featureParser){
        this.features = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(featuresFile))) {
            String sCurrentLine;
            Integer item;
            F tag;
            Double count;
            String[] elements;
            while ((sCurrentLine = br.readLine()) != null) {
                elements = sCurrentLine.split(separator);
                try{
                    item = Integer.parseInt(elements[0]);
                    tag = featureParser.parse(elements[1]);
                    count = Double.parseDouble(elements[2]);
                    addFeature(item, tag,count);
                    
                }catch(NumberFormatException ex){
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(RatingsImpl.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }
    
    public Set<F> getFeatures(int id){
        if (this.features.containsKey(id))
            return this.features.get(id).keySet();
        return null;
    }
    
    public Double getFeature(int id, F feature){
        if (this.features.containsKey(id) && this.features.get(id).containsKey(feature))
            return this.features.get(id).get(feature);
        return null;
    }
    
    public void setFeature(int id, F feature, double value){
        if(this.features.containsKey(id)){
            this.features.get(id).put(feature, value);    
        }else{
            HashMap<F, Double> freq = new HashMap<>();
            freq.put(feature, value);
            this.features.put(id, freq);
        }
        
        
       
    }
    
    public Set<Integer> getIDs(){
        return this.features.keySet();
    }

    private void addFeature(Integer item, F tag, double count) {
        setFeature(item, tag, count);
    }
    
   
}

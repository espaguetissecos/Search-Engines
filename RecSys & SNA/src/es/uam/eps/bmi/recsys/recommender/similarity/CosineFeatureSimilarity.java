/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.recsys.recommender.similarity;

import es.uam.eps.bmi.recsys.data.Features;
import java.util.Iterator;

/**
 *
 * @author Javi
 */
public class CosineFeatureSimilarity<F> extends FeatureSimilarity {
    
    public CosineFeatureSimilarity(Features features) {
        super(features);
        
        
    }

    @Override
    public double sim(int x, int y) {
        
       if(this.yFeatures.getFeatures(y)!=null && this.xFeatures.getFeatures(x)!=null){

        Iterator<F>it_x = this.xFeatures.getFeatures(x).iterator();
       
        double contador = 0;
        double mod_y = 0;
        double mod_x = 0;
        boolean flag = true;
        while (it_x.hasNext()){
            F f_x = (F) it_x.next();
            Iterator<F>it_y = this.yFeatures.getFeatures(y).iterator();

            while(it_y.hasNext()){
                F f_y = (F) it_y.next();
                if(f_y.equals(f_x)){
                    contador += this.xFeatures.getFeature(x, f_x)* this.yFeatures.getFeature(y, f_y);
                }
                if(flag){
                    mod_y += Math.pow(this.yFeatures.getFeature(y, f_y), 2);
                }
            }
            flag = false;

            mod_x += Math.pow(this.xFeatures.getFeature(x, f_x), 2);

        }


        mod_x = Math.sqrt(mod_x);
        mod_y = Math.sqrt(mod_y);
        
        return (contador/(mod_x*mod_y));
        
        }
       return -1;
    }
        
        

    
    
 
    
    
}

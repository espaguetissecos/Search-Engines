/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.recsys.recommender.similarity;

import es.uam.eps.bmi.recsys.data.Features;

/**
 *
 * @author Javi
 */
public class JaccardFeatureSimilarity extends FeatureSimilarity{

    public JaccardFeatureSimilarity(Features features) {
        super(features);
    }

    @Override
    public double sim(int x, int y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

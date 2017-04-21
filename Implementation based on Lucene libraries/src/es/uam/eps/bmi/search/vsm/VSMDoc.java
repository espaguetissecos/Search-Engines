/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.vsm;

import org.apache.lucene.search.ScoreDoc;

/**
*
* @author  Francisco Andreu y Javier Hernandez
* @version 1.0
* @since   2017-02-10
*/

public class VSMDoc implements Comparable{
    public int doc;
    public float score;


    public VSMDoc(int id, float score) {
        this.doc = id;
        this.score = score;
    }

    @Override
    public int compareTo(Object o) {
        VSMDoc d = (VSMDoc) o;
        return this.score < d.score ? -1 : this.score > d.score ? 1 : 0; 
    }

    
}
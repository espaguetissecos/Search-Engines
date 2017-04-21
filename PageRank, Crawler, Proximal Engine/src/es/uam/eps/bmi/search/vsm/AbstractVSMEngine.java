package es.uam.eps.bmi.search.vsm;

import es.uam.eps.bmi.search.AbstractEngine;
import es.uam.eps.bmi.search.index.Index;

/**
 *
 * @author pablo
 */
public abstract class AbstractVSMEngine extends AbstractEngine {
    public AbstractVSMEngine(Index index) {
        super(index);
    }
    
    public static double tfidf(long freq, long docFreq, int numDocs) {
        return (1 + Math.log(freq)) * (1 + Math.log(numDocs) - Math.log(1 + docFreq));
    }
}

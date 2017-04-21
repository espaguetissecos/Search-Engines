/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.index;

import es.uam.eps.bmi.search.index.freq.FreqVector;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Kaiko
 */
public interface Index {
    
    /**
     * Cargar Index
     * @param ruta ruta del indice creado
     */
    public void load(String ruta) throws IOException;
    
    /**
     * Devuelve los identificadores de los resultados
     * 
     * @return  identificadores de los documentos indexados
     */
    List<String> getIDs();

    /**
     * Devuelve la lista de términos extraídos de los documentos indexados
     * 
     * @return lista de términos extraídos
     */
    List<String> getAllTerms();
    

    public String getDocPath(int doc);

    public long getTermDocFreq(String t2);

    public FreqVector getDocVector(int docID);

    public long getTermFreq(String word, int docID);

    public long getTermTotalFreq(String t2);

    

}
    


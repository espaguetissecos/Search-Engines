/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.vsm;

import es.uam.eps.bmi.search.AbstractEngine;
import es.uam.eps.bmi.search.index.NoIndexException;
import es.uam.eps.bmi.search.index.lucene.LuceneIndex;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.ranking.impl.VSMRanking;
import es.uam.eps.bmi.search.ranking.impl.VSMRankingDoc;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexNotFoundException;
import org.apache.lucene.store.FSDirectory;

/**
*
* @author  Francisco Andreu y Javier Hernandez
* @version 1.0
* @since   2017-02-10
*/
public class VSMEngine extends AbstractEngine {

    public VSMEngine(String path) throws IOException {
        super(path);

    }

    /**
     * Lee el directorio donde esta el index.
     *
     * @param path : La ruta del indice
     * @throws java.io.IOException
     */
    @Override
    public void loadIndex(String path) throws IOException {
        this.indexFolder = path;
        Path p = Paths.get(path);
        this.index = new LuceneIndex(path);
        LuceneIndex index_aux = (LuceneIndex) this.index;
        index_aux.indexReader = DirectoryReader.open(FSDirectory.open(p));

    }

    /**
     * Calcula los tf-idf para cada término y doc y posteriormente realiza el 
     * producto escalar entre los vectores. De ese producto lo divide entre el
     * módulo de Q(el cual se calcula en esta misma funcion puesto que el coste
     * computacional no es muy alto) multiplicado por el modulo de D(que ha sido
     * previamente calculado en el Builder)
     *
     * @param query : La query a buscar
     * @param cutoff : Hasta que posicion del ranking devuelve
     * @return
     * @throws IOException
     */
    @Override
    //*Me lo he inventado entero*//
    public SearchRanking search(String query, int cutoff) throws IOException {

        float tf;
        float idf;
        int n_term;
        float xEscalar;
        float score;
        float normalizedD;
        float sumPowD;
        float normalizedQ;
        float sumPowQ;
        float moduloQ = 0;
        int i = 0;

        //try {
        // this.loadIndex();
        LuceneIndex index_aux = (LuceneIndex) this.index;

        String[] terms = query.split(" ");
        float[] moduloV = new float[index_aux.indexReader.numDocs()];
        VSMDoc[] scores = new VSMDoc[index_aux.indexReader.numDocs()];
        float[] d = new float[terms.length];
        float[] q = new float[terms.length];

        FileReader fr;
        File archivo ;
        BufferedReader br;
        archivo = new File (this.indexFolder + "/modulos.txt");
        fr = new FileReader (archivo);
        br = new BufferedReader(fr);
        
        // Lectura del fichero
        String linea;
        while((linea=br.readLine())!=null){
            moduloV[i] = Float.parseFloat(linea);
            i++;
        }
           
        for (int id = 0; id < index_aux.indexReader.numDocs(); id++) {
            n_term = 0;
            xEscalar = 0;
            normalizedD = 0;
            sumPowD = 0;
            normalizedQ = 0;
            sumPowQ = 0;
            score = 0;

            for (String term : terms) {

                tf = (float) ((index_aux.getTermFreq(term, id) <= 0) ? 0 : (1 + (Math.log(index_aux.getTermFreq(term, id)) / Math.log(2))));
                idf = (float) (index_aux.getTermDocFreq(term) <= 0 ? 0 : ((Math.log(1 + (index_aux.indexReader.numDocs() / (1 + index_aux.getTermDocFreq(term))))) / Math.log(2)));
                d[n_term] = tf * idf;
                q[n_term] = tf;

                n_term++;
            }
            for (i = 0; i < terms.length; i++) {
                //El producto escalar de la query en el documento "id"                    
                xEscalar = xEscalar + (d[i] * q[i]);

            }

            for (i = 0; i < q.length; i++)
                moduloQ += Math.pow(q[i], 2);
            moduloQ = xEscalar != 0 ? (float) Math.sqrt(moduloQ) : 1;

            score = (xEscalar)/(moduloV[id] * moduloQ);
            scores[id] = new VSMDoc(id, score);
            moduloQ = 0;

        }

        Arrays.sort(scores, Collections.reverseOrder());

        VSMDoc[] topdocs = Arrays.copyOfRange(scores, 0, scores.length < cutoff ? scores.length : cutoff);

        return new VSMRanking(index_aux, topdocs);
    }
}


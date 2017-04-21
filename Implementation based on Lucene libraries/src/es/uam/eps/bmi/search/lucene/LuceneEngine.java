/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.lucene;

import es.uam.eps.bmi.search.AbstractEngine;
import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.index.NoIndexException;
import es.uam.eps.bmi.search.index.lucene.LuceneIndex;
import es.uam.eps.bmi.search.index.lucene.LuceneIndexBuilder;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.ranking.SearchRankingIterator;
import es.uam.eps.bmi.search.ranking.lucene.LuceneRanking;
import es.uam.eps.bmi.search.ranking.lucene.LuceneRankingDoc;
import es.uam.eps.bmi.search.ranking.lucene.LuceneRankingIterator;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexNotFoundException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
*
* @author  Francisco Andreu y Javier Hernandez
* @version 1.0
* @since   2017-02-10
*/
public class LuceneEngine extends AbstractEngine {
    
  
    
    /**
     * 
     * @param path
     * @throws IOException 
     */
    public LuceneEngine(String path) throws IOException {
        super(path);

    }

    /**
     * Lee el directorio donde esta el index.
     *
     * @param path : La ruta del indice
     * @throws java.io.IOException
     */
    @Override
    public void loadIndex(String path) throws IOException{
        try{
            this.indexFolder = path;
            Path p = Paths.get(path);
            this.index = new LuceneIndex(path);
            LuceneIndex index_aux = (LuceneIndex)this.index; 
           
            index_aux.indexReader = DirectoryReader.open(FSDirectory.open(p));
        }catch(IndexNotFoundException e){
            throw  new NoIndexException(this.indexFolder);
        }
            

            
        
        
        
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
    public SearchRanking search(String query, int cutoff) throws IOException {
        
        LuceneIndex index_aux = (LuceneIndex)this.index; 

        try {
            Analyzer analyzer = new StandardAnalyzer();
            IndexSearcher indexSearcher = new IndexSearcher(index_aux.indexReader);
            QueryParser queryParser = new QueryParser(LuceneIndexBuilder.getCONTENTS(), analyzer);
            Query queryQuery = queryParser.parse(query);
            TopDocs search = indexSearcher.search(queryQuery, cutoff);            
            return new LuceneRanking(index_aux, search.scoreDocs);
          
        } catch (IndexNotFoundException|NullPointerException|ParseException ex){
            throw new NoIndexException(this.indexFolder);
            
        }
    }

}

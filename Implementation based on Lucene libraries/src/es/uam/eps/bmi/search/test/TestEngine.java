package es.uam.eps.bmi.search.test;

import es.uam.eps.bmi.search.SearchEngine;
import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.index.IndexBuilder;
import es.uam.eps.bmi.search.index.NoIndexException;
import es.uam.eps.bmi.search.index.freq.FreqVector;
import es.uam.eps.bmi.search.index.freq.TermFreq;
import es.uam.eps.bmi.search.index.lucene.LuceneIndex;
import es.uam.eps.bmi.search.index.lucene.LuceneIndexBuilder;
import es.uam.eps.bmi.search.lucene.LuceneEngine;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.ranking.SearchRankingDoc;
import es.uam.eps.bmi.search.ui.TextResultDocRenderer;
import es.uam.eps.bmi.search.vsm.VSMEngine;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author pablo
 */
public class TestEngine {
    public static void main (String a[]) throws IOException {
        
        testCollection ("src/es/uam/eps/bmi/search/ranking", "index", "size", "public abstract");
        testCollection ("collections/docs.zip", "index", "seat", "obama family tree");
       testCollection ("collections/urls.txt", "index", "wikipedia", "information probability");
        testSearch (new VSMEngine("index"), "information probability", 5);
    }
    
    static void testCollection(String collectionPath, String indexPath, String word, String query) throws IOException {
        
        // Prueba de creación de índice
        
        IndexBuilder builder = new LuceneIndexBuilder();
        builder.build(collectionPath, indexPath);
        
        //Pruebas de inspección del índice
        
        Index index = new LuceneIndex(indexPath);
        
        List<String> terms = index.getAllTerms();
        
        Collections.sort(terms, (String t1, String t2) -> (int) Math.signum(index.getTermTotalFreq(t2) - index.getTermTotalFreq(t1)));
        
        System.out.println("------------------------------");
        System.out.println("Collection: " + collectionPath);
        System.out.println("\n  Most frequent terms:");
        terms.subList(0, 5).forEach((term) -> {
            System.out.println("\t" + term + "\t" + index.getTermTotalFreq(term));
        });
        
        int docID = 2;
        FreqVector vector = index.getDocVector(docID);
        int initialTerm = (int) vector.size() / 2, nTerms = 5;
        System.out.print("\n  A few term frequencies for docID = " + docID + " - " +index.getDocPath(docID) + ": ");
        int i = 0;
        for (TermFreq f : vector) 
            if (++i >= initialTerm && i < initialTerm + nTerms) System.out.print(f.getTerm() + " (" + f.getFreq() + ") ");
        System.out.println();
        System.out.println("\n  Frequency of word \"" + word + "\" in document " + docID + " - " + index.getDocPath(docID) + ": " + index.getTermFreq(word, docID));
        System.out.println("\n  Total frequency of word \"" + word + "\" in the collection: " + index.getTermTotalFreq(word) + " occurrences over " + index.getTermDocFreq(word) + " documents\n");
        
        // Pruebas de búsqueda
        
        int cutoff = 5;
        // Provocamos error
         SearchEngine engine = new LuceneEngine("prueba");

        try {

            testSearch (engine, query, cutoff);
        } catch (NoIndexException ex) {
            System.out.println ("\n  No index found in " + ex.getFolder()+ "!\n");
        }
        
        engine.loadIndex(indexPath);
        testSearch (engine, query, cutoff);
    }
    
    static void testSearch (SearchEngine engine, String query, int cutoff) throws IOException {
        SearchRanking ranking = engine.search(query, cutoff);
        System.out.println("  Top " + cutoff + " results for query \"" + query + "\"");
        for (SearchRankingDoc result : ranking)
            System.out.println("\t" + new TextResultDocRenderer(result));
        System.out.println();
    }
}

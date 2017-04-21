package es.uam.eps.bmi.search.test;

import es.uam.eps.bmi.search.SearchEngine;
import es.uam.eps.bmi.search.index.Config;
import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.index.impl.DiskIndex;
import es.uam.eps.bmi.search.index.impl.DiskIndexBuilder;
import es.uam.eps.bmi.search.index.impl.SerializedRAMIndex;
import es.uam.eps.bmi.search.index.impl.SerializedRAMIndexBuilder;
import es.uam.eps.bmi.search.index.lucene.LuceneForwardIndex;
import es.uam.eps.bmi.search.index.lucene.LuceneForwardIndexBuilder;
import es.uam.eps.bmi.search.index.lucene.LuceneIndex;
import es.uam.eps.bmi.search.index.lucene.LuceneIndexBuilder;
import es.uam.eps.bmi.search.index.structure.Posting;
import es.uam.eps.bmi.search.lucene.LuceneEngine;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.ranking.SearchRankingDoc;
import es.uam.eps.bmi.search.ui.TextResultDocRenderer;
import es.uam.eps.bmi.search.util.Timer;
import es.uam.eps.bmi.search.vsm.DocBasedVSMEngine;
import es.uam.eps.bmi.search.vsm.SlowVSMEngine;
import es.uam.eps.bmi.search.vsm.TermBasedVSMEngine;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

/**
 *
 * @author pablo
 */
public class TestEngine {
      public static PrintStream out; 

    public static void main (String a[]) throws IOException {
        out = new PrintStream("Output.txt");
        ///////////////////////////////////
        // Índices: pruebas de correción //
        ///////////////////////////////////
        
        String collPath = "collections/urls.txt";
        String baseIndexPath = "index/urls/";
        
        out.println("-----------------------");
        //long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        // Construcción
        new LuceneForwardIndexBuilder().build(collPath, baseIndexPath + "/lucene/forward");
        //long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
       
        //beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        new LuceneIndexBuilder().build(collPath, baseIndexPath + "/lucene");
        //afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
              
        //beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        new SerializedRAMIndexBuilder().build(collPath, baseIndexPath + "/ram");
        //afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
       // out.println("SerializedRAMIndexBuilder" + afterUsedMem);
        // out.println("SerializedRAMIndexBuilder" + (afterUsedMem-beforeUsedMem));

        //RAM used in the building process
        long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
         new DiskIndexBuilder().build(collPath, baseIndexPath + "/disk");
        long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        //out.println("Disk" + afterUsedMem);
         //out.println("Disk" + (afterUsedMem-beforeUsedMem));

        // Inspección
        out.println("-----------------------");
        out.println("Checking index correction on URL collection");
        testIndex(new LuceneForwardIndex(baseIndexPath + "/lucene/forward"), "information");
        testIndex(new LuceneIndex(baseIndexPath + "/lucene"), "information");
        testIndex(new SerializedRAMIndex(baseIndexPath + "/ram"), "information");
        testIndex(new DiskIndex(baseIndexPath + "/disk"), "information");

        /////////////////////////////////////
        // Índices: pruebas de rendimiento //
        /////////////////////////////////////
        testIndexPerformance("1k", "collections/docs1k.zip", "index/1k");
        //testIndexPerformance("10k", "collections/docs10k.zip", "index/10k");
        //testIndexPerformance("100k", "collections/docs100k.zip", "index/100k");

        /////////////////////////////////////
        // Búsqueda: pruebas de corrección //
        /////////////////////////////////////

        out.println("-----------------------");
        out.println("Checking engine results on URL collection");
        String query = "information probability";
        Index luceneFwdIndex = new LuceneForwardIndex(baseIndexPath + "/lucene/forward");
        Index luceneIndex = new LuceneIndex(baseIndexPath + "/lucene");
        Index ramIndex = new SerializedRAMIndex(baseIndexPath + "/ram");
        Index diskIndex = new DiskIndex(baseIndexPath + "/disk");
        
       testSearch(new LuceneEngine(baseIndexPath + "/lucene"), query, 5);
        testSearch(new SlowVSMEngine(luceneFwdIndex), query, 5);
        
        testSearch(new TermBasedVSMEngine(luceneIndex), query, 5);
        testSearch(new TermBasedVSMEngine(ramIndex), query, 5);
        testSearch(new TermBasedVSMEngine(diskIndex), query, 5);

        testSearch(new DocBasedVSMEngine(luceneIndex), query, 5);
        testSearch(new DocBasedVSMEngine(ramIndex), query, 5);
        testSearch(new DocBasedVSMEngine(diskIndex), query, 5);
        
        //////////////////////////////////////
        // Búsqueda: pruebas de rendimiento //
        //////////////////////////////////////*/

        testSearchPerformance("1k", "index/1k", "obama family tree", 6);
        //testSearchPerformance("10k", "index/10k", "air tavel information", 5);
        //testSearchPerformance("100k", "index/100k", "living in india", 5);
        
        out.close();
    }
    
    static void testIndex(Index index, String word) throws IOException {
        out.println("  " + index.getClass().getSimpleName());
        out.print("\tWord \"" + word + "\" occurs in " +index.getDocFreq(word) + " documents: ");
        for (Posting posting : index.getPostings(word))
            out.print(posting.getDocID() + " (" + posting.getFreq() + ") ");
        out.println();
    }
    
    static void testIndexPerformance(String collName, String collPath, String baseIndexPath) throws IOException {
        out.println("-----------------------");
        out.println("Testing index performance on " + collName + " document collection");

        Timer.reset("  Build time...");
        new LuceneForwardIndexBuilder().build(collPath, baseIndexPath + "/lucene/forward");
        Timer.time("\tLuceneForwardIndex:\t");
        new LuceneIndexBuilder().build(collPath, baseIndexPath + "/lucene");
        Timer.time("\tLuceneIndex:\t");
        new SerializedRAMIndexBuilder().build(collPath, baseIndexPath + "/ram");
        Timer.time("\tRAMIndex:\t");
        new DiskIndexBuilder().build(collPath, baseIndexPath + "/disk");
        Timer.time("\tDiskIndex:\t");        
        Timer.reset("  Load time...");
        new LuceneForwardIndex(baseIndexPath + "/lucene/forward");
        Timer.time("\tLuceneForwardIndex:\t");
        new LuceneIndex(baseIndexPath + "/lucene");
        Timer.time("\tLuceneIndex:\t");
        

        new SerializedRAMIndex(baseIndexPath + "/ram");
        Timer.time("\tRAMIndex:\t");

        new DiskIndex(baseIndexPath + "/disk");
        Timer.time("\tDiskIndex:\t"); 


        out.println("  Disk space...");
        out.println("\tLuceneForwardIndex:\t" + diskSpace(baseIndexPath + "/lucene/forward") + "K");
        out.println("\tLuceneIndex:\t" + diskSpace(baseIndexPath + "/lucene") + "K");
        out.println("\tRAMIndex:\t" + diskSpace(baseIndexPath + "/ram") + "K");
        out.println("\tDiskIndex:\t" + diskSpace(baseIndexPath + "/disk") + "K");
    }
    
    static void testSearchPerformance(String collName, String baseIndexPath, String query, int cutoff) throws IOException {
        out.println("-----------------------");
        out.println("Testing engine performance on " + collName + " document collection");
        Index luceneFwdIndex = new LuceneForwardIndex(baseIndexPath + "/lucene/forward");
        Index luceneIndex = new LuceneIndex(baseIndexPath + "/lucene");
        Index ramIndex = new SerializedRAMIndex(baseIndexPath + "/ram");
        Index diskIndex = new DiskIndex(baseIndexPath + "/disk");
        
        Timer.reset();
       testSearch(new LuceneEngine(baseIndexPath + "/lucene"), query, cutoff);
        Timer.time("  --> ");
        testSearch(new SlowVSMEngine(luceneFwdIndex), query, cutoff);

        Timer.time("  --> ");
        
        testSearch(new TermBasedVSMEngine(luceneIndex), query, cutoff);

        Timer.time("  --> ");
        testSearch(new TermBasedVSMEngine(ramIndex), query, cutoff);

                 
        Timer.time("  --> ");
        testSearch(new TermBasedVSMEngine(diskIndex), query, cutoff);
        Timer.time("  --> ");
        
        testSearch(new DocBasedVSMEngine(diskIndex), query, cutoff);

        Timer.time("  --> ");
    }
    
    static void testSearch (SearchEngine engine, String query, int cutoff) throws IOException {
        SearchRanking ranking = engine.search(query, cutoff);
        out.println("  " + engine.getClass().getSimpleName() 
                + " + " + engine.getIndex().getClass().getSimpleName()
                + ": top " + cutoff + " for query \"" + query + "\"");
        for (SearchRankingDoc result : ranking)
             out.println("\t" + new TextResultDocRenderer(result));
    }

    static long diskSpace(String dir) {
        long space = 0;
        for (File file : new File(dir).listFiles())
            if (file.isFile()) space += file.length();
        return space / 1000;
    }
}

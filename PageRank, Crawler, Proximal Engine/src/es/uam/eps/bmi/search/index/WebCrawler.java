/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.index;


import es.uam.eps.bmi.search.graph.PageRank;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author root
 */
public class WebCrawler {
    
    private IndexBuilder indexBuilder;
    private static int maxDocs;
    protected static volatile LinkedList<String> queueToVisit;
    private static final Object lockList = new Object();
    private static final Object linkLock = new Object();

    
    protected static volatile Set<String> pagesToIndex;
    private static final Object lockSet = new Object();
   
    protected static final String urlsPath = "collections/crawler/";
    protected static final String baseIndexPath = "index/urls/crawler/";
    private final String[] seeds = {"https://jsoup.org/",
                                    "http://stackoverflow.com/", 
                                    "https://www.uam.es/UAM/Home.htm", 
                                    "https://www.edx.org/",
                                    "https://docs.oracle.com/en/"};
    
    private List<Thread> threads;
    public static  int addPagesToIndex(String url, int number){
         synchronized (lockSet) {
            pagesToIndex.add(url);
            number++;
            return number;
        }
        
    }
    
    public static void listAddAll(Collection<? extends String> list){
        synchronized(lockList){
            queueToVisit.addAll(list);
        }
        
    }
    
    public static void listAdd(String url){
        synchronized(lockList){
            queueToVisit.add(url);
        }
        
    }
   
    public WebCrawler(IndexBuilder indexBuilder, int maxNumDoc) throws IOException{
        this.indexBuilder = indexBuilder;
        maxDocs = maxNumDoc;
        queueToVisit = new LinkedList<>();
        pagesToIndex = new HashSet<>();
        threads = new ArrayList<>();
        File f = new File(urlsPath);
        f.mkdirs();
        
        for(File file: f.listFiles()){
            if (!file.isDirectory()) 
                file.delete();
        }
        f = new File(baseIndexPath + "webgraph");

        f.mkdirs();
        
        for(File file: f.listFiles()){
            if (!file.isDirectory()) 
                file.delete();
        }
        
        
    }
    
    public static void saveLinks(Set<String> links, String url) throws FileNotFoundException{
       synchronized(linkLock){  
        try (PrintStream out = new PrintStream( new FileOutputStream(baseIndexPath +"webgraph"+ Config.graphFileName, true))) {
                links.forEach((link) -> {
                 
                        out.println(url+"\t"+link);

                    
                });
            }
        }
    }
    public void startCrawler() throws IOException{

        int docsPerThread = (maxDocs/seeds.length);
        
        for (String seed : seeds) {
            WebCrawlerThread thread = new WebCrawlerThread(seed, docsPerThread);
            threads.add(thread);
            thread.start();
        }
        
        
        threads.forEach((thread) -> {
            try {
                thread.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        try ( 
            
            PrintStream outWebs = new PrintStream( new FileOutputStream(urlsPath+"urls.txt", true))) {
            pagesToIndex.forEach((url) -> {
                outWebs.println(url);
            });

        }

        this.indexBuilder.build(urlsPath, baseIndexPath);

       
        
    }
    
   
    
    
    
   
    
}

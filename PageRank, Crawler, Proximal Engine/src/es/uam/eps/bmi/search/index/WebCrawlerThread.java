/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.index;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Javi
 */
public class WebCrawlerThread extends Thread {
     private static int maxDocs;
     private String seed;
     private Integer number;
     
     
    public WebCrawlerThread(String url, int max){
        maxDocs = max;
        seed = url;
        number =0;
        
    }
     @Override
     public void run () {
	//For the first seed
       WebCrawler.listAdd(seed);

        while (number < maxDocs){
            try {
                String nextPage= WebCrawler.queueToVisit.poll();            
                storeLinksUrls(nextPage);
            } catch (IOException ex) {
                Logger.getLogger(WebCrawlerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
     
      private void storeLinksUrls(String url) throws IOException{
        //To eliminate duplicates in the links we use a set
        Set<String> links = new HashSet<>(); 
        try{
            
            Document doc  = Jsoup.parse(new URL(url), 10000);
                // this is your original HTML content 
                doc.select("a[href]").forEach((link) -> {
                String urlString = link.absUrl("href");
                urlString = urlString.split("#", 2)[0];
                if(!urlString.isEmpty())
                    links.add(urlString);       
        }); 
         
        //Solo añadimos a la lista de paginas a visitar las que no hayamos indexado y no se encuentren ya en la lista ordenada
        WebCrawler.listAddAll(links.stream().filter((x)-> !WebCrawler.queueToVisit.contains(x) && !WebCrawler.pagesToIndex.contains(x)).collect(Collectors.toSet()));
        WebCrawler.saveLinks(links, url);
        
        
            
        number = WebCrawler.addPagesToIndex(url, number);
        

        }catch(IOException e){
                //e.printStackTrace();
                //System.out.println("Error retrieving the links for the url:"+ url+". "+e.getMessage());
        }    
       
    }
    
}

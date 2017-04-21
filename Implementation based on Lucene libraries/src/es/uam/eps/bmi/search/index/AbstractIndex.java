package es.uam.eps.bmi.search.index;

import java.io.IOException;

/**
 *
 * @author pablo
 */
public abstract class AbstractIndex implements Index {
    protected String indexFolder;
    
    public AbstractIndex(String path) throws IOException {
        this.indexFolder = path;
        try { load(); }
        catch (NoIndexException ex) {
            System.out.println("Warning: created index pointing to " + this.indexFolder + ", no index files there."); 
            for (StackTraceElement trace : ex.getStackTrace()) System.out.println("\tat: " + trace);
            System.out.println("This is just an informative warning and requires no immediate action ;-)"); 
        }
    }
    
    public void load() throws IOException {
        load(indexFolder);
    }
    
    public String getFolder() {
        return indexFolder;
    }
}

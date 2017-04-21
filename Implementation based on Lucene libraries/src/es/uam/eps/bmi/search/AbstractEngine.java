package es.uam.eps.bmi.search;

import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.index.NoIndexException;
import java.io.IOException;

/**
 *
 * @author pablo
 */
public abstract class AbstractEngine implements SearchEngine {
    protected String indexFolder;
    public Index index;

    public AbstractEngine(String path) throws IOException {
        indexFolder = path;
        try { loadIndex(); }
        catch (NoIndexException ex) {
            System.out.println("Warning: created index pointing to " + indexFolder + ", no index files there."
                    + "This is just an informative warning and requires no immediate action, but make sure to load an index sometime... ;-)"); 
        }
    }
    
    public void loadIndex() throws IOException {
        loadIndex(indexFolder);
    }
    
    public String getIndexFolder() {
        return indexFolder;
    }
}

package es.uam.eps.bmi.search;

import es.uam.eps.bmi.search.ranking.SearchRanking;
import java.io.IOException;

/**
 *
 * @author pablo
 */
public interface SearchEngine {
    public SearchRanking search(String query, int cutoff) throws IOException;
    public void loadIndex(String path) throws IOException;
    public void loadIndex() throws IOException;
    public String getIndexFolder() throws IOException;
}

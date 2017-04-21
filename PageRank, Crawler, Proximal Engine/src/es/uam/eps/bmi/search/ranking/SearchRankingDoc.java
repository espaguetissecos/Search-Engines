package es.uam.eps.bmi.search.ranking;

import java.io.IOException;

/**
 *
 * @author pablo
 */
public abstract class SearchRankingDoc implements Comparable<SearchRankingDoc> {
    public abstract double getScore();
    public abstract int getDocID();
    public abstract String getPath() throws IOException;
    
    public int compareTo(SearchRankingDoc d) {
       return (int) Math.signum(d.getScore() - getScore());
    }
}

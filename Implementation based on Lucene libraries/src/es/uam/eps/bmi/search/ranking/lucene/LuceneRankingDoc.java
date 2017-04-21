package es.uam.eps.bmi.search.ranking.lucene;

import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.ranking.SearchRankingDoc;
import java.io.IOException;
import org.apache.lucene.search.ScoreDoc;

/**
 *
 * @author pablo
 */
public class LuceneRankingDoc extends SearchRankingDoc {

    LuceneRankingDoc (Index idx, ScoreDoc r) {
        index = idx;
        rankedDoc = r;
    }
    public double getScore() throws IOException {
        return  super.getScore();
    }

    public String getPath() throws IOException {
        return super.getPath();
    }
}

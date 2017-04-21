package es.uam.eps.bmi.search.ranking.impl;

import es.uam.eps.bmi.search.index.DocumentMap;
import es.uam.eps.bmi.search.ranking.SearchRankingDoc;
import java.io.IOException;

/**
 *
 * @author pablo
 */
public class RankingDocImpl extends SearchRankingDoc {
    DocumentMap docMap;
    int docID;
    double score;
    
    RankingDocImpl (DocumentMap m, int id, double s) {
        docMap = m;
        docID = id;
        score = s;
    }
    public double getScore() {
        return score;
    }

    public String getPath() throws IOException {
        return docMap.getDocPath(docID);
    }

    public int getDocID() {
        return docID;
    }
}

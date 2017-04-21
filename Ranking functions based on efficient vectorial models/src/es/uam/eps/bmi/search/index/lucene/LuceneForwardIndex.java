package es.uam.eps.bmi.search.index.lucene;

import es.uam.eps.bmi.search.index.ForwardIndex;
import es.uam.eps.bmi.search.index.freq.FreqVector;
import es.uam.eps.bmi.search.index.freq.lucene.LuceneFreqVector;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.index.Term;

/**
 *
 * @author pablo
 */
public class LuceneForwardIndex extends LuceneIndex implements ForwardIndex {

    public LuceneForwardIndex(String path) throws IOException {
        super(path);
    }

    public FreqVector getDocVector(int docID) throws IOException {
        return new LuceneFreqVector(index.getTermVector(docID, "content"));
    }

    @Override
    public long getTermFreq(String term, int docID) throws IOException {
        return getDocVector(docID).getFreq(term);
    }

    @Override
    public long getDocFreq(String word) {

        try {
            return this.index.docFreq(new Term(LuceneIndexBuilder.getCONTENTS(), word));
        } catch (IOException ex) {
            Logger.getLogger(LuceneIndex.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }
}

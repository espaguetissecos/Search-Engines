package es.uam.eps.bmi.search.index.lucene;

/**
 *
 * @author pablo
 */
public class LuceneForwardIndexBuilder extends LuceneIndexBuilder {
    public LuceneForwardIndexBuilder() {
        type.setStoreTermVectors (true);
    }
}

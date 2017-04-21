package es.uam.eps.bmi.search.index.lucene;

import org.apache.lucene.index.IndexOptions;

/**
 *
 * @author pablo
 */
public class LucenePositionalIndexBuilder extends LuceneIndexBuilder {
    public LucenePositionalIndexBuilder() {
        type.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
    }
}

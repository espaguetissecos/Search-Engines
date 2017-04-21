package es.uam.eps.bmi.search.test;
import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.index.IndexBuilder;
import es.uam.eps.bmi.search.index.lucene.LuceneIndex;
import es.uam.eps.bmi.search.index.lucene.LuceneIndexBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author pablo
 */
public class TermDocs {

    public static void main(String a[]) throws IOException {

        String indexPath = "index";
        String collectionPath = "collections/docs.zip";
        final FileWriter fichero1 = new FileWriter(indexPath + "/termdoc.txt");
        final PrintWriter pw1 = new PrintWriter(fichero1);
        int i=0;
        
        final FileWriter fichero2 = new FileWriter(indexPath + "/termdocfreq.txt");
        final PrintWriter pw2 = new PrintWriter(fichero2);

        
        IndexBuilder builder = new LuceneIndexBuilder();
        builder.build(collectionPath, indexPath);

        Index index = new LuceneIndex(indexPath);
        List<String> terms = index.getAllTerms();

        Collections.sort(terms, (String t1, String t2) -> (int) Math.signum(index.getTermTotalFreq(t2) - index.getTermTotalFreq(t1)));
        terms.forEach((term) -> {
            pw1.println(terms.indexOf(term) + "\t" + index.getTermTotalFreq(term));
        });

        Collections.sort(terms, (String t1, String t2) -> (int) Math.signum(index.getTermDocFreq(t2) - index.getTermDocFreq(t1)));
        terms.forEach((term) -> {        
                    pw2.println(terms.indexOf(term) + "\t" + index.getTermDocFreq(term));
        });
        pw1.close();
        pw2.close();

    }

}

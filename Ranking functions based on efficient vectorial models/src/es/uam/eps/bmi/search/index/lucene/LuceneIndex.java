package es.uam.eps.bmi.search.index.lucene;

import es.uam.eps.bmi.search.index.AbstractIndex;
import es.uam.eps.bmi.search.index.NoIndexException;
import es.uam.eps.bmi.search.index.structure.PostingsList;
import es.uam.eps.bmi.search.index.structure.lucene.LucenePostingsList;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexNotFoundException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

/**
 *
 * @author pablo
 */
public class LuceneIndex extends AbstractIndex {
    IndexReader index;
    
    public LuceneIndex(String path) throws IOException {
        super(path);
    }

   

    @Override
    public String getDocPath(int docID) {
        try {
            return index.document(docID).get("path");
        } catch (IOException ex) {
            Logger.getLogger(LuceneIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @Override
    public List<String> getAllTerms() {
        try {
            List<String> termList = new ArrayList<>();
            TermsEnum terms = MultiFields.getFields(index).terms("content").iterator();
            while (terms.next() != null)
                termList.add(terms.term().utf8ToString());
            return termList;
        } catch (IOException ex) {
            Logger.getLogger(LuceneIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public int numDocs() {
        return index.numDocs();
    }

    
    
    public void load(String path) throws IOException {
        try {
            index = DirectoryReader.open(FSDirectory.open(Paths.get(path)));
            loadNorms(path);
        } catch (IndexNotFoundException ex) {
            throw new NoIndexException(path);
        }
    }
    
    public double getDocNorm(int docID) throws IOException {
        return docNorms[docID];
    }

    public PostingsList getPostings(String term) {
        
        try{
             TermsEnum terms = MultiFields.getFields(index).terms("content").iterator();
        terms.seekExact(new BytesRef(term));
        return new LucenePostingsList(terms.postings(null), terms.docFreq());
        }catch(IOException we){
            
        }
        return null;
       
    }
   
    public List<String> getIDs() {
        return null;

    }

 
    public String getFolder() {
        return this.indexFolder;
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

    @Override
    public long getTotalFreq(String term) throws IOException {
        try {       
            return this.index.totalTermFreq(new Term(LuceneIndexBuilder.getCONTENTS(),term));
        } catch (IOException ex) {
            return -1;
        }
        
    }

   
    
}

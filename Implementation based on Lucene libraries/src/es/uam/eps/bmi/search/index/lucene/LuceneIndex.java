/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.index.lucene;

import es.uam.eps.bmi.search.index.AbstractIndex;
import es.uam.eps.bmi.search.index.NoIndexException;
import es.uam.eps.bmi.search.index.freq.FreqVector;
import es.uam.eps.bmi.search.index.freq.lucene.LuceneFreqVector;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexNotFoundException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import static org.apache.lucene.search.DocIdSetIterator.NO_MORE_DOCS;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

/**
*
* @author  Francisco Andreu y Javier Hernandez
* @version 1.0
* @since   2017-02-10
*/
public class LuceneIndex extends AbstractIndex {

    public IndexReader indexReader;

    public LuceneIndex(String path) throws IOException {
        super(path);
    }

    @Override
    public void load(String ruta) throws IOException  {
       
             this.indexFolder = ruta;
        Directory directory = FSDirectory.open(Paths.get(this.indexFolder));
        indexReader = DirectoryReader.open(directory);
        
       
        
    }

    @Override
    public List<String> getIDs() {
        return null;

    }

    @Override
    public List<String> getAllTerms() {
        BytesRef text;
        long freq;
        List<String> terms = new ArrayList<>();
        // Eliminamos duplicados utilizando un set.
        try {
            Fields fields = MultiFields.getFields(this.indexReader);
            TermsEnum terms_ = fields.terms(LuceneIndexBuilder.getCONTENTS()).iterator();
            while ((text = terms_.next()) != null){
                    terms.add(text.utf8ToString());
            }
            
        } catch (IOException ex) {
            Logger.getLogger(LuceneIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
        return terms;
    }

    public String getFolder() {
        return this.indexFolder;
    }

    @Override
    public String getDocPath(int doc) {
        try {
            return indexReader.document(doc).getField(LuceneIndexBuilder.getPATH()).stringValue();

        } catch (IOException ex) {
            Logger.getLogger(LuceneIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    @Override
    public FreqVector getDocVector(int docID) {
        Terms terms = null;
        try {
            terms = this.indexReader.getTermVector(docID, LuceneIndexBuilder.getCONTENTS());
            
            LuceneFreqVector vector = new LuceneFreqVector(terms);
            return vector;
        } catch (IOException ex) {
            Logger.getLogger(LuceneIndex.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public long getTermDocFreq(String t2) {
        
        try {
            return this.indexReader.docFreq(new Term(LuceneIndexBuilder.getCONTENTS(), t2));
        } catch (IOException ex) {
            Logger.getLogger(LuceneIndex.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        
       
    }

    @Override
    public long getTermFreq(String word, int docID) {
        try {
            return this.getDocVector(docID).getFreq(word);
        } catch (IOException ex) {
            Logger.getLogger(LuceneIndex.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }

    }

    @Override
    public long getTermTotalFreq(String word) {
        try {       
            return this.indexReader.totalTermFreq(new Term(LuceneIndexBuilder.getCONTENTS(),word));
        } catch (IOException ex) {
            Logger.getLogger(LuceneIndex.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        
    }

}

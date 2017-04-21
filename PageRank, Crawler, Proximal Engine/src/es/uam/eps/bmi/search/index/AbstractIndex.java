package es.uam.eps.bmi.search.index;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author pablo
 */
public abstract class AbstractIndex implements Index {
    protected String indexFolder;
    protected double docNorms[];
    
    public AbstractIndex()  {}
    
    public AbstractIndex(String path) throws IOException {
        indexFolder = path;
    }
    
    public String getFolder() {
        return indexFolder;
    }

    public double getDocNorm(int docID) throws IOException {
        return docNorms[docID];
    }

    public void loadNorms(String path) throws FileNotFoundException {
        File f = new File(path + Config.normsFileName);
        if (!f.exists()) return;
        Scanner scn = new Scanner(f);
        docNorms = new double[numDocs()];
        for (int docID = 0; docID < docNorms.length; docID++)
            docNorms[docID] = new Double(scn.nextLine());
        scn.close();
    }
}

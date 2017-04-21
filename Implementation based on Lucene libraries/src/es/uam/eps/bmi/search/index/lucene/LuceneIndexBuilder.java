/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.index.lucene;

import es.uam.eps.bmi.search.index.IndexBuilder;
import es.uam.eps.bmi.search.index.freq.FreqVector;
import es.uam.eps.bmi.search.index.freq.TermFreq;
import es.uam.eps.bmi.search.parser.Parser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import static org.apache.lucene.analysis.standard.StandardAnalyzer.STOP_WORDS_SET;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.jsoup.Jsoup;

/**
*
* @author  Francisco Andreu y Javier Hernandez
* @version 1.0
* @since   2017-02-10
*/
public class LuceneIndexBuilder implements IndexBuilder {

    private static final String CONTENTS = "contents";

    private static final String PATH = "path";

    public static String getCONTENTS() {
        return CONTENTS;
    }

    static String getPATH() {
        return PATH;
    }
    /**
     *Constructor del index
     * @param collectionPath : la ruta de los ficheros
     * @param indexPath : La ruta del index
     * @throws IOException
     */
    @Override
    public void build(String collectionPath, String indexPath) throws IOException {

        Directory dir = FSDirectory.open(Paths.get(indexPath));

        CharArraySet stopSet = CharArraySet.copy(STOP_WORDS_SET);

        //Anyadir palabras al set de StopWords
        //stopSet.add("information");
        //stopSet.add("probability");
        //stopSet.add("public");
        Analyzer analyzer = new StandardAnalyzer(stopSet);
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        /* NUEVO */
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        config.setCommitOnClose(true);
        //Esto no se si vale para algo, pero lo dejamos: NO CREO QUE VALGA PARA NADA
        //config.setRAMBufferSizeMB(1024.0); 

        try (IndexWriter indexWriter = new IndexWriter(dir, config)) {

            File f = new File(collectionPath);

            if (f.isFile() && collectionPath.endsWith(".zip")) {
                ZipFile docDir = new ZipFile(f);
                try (ZipFile zipFile = docDir) {
                    Enumeration zipEntries = zipFile.entries();
                    while (zipEntries.hasMoreElements()) {
                        ZipEntry entry = ((ZipEntry) zipEntries.nextElement());

                        Document document = new Document();
                        StringField pathField = new StringField(PATH, f.getAbsolutePath() + System.getProperty("file.separator") + entry.getName(), StringField.Store.YES);
                        InputStream stream = zipFile.getInputStream(entry);
                        String text = Parser.parse(entry.getName(), stream);
                        FieldType fieldType = new FieldType();
                        fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
                        fieldType.setStoreTermVectors(true);
                        Field contentField = new Field(CONTENTS, text, fieldType);
                        document.add(pathField);
                        document.add(contentField);
                        indexWriter.addDocument(document);

                    }
                } catch (IOException e) {
                    System.err.println("Error al crear El indexWriter. Revisar indexPath.");
                    System.err.println(e);
                    System.exit(1);
                }
            } else if (f.isFile() && collectionPath.endsWith(".txt")) {
                try (Reader reader = new FileReader(f)) {
                    BufferedReader br = new BufferedReader(reader);
                    String url;
                    while ((url = br.readLine()) != null) {
                        Document document = new Document();
                        StringField pathField = new StringField(PATH, url, StringField.Store.YES);

                        //*TodoVer esto porque tarda mucho*//
                        String text = Parser.parse(url, null);

                        FieldType fieldType = new FieldType();
                        fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
                        fieldType.setStoreTermVectors(true);
                        Field contentField = new Field(CONTENTS, text, fieldType);
                        document.add(pathField);
                        document.add(contentField);
                        indexWriter.addDocument(document);
                    }

                    reader.close();
                } catch (IOException e) {
                    System.err.println("Error al crear indice para un fichero de texto.");
                    System.err.println(e);
                    System.exit(1);
                }

            } else if (f.isDirectory()) {
                File[] files = f.listFiles();
                for (File file : files) {
                    if (file.isDirectory()) {
                        continue;
                    }
                    Document document = new Document();

                    StringField pathField = new StringField(PATH, file.getAbsolutePath(), StringField.Store.YES);

                    String text = Parser.parse(file.getPath(), null);

                    FieldType fieldType = new FieldType();
                    fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
                    fieldType.setStoreTermVectors(true);

                    Field contentField = new Field(CONTENTS, text, fieldType);

                    document.add(pathField);
                    document.add(contentField);
                    indexWriter.addDocument(document);

                }

            }
            indexWriter.close();
            LuceneIndex index = new LuceneIndex(indexPath);
            FileWriter fichero = new FileWriter(indexPath + "/modulos.txt");
            double[] vector;
            int j = 0;
            double modulo = 0;
            FreqVector v;
            try (PrintWriter pw = new PrintWriter(fichero)) {
                for (int i = 0; i < index.indexReader.numDocs(); i++) {
                    v = index.getDocVector(i);
                    vector = new double[(int) v.size()];
                    for (TermFreq t : v) {
                        vector[j] = t.getFreq();
                        modulo += Math.pow(vector[j], 2);
                        j++;
                    }
                    j = 0;
                    modulo = Math.sqrt(modulo);
                    pw.println(modulo);
                    modulo = 0;
                }

                pw.close();
            }

        }
    }
}

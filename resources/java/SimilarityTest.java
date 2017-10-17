package net.sf.jtmt.similarity.matrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.jtmt.indexers.matrix.IdfIndexer;
import net.sf.jtmt.indexers.matrix.LsiIndexer;
import net.sf.jtmt.indexers.matrix.VectorGenerator;
import net.sf.jtmt.similarity.matrix.CosineSimilarity;
import net.sf.jtmt.similarity.matrix.JaccardSimilarity;
import net.sf.jtmt.similarity.matrix.Searcher;
import net.sf.jtmt.similarity.matrix.Searcher.SearchResult;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.math.linear.RealMatrix;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * Unit tests for Similarity implementations.
 * @author Sujit Pal
 * @version $Revision: 55 $
 */
public class SimilarityTest {

  private VectorGenerator vectorGenerator;
  
  @Before
  public void setUp() throws Exception {
    vectorGenerator = new VectorGenerator();
    vectorGenerator.setDataSource(new DriverManagerDataSource(
      "com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/tmdb", "root", "orange"));
    Map<String,Reader> documents = new LinkedHashMap<String,Reader>();
    BufferedReader reader = new BufferedReader(
      new FileReader("src/test/resources/data/indexing_sample_data.txt"));
    String line = null;
    while ((line = reader.readLine()) != null) {
      String[] docTitleParts = StringUtils.split(line, ";");
      documents.put(docTitleParts[0], new StringReader(docTitleParts[1]));
    }
    vectorGenerator.generateVector(documents);
  }

  @Test
  public void testJaccardSimilarityWithTfIdfVector() throws Exception {
    IdfIndexer indexer = new IdfIndexer();
    RealMatrix termDocMatrix = indexer.transform(vectorGenerator.getMatrix());
    JaccardSimilarity jaccardSimilarity = new JaccardSimilarity();
    RealMatrix similarity = jaccardSimilarity.transform(termDocMatrix);
    prettyPrintMatrix("Jaccard Similarity (TF/IDF)", similarity, 
      vectorGenerator.getDocumentNames(), new PrintWriter(System.out, true));
  }
  
  @Test
  public void testJaccardSimilarityWithLsiVector() throws Exception {
    LsiIndexer indexer = new LsiIndexer();
    RealMatrix termDocMatrix = indexer.transform(vectorGenerator.getMatrix());
    JaccardSimilarity jaccardSimilarity = new JaccardSimilarity();
    RealMatrix similarity = jaccardSimilarity.transform(termDocMatrix);
    prettyPrintMatrix("Jaccard Similarity (LSI)", similarity, 
      vectorGenerator.getDocumentNames(), new PrintWriter(System.out, true));
  }

  @Test
  public void testCosineSimilarityWithTfIdfVector() throws Exception {
    IdfIndexer indexer = new IdfIndexer();
    RealMatrix termDocMatrix = indexer.transform(vectorGenerator.getMatrix());
    CosineSimilarity cosineSimilarity = new CosineSimilarity();
    RealMatrix similarity = cosineSimilarity.transform(termDocMatrix);
    prettyPrintMatrix("Cosine Similarity (TF/IDF)", similarity, 
      vectorGenerator.getDocumentNames(), new PrintWriter(System.out, true));
  }
  
  @Test
  public void testCosineSimilarityWithLsiVector() throws Exception {
    LsiIndexer indexer = new LsiIndexer();
    RealMatrix termDocMatrix = indexer.transform(vectorGenerator.getMatrix());
    CosineSimilarity cosineSimilarity = new CosineSimilarity();
    RealMatrix similarity = cosineSimilarity.transform(termDocMatrix);
    prettyPrintMatrix("Cosine Similarity (LSI)", similarity, 
      vectorGenerator.getDocumentNames(), new PrintWriter(System.out, true));
  }
  
  @Test
  public void testSearchWithTfIdfVector() throws Exception {
    // generate the term document matrix via the appropriate indexer
    IdfIndexer indexer = new IdfIndexer();
    RealMatrix termDocMatrix = indexer.transform(vectorGenerator.getMatrix());
    // set up the query
    Searcher searcher = new Searcher();
    searcher.setDocuments(vectorGenerator.getDocumentNames());
    searcher.setTerms(vectorGenerator.getWords());
    searcher.setSimilarity(new CosineSimilarity());
    searcher.setTermDocumentMatrix(termDocMatrix);
    // run the query
    List<SearchResult> results = searcher.search("human computer interface");
    prettyPrintResults("human computer interface", results);
  }

  @Test
  public void testSearchWithLsiVector() throws Exception {
    // generate the term document matrix via the appropriate indexer
    LsiIndexer indexer = new LsiIndexer();
    RealMatrix termDocMatrix = indexer.transform(vectorGenerator.getMatrix());
    // set up the query
    Searcher searcher = new Searcher();
    searcher.setDocuments(vectorGenerator.getDocumentNames());
    searcher.setTerms(vectorGenerator.getWords());
    searcher.setSimilarity(new CosineSimilarity());
    searcher.setTermDocumentMatrix(termDocMatrix);
    // run the query
    List<SearchResult> results = searcher.search("human computer interface");
    prettyPrintResults("human computer interface", results);
  }

  private void prettyPrintMatrix(String legend, RealMatrix matrix, String[] documentNames,  
      PrintWriter writer) {
    writer.printf("=== %s ===%n", legend);
    writer.printf("%6s", " ");
    for (int i = 0; i < documentNames.length; i++) {
      writer.printf("%8s", documentNames[i]);
    }
    writer.println();
    for (int i = 0; i < documentNames.length; i++) {
      writer.printf("%6s", documentNames[i]);
      for (int j = 0; j < documentNames.length; j++) {
        writer.printf("%8.4f", matrix.getEntry(i, j));
      }
      writer.println();
    }
    writer.flush();
  }
  
  private void prettyPrintResults(String query, List<SearchResult> results) {
    System.out.printf("Results for query: [%s]%n", query);
    for (SearchResult result : results) {
      System.out.printf("%s (score = %8.4f)%n", result.title, result.score);
    }
  }
}

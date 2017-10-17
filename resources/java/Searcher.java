package net.sf.jtmt.similarity.matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math.linear.OpenMapRealMatrix;
import org.apache.commons.math.linear.RealMatrix;

/**
 * Models a search query against the generated term document matrix.
 * @author Sujit Pal
 * @version $Revision: 21 $
 */
public class Searcher {

  public class SearchResult {
    public String title;
    public double score;
    public SearchResult(String title, double score) {
      this.title = title;
      this.score = score;
    }
  };
  
  private RealMatrix termDocumentMatrix;
  private List<String> documents;
  private List<String> terms;
  private AbstractSimilarity similarity;
  
  public void setTermDocumentMatrix(RealMatrix termDocumentMatrix) {
    this.termDocumentMatrix = termDocumentMatrix;
  }
  
  public void setDocuments(String[] documents) {
    this.documents = Arrays.asList(documents);
  }
  
  public void setTerms(String[] terms) {
    this.terms = Arrays.asList(terms);
  }
  
  public void setSimilarity(AbstractSimilarity similarity) {
    this.similarity = similarity;
  }
  
  public List<SearchResult> search(String query) {
    // build up query matrix
    RealMatrix queryMatrix = getQueryMatrix(query);
    final Map<String,Double> similarityMap = new HashMap<String,Double>();
    for (int i = 0; i < termDocumentMatrix.getColumnDimension(); i++) {
      double sim = similarity.computeSimilarity(queryMatrix, 
        termDocumentMatrix.getSubMatrix(
          0, termDocumentMatrix.getRowDimension() - 1, i, i));
      if (sim > 0.0D) {
        similarityMap.put(documents.get(i), sim);
      }
    }
    return sortByScore(similarityMap);
  }
  
  private RealMatrix getQueryMatrix(String query) {
    RealMatrix queryMatrix = new OpenMapRealMatrix(terms.size(), 1);
    String[] queryTerms = query.split("\\s+");
    for (String queryTerm : queryTerms) {
      int termIdx = 0;
      for (String term : terms) {
        if (queryTerm.equalsIgnoreCase(term)) {
          queryMatrix.setEntry(termIdx, 0, 1.0D);
        }
        termIdx++;
      }
    }
    queryMatrix = queryMatrix.scalarMultiply(1 / queryMatrix.getNorm());
    return queryMatrix;
  }
  
  private List<SearchResult> sortByScore(final Map<String,Double> similarityMap) {
    List<SearchResult> results = new ArrayList<SearchResult>();
    List<String> docNames = new ArrayList<String>();
    docNames.addAll(similarityMap.keySet());
    Collections.sort(docNames, new Comparator<String>() {
      public int compare(String s1, String s2) {
        return similarityMap.get(s2).compareTo(similarityMap.get(s1));
      }
    });
    for (String docName : docNames) {
      double score = similarityMap.get(docName);
      if (score < 0.00001D) {
        continue;
      }
      results.add(new SearchResult(docName, score));
    }
    return results;
  }
}

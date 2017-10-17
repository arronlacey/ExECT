package net.sf.jtmt.similarity.matrix;

import org.apache.commons.math3.*;

/**
 * Implements Cosine Similarity for a term document matrix.
 * A o B = x1*x2 + y1*y2
 * dist(A,0) = sqrt((xa-x0)^2 + (ya-y0)^2) == |A|
 * Therefore:
 * sim(A,B) = cos t = A o B/|A|x|B|  
 * 
 * @author Sujit Pal
 * @version $Revision: 21 $
 */
public class CosineSimilarity extends AbstractSimilarity {

  @Override
  public double computeSimilarity(RealMatrix sourceDoc, RealMatrix targetDoc) {
    if (sourceDoc.getRowDimension() != targetDoc.getRowDimension() ||
        sourceDoc.getColumnDimension() != targetDoc.getColumnDimension() ||
        sourceDoc.getColumnDimension() != 1) {
      throw new IllegalArgumentException(
        "Matrices are not column matrices or not of the same size");
    }
    // max col sum, only 1 col, so...
    double dotProduct = dot(sourceDoc, targetDoc);
    // sqrt of sum of squares of all elements, only one col, so...
    double eucledianDist = sourceDoc.getFrobeniusNorm() * targetDoc.getFrobeniusNorm();
    return dotProduct / eucledianDist;
  }
  
  private double dot(RealMatrix source, RealMatrix target) {
    int maxRows = source.getRowDimension();
    int maxCols = source.getColumnDimension();
    RealMatrix dotProduct = new OpenMapRealMatrix(maxRows, maxCols);
    for (int row = 0; row < maxRows; row++) {
      for (int col = 0; col < maxCols; col++) {
        dotProduct.setEntry(row, col, 
          source.getEntry(row, col) * target.getEntry(row, col));
      }
    }
    return dotProduct.getNorm();
  }
}

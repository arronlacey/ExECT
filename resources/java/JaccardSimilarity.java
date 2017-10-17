package net.sf.jtmt.similarity.matrix;

import org.apache.commons.math.linear.RealMatrix;

/**
 * Implements Jaccard Similarity for a term-document matrix.
 *   sim(A,B) = (D(A) ^ D(B)) / (D(A) v D(B))
 *            = (a(x)b(x) + a(y)b(y)) / 
 *              (a(x) + b(x) + a(y) + b(y) - a(x)b(x) - a(y)b(y)
 * @author Sujit Pal
 * @version $Revision: 2 $
 */
public class JaccardSimilarity extends AbstractSimilarity {

  @Override
  public double computeSimilarity(RealMatrix source, RealMatrix target) {
    double intersection = 0.0D;
    for (int i = 0; i < source.getRowDimension();i++) {
      intersection += Math.min(source.getEntry(i, 0), target.getEntry(i, 0));
    }
    if (intersection > 0.0D) {
      double union = source.getNorm() + target.getNorm() - intersection;
      return intersection / union;
    } else {
      return 0.0D;
    }
  }
}

package fusion.oapt.algorithm.merger.matchingMerge;

import net.didion.jwnl.JWNLException;


public interface SimilarityMeasure {

    public double getSimilarity(String string1, String string2) throws JWNLException;

}


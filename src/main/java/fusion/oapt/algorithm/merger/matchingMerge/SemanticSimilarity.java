package fusion.oapt.algorithm.merger.matchingMerge;

import net.didion.jwnl.JWNLException;

public class SemanticSimilarity implements SimilarityMeasure {

    SimilarityFinder sf;

    public SemanticSimilarity() throws Exception {
        sf = new SimilarityFinder("shef.nlp.wordnet.similarity.Lin");
    }

    public double getSimilarity(String string1, String string2) throws JWNLException {
        double bestSim = sf.computeBestSimilarity(string1, string2, null).getBestSim();
        return bestSim;
    }
}

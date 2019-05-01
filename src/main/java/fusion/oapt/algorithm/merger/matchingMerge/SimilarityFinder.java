package fusion.oapt.algorithm.merger.matchingMerge;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import fusion.oapt.algorithm.matcher.string.WordNetMatcher;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.dictionary.Dictionary;

public class SimilarityFinder {

    SimilarityMeasure sim;
    Dictionary dict;

    /**
     * Constructor of this class
     * @param similarityType String representing on of the implemented 
     * similarity measures. Currently shef.nlp.wordnet.similarity.Lin and
     * shef.nlp.wordnet.similarity.JCn are implemented
     * @throws java.lang.Exception
     */
    public SimilarityFinder(String similarityType) throws Exception {
        //Create a map to hold the similarity config params
        Map<String, String> params = new HashMap<String, String>();

        //the simType parameter is the class name of the measure to use
        params.put("simType", similarityType);

        URL urlInfocontent = getClass().getClassLoader().getResource("resources/ic-bnc-resnik-add1.dat"); 
        URL urlMapping = getClass().getClassLoader().getResource("resources/domain_independent.txt");

        //this param should be the URL to an infocontent file (if required by the similarity measure being loaded)
        params.put("InfoContent", urlInfocontent.toString());

        //this param should be the URL to a mapping file if the user needs to make synset mappings
        params.put("mapping", urlMapping.toString());

        //create the similarity measure
        //sim = SimilarityMeasure.newInstance(params);
       
        dict = Dictionary.getInstance();
    }

    /**
     * Gets similarity between the two synsets
     * @param first First of the compared sysnsets
     * @param second Second of the compared synsets
     * @return returns value from 0 to 1 representing level of similarity.
     * 0 - no similarity. 1 - synsets are equal
     * @throws net.didion.jwnl.JWNLException
     */
    public double computeSimilarity(Synset first, Synset second) throws JWNLException 
    {
       WordNetMatcher wnM=new WordNetMatcher(); 
       return wnM.compute(first.toString(), second.toString());
    	//return sim.getSimilarity(first, second);
    }

    /**
     * Finds the most similar meanings of given words.
     * @param first Lemma of first of the two compared words
     * @param second Lemma of second of the two compared words
     * @param pos Comparison can be limited to a given Part of Speach. All POS are
     * compared when pos equals null
     * @return Returns an object of type BestSimValueContainer containg found similarity,
     * best meanings of each words and their POS
     * @throws net.didion.jwnl.JWNLException
     */
    public BestSimValueContainer computeBestSimilarity(String first, String second, POS pos) throws JWNLException {
        POS bestPOS = null;
        int bestSenseOfFirst = -1;
        int bestSenseOfSecond = -1;
        double bestSim = 0;

        if (first.matches(".*([a-zA-Z])+([0-9])+.*") ||
                first.matches(".*([0-9])+([a-zA-Z])+.*") ||
                second.matches(".*([a-zA-Z])+([0-9])+.*") ||
                second.matches(".*([0-9])+([a-zA-Z])+.*")) {
            return new BestSimValueContainer(first, second, bestPOS, bestSenseOfFirst, bestSenseOfSecond, bestSim);
        }

         IndexWord[] iwordsFirst = null;

        if (pos == null) {
            iwordsFirst = dict.lookupAllIndexWords(first).getIndexWordArray();
        } else {
            iwordsFirst = new IndexWord[1];
            iwordsFirst[0] = dict.lookupIndexWord(pos, first);
        }
        //for each part of speach available for first word
        for (int i = 0; i < iwordsFirst.length; i++) {
            IndexWord iwordFirst = iwordsFirst[i];

            if (iwordFirst != null) {

                //for each meaning of first word (synset)
                for (int j = 1; j <= iwordFirst.getSenses().length; j++) {

                    //lookup all meanings of second word for given POS. When POS are different similarity alwas equals 0
                    IndexWord iwordSecond = dict.lookupIndexWord(iwordFirst.getPOS(), second);

                    if (iwordSecond != null) {
                        //for each meaning of second word (synset)
                        for (int k = 1; k <= iwordSecond.getSenses().length; k++) {
                            double currentSim = computeSimilarity(iwordFirst.getSense(j), iwordSecond.getSense(k));
                            if (bestSim < currentSim) {
                                bestSim = currentSim;
                                bestSenseOfFirst = j;
                                bestSenseOfSecond = k;
                                bestPOS = iwordFirst.getPOS();
                            }
                        }
                    }
                }
            }
        }

        return new BestSimValueContainer(first, second, bestPOS, bestSenseOfFirst, bestSenseOfSecond, bestSim);
    }
}


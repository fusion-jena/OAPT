
package fusion.oapt.model.ext.sentence.filter;

import java.util.ArrayList;
import java.util.Iterator;

import fusion.oapt.model.ext.sentence.RDFSentence;
import fusion.oapt.model.Constant;

public class NegativeSentenceFilter implements RDFSentenceFilter
{
    public ArrayList<RDFSentence> filter(ArrayList<RDFSentence> sentences)
    {
        ArrayList<RDFSentence> result = new ArrayList<RDFSentence>(sentences.size());
        nextSentence:
        for (Iterator<RDFSentence> i = sentences.iterator(); i.hasNext();) {
            RDFSentence sentence = i.next();
            if (sentence.getPredicateVocabularyURIs().contains(
            		Constant.OWL_NS + "disjointWith") || 
                    sentence.getPredicateVocabularyURIs().contains(
                    Constant.OWL_NS + "complementOf") || 
                    sentence.getPredicateVocabularyURIs().contains(
                    Constant.OWL_NS + "differentFrom")) {
                continue nextSentence;
            }
            result.add(sentence);
        }
        return result;
    }
}

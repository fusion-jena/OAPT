
package fusion.oapt.model.ext.sentence.filter;

import java.util.ArrayList;

import fusion.oapt.model.ext.sentence.RDFSentence;

public interface RDFSentenceFilter
{
    public ArrayList<RDFSentence> filter(ArrayList<RDFSentence> sentences);
}

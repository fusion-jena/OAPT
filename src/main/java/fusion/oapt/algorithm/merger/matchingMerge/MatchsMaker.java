package fusion.oapt.algorithm.merger.matchingMerge;

import java.math.BigDecimal;

import fusion.oapt.algorithm.matcher.string.ISub;
import fusion.oapt.algorithm.matcher.string.WordNetMatcher;
import net.didion.jwnl.JWNLException;


public class MatchsMaker {

    private String _lString, _rString;
    private String[] _leftTokens, _rightTokens;
    private int leftLen, rightLen;
    private BigDecimal[][] cost;
//    private boolean _accentInsensitive;
//    Similarity getSimilarity;

    public MatchsMaker(String[] leftTokens, String[] rightTokens) throws JWNLException, Exception {
//        System.out.println("LL: " + leftTokens.length + " RL: " + rightTokens.length);
        if (leftTokens.length > rightTokens.length) {
            _leftTokens = rightTokens;
            _rightTokens = leftTokens;
        } else {
            _leftTokens = leftTokens;
            _rightTokens = rightTokens;
        }

        initialize();

    }

    public MatchsMaker(String left, String right) throws Exception {
//        _accentInsensitive = accentInsensitive;

        _lString = left;
        _rString = right;

//        if (_accentInsensitive) {
//            _lString = StripAccents(_lString);
//            _rString = StripAccents(_rString);
//        }

        _leftTokens = (String[]) JWNLUtils.splitStringBySpaces(JWNLUtils.replaceConncetors(JWNLUtils.normalizeCasing(_lString)));
        _rightTokens = (String[]) JWNLUtils.splitStringBySpaces(JWNLUtils.replaceConncetors(JWNLUtils.normalizeCasing(_rString)));

        if (_leftTokens.length > _rightTokens.length) {
            String[] tmp = _leftTokens;
            _leftTokens = _rightTokens;
            _rightTokens = tmp;
            String s = _lString;
            _lString = _rString;
            _rString = s;
        }

        initialize();
    }

//    private void MyInit() throws Exception {
//        //ISimilarity lexical=new LexicalSimilarity() ;
//        //getSimilarity=new Similarity(lexical.GetSimilarity) ;
//
////        System.out.println("lstring: " + _lString);
////        System.out.println("rstring: " + _rString);
//
//
//        Initialize();
//
//    }
    private void initialize() throws JWNLException, Exception {

        leftLen = _leftTokens.length - 1;
        rightLen = _rightTokens.length - 1;
        cost = new BigDecimal[leftLen + 1][rightLen + 1];
        //SimilarityMeasure measure = new SemanticSimilarity();
       // SimilarityMeasure measure2 = new Leven();
        ISub isubM=new ISub();
        WordNetMatcher wnM=new WordNetMatcher();
        //calculation of similarities between all wards
        for (int i = 0; i <= leftLen; i++) {
            for (int j = 0; j <= rightLen; j++) {
                //maximal result from all measuers is taken int account
                BigDecimal sim1 = BigDecimal.valueOf(isubM.getSimilarity(_leftTokens[i], _rightTokens[j]));//measure.getSimilarity(_leftTokens[i], _rightTokens[j]));
                BigDecimal sim2 = BigDecimal.valueOf(wnM.compute(_leftTokens[i], _rightTokens[j]));//measure2.getSimilarity(_leftTokens[i], _rightTokens[j]));
//                System.out.println("A: " + _leftTokens[i] + " B: " + _rightTokens[j] + " SS: " + sim1 + " LS: " + sim2);
                if (sim1.compareTo(sim2) == 1) {
                    cost[i][j] = sim1;
//                    System.out.println("A: " + _leftTokens[i] + " B: " + _rightTokens[j] + " S: " + sim1);
                } else {
                    cost[i][j] = sim2;
//                    System.out.println("A: " + _leftTokens[i] + " B: " + _rightTokens[j] + " S: " + sim2);
                }
               
            }
        }
    }

    public double getScore() {
        BipartiteMatcher match = new BipartiteMatcher(_leftTokens, _rightTokens, cost);
        return match.getScore();
    }
}

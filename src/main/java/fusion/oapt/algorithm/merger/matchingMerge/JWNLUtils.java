package fusion.oapt.algorithm.merger.matchingMerge;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Pointer;
import net.didion.jwnl.data.PointerType;
import net.didion.jwnl.data.PointerUtils;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.list.PointerTargetNode;
import net.didion.jwnl.data.list.PointerTargetNodeList;
import net.didion.jwnl.data.relationship.AsymmetricRelationship;
import net.didion.jwnl.data.relationship.Relationship;
import net.didion.jwnl.data.relationship.RelationshipFinder;
import net.didion.jwnl.data.relationship.RelationshipList;
import net.didion.jwnl.dictionary.Dictionary;

public class JWNLUtils {

    //meronym - a element that is a part of something
    public final static int MERONYM = 1;
    //holonym - an element that is a part of something bigger, opposite to meronym
    public final static int HOLONYM = 2;
    //attribute - an element that defines given word
    public final static int ATTRIBUTES = 3;
    //see also - words related to given word
    public final static int ALSO_SEE = 4;
    //antonyms - words opposite to given word
    public final static int ANTONYMS = 5;
    //synonyms - words meaning the same as given word
    public final static int SYNONYMS = 6;
    //coordinate terms - hyponyms of word's hypernyms (siblings)
    public final static int COORDINATE_TERMS = 7;
    //similar to - words with meaning close to given word
    public final static int SIMILAR_TO = 8;
    //concepts are considered equal
    public final static int DIFFERENT = 0;
    //concepts are consifered different
    public final static int EQUAL = 1;
    //first concept is considered to be more general than second
    public final static int FIRST_MORE_GENERAL_THAN_SECOND = 2;
    //second concept is considered to be more general than first
    public final static int SECOND_MORE_GENERAL_THAN_FIRST = 3;
    //both concepts are simillar but are siblings according to wordnet
    public final static int SIBLINGS = 4;

    /**
     * Returns list of polysemy counts for word.
     * @param word word to examine
     * @return Count of each polysem for given word, result[0] is count for noun, 1 for verb, 2 for adjective, 3 for adverb
     * @throws net.didion.jwnl.JWNLException
     */
    public static int[] getPolysemyCount(String word) throws JWNLException {
        Dictionary dict = Dictionary.getInstance();
        int[] polysemies = new int[4];
        IndexWord noun_form = dict.getIndexWord(POS.NOUN, word);
        polysemies[0] = (noun_form == null) ? 0 : noun_form.getSenses().length;
        IndexWord verb_form = dict.getIndexWord(POS.VERB, word);
        polysemies[1] = (verb_form == null) ? 0 : verb_form.getSenses().length;
        IndexWord adj_form = dict.getIndexWord(POS.ADJECTIVE, word);
        polysemies[2] = (adj_form == null) ? 0 : adj_form.getSenses().length;
        IndexWord adv_form = dict.getIndexWord(POS.ADVERB, word);
        polysemies[3] = (adv_form == null) ? 0 : adv_form.getSenses().length;
        return polysemies;
    }

    /**
     * Returns the POS of the word with the highest counts of polysems. When count of polysems is equal for more than one POS, the order is: noun, verb, adjective, adverb.
     * @param word word to examine
     * @return Best match for POS for egzamined word
     * @throws net.didion.jwnl.JWNLException
     */
    public static POS getBestPOS(String word) throws JWNLException {
        Dictionary dict = Dictionary.getInstance();

        POS pos = POS.NOUN;
        IndexWord current_form = dict.getIndexWord(POS.NOUN, word);
        int count = (current_form == null) ? 0 : current_form.getSenses().length;

        current_form = dict.getIndexWord(POS.VERB, word);
        if ((current_form != null) && (current_form.getSenses().length > count)) {
            count = current_form.getSenses().length;
            pos = POS.VERB;
        }

        current_form = dict.getIndexWord(POS.ADJECTIVE, word);
        if ((current_form != null) && (current_form.getSenses().length > count)) {
            count = current_form.getSenses().length;
            pos = POS.ADJECTIVE;
        }

        current_form = dict.getIndexWord(POS.ADVERB, word);
        if ((current_form != null) && (current_form.getSenses().length > count)) {
            count = current_form.getSenses().length;
            pos = POS.ADVERB;
        }
        return pos;
    }

    /**
     * Generates coma separated list of words (surrounded with quotes) that are a part of given sysnet
     * @param synset a Synset for which list of words is generated
     * @return String that contain the synset content as a coma separated list of words surrounded with quotes
     */
    public static String getListOfWords(Synset synset) {
        String words = "";
        for (int k = 0; k < synset.getWordsSize(); k++) {
            if (k == 0) {
                words = "\"" + synset.getWord(k).getLemma() + "\"";
            } else {
                words = words.concat(", \"" + synset.getWord(k).getLemma() + "\"");
            }
        }
        return words;
    }

    /**
     * Prints only direct meronyms/holonyms of given word. Only the first meaning of given word is considered.
     * @param word an IdexWord for which meronyms are looked for
     * @param type the type of relation that should be printed, listed and described in JWNLUtils
     * @throws net.didion.jwnl.JWNLException
     * @see JWNLUtils
     */
    public static void printDirectRelationResults(IndexWord word, int type, int meaning) throws JWNLException {
        //get list of direct holonyms of the first meaning of given word
        PointerTargetNodeList nodeList = null;
        switch (type) {
            case JWNLUtils.HOLONYM:
                nodeList = PointerUtils.getInstance().getHolonyms(word.getSense(meaning));
                break;
            case JWNLUtils.MERONYM:
                nodeList = PointerUtils.getInstance().getMeronyms(word.getSense(meaning));
                break;
            case JWNLUtils.ATTRIBUTES:
                nodeList = PointerUtils.getInstance().getAttributes(word.getSense(meaning));
                break;
            case JWNLUtils.ALSO_SEE:
                nodeList = PointerUtils.getInstance().getAlsoSees(word.getSense(meaning));
                break;
            case JWNLUtils.ANTONYMS:
                nodeList = PointerUtils.getInstance().getAntonyms(word.getSense(meaning));
                break;
            case JWNLUtils.SYNONYMS:
                nodeList = PointerUtils.getInstance().getSynonyms(word.getSense(meaning));
                break;
            case JWNLUtils.COORDINATE_TERMS:
                nodeList = PointerUtils.getInstance().getCoordinateTerms(word.getSense(meaning));
                break;
            default:
                System.out.println("Wrong relation type!");
                return;
        }

        //if there are any we print each meaning of them
        if (nodeList.size() > 0) {
            //printing only words contained within each synset
            Object[] resultsAsArray = nodeList.toArray();
            for (int i = 0; i < resultsAsArray.length; i++) {
                Synset synset = ((PointerTargetNode) resultsAsArray[i]).getSynset();
                System.out.println("  " + JWNLUtils.getListOfWords(synset));
            }
        }
    }

    /**
     * Finds and prints all pointers related to given word. Printed information includes pointer type, part of speach that it applies to and target words.
     * @param word IndexWord for which pointers are being looked for
     * @throws net.didion.jwnl.JWNLException
     */
    public static void printAllPointers(IndexWord word) throws JWNLException {
        Synset[] senses = word.getSenses();
        for (Synset sense : senses) {
            Pointer[] pointers = sense.getPointers();
            for (Pointer pointer : pointers) {
                System.out.println(pointer.getType().toString());
                System.out.println("Words: " + JWNLUtils.getListOfWords(pointer.getTargetSynset()));
                System.out.println("Gloss: " + pointer.getTargetSynset().getGloss());
            }
            System.out.println();
        }
    }

    /**
     * Divides given sentence into an array. Each field of the array contains
     * only one word. The sentence is divided by spaces.
     * @param sentence String that will be splitted into an array
     * @return Array of strings containing splitted sentence
     */
    public static String[] splitStringBySpaces(String sentence) {
//        sentence = replaceConncetors(sentence);
        String[] splitted = sentence.split(" ");
        Vector<String> tmp = new Vector<String>();
        for (int i = 0; i < splitted.length; i++) {
            String s = splitted[i];
            if (s.length() > 2) {
                tmp.addElement(s);
            }
        }
        String[] splittedWithoutEmptyStringsArray = new String[tmp.size()];
        for (int i = 0; i < tmp.size(); i++) {
            splittedWithoutEmptyStringsArray[i] = tmp.elementAt(i);
        }
        return splittedWithoutEmptyStringsArray;
    }

    /**
     * Removes duplicates form given string array
     * @param array of strings that should have the duplicates removed
     * @return array of unique strings
     */
    public static String[] removeDuplicates(String[] tokens) {

        HashSet<String> tmpSet = new HashSet<String>();
        for (String str : tokens) {
            tmpSet.add(str);
        }

        String[] result = new String[tmpSet.size()];
        tmpSet.toArray(result);

        return result;
    }

    /**
     * Prints all senses for each Part of Speach of given lemma
     * @param lemma String for whitch meanings should be printed
     * @throws net.didion.jwnl.JWNLException
     */
    public static void printAllSenses(String lemma) throws JWNLException {
        Dictionary dict = Dictionary.getInstance();
        System.out.println("Senses of the word " + lemma + ":");
        IndexWord[] iwords1 = dict.lookupAllIndexWords(lemma).getIndexWordArray();
        //for each part of speach available for that word
        for (int j = 0; j < iwords1.length; j++) {
            IndexWord word1 = iwords1[j];
            System.out.println("POS: " + word1.getPOS());
            //for each meaning (synset)
            for (int k = 1; k <= word1.getSenses().length; k++) {
                System.out.println(word1.getSense(k));
            }
        }
    }

    /**
     * Replaces word connectors in given string
     * All non letter and non digit characters are converted into spaces
     * only "-" is left unchanged
     * final string is then trimmed to remove trailing white spaces that would
     * occur in some cases
     * @param connectedWords Words to be separated
     * @return space-separated list of words
     */
    public static String replaceConncetors(String connectedWords) {
//        String str = connectedWords.replaceAll("([\'\"\\[\\]\\s\\{}():;_])+", " ").trim();
        String str = connectedWords.replaceAll("([^a-zA-ZÄ…Ä™Å›Ä‡ÅºÅ¼Ã³Å‚Å„Ä„Ä˜ÅšÄ†Å¹Å»Ã“ÅÅƒ0-9])+", " ").trim();
//        System.out.println("po usuniÄ™ciu znakÃ³w niedrukowalnych: " + str);
        return str;
    }

    /**
     * Inserts spaces between words smashed together in string
     * splitted words are distinguished by camel case, hyphen is threated as word connector
     * @param connectedWords string with camel case smashed words
     * @return string with spaces inserted before first letters of smashed words, the string is also normalized to lower case
     */
    public static String normalizeCasing(String connectedWords) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < connectedWords.length(); i++) {
            char curChar = connectedWords.charAt(i);
            if (Character.isUpperCase(curChar)) {
                if (i > 0 && connectedWords.charAt(i - 1) != " ".charAt(0)
                        && connectedWords.charAt(i - 1) != "-".charAt(0)
                        && !Character.isUpperCase(connectedWords.charAt(i - 1))) {
                    result.append(" ");
                }
                result.append(Character.toLowerCase(curChar));
            } else {
                result.append(curChar);
            }
        }
        return result.toString();
    }

    /**
     * Checks whether value of the given string represents an integer.
     * @param input String to be checked
     * @return true when string value represents an integer and false when it's not
     */
    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //TODO: zrobiÄ‡ by zwracaÅ‚o wartoÅ›c pozwalajÄ…cÄ… na automatyzacjÄ™ tego.
    //Chyba starczy dÅ‚ugoÅ›c sciezki i wspolny rodzic
    /**
     * Finds and prints a path from one word to another using hypernym relation to find common
     * parrent and homonym relation to get to ther word if necessary.
     * @param start a Synset representing word begining the path
     * @param end a Synset representing end of the path
     */
    public static void findAndPrintPath(Synset start, Synset end) throws JWNLException {
        //wyszukujemy relacji typu hypernym pomiedzy dwoma wyrazami
        RelationshipList list = RelationshipFinder.getInstance().findRelationships(start, end, PointerType.HYPERNYM);
        System.out.println("lista ma: " + list.size());
        System.out.println("Hypernym relationship between \"" + start.getGloss() + "\" and \"" + end.getGloss() + "\":");
        for (Iterator itr = list.iterator(); itr.hasNext();) {
            Relationship rel = ((Relationship) itr.next());
            System.out.println("Common Parent Index: " + ((AsymmetricRelationship) rel).getCommonParentIndex());
            System.out.println("Depth: " + ((Relationship) rel).getDepth());
            System.out.println();
            rel.getNodeList().print();
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Finds shortest path from one word to another using hypernym relation to find common
     * parrent and homonym relation to get to ther word if necessary. Basing on parent index
     * decides whether synsets start and end are siblings, synset start contains synset end
     * or vice versa. For multiple paths only the shortest one is taken into account.
     * @param start a Synset representing word begining the path
     * @param end a Synset representing end of the path
     * @return returns wheter synsets are siblings, synset start contains sysnet end or
     * synset end contains sysnset start
     */
    public static int checkRelationsBetweenSynsets(Synset start, Synset end) throws JWNLException {
        //wyszukujemy relacji typu hypernym pomiedzy dwoma wyrazami
        RelationshipList list = RelationshipFinder.getInstance().findRelationships(start, end, PointerType.HYPERNYM);
//        System.out.println("lista ma: " + list.size());

        int depth = -1;
        int commonParent = -1;

        for (Iterator itr = list.iterator(); itr.hasNext();) {
            Relationship rel = ((Relationship) itr.next());
            int currentDepth = ((Relationship) rel).getDepth();
            if ((depth == -1) || currentDepth < depth) {
                depth = currentDepth;
                commonParent = ((AsymmetricRelationship) rel).getCommonParentIndex();
            }
        }

        if (commonParent == 0) {
            return JWNLUtils.FIRST_MORE_GENERAL_THAN_SECOND;
        } else if (commonParent == depth) {
            return JWNLUtils.SECOND_MORE_GENERAL_THAN_FIRST;
        } else {
            return JWNLUtils.SIBLINGS;
        }
    }

    /**
     * Compares two given strings using bipartite graphs. Similarity between two given words
     * is calculated as maximum from semantic similarity and Levenhstein distance
     * Compared strings are normalized to lower case, stripped of connectors and tokenized into
     * individual strings
     * @param aString first of the two compared strings
     * @param bString seconf of the two compared strings
     * @return value form 0 to 1 showing how similar the strings are, where 0 means no similarity
     * and 1 means identical strings (after normalization and tokenization!)
     */
    public static double compareTexts(String aString, String bString) throws JWNLException, Exception {
        String[] aStrings = (String[]) JWNLUtils.splitStringBySpaces(JWNLUtils.replaceConncetors(JWNLUtils.normalizeCasing(aString)));
        String[] bStrings = (String[]) JWNLUtils.splitStringBySpaces(JWNLUtils.replaceConncetors(JWNLUtils.normalizeCasing(bString)));

        double score = compareTexts(aStrings, bStrings);
        return score;
    }

    /**
     * Compares two given strings using bipartite graphs. Similarity between two given words
     * is calculated as maximum from semantic similarity and Levenhstein distance
     * Expects compared strings as string arrays with content normalized to lower case, each string in array
     * shoul represent only one word
     * @param aStrings first tokenized array of the two compared strings
     * @param bStrings seconf tokenized array of the two compared strings
     * @return value form 0 to 1 showing how similar the strings are, where 0 means no similarity
     * and 1 means identical string arrays
     */
    public static double compareTexts(String[] aStrings, String[] bStrings) throws JWNLException, Exception {
//        for(String text : aStrings) {
//            System.out.println("A: " + text);
//        }
//
//        for(String text : bStrings) {
//            System.out.println("B: " + text);
//        }

        MatchsMaker match = new MatchsMaker(aStrings, bStrings);
        double score = match.getScore();
        return score;
    }
}


package fusion.oapt.algorithm.merger.matchingMerge;

import net.didion.jwnl.data.POS;

/**
 *Container class storing results of similarity finding
 * @author boing
 */
public class BestSimValueContainer {
    private POS bestPos;
    private int bestSenseOfFirst;
    private int bestSenseOfSecond;
    private double bestSim;
    private String firstWord;
    private String secondWord;

    public BestSimValueContainer(String firstWord, String secondWord, POS bestPos, int bestSenseOfFirst, int bestSenseOfSecond, double bestSim) {
        this.bestPos = bestPos;
        this.bestSenseOfFirst = bestSenseOfFirst;
        this.bestSenseOfSecond = bestSenseOfSecond;
        this.bestSim = bestSim;
        this.firstWord = firstWord;
        this.secondWord = secondWord;
    }

    public int getBestSenseOfFirst() {
        return bestSenseOfFirst;
    }

    public int getBestSenseOfSecond() {
        return bestSenseOfSecond;
    }

    public double getBestSim() {
        return bestSim;
    }

    public POS getBestPos() {
        return bestPos;
    }

        public String getFirstWord() {
        return firstWord;
    }

    public String getSecondWord() {
        return secondWord;
    }
    
    public void setBestPos(POS bestPos) {
        this.bestPos = bestPos;
    }

    public void setBestSenseOfFirst(int bestSenseOfFirst) {
        this.bestSenseOfFirst = bestSenseOfFirst;
    }

    public void setBestSenseOfSecond(int bestSenseOfSecond) {
        this.bestSenseOfSecond = bestSenseOfSecond;
    }

    public void setBestSim(double bestSim) {
        this.bestSim = bestSim;
    }

    public void setFirstWord(String firstWord) {
        this.firstWord = firstWord;
    }

    public void setSecondWord(String secondWord) {
        this.secondWord = secondWord;
    }


    @Override
    public String toString() {
        String str = "Best matching (" + bestSim + ") was found for " + bestPos + " for meanings: first(" + firstWord + " " + bestSenseOfFirst + ") and second(" + secondWord + " " + bestSenseOfSecond + ")";
        return str;
    }

    public String toShortString() {
        String str = firstWord + " " + secondWord + " " + bestSim;
        return str;
    }

}


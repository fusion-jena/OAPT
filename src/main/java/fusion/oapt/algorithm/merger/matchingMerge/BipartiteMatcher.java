package fusion.oapt.algorithm.merger.matchingMerge;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author boing
 */
public class BipartiteMatcher {

    private String[] leftTokens, rightTokens;
    private BigDecimal[][] cost;
    private BigDecimal[] leftLabel, rightLabel;
    private int[] _previous, _incomming, _outgoing; //connect with the left and right
    private boolean[] leftVisited, rightVisited;
    int numOfLeftTokens, numOfRightTokens;
    boolean _errorOccured = false;
    boolean stop = false;

    public BipartiteMatcher(String[] left, String[] right, BigDecimal[][] cost) {
        leftTokens = left;
        rightTokens = right;
        //swap
        if (leftTokens.length > rightTokens.length) {
            BigDecimal[][] tmpCost = new BigDecimal[rightTokens.length][leftTokens.length];

            for (int i = 0; i < rightTokens.length; i++) {
                for (int j = 0; j < leftTokens.length; j++) {
                    tmpCost[i][j] = cost[j][i];
                }
            }


            this.cost = tmpCost;

            String[] tmp = leftTokens;
            leftTokens = rightTokens;
            rightTokens = tmp;
        } else {
            this.cost = cost;
        }


//        for (int i = 0; i < leftTokens.length; i++) {
//            System.out.println(leftTokens[i]);
//            for (int j = 0; j < rightTokens.length; j++) {
//                System.out.println(rightTokens[j] + " " + cost[i][j]);
//            }
//        }

        myInit();

        makeMatching();
    }

    private void myInit() {
        initialize();

        leftVisited = new boolean[numOfLeftTokens + 1];
        rightVisited = new boolean[numOfRightTokens + 1];
        _previous = new int[(numOfLeftTokens + numOfRightTokens) + 2];
    }

    private void initialize() {
        numOfLeftTokens = leftTokens.length - 1;
        numOfRightTokens = rightTokens.length - 1;

        leftLabel = new BigDecimal[numOfLeftTokens + 1];
        rightLabel = new BigDecimal[numOfRightTokens + 1];


        for (int i = 0; i < leftLabel.length; i++) {
            leftLabel[i] = new BigDecimal(0);
        }

        for (int i = 0; i < rightLabel.length; i++) {
            rightLabel[i] = new BigDecimal(0);
        }

        //init distance
        for (int i = 0; i <= numOfLeftTokens; i++) {

            BigDecimal maxLeft = new BigDecimal(-1 * Double.MIN_VALUE);


            for (int j = 0; j <= numOfRightTokens; j++) {
                if ((cost[i][j]).compareTo(maxLeft) == 1) {
                    maxLeft = cost[i][j];
//                    System.out.println("maxLeft[" + i + "][" + j + "] = " + maxLeft);
                }
                leftLabel[i] = maxLeft;
            }
//            System.out.println("leftLabel[" + i + "] = " + maxLeft);
        }
    }

    private void flush() {
        for (int i = 0; i < _previous.length; i++) {
            _previous[i] = -1;
        }
        for (int i = 0; i < leftVisited.length; i++) {
            leftVisited[i] = false;
        }
        for (int i = 0; i < rightVisited.length; i++) {
            rightVisited[i] = false;
        }
    }

    boolean findPath(int source) {
//        System.out.println("Source: " + source);
        flush();
        stop = false;
        walk(source);

        return stop;
    }

    void increaseMatchs(int li, int lj) {
        int[] tmpOut = _outgoing;

        int i, j, k;
        i = li;
        j = lj;
        _outgoing[i] = j;
        _incomming[j] = i;
//        System.out.println("I1 = " + _incomming[j]);


        if (_previous[i] != -1) {
            do {
                j = tmpOut[i];
                k = _previous[i];
                _outgoing[k] = j;
                _incomming[j] = k;
//                System.out.println("I2 = " + _incomming[j]);
                i = k;
            } while (_previous[i] != -1);
        }
    }

    private void walk(int i) {
        leftVisited[i] = true;
//        System.out.println("Walk[" + i + "]");

        for (int j = 0; j <= numOfRightTokens; j++) {
//            System.out.println("1: " + rightVisited[j] + " " + leftLabel[i] + " " + rightLabel[j] + " " + cost[i][j]);
            if (stop) {
                return;
            } else if (!rightVisited[j] && (leftLabel[i].add(rightLabel[j]).compareTo(cost[i][j]) == 0)) {
                if (_incomming[j] == -1) {// if found a path
                    stop = true;
                    increaseMatchs(i, j);
                    return;
                } else {
                    int k = _incomming[j];
                    rightVisited[j] = true;
                    _previous[k] = i;
                    walk(k);
                }
            }
        }
    }
//        #region BreadFirst
//    boolean findPath(int source) {
//        int head, tail, idxHead = 0;
//        int[] visited = new int[(numOfLeftTokens + numOfRightTokens) + 2],
//                q = new int[(numOfLeftTokens + numOfRightTokens) + 2];
//        head = 0;
//        for (int i = 0; i < visited.length; i++) {
//            visited[i] = 0;
//        }
//        flush();
//
//        head = -1;
//        tail = 0;
//        q[tail] = source;
//        visited[source] = 1;
//        leftVisited[source] = true;
//        int nMerge = numOfLeftTokens + numOfRightTokens + 1;
//
//        while (head <= tail) {
//            ++head;
//            idxHead = q[head];
//
//
//            for (int j = 0; j <= (numOfLeftTokens + numOfRightTokens + 1); j++) {
//                if (visited[j] == 0) {
//                    if (j > numOfLeftTokens) //j is stay at the RightSide
//                    {
//                        int idxRight = j - (numOfLeftTokens + 1);
//                        if ((idxHead <= numOfLeftTokens) && (leftLabel[idxHead] + rightLabel[idxRight] == cost[idxHead][idxRight])) {
//                            ++tail;
//                            q[tail] = j;
//                            visited[j] = 1;
//                            _previous[j] = idxHead;
//                            rightVisited[idxRight] = true;
//                            if (_incomming[idxRight] == -1) // pretty good, found a path
//                            {
//                                return true;
//                            }
//
//                        }
//                    } else if (j <= numOfLeftTokens) // is stay at the left
//                    {
//                        if (idxHead > numOfLeftTokens && _incomming[idxHead - (numOfLeftTokens + 1)] == j) {
//                            ++tail;
//                            q[tail] = j;
//                            visited[j] = 1;
//                            _previous[j] = idxHead;
//                            leftVisited[j] = true;
//                        }
//                    }
//                }
//            }
//        }
//
//        return false;//not found
//    }
//
//    void Increase_Matchs(int j) {
//        if (_previous[j] != -1) {
//            do {
//                int i = _previous[j];
//                _outgoing[i] = j - (numOfLeftTokens + 1);
//                _incomming[j - (numOfLeftTokens + 1)] = i;
//                j = _previous[i];
//            } while (j != -1);
//        }
//    }

//           #endregion
    BigDecimal getMinDeviation() {
        BigDecimal min = new BigDecimal(Double.MAX_VALUE);

        for (int i = 0; i <= numOfLeftTokens; i++) {
            if (leftVisited[i]) {
                for (int j = 0; j <= numOfRightTokens; j++) {
                    if (!rightVisited[j]) {
                        if ((leftLabel[i]).add(rightLabel[j]).subtract(cost[i][j]).compareTo(min) == -1) {
                            min = (leftLabel[i].add(rightLabel[j])).subtract(cost[i][j]);
                        }
                    }
                }
            }
        }
        return min;
    }

    private void relabels() {
        BigDecimal dev = getMinDeviation();
//        System.out.println("Deviation: " + dev);

        for (int k = 0; k <= numOfLeftTokens; k++) {
            if (leftVisited[k]) {
//                leftLabel[k] -= dev;
                leftLabel[k] = (leftLabel[k]).subtract(dev);
            }
        }

        for (int k = 0; k <= numOfRightTokens; k++) {
            if (rightVisited[k]) {
//                rightLabel[k] += dev;
                rightLabel[k] = (rightLabel[k]).add(dev);
            }
        }
    }

    private void makeMatching() {
        _outgoing = new int[numOfLeftTokens + 1];
        _incomming = new int[numOfRightTokens + 1];


        for (int i = 0; i < _outgoing.length; i++) {
            _outgoing[i] = -1;
        }

        for (int i = 0; i < _incomming.length; i++) {
            _incomming[i] = -1;
        }

        for (int k = 0; k <= numOfLeftTokens; k++) {
            if (_outgoing[k] == -1) {
                boolean found = false;

                do {
//                    System.out.println("k = " + k);
                    found = findPath(k);

                    if (!found) {
                        relabels();
                    }
                } while (!found);
            }
        }
    }

    private BigDecimal getTotal() {
        BigDecimal nTotal = new BigDecimal(0);

//        BigDecimal nA = 0;

        for (int i = 0; i <= numOfLeftTokens; i++) {
            if (_outgoing[i] != -1) {
                nTotal = nTotal.add(cost[i][_outgoing[i]]);
//                System.out.println(leftTokens[i] + " <-> " + rightTokens[_outgoing[i]] + " : " + cost[i][_outgoing[i]]);

//                BigDecimal a = 1.0F - Math.max(leftTokens[i].length(), rightTokens[_outgoing[i]].length()) != 0 ? cost[i][_outgoing[i]] / Math.max(leftTokens[i].length(), rightTokens[_outgoing[i]].length()) : 1;
//                nA += a;
            }
        }

//        System.out.println(numOfLeftTokens + " " + numOfRightTokens);

        return nTotal;
    }

    public double getScore() {
        BigDecimal dis = getTotal();

        double maxLen = numOfRightTokens + 1;

//        int l1 = 0;
//        int l2 = 0;

//        for (String s : rightTokens) {
//            l1 += s.length();
//        }
//
//        for (String s : leftTokens) {
//            l2 += s.length();
//        }
//        maxLen = Math.max(l1, l2);



        if (_errorOccured) {
            return 0;
        } else if (maxLen > 0) {
//            System.out.println("tutaj1 " + dis + " " + maxLen);
//            return dis / maxLen;
            return (dis.divide(BigDecimal.valueOf(maxLen),20, RoundingMode.UP)).doubleValue();
        } else {
//            System.out.println("tutaj");
            return 1.0F;
        }
    }
//    public double Score {
//        get {
//            if (_errorOccured) {
//                return 0;
//            } else {
//                return GetScore();
//            }
//        }
//    }
}

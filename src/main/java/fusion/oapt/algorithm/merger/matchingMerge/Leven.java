package fusion.oapt.algorithm.merger.matchingMerge;

public class Leven implements SimilarityMeasure{

    private int min3(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    private int computeDistance(String s, String t) {
        int n = s.length();
        int m = t.length();
        int[][] distance = new int[n + 1][m + 1];
        int cost = 0;

        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        //init1
        for (int i = 0; i <= n; distance[i][0] = i++) ;
        for (int j = 0; j <= m; distance[0][j] = j++) ;

        //find min distance
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                cost = (t.substring(j - 1, j).equals(s.substring(i - 1, i)) ? 0 : 1);
                distance[i][j] = min3(distance[i - 1][j] + 1,
                        distance[i][j - 1] + 1,
                        distance[i - 1][j - 1]
                        + cost);
            }
        }

        return distance[n][m];
    }

    public double getSimilarity(String string1, String string2) {

        double dis = computeDistance(string1, string2);
        double maxLen = string1.length();
        if (maxLen < (double) string2.length()) {
            maxLen = string2.length();
        }

        double minLen = string1.length();
        if (minLen > (double) string2.length()) {
            minLen = string2.length();
        }


        if (maxLen == 0.0F) {
            return 1.0F;
        } else {
            return 1.0F - dis/maxLen;
        }
    }

    public Leven() {
    }
}


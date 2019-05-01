
package fusion.oapt.general.cc;

import java.util.Iterator;

import fusion.oapt.model.Constant;
import fusion.oapt.model.Node;
import fusion.oapt.model.NodeCategory;
import fusion.oapt.model.RBGModel;
import fusion.oapt.general.output.Alignment;
import fusion.oapt.general.cc.Parameters;

public class LingComp
{
    private RBGModel modelA = null,  modelB = null;
    private int counts[] = new int[6];

    public LingComp(RBGModel modelA, RBGModel modelB)
    {
        this.modelA = modelA;
        this.modelB = modelB;
        countNodesByCategory();
    }

    private void countNodesByCategory()
    {
        for (Iterator<Node> iter = modelA.listNodes(); iter.hasNext();) {
            Node node = iter.next();
            int category = NodeCategory.getCategoryWithoutExternal(node);
            if (category == Constant.ONTOLOGY_CLASS) {
                if (!node.isAnon()) {
                    counts[0]++;
                }
            } else if (category == Constant.ONTOLOGY_PROPERTY) {
                if (!node.isAnon()) {
                    counts[1]++;
                }
            } else if (category == Constant.ONTOLOGY_INSTANCE) {
                if (!node.isAnon()) {
                    counts[2]++;
                }
            }
        }
        for (Iterator<Node> iter = modelB.listNodes(); iter.hasNext();) {
            Node node = iter.next();
            int category = NodeCategory.getCategoryWithoutExternal(node);
            if (category == Constant.ONTOLOGY_CLASS) {
                if (!node.isAnon()) {
                    counts[3]++;
                }
            } else if (category == Constant.ONTOLOGY_PROPERTY) {
                if (!node.isAnon()) {
                    counts[4]++;
                }
            } else if (category == Constant.ONTOLOGY_INSTANCE) {
                if (!node.isAnon()) {
                    counts[5]++;
                }
            }
        }
    }

    private int[] countSizeWithoutInstances()
    {
        int sizes[] = new int[2];
        if (counts[0] != 0 && counts[3] != 0) {
            sizes[0] += counts[0];
            sizes[1] += counts[3];
        }
        if (counts[1] != 0 && counts[4] != 0) {
            sizes[0] += counts[1];
            sizes[1] += counts[4];
        }
        return sizes;
    }

    private int[] countSizeWithInstances()
    {
        int sizes[] = new int[2];
        if (counts[0] != 0 && counts[3] != 0) {
            sizes[0] += counts[0];
            sizes[1] += counts[3];
        }
        if (counts[1] != 0 && counts[4] != 0) {
            sizes[0] += counts[1];
            sizes[1] += counts[4];
        }
        if (counts[2] != 0 && counts[5] != 0) {
            sizes[0] += counts[2];
            sizes[1] += counts[5];
        }
        return sizes;
    }

    public int estimate1(Alignment as)
    {
        int sizes[] = countSizeWithoutInstances();
        int temp = sizes[0] < sizes[1] ? sizes[0] : sizes[1];
        int elemSetSize = as.size(Parameters.lingHighSim);
        double value = elemSetSize / (double) (temp);
        if (value > Parameters.lingHighValue) {
            return Parameters.highComp;
        } else if (value > Parameters.lingLowValue) {
            return Parameters.mediumComp;
        } else {
            return Parameters.lowComp;
        }
    }

    public int estimate2(Alignment as)
    {
        int sizes[] = countSizeWithInstances();
        int temp = sizes[0] < sizes[1] ? sizes[0] : sizes[1];
        int elemSetSize = as.size(Parameters.lingHighSim);
        double value = elemSetSize / (double) temp;
        if (value > Parameters.lingHighValue) {
            return Parameters.highComp;
        } else if (value > Parameters.lingLowValue) {
            return Parameters.mediumComp;
        } else {
            return Parameters.lowComp;
        }
    }
}

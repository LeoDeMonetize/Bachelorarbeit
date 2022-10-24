import javax.xml.crypto.dsig.spec.HMACParameterSpec;
import java.security.interfaces.ECKey;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PST {

    ElementPST root;

    public int getHIS(int[] a) throws Exception {
        buildHIS(a);
        ArrayList<ElementPST> Points = new ArrayList(root.Cv);

        for (ElementPST e : Points) {

            ElementPST MaxQ = RMaxQ(e);

            if (MaxQ.value == -10) {
                continue;
            }
            e.prec = MaxQ;
            e.score = e.value + MaxQ.score;
            root.updatePv(e);
        }

        return root.Cv.stream().map(k -> k.score).max(Comparator.naturalOrder()).get();
    }

    private ElementPST RMaxQ(ElementPST e) {
        return root.RMaxQStart(e);
    }

    private void buildHIS(int[] a) {
        //Creating a list of tree-Elements
        //-1 means -infty
        //Sorting beforehand for building the tree

        AtomicInteger i = new AtomicInteger(-1);
        List<ElementPST> collection = Arrays.stream(a).mapToObj(j -> new ElementPST(j, i.incrementAndGet())).toList();

        //Setting the Cv of the leafs as leafs (Will not matter in calculations)
        for (ElementPST elementPST : collection) {
            elementPST.setCvLeaf();
            elementPST.Pv = null;
        }
        ArrayList<ElementPST> tuples = new ArrayList<>(collection);
        tuples.sort(ElementPST::compareTo);
        ArrayList<ElementPST> nextIteration;
        //Building the PST From Bottom up
        //Merging two succeding Elements into one
        //When all Elements merged, new Elements will be
        //Used in next Step until only one is left
        while (tuples.size() != 1) {
            nextIteration = new ArrayList<>();
            while (!tuples.isEmpty()) {
                ElementPST temp = new ElementPST(0, 0);
                temp.setLeft(tuples.remove(0));
                if (tuples.isEmpty()) {
                    temp.setRight(new ElementPST(Integer.MAX_VALUE, -1));
                    temp.Cv = new ArrayList<>(temp.left.Cv);
                    nextIteration.add(temp);
                } else {
                    temp.setRight(tuples.remove(0));
                    temp.Cv = new ArrayList<>(temp.left.Cv);
                    temp.Cv.addAll(temp.right.Cv);
                    nextIteration.add(temp);
                }
                //Hv is the last Element of Cv, since Cv is orderes in the first step
                temp.Hv = temp.Cv.get(temp.Cv.size() - 1).value;
                //Pv is undefined for inner nodes at first
                temp.Pv = null;
            }
            tuples = nextIteration;
        }
        root = tuples.remove(0);
        //Now that we have the Tree, we need to set the Pv of the nodes
        //For the first time
        root.setPv();
    }

}

class ElementPST implements Comparable<ElementPST> {
    int score; //Sum of HIS for that number
    int priority; //Position in Array
    int value; //Value of Number
    ArrayList<ElementPST> Cv = new ArrayList<>();//All Leaves under this Node
    ArrayList<ElementPST> usedAsPv = new ArrayList<>();
    int Hv; //heaviest Leaf under this node
    ElementPST Pv; //Biggest Score of leaves under this node, that is not Pv of a node under this one
    ElementPST left;
    ElementPST right;
    ElementPST prec; //Reconstructing HIS
    static ElementPST maxPoint; //RMaxQ

    public ElementPST(int value, int priority) {
        this.value = value;
        this.score = value;
        this.priority = priority;

    }

    public void setPv() {
        if (left == null && right == null)
            Pv = new ElementPST(-10, -10);
        else {
            //Now we need to get the biggest score in the List of Cv
            //That is not used in a lower Node as Pv
            //We do this bottomUp, so we start at the lowest node
            left.setPv();
            right.setPv();
            ArrayList<ElementPST> availableAsPv = new ArrayList<>(Cv);
            usedAsPv.addAll(left.usedAsPv);
            usedAsPv.addAll(right.usedAsPv);
            availableAsPv.removeAll(usedAsPv);
            if (availableAsPv.isEmpty())
                return;
            Pv = availableAsPv.get(0);
            for (ElementPST e : availableAsPv) {
                if (Pv.score < e.score)
                    Pv = e;
            }
            usedAsPv.add(Pv);
        }
    }

    public void setCvLeaf() {
        Cv.add(this);
    }

    public void setLeft(ElementPST left) {
        this.left = left;
    }

    public void setRight(ElementPST right) {
        this.right = right;
    }

    public String toString() {
        String output = "";
        if (left != null) {
            output += left.toString();
        }
        output += " (" + value + "," + priority + "," + score + "," + Pv + ") ";
        if (right != null) {
            output += right.toString();
        }
        return output;
    }

    @Override
    public int compareTo(ElementPST o) {
        return this.value - o.value;
    }

    public ElementPST RMaxQStart(ElementPST e) {
        maxPoint = new ElementPST(-10, -10);
        RMaxQ(e);

        return maxPoint;
    }

    //Finds the largest Element smaller than e in the Tree
    private void RMaxQ(ElementPST e) {
        if (Pv != null && Pv.priority < e.priority && Pv.value < e.value && Pv.score >= maxPoint.score) {
            maxPoint = Pv;
        }
        if (left != null) {
            left.RMaxQ(e);
            if (e.value > left.Hv) {
                right.RMaxQ(e);
            }
        }
    }

    public void updatePv(ElementPST e) {
        if (left != null) {
            if (Pv == null) {
                return;
            }
            if (Pv.score < e.score) {
                swap(Pv, e);
                if (e.value < left.Hv)
                    left.updatePv(e);
                else
                    right.updatePv(e);
            }
        }
    }

    public ElementPST(ElementPST other) {
        this.score = other.score;
        this.priority = other.priority;
        this.value = other.value;
        this.Cv = other.Cv;
        this.usedAsPv = other.usedAsPv;
        this.Hv = other.Hv;
        this.Pv = other.Pv;
        this.left = other.left;
        this.right = other.right;
        this.prec = other.prec;
    }

    public static void swap(ElementPST one, ElementPST two) {

        ElementPST temp = new ElementPST(one);
        one = two;
        two = temp;
    }


}

/*
    This code is based on a Tutorial from https://www.baeldung.com/java-avl-trees

    The Licensing is as follows:
    MIT License

Copyright (c) 2022 Leonardowitsch Auterhoff

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

import javax.swing.*;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class AVLTree{

    AVLElement root;

    public AVLTree() {
        root = null;
    }

    public AVLElement HIS(int[] a){
        AtomicInteger start = new AtomicInteger(0);
        for (int i = 0; i < a.length; i++) {
            //System.out.println("current "+i+" "+a[i]);
            AVLElement temp = new AVLElement(i, a[i]);
            AVLElement prev = RMaxQ(root,temp,leftMost(root));
            //System.out.println(temp);
            //System.out.println("prev "+prev);
            if (prev!=null &&prev.weight< temp.weight) {
                temp.score = temp.weight + prev.score;
                temp.prec = prev;
            } else
                temp.score = temp.weight;
            //System.out.println("inserted "+temp);
            //System.out.println();
            insert(temp);

            //System.out.println();
        }
        return biggestScore(root);
    }

    public AVLElement findNext(AVLElement current){
        if(current.right!=null){
            return findLeftMost(current);
        }else
        {
            AVLElement succ = null;
            AVLElement anc = current;
            while(anc!=current){

                if(current.compareTo(anc) <0){
                    succ=anc;
                    anc=anc.left;
                }else
                    anc=anc.right;
            }
            return succ;
        }
    }

    public AVLElement findLeftMost(AVLElement current){
        if(current==null){
            return null;
        }
        while(current.left!=null){
            current=current.left;
        }
        return current;
    }
    public AVLElement biggestScore(AVLElement start){
        if(start==null)
            return null;
        AVLElement left = biggestScore(start.left);
        AVLElement right = biggestScore(start.right);
        if(left== null && right == null)
            return start;
        if(left==null||right==null){
            return left == null?
                    (start.score > right.score ? start : right) :
                    (start.score > left.score ? start : left);
        }else{
            left = start.score > left.score ? start : left;
            left = left.score > right.score ? left : right;
            return left;
        }
    }

    //No 2 Nodes with a same value can be stored
    //Starts inserting an Element at the root
    public AVLElement insert(AVLElement toInsert){
        root=insert(root,toInsert);
        return root;
    }

    public AVLElement insert(AVLElement e, AVLElement toInsert){
        if(e==null){
            return toInsert;
        }else if(e.compareTo(toInsert) > 0){
            e.left=insert(e.left,toInsert);
        }else if(e.compareTo(toInsert) < 0){
            e.right=insert(e.right,toInsert);
        }else
            throw new IllegalArgumentException("Value is already stored in the list");

        return rebalance(e);
    }

    //deleting an Element in an AVL Tree
    //Finds the Element recursively and then propagates the delete
    public AVLElement delete(AVLElement current, AVLElement toDelete){
        if(current == null)
            return current;
        else if(current.compareTo(toDelete) > 0)
            current.left=delete(current.left,toDelete);
        else if(current.compareTo(toDelete) < 0)
            current.right=delete(current.right,toDelete);
        else {
            if(current.left==null || current.right == null){
                current = (current.left==null)? current.right : current.left;
            }else{
                AVLElement leftMost = leftMost(current.right);
                current.setValues(leftMost);
                current.right=delete(current.right,leftMost);
            }
        }

        if(current!=null)
            current=rebalance(current);

        return current;
    }

    //Getting the left Most Child of a node
    public AVLElement leftMost(AVLElement e){
        if(e==null)
            return null;
        while(e.left!=null){
            e=e.left;
        }
        return e;
    }


    //Getting the AVL-Balance Variables


    public void updateHeight(AVLElement e){
        e.height=1+Math.max(getHeight(e.left),getHeight(e.right));
    }

    public int getHeight(AVLElement e){
        return e== null ? -1 : e.height;
    }

    public int balanceFactor(AVLElement e){
        if(e==null){
            return 0;
        }
        return getHeight(e.right)-getHeight(e.left);
    }



    //Rotations

    //Right Rotation
    public AVLElement rotateRight(AVLElement e){
        AVLElement x = e.left;
        AVLElement z = x.right;
        x.right=e;
      ;
        e.left=z;
        updateHeight(e);
        updateHeight(x);
        return x;
    }


    //Left Rotation
    public AVLElement rotateLeft(AVLElement e){
        AVLElement x = e.right;
        AVLElement z = x.left;
        x.left=e;
        e.right=z;
        updateHeight(e);
        updateHeight(x);
        return x;
    }

    //Rebalances the Tree at the Node e
    //This Method MUST be used after all changes on the tree
    //Specifically inserts and deletes
    public AVLElement rebalance(AVLElement e){
        updateHeight(e);
        int balance = balanceFactor(e);
        if(balance > 1){
            if(getHeight(e.right.right) > getHeight(e.right.left)){
                e = rotateLeft(e);
            }else{
                e.right=rotateRight(e.right);
                e = rotateLeft(e);
            }
        }else if (balance < -1){
            if(getHeight(e.left.left) > getHeight(e.left.right)) {
                e = rotateRight(e);
            } else{
                e.left=rotateLeft(e.left);
                e=rotateRight(e);
            }
        }
        return e;
    }


    //RMaxQ in an AVL tree
    //Returns the largest Element q with q  < @e
    //Starts at the node @start
    //This Code is based on the following implementation
    // https://stackoverflow.com/a/42859518
    public AVLElement RMaxQ(AVLElement start,AVLElement e,
                            AVLElement bestSoFar){
        //System.out.println("best  "+bestSoFar);
        if (start == null) return bestSoFar;
//        System.out.println();
       //System.out.println(start+"|"+start.left+"|"+start.right+"   | best "+bestSoFar);

        /*if(start.compareTo(e) >= 0){
            return RMaxQ(start.left,e,bestSoFar);
        }
        if(start.compareTo(e)<0){
            System.out.println("best  "+bestSoFar);
            return (bestSoFar.score > start.score && bestSoFar.weight < e.weight)?
                    RMaxQ(start.right,e,bestSoFar) :
                    RMaxQ(start.right,e,start);
        }*/

        //System.out.println(start+"|"+start.left+"|"+start.right+"|  best|"+bestSoFar);

        if(start.compareTo(e) == 0){
            return RMaxQ(start.left,e,bestSoFar);
        }
        if(start.compareTo(e) > 0 ){
            return RMaxQ(start.left,e,bestSoFar);
        }
        else{
            if(start.compareTo(e) < 0 && start.score > bestSoFar.score)
                bestSoFar=start;
            AVLElement left = RMaxQ(start.left,e,bestSoFar);

            AVLElement right = RMaxQ(start.right,e,bestSoFar);
            return left.score >right.score ? left : right;
        }
    }
}

class AVLElement implements Comparable<AVLElement> {

    int i; //position in Array
    int score; //Current HIS
    int weight; //ai in the Array
    AVLElement prec;



    int height;
    AVLElement left;
    AVLElement right;

    public AVLElement(int i,int weight){
        this.i=i;
        this.score=0;
        this.weight=weight;

    }

    @Override
    public int compareTo(AVLElement o) {
        if(weight != o.weight)
            return weight-o.weight;
        else
            return score-o.score;

    }

    public void setValues(AVLElement e){
        this.i=e.i;
        this.score=e.score;
        this.weight=e.weight;
    }

    public String toString(){
        return i+" "+score+" "+weight;

    }

    //Returns true, iff this Element has a lower weight, but bigger score than e


    public String getPrec(){
        String result =""+weight;
        AVLElement current = prec;
        while(prec!=null){
            result = prec.weight +" "+result;
            prec=prec.prec;
        }
        return result;
    }
}

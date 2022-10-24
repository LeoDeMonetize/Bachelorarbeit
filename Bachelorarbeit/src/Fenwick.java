import java.util.Arrays;



/*
Most of this implementation is based off
https://sites.google.com/site/indy256/algo/fenwick_tree
Author: Andrei Navumenka (Username: indy256)
*/



public class Fenwick {
}
class FenwickTree {

    // T[i] = max(T[i], value)
    public static void set(int[] t, int i, int value) {

        for (; i < t.length; i |= i + 1) {
            t[i] = Math.max(t[i], value);

        }
    }

    // max[0..i]
    public static int max(int[] t, int i) {
        int res = Integer.MIN_VALUE;
        for (; i >= 0; i = (i & (i + 1)) - 1)
            res = Math.max(res, t[i]);
        return res;
    }

    // Usage example
   static int HIS(int[] test){
       int[] rank = findRank(test);
       int[] maxbit = new int[test.length];
       int[] nodes = new int[test.length];
       for(int i =0; i< test.length;i++){
           int r = rank[i];
           int s = max(maxbit,r);
           set(maxbit,r,test[i]+s);
           nodes[i]=test[i]+s;
       }
       /*int max =max(maxbit,test.length-1);
        for(int i = test.length-1; i >=0;i--){
            if(nodes[i]==max){
                System.out.println(test[i]);
                max-=test[i];
            }
        }*/
       return max(maxbit,test.length-1);
   }

    //Using the following Implementation
    //https://stackoverflow.com/a/39375060
    private static int[] findRank(int[] x){
        int [] R = new int[x.length];
        if(x.length == 0)return R;
        Integer [] I = new Integer[x.length];
        for(int i = 0; i < x.length; i++)
            I[i] = i;
        Arrays.sort(I, (i0, i1) -> (int) Math.signum(x[i0]-x[i1]));
        int j = 0;
        for(int i = 0; i < x.length; i++){
            if(x[I[i]] != x[I[j]])
                j = i;
            R[I[i]] = j;
        }
        return R;
    }
}
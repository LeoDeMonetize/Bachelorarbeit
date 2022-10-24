import jdk.jshell.execution.Util;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

public class main {

    public static void main(String...args) throws Exception {
        long sumMine = 0;
        long sumStandard=0;
        double testcases=1;
        for(int j = 0;j<testcases;j++) {
            int x = 100000;
            int[] a = new int[x];
            for (int i = 0; i < x; i++) {
                a[i] = i;
            }
            for(int i = 0; i < x ; i++){
                int toSwap = (int)(Math.random()*x);
                int temp = a[i];
                a[i]=a[toSwap];
                a[toSwap]=temp;
            }


            /*for(int i  = 0 ; i < a.length ; i ++){
                System.out.print(a[i]+" ");
            }
            System.out.println();
*/
            long curTime = System.currentTimeMillis();
            curTime = System.currentTimeMillis();
            int his = FenwickTree.HIS(a);
            curTime= System.currentTimeMillis()-curTime;
            sumStandard+=curTime;
            System.out.println(curTime);

            curTime=System.currentTimeMillis();
            newNode result1= DynamicAlgorithm.HIS(a);
            curTime= System.currentTimeMillis()-curTime;
            sumMine+=curTime;








            if(result1.evalInt()!=his){
                System.out.print(result1.evalInt()+" "+his);
            }

            //sum+=curTime;
        }
        System.out.println(sumMine +" "+ sumStandard);

    }
}


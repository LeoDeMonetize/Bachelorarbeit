import java.util.Arrays;




public class DynamicAlgorithm {

    public static newNode HIS(int[] a) throws Exception {

        newNode[] nodes=new newNode[Arrays.stream(a).max().getAsInt()+1];
        LinkedList<Tuple<Integer>> L = new LinkedList<Tuple<Integer>>();
        for(int i = 0; i < a.length;i++){
            Element<Tuple<Integer>> compareTo = new Element<>(new Tuple<Integer>(a[i], 0));
            Element<Tuple<Integer>> prev = L.prev(compareTo);
            Tuple<Integer> sv = prev!=null ? prev.content : new Tuple<Integer>(0,0);

            if(sv.first>a[i])
                throw new Exception("Error! Monotony not satisfied");

            Element<Tuple<Integer>> next= (sv.equals(new Tuple<>(0,0))) ? null : prev.next;
            if(L.head!=null&&L.head.compareTo(compareTo)>=0)
                next=L.head;
            while(next != null){

                Tuple<Integer> tw = next.content;
                if(sv.second+a[i]<tw.second){
                    break;
                }
                L.delete(next);
                next=next.next;
            }

            L.insert(new Tuple<Integer>(a[i],sv.second+a[i]));
            nodes[a[i]] = new newNode(a[i],nodes[sv.first]);
        }

        newNode node = nodes[L.getTail().content.first];
        if(node.evalInt()!=L.tail.content.second) {
            throw new Exception("ERROR! HIS IS NOT RECONSTRUCTIBLE. PLEASE CORRECT CODE!");
        }
        return node;
    }


    public static newNode HISStandard(int[] a) throws Exception{
        newNode[] nodes=new newNode[Arrays.stream(a).max().getAsInt()+1];
        StandardLibraryList<Tuple<Integer>> sll = new StandardLibraryList<>();
        for(int i = 0; i < a.length;i++){
            Tuple<Integer> prev = sll.prev(new Tuple<Integer>(a[i],0));
            prev = prev==null ? new Tuple<Integer>(0,0) : prev;
            sll.add(new Tuple<Integer>(a[i],prev.second+a[i]));
            nodes[a[i]]=new newNode(a[i],nodes[prev.first]);
        }
        return nodes[sll.get(sll.size()-1).first];
    }
}

class newNode{
    int current;
    newNode next;

    public newNode(int current, newNode next){
        this.current=current;
        this.next=next;
    }

    public String evalString(){
        String output=current+" ";
        if(next==null)
            return output;
        else
            return next.evalString() + output;
    }

    public int evalInt(){
        if(next==null)
            return current;
        else
            return current+next.evalInt();
    }


}

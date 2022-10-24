import java.util.*;


public class StandardLibraryList<T extends DualArgument<T>> {

    ArrayList<T> list = new ArrayList();

    public StandardLibraryList(){

    }

    public void add(T newElement){
        if(list.size()==0){
            list.add(newElement);
            return;
        }
        for(int i = 0; i < list.size();i++){
            if(list.get(i).compareTo(newElement)<0){
                continue;
            }
            list.add(i,newElement);
            deleteSmallerInSecond(i);
            return;
        }
        list.add(list.size(),newElement);
    }

    public void deleteSmallerInSecond(int i){
        //i is the position of the just added Element
        if(i==list.size()-1)
            return;
        else{
            while(i+1 < list.size() && list.get(i).compareToSecond(list.get(i+1))<=0){
                list.remove(i+1);
            }
        }
    }

    public String toString(){
        String output="";
        for(T o : list){
            output+=o.toString();
        }
        return output;
    }

    public T get(int i){
        return list.get(i);
    }

    public int size(){
        return list.size();
    }

    public T prev(T o){
        if(size()==0)
            return null;
        for(int i = 0; i < list.size();i++){
            if(list.get(i).compareTo(o)<=0) {
                continue;
            }
            if(i == 0)
                return null;
            return list.get(i-1);
        }

        return list.get(size()-1);
    }

    public T next(T o){
        for(int i = 0; i < list.size();i++){
            if(list.get(i).compareTo(o)<0){
                continue;
            }
            return i+1 >= list.size() ? null : list.get(i+1);
        }
        return null;
    }

}

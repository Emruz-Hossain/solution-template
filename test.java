import java.util.*;
public class test{
    public static void main(String[] args) {
        Vector<Integer> V1 = new Vector<Integer>();
        Vector<Vector<Integer>> V2 = new Vector<Vector<Integer>>();
        V1.add(5);
        V2.add(V1);
        V1.clear();
        V1.add(6);
        V2.add(V1);
        for(int i=0;i<V2.size();i++)
            System.out.println(V2.get(i));
    }
}
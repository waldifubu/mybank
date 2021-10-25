package alittlexample;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class GenericMethods {

    public static <T> String random(T m, T n) {
        T t = Math.random() > 0.5 ? m : n;

        System.out.println(t);

        return t.toString();
    }

    public static void main(String[] args) {
        String s = random("Cheese", "Ham");
//        System.out.println(s);

        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");

        reverse(list);
    }


    public static void reverse(List<?> list) {
        ListIterator<?> fwd = list.listIterator();
        ListIterator<?> rev = list.listIterator(list.size() - 1);
        for (int i = 0, j = 100, mid = list.size() >> 1; i < mid; i++, j--) {

            //System.out.println(list.size() >> 1);

            Object tmp = fwd.next();
            System.out.println(tmp);
//            fwd.set(rev.previous());
        }
    }

}

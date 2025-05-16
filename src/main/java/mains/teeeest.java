package mains;

import model.Gate;
import model.Typee;

import java.util.ArrayList;
import java.util.List;

public class teeeest {
    public static void main(String[] args) {

        Gate g = new Gate(new Typee(1), true);

        List<Gate> list1 = new ArrayList<>();
        List<Gate> list2 = new ArrayList<>();

        list1.add(g);
        list2.add(g);
        System.out.println(list2.get(0).Is_outer());

        list1.get(0).setIs_outer(false);

        // حالا:
        System.out.println(list2.get(0).Is_outer()); // ➜ false
    }
}

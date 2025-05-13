package controller;

import mains.Filee;
import model.Gate;
import model.Sysbox;
import model.Typee;

import java.util.Arrays;

public class Add_level {
    public static void main(String[] args) {
        Gate gateout1 = new Gate(new Typee(1),true);
        Gate gateout2 = new Gate(new Typee(2),true);
        Gate gatein1 = new Gate(new Typee(1),false);
        Gate gatein2 = new Gate(new Typee(2),false);
        Filee.level_stack.sysboxes.add(new Sysbox(5,5,15,20));
        Filee.level_stack.sysboxes.getLast().inner_gates.addAll(Arrays.asList(gatein1, gatein2, gatein1));
        Filee.level_stack.sysboxes.getLast().outer_gates.addAll(Arrays.asList(gateout1, gateout1, gateout2));

        Filee.level_stack.sysboxes.add(new Sysbox(150,200));
        Filee.level_stack.sysboxes.getLast().inner_gates.addAll(Arrays.asList(gatein1, gatein2));
        Filee.level_stack.sysboxes.getLast().outer_gates.addAll(Arrays.asList(gateout1, gateout2));

        Filee.level_stack.sysboxes.add(new Sysbox(150,5));
        Filee.level_stack.sysboxes.getLast().inner_gates.addAll(Arrays.asList(gatein1, gatein1));
        Filee.level_stack.sysboxes.getLast().outer_gates.addAll(Arrays.asList(gateout2, gateout2));




    }
}

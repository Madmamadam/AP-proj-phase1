package controller;

import model.Gate;
import model.Signal;
import model.Sysbox;
import model.Typee;

import java.util.Arrays;

import static mains.Filee.level_stack;

public class Add_level {
    public static void start(){
        //level constraint
        level_stack.constraintss.setMaximum_length(555000);
        level_stack.constraintss.setMaximum_noise(5.0);


        //intial model
        Gate gateout1 = new Gate(new Typee(1),true);
        Gate gateout2 = new Gate(new Typee(2),true);
        Gate gatein1 = new Gate(new Typee(1),false);
        Gate gatein2 = new Gate(new Typee(2),false);

        Signal recSignal = new Signal(new Typee(1));
        Signal triSignal = new Signal(new Typee(2));


        level_stack.sysboxes.add(new Sysbox(50,250,150,400));
        level_stack.sysboxes.getLast().inner_gates.addAll(Arrays.asList(gatein1.cloneGate(), gatein2.cloneGate(), gatein1.cloneGate()));
        level_stack.sysboxes.getLast().outer_gates.addAll(Arrays.asList(gateout1.cloneGate(), gateout1.cloneGate(), gateout2.cloneGate()));

        level_stack.sysboxes.add(new Sysbox(450,400));
        level_stack.sysboxes.getLast().inner_gates.addAll(Arrays.asList(gatein1.cloneGate(), gatein2.cloneGate()));
        level_stack.sysboxes.getLast().outer_gates.addAll(Arrays.asList(gateout1.cloneGate(), gateout2.cloneGate()));
        level_stack.sysboxes.add(new Sysbox(450,145));
        level_stack.sysboxes.getLast().inner_gates.addAll(Arrays.asList(gatein1.cloneGate(), gatein1.cloneGate()));
        level_stack.sysboxes.getLast().outer_gates.addAll(Arrays.asList(gateout1.cloneGate(), gateout1.cloneGate()));

        level_stack.sysboxes.getFirst().setStarter(true);
        Sysbox starterSysbox = level_stack.sysboxes.getFirst();
        starterSysbox.setStarter(true);
        starterSysbox.signal_bank.addAll(Arrays.asList(recSignal.cloneSignal(),recSignal.cloneSignal(),triSignal.cloneSignal()));

        for(Sysbox sysbox:level_stack.sysboxes) {
            Inital_Load.GateAssign(sysbox);
        }
        level_stack.signals.addAll(starterSysbox.signal_bank);
    }


}

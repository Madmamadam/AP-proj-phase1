package controller;

import mains.Configg;
import model.*;

import java.util.Arrays;
import java.util.Collections;

import static mains.Filee.level_stack;
import static mains.MainGame.signal_run_frame_counter;

public class Add_level {
    public static void start(int level) {
        Configg cons =Configg.getInstance();
        if (level == 1) {
            //level constraint
            level_stack.constraintss.setMaximum_length(3000);
            level_stack.constraintss.setMaximum_noise(5.0);
            level_stack.constraintss.setMaximum_time_sec(40);
            level_stack.constraintss.setMaximum_dead_ratio(0.2);


            //intial model
            Gate gateout1 = new Gate(new Typee(1), true);
            Gate gateout2 = new Gate(new Typee(2), true);
            Gate gatein1 = new Gate(new Typee(1), false);
            Gate gatein2 = new Gate(new Typee(2), false);

            Signal recSignal = new Signal(new Typee(1));
            Signal triSignal = new Signal(new Typee(2));


            level_stack.sysboxes.add(new Sysbox(50, 250, 150, 400));
            level_stack.sysboxes.getLast().inner_gates.addAll(Arrays.asList(gatein1.cloneGate(), gatein2.cloneGate(), gatein1.cloneGate()));
            level_stack.sysboxes.getLast().outer_gates.addAll(Arrays.asList(gateout1.cloneGate(), gateout1.cloneGate(), gateout2.cloneGate()));

            level_stack.sysboxes.add(new Sysbox(450, 400));
            level_stack.sysboxes.getLast().inner_gates.addAll(Arrays.asList(gatein1.cloneGate(), gatein2.cloneGate()));
            level_stack.sysboxes.getLast().outer_gates.addAll(Arrays.asList(gateout1.cloneGate(), gateout2.cloneGate()));
            level_stack.sysboxes.add(new Sysbox(450, 145));
            level_stack.sysboxes.getLast().inner_gates.addAll(Arrays.asList(gatein1.cloneGate(), gatein1.cloneGate()));
            level_stack.sysboxes.getLast().outer_gates.addAll(Arrays.asList(gateout1.cloneGate(), gateout1.cloneGate()));

            level_stack.sysboxes.getFirst().setStarter(true);
            Sysbox starterSysbox = level_stack.sysboxes.getFirst();
            starterSysbox.setStarter(true);
            starterSysbox.signal_bank.addAll(Arrays.asList(recSignal.cloneSignal(), recSignal.cloneSignal(), triSignal.cloneSignal()));

            for (Sysbox sysbox : level_stack.sysboxes) {
                Inital_Load.GateAssign(sysbox);
                System.out.println("Add_level sysbox.signal_bank.size() " + sysbox.signal_bank.size());
            }
            //باید برای اونایی که بعدا اضافه میشن هم بکنیم اینکارو
            level_stack.signals.addAll(starterSysbox.signal_bank);

            level_stack.After_signals.add(new After_Frame_And_Signal_start(recSignal.cloneSignal(), 60 * 5));
            level_stack.After_signals.add(new After_Frame_And_Signal_start(recSignal.cloneSignal(), 60 * 2));
            level_stack.After_signals.add(new After_Frame_And_Signal_start(triSignal.cloneSignal(), 60 * 5));
        }
        if(level==2) {
            //level constraint
            level_stack.constraintss.setMaximum_length(10000);
            level_stack.constraintss.setMaximum_noise(5.0);
            level_stack.constraintss.setMaximum_time_sec(40);
            level_stack.constraintss.setMaximum_dead_ratio(0.2);


            //intial model
            Gate gateout1 = new Gate(new Typee(1), true);
            Gate gateout2 = new Gate(new Typee(2), true);
            Gate gatein1 = new Gate(new Typee(1), false);
            Gate gatein2 = new Gate(new Typee(2), false);

            Signal recSignal = new Signal(new Typee(1));
            Signal triSignal = new Signal(new Typee(2));


            level_stack.sysboxes.add(new Sysbox(50, 250, 150, 400));
            level_stack.sysboxes.getLast().inner_gates.addAll(Arrays.asList(gatein1.cloneGate(), gatein2.cloneGate(), gatein1.cloneGate()));
            level_stack.sysboxes.getLast().outer_gates.addAll(Arrays.asList(gateout1.cloneGate(), gateout1.cloneGate(), gateout2.cloneGate()));

            level_stack.sysboxes.add(new Sysbox(450, 400));
            level_stack.sysboxes.getLast().inner_gates.addAll(Arrays.asList(gatein1.cloneGate(), gatein2.cloneGate()));
            level_stack.sysboxes.getLast().outer_gates.addAll(Arrays.asList(gateout1.cloneGate(), gateout2.cloneGate()));

            level_stack.sysboxes.add(new Sysbox(450, 145));
            level_stack.sysboxes.getLast().inner_gates.addAll(Arrays.asList(gatein1.cloneGate(), gatein1.cloneGate()));
            level_stack.sysboxes.getLast().outer_gates.addAll(Arrays.asList(gateout1.cloneGate(), gateout1.cloneGate()));

            level_stack.sysboxes.add(new Sysbox(450, 700));
            level_stack.sysboxes.getLast().inner_gates.addAll(Arrays.asList(gatein1.cloneGate(), gatein2.cloneGate() , gatein1.cloneGate() , gatein2.cloneGate()));
            level_stack.sysboxes.getLast().outer_gates.addAll(Arrays.asList(gateout1.cloneGate(), gateout1.cloneGate() , gateout2.cloneGate() , gateout2.cloneGate()));

            level_stack.sysboxes.add(new Sysbox(650, 400, (int) cons.getSysbox_default_width(),20));
            level_stack.sysboxes.getLast().inner_gates.add(gatein1.cloneGate());
            level_stack.sysboxes.getLast().outer_gates.add(gateout1.cloneGate());



            level_stack.sysboxes.add(new Sysbox(850,145));
            level_stack.sysboxes.getLast().inner_gates.addAll(Arrays.asList(gatein1.cloneGate(), gatein2.cloneGate()));
            level_stack.sysboxes.getLast().outer_gates.addAll(Arrays.asList(gateout2.cloneGate(), gateout1.cloneGate()));


            level_stack.sysboxes.add(new Sysbox(850,400));
            level_stack.sysboxes.getLast().inner_gates.addAll(Arrays.asList(gatein1.cloneGate(), gatein2.cloneGate()));
            level_stack.sysboxes.getLast().outer_gates.addAll(Arrays.asList(gateout2.cloneGate(), gateout1.cloneGate()));


//            level_stack.sysboxes.add(new Sysbox(850, 700));








            level_stack.sysboxes.getFirst().setStarter(true);
            Sysbox starterSysbox = level_stack.sysboxes.getFirst();
            starterSysbox.setStarter(true);
            starterSysbox.signal_bank.addAll(Arrays.asList(recSignal.cloneSignal(), recSignal.cloneSignal(), triSignal.cloneSignal()));

            for (Sysbox sysbox : level_stack.sysboxes) {
                Inital_Load.GateAssign(sysbox);
                System.out.println("Add_level sysbox.signal_bank.size() " + sysbox.signal_bank.size());
            }
            level_stack.signals.addAll(starterSysbox.signal_bank);

            level_stack.After_signals.add(new After_Frame_And_Signal_start(recSignal.cloneSignal(), 60 * 5));
            level_stack.After_signals.add(new After_Frame_And_Signal_start(recSignal.cloneSignal(), 60 * 2));
            level_stack.After_signals.add(new After_Frame_And_Signal_start(triSignal.cloneSignal(), 60 * 5));
            level_stack.After_signals.add(new After_Frame_And_Signal_start(recSignal.cloneSignal(), 60 * 20));
        }

    }
}

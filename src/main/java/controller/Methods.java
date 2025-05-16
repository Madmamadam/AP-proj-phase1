package controller;

import model.Gate;
import model.Signal;
import model.Sysbox;
import model.Wire;

import java.util.Objects;

import static mains.Filee.level_stack;


public class Methods {
    //simple just find first better
    public Object recommended_gate(Sysbox sysbox, Signal signal) {
        boolean second_found = false;
        Gate secound_gate = null;
        for(Gate gate:sysbox.outer_gates){
            if (Objects.equals(gate.getTypee(),signal.getTypee()) && !gate.isIn_use()){
                return gate;
            }
            if(!gate.isIn_use() && !second_found){
                second_found = true;
                secound_gate = gate;
            }
        }
        if(second_found){
            return secound_gate;
        }
        return null;

    }
    public void update_signal_postion_onwire(Signal signal) {

    }
}

package controller;

import model.Gate;
import model.Signal;
import model.Sysbox;

import java.util.Objects;

public class In_Sysbox_Methods {
    //simple just find first better
    public Object recommended_gate(Sysbox sysbox, Signal signal) {
        for(Gate gate:sysbox.outer_gates){
            if (Objects.equals(gate.getType(),signal.getTypee()) && gate.equals()){
                return gate;
            }

        }

    }
}

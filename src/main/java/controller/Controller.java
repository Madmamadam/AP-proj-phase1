package controller;

import mains.Filee;
import model.Gate;
import model.Signal;
import model.Sysbox;

public class Controller {

    public void Signals_Update(){
        //it's after load signals stack

        //first: update signals that on sysbox  (Assign wire)
        for (Sysbox sysbox : Filee.level_stack.sysboxes) {
            Gate recommendedGate;
            boolean found = false;
            for(Gate gate:sysbox.outer_gates){

                    
            }
            sysbox.signal_bank.getFirst().setLinked_wire();

        }
        //second: update signals that on wire (move on wire or add them to a sysbox)
        for(Signal signal : Filee.level_stack.signals){

        }

    }


}

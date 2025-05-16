package controller;

import mains.Configg;
import model.Gate;
import model.Signal;
import model.Sysbox;

import static mains.Filee.level_stack;

public class Controller {

    public void Signals_Update(){
        Methods methods = new Methods();
        Configg cons = Configg.getInstance();
        //it's after load signals stack

        //first: update signals that on sysbox  (Assign wire)
        for (Sysbox sysbox : level_stack.sysboxes) {
            Signal signal=sysbox.signal_bank.getFirst();
            for (int i=0;i<cons.getBank_capacity() &&signal!=null;i++){
                if(methods.recommended_gate(sysbox,signal) != null){
                    Gate recom_gate = (Gate) methods.recommended_gate(sysbox,signal);
                    signal.setLinked_wire(recom_gate.getWire());
                    signal.setLength_on_wire(0.0);

                    signal.setIs_updated(true);
                    sysbox.signal_bank.removeFirst();
                }
                //if every gate is used
                else {
                    break;
                }
            }
        }
        //second: update signals that on wire (move on wire or add them to a sysbox)
        for(Signal signal : level_stack.signals){
            if(!signal.isIs_updated()){
                signal.setLength_on_wire(signal.getLength_on_wire()+cons.getDelta_wire_length());
            }
            else{signal.setIs_updated(false);}

        }

    }


}

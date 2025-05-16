package controller;

import mains.Filee;
import model.Signal;
import model.Sysbox;

import static mains.Filee.level_stack;

public class Controller {

    public void Signals_Update(){
        Methods methods = new Methods();
        //it's after load signals stack

        //first: update signals that on sysbox  (Assign wire)
        for (Sysbox sysbox : level_stack.sysboxes) {
            Signal signal=sysbox.signal_bank.getFirst();
            if(signal!=null){
                if(methods.recommended_gate(sysbox,signal) !=null){
                    //set gate or wire
//                    sysbox
                }
            }
        }
        //second: update signals that on wire (move on wire or add them to a sysbox)
        for(Signal signal : level_stack.signals){

        }

    }


}

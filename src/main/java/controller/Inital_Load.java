package controller;

import mains.Configg;
import mains.Filee;
import model.Signal;
import model.Sysbox;

public class Inital_Load {
    public static void GateAssign(Sysbox sysbox){
        Configg cons = Configg.getInstance();
        int amount=sysbox.inner_gates.size();
        if(amount==1){amount=2;}

        for (int i = 0; i < sysbox.inner_gates.size(); i++){
            sysbox.inner_gates.get(i).setSysbox(sysbox);
//            sysbox.inner_gates.get(i).setX(sysbox.getRectangle().getX()+sysbox.getRectangle().getWidth()/2);
//            sysbox.inner_gates.get(i).setY(sysbox.getRectangle().getY()+i*sysbox.getRectangle().getHeight()/(amount-1));
            sysbox.inner_gates.get(i).setX(sysbox.getRectangle().getX());
            sysbox.inner_gates.get(i).setY(sysbox.getRectangle().getY()+i*sysbox.getRectangle().getHeight()/(amount-1));

        }

        amount=sysbox.outer_gates.size();
        if(amount==1){amount=2;}

        for (int i = 0; i < sysbox.outer_gates.size(); i++){
            sysbox.outer_gates.get(i).setSysbox(sysbox);
            sysbox.outer_gates.get(i).setX(sysbox.getRectangle().getX()+sysbox.getRectangle().getWidth());
            sysbox.outer_gates.get(i).setY(sysbox.getRectangle().getY()+i*(sysbox.getRectangle().getHeight()-cons.getBorder())/(amount-1));
        }
    }


    public void Load_and_initial_Signal_Stack(){
        for (Sysbox sysbox :Filee.level_gamemodel.sysboxes){
            for (Signal signal : sysbox.signal_bank){

            }
        }
    }
}

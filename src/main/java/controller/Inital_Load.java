package controller;

import mains.Configg;
import mains.Filee;
import model.Sysbox;

public class Inital_Load {
    public void GateAssign(Sysbox sysbox){
        Configg cons = Configg.getInstance();
        int amount=sysbox.inner_gates.size();
        for (int i = 0; i < sysbox.inner_gates.size(); i++){
            sysbox.inner_gates.get(i).setX(sysbox.getRectangle().getX()+sysbox.getRectangle().getWidth()/2);
            sysbox.inner_gates.get(i).setY(sysbox.getRectangle().getY()+i*sysbox.getRectangle().getHeight()/(amount-1));
        }

        amount=sysbox.outer_gates.size();
        for (int i = 0; i < sysbox.inner_gates.size(); i++){
            sysbox.inner_gates.get(i).setX(sysbox.getRectangle().getX()-sysbox.getRectangle().getWidth()/2);
            sysbox.inner_gates.get(i).setY(sysbox.getRectangle().getY()-(sysbox.getRectangle().getHeight()-cons.getBorder())/2+i*(sysbox.getRectangle().getHeight()-cons.getBorder())/(amount-1));
        }
    }


    public void LoadSignal_Stack(){
        for (Sysbox sysbox :Filee.level_stack.sysboxes){

        }
    }
}

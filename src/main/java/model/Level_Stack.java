package model;

import java.util.ArrayList;

public class Level_Stack {
    public ArrayList<Sysbox> sysboxes = new ArrayList<>();
    private double level_wires_length ;
    public ArrayList<Signal> signals = new ArrayList<>();
    public ArrayList<Wire> wires = new ArrayList<>();
    public Constraintss constraintss = new Constraintss(); ;
    public ArrayList<Pairs> collapsedPairs = new ArrayList<>();
    public Boolean Oairyaman = false;
    public boolean Oatar = false;
    public boolean OAnahita =false;

    private double sekke;

    public Level_Stack() {
        this.level_wires_length=0;
    }

    public double getLevel_wires_length() {
        return level_wires_length;
    }

    public void setLevel_wires_length(double level_wires_length) {
        this.level_wires_length = level_wires_length;
    }

    public double getSekke() {
        return sekke;
    }

    public void setSekke(double sekke) {
        this.sekke = sekke;
    }

    public Level_Stack getClone() {
        Level_Stack clone_stack = new Level_Stack();
        clone_stack.sysboxes = new ArrayList<Sysbox>();
        clone_stack.level_wires_length = this.level_wires_length;
        clone_stack.sekke = this.sekke;
        clone_stack.collapsedPairs = new ArrayList<Pairs>();
        clone_stack.wires = this.wires;
        // anything in sysbox but signal_bank and gate state
        for(Sysbox sysbox : this.sysboxes) {
            clone_stack.sysboxes.add(sysbox.getclone());
        }




//      clone_stack.sysboxes = this.sysboxes;

//        for (int i = 0; i < this.sysboxes.size(); i++) {
//            clone_stack.sysboxes.get(i).signal_bank = new ArrayList<Signal>();
//            for(Signal signal: this.sysboxes.get(i).signal_bank) {
//                clone_stack.sysboxes.get(i).signal_bank.add(signal.cloneSignal());
//            }
//        }
//        clone_stack.signals.addAll(clone_stack.sysboxes.getFirst().signal_bank);
//
//        for(Sysbox sysbox : clone_stack.sysboxes) {
//            for(Gate gate:sysbox.inner_gates){
//                gate.setIn_use(false);
//            }
//            for (Gate gate : sysbox.outer_gates) {
//                gate.setIn_use(false);
//            }
//        }
        clone_stack.collapsedPairs=new ArrayList<Pairs>();
        clone_stack.constraintss = this.constraintss;
        clone_stack.Oairyaman = this.Oairyaman;
        clone_stack.Oatar = this.Oatar;
        clone_stack.OAnahita=this.OAnahita;
        return clone_stack;
    }
}

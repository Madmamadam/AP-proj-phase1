package model;

import javafx.scene.shape.Circle;

import java.util.ArrayList;

import static view.Paintt.HUD_wiring_update;

public class LevelGame_StaticDataModel {
    public Boolean stop_wiring = false;
    public ArrayList<Sysbox> sysboxes = new ArrayList<>();
    public ArrayList<After_Frame_And_Signal_start> After_signals = new ArrayList<>();
    public ArrayList<Circle> impulse_circles = new ArrayList<>();
    private double level_wires_length ;
    public ArrayList<Signal> signals = new ArrayList<>();
    public ArrayList<Wire> wires = new ArrayList<>();
    public Constraintss constraintss = new Constraintss(); ;
    public ArrayList<Pairs> collapsedPairs = new ArrayList<>();
    public Boolean Oairyaman = false;
    public boolean Oatar = false;
    public boolean OAnahita =false;

    private double sekke;

    public LevelGame_StaticDataModel() {
        this.level_wires_length=0;
    }

    public double getLevel_wires_length() {
        return level_wires_length;
    }

    public void setLevel_wires_length(double level_wires_length) {
        this.level_wires_length = level_wires_length;
        HUD_wiring_update();
    }

    public double getSekke() {
        return sekke;
    }

    public void setSekke(double sekke) {
        this.sekke = sekke;
    }

    public LevelGame_StaticDataModel getClone() {
        LevelGame_StaticDataModel clone_stack = new LevelGame_StaticDataModel();
        clone_stack.signals = new ArrayList<Signal>();
        clone_stack.sysboxes = new ArrayList<Sysbox>();
        clone_stack.level_wires_length = this.level_wires_length;
        clone_stack.sekke = this.sekke;
        clone_stack.collapsedPairs = new ArrayList<Pairs>();
        clone_stack.wires = new ArrayList<Wire>();
        clone_stack.constraintss = this.constraintss;
        clone_stack.Oairyaman = this.Oairyaman;
        clone_stack.Oatar = this.Oatar;
        clone_stack.OAnahita=this.OAnahita;

        // anything in sysbox but signal_bank and gate state
        for(Sysbox sysbox : this.sysboxes) {
            clone_stack.sysboxes.add(sysbox.getclone());
        }

        for(After_Frame_And_Signal_start d_signal : this.After_signals) {
            After_Frame_And_Signal_start d_signal_clone = new After_Frame_And_Signal_start(d_signal.signal.cloneSignal(),d_signal.adding_frame);
            clone_stack.After_signals.add(d_signal_clone);
        }



        clone_stack.signals.addAll(clone_stack.sysboxes.getFirst().signal_bank);


        //clone wires
        System.out.println("this.wires.size()= "+this.wires.size());
        for(Wire old_wire : this.wires) {

            Sysbox First_old_Sysbox = old_wire.getFirstgate().getSysbox();
            Sysbox Second_old_Sysbox = old_wire.getSecondgate().getSysbox();
            int outer_index=First_old_Sysbox.outer_gates.indexOf(old_wire.getFirstgate());
            int inner_index=Second_old_Sysbox.inner_gates.indexOf(old_wire.getSecondgate());

            int A1 =this.sysboxes.indexOf(First_old_Sysbox);
            int A2 =this.sysboxes.indexOf(Second_old_Sysbox);

            Sysbox First_new_Sysbox  =clone_stack.sysboxes.get(A1);
            Sysbox Second_new_Sysbox =clone_stack.sysboxes.get(A2);

            Gate outer_gate=First_new_Sysbox.outer_gates.get(outer_index);
            Gate inner_gate=Second_new_Sysbox.inner_gates.get(inner_index);

            Wire wire = new Wire(outer_gate,inner_gate);
            outer_gate.setWire(wire);
            inner_gate.setWire(wire);

            if(outer_index<0 || inner_index<0 || A1 <0 || A2 <0) {
                System.out.println("++++++++++++++ index problem");
            }
            else {
                System.out.println("index is ok");
            }



            clone_stack.wires.add(wire);

        }



        return clone_stack;
    }

}

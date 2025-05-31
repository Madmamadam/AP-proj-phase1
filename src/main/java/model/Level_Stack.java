package model;

import mains.Configg;

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
        Level_Stack clone = new Level_Stack();
        clone.level_wires_length = this.level_wires_length;
        clone.sekke = this.sekke;
        clone.collapsedPairs = new ArrayList<Pairs>();
        clone.wires = this.wires;
        for (Signal signal : this.signals) {
            clone.signals.add(signal.cloneSignal());
        }
        for (Sysbox sysbox : this.sysboxes) {
            clone.sysboxes.add(sysbox.getclone());
        }

        clone.collapsedPairs=new ArrayList<Pairs>();
        clone.constraintss = this.constraintss;
        clone.Oairyaman = this.Oairyaman;
        clone.Oatar = this.Oatar;
        clone.OAnahita=this.OAnahita;
        return clone;
    }
    //    private static Level_Stack level_instance;
//    public static Level_Stack getLevel_instance() {
//        if (level_instance == null) {
//            level_instance = new Level_Stack();
//        }
//        return level_instance;
//    }
}

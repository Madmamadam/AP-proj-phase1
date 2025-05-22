package model;

import mains.Configg;

import java.util.ArrayList;

public class Level_Stack {
    public ArrayList<Sysbox> sysboxes = new ArrayList<>();
    private double level_wires_length ;
    public ArrayList<Signal> signals = new ArrayList<>();
    public ArrayList<Wire> wires = new ArrayList<>();
    public Constraintss constraintss = new Constraintss(); ;

    public Level_Stack() {
        this.level_wires_length=0;
    }

    public double getLevel_wires_length() {
        return level_wires_length;
    }

    public void setLevel_wires_length(double level_wires_length) {
        this.level_wires_length = level_wires_length;
    }

    //    private static Level_Stack level_instance;
//    public static Level_Stack getLevel_instance() {
//        if (level_instance == null) {
//            level_instance = new Level_Stack();
//        }
//        return level_instance;
//    }
}

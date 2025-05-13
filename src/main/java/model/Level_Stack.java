package model;

import mains.Configg;

import java.util.ArrayList;

public class Level_Stack {
    public ArrayList<Sysbox> sysboxes;
    double level_wire_length;
    public ArrayList<Signal> signals;
    public ArrayList<Wire> wires;
    private Level_Stack() {}


//    private static Level_Stack level_instance;
//    public static Level_Stack getLevel_instance() {
//        if (level_instance == null) {
//            level_instance = new Level_Stack();
//        }
//        return level_instance;
//    }
}

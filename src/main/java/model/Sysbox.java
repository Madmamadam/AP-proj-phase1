package model;

import javafx.scene.shape.Polygon;

import java.util.ArrayList;

public class Sysbox {
    private int id;
    private Polygon polygon;
    private boolean indicator_state;
    private Polygon indicator_polygon;
    private ArrayList<Signal> signal_bank;
    private ArrayList<Gate> inner_gates;
    private ArrayList<Gate> outer_gates;



    public Sysbox() {}
}

package model;

import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import mains.Configg;

import java.io.ObjectInputFilter;
import java.util.ArrayList;

public class Sysbox {
    private int id;
    private Rectangle rectangle;
    private boolean indicator_state;
    private Rectangle indicator_rectangle;
    public ArrayList<Signal> signal_bank = new ArrayList<>();
    public ArrayList<Gate> inner_gates = new ArrayList<>(); ;
    public ArrayList<Gate> outer_gates = new ArrayList<>(); ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public boolean isIndicator_state() {
        return indicator_state;
    }

    public void setIndicator_state(boolean indicator_state) {
        this.indicator_state = indicator_state;
    }

    public Rectangle getIndicator_rectangle() {
        return indicator_rectangle;
    }

    public void setIndicator_rectangle(Rectangle indicator_rectangle) {
        this.indicator_rectangle = indicator_rectangle;
    }

    public Sysbox() {
        Configg cons=Configg.getInstance();
        this.rectangle = new Rectangle();
        this.rectangle.setFill(cons.getSysbox_color());
        this.indicator_rectangle.setFill(cons.getOff_indicator_color());
    }
    public Sysbox(int x,int y) {
        this();
        Configg cons=Configg.getInstance();
        this.rectangle = new Rectangle(x,y,cons.getIndicator_default_width(),cons.getIndicator_default_height());
    }
    public Sysbox(int x ,int y, int width, int height) {
        this();
        this.rectangle = new Rectangle(x, y, width, height);
    }


}

package model;

import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import mains.Configg;

import java.util.ArrayList;

public class Sysbox {
    private Rectangle rectangle;
    private boolean indicator_on_state;
    private Rectangle indicator_rectangle;
    public ArrayList<Signal> signal_bank = new ArrayList<>();
    public ArrayList<Gate> inner_gates = new ArrayList<>(); ;
    public ArrayList<Gate> outer_gates = new ArrayList<>(); ;
    private boolean starter;
    private boolean healthy =true;
    private String state;// 1.casual 2.data_spying 3.ddos_attacker 4.vpn
    private Label state_label=new Label();


    public Label getState_label() {
        return state_label;
    }

    public void setState_label(Label state_label) {
        this.state_label = state_label;
    }

    public String getState() {
        return state;
    }


    public boolean isHealthy() {
        return healthy;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public boolean isIndicator_on_state() {
        return indicator_on_state;
    }

    public void setIndicator_on_state(boolean indicator_on_state) {
        Configg cons = Configg.getInstance();
        this.indicator_on_state = indicator_on_state;
        indicator_rectangle.setFill(indicator_on_state ? cons.getOn_indicator_color() : cons.getOff_indicator_color());
    }

    public Rectangle getIndicator_rectangle() {
        return indicator_rectangle;
    }

    public void setIndicator_rectangle(Rectangle indicator_rectangle) {
        this.indicator_rectangle = indicator_rectangle;
    }

    public boolean isStarter() {
        return starter;
    }

    public void setStarter(boolean starter) {
        this.starter = starter;
    }

    public Sysbox() {
//        Configg cons=Configg.getInstance();
//        this.rectangle = new Rectangle();
//        this.indicator_rectangle=new Rectangle();
//        this.rectangle.setFill(cons.getSysbox_color());
//        this.indicator_rectangle.setFill(cons.getOff_indicator_color());
//        this.indicator_rectangle.setWidth(cons.getIndicator_default_width());
//        this.indicator_rectangle.setHeight(cons.getIndicator_default_height());
    }
    public Sysbox(int x,int y) {
        this.starter =false;
        Configg cons=Configg.getInstance();
        this.rectangle = new Rectangle(x,y,cons.getSysbox_default_width(),cons.getSysbox_default_height());
        this.rectangle.setFill(cons.getSysbox_color());
        this.indicator_rectangle=new Rectangle();
        this.indicator_rectangle.setFill(cons.getOff_indicator_color());
        this.indicator_rectangle.setWidth(cons.getIndicator_default_width());
        this.indicator_rectangle.setHeight(cons.getIndicator_default_height());
        this.indicator_rectangle.setX(this.rectangle.getX()+this.rectangle.getWidth()/2-this.indicator_rectangle.getWidth()/2);
        this.indicator_rectangle.setY(this.rectangle.getY()+cons.getIndicator_y_from_head());
        this.signal_bank=new ArrayList<>();

        state_label.setLayoutX(indicator_rectangle.getX()-10);
        state_label.setLayoutY(indicator_rectangle.getY()+20);

        state="casual";
        state_label.setText(state);
        state_label.setFont(new Font(10));

    }
    public Sysbox(int x , int y,String state){
        this(x,y);
        this.state=state;
        state_label.setText(state);

    }
    public Sysbox(int x ,int y, int width, int height) {
        Configg cons=Configg.getInstance();
        this.starter =false;
        this.rectangle = new Rectangle(x, y, width, height);
        this.rectangle.setFill(cons.getSysbox_color());
        this.indicator_rectangle=new Rectangle();
        this.indicator_rectangle.setFill(cons.getOff_indicator_color());
        this.indicator_rectangle.setWidth(cons.getIndicator_default_width());
        this.indicator_rectangle.setHeight(cons.getIndicator_default_height());
        this.indicator_rectangle.setX(this.rectangle.getX()+this.rectangle.getWidth()/2-this.indicator_rectangle.getWidth()/2);
        this.indicator_rectangle.setY(this.rectangle.getY()+cons.getIndicator_y_from_head());
        this.signal_bank=new ArrayList<>();

        state_label.setLayoutX(indicator_rectangle.getX()-10);
        state_label.setLayoutY(indicator_rectangle.getY()+20);

        state="casual";
        state_label.setText(state);
        state_label.setFont(new Font(10));
    }
    public Sysbox(int x ,int y, int width, int height, String state) {
        this(x, y, width, height);
        this.state=state;
        state_label.setText(state);
        state_label.setLayoutX(indicator_rectangle.getX());
        state_label.setLayoutY(indicator_rectangle.getY()+20);
    }

    public Sysbox getClone(){
        Sysbox clone = new Sysbox();
        for (Signal signal : signal_bank) {
            clone.signal_bank.add(signal.cloneSignal());
        }
        for (Gate oldgate : inner_gates) {
            Gate gate = oldgate.cloneGate();
            clone.inner_gates.add(gate);
            gate.setSysbox(clone);
            gate.setIn_use(false);

        }
        for (Gate oldgate : outer_gates) {
            Gate gate = oldgate.cloneGate();
            clone.outer_gates.add(gate);
            gate.setSysbox(clone);
            gate.setIn_use(false);
        }
        clone.rectangle=this.rectangle;
        clone.indicator_on_state=this.indicator_on_state;
        clone.indicator_rectangle=this.indicator_rectangle;
        clone.state=this.state;
        clone.state_label=this.state_label;

        clone.starter=this.starter;
        return clone;

    }

}

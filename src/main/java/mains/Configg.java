package mains;

import javafx.scene.paint.Color;

public class Configg {
    private int bank_capacity=5;
    private double default_delta_wire_length =0.5;

    private double virtual_frequency=60*200;

    private double noise_add_every_hit=1;


    private double impulse_radius=250;
    private Color impulse_color=Color.rgb(236, 193, 20, 0.7);
    private double impulse_show_time=0.5;
    private double impulse_resttime=0.5;

    private double impulse_delta_r=5;
    private double impulse_move_time=1.0;



    private double health_bar_width=4;
    private double health_bar_back_length=250;
    private Color health_bar_back_color=Color.GRAY;
    private Color health_bar_show_color=Color.GREEN;

    private double sysbox_default_width=40;
    private double sysbox_default_height=100;
    private double indicator_default_width=12;
    private double indicator_default_height=4;
    private double indicator_y_from_head=10;
    private double border=0;  // باگ داره
    private Color off_indicator_color=Color.RED;
    private Color on_indicator_color=Color.BLUE;
    private Color sysbox_color=Color.GRAY;


    private double gate_rectangle_width=15;
    private double gate_rectangle_height=15;
    private double gate_triangle_radius =12;
    private Color gate_rectangle_color=Color.BROWN;
    private Color gate_triangle_color=Color.BROWN;

    private double signal_rectangle_width=gate_rectangle_width;
    private double signal_rectangle_height=gate_rectangle_height;
    private double signal_triangle_radius =gate_triangle_radius;
    private Color signal_rectangle_color=Color.BLUE;
    private Color signal_triangle_color=Color.BLUE;



    private Color line_color=Color.GREEN;
    private Color wrong_line_color=Color.RED;
    private double seeing_wrong_line_duration=1;
    private double line_width=4;

    private double rectangle_signal_sekke_added =1;
    private double traiangle_signal_sekke_added =2;




    public int getBank_capacity() {
        return bank_capacity;
    }

    public double getDefault_delta_wire_length() {
        return default_delta_wire_length;
    }

    public Color getSysbox_color() {
        return sysbox_color;
    }

    public void setSysbox_color(Color sysbox_color) {
        this.sysbox_color = sysbox_color;
    }

    public Color getOn_indicator_color() {
        return on_indicator_color;
    }

    public void setOn_indicator_color(Color on_indicator_color) {
        this.on_indicator_color = on_indicator_color;
    }

    public Color getOff_indicator_color() {
        return off_indicator_color;
    }

    public void setOff_indicator_color(Color off_indicator_color) {
        this.off_indicator_color = off_indicator_color;
    }

    public double getIndicator_y_from_head() {
        return indicator_y_from_head;
    }

    public void setIndicator_y_from_head(double indicator_y_from_head) {
        this.indicator_y_from_head = indicator_y_from_head;
    }

    public double getIndicator_default_height() {
        return indicator_default_height;
    }

    public void setIndicator_default_height(double indicator_default_height) {
        this.indicator_default_height = indicator_default_height;
    }

    public double getIndicator_default_width() {
        return indicator_default_width;
    }

    public void setIndicator_default_width(double indicator_default_width) {
        this.indicator_default_width = indicator_default_width;
    }

    public double getSysbox_default_height() {
        return sysbox_default_height;
    }

    public void setSysbox_default_height(double sysbox_default_height) {
        this.sysbox_default_height = sysbox_default_height;
    }

    public double getSysbox_default_width() {
        return sysbox_default_width;
    }

    public void setSysbox_default_width(double sysbox_default_width) {
        this.sysbox_default_width = sysbox_default_width;
    }

    public double getBorder() {
        return border;
    }

    public void setBorder(double border) {
        this.border = border;
    }

    public double getGate_rectangle_width() {
        return gate_rectangle_width;
    }

    public void setGate_rectangle_width(double gate_rectangle_width) {
        this.gate_rectangle_width = gate_rectangle_width;
    }

    public double getGate_rectangle_height() {
        return gate_rectangle_height;
    }

    public void setGate_rectangle_height(double gate_rectangle_height) {
        this.gate_rectangle_height = gate_rectangle_height;
    }

    public double getGate_triangle_radius() {
        return gate_triangle_radius;
    }

    public void setGate_triangle_radius(double gate_triangle_radius) {
        this.gate_triangle_radius = gate_triangle_radius;
    }

    public Color getGate_rectangle_color() {
        return gate_rectangle_color;
    }

    public void setGate_rectangle_color(Color gate_rectangle_color) {
        this.gate_rectangle_color = gate_rectangle_color;
    }

    public Color getGate_triangle_color() {
        return gate_triangle_color;
    }

    public void setGate_triangle_color(Color gate_triangle_color) {
        this.gate_triangle_color = gate_triangle_color;
    }

    public double getSignal_rectangle_width() {
        return signal_rectangle_width;
    }

    public double getSignal_rectangle_height() {
        return signal_rectangle_height;
    }

    public double getSignal_triangle_radius() {
        return signal_triangle_radius;
    }

    public double getLine_width() {
        return line_width;
    }

    public void setLine_width(double line_width) {
        this.line_width = line_width;
    }

    public Color getLine_color() {
        return line_color;
    }

    public void setLine_color(Color line_color) {
        this.line_color = line_color;
    }

    public Color getWrong_line_color() {
        return wrong_line_color;
    }

    public void setWrong_line_color(Color wrong_line_color) {
        this.wrong_line_color = wrong_line_color;
    }

    public double getSeeing_wrong_line_duration() {
        return seeing_wrong_line_duration;
    }

    public Color getSignal_rectangle_color() {
        return signal_rectangle_color;
    }

    public Color getSignal_triangle_color() {
        return signal_triangle_color;
    }

    public double getRectangle_signal_sekke_added() {
        return rectangle_signal_sekke_added;
    }

    public double getTraiangle_signal_sekke_added() {
        return traiangle_signal_sekke_added;
    }

    public double getImpulse_radius() {
        return impulse_radius;
    }

    public Color getImpulse_color() {
        return impulse_color;
    }

    public double getImpulse_show_time() {
        return impulse_show_time;
    }

    public double getImpulse_resttime() {
        return impulse_resttime;
    }

    public double getImpulse_move_time() {
        return impulse_move_time;
    }

    public double getImpulse_delta_r() {
        return impulse_delta_r;
    }

    public double getNoise_add_every_hit() {
        return noise_add_every_hit;
    }
    public double getVirtual_frequency(){
        return virtual_frequency;
    }

    public double getHealth_bar_width() {
        return health_bar_width;
    }

    public Color getHealth_bar_back_color() {
        return health_bar_back_color;
    }

    public Color getHealth_bar_show_color() {
        return health_bar_show_color;
    }

    public double getHealth_bar_back_length() {
        return health_bar_back_length;
    }

    //
//  constructors
    //
    private Configg(){}

    private static Configg instance;
    public static Configg getInstance() {
        if (instance == null) {
            instance = new Configg();
        }
        return instance;
    }
}

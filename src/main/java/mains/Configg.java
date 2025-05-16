package mains;

import javafx.scene.paint.Color;

public class Configg {
    double sysbox_default_width=40;
    double sysbox_default_height=100;
    double indicator_default_width=4;
    double indicator_default_height=2;
    double indicator_y_from_head=5;
    double border=0.2;

    double gate_rectangle_width=10;
    double gate_rectangle_height=10;
    double gate_triangle_radius =10;
    Color gate_rectangle_color=Color.BROWN;
    Color gate_triangle_color=Color.BROWN;

    Color off_indicator_color=Color.GREEN;
    Color on_indicator_color=Color.BLUE;
    Color sysbox_color=Color.PURPLE;

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

    private Configg(){}
    private static Configg instance;
    public static Configg getInstance() {
        if (instance == null) {
            instance = new Configg();
        }
        return instance;
    }
}

package mains;

import javafx.scene.paint.Color;

public class Configg {
    double sysbox_default_width=20;
    double sysbox_default_height=50;
    double indicator_default_width=4;
    double indicator_default_height=2;
    double indicator_y_from_head=5;
    Color off_indicator_color=Color.BLACK;
    Color on_indicator_color=Color.BLUE;
    Color sysbox_color=Color.WHITE;
    double border=2;

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

    private static Configg instance;
    public static Configg getInstance() {
        if (instance == null) {
            instance = new Configg();
        }
        return instance;
    }
}

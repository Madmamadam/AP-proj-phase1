package model;

public class Pairs {
    public Signal signal1;
    public Signal signal2;
    public int adding_frame;

    public Pairs(Signal signal1, Signal signal2 , int adding_frame) {
        this.signal1 = signal1;
        this.signal2 = signal2;
        this.adding_frame = adding_frame;
    }
}

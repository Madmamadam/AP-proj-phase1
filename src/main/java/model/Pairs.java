package model;

public class Pairs {
    public Signal signal1;
    public Signal signal2;
    public Double adding_time ;
    public Pairs(Signal signal1, Signal signal2) {
        this.signal1 = signal1;
        this.signal2 = signal2;
        long adding_time_long =  System.nanoTime();
        this.adding_time = adding_time_long/1000000000.0;
    }
}

package model;

public class Wire {
    private int wire_id;
    Gate decoy_firstgate;
    Gate decoy_secondgate;

    public int getWire_id() {
        return wire_id;
    }

    public void setWire_id(int wire_id) {
        this.wire_id = wire_id;
    }
}

package model;

public class Wire {
    Gate decoy_firstgate;
    Gate decoy_secondgate;

    public Gate getDecoy_firstgate() {
        return decoy_firstgate;
    }

    public void setDecoy_firstgate(Gate decoy_firstgate) {
        this.decoy_firstgate = decoy_firstgate;
    }

    public Gate getDecoy_secondgate() {
        return decoy_secondgate;
    }

    public void setDecoy_secondgate(Gate decoy_secondgate) {
        this.decoy_secondgate = decoy_secondgate;
    }
}

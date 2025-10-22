package model;

import java.util.Objects;

public class EventHappenWithFrame {
    Object objectEffectedEvent;
    int frame;

    public EventHappenWithFrame(Object object, int signalRunFrameCounter) {
        frame = signalRunFrameCounter;
        objectEffectedEvent = object;
    }
}

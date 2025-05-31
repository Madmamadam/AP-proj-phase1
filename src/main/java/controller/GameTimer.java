package controller;

import javafx.animation.AnimationTimer;

public class GameTimer {
    private boolean stopping = true;
    private double time_sec = 0.0; // تایم در ثانیه
    private long lastUpdate = 0;

    private final AnimationTimer timer;

    public GameTimer() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!stopping) {
                    if (lastUpdate > 0) {
                        long elapsedNanos = now - lastUpdate;
                        time_sec += elapsedNanos / 1_000_000_000.0;
                    }
                    lastUpdate = now;
                } else {
                    lastUpdate = 0; // صفر کن که در زمان Resume پرش نداشته باشیم
                }
            }
        };
        timer.start();
    }

    public void setStopping(boolean stop) {
        this.stopping = stop;
    }

    public double getTime_sec() {
        return time_sec;
    }
    public void setTime_sec(double time_sec) {
        this.lastUpdate=0;
        this.time_sec = time_sec;
    }

    public void restart() {
        this.time_sec = 0;
        this.lastUpdate = 0;
    }
}

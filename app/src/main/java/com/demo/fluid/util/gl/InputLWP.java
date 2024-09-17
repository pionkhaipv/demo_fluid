package com.demo.fluid.util.gl;

import com.magicfluids.MotionEventWrapper;

public final class InputLWP {
    private MotionEventWrapper[] Events = new MotionEventWrapper[1024];
    private int NumEvents;

    public InputLWP() {
        for (int i = 0; i < 1024; i++) {
            this.Events[i] = new MotionEventWrapper();
        }
        this.NumEvents = 0;
    }

    public MotionEventWrapper[] getEvents() {
        return this.Events;
    }

    public void setEvents(MotionEventWrapper[] motionEventWrapperArr) {
        this.Events = motionEventWrapperArr;
    }

    public int getNumEvents() {
        return this.NumEvents;
    }

    public void setNumEvents(int i) {
        this.NumEvents = i;
    }
}

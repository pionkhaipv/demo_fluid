package com.demo.fluid.gl;

import com.magicfluids.MotionEventWrapper;

public final class Input {
    public MotionEventWrapper[] Events = new MotionEventWrapper[1024];
    public int NumEvents;

    public Input() {
        for (int i = 0; i < 1024; i++) {
            this.Events[i] = new MotionEventWrapper();
        }
        this.NumEvents = 0;
    }
}

package com.magicfluids;

public class MotionEventWrapper {
    public static final int EVENT_DOWN = 0;
    public static final int EVENT_MOVE = 2;
    public static final int EVENT_POINTER_DOWN = 5;
    public static final int EVENT_POINTER_UP = 6;
    public static final int EVENT_UP = 1;
    public int ID;
    public float PosX;
    public float PosY;
    public int Type;

    public void set(MotionEventWrapper motionEventWrapper) {
        this.Type = motionEventWrapper.Type;
        this.PosX = motionEventWrapper.PosX;
        this.PosY = motionEventWrapper.PosY;
        this.ID = motionEventWrapper.ID;
    }
}

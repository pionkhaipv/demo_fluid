package com.demo.fluid.gl;

import android.view.MotionEvent;
import com.magicfluids.MotionEventWrapper;
import java.util.concurrent.ArrayBlockingQueue;
import kotlin.jvm.internal.DefaultConstructorMarker;

public final class InputBufferLWP {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static InputBufferLWP Instance = new InputBufferLWP();
    public static final int MAX_EVENTS = 1024;
    private ArrayBlockingQueue<MotionEventWrapper> eventPool;
    private ArrayBlockingQueue<MotionEventWrapper> newEvents;

    public InputBufferLWP() {
        init();
    }

    public void init() {
        this.eventPool = new ArrayBlockingQueue<>(1024);
        this.newEvents = new ArrayBlockingQueue<>(1024);
        for (int i = 0; i < 1024; i++) {
            ArrayBlockingQueue<MotionEventWrapper> arrayBlockingQueue = this.eventPool;
            arrayBlockingQueue.add(new MotionEventWrapper());
        }
    }

    private boolean addNewEvent(int i, int i2, float f, float f2) {
        ArrayBlockingQueue<MotionEventWrapper> arrayBlockingQueue = this.eventPool;
        MotionEventWrapper poll = arrayBlockingQueue.poll();
        if (poll == null) {
            return false;
        }
        poll.Type = i;
        poll.PosX = f;
        poll.PosY = f2;
        poll.ID = i2;
        ArrayBlockingQueue<MotionEventWrapper> arrayBlockingQueue2 = this.newEvents;
        arrayBlockingQueue2.add(poll);
        return true;
    }

    public void addEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        int i = 0;
        if (actionMasked == 0) {
            int i2 = 0;
            while (i2 < motionEvent.getPointerCount() && addNewEvent(0, motionEvent.getPointerId(i2), motionEvent.getX(i2), motionEvent.getY(i2))) {
                i2++;
            }
        } else if (actionMasked == 1 || actionMasked == 3) {
            while (i < motionEvent.getPointerCount() && addNewEvent(1, motionEvent.getPointerId(i), motionEvent.getX(i), motionEvent.getY(i))) {
                i++;
            }
        } else {
            if (actionMasked == 5) {
                int actionIndex = motionEvent.getActionIndex();
                if (!addNewEvent(5, motionEvent.getPointerId(actionIndex), motionEvent.getX(actionIndex), motionEvent.getY(actionIndex))) {
                    return;
                }
            }
            if (actionMasked == 6) {
                int actionIndex2 = motionEvent.getActionIndex();
                if (!addNewEvent(6, motionEvent.getPointerId(actionIndex2), motionEvent.getX(actionIndex2), motionEvent.getY(actionIndex2))) {
                    return;
                }
            }
            if (actionMasked == 2) {
                while (i < motionEvent.getPointerCount() && addNewEvent(2, motionEvent.getPointerId(i), motionEvent.getX(i), motionEvent.getY(i))) {
                    i++;
                }
            }
        }
    }

    public void getCurrentInputState(InputLWP inputLWP) {
        inputLWP.setNumEvents(0);
        ArrayBlockingQueue<MotionEventWrapper> arrayBlockingQueue = this.newEvents;
        int size = arrayBlockingQueue.size();
        while (inputLWP.getNumEvents() < size) {
            ArrayBlockingQueue<MotionEventWrapper> arrayBlockingQueue2 = this.newEvents;
            MotionEventWrapper poll = arrayBlockingQueue2.poll();
            if (poll != null) {
                MotionEventWrapper[] events = inputLWP.getEvents();
                int numEvents = inputLWP.getNumEvents();
                inputLWP.setNumEvents(numEvents + 1);
                MotionEventWrapper motionEventWrapper = events[numEvents];
                motionEventWrapper.set(poll);
                ArrayBlockingQueue<MotionEventWrapper> arrayBlockingQueue3 = this.eventPool;
                arrayBlockingQueue3.add(poll);
            }
        }
    }

    public static final class Companion {
        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}

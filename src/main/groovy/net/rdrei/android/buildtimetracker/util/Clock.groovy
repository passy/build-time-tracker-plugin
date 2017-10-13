package net.rdrei.android.buildtimetracker.util

class Clock {
    long startTimeInMs

    Clock() {
        Clock(System.currentTimeMillis())
    }

    Clock(long startTimeInMs) {
        this.startTimeInMs = startTimeInMs
    }

    long getTimeInMs() {
        return System.currentTimeMillis() - startTimeInMs
    }
}

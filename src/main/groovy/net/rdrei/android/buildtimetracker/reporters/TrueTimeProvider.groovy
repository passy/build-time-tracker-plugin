package net.rdrei.android.buildtimetracker.reporters;

public class TrueTimeProvider {
    public long getCurrentTime() {
        return System.currentTimeMillis();
    }
}
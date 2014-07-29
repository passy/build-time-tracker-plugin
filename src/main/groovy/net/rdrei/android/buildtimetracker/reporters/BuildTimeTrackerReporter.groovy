package net.rdrei.android.buildtimetracker.reporters

import net.rdrei.android.buildtimetracker.Timing

interface BuildTimeTrackerReporter {
    public run(List<Timing> timings)
}

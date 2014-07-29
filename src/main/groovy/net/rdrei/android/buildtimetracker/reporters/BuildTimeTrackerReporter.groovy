package net.rdrei.android.buildtimetracker.reporters

import net.rdrei.android.buildtimetracker.internal.Timing

interface BuildTimeTrackerReporter {
    public run(List<Timing> timings)
}

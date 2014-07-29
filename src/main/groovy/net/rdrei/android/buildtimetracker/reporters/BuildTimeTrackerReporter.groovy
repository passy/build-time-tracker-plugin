package net.rdrei.android.buildtimetracker.reporters

import net.rdrei.android.buildtimetracker.Timing

abstract class BuildTimeTrackerReporter {
    BuildTimeTrackerReporter(Map<String, String> options) {
    }

    abstract public run(List<Timing> timings)
}

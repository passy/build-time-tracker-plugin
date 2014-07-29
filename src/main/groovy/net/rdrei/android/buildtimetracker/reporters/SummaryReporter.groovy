package net.rdrei.android.buildtimetracker.reporters

import net.rdrei.android.buildtimetracker.internal.Timing

class SummaryReporter implements BuildTimeTrackerReporter {
    @Override
    def run(List<Timing> timings) {
        def total = 0
        println "Task timings:"
        for (timing in timings) {
            if (timing.ms >= 50) {
                printf "%7sms  %s\n", timing.ms, timing.path
            }
            total += timing.ms
        }

        printf "Total time wasted: %7sms\n", total
    }
}

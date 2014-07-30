package net.rdrei.android.buildtimetracker.reporters

import net.rdrei.android.buildtimetracker.Timing

class CSVSummaryReporter extends AbstractBuildTimeTrackerReporter {
    CSVSummaryReporter(Map<String, String> options) {
        super(options)
    }

    @Override
    def run(List<Timing> timings) {
    }
}

package net.rdrei.android.buildtimetracker.reporters

import net.rdrei.android.buildtimetracker.Timing

abstract class AbstractBuildTimeTrackerReporter {
    Map<String, String> options

    AbstractBuildTimeTrackerReporter(Map<String, String> options) {
        this.options = options
    }

    public abstract run(long start, List<Timing> timings)

    public String getOption(String name, String defaultVal) {
        options[name] == null ? defaultVal : options[name]
    }
}

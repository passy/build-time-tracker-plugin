package net.rdrei.android.buildtimetracker.reporters

import net.rdrei.android.buildtimetracker.Timing
import java.util.concurrent.TimeUnit
import jline.TerminalFactory

class SummaryReporter extends AbstractBuildTimeTrackerReporter {
    def SQUARE = "▇"
    def FILL = " "

    SummaryReporter(Map<String, String> options) {
        super(options)
    }

    @Override
    def run(List<Timing> timings) {
        def threshold = getOption("threshold", "50").toInteger()

        if (getOption("ordered", "false").toBoolean()) {
            timings = timings.sort(false, { it.ms })
        }

        println "== Build Time Summary =="
        formatTable(timings, threshold)
    }

    def formatTable(List<Timing> timings, int threshold) {
        def total = timings.sum { t -> t.ms }
        def longestTaskName = timings.inject(0, { acc, val -> Math.max(acc, val.path.length()) })
        def longestTiming = timings.inject(0, { acc, val -> Math.max(acc, val.ms )})
        def maxColumns = TerminalFactory.get().width
        if (maxColumns == null) maxColumns = 80

        def maxBarWidth
        if (longestTaskName > maxColumns / 2) {
            maxBarWidth = (maxColumns - 20) / 2
        } else {
            maxBarWidth = maxColumns - (longestTaskName + 20)
        }

        def longestBar = maxBarWidth * (longestTiming / total)

        for (timing in timings) {
            if (timing.ms >= threshold) {
                printf("%s %s (%s)\n",
                        createBar(timing.ms / total, longestBar),
                        shortenTaskName(timing.path, maxBarWidth),
                        formatDuration(timing.ms))
            }
        }
    }

    def static shortenTaskName(String taskName, def max) {
        if (taskName.length() < max) { return taskName }

        int partLength = Math.floor((max - 3) / 2) as int
        def start = taskName.substring(0, partLength + 1)
        def end = taskName.substring(taskName.length() - partLength)

        start.trim() + '…' + end.trim()
    }

    def createBar(def frac, def max) {
        def rounded = Math.round(frac * 100)

        def barLength = Math.ceil(max * frac)
        def bar = FILL * (max - barLength) + SQUARE * barLength
        def formatted = rounded < 10 ? " " + rounded : rounded;

        return bar + ' ' + formatted + '%'
    }

    String formatDuration(long ms) {
        def minutes = TimeUnit.MILLISECONDS.toMinutes(ms)
        def seconds = TimeUnit.MILLISECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(minutes)
        def millis = ms - TimeUnit.MINUTES.toMillis(minutes) - TimeUnit.SECONDS.toMillis(seconds)
        String.format("%d:%02d.%03d",
                minutes,
                seconds,
                millis
        );
    }
}

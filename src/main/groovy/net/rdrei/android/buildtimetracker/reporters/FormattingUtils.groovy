package net.rdrei.android.buildtimetracker.reporters

import java.util.concurrent.TimeUnit

class FormattingUtils {
    private FormattingUtils() {}

    static String formatDuration(long ms) {
        def hours = TimeUnit.MILLISECONDS.toHours(ms)
        def minutes = TimeUnit.MILLISECONDS.toMinutes(ms) - TimeUnit.HOURS.toMinutes(hours)
        def seconds = TimeUnit.MILLISECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.HOURS.toSeconds(hours)
        def millis = ms - TimeUnit.MINUTES.toMillis(minutes) - TimeUnit.SECONDS.toMillis(seconds) - TimeUnit.HOURS.toMillis(hours)
        if (hours > 0) {
            String.format("%d:%02d:%02d",
                    hours,
                    minutes,
                    seconds
            )
        } else {
            String.format("%d:%02d.%03d",
                    minutes,
                    seconds,
                    millis
            )
        }
    }
}

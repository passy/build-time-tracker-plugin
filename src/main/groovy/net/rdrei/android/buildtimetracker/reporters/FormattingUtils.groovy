package net.rdrei.android.buildtimetracker.reporters

import java.util.concurrent.TimeUnit

class FormattingUtils {
    private FormattingUtils() {}

    static String formatDuration(long ms) {
        def minutes = TimeUnit.MILLISECONDS.toMinutes(ms)
        def seconds = TimeUnit.MILLISECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(minutes)
        def millis = ms - TimeUnit.MINUTES.toMillis(minutes) - TimeUnit.SECONDS.toMillis(seconds)
        String.format("%d:%02d.%03d",
                minutes,
                seconds,
                millis
        )
    }
}

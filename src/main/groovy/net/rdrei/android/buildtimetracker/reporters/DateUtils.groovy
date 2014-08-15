package net.rdrei.android.buildtimetracker.reporters;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class DateUtils {
    public DateUtils() {}

    long getMidnightTimestamp() {
        DateTime.now(DateTimeZone.UTC).withTime(0, 0, 0, 0).getMillis()
    }
}

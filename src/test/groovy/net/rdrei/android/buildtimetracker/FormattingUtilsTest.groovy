package net.rdrei.android.buildtimetracker

import net.rdrei.android.buildtimetracker.reporters.FormattingUtils
import org.junit.Test

import static org.junit.Assert.*

class FormattingUtilsTest {
    @Test
    void testMinutesFormatting() {
        assertEquals "1:57.006", FormattingUtils.formatDuration(117006)
    }

    @Test
    void testHoursFormatting() {
        assertEquals "13:37:03.001", FormattingUtils.formatDuration(49023001)
    }

    @Test
    void testHoursZeroFill() {
        assertEquals "3:07:03.123", FormattingUtils.formatDuration(11223123)
    }
}

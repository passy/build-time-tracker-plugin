package net.rdrei.android.buildtimetracker.reporters

import au.com.bytecode.opencsv.CSVReader
import groovy.mock.interceptor.MockFor
import net.rdrei.android.buildtimetracker.Timing
import org.gradle.api.logging.Logger
import org.gradle.internal.TrueTimeProvider
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import static org.junit.Assert.*

class CSVSummaryReporterTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    Logger mockLogger = new MockFor(Logger).proxyInstance()

    @Test
    void testThrowsErrorWithoutCSV() {
        def reporter = new CSVSummaryReporter([:], mockLogger)
        def err
        try {
            reporter.run([])
        } catch (ReporterConfigurationError e) {
            err = e
        }

        assertNotNull err
        assertEquals err.errorType, ReporterConfigurationError.ErrorType.REQUIRED
        assertEquals err.optionName, "csv"
    }

    @Test
    void testThrowsErrorWithInvalidFile() {
        def reporter = new CSVSummaryReporter([csv: "/invalid/file"], mockLogger)
        def err

        try {
            reporter.run([])
        } catch (ReporterConfigurationError e) {
            err = e
        }

        assertNotNull err
        assertEquals err.errorType, ReporterConfigurationError.ErrorType.INVALID
        assertEquals err.optionName, "csv"
    }
}

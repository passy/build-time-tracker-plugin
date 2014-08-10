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

    def getFixture(String name) {
        new File(getClass().getClassLoader().getResource(name).getPath())
    }

    @Test
    void testThrowsErrorWithoutCSV() {
        Logger logger = new MockFor(Logger).proxyInstance()
        def reporter = new CSVSummaryReporter([:], logger)
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
        Logger logger = new MockFor(Logger).proxyInstance()
        def reporter = new CSVSummaryReporter([csv: "/invalid/file"], logger)
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

    @Test
    void testRunsWithValidEmptyFile() {
        def mockLogger = new MockFor(Logger)
        def reporter = new CSVSummaryReporter([csv: getFixture("empty.csv")], mockLogger.proxyInstance())
        reporter.run([])
        // Expect no calls to the logger.
    }

    @Test
    void testReportsTotalSummary() {
        def mockLogger = new MockFor(Logger)
        def lines = []
        mockLogger.demand.quiet(2) { l -> lines << l }

        def reporter = new CSVSummaryReporter([csv: getFixture("times.csv")], mockLogger.proxyInstance())
        reporter.run([])

        assertEquals lines[1].trim(), "Total build time: 1:57.006"
    }
}

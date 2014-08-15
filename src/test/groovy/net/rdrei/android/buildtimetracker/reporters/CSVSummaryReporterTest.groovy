package net.rdrei.android.buildtimetracker.reporters

import groovy.mock.interceptor.MockFor
import org.gradle.api.logging.Logger
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.ocpsoft.prettytime.PrettyTime

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
        def err = null
        try {
            reporter.run([])
        } catch (ReporterConfigurationError e) {
            err = e
        }

        assertNotNull err
        assertEquals ReporterConfigurationError.ErrorType.REQUIRED, err.errorType
        assertEquals "csv", err.optionName
    }

    @Test
    void testThrowsErrorWithInvalidFile() {
        Logger logger = new MockFor(Logger).proxyInstance()
        def reporter = new CSVSummaryReporter([csv: "/invalid/file"], logger)
        def err = null

        try {
            reporter.run([])
        } catch (ReporterConfigurationError e) {
            err = e
        }

        assertNotNull err
        assertEquals ReporterConfigurationError.ErrorType.INVALID, err.errorType
        assertEquals "csv", err.optionName
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
        def mockPrettyTime = new MockFor(PrettyTime)
        def mockLogger = new MockFor(Logger)
        def lines = []
        mockLogger.demand.quiet(4) { l -> lines << l }
        mockPrettyTime.demand.format { "2 weeks" }


        def reporter = new CSVSummaryReporter([csv: getFixture("times.csv")], mockLogger.proxyInstance())
        reporter.run([])

        assertEquals "Total build time: 1:57.006", lines[2].trim()
        assertEquals "(measured since 2 weeks ago)", lines[3].trim()
    }

    @Test
    void testReportsDailySummary() {
        def mockLogger = new MockFor(Logger)
        def mockDateUtils = new MockFor(DateUtils)
        def lines = []
        mockLogger.demand.quiet(4) { l -> lines << l }
        mockDateUtils.demand.getMidnightTimestamp { 1407188121286L }

        def reporter = new CSVSummaryReporter([csv: getFixture("times.csv")], mockLogger.proxyInstance())
        reporter.run([])

        assertEquals "Build time today: 1234", lines[1].trim()
    }
}

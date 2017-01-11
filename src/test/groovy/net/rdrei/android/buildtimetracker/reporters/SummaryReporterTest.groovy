package net.rdrei.android.buildtimetracker.reporters

import groovy.mock.interceptor.MockFor
import net.rdrei.android.buildtimetracker.Timing
import org.gradle.BuildResult
import org.gradle.api.logging.Logger

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue
import static org.junit.Assert.assertFalse

import org.junit.Test

class SummaryReporterTest {
    @Test
    void testLinesCountMatchesTimings() {
        def mockLogger = new MockFor(Logger)
        mockLogger.demand.quiet(3) {}

        def reporter = new SummaryReporter([:], mockLogger.proxyInstance())
        reporter.run([
                new Timing(100, "task1", true, false, true),
                new Timing(200, "task2", false, true, false)
        ])
    }

    @Test
    void testIncludesSummaryHeader() {
        def mockLogger = new MockFor(Logger)
        def lines = []
        mockLogger.demand.quiet(3) { l -> lines << l }

        def reporter = new SummaryReporter([:], mockLogger.proxyInstance())
        reporter.run([
                new Timing(100, "task1", true, false, true),
                new Timing(200, "task2", false, true, false)
        ])

        assertEquals lines[0], "== Build Time Summary =="
    }

    @Test
    void testExcludesBelowTreshold() {
        def mockLogger = new MockFor(Logger)
        mockLogger.demand.quiet(2) {}

        def reporter = new SummaryReporter([threshold: 150], mockLogger.proxyInstance())
        reporter.run([
                new Timing(100, "task1", true, false, true),
                new Timing(200, "task2", false, true, false)
        ])
    }

    @Test
    void testDoesntOrderWithoutOptionEnabled() {
        def mockLogger = new MockFor(Logger)
        def lines = []
        mockLogger.demand.quiet(4) { l -> lines << l }

        def reporter = new SummaryReporter([ordered: false], mockLogger.proxyInstance())
        reporter.run([
                new Timing(300, "task1", true, false, true),
                new Timing(100, "task3", true, false, true),
                new Timing(200, "task2", false, true, false)
        ])

        // Don't hard code the exact format, don't unit test design
        assertTrue lines[1].contains("0:00.300")
        assertTrue lines[2].contains("0:00.100")
        assertTrue lines[3].contains("0:00.200")
    }

    @Test
    void testDoesntOrderWithOptionEnabled() {
        def mockLogger = new MockFor(Logger)
        def lines = []
        mockLogger.demand.quiet(4) { l -> lines << l }

        def reporter = new SummaryReporter([ordered: true], mockLogger.proxyInstance())
        reporter.run([
                new Timing(300, "task1", true, false, true),
                new Timing(100, "task3", true, false, true),
                new Timing(200, "task2", false, true, false)
        ])

        assertTrue lines[1].contains("0:00.100")
        assertTrue lines[2].contains("0:00.200")
        assertTrue lines[3].contains("0:00.300")
    }

    @Test
    void testOutputIncludesTaskName() {
        def mockLogger = new MockFor(Logger)
        def lines = []
        mockLogger.demand.quiet(4) { l -> lines << l }

        def reporter = new SummaryReporter([:], mockLogger.proxyInstance())
        reporter.run([
                new Timing(300, "task1", true, false, true),
                new Timing(100, "task3", true, false, true),
                new Timing(200, "task2", false, true, false)
        ])

        assertTrue lines[1].contains("task1")
        assertTrue lines[2].contains("task3")
        assertTrue lines[3].contains("task2")
    }

    @Test
    void testOutputIncludesShortenedLongTaskName() {
        def mockLogger = new MockFor(Logger)
        def mockTerminal = new MockFor(TerminalInfo)
        def lines = []
        mockLogger.demand.quiet(2) { l -> lines << l }
        mockTerminal.demand.getWidth(1) { 80 }

        mockTerminal.use {
            def reporter = new SummaryReporter([:], mockLogger.proxyInstance())
            reporter.run([
                    new Timing(300, "thisIsAReallyLongNameJustForTask1", true, false, true)
            ])
        }

        assert lines[1].contains("thisIsAReally…JustForTask1")
    }

    @Test
    void testOutputIncludesLongTaskName() {
        def mockLogger = new MockFor(Logger)
        def mockTerminal = new MockFor(TerminalInfo)
        def lines = []
        mockLogger.demand.quiet(2) { l -> lines << l }
        mockTerminal.demand.getWidth(1) { 80 }

        mockTerminal.use {
            def reporter = new SummaryReporter([shortenTaskNames: false], mockLogger.proxyInstance())
            reporter.run([
                new Timing(300, "thisIsAReallyLongNameJustForTask1", true, false, true)
            ])
        }

        assert lines[1].contains("thisIsAReallyLongNameJustForTask1")
    }

    @Test
    void testEmptyTaskList() {
        def mockLogger = new MockFor(Logger)
        mockLogger.demand.quiet(0) {}

        def reporter = new SummaryReporter([:], mockLogger.proxyInstance())
        reporter.run([
        ])
    }

    @Test
    void testOutputIncludesUnicodeBars() {
        def mockLogger = new MockFor(Logger)
        def lines = []
        mockLogger.demand.quiet(4) { l -> lines << l }

        def reporter = new SummaryReporter([:], mockLogger.proxyInstance())
        reporter.run([
                new Timing(300, "task1", true, false, true),
                new Timing(100, "task3", true, false, true),
        ])

        assertTrue lines[1].contains(SummaryReporter.UNICODE_SQUARE)
        assertFalse lines[1].contains(SummaryReporter.ASCII_SQUARE)
        assertTrue lines[2].contains(SummaryReporter.UNICODE_SQUARE)
        assertFalse lines[2].contains(SummaryReporter.ASCII_SQUARE)
    }

    @Test
    void testOutputIncludesASCIIBars() {
        def mockLogger = new MockFor(Logger)
        def lines = []
        mockLogger.demand.quiet(4) { l -> lines << l }

        def reporter = new SummaryReporter([barstyle: "ascii"], mockLogger.proxyInstance())
        reporter.run([
                new Timing(300, "task1", true, false, true),
                new Timing(100, "task3", true, false, true),
        ])

        assertTrue lines[1].contains(SummaryReporter.ASCII_SQUARE)
        assertFalse lines[1].contains(SummaryReporter.UNICODE_SQUARE)
        assertTrue lines[2].contains(SummaryReporter.ASCII_SQUARE)
        assertFalse lines[2].contains(SummaryReporter.UNICODE_SQUARE)
    }

    @Test
    void testOutputIncludesNoBars() {
        def mockLogger = new MockFor(Logger)
        def lines = []
        mockLogger.demand.quiet(4) { l -> lines << l }

        def reporter = new SummaryReporter([barstyle: "none"], mockLogger.proxyInstance())
        reporter.run([
                new Timing(300, "task1", true, false, true),
                new Timing(100, "task3", true, false, true),
        ])

        assertFalse lines[1].contains(SummaryReporter.ASCII_SQUARE)
        assertFalse lines[1].contains(SummaryReporter.UNICODE_SQUARE)
        assertFalse lines[2].contains(SummaryReporter.ASCII_SQUARE)
        assertFalse lines[2].contains(SummaryReporter.UNICODE_SQUARE)
    }

    @Test
    void testOutputIncludesPercentagesEvenWithoutBars() {
        def mockLogger = new MockFor(Logger)
        def lines = []
        mockLogger.demand.quiet(4) { l -> lines << l }

        def reporter = new SummaryReporter([barstyle: "none"], mockLogger.proxyInstance())
        reporter.run([
                new Timing(300, "task1", true, false, true),
                new Timing(100, "task3", true, false, true),
        ])

        assertTrue lines[1].contains("%")
        assertTrue lines[2].contains("%")
    }

    @Test
    void testOutputContainsStatusSuccessMessage() {
        def mockLogger = new MockFor(Logger)
        def mockBuildResult = new BuildResult(null, null)
        def lines = []
        mockLogger.ignore.quiet { l -> lines << l }
        mockLogger.ignore.lifecycle { l -> lines << l }

        def reporter = new SummaryReporter([:], mockLogger.proxyInstance())
        reporter.run([
                new Timing(300, "task1", true, false, true),
                new Timing(100, "task3", true, false, true),
        ])

        reporter.onBuildResult(mockBuildResult)
        assertTrue lines[4].contains("BUILD SUCCESSFUL")
    }

    @Test
    void testOutputContainsStatusFailureMessage() {
        def mockLogger = new MockFor(Logger)
        def mockBuildResult = new BuildResult(null, new Throwable())
        def lines = []
        mockLogger.ignore.lifecycle { l -> lines << l }
        mockLogger.ignore.quiet { l -> lines << l }
        mockLogger.ignore.error { l -> lines << l }

        def reporter = new SummaryReporter([:], mockLogger.proxyInstance())
        reporter.run([
                new Timing(300, "task1", false, false, true),
                new Timing(100, "task3", true, false, true),
        ])

        reporter.onBuildResult(mockBuildResult)
        assertTrue lines[4].contains("BUILD FAILED")
    }
}

package net.rdrei.android.buildtimetracker.reporters

import groovy.json.JsonSlurper
import groovy.mock.interceptor.MockFor
import net.rdrei.android.buildtimetracker.Timing
import org.gradle.api.logging.Logger
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue
import static org.junit.Assert.assertFalse

class JSONReporterTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    Logger mockLogger = new MockFor(Logger).proxyInstance()

    File mkTemporaryFile(String name) {
        File file = folder.newFile name
        if (file.exists()) {
            file.delete()
        }

        file
    }

    @Test
    void createsOutputJSON() {
        File file = mkTemporaryFile "test.json"
        assertFalse("Output JSON file exists.", file.exists())

        JSONReporter reporter = new JSONReporter([ output: file.getPath() ], mockLogger)

        reporter.run([
            new Timing(100, "task1", true, false, true),
            new Timing(200, "task2", false, true, false)
        ])

        assertTrue "Output JSON does not exist.", file.exists()
    }


    @Test
    void writesTimingsToOutputJSON() {
        File file = mkTemporaryFile "test.json"
        JSONReporter reporter = new JSONReporter([ output: file.getPath() ], mockLogger)

        reporter.run([
            new Timing(100, "task1", true, false, true),
            new Timing(200, "task2", false, true, false)
        ])

        def jsonSlurper = new JsonSlurper()
        def jsonObject = jsonSlurper.parseText(new File(file.getPath()).text)

        def measurements = jsonObject.measurements.iterator()

        // Verify first task
        def line0 = measurements.next()
        assertNotNull line0
        assertEquals 11, line0.size()
        assertEquals "task1", line0.task
        assertEquals 100, line0.ms

        // Verify second task
        def line1 = measurements.next()
        assertNotNull line1
        assertEquals 11, line1.size()
        assertEquals "task2", line1.task
        assertEquals 200, line1.ms

    }

    @Test
    void writesTimingsSuccessTrue() {
        File file = mkTemporaryFile "test.json"
        JSONReporter reporter = new JSONReporter([ output: file.getPath() ], mockLogger)

        reporter.run([
            new Timing(100, "task1", true, false, true),
            new Timing(200, "task2", true, true, false)
        ])

        def jsonSlurper = new JsonSlurper()
        def jsonObject = jsonSlurper.parseText(new File(file.getPath()).text)
        def measurements = jsonObject.measurements.iterator()

        // Verify first task
        assertEquals measurements.next().success, true
        // Verify second task
        assertEquals measurements.next().success, true
        // Verify overall success
        assertEquals jsonObject.success, true
    }

    @Test
    void writesTimingsSuccessFalse() {
        File file = mkTemporaryFile "test.json"
        JSONReporter reporter = new JSONReporter([ output: file.getPath() ], mockLogger)

        reporter.run([
            new Timing(100, "task1", true, false, true),
            new Timing(200, "task2", false, true, false)
        ])

        def jsonSlurper = new JsonSlurper()
        def jsonObject = jsonSlurper.parseText(new File(file.getPath()).text)
        def measurements = jsonObject.measurements.iterator()

        // Verify first task
        assertEquals measurements.next().success, true
        // Verify second task
        assertEquals measurements.next().success, false
        // Verify overall success
        assertEquals jsonObject.success, false
    }

}
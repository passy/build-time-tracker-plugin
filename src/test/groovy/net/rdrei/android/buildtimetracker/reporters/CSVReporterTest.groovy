package net.rdrei.android.buildtimetracker.reporters

import au.com.bytecode.opencsv.CSVReader
import groovy.mock.interceptor.MockFor
import org.gradle.api.logging.Logger
import net.rdrei.android.buildtimetracker.Timing
import org.gradle.internal.TimeProvider
import org.gradle.internal.TrueTimeProvider
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotEquals
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue
import static org.junit.Assert.assertFalse

class CSVReporterTest {

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
    void createsOutputCSV() {
        File file = mkTemporaryFile "test.csv"
        assertFalse("Output CSV exists.", file.exists())

        CSVReporter reporter = new CSVReporter([ output: file.getPath() ], mockLogger)

        reporter.run([
                new Timing(100, "task1", true, false, true),
                new Timing(200, "task2", false, true, false)
        ])

        assertTrue "Output CSV does not exist.", file.exists()
    }

    @Test
    void writesHeaderToOutputCSV() {
        File file = mkTemporaryFile "test.csv"
        CSVReporter reporter = new CSVReporter([ output: file.getPath() ], mockLogger)

        reporter.run([
                new Timing(100, "task1", true, false, true),
                new Timing(200, "task2", false, true, false)
        ])

        CSVReader reader = new CSVReader(new FileReader(file))

        String[] header = reader.readNext()
        assertNotNull header
        assertEquals 7, header.length
        assertEquals "timestamp", header[0]
        assertEquals "order", header[1]
        assertEquals "task", header[2]
        assertEquals "success", header[3]
        assertEquals "did_work", header[4]
        assertEquals "skipped", header[5]
        assertEquals "ms", header[6]

        reader.close()
    }

    @Test
    void doesNotWritesHeaderToOutputCSVWhenHeaderOptionFalse() {
        File file = mkTemporaryFile "test.csv"
        CSVReporter reporter = new CSVReporter([
                output: file.getPath(),
                header: "false"
        ], mockLogger)

        reporter.run([
                new Timing(100, "task1", true, false, true),
                new Timing(200, "task2", false, true, false)
        ])

        CSVReader reader = new CSVReader(new FileReader(file))

        String[] line = reader.readNext()
        assertNotNull line
        assertEquals 7, line.length
        assertNotEquals "timestamp", line[0]
        assertNotEquals "order", line[1]
        assertNotEquals "task", line[2]
        assertNotEquals "success", line[3]
        assertNotEquals "did_work", line[4]
        assertNotEquals "skipped", line[5]
        assertNotEquals "ms", line[6]

        reader.close()
    }

    @Test
    void writesTimingsToOutputCSV() {
        File file = mkTemporaryFile "test.csv"
        CSVReporter reporter = new CSVReporter([ output: file.getPath() ], mockLogger)

        reporter.run([
                new Timing(100, "task1", true, false, true),
                new Timing(200, "task2", false, true, false)
        ])

        CSVReader reader = new CSVReader(new FileReader(file))

        Iterator<String[]> lines = reader.readAll().iterator()

        // Skip the header
        lines.next()

        // Verify first task
        String[] line = lines.next()
        assertNotNull line
        assertEquals 7, line.length
        assertEquals "task1", line[2]
        assertEquals "100", line[6]

        // Verify second task
        line = lines.next()
        assertNotNull line
        assertEquals 7, line.length
        assertEquals "task2", line[2]
        assertEquals "200", line[6]

        reader.close()
    }

    @Test
    void includesBuildTimestampInOutputCSVRows() {
        def mockTimeProvider = new MockFor(TrueTimeProvider)
        mockTimeProvider.demand.getCurrentTime { 1234 }

        mockTimeProvider.use {
            File file = mkTemporaryFile "test.csv"
            CSVReporter reporter = new CSVReporter([ output: file.getPath() ], mockLogger)

            reporter.run([
                    new Timing(100, "task1", true, false, true),
                    new Timing(200, "task2", false, true, false)
            ])

            CSVReader reader = new CSVReader(new FileReader(file))

            Iterator<String[]> lines = reader.readAll().iterator()

            // Skip the header
            lines.next()

            // Verify first task
            String[] line = lines.next()
            assertNotNull line
            assertEquals 7, line.length
            assertEquals "1234", line[0]

            // Verify second task
            line = lines.next()
            assertNotNull line
            assertEquals 7, line.length
            assertEquals "1234", line[0]

            reader.close()
        }
    }

    @Test
    void includesTaskOrderInOutputCSVRows() {
        File file = mkTemporaryFile "test.csv"
        CSVReporter reporter = new CSVReporter([ output: file.getPath() ], mockLogger)

        reporter.run([
                new Timing(100, "task1", true, false, true),
                new Timing(200, "task2", false, true, false)
        ])

        CSVReader reader = new CSVReader(new FileReader(file))

        Iterator<String[]> lines = reader.readAll().iterator()

        // Skip the header
        lines.next()

        // Verify first task
        String[] line = lines.next()
        assertNotNull line
        assertEquals 7, line.length
        assertEquals "0", line[1]

        // Verify second task
        line = lines.next()
        assertNotNull line
        assertEquals 7, line.length
        assertEquals "1", line[1]

        reader.close()
    }

    @Test
    void includesTaskSuccessInOutputCSVRows() {
        File file = mkTemporaryFile "test.csv"
        CSVReporter reporter = new CSVReporter([ output: file.getPath() ], mockLogger)

        reporter.run([
                new Timing(100, "task1", true, false, true),
                new Timing(200, "task2", false, true, false)
        ])

        CSVReader reader = new CSVReader(new FileReader(file))

        Iterator<String[]> lines = reader.readAll().iterator()

        // Skip the header
        lines.next()

        // Verify first task
        String[] line = lines.next()
        assertNotNull line
        assertEquals 7, line.length
        assertEquals "true", line[3]

        // Verify second task
        line = lines.next()
        assertNotNull line
        assertEquals 7, line.length
        assertEquals "false", line[3]

        reader.close()
    }

    @Test
    void includesTaskDidWorkInOutputCSVRows() {
        File file = mkTemporaryFile "test.csv"
        CSVReporter reporter = new CSVReporter([ output: file.getPath() ], mockLogger)

        reporter.run([
                new Timing(100, "task1", true, false, true),
                new Timing(200, "task2", false, true, false)
        ])

        CSVReader reader = new CSVReader(new FileReader(file))

        Iterator<String[]> lines = reader.readAll().iterator()

        // Skip the header
        lines.next()

        // Verify first task
        String[] line = lines.next()
        assertNotNull line
        assertEquals 7, line.length
        assertEquals "false", line[4]

        // Verify second task
        line = lines.next()
        assertNotNull line
        assertEquals 7, line.length
        assertEquals "true", line[4]

        reader.close()
    }

    @Test
    void includesTaskSkippedInOutputCSVRows() {
        File file = mkTemporaryFile "test.csv"
        CSVReporter reporter = new CSVReporter([ output: file.getPath() ], mockLogger)

        reporter.run([
                new Timing(100, "task1", true, false, true),
                new Timing(200, "task2", false, true, false)
        ])

        CSVReader reader = new CSVReader(new FileReader(file))

        Iterator<String[]> lines = reader.readAll().iterator()

        // Skip the header
        lines.next()

        // Verify first task
        String[] line = lines.next()
        assertNotNull line
        assertEquals 7, line.length
        assertEquals "true", line[5]

        // Verify second task
        line = lines.next()
        assertNotNull line
        assertEquals 7, line.length
        assertEquals "false", line[5]

        reader.close()
    }

    @Test
    void appendsCSV() {
        File file = mkTemporaryFile "test.csv"
        file.write "existing content\n"

        CSVReporter reporter = new CSVReporter([
                output: file.getPath(),
                append: "true",
                header: "false"
        ], mockLogger)

        reporter.run([
                new Timing(100, "task1", true, false, true),
                new Timing(200, "task2", false, true, false)
        ])

        CSVReader reader = new CSVReader(new FileReader(file))

        Iterator<String[]> lines = reader.readAll().iterator()

        // Verify existing content
        String[] line = lines.next()
        assertNotNull line
        assertEquals 1, line.length
        assertEquals "existing content", line[0]

        // Verify first task
        line = lines.next()
        assertNotNull line
        assertEquals 7, line.length
        assertEquals "task1", line[2]
        assertEquals "100", line[6]

        // Verify second task
        line = lines.next()
        assertNotNull line
        assertEquals 7, line.length
        assertEquals "task2", line[2]
        assertEquals "200", line[6]

        reader.close()
    }
}

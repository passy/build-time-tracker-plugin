package net.rdrei.android.buildtimetracker.reporters

import au.com.bytecode.opencsv.CSVReader
import org.junit.After

import java.io.File
import java.io.FileReader
import net.rdrei.android.buildtimetracker.Timing
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotEquals
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue
import static org.junit.Assert.assertFalse

class CSVReporterTest {

    @Before
    void setUp() {
        File f = new File("buildtime/test.csv")
        if (f.exists()) {
            f.delete()
        }
        assertFalse("Output CSV does not exist.", f.exists())
    }

    @After
    void tearDown() {
        File f = new File("buildtime/test.csv")
        if (f.exists()) {
            f.delete()
        }
    }

    @Test
    void createsOutputCSV() {
        CSVReporter reporter = new CSVReporter([ output: "buildtime/test.csv" ])

        reporter.run(1234, [
                new Timing(100, "task1"),
                new Timing(200, "task2")
        ])

        File f = new File("buildtime/test.csv")
        assertTrue("Output CSV exists.", f.exists())
    }

    @Test
    void writesHeaderToOutputCSV() {
        CSVReporter reporter = new CSVReporter([ output: "buildtime/test.csv" ])

        reporter.run(1234, [
                new Timing(100, "task1"),
                new Timing(200, "task2")
        ])

        CSVReader reader = new CSVReader(new FileReader("buildtime/test.csv"))

        String[] header = reader.readNext()
        assertNotNull(header)
        assertEquals("timestamp", header[0])
        assertEquals("order", header[1])
        assertEquals("task", header[2])
        assertEquals("ms", header[3])
    }

    @Test
    void writesHeaderToOutputCSVWhenHeaderOptionNotFalse() {
        CSVReporter reporter = new CSVReporter([
                output: "buildtime/test.csv",
                header: "something not false"
        ])

        reporter.run(1234, [
                new Timing(100, "task1"),
                new Timing(200, "task2")
        ])

        CSVReader reader = new CSVReader(new FileReader("buildtime/test.csv"))

        String[] header = reader.readNext()
        assertNotNull(header)
        assertEquals("timestamp", header[0])
        assertEquals("order", header[1])
        assertEquals("task", header[2])
        assertEquals("ms", header[3])
    }

    @Test
    void doesNotWritesHeaderToOutputCSVWhenHeaderOptionFalse() {
        CSVReporter reporter = new CSVReporter([
                output: "buildtime/test.csv",
                header: "false"
        ])

        reporter.run(1234, [
                new Timing(100, "task1"),
                new Timing(200, "task2")
        ])

        CSVReader reader = new CSVReader(new FileReader("buildtime/test.csv"))

        String[] line = reader.readNext()
        assertNotNull(line)
        assertNotEquals("timestamp", line[0])
        assertNotEquals("order", line[1])
        assertNotEquals("task", line[2])
        assertNotEquals("ms", line[3])
    }

    @Test
    void writesTimingsToOutputCSV() {
        CSVReporter reporter = new CSVReporter([ output: "buildtime/test.csv" ])

        reporter.run(1234, [
                new Timing(100, "task1"),
                new Timing(200, "task2")
        ])

        CSVReader reader = new CSVReader(new FileReader("buildtime/test.csv"))

        Iterator<String[]> lines = reader.readAll().iterator()

        // Skip the header
        lines.next()

        // Verify first task
        String[] line = lines.next()
        assertNotNull(line)
        assertEquals("task1", line[2])
        assertEquals("100", line[3])

        // Verify second task
        line = lines.next()
        assertNotNull(line)
        assertEquals("task2", line[2])
        assertEquals("200", line[3])
    }

    @Test
    void includesBuildTimestampInOutputCSVRows() {
        CSVReporter reporter = new CSVReporter([ output: "buildtime/test.csv" ])

        reporter.run(1234, [
                new Timing(100, "task1"),
                new Timing(200, "task2")
        ])

        CSVReader reader = new CSVReader(new FileReader("buildtime/test.csv"))

        Iterator<String[]> lines = reader.readAll().iterator()

        // Skip the header
        lines.next()

        // Verify first task
        String[] line = lines.next()
        assertNotNull(line)
        assertEquals("1234", line[0])

        // Verify second task
        line = lines.next()
        assertNotNull(line)
        assertEquals("1234", line[0])
    }

    @Test
    void includesTaskOrderInOutputCSVRows() {
        CSVReporter reporter = new CSVReporter([ output: "buildtime/test.csv" ])

        reporter.run(1234, [
                new Timing(100, "task1"),
                new Timing(200, "task2")
        ])

        CSVReader reader = new CSVReader(new FileReader("buildtime/test.csv"))

        Iterator<String[]> lines = reader.readAll().iterator()

        // Skip the header
        lines.next()

        // Verify first task
        String[] line = lines.next()
        assertNotNull(line)
        assertEquals("0", line[1])

        // Verify second task
        line = lines.next()
        assertNotNull(line)
        assertEquals("1", line[1])
    }

    @Test
    void includesTotalBuildtimeInOutputCSV() {
        CSVReporter reporter = new CSVReporter([ output: "buildtime/test.csv" ])

        reporter.run(1234, [
                new Timing(100, "task1"),
                new Timing(200, "task2")
        ])

        CSVReader reader = new CSVReader(new FileReader("buildtime/test.csv"))

        Iterator<String[]> lines = reader.readAll().iterator()

        // Skip the header and tasks
        lines.next()
        lines.next()
        lines.next()

        // Verify total line
        String[] total = lines.next()
        assertNotNull(total)
        assertEquals("1234", total[0])
        assertEquals("*", total[1])
        assertEquals("total", total[2])
        assertEquals("300", total[3])
    }
}

package net.rdrei.android.buildtimetracker.reporters

import au.com.bytecode.opencsv.CSVReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.PrintWriter
import net.rdrei.android.buildtimetracker.Timing
import org.junit.After
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
                new Timing(100, "task1", true, false, true),
                new Timing(200, "task2", false, true, false)
        ])

        File f = new File("buildtime/test.csv")
        assertTrue("Output CSV exists.", f.exists())
    }

    @Test
    void writesHeaderToOutputCSV() {
        CSVReporter reporter = new CSVReporter([ output: "buildtime/test.csv" ])

        reporter.run(1234, [
                new Timing(100, "task1", true, false, true),
                new Timing(200, "task2", false, true, false)
        ])

        CSVReader reader = new CSVReader(new FileReader("buildtime/test.csv"))

        String[] header = reader.readNext()
        assertNotNull(header)
        assertEquals(7, header.length)
        assertEquals("timestamp", header[0])
        assertEquals("order", header[1])
        assertEquals("task", header[2])
        assertEquals("success", header[3])
        assertEquals("did_work", header[4])
        assertEquals("skipped", header[5])
        assertEquals("ms", header[6])

        reader.close()
    }

    @Test
    void doesNotWritesHeaderToOutputCSVWhenHeaderOptionFalse() {
        CSVReporter reporter = new CSVReporter([
                output: "buildtime/test.csv",
                header: "false"
        ])

        reporter.run(1234, [
                new Timing(100, "task1", true, false, true),
                new Timing(200, "task2", false, true, false)
        ])

        CSVReader reader = new CSVReader(new FileReader("buildtime/test.csv"))

        String[] line = reader.readNext()
        assertNotNull(line)
        assertEquals(7, line.length)
        assertNotEquals("timestamp", line[0])
        assertNotEquals("order", line[1])
        assertNotEquals("task", line[2])
        assertNotEquals("success", line[3])
        assertNotEquals("did_work", line[4])
        assertNotEquals("skipped", line[5])
        assertNotEquals("ms", line[6])

        reader.close()
    }

    @Test
    void writesTimingsToOutputCSV() {
        CSVReporter reporter = new CSVReporter([ output: "buildtime/test.csv" ])

        reporter.run(1234, [
                new Timing(100, "task1", true, false, true),
                new Timing(200, "task2", false, true, false)
        ])

        CSVReader reader = new CSVReader(new FileReader("buildtime/test.csv"))

        Iterator<String[]> lines = reader.readAll().iterator()

        // Skip the header
        lines.next()

        // Verify first task
        String[] line = lines.next()
        assertNotNull(line)
        assertEquals(7, line.length)
        assertEquals("task1", line[2])
        assertEquals("100", line[6])

        // Verify second task
        line = lines.next()
        assertNotNull(line)
        assertEquals(7, line.length)
        assertEquals("task2", line[2])
        assertEquals("200", line[6])

        reader.close()
    }

    @Test
    void includesBuildTimestampInOutputCSVRows() {
        CSVReporter reporter = new CSVReporter([ output: "buildtime/test.csv" ])

        reporter.run(1234, [
                new Timing(100, "task1", true, false, true),
                new Timing(200, "task2", false, true, false)
        ])

        CSVReader reader = new CSVReader(new FileReader("buildtime/test.csv"))

        Iterator<String[]> lines = reader.readAll().iterator()

        // Skip the header
        lines.next()

        // Verify first task
        String[] line = lines.next()
        assertNotNull(line)
        assertEquals(7, line.length)
        assertEquals("1234", line[0])

        // Verify second task
        line = lines.next()
        assertNotNull(line)
        assertEquals(7, line.length)
        assertEquals("1234", line[0])

        reader.close()
    }

    @Test
    void includesTaskOrderInOutputCSVRows() {
        CSVReporter reporter = new CSVReporter([ output: "buildtime/test.csv" ])

        reporter.run(1234, [
                new Timing(100, "task1", true, false, true),
                new Timing(200, "task2", false, true, false)
        ])

        CSVReader reader = new CSVReader(new FileReader("buildtime/test.csv"))

        Iterator<String[]> lines = reader.readAll().iterator()

        // Skip the header
        lines.next()

        // Verify first task
        String[] line = lines.next()
        assertNotNull(line)
        assertEquals(7, line.length)
        assertEquals("0", line[1])

        // Verify second task
        line = lines.next()
        assertNotNull(line)
        assertEquals(7, line.length)
        assertEquals("1", line[1])

        reader.close()
    }

    @Test
    void includesTaskSuccessInOutputCSVRows() {
        CSVReporter reporter = new CSVReporter([ output: "buildtime/test.csv" ])

        reporter.run(1234, [
                new Timing(100, "task1", true, false, true),
                new Timing(200, "task2", false, true, false)
        ])

        CSVReader reader = new CSVReader(new FileReader("buildtime/test.csv"))

        Iterator<String[]> lines = reader.readAll().iterator()

        // Skip the header
        lines.next()

        // Verify first task
        String[] line = lines.next()
        assertNotNull(line)
        assertEquals(7, line.length)
        assertEquals("true", line[3])

        // Verify second task
        line = lines.next()
        assertNotNull(line)
        assertEquals(7, line.length)
        assertEquals("false", line[3])

        reader.close()
    }

    @Test
    void includesTaskDidWorkInOutputCSVRows() {
        CSVReporter reporter = new CSVReporter([ output: "buildtime/test.csv" ])

        reporter.run(1234, [
                new Timing(100, "task1", true, false, true),
                new Timing(200, "task2", false, true, false)
        ])

        CSVReader reader = new CSVReader(new FileReader("buildtime/test.csv"))

        Iterator<String[]> lines = reader.readAll().iterator()

        // Skip the header
        lines.next()

        // Verify first task
        String[] line = lines.next()
        assertNotNull(line)
        assertEquals(7, line.length)
        assertEquals("false", line[4])

        // Verify second task
        line = lines.next()
        assertNotNull(line)
        assertEquals(7, line.length)
        assertEquals("true", line[4])

        reader.close()
    }

    @Test
    void includesTaskSkippedInOutputCSVRows() {
        CSVReporter reporter = new CSVReporter([ output: "buildtime/test.csv" ])

        reporter.run(1234, [
                new Timing(100, "task1", true, false, true),
                new Timing(200, "task2", false, true, false)
        ])

        CSVReader reader = new CSVReader(new FileReader("buildtime/test.csv"))

        Iterator<String[]> lines = reader.readAll().iterator()

        // Skip the header
        lines.next()

        // Verify first task
        String[] line = lines.next()
        assertNotNull(line)
        assertEquals(7, line.length)
        assertEquals("true", line[5])

        // Verify second task
        line = lines.next()
        assertNotNull(line)
        assertEquals(7, line.length)
        assertEquals("false", line[5])

        reader.close()
    }

    @Test
    void appendsCSV() {
        File file = new File("buildtime/test.csv");
        file.getParentFile().mkdirs()
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)))
        writer.println("existing content")
        writer.close()

        CSVReporter reporter = new CSVReporter([
                output: "buildtime/test.csv",
                append: "true",
                header: "false"
        ])

        reporter.run(1234, [
                new Timing(100, "task1", true, false, true),
                new Timing(200, "task2", false, true, false)
        ])

        CSVReader reader = new CSVReader(new FileReader("buildtime/test.csv"))

        Iterator<String[]> lines = reader.readAll().iterator()

        // Verify existing content
        String[] line = lines.next()
        assertNotNull(line)
        assertEquals(1, line.length)
        assertEquals("existing content", line[0])

        // Verify first task
        line = lines.next()
        assertNotNull(line)
        assertEquals(7, line.length)
        assertEquals("task1", line[2])
        assertEquals("100", line[6])

        // Verify second task
        line = lines.next()
        assertNotNull(line)
        assertEquals(7, line.length)
        assertEquals("task2", line[2])
        assertEquals("200", line[6])

        reader.close()
    }
}

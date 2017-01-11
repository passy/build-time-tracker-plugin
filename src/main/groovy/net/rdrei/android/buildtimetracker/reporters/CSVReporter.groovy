package net.rdrei.android.buildtimetracker.reporters

import au.com.bytecode.opencsv.CSVWriter
import net.rdrei.android.buildtimetracker.Timing
import org.gradle.api.logging.Logger

import java.text.DateFormat
import java.text.SimpleDateFormat

class CSVReporter extends AbstractBuildTimeTrackerReporter {
    CSVReporter(Map<String, String> options, Logger logger) {
        super(options, logger)
    }

    @Override
    def run(List<Timing> timings) {
        long timestamp = new TrueTimeProvider().getCurrentTime()
        String output = getOption("output", "")
        boolean append = getOption("append", "false").toBoolean()
        TimeZone tz = TimeZone.getTimeZone("UTC")
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss,SSS'Z'")
        df.setTimeZone(tz)

        File file = new File(output)
        file.getParentFile()?.mkdirs()

        CSVWriter writer = new CSVWriter(new BufferedWriter(new FileWriter(file, append)))

        if (getOption("header", "true").toBoolean()) {
            String[] headers = ["timestamp", "order", "task", "success", "did_work", "skipped", "ms", "date",
                                "cpu", "memory", "os"]
            writer.writeNext(headers)
        }

        def info = new SysInfo()
        def osId = info.getOSIdentifier()
        def cpuId = info.getCPUIdentifier()
        def maxMem = info.getMaxMemory()

        timings.eachWithIndex { timing, idx ->
            String[] line = [
                    timestamp.toString(),
                    idx.toString(),
                    timing.path,
                    timing.success.toString(),
                    timing.didWork.toString(),
                    timing.skipped.toString(),
                    timing.ms.toString(),
                    df.format(new Date(timestamp)),
                    cpuId,
                    maxMem,
                    osId
            ].toArray()
            writer.writeNext(line)
        }

        writer.close()
    }
}

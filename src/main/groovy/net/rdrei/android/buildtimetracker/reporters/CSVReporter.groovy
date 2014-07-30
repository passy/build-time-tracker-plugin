package net.rdrei.android.buildtimetracker.reporters

import au.com.bytecode.opencsv.CSVWriter
import java.io.File
import java.io.FileWriter
import net.rdrei.android.buildtimetracker.Timing

class CSVReporter extends AbstractBuildTimeTrackerReporter {
    CSVReporter(Map<String, String> options) {
        super(options)
    }

    @Override
    def run(long start, List<Timing> timings) {
        String output = getOption("output", "")
        boolean append = getOption("append", "false").toBoolean()

        File file = new File(output)
        file.getParentFile().mkdirs()
        CSVWriter writer = new CSVWriter(new BufferedWriter(new FileWriter(file, append)))

        if (getOption("header", "true").toBoolean()) {
            String[] headers = ["timestamp", "order", "task", "success", "did_work", "skipped", "ms"]
            writer.writeNext(headers)
        }

        timings.eachWithIndex { timing, idx ->
            String[] line = [
                    start.toString(),
                    idx.toString(),
                    timing.path,
                    timing.success.toString(),
                    timing.didWork.toString(),
                    timing.skipped.toString(),
                    timing.ms.toString()
            ].toArray()
            writer.writeNext(line)
        }

        writer.close()
    }
}

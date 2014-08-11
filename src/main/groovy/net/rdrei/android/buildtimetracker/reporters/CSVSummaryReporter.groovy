package net.rdrei.android.buildtimetracker.reporters

import au.com.bytecode.opencsv.CSVReader
import net.rdrei.android.buildtimetracker.Timing
import org.gradle.api.logging.Logger

class CSVSummaryReporter extends AbstractBuildTimeTrackerReporter {
    CSVSummaryReporter(Map<String, String> options, Logger logger) {
        super(options, logger)
    }

    @Override
    def run(List<Timing> timings) {
        def csv = getOption("csv", "")
        def csvFile = new File(csv)

        if (csv.isEmpty()) {
            throw new ReporterConfigurationError(
                    ReporterConfigurationError.ErrorType.REQUIRED,
                    this.getClass().getSimpleName(),
                    "csv"
            )
        }

        if (!csvFile.exists() || !csvFile.isFile()) {
            throw new ReporterConfigurationError(
                    ReporterConfigurationError.ErrorType.INVALID,
                    this.getClass().getSimpleName(),
                    "csv",
                    "$csv either doesn't exist or is not a valid file"
            )
        }

        printReport(new CSVReader(new BufferedReader(new FileReader(csvFile))))
    }

    void printReport(CSVReader reader) {
        def lines = reader.readAll()
        if (lines.size() == 0) return

        logger.quiet "== CSV Build Time Summary =="

        long total = lines.collect { Long.valueOf(it[6]) }.sum()
        logger.quiet "Total build time: " + FormattingUtils.formatDuration(total)
    }
}

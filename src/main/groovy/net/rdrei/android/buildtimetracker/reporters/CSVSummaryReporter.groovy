package net.rdrei.android.buildtimetracker.reporters

import net.rdrei.android.buildtimetracker.Timing
import org.gradle.api.logging.Logger

class CSVSummaryReporter extends AbstractBuildTimeTrackerReporter {

    CSVSummaryReporter(Map<String, String> options, Logger logger) {
        super(options, logger)
    }

    @Override
    def run(List<Timing> timings) {
        String csv = getOption("csv", "")
        File csvFile = new File(csv)
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
    }
}

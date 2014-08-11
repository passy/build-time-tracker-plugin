package net.rdrei.android.buildtimetracker.reporters

class ReporterConfigurationError extends Exception {
    String reporterName
    String optionName
    ErrorType errorType
    String details

    static enum ErrorType {
        REQUIRED,
        INVALID,
        OTHER
    }

    ReporterConfigurationError(ErrorType errorType, String reporterName, String optionName) {
        this(errorType, reporterName, optionName, null)
    }

    ReporterConfigurationError(ErrorType errorType, String reporterName, String optionName,
                               String details) {
        super(generateMessage(errorType, reporterName, optionName, details))
        this.reporterName = reporterName
        this.optionName = optionName
        this.errorType = errorType
        this.details = details
    }

    static String generateMessage(ErrorType errorType, String reporterName, String optionName,
                                  String details) {
        def msg

        switch (errorType) {
            case ErrorType.REQUIRED:
                msg = "$reporterName requires option $optionName to be set"
                if (details != null) msg += ": $details"
                break
            case ErrorType.INVALID:
                msg = "Option $optionName set for $reporterName is invalid"
                if (details != null) msg += ": $details"
                break
            default:
                msg = details ?: "Unknown error. Well, fuck."
        }

        msg
    }
}

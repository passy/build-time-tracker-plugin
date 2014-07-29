package net.rdrei.android.buildtimetracker

import net.rdrei.android.buildtimetracker.reporters.BuildTimeTrackerReporter
import net.rdrei.android.buildtimetracker.reporters.SummaryReporter
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.Plugin
import org.gradle.api.Project

class BuildTimeTrackerPlugin implements Plugin<Project> {
    def REPORTERS = [
        summary: SummaryReporter
    ]

    NamedDomainObjectCollection<ReporterExtension> reporterExtensions

    @Override
    void apply(Project project) {
        project.extensions.create("buildtimetracker", BuildTimeTrackerExtension)
        reporterExtensions = project.buildtimetracker.extensions.reporters = project.container(ReporterExtension)
        project.gradle.addBuildListener(new TimingRecorder(this))
    }

    List<BuildTimeTrackerReporter> getReporters() {
        def reporters = []

        // TODO: Use some functional construct here
        reporterExtensions.each { ext ->
            if (REPORTERS.containsKey(ext.name)) {
                reporters.add(REPORTERS.get(ext.name).newInstance(ext.options))
            }
        }

        reporters
    }
}

class BuildTimeTrackerExtension {
    // Not in use at the moment.
}

class ReporterExtension {
    final String name
    final Map<String, String> options = [:]

    ReporterExtension(String name) {
        this.name = name
    }

    @Override
    String toString() {
        return name
    }

    def methodMissing(String name, args) {
        // I'm feeling really naughty.
        if (args.length == 1 && args[0] instanceof String) {
            options[name] = args[0]
        } else {
            throw new MissingMethodException(name, this.class, args)
        }
    }
}

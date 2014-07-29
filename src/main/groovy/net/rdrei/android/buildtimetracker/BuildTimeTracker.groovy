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
                reporters.add(REPORTERS.get(ext.name).newInstance())
            }
        }

        reporters
    }
}

class BuildTimeTrackerExtension {
}

class ReporterExtension {
    final String name

    ReporterExtension(String name) {
        this.name = name
    }

    @Override
    String toString() {
        return name
    }
}

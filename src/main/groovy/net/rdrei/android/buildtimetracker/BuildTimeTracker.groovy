package net.rdrei.android.buildtimetracker

import net.rdrei.android.buildtimetracker.internal.TimingsListener
import net.rdrei.android.buildtimetracker.reporters.PrintReporter
import org.gradle.api.Plugin
import org.gradle.api.Project

class BuildTimeTrackerPlugin implements Plugin<Project> {
    def REPORTERS = [
        print: PrintReporter
    ]

    @Override
    void apply(Project project) {
        def extension = project.extensions.create("buildtimetracker", BuildTimeTrackerConfig)
        def reporters = project.buildtimetracker.extensions.reporters = project.container(Reporter)
        project.gradle.addBuildListener(new TimingsListener(reporters))
    }
}

class BuildTimeTrackerConfig {
    boolean silent = false
}

class Reporter {
    final String name

    Reporter(String name) {
        this.name = name
    }

    @Override
    String toString() {
        return name
    }
}

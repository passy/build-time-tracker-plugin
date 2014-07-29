package net.rdrei.android.buildtimetracker.internal

import net.rdrei.android.buildtimetracker.BuildTimeTrackerConfig
import net.rdrei.android.buildtimetracker.Reporter
import net.rdrei.android.buildtimetracker.reporters.BuildTimeTrackerReporter
import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle
import org.gradle.api.tasks.TaskState
import org.gradle.util.Clock

class Timing {
    long ms
    String path

    Timing(long ms, String path) {
        this.ms = ms
        this.path = path
    }
}

class TimingsListener implements TaskExecutionListener, BuildListener {
    private Clock clock
    private timings = []
    private reporters = []

    TimingsListener(NamedDomainObjectContainer<Reporter> reporters) {
        this.reporters = reporters
    }

    @Override
    void beforeExecute(Task task) {
        clock = new Clock()
    }

    @Override
    void afterExecute(Task task, TaskState taskState) {
        def timing = new Timing(clock.getTimeInMs(), task.getPath())
        timings.add(timing)
    }

    @Override
    void buildFinished(BuildResult result) {
        def total = 0
        println "Task timings:"
        for (timing in timings) {
            if (timing.ms >= 50) {
                printf "%7sms  %s\n", timing
            }
            total += timing.ms
        }

        printf "Total time wasted: %7sms\n", total

        println "REPORTERS:" + reporters
        reporters.each {
            print "reporter: $it"
        }
        println "------"
    }

    @Override
    void buildStarted(Gradle gradle) {}

    @Override
    void projectsEvaluated(Gradle gradle) {}

    @Override
    void projectsLoaded(Gradle gradle) {}

    @Override
    void settingsEvaluated(Settings settings) {}

    List<String> getTasks() {
        def tasks = []
        for (timing in timings) {
            tasks.add(timing.path)
        }

        return tasks
    }

    Long getTiming(String path) {
        for (timing in timings) {
            if (timing.path == path) {
                return timing.ms
            }
        }

        return null
    }
}

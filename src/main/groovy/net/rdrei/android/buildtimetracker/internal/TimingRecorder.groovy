package net.rdrei.android.buildtimetracker.internal

import net.rdrei.android.buildtimetracker.BuildListenerAdapter
import net.rdrei.android.buildtimetracker.BuildTimeTrackerConfig
import net.rdrei.android.buildtimetracker.Reporter
import net.rdrei.android.buildtimetracker.reporters.BuildTimeTrackerReporter
import net.rdrei.android.buildtimetracker.reporters.SummaryReporter
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

class TimingRecorder extends BuildListenerAdapter implements TaskExecutionListener {
    private Clock clock
    private timings = []
    private NamedDomainObjectContainer<Reporter> reporters

    TimingRecorder(NamedDomainObjectContainer<Reporter> reporters) {
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
        println "REPORTERS:" + reporters
        reporters.each { reporter ->
            reporter.run(timings)
            printf "reporter: %s", reporter
        }

        // TODO: Add using DSL syntax eventually.
        new SummaryReporter().run(timings)
    }

    List<String> getTasks() {
        List<String> tasks = []
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

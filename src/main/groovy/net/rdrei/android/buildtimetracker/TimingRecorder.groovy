package net.rdrei.android.buildtimetracker

import net.rdrei.android.buildtimetracker.reporters.SummaryReporter
import org.gradle.BuildResult
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
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
    private List<Timing> timings = []
    private BuildTimeTrackerPlugin plugin

    TimingRecorder(BuildTimeTrackerPlugin plugin) {
        this.plugin = plugin
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
        plugin.reporters.each { reporter ->
            reporter.run(timings)
        }
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

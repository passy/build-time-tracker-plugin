package net.rdrei.android.buildtimetracker

import org.gradle.BuildResult
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.invocation.Gradle
import org.gradle.api.tasks.TaskState
import org.gradle.util.Clock

class Timing {
    long ms
    String path
    boolean success
    boolean didWork
    boolean skipped

    Timing(long ms, String path, boolean success, boolean didWork, boolean skipped) {
        this.ms = ms
        this.path = path
        this.success = success
        this.didWork = didWork
        this.skipped = skipped
    }
}

class TimingRecorder extends BuildAndTaskExecutionListenerAdapter implements TaskExecutionListener {
    private Clock clock
    private List<Timing> timings = []
    private BuildTimeTrackerPlugin plugin

    TimingRecorder(BuildTimeTrackerPlugin plugin) {
        this.plugin = plugin
    }

    @Override
    void buildStarted(Gradle gradle) {
        clock = new Clock()
    }

    @Override
    void afterExecute(Task task, TaskState taskState) {
        def timing = new Timing(
                clock.getTimeInMs(),
                task.getPath(),
                taskState.getFailure() != null,
                taskState.getDidWork(),
                taskState.getSkipped()
        )
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

        tasks
    }

    Long getTiming(String path) {
        for (timing in timings) {
            if (timing.path == path) {
                return timing.ms
            }
        }

        null
    }
}

package net.rdrei.android.buildtimetracker

import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener

import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle
import org.gradle.api.tasks.TaskState

class BuildAndTaskExecutionListenerAdapter implements BuildListener, TaskExecutionListener {
    @Override
    void buildStarted(Gradle gradle) { }

    @Override
    void settingsEvaluated(Settings settings) { }

    @Override
    void projectsLoaded(Gradle gradle) { }

    @Override
    void projectsEvaluated(Gradle gradle) { }

    @Override
    void buildFinished(BuildResult buildResult) { }

    @Override
    void beforeExecute(Task task) {}

    @Override
    void afterExecute(Task task, TaskState taskState) {}
}

package net.rdrei.android.buildtimetracker

import java.lang.Override
import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.Task
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle
import org.gradle.api.tasks.TaskState

class BuildAndTaskExecutionListenerAdapter implements BuildListener {
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
    void beforeExecute(org.gradle.api.Task task) {}

    @Override
    void afterExecute(org.gradle.api.Task task, org.gradle.api.tasks.TaskState taskState) {}
}

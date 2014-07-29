package net.rdrei.android.buildtimetracker.internal

import groovy.mock.interceptor.MockFor
import net.rdrei.android.buildtimetracker.BuildTimeTrackerPlugin
import net.rdrei.android.buildtimetracker.ReporterExtension
import net.rdrei.android.buildtimetracker.TimingRecorder
import net.rdrei.android.buildtimetracker.reporters.SummaryReporter
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.util.Clock
import org.junit.Test

import static org.junit.Assert.assertEquals

class TimingsListenerTest {

    Task mockTask(String path) {
        def mockTask = new MockFor(Task)
        mockTask.demand.getPath { path }

        mockTask.proxyInstance()
    }

    MockFor mockClock(int ms) {
        def mockClock = new MockFor(Clock)
        mockClock.demand.getTimeInMs { ms }

        mockClock
    }

    NamedDomainObjectContainer<ReporterExtension> buildReporters() {
        new ProjectBuilder().build().container(ReporterExtension)
    }

    BuildTimeTrackerPlugin buildPlugin() {
        def plugin = new BuildTimeTrackerPlugin()
    }

    @Test
    void recordsTaskPaths() {
        mockClock(0).use {
            def plugin = buildPlugin()
            TimingRecorder listener = new TimingRecorder(plugin)
            Task task = mockTask("test")

            listener.beforeExecute(task)
            listener.afterExecute(task, null)

            assertEquals(["test"], listener.getTasks())
        }
    }

    @Test
    void recordsTaskTiming() {
        mockClock(123).use {
            TimingRecorder listener = new TimingRecorder()
            Task task = mockTask("test")

            listener.beforeExecute(task)
            listener.afterExecute(task, null)

            assertEquals(123, listener.getTiming("test"))
        }
    }

    @Test
    void buildFinishes() {
        mockClock(0).use {
            def plugin = buildPlugin()

            TimingRecorder listener = new TimingRecorder(plugin)
            Task task = mockTask("test")

            listener.beforeExecute(task)
            listener.afterExecute(task, null)
            listener.buildFinished(null)
        }
    }

    @Test
    void callsReportersOnBuildFinished() {
        def mockReporter = new MockFor(ReporterExtension)
        mockReporter.demand.getName(1..3) { "test-reporter" }
        mockReporter.demand.run { timings ->
            assertEquals 1, timings.size
            assertEquals "test", timings.get(0).path
            assertEquals 123, timings.get(0).ms
        }

        mockClock(123).use {
            NamedDomainObjectContainer<ReporterExtension> reporters = buildReporters()
            def proxyReporter = mockReporter.proxyInstance()
            reporters.add(proxyReporter)

            TimingRecorder listener = new TimingRecorder(reporters)
            Task task = mockTask("test")

            listener.beforeExecute(task)
            listener.afterExecute(task, null)
            listener.buildFinished(null)

            mockReporter.verify(proxyReporter)
        }
    }
}

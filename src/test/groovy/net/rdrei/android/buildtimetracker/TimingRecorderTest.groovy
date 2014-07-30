package net.rdrei.android.buildtimetracker

import groovy.mock.interceptor.MockFor
import net.rdrei.android.buildtimetracker.BuildTimeTrackerPlugin
import net.rdrei.android.buildtimetracker.TimingRecorder
import net.rdrei.android.buildtimetracker.reporters.AbstractBuildTimeTrackerReporter
import org.gradle.api.Task
import org.gradle.util.Clock
import org.junit.Test

import static org.junit.Assert.assertEquals

class TimingRecorderTest {

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

    MockFor mockClock(int start, int task) {
        def mockClock = new MockFor(Clock)
        mockClock.demand.getTimeInMs { start }
        mockClock.demand.getTimeInMs { task }

        mockClock
    }

    BuildTimeTrackerPlugin buildPlugin() {
        new BuildTimeTrackerPlugin()
    }


    @Test
    void recordStartTimeOnBuildStarted() {
        mockClock(123).use {
            def plugin = buildPlugin()
            TimingRecorder listener = new TimingRecorder(plugin)
            Task task = mockTask("test")

            listener.buildStarted(null)

            assertEquals(123, listener.getStart())
        }
    }

    @Test
    void recordsTaskPaths() {
        mockClock(0, 0).use {
            def plugin = buildPlugin()
            TimingRecorder listener = new TimingRecorder(plugin)
            Task task = mockTask("test")

            listener.buildStarted(null)
            listener.beforeExecute(task)
            listener.afterExecute(task, null)

            assertEquals(["test"], listener.getTasks())
        }
    }

    @Test
    void recordsTaskTiming() {
        mockClock(0, 123).use {
            TimingRecorder listener = new TimingRecorder()
            Task task = mockTask("test")

            listener.buildStarted(null)
            listener.beforeExecute(task)
            listener.afterExecute(task, null)

            assertEquals(123, listener.getTiming("test"))
        }
    }

    @Test
    void buildFinishes() {
        mockClock(0, 0).use {
            def plugin = buildPlugin()

            TimingRecorder listener = new TimingRecorder(plugin)
            Task task = mockTask("test")

            listener.buildStarted(null)
            listener.beforeExecute(task)
            listener.afterExecute(task, null)
            listener.buildFinished(null)
        }
    }

    @Test
    void callsReportersOnBuildFinished() {
        def mockReporter = new MockFor(AbstractBuildTimeTrackerReporter)
        mockReporter.demand.run { start, timings ->
            assertEquals 1, timings.size
            assertEquals "test", timings.get(0).path
            assertEquals 123, timings.get(0).ms
        }
        def proxyReporter = mockReporter.proxyInstance([:])

        def mockPlugin = new MockFor(BuildTimeTrackerPlugin)
        mockPlugin.demand.getReporters { [ proxyReporter ] }
        def proxyPlugin = mockPlugin.proxyInstance()

        mockClock(0, 123).use {
            TimingRecorder listener = new TimingRecorder(proxyPlugin)
            Task task = mockTask("test")

            listener.buildStarted(null)
            listener.beforeExecute(task)
            listener.afterExecute(task, null)
            listener.buildFinished(null)

            mockReporter.verify(proxyReporter)
            mockPlugin.verify(proxyPlugin)
        }
    }
}

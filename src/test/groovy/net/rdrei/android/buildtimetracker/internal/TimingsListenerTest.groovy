package net.rdrei.android.buildtimetracker.internal

import groovy.mock.interceptor.MockFor
import org.gradle.api.Task
import org.gradle.util.Clock
import org.junit.Test

import static org.junit.Assert.assertEquals

class TimingsListenerTest {

    Task mockTask(String path) {
        def mockTask = new MockFor(Task.class)
        mockTask.demand.getPath { path }

        return mockTask.proxyInstance()
    }

    MockFor mockClock(int ms) {
        def mockClock = new MockFor(Clock.class)
        mockClock.demand.getTimeInMs { 123 }

        return mockClock
    }

    @Test
    void recordsTaskPaths() {
        mockClock(0).use {
            TimingsListener listener = new TimingsListener()
            Task task = mockTask("test")

            listener.beforeExecute(task)
            listener.afterExecute(task, null)

            assertEquals(["test"], listener.getTasks())
        }
    }

    @Test
    void recordsTaskTiming() {
        mockClock(123).use {
            TimingsListener listener = new TimingsListener()
            Task task = mockTask("test")

            listener.beforeExecute(task)
            listener.afterExecute(task, null)

            assertEquals(123, listener.getTiming("test"))
        }
    }
}

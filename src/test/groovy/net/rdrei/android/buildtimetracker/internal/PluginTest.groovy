package net.rdrei.android.buildtimetracker

import groovy.mock.interceptor.MockFor
import net.rdrei.android.buildtimetracker.TimingRecorder
import org.gradle.util.Clock
import net.rdrei.android.buildtimetracker.reporters.SummaryReporter
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PluginTest {
    Project project

    MockFor mockClock(int ms) {
        def mockClock = new MockFor(Clock)
        mockClock.demand.getTimeInMs(0..1) { ms }

        mockClock
    }

    @Before
    void setUp() {
        project = ProjectBuilder.builder()
                .build()
    }

    @Test
    void testSummaryInvocation() {
        def mockSummaryReporter = new MockFor(SummaryReporter)
        mockSummaryReporter.demand.run { timings ->
            assertEquals 1, timings.size
            assertEquals "test", timings.get(0).path
            assertEquals 123, timings.get(0).ms
        }

        mockClock(123).use {
            project.apply plugin: 'build-time-tracker'
            project.buildtimetracker {
                reporters {
                    summary {}
                }
            }
        }
    }
}

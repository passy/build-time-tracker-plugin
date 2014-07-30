package net.rdrei.android.buildtimetracker

import org.gradle.api.tasks.TaskState

class TestTaskState implements TaskState {
    boolean executed = false
    Throwable failure = null
    boolean didWork = false
    boolean skipped = false
    String skipMessage = null

    @java.lang.Override
    boolean getExecuted() {
        executed
    }

    @java.lang.Override
    java.lang.Throwable getFailure() {
        failure
    }

    @java.lang.Override
    void rethrowFailure() {
        throw failure
    }

    @java.lang.Override
    boolean getDidWork() {
        didWork
    }

    @java.lang.Override
    boolean getSkipped() {
        skipped
    }

    @java.lang.Override
    java.lang.String getSkipMessage() {
        skipMessage
    }
}

class TaskStateBuilder {
    TestTaskState state = new TestTaskState()

    TaskStateBuilder withExecuted(boolean executed) {
        state.executed = executed
        this
    }

    TaskStateBuilder withFailure(Throwable failure) {
        state.failure = failure
        this
    }

    TaskStateBuilder withDidWork(boolean didWork) {
        state.didWork = didWork
        this
    }

    TaskStateBuilder withSkipped(boolean skipped) {
        state.skipped = skipped
        this
    }

    TaskStateBuilder withSkipMessage(boolean skipMessage) {
        state.skipMessage = skipMessage
        this
    }

    TaskState build() {
        state
    }
}
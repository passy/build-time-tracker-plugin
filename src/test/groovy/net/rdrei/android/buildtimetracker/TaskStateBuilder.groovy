package net.rdrei.android.buildtimetracker

import org.gradle.api.tasks.TaskState

class TestTaskState implements TaskState {
    boolean executed = false
    Throwable failure = null
    boolean didWork = false
    boolean skipped = false
    boolean upToDate = false
    String skipMessage = null

    @Override
    boolean getExecuted() {
        executed
    }

    @Override
    Throwable getFailure() {
        failure
    }

    @Override
    void rethrowFailure() {
        throw failure
    }

    @Override
    boolean getDidWork() {
        didWork
    }

    @Override
    boolean getSkipped() {
        skipped
    }

    @Override
    String getSkipMessage() {
        skipMessage
    }

    @Override
    boolean getUpToDate() {
        upToDate
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

    TaskState withUpToDate(boolean upToDate) {
        state.upToDate = upToDate
        this
    }

    TaskState build() {
        state
    }
}
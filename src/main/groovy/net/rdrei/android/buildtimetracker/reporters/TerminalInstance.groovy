package net.rdrei.android.buildtimetracker.reporters

import jline.Terminal
import jline.TerminalFactory

/**
 * Singleton wrapper around {@link jline.TerminalFactory} to work around jline2#163
 */
class TerminalInstance {
    private static Terminal sInstance = TerminalFactory.get()

    public static Terminal get() {
        return sInstance
    }
}

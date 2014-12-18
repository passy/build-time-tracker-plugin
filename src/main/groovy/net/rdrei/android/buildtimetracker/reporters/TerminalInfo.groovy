package net.rdrei.android.buildtimetracker.reporters

import groovy.transform.Memoized

/**
 * Singleton info class for determining the width of the terminal we run in.
 */
class TerminalInfo {
    @Memoized
    public int getWidth(int fallback) {
        def cols = System.getenv("COLUMNS")
        if (cols != null) {
            return Integer.parseInt(cols, 10)
        }

        try {
            def res = Integer.parseInt("tput cols".execute().text.trim())
        } catch (Exception ignored) {
            fallback
        }
    }
}

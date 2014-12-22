package net.rdrei.android.buildtimetracker.reporters

import groovy.transform.Memoized

/**
 * Singleton info class for determining the width of the terminal we run in.
 */
class TerminalInfo {
    @Memoized
    public int getWidth(int fallback) {
        // Start by trying to get the `COLUMNS` env variable, which is swallowed by Gradle most of the time.
        def cols = System.getenv("COLUMNS")
        if (cols != null) {
            return Integer.parseInt(cols, 10)
        }

        // tput requires $TERM to be set, otherwise it's going to print an error.
        // This unfortunately means this doesn't work in daemon mode.
        if (System.getenv("TERM") == null) {
            return fallback
        }

        // Totally unportable way of detecting the terminal width on POSIX and OS X.
        try {
            Process p = Runtime.getRuntime().exec([ "bash", "-c", "tput cols 2> /dev/tty" ] as String[])
            p.waitFor()
            def reader = new BufferedReader(new InputStreamReader(p.getInputStream()))
            def line = reader.readLine()?.trim()
            if (line != null) Integer.valueOf(line) else fallback
        } catch (IOException ignored) {
            fallback
        }
    }
}

package net.rdrei.android.buildtimetracker.reporters;

public class SysInfo {
    String getOSIdentifier() {
        ["os.name", "os.version", "os.arch"].collect { System.getProperty(it) }.join(" ")
    }

    long getMaxMemory() {
        MemoryUtil.physicalMemoryAvailable
    }

    String getCPUIdentifier() {
        def os = System.getProperty("os.name")
        if (os.equalsIgnoreCase("mac os x")) {
            def proc = ["sysctl", "-n", "machdep.cpu.brand_string"].execute()
            proc.waitFor()

            if (proc.exitValue() == 0) {
                return proc.in.text
            }
        } else if (os.equalsIgnoreCase("linux")) {
            def osName = ""
            new File("/proc/cpuinfo").eachLine {
                if (!osName.isEmpty()) return

                if (it.startsWith("model name")) {
                    osName = it.split(": ")[1]
                }
            }
            return osName
        }

        return ""
    }
}

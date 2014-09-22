package net.rdrei.android.buildtimetracker.reporters;

/**
 * This hackery doesn't work in Groovy, so we have to
 * implement this in Java.
 */
public class MemoryUtil {
    public static long getPhysicalMemoryAvailable() {
        com.sun.management.OperatingSystemMXBean bean =
                (com.sun.management.OperatingSystemMXBean)
                        java.lang.management.ManagementFactory.getOperatingSystemMXBean();
        return bean.getTotalPhysicalMemorySize();
    }
}

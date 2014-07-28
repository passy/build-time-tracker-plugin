package net.rdrei.android.buildtimetracker

import net.rdrei.android.buildtimetracker.internal.TimingsListener
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.StopExecutionException

class BuildTimeTrackerPlugin implements Plugin<Project> {
  final Logger log = Logging.getLogger BuildTimeTrackerPlugin

  @Override void apply(Project project) {
      project.getGradle().addBuildListener(new TimingsListener())
  }
}

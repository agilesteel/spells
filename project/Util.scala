import scala.util._
import scala.sys.process._

import sbt._

object Util {
  def styled(in: Any): String =
    scala.Console.CYAN + in + scala.Console.RESET

  def prompt(projectName: String): String =
    gitPrompt
      .fold(projectPrompt(projectName)) { g =>
        s"$g:${projectPrompt(projectName)}"
      }

  private def projectPrompt(projectName: String): String =
    s"sbt:${styled(projectName)}"

  def projectName(state: State): String =
    Project
      .extract(state)
      .currentRef
      .project

  private def gitPrompt: Option[String] =
    for {
      b <- branch.map(styled)
      h <- hash.map(styled)
    } yield s"git:$b:$h"

  private def branch: Option[String] =
    run("git rev-parse --abbrev-ref HEAD")

  private def hash: Option[String] =
    run("git rev-parse --short HEAD")

  private def run(command: String): Option[String] =
    Try(
      command
        .split(" ")
        .toSeq
        .!!(noopProcessLogger)
        .trim
    ).toOption

  private val noopProcessLogger: ProcessLogger =
    ProcessLogger(_ => (), _ => ())

  def SemVer(scalaVersion: String): (Int, Int, Int) = {
    val version = scalaVersion.split("\\.")

    val major = version.head.toInt
    val minor = version.tail.head.toInt
    val patch = version.tail.tail.head.toInt

    (major, minor, patch)
  }

  def `isScalaVersionSmallerThan 2.12`(semVer: (Int, Int, Int)): Boolean = {
    val (major, minor, patch) = semVer

    major == 2 && minor < 12
  }
}

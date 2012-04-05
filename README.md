[Java 7]: http://www.oracle.com/technetwork/java/javase/downloads/index.html
[SBT 0.11.2]: https://github.com/harrah/xsbt/wiki

# spells 1.0

This is a small scala "util" library, which will hopefully grow over time.

## Features:

* AnsiPrint - ansi styled outputs for your strings

## Setup

### Requirements 

* [Java 7]
* [SBT 0.11.2]
* Internet connection (in order for SBT to be able to download the necessary dependencies)

### Configuring your project's dependencies

SBT allows you to pull dependencies directly from github. Unfortunately the light build configuration is not sufficient for this. You have to use the full build configuration which might look like this:

	import sbt._
	import Keys._

	object MyBuild extends Build {
	   lazy val myProject = Project("My Project", file(".")) dependsOn spells
	   lazy val spells = RootProject(uri("git://github.com/agilesteel/spells.git"))
	}

### Usage

Here is an example of ``AnsiPrint`` in action:

	package myproject

	import spells._
	import Spells._
 
	object Ansi extends App {
	   println("white")
	   println("green")(Green)
	   println("green".green)
	   println("green".magenta)(Green)
	   println("yellow".yellow + "green".green + "yellow".yellow)(Yellow)
	   println("yellow" + "green".green)(Yellow)
	   println("yellow".yellow + "green")(Green)
	   println("yellow" + "green".green + "yellow")(Yellow)
	   println("yellow".yellow + "green" + "yellow".yellow)(Green)
	   println("yellow" + "green".green + "yellow".yellow)(Yellow)
	   println("yellow".yellow + "green".green + "yellow")(Yellow)
	   println("yellow" + "red".red + "yellow" + "green".green + "yellow")(Yellow)
	}

	object OberridingDefaultStyle extends App {
	   implicit val defaultStyle = Yellow
	   println("yellow" + "red".red + "yellow" + "green".green + "yellow")
	}
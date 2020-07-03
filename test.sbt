Test / parallelExecution := false
Test / testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-oSD")
Test / turbo := true

Test / testOptions +=
  Tests.Setup(UserConfigFileManager.createSpellsConfigFileForCurrentUser)

Test / testOptions +=
  Tests.Cleanup(UserConfigFileManager.deleteSpellsConfigFileForCurrentUser)

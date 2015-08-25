package spells.user

class DoubleEntryConfigKeeping extends UnitTestConfiguration {
  test("The spells library should contain a reference.conf file") {
    new java.io.File(getClass.getResource("/reference.conf").getFile) should exist
  }

  test("The reference.conf file should be valid") {
    (spells.coverage.`should-be-happy`: Boolean) should be(true)
    (spells.terminal.`width-in-characters`: Int) should be(160)
  }
}

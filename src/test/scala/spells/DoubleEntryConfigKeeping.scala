package spells

class DoubleEntryConfigKeeping extends UnitTestConfiguration {
  test("spells should contain a reference.conf file") {
    new java.io.File(getClass.getResource("/reference.conf").getFile) should exist
  }

  test("reference.conf file should be valid") {
    (spells.feature.`copy-file-path-to-clipboard-when-debug-printing`: Boolean) should be(false)
  }
}

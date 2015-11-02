package spells.user

class DoubleEntryConfigKeeping extends UnitTestConfiguration {
  test("The spells library should contain a reference.conf file") {
    new java.io.File(getClass.getResource("/reference.conf").getFile) should exist
  }

  test("The reference.conf file should be valid") {
    (spells.terminal.WidthInCharacters: Int) should be(160)

    (spells.XrayReport.DisplayDateTime: Boolean) should be(true)
    (spells.XrayReport.DisplayDuration: Boolean) should be(true)
    (spells.XrayReport.DisplayLocation: Boolean) should be(true)
    (spells.XrayReport.DisplayThread: Boolean) should be(true)
    (spells.XrayReport.DisplayClass: Boolean) should be(true)
    (spells.XrayReport.DisplayType: Boolean) should be(true)

    (spells.XrayReport.DescriptionStyle: spells.Ansi.Style) should be(Green)
    (spells.XrayReport.DateTimeStyle: spells.Ansi.Style) should be(Reset)
    (spells.XrayReport.DurationStyle: spells.Ansi.Style) should be(Reset)
    (spells.XrayReport.LocationStyle: spells.Ansi.Style) should be(Reset)
    (spells.XrayReport.ThreadStyle: spells.Ansi.Style) should be(Reset)
    (spells.XrayReport.ClassStyle: spells.Ansi.Style) should be(Reset)
    (spells.XrayReport.TypeStyle: spells.Ansi.Style) should be(Reset)
    (spells.XrayReport.ValueStyle: spells.Ansi.Style) should be(Magenta)
  }
}

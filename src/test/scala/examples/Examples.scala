package examples

import AnsiStyle._

case object `1_GettingStarted` extends App {
  println {
    s"""|
        |The best way to use spells is extending spells.Spells:
        |
        |-----------------------------------------
        |package company.project
        |
        |package object util ${"extends spells.SpellsModule".magenta}
        |-----------------------------------------
        |""".stripMargin
  }
}

case object `2_TheBasics` extends App {
  println {
    s"""|
        |The core abstraction of spells is called ${"AnsiStyle".magenta}.
        |Every String can be converted into an AnsiStyle like this:
        |
        |-----------------------------------------------
        |scala> "my awesome style".toAnsiStyle
        |res0: spells.AnsiStyle = my awesome stylesample
        |-----------------------------------------------
        |
        |Notice how spells attempts to provide a sample of the given style.
        |The list of all ansi codes can be found here ${"http://ascii-table.com/ansi-escape-sequences.php".yellow}.
        |Spells provides a couple of ansi codes out of the box, here is an example:
        |
        |-------------------------------
        |scala> Green
        |res0: spells.AnsiStyle = $Green
        |-------------------------------
        |""".stripMargin
  }
}

case object `3_UsingStyles` extends App {
  println {
    s"""|
        |Spells provides an implicit conversion from ${"Any".magenta} to ${"AnsiString".magenta},
        |which in turn provides a method called ${"in".magenta}, which expects an instance of ${"AnsiStyle".magenta} as its argument
        |and returns a ${"String".magenta} wrapped in the given style.
        |
        |This means that ${"in".magenta} can be called on an instance of any type and result in a ${"String".magenta}.
        |Here is an example:
        |
        |-------------------
        |scala> 1337 in Cyan
        |res0: String = ${1337.cyan}
        |-------------------
        |
        |Spells provides convenience methods out of the box, which call ${"in".magenta} behind the scenes for every ${"AnsiStyle".magenta}.
        |Here is the same example rewritten using one of the convenience methods:
        |
        |-------------------
        |scala> 1337.cyan
        |res0: String = ${1337.cyan}
        |-------------------
        |
        |If you had any trouble understanding this chapter please reread the previous one - ${`2_TheBasics`.yellow}.
        |""".stripMargin
  }
}

case object `4_ComposingStyles` extends App {
  println {
    s"""|
        |The tools shown in previous chapter - ${`3_UsingStyles`.yellow} are very convenient to use,
        |but their convenience has a price: they ${"override each other".red}. For instance:
        |
        |-----------------------------
        |scala> "example".green.yellow
        |res0: String = ${"example".yellow}
        |-----------------------------
        |
        |This behavior is very unfortunate, because you are rarely in control*
        |of the value you are about to apply a style to. Here is an example:
        |
        |-------------------------------------------------------------------------------------------
        |scala> val id = 4711
        |id: Int = 4711
        |
        |scala> val message = s"The id: $${id.green} is very important to me."
        |message: String = The id: ${4711.green} is very important to me.
        |
        |scala> message.cyan ${"// who would have thought that 'message' already contained an ansi code?".red}
        |res1: String = ${"The id: 4711 is very important to me.".cyan}
        |-------------------------------------------------------------------------------------------
        |
        |Fortunately spells provides a solution** in the form of a method called ${"styled".magenta}.
        |And here it is in action:
        |
        |----------------------------------------------------
        |scala> styled(message)(Cyan)
        |res2: String = ${styled(s"The id: ${4711.green} is very important to me.")(Cyan)}
        |----------------------------------------------------
        |
        |*  To be fair, you rarely need that much control.
        |** Be aware that ${"styled".magenta} has to traverse its input to do the magic trick.
        |
        |If you had any trouble understanding this chapter please reread the previous one - ${`3_UsingStyles`.yellow}.
        |""".stripMargin
  }
}

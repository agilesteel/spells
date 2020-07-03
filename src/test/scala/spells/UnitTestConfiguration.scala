package spells

import org.scalatest._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should

trait UnitTestConfiguration
    extends AnyFunSuite
       with should.Matchers
       with BeforeAndAfterEach
       with Inspectors

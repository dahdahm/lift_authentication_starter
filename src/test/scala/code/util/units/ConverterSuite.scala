package code.util.units

import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import Converter._

@RunWith(classOf[JUnitRunner])
class ConverterSuite extends FunSuite {

  test("parsing 3 as string returns 3") {
    assert(Some(3.0) === parse[Double]("3"))
  }

  test("parsing a non valid string returns empty option") {
    assert(None === parse[Double]("this will fail"))
  }
}

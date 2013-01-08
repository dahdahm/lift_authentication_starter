package code.util.units

import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import code.util.units.Bytes._

@RunWith(classOf[JUnitRunner])
class BytesSuite extends FunSuite {
  val exabyte = 1152921504606846976L
  val petabyte = 1125899906842624L
  val terabyte = 1099511627776L
  val gigabyte = 1073741824L
  val megabyte = 1048576L
  val kilobyte = 1024L
  val byte = 1L

  // format method:
  test("format 0 bytes returns 0 B") {
    assert("0 B" === format(0))
  }

  test("format negative number returns correctly formatted string") {
    assert("-123 B" === format(-123))
  }
  
  test("format negative number with multiple units present displays minus only once") {
    val negativeNumber = - (megabyte * 2 + kilobyte * 3 + byte * 987)
    assert("-2 MB, 3 kB, 987 B" === format(negativeNumber))
  }

  test("format less than 1kB returns bytes only") {
    assert("1023 B" === format(1023))
  }

  test("format more than 1kB returns kilobytes and bytes") {
    assert("1 kB, 476 B" === format(1500))
  }

  test("format works correctly with all units present up to EB") {
    val scarryNumber = exabyte + petabyte + terabyte + gigabyte + megabyte + kilobyte + byte
    assert("1 EB, 1 PB, 1 TB, 1 GB, 1 MB, 1 kB, 1 B" === format(scarryNumber))
  }

  test("format skips missing units correctly") {
    assert("1 EB, 1 TB, 1 kB" === format(exabyte + terabyte + kilobyte))
  }

  // formatRound method:
  test("formatRound 0 bytes returns 0 B") {
    assert("0 B" === formatRound(0))
  }

  test("formatRound negative number returns correctly formatted string") {
    assert("-123.0 B" === formatRound(-123))
  }
  
  test("formatRound negative number with multiple units present displays minus only once") {
    val negativeNumber = - (megabyte * 2 + kilobyte * 3 + byte * 987)
    assert("-2.01 MB" === formatRound(negativeNumber))
  }

  test("formatRound less than 1kB returns bytes only") {
    assert("1023.0 B" === formatRound(1023))
  }

  test("formatRound more than 1kB returns kilobytes and bytes rounded") {
    assert("1.47 kB" === formatRound(1500))
  }

  test("formatRound works correctly with all units present up to EB") {
    val scarryNumber = exabyte + petabyte + terabyte + gigabyte + megabyte + kilobyte + byte
    assert("1.01 EB" === formatRound(scarryNumber))
  }

  test("formatRound skips missing units correctly") {
    assert("1.01 EB" === formatRound(exabyte + terabyte + kilobyte))
  }
}
package code.util.units

import math._

/**
 * Utility class that performs various operations on transforming and
 * formatting integers representing bytes to human-readable Disk space.
 */
object Bytes {

  /**
   * Includes only types that fit in Long 2^63-1
   */
  val unitsInBytes: List[(String, Long)] = List(
    //("YB", pow(2, 83)),
    //("ZB", pow(2, 73)),
    ("EB", pow(2, 60).toLong),
    ("PB", pow(2, 50).toLong),
    ("TB", pow(2, 40).toLong),
    ("GB", pow(2, 30).toLong),
    ("MB", pow(2, 20).toLong),
    ("kB", pow(2, 10).toLong),
    ("B", 1))

  /**
   * Returns unit of measure for all other units.
   * There is no smaller unit then this one in application use.
   */
  def getSmallestUnitName =
    (unitsInBytes find (_._2 == 1)).get._1

  /**
   * Takes number of bytes and formats it to human readable string
   * separating GB, MB, KB, etc.
   * Returns all types of units present including bytes, so
   * no rounding is done.
   * For shorter representation with rounding to the nearest unit
   * see 'formatRound'.
   */
  def format(bytes: Long): String = {
    def format(n: Long, units: List[(String, Long)], separator: String): String =
      units match {
        case List() => n.toString + separator + getSmallestUnitName
        case (name, value) :: tail =>
          if (n > value) {
            val rem = n % value
            (n / value) + separator + name + (if (rem > 0) ", " + format(rem, tail, separator) else "")
          } else if (n == value)
            1 + separator + name
          else
            format(n, tail, separator)
      }
    if (bytes >= 0)
      format(bytes, unitsInBytes, " ")
    else
      "-" + format(abs(bytes), unitsInBytes, " ")
  }

  /**
   * Takes number of bytes and formats it to human readable string
   * rounding to the largest unit of measure (GB, MB, etc) of at
   * least one unit.
   * For longer representation without rounding see 'format' method.
   */
  def formatRound(bytes: Long): String = {
    def formatRound(n: Long, units: List[(String, Long)], separator: String): String =
      units match {
        case List() => n.toString + separator + getSmallestUnitName
        case (name, value) :: tail =>
          if (n >= value)
            round(n.toDouble / value) + separator + name
          else
            formatRound(n, tail, separator)
      }
    if (bytes >= 0)
      formatRound(bytes, unitsInBytes, " ")
    else
      "-" + formatRound(abs(bytes), unitsInBytes, " ")
  }

  /**
   * Rounds number to 2 decimal places. I didn't find such
   * facility in Scala at the moment.
   * TODO: replace with a proper library call, or implement
   * more precise solution (note ceil).
   */
  def round(number: Double): Double =
    ((number * 100).ceil / 100)

}
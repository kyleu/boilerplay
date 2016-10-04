package utils

object NumberUtils {
  private[this] val numFormatter = java.text.NumberFormat.getNumberInstance(java.util.Locale.US)

  def withCommas(i: Int) = numFormatter.format(i.toLong)
  def withCommas(l: Long) = numFormatter.format(l)
  def withCommas(d: Double) = numFormatter.format(d)

  def toWords(i: Int, properCase: Boolean = false) = {
    val ret = i match {
      case 0 => "zero"
      case 1 => "one"
      case 2 => "two"
      case 3 => "three"
      case 4 => "four"
      case 5 => "five"
      case 6 => "six"
      case 7 => "seven"
      case 8 => "eight"
      case 9 => "nine"
      case 10 => "ten"
      case 11 => "eleven"
      case 12 => "twelve"
      case _ => i.toString
    }
    if (properCase) {
      ret.head.toUpper + ret.tail
    } else {
      ret
    }
  }
}

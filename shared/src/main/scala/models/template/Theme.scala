package models.template

import enumeratum.values._

import scala.util.Random

sealed abstract class Theme(val value: String, val color: String) extends StringEnumEntry {
  override def toString: String = value
}

object Theme extends StringEnum[Theme] with StringCirceEnum[Theme] {
  case object Red extends Theme("red", "#c62828")
  case object Pink extends Theme("pink", "#d81b60")
  case object Purple extends Theme("purple", "#8e24aa")
  case object DeepPurple extends Theme("deeppurple", "#4527a0")
  case object Indigo extends Theme("indigo", "#3949ab")
  case object Blue extends Theme("blue", "#1976d2")
  case object LightBlue extends Theme("lightblue", "#039be5")
  case object Cyan extends Theme("cyan", "#00acc1")
  case object Teal extends Theme("teal", "#00897b")
  case object Green extends Theme("green", "#388e3c")
  case object LightGreen extends Theme("lightgreen", "#7cb342")
  case object Yellow extends Theme("yellow", "#fdd835")
  case object Amber extends Theme("amber", "#ffa000")
  case object Orange extends Theme("orange", "#f57c00")
  case object DeepOrange extends Theme("deeporange", "#f4511e")
  case object Brown extends Theme("brown", "#6d4c41")
  case object Grey extends Theme("grey", "#757575")
  case object BlueGrey extends Theme("bluegrey", "#607d8b")
  case object Black extends Theme("black", "#333333")

  def getRandom = values(Random.nextInt(values.size))
  override val values = findValues
}

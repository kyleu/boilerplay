package com.kyleu.projectile.util

object NumberUtils {
  def withCommas(i: Int) = i.toString.reverse.grouped(3).mkString(",").reverse
  def withCommas(l: Long) = l.toString.reverse.grouped(3).mkString(",").reverse
}

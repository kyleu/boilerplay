package models.test

case class Tree[T](node: T, children: Seq[Tree[T]] = Nil) {
  def toSeq(indentionLevel: Int = 0): Seq[(Int, T)] = (indentionLevel, node) +: children.flatMap(_.toSeq(indentionLevel + 1))
}

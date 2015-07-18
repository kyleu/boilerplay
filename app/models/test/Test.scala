package models.test

case class Test(id: String, run: () => Any = () => ()) {
  def toTree = Tree(this)
}

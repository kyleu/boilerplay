package services.test

import models.test.{ Test, Tree }

class AllTests {
  val all = Tree(Test("all"), Seq(
    new BasicTests().all
  ))
}

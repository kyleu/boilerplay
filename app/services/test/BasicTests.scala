package services.test

import models.test.Test

class BasicTests {
  val all = Test("basic", () => {
    assert("X" == "X")
    assert(1 == 1)
    assert(true)
  }).toTree
}

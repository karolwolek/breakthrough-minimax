package math

import org.scalatest.funsuite.AnyFunSuite

class MathUtilsTests extends AnyFunSuite:

  test("'double' should hanle 0") {
    val result = MathUtils.double(0)
    assert(result == 0)
  }

  test("'double' should hanle 1") {
    val result = MathUtils.double(1)
    assert(result == 2)
  }

  test("test with Int.MaxValue") (pending)

end MathUtilsTests

package option
import org.junit._
import Assert._

class TestSuite {

  @Test def testArgs1() {
    val opts = new GetOpts("abc=i=m", "isSet=b=m")
    opts.parse("-abc 13 -isSet");
    assertEquals(opts.getOpt("abc").value, 13)
    assertEquals(opts.getOpt("isSet").value, true)
  }

  /**
   * missing mandatory option
   */
  @Test def testArgs2() {
    val opts = new GetOpts("abc=i", "isSet=b=m", "path=s")
    opts.parse("-path a/b");
    assertEquals(opts.getOpt("path").value, "a/b")
  }

  @Test def testArgs3() {
    val opts = new GetOpts("abc=i", "isSet=b", "path=s", "b2=b")
    opts.parse("-path a/b -b2 -abc 33");
    assertEquals(opts.getOpt("path").value, "a/b")
    assertEquals(opts.isDefineOpt("isSet"), false)
    assertEquals(opts.getOpt("abc").value, 33)
    assertEquals(opts.getOpt("b2").value, true)
  }

  /**
   * option undefined
   */
  @Test def testArgs4() {
    val opts = new GetOpts("abc=i", "isSet=b", "path=s", "b2=b")
    opts.parse("-path a/b -b2 -abc 33");
    assertEquals(opts.getOpt("path").value, "a/b")
    assertEquals(opts.isDefineOpt("isSet"), false)
    try {
      assertEquals(opts.getOpt("isSet").value, true)
    } catch {
      case ex: UsageError => print(ex.msg)
      case _ => fail("unexpect exception");
    }
  }

  /**
   * input arguments format error
   */
  @Test def testArgs5() {
    val opts = new GetOpts("abc=i", "isSet=b", "path=s", "b2=b")
    try {
      opts.parse("-path a/b -b2 -abc 33 sdf");
    } catch {
      case ex: UsageError =>
      case _ => fail("unpect exception");
    }
  }

  /**
   * float type
   */
  @Test def testArgs6() {
    val opts = new GetOpts("abc=i", "isSet=b", "path=s", "f0=f")
    opts.parse("-path a/b -b2 -abc 33 -f0 3.4");
    assertEquals(opts.getOpt("f0").value, 3.4F)
  }
}
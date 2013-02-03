package option.example

import option._

object HelloWorld {
  def main(args: Array[String]) {
    val opt = new GetOpts("times=i=m", "words=s");
    opt.parse(args);
    val times = opt.getOpt("times").asInstanceOf[IntArg].value;
    var words = "hello, world!"
    if (opt.isDefineOpt("words")) {
      words = opt.getOpt("words").asInstanceOf[StringArg].value
    }
    for (i <- 1 to times) {
      println(words)
    }
  }
} 
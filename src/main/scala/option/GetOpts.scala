package option
import scala.collection.mutable.OpenHashMap
import scala.collection.mutable.ArrayStack
import scala.collection.immutable.HashMap
import scala.collection.immutable.HashSet
import java.util.ArrayList

case class DesignError(msg: String) extends Error(msg)
case class UsageError(msg: String) extends Error(msg)

trait Arg {
  type ValueType
  val value: ValueType
}

case class IntArg(val v: String) extends Arg {
  type ValueType = Int
  val value: ValueType = parseIntValue(v)

  private def parseIntValue(v: String): Int = {
    try {
      Integer.parseInt(v)
    } catch {
      case nfe: NumberFormatException => throw DesignError("number format exception")
    }
  }
}

case class FloatArg(val v: String) extends Arg {
  type ValueType = Float
  val value: ValueType = parseFloatValue(v)

  private def parseFloatValue(v: String): Float = {
    try {
      v.toFloat;
    } catch {
      case nfe: NumberFormatException => throw DesignError("number format exception")
    }
  }
}

case class StringArg(val v: String) extends Arg {
  type ValueType = String
  val value: ValueType = v
}

case class BooleanArg() extends Arg {
  type ValueType = Boolean
  val value: ValueType = true
}

class GetOpts(val patterns: String*) {
  private val options = new OpenHashMap[String, String];
  private val mandatory = new HashSet[String];
  for (patt <- patterns) {
    parsePattern(patt)
  }
  var optsPair = new HashMap[String, Arg];

  def parse(args: Array[String]): Unit = {
    val optionsStack = new ArrayStack[String];
    val optKeys = new HashSet[String];
    optionsStack ++= args.reverse;
    while (!optionsStack.isEmpty) {

      var key = optionsStack.pop()
      key = checkKey(key)
      optKeys.+(key)
      val value = options.getOrElse(key, "")
      parseValue(key, value);
    }
    checkMandatory()

    def parseValue(key: String, tpe: String) = {
      if ("s".equals(tpe)) {
        optsPair += key -> StringArg(optionsStack.pop());
      } else if ("i".equals(tpe)) {
        optsPair += key -> IntArg(optionsStack.pop());
      } else if ("b".equals(tpe)) {
        optsPair += key -> BooleanArg()
      } else if ("f".equals(tpe)) {
        optsPair += key -> FloatArg(optionsStack.pop())
      }
    }

    def checkMandatory(): Unit = {
      for (item <- mandatory) {
        if (optKeys.contains(item)) {
          throw UsageError("missing mandatory options " + item)
        }
      }
    }

    def checkKey(key: String): String = {
      if (!key.startsWith("-")) {
        throw UsageError("option %s should start with -".format(key));
      }
      key.substring(1)
    }
  }
  def parse(args: String): Unit = {
    parse(args.split("\\s+"));
  }

  def isDefineOpt(name: String): Boolean = {
    optsPair.contains(name);
  }

  def getOpt(name: String): Arg = {
    if (!isDefineOpt(name)) {
      throw UsageError("option %s undefined.".format(name))
    }
    optsPair.get(name).get
  }

  private def parsePattern(pat: String) {
    def designError(msg: String) = {
      throw new DesignError(msg);
    }
    val keyValue = pat.split("=");
    if (keyValue.length == 3) {
      mandatory.+(keyValue(0))
    }
    options += keyValue(0) -> keyValue(1)
  }
}
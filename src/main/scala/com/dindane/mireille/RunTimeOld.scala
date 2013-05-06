package main.scala.com.dindane.mireille

import java.lang.invoke._

object RunTimeOld {

  val FALLBACK = MethodHandles.lookup().findVirtual(classOf[InliningCacheCallSite],
    "fallback",
    MethodType.methodType(classOf[Object], classOf[Array[Object]]))

  var callsCounter = 0

  def bootstrap(lookUp: MethodHandles.Lookup, methodName: String, methodType: MethodType): CallSite = {
    new InliningCacheCallSite(lookUp, methodName, methodType)
  }

  Runtime.getRuntime.addShutdownHook(new Thread {
    override def run {
      System.out.println("\nI'm not a fish, I'm a man")
      System.out.println("Hook, in, mouth!\n")
      System.out.println(callsCounter)
    }
  })

  class InliningCacheCallSite(val lookUp: MethodHandles.Lookup, val methodName: String, val methodType: MethodType)
    extends MutableCallSite(methodType) {

    val fallbackMethodHandle: MethodHandle = FALLBACK
      .bindTo(this)
      .asCollector(classOf[Array[Object]], methodType.parameterCount())
      .asType(methodType)

    setTarget(fallbackMethodHandle)

    def fallback(args: Array[Object]) = {
      callsCounter += 1
      try {
        System.out.print("Object: ")
        System.out.println(args(0))
        System.out.print("Method name: ")
        System.out.println(methodName)
        if (args(1) != null) {
          System.out.print("Argument: ")
          System.out.println(args(1))
        }
      } catch { case _: Throwable => () }
      System.out.println("=====")

      val method = lookUp.findVirtual(args(0).getClass,
        methodName,
        methodType.dropParameterTypes(0, 1)).asType(methodType)

      method.invokeWithArguments(args: _*)
    }
  }

}

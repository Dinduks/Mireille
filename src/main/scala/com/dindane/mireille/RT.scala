package main.scala.com.dindane.mireille

import java.lang.invoke._

object RT {

  val FALLBACK = MethodHandles.lookup().findVirtual(classOf[InliningCacheCallSite],
    "fallback",
    MethodType.methodType(classOf[Object], classOf[Array[Object]]))

  def bootstrap(lookUp: MethodHandles.Lookup, methodName: String, methodType: MethodType): CallSite = {
    new InliningCacheCallSite(lookUp, methodName, methodType)
  }

  class InliningCacheCallSite(val lookUp: MethodHandles.Lookup, val methodName: String, val methodType: MethodType)
    extends MutableCallSite(methodType) {

    val fallbackMethodHandle: MethodHandle = FALLBACK
      .bindTo(this)
      .asCollector(classOf[Array[Object]], methodType.parameterCount())
      .asType(methodType)

    setTarget(fallbackMethodHandle)

    def fallback(args: Array[Object]) = {
      val method = lookUp.findVirtual(args(0).getClass,
        methodName,
        methodType.dropParameterTypes(0, 1)).asType(methodType)

      method.invokeWithArguments(args: _*)
    }
  }

}

package main.scala.com.dindane.mireille

import java.lang.invoke.{CallSite, MethodType, MethodHandles}

object RT {

    def bootstrap(lookUp: MethodHandles.Lookup, methodName: String, methodType: MethodType): CallSite = {
      throw new BootstrapMethodError();
    }

}

package test.scala.com.dindane.mireille

import org.specs2.mutable._
import java.nio.file._
import main.scala.com.dindane.mireille.Reader
import org.objectweb.asm.Type


class ReaderSpec extends Specification {
  "Reader.getInvokeVirtualInfo" should {
    "return a Seq whose size is the number of INVOKEVIRTUAL calls" in {
      val path: Path = Paths.get(System.getProperty("user.dir"))
        .resolve("src/test/scala/com/dindane/mireille/resources/A.class")
      val invokeVirtualCalls = Reader.getInvokeVirtualCalls(path)

      invokeVirtualCalls must be size(4)
    }

    "return InvokeVirtualCall objects with the correct information" in {
      val path: Path = Paths.get(System.getProperty("user.dir"))
        .resolve("src/test/scala/com/dindane/mireille/resources/A.class")
      val invokeVirtualCalls = Reader.getInvokeVirtualCalls(path)

      invokeVirtualCalls(0).className must_== "foo/A"
      invokeVirtualCalls(0).methodName must_== "bar"
      invokeVirtualCalls(0).arguments(0).getInternalName must_== "java/lang/String"
      invokeVirtualCalls(0).returnType.getInternalName must_== "java/lang/String"
      invokeVirtualCalls(0).owner must_== "foo/A"

      invokeVirtualCalls(1).methodName must_== "foo"
      invokeVirtualCalls(1).arguments(0) must_== Type.INT_TYPE
      invokeVirtualCalls(1).arguments(1) must_== Type.FLOAT_TYPE
      invokeVirtualCalls(1).returnType must_== Type.BOOLEAN_TYPE
      invokeVirtualCalls(1).owner must_== "foo/B"

      invokeVirtualCalls(2).methodName must_== "bar"
      invokeVirtualCalls(2).arguments(0).getInternalName must_== "java/lang/String"
      invokeVirtualCalls(2).returnType.getInternalName must_== "java/lang/String"
      invokeVirtualCalls(2).owner must_== "foo/A"

      invokeVirtualCalls(3).methodName must_== "toString"
      invokeVirtualCalls(3).returnType.getInternalName must_== "java/lang/String"
      invokeVirtualCalls(3).owner must_== "java/lang/Object"
    }
  }
}

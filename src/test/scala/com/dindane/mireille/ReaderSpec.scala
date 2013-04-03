package test.scala.com.dindane.mireille

import org.specs2.mutable._
import java.nio.file._
import main.scala.com.dindane.mireille.Reader
import org.objectweb.asm.Type


class ReaderSpec extends Specification {
  "Reader.getInvokeDynamicInfo" should {
    "return a Seq whose size is the number of INVOKEDYNAMIC calls" in {
      val path: Path = Paths.get(System.getProperty("user.dir"))
        .resolve("src/test/scala/com/dindane/mireille/resources/core.class")
      val invokeDynamicCalls = Reader.getInvokeDynamicCalls(Files.newInputStream(path, StandardOpenOption.READ))

      invokeDynamicCalls must be size(40)
    }

    "return invokeDynamicCalls objects with the correct information" in {
      val path: Path = Paths.get(System.getProperty("user.dir"))
        .resolve("src/test/scala/com/dindane/mireille/resources/core.class")
      val invokeDynamicCalls = Reader.getInvokeDynamicCalls(Files.newInputStream(path, StandardOpenOption.READ))

      invokeDynamicCalls(0).className must_== "ruby/core/core"
      invokeDynamicCalls(0).file.get must_== "core"

      invokeDynamicCalls(36).methodName must_== "+"
      invokeDynamicCalls(36).arguments.size must_== 1
      invokeDynamicCalls(36).arguments(0).toString must_== "Ljava/lang/Object;"
      invokeDynamicCalls(36).returnType.toString must_== "V"
      invokeDynamicCalls(36).lineNumber.get must_== 83

      invokeDynamicCalls(36).bootstrapMethod.getOwner must_== "emerald/rt/RT"
      invokeDynamicCalls(36).bootstrapMethod.getName  must_== "bsm_declare_predefined_method"
    }
  }

  "Reader.getInvokeVirtualInfo" should {
    "return a Seq whose size is the number of INVOKEVIRTUAL calls" in {
      val path: Path = Paths.get(System.getProperty("user.dir"))
        .resolve("src/test/scala/com/dindane/mireille/resources/A.class")
      val invokeVirtualCalls = Reader.getInvokeVirtualCalls(Files.newInputStream(path, StandardOpenOption.READ))

      invokeVirtualCalls must be size(4)
    }

    "return InvokeVirtualCall objects with the correct information" in {
      val path: Path = Paths.get(System.getProperty("user.dir"))
        .resolve("src/test/scala/com/dindane/mireille/resources/A.class")
      val invokeVirtualCalls = Reader.getInvokeVirtualCalls(Files.newInputStream(path, StandardOpenOption.READ))

      invokeVirtualCalls(0).className must_== "foo/A"
      invokeVirtualCalls(0).file.get must_== "main.java"

      invokeVirtualCalls(0).methodName must_== "bar"
      invokeVirtualCalls(0).arguments(0).getInternalName must_== "java/lang/String"
      invokeVirtualCalls(0).returnType.getInternalName must_== "java/lang/String"
      invokeVirtualCalls(0).owner must_== "foo/A"
      invokeVirtualCalls(0).lineNumber.get must_== 4

      invokeVirtualCalls(1).methodName must_== "foo"
      invokeVirtualCalls(1).arguments(0) must_== Type.INT_TYPE
      invokeVirtualCalls(1).arguments(1) must_== Type.FLOAT_TYPE
      invokeVirtualCalls(1).returnType must_== Type.BOOLEAN_TYPE
      invokeVirtualCalls(1).owner must_== "foo/B"
      invokeVirtualCalls(1).lineNumber.get must_== 9

      invokeVirtualCalls(2).methodName must_== "bar"
      invokeVirtualCalls(2).arguments(0).getInternalName must_== "java/lang/String"
      invokeVirtualCalls(2).returnType.getInternalName must_== "java/lang/String"
      invokeVirtualCalls(2).owner must_== "foo/A"
      invokeVirtualCalls(2).lineNumber.get must_== 10

      invokeVirtualCalls(3).methodName must_== "toString"
      invokeVirtualCalls(3).returnType.getInternalName must_== "java/lang/String"
      invokeVirtualCalls(3).owner must_== "java/lang/Object"
      invokeVirtualCalls(3).lineNumber.get must_== 14
    }
  }
}

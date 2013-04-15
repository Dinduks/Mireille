package test.scala.com.dindane.mireille

import org.specs2.mutable._
import java.nio.file._
import main.scala.com.dindane.mireille.{Util, Reader}
import org.objectweb.asm.Type

class UtilSpec extends Specification {

  "Util.getPackageNameFromFQCN" should {
    "return the package name" in {
      Util.getPackageNameFromFQCN("foo/class") must_== "foo"
      Util.getPackageNameFromFQCN("foo/bar/class") must_== "foo/bar"
    }
  }

  "Util.getClassNameFromFQCN" should {
    "return the class name" in {
      Util.getClassNameFromFQCN("foo/class") must_== "class"
      Util.getClassNameFromFQCN("foo/bar/class") must_== "class"
    }
  }

}

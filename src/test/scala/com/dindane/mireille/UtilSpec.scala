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

  "Util.changeFileName" should {
    "return the changed filename" in {
      val path1: Path = Paths.get("/foo/bar/baz")
      val path2: Path = Paths.get("/foo")
      val path3: Path = Paths.get("foo")

      Util.changeFileName(path1, "bla").toString must_== "/foo/bar/bla"
      Util.changeFileName(path2, "bar").toString must_== "/bar"
      Util.changeFileName(path3, "bar").toString must_== "bar"
    }
  }

}

package main.scala.com.dindane.mireille

import java.nio.file.{Paths, Path}

/**
 * An utility object used to do various repetitive tasks
 */
object Util {

  def getClassNameFromFQCN(fqcn: String): String = fqcn.split('/').last
  def getPackageNameFromFQCN(fqcn: String): String = fqcn.substring(0, fqcn.lastIndexOf("/"));

  def changeFileName(path: Path, newFileName: String): Path = {
    if (path.getParent != null) // Aaaaaah Java...
      path.getParent.resolve(newFileName)
    else
      Paths.get(newFileName)
  }

  def getStringWithoutPrefix(prefix: String, string: String): String = {
    if (prefix(0) != string(0)) throw new Exception("The strings don't have a common prefix.")
    string.substring(prefix.size + 0)
  }

}

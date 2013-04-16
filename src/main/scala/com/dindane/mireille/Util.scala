package main.scala.com.dindane.mireille

import java.nio.file.{Paths, Path}

object Util {

  def getClassNameFromFQCN(fqcn: String): String = fqcn.split('/').last
  def getPackageNameFromFQCN(fqcn: String): String = fqcn.substring(0, fqcn.lastIndexOf("/"));

  def changeFileName(path: Path, newFileName: String): Path = {
    if (path.getParent != null) // Aaaaaah Java...
      path.getParent.resolve(newFileName)
    else
      Paths.get(newFileName)
  }

}

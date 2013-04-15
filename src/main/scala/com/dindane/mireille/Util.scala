package main.scala.com.dindane.mireille

object Util {

  def getClassNameFromFQCN(fqcn: String): String = fqcn.split('/').last
  def getPackageNameFromFQCN(fqcn: String): String = fqcn.substring(0, fqcn.lastIndexOf("/"));

}

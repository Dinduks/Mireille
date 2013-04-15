package main.scala.com.dindane.mireille

object Util {

  def getClassNameFromFQCN(fqcn: String): String = fqcn.split('/').last
  def getPackageNameFromFQCN(fqcn: String): String = fqcn.split('/').take(fqcn.split('/').size - 1).mkString("/")

}

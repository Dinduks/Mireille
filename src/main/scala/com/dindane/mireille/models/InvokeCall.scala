package main.scala.com.dindane.mireille.models

import org.objectweb.asm.Type

case class InvokeCall (
  val className: String,
  val methodName: String,
  val owner: String,
  val arguments: Seq[Type],
  val returnType: Type,
  val file: Option[String],
  val lineNumber: Option[Int]
)
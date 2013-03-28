package main.scala.com.dindane.mireille.models

import org.objectweb.asm._

case class InvokeDynamicCall(
  className: String,
  methodName: String,
  arguments: Seq[Type],
  returnType: Type,
  file: Option[String],
  lineNumber: Option[Int],
  bootstrapMethod: Handle
)
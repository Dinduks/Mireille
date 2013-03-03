package main.scala.com.dindane.mireille.models

import org.objectweb.asm.Type

case class InvokeVirtualCall(
  className: String,
  methodName: String,
  owner: String,
  arguments: Seq[Type],
  returnType: Type,
  file: String,
  lineNumber: Int
)
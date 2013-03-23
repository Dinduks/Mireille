package main.scala.com.dindane.mireille.models

import org.objectweb.asm.Type

class InvokeVirtualCall(
  className: String,
  methodName: String,
  owner: String,
  arguments: Seq[Type],
  returnType: Type,
  file: Option[String],
  lineNumber: Option[Int]
) extends InvokeCall(
  className,
  methodName,
  owner,
  arguments,
  returnType,
  file,
  lineNumber
)

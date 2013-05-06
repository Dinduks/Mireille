package main.scala.com.dindane.mireille.models

import org.objectweb.asm.Type

/**
 * A model representing an Invoke Virtual Call
 *
 * @param className
 * @param methodName
 * @param owner
 * @param arguments
 * @param returnType
 * @param file
 * @param lineNumber
 */
case class InvokeVirtualCall(
  className: String,
  methodName: String,
  owner: String,
  arguments: Seq[Type],
  returnType: Type,
  file: Option[String],
  lineNumber: Option[Int]
)
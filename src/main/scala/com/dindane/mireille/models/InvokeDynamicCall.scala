package main.scala.com.dindane.mireille.models

import org.objectweb.asm._

/**
 * A model representing an Invoke Dynamic Call
 *
 * @param className
 * @param methodName
 * @param arguments
 * @param returnType
 * @param file
 * @param lineNumber
 * @param bootstrapMethod
 */
case class InvokeDynamicCall(
  className: String,
  methodName: String,
  arguments: Seq[Type],
  returnType: Type,
  file: Option[String],
  lineNumber: Option[Int],
  bootstrapMethod: Handle
)
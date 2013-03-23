package main.scala.com.dindane.mireille.visitors

import org.objectweb.asm._

class InvokeVirtualVisitor(className: String) extends InvokeVisitor(className, Opcodes.INVOKEVIRTUAL)

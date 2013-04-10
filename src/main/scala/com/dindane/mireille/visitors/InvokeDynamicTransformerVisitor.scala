package main.scala.com.dindane.mireille.visitors

import org.objectweb.asm.{ClassWriter, MethodVisitor, ClassVisitor, Opcodes}

class InvokeDynamicTransformerVisitor(className: String, classWriter: ClassWriter)
  extends ClassVisitor(Opcodes.ASM4, classWriter) {
//  override def visitMethod(access: Int, name: String, desc: String, signature: String, exceptions: Array[String]) = {
//    new MethodVisitor(Opcodes.ASM4) {
//
//    }
//  }
}
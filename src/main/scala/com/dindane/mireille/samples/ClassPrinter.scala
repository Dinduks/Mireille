package com.dindane.mireille.samples

import org.objectweb.asm._

class ClassPrinter extends ClassVisitor(Opcodes.ASM4) {

  override def visit(version: Int, access: Int, name: String, signature: String, superName: String, interfaces: Array[String]) {
    println(name + " extends " + superName + " {")
  }

  override def visitSource(source: String, debug: String) {
  }

  override def visitOuterClass(owner: String, name: String, desc: String) {
  }

  override def visitAnnotation(desc: String, visible: Boolean): AnnotationVisitor = {
    return null
  }

  override def visitAttribute(attr: Attribute) {
  }

  override def visitInnerClass(name: String, outerName: String, innerName: String, access: Int) {
  }

  override def visitField(access: Int, name: String, desc: String, signature: String, value: Any): FieldVisitor = {
    println(" " + desc + " " + name)
    return null
  }

  override def visitMethod(access: Int, name: String, desc: String, signature: String, exceptions: Array[String]): MethodVisitor = {
    println(" " + name + desc)
    return null
  }

  override def visitEnd {
    println("}")
  }

}

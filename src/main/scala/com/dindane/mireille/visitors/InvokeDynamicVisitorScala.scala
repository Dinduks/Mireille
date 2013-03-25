package main.scala.com.dindane.mireille.visitors

import collection._
import org.objectweb.asm._
import main.scala.com.dindane.mireille.models.InvokeDynamicCall

class InvokeDynamicVisitorScala(className: String) extends ClassVisitor(Opcodes.ASM4) {

  val invokeDynamicCalls: mutable.MutableList[InvokeDynamicCall] = mutable.MutableList.empty
  private var fileName: Option[String] = None

  override def visitSource(source: String, debug: String) {
    fileName = Some(source)
  }

  override def visitMethod(access: Int, name: String, description: String, signature: String, exceptions: Array[String]): MethodVisitor = {
    return new MethodVisitor(Opcodes.ASM4) {
      private var lineNumber: Option[Int] = None

      override def visitLineNumber(line: Int, startLabel: Label) {
        lineNumber = Some(line)
        println("== lineNumber %d".format(line))

        super.visitLineNumber(line, startLabel)
      }


      override def visitInvokeDynamicInsn(name: String, desc: String, bsm: Handle, bsmArgs: Object*) = {
        println("===== INVOKEDYNAMIC call in the %s method".format(name))
      }
    }
  }

}

package main.scala.com.dindane.mireille.visitors

import collection._
import org.objectweb.asm._
import main.scala.com.dindane.mireille.models.InvokeDynamicCall

/**
 * A class visitor that visits a Java class and stores its Invoke Dynamic calls
 * in the <code>invokeDynamicCalls</code> List
 *
 * @param className The class to be visited
 */
class InvokeDynamicVisitor(className: String) extends ClassVisitor(Opcodes.ASM4) {

  val invokeDynamicCalls: mutable.MutableList[InvokeDynamicCall] = mutable.MutableList.empty
  private var fileName: Option[String] = None

  override def visitSource(source: String, debug: String) {
    fileName = Some(source)
  }

  override def visitMethod(access: Int, name: String, description: String, signature: String, exceptions: Array[String]): MethodVisitor = {
    new MethodVisitor(Opcodes.ASM4) {
      private var lineNumber: Option[Int] = None

      override def visitLineNumber(line: Int, startLabel: Label) {
        lineNumber = Some(line)
      }

      override def visitInvokeDynamicInsn(name: String, desc: String, bsm: Handle, bsmArgs: Object*) {
        invokeDynamicCalls += InvokeDynamicCall(className,
          name,
          Type.getArgumentTypes(description),
          Type.getReturnType(description),
          fileName,
          lineNumber,
          bsm)
      }
    }
  }

}

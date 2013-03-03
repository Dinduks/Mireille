package main.scala.com.dindane.mireille.visitors

import collection._
import org.objectweb.asm._
import main.scala.com.dindane.mireille.models.InvokeVirtualCall

class InvokeVirtualVisitor(className: String) extends ClassVisitor(Opcodes.ASM4) {

  val invokeVirtualCalls: mutable.MutableList[InvokeVirtualCall] = mutable.MutableList.empty
  private var fileName: Option[String] = None

  override def visitSource(source: String, debug: String) {
    fileName = Some(source)
  }

  override def visitMethod(access: Int, name: String, description: String, signature: String, exceptions: Array[String]): MethodVisitor = {
    new MethodVisitor(Opcodes.ASM4, super.visitMethod(access, name, description, signature, exceptions)) {
      override def visitMethodInsn(opcode: Int, owner: String, name: String, description: String) = {
        if (opcode == Opcodes.INVOKEVIRTUAL) {
          invokeVirtualCalls += InvokeVirtualCall(className,
            name,
            owner,
            Type.getArgumentTypes(description),
            Type.getReturnType(description),
            fileName.getOrElse(""),
            0)
        }
      }
    }
  }

}

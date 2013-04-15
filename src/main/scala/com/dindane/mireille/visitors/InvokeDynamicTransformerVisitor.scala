package main.scala.com.dindane.mireille.visitors

import org.objectweb.asm._
import java.lang.invoke.{MethodHandle, CallSite, MethodHandles, MethodType}
import main.scala.com.dindane.mireille.RT

class InvokeDynamicTransformerVisitor(className: String, classVisitor: ClassVisitor)
  extends ClassVisitor(Opcodes.ASM4, classVisitor) {

  override def visitMethod(access: Int, name: String, desc: String, signature: String, exceptions: Array[String]) = {
    val methodVisitor = classVisitor.visitMethod(access, name, desc, signature, exceptions)
    new InvokeDynamicTransformerAdapter(methodVisitor)
  }

}

class InvokeDynamicTransformerAdapter(methodVisitor: MethodVisitor)
  extends MethodVisitor(Opcodes.ASM4, methodVisitor) {

  override def visitMethodInsn(opcode: Int, owner: String, name: String, description: String) {
    if (opcode == Opcodes.INVOKEVIRTUAL) {
      val bsm = new Handle(
        Opcodes.H_INVOKESTATIC,
        "main/scala/com/dindane/mireille/RT",
        "bootstrap",
        // TODO: Use .type
        MethodType.methodType(classOf[CallSite], classOf[MethodHandles.Lookup], classOf[String], classOf[MethodType])
          .toMethodDescriptorString
      )
      // TODO: Detect if owner is an array
      super.visitInvokeDynamicInsn(name, "(L" + owner + ';' + description.substring(1), bsm)
    } else {
      super.visitMethodInsn(opcode, owner, name, description)
    }
  }

}

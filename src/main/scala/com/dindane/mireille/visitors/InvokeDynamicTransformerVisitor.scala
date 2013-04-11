package main.scala.com.dindane.mireille.visitors

import org.objectweb.asm._

class InvokeDynamicTransformerVisitor(className: String, classVisitor: ClassVisitor)
  extends ClassVisitor(Opcodes.ASM4, classVisitor) {

  var isStaticInitPatched: Boolean = false

  override def visitMethod(access: Int, name: String, desc: String, signature: String, exceptions: Array[String]) = {
    val methodVisitor = classVisitor.visitMethod(access, name, desc, signature, exceptions)
    new InvokeDynamicTransformerAdapter(methodVisitor)
  }

}

class InvokeDynamicTransformerAdapter(methodVisitor: MethodVisitor)
  extends MethodVisitor(Opcodes.ASM4, methodVisitor) {

  override def visitMethodInsn(opcode: Int, owner: String, name: String, description: String) {
    if (opcode == Opcodes.INVOKEVIRTUAL) {
      val bsm = new Handle(Opcodes.H_INVOKESTATIC, owner, name, description)
      super.visitInvokeDynamicInsn(name, description, bsm)
    } else {
      super.visitMethodInsn(opcode, owner, name, description)
    }
  }

}

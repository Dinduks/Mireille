package main.scala.com.dindane.mireille.visitors

import org.objectweb.asm._
import java.lang.invoke.{CallSite, MethodType}
import java.lang.invoke.MethodHandles.Lookup
import main.scala.com.dindane.mireille.Util

class InvokeDynamicTransformerVisitor(className: String, classVisitor: ClassVisitor)
  extends ClassVisitor(Opcodes.ASM4, classVisitor) {

  override def visit(version: Int, access: Int, name: String, signature: String, superName: String, interfaces: Array[String]) {
    val newName: String = Util.getPackageNameFromFQCN(name) + "/" + "Indy" + Util.getClassNameFromFQCN(name)

    super.visit(version, access, newName, signature, superName, interfaces)
  }

  override def visitMethod(access: Int, name: String, desc: String, signature: String, exceptions: Array[String]) = {
    val methodVisitor = classVisitor.visitMethod(access, name, desc, signature, exceptions)
    new InvokeDynamicTransformerAdapter(methodVisitor)
  }

}

class InvokeDynamicTransformerAdapter(methodVisitor: MethodVisitor)
  extends MethodVisitor(Opcodes.ASM4, methodVisitor) {

  override def visitMethodInsn(opcode: Int, owner: String, name: String, description: String) {
    if (opcode == Opcodes.INVOKEVIRTUAL || opcode == Opcodes.INVOKEINTERFACE) {
      val methodType: MethodType = MethodType.methodType(classOf[CallSite], classOf[Lookup], classOf[String],
        classOf[MethodType])

      val bootstrapMethod = new Handle(Opcodes.H_INVOKESTATIC,
        "main/scala/com/dindane/mireille/RT",
        "bootstrap",
        methodType.toMethodDescriptorString)

      val prefix = if (owner(0) == '[') { "(" + owner } else { "(L" + owner + ';' }
      val newDescription = prefix + description.substring(1)

      super.visitInvokeDynamicInsn(name, newDescription, bootstrapMethod)
    } else {
      super.visitMethodInsn(opcode, owner, name, description)
    }
  }

}

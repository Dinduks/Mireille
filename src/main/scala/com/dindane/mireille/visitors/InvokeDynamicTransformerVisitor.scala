package main.scala.com.dindane.mireille.visitors

import java.lang.invoke.{CallSite, MethodType}
import java.lang.invoke.MethodHandles.Lookup
import org.objectweb.asm._

class InvokeDynamicTransformerVisitor(className: String, classVisitor: ClassVisitor)
  extends ClassVisitor(Opcodes.ASM4, classVisitor) {
  private var fileName: Option[String] = None

  override def visit(version: Int, access: Int, name: String, signature: String, superName: String, interfaces: Array[String]) {
    super.visit(Opcodes.V1_7, access, name, signature, superName, interfaces)
  }

  override def visitSource(source: String, debug: String) {
    fileName = Some(source)
  }

  override def visitMethod(access: Int, name: String, desc: String, signature: String, exceptions: Array[String]) = {
    val methodVisitor = classVisitor.visitMethod(access, name, desc, signature, exceptions)
    new InvokeDynamicTransformerAdapter(methodVisitor, fileName)
  }

}

class InvokeDynamicTransformerAdapter(methodVisitor: MethodVisitor, fileName: Option[String])
  extends MethodVisitor(Opcodes.ASM4, methodVisitor) {
  private var lineNumber: Option[Int] = None

  override def visitLineNumber(line: Int, startLabel: Label) {
    lineNumber = Some(line)
  }

  override def visitMethodInsn(opcode: Int, owner: String, name: String, description: String) {
    if (opcode == Opcodes.INVOKEVIRTUAL || opcode == Opcodes.INVOKEINTERFACE) {
      val methodType: MethodType = MethodType.methodType(classOf[CallSite], classOf[Lookup], classOf[String],
        classOf[MethodType], classOf[String], classOf[Integer], classOf[String])

      val bootstrapMethod = new Handle(Opcodes.H_INVOKESTATIC,
        "main/scala/com/dindane/mireille/runtime/RT",
        "bootstrap",
        methodType.toMethodDescriptorString)

      val prefix = if (owner(0) == '[') { "(" + owner } else { "(L" + owner + ';' }
      val newDescription = prefix + description.substring(1)

      super.visitInvokeDynamicInsn(name,
        newDescription,
        bootstrapMethod,
        fileName.getOrElse("<unknown_file>"),
        lineNumber.get: java.lang.Integer,
        description)
    } else {
      super.visitMethodInsn(opcode, owner, name, description)
    }
  }

}

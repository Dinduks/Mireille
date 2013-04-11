package main.scala.com.dindane.mireille

import java.io.InputStream
import org.objectweb.asm.{Opcodes, ClassWriter, ClassReader}
import visitors.InvokeDynamicTransformerVisitor

object Transformer {

  // TODO: Change this methods signature to a more logical one
  def invokeVirtualToInvokeDynamic(is: InputStream): ClassWriter = {
    val classReader = new ClassReader(is)
    val classWriter = new ClassWriter(0)
    val classVisitor = new InvokeDynamicTransformerVisitor(classReader.getClassName(), classWriter)
    classReader.accept(classVisitor, 0)
    classWriter
  }
}
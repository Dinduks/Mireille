package main.scala.com.dindane.mireille

import java.io.PrintWriter
import org.objectweb.asm.{ClassWriter, ClassReader}
import org.objectweb.asm.util.CheckClassAdapter
import visitors.InvokeDynamicTransformerVisitor

/**
 * An utility object used to transform classes
 */
object Transformer {

  def invokeVirtualToInvokeDynamic(classReader: ClassReader): ClassWriter = {
    val classWriter = new ClassWriter(0)
    val classVisitor = new InvokeDynamicTransformerVisitor(classReader.getClassName, classWriter)
    classReader.accept(classVisitor, 0)

    CheckClassAdapter.verify(new ClassReader(classWriter.toByteArray), false, new PrintWriter(System.err))

    classWriter
  }
}
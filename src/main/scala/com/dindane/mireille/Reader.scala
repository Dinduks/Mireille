package main.scala.com.dindane.mireille

import models.{InvokeDynamicCall, InvokeVirtualCall}
import java.io.InputStream
import org.objectweb.asm.ClassReader
import visitors.{InvokeDynamicVisitor, InvokeVirtualVisitor}

/**
 * An utility object used to read classes' information
 */
object Reader {

  def getInvokeVirtualCalls(is: InputStream): Seq[InvokeVirtualCall] = {
    val classReader: ClassReader = new ClassReader(is)
    val invokeVirtualVisitor = new InvokeVirtualVisitor(classReader.getClassName)
    classReader.accept(invokeVirtualVisitor, 0)

    invokeVirtualVisitor.invokeVirtualCalls
  }

  def getInvokeDynamicCalls(is: InputStream): Seq[InvokeDynamicCall] = {
    val classReader = new ClassReader(is)
    val invokeDynamicVisitor = new InvokeDynamicVisitor(classReader.getClassName)
    classReader.accept(invokeDynamicVisitor, 0)

    invokeDynamicVisitor.invokeDynamicCalls
  }

}

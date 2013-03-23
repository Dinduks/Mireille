package main.scala.com.dindane.mireille

import models.InvokeCall
import java.io.InputStream
import java.nio.file.{StandardOpenOption, Files, Path}
import org.objectweb.asm.ClassReader
import visitors.InvokeVirtualVisitor

object Reader {

  def getInvokeVirtualCalls(is: InputStream): Seq[InvokeCall] = {
    val classReader: ClassReader = new ClassReader(is)
    val invokeVirtualVisitor = new InvokeVirtualVisitor(classReader.getClassName)
    classReader.accept(invokeVirtualVisitor, 0)

    invokeVirtualVisitor.calls
  }

  def getInvokeVirtualCalls(path: Path): Seq[InvokeCall] = {
    getInvokeVirtualCalls(Files.newInputStream(path, StandardOpenOption.READ))
  }

}

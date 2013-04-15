package main.scala

import com.dindane.mireille.models.InvokeVirtualCall
import com.dindane.mireille.{Transformer, Reader}
import java.nio.file.{Path, Files, StandardOpenOption, Paths}
import org.objectweb.asm.ClassWriter

object Main {

  def main (args: Array[String]) {
    {
    val path: Path = Paths.get(System.getProperty("user.dir"))
      .resolve("src/test/scala/com/dindane/mireille/resources/TestOriginal.class")
    val path2: Path = Paths.get(System.getProperty("user.dir"))
      .resolve("src/test/scala/com/dindane/mireille/resources/IndyTestOriginal.class")
    val is = Files.newInputStream(path, StandardOpenOption.READ)

    val cw: ClassWriter = Transformer.invokeVirtualToInvokeDynamic(is)
    val bytes: Array[Byte] = cw.toByteArray

    Files.write(path2, bytes)
    }

    if (1 == args.size) {
      try {
        val path = Paths.get(args(0))
        val fileName = path.getFileName.toString

        printVirtualCalls(fileName, Reader.getInvokeVirtualCalls(path))
      } catch {
        case e: Exception => {
          println("The specified class file was not found.")
          println("Exiting.")
        }
      }
    }
  }

  def printVirtualCalls(fileName: String, invokeVirtualCalls: Seq[InvokeVirtualCall]) {
    if (0 == invokeVirtualCalls.size) {
      println("No INVOKEVIRTUAL calls were found.")
      println("Exiting.")
    } else {
      println("=== INVOKEVIRTUAL calls in %s, from %s:".format(
        fileName, invokeVirtualCalls(0).file.getOrElse("an unknown file")
      ))

      invokeVirtualCalls.map { invokeVirtualCall =>
        println()
        println("Method name: %s".format(invokeVirtualCall.methodName))
        println("Called on: %s".format(invokeVirtualCall.owner))
        if (None != invokeVirtualCall.lineNumber) {
          println("In line: %s".format(invokeVirtualCall.lineNumber.get))
        }
        println("Takes these arguments: %s".format(invokeVirtualCall.arguments.mkString(", ")))
        println("Returns: %s".format(invokeVirtualCall.returnType))
      }
    }
  }

}
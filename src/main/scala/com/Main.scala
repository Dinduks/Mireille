package main.scala

import com.dindane.mireille.models.InvokeVirtualCall
import com.dindane.mireille.Reader
import java.nio.file.Paths

object Main {

  def main (args: Array[String]) {
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
      println("=== INVOKEVIRTUAL calls in '%s', from '%s':".format(
        fileName, invokeVirtualCalls(0).file
      ))

      invokeVirtualCalls.map { invokeVirtualCall =>
        println()
        println("Method name: %s".format(invokeVirtualCall.methodName))
        println("Called on: %s".format(invokeVirtualCall.owner))
        println("Takes these arguments: %s".format(invokeVirtualCall.arguments.mkString(", ")))
        println("Returns: %s".format(invokeVirtualCall.returnType))
      }
    }
  }

}
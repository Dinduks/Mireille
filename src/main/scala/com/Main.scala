package main.scala

import com.dindane.mireille.models.InvokeVirtualCall
import main.scala.com.dindane.mireille.{Util, Transformer, Reader}
import java.nio.file.{Path, Files, StandardOpenOption, Paths}
import org.objectweb.asm.{ClassReader, ClassWriter}
import java.io.{File, FileNotFoundException}
import org.apache.commons.io.filefilter._
import main.scala.com.dindane.mireille.jar.JarUtil
import org.apache.commons.io.FileUtils

object Main {

  def main (args: Array[String]) {
    if (2 == args.size) {
      if (args(0) == "printinvi") {
        try {
          val path = Paths.get(args(1))
          val fileName = path.getFileName.toString

          printVirtualCalls(fileName, Reader.getInvokeVirtualCalls(Files.newInputStream(path, StandardOpenOption.READ)))
        } catch {
          case e: FileNotFoundException => {
            println("The specified class file was not found.")
            println("Exiting.")
          }
          case e: Exception => println(e)
        }
      } else if (args(0) == "indynamize") {
        val sourcePath: Path = Paths.get(System.getProperty("user.dir")).resolve(args(1))
        val sourceFile: File = new java.io.File(sourcePath.toString)

        if (FileFilterUtils.suffixFileFilter("class").accept(sourceFile)) {
          indynamizeAClass(sourcePath)
        } else if (FileFilterUtils.suffixFileFilter("jar").accept(sourceFile)) {
          indynamizeAJar(sourcePath, sourceFile)
        } else {
          println("The specified file is not a JAR or a class")
        }
      }
    } else {
      println("Command unknown or lacking parameters")
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

  def indynamizeAClass(sourcePath: Path) = {
    val targetPath: Path = sourcePath.getParent.resolve("indy").resolve(sourcePath.getFileName)

    try { Files.createDirectory(targetPath.getParent) } catch { case _: Throwable => () }

    val is = Files.newInputStream(sourcePath, StandardOpenOption.READ)
    val cw: ClassWriter = Transformer.invokeVirtualToInvokeDynamic(new ClassReader(is))
    val bytes: Array[Byte] = cw.toByteArray

    Files.write(targetPath, bytes)

    is.close
  }

  def indynamizeAJar(sourcePath: Path, sourceFile: File) = {
    val jarFiles: Seq[String] = JarUtil.getFiles(sourcePath)
    val extractionDir: Path = Paths.get(
      "%s%smireille-test-%s".format(System.getProperty("java.io.tmpdir"), java.io.File.separator, scala.util.Random.nextInt))
    val targetPath: Path = extractionDir.resolve("indy")

    JarUtil.extract(sourcePath, extractionDir)

    jarFiles map { jarFile =>
      if (jarFile == "META-INF/MANIFEST.MF") {
        try { Files.createDirectories(targetPath.resolve(Paths.get("META-INF"))) }
        Files.copy(extractionDir.resolve(Paths.get(jarFile)), targetPath.resolve(Paths.get(jarFile)))
      } else {
        val is = Files.newInputStream(Paths.get(extractionDir + java.io.File.separator + jarFile), StandardOpenOption.READ)
        val classReader: ClassReader = new ClassReader(is)
        val cw: ClassWriter = Transformer.invokeVirtualToInvokeDynamic(classReader)
        val bytes: Array[Byte] = cw.toByteArray

        Files.write(targetPath, bytes)

        is.close
      }
    }

    def jarNameToPatchedName(jarName: String) = "%s-patched.jar".format(jarName.substring(0, jarName.size - 4))
    JarUtil.createFromDirFiles(targetPath, Paths.get(jarNameToPatchedName(sourceFile.getName)))

    FileUtils.deleteDirectory(extractionDir.toFile)
  }

}

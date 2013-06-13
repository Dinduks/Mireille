package main.scala

import com.dindane.mireille.models.InvokeVirtualCall
import main.scala.com.dindane.mireille.jar.JarUtil
import main.scala.com.dindane.mireille.{Transformer, Reader}
import java.io.{File, FileNotFoundException}
import java.nio.file._
import org.apache.commons.io.filefilter._
import org.apache.commons.io.FileUtils
import org.objectweb.asm.{ClassReader, ClassWriter}

object Main {

  def main (args: Array[String]) {
    if (2 <= args.size) {
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
      } else if (args(0) == "patch") {
        val sourcePath: Path = Paths.get(System.getProperty("user.dir")).resolve(args(1))
        val sourceFile: File = new java.io.File(sourcePath.toString)
        val jsonify = args.size == 3 && args(2) == "--json"

        if (FileFilterUtils.suffixFileFilter("class").accept(sourceFile)) {
          patchAClass(sourcePath, jsonify)
        } else if (FileFilterUtils.suffixFileFilter("jar").accept(sourceFile)) {
          patchAJar(sourcePath, jsonify)
        } else {
          println("The specified file is neither a JAR nor a class")
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

  def patchAClass(sourcePath: Path, jsonify: Boolean) = {
    val targetPath: Path = sourcePath.getParent.resolve("indy").resolve(sourcePath.getFileName)

    try { Files.createDirectory(targetPath.getParent) } catch { case _: Throwable => () }

    try {
      val is = Files.newInputStream(sourcePath, StandardOpenOption.READ)
      val cw: ClassWriter = Transformer.invokeVirtualToInvokeDynamic(new ClassReader(is), jsonify)
      val bytes: Array[Byte] = cw.toByteArray

      Files.write(targetPath, bytes)

      is.close
    } catch {
      case e: NoSuchFileException => println("The file \"%s\" was not found.".format(sourcePath.toString))
      case e: Throwable => e.printStackTrace
    }
  }

  def patchAJar(sourcePath: Path, jsonify: Boolean) = {
    try {
      val jarFiles: Seq[String] = JarUtil.getFiles(sourcePath)
      val extractionDir: Path = Paths.get("%s%smireille-test-%s".format(System.getProperty("java.io.tmpdir"),
        java.io.File.separator,
        scala.util.Random.nextInt))
      val targetPath: Path = extractionDir.resolve("patched")

      JarUtil.extract(sourcePath, extractionDir)

      jarFiles foreach { jarFile =>
        if ((".class$".r findFirstIn jarFile) == None) {
          if (Paths.get(jarFile).getParent != null) {
            try { Files.createDirectories(targetPath.resolve(Paths.get(jarFile).getParent)) }
            Files.copy(extractionDir.resolve(Paths.get(jarFile)), targetPath.resolve(Paths.get(jarFile)))
          }
        } else {
          val is = Files.newInputStream(extractionDir.resolve(jarFile), StandardOpenOption.READ)
          val classReader: ClassReader = new ClassReader(is)
          val cw: ClassWriter = Transformer.invokeVirtualToInvokeDynamic(classReader, jsonify)
          val bytes: Array[Byte] = cw.toByteArray

          Files.createDirectories(targetPath.resolve(jarFile).getParent)
          Files.write(targetPath.resolve(jarFile), bytes)

          is.close
        }
      }

      JarUtil.copyRunTimeClassToExtractDir(targetPath)

      def jarNameToPatchedName(jarName: String) = "%s-patched.jar".format(jarName.substring(0, jarName.size - 4))
      JarUtil.createFromDirFiles(targetPath, sourcePath.getParent.resolve(jarNameToPatchedName(sourcePath.getFileName.toString)))

      FileUtils.deleteDirectory(extractionDir.toFile)
    } catch {
      case e: FileNotFoundException => println("The file \"%s\" was not found.".format(sourcePath.toString))
      case e: Throwable => e.printStackTrace
    }
  }

}
package main.scala.com.dindane.mireille.jar

import collection.JavaConversions.collectionAsScalaIterable
import collection.JavaConversions.enumerationAsScalaIterator
import java.util.jar.JarFile
import java.io._
import org.apache.commons.io.FileUtils
import java.util.zip.{ZipEntry, ZipOutputStream}
import main.scala.com.dindane.mireille.Util

object JarUtil {

  def getFiles(jarPath: String): Seq[String] = (new JarFile(jarPath)).entries.toSeq.map(file => file.getName)

  def extract(jarPath: String, destinationDir: String) = {
    val jar: JarFile = new JarFile(jarPath)

    jar.entries.toList.map { jarEntry =>
      val file: File = new File(destinationDir + java.io.File.separator + jarEntry.getName)
      FileUtils.forceMkdir(new File(file.getParent))

      if (jarEntry.isDirectory) {
        file.mkdir
      }
      else {
        val is = jar.getInputStream(jarEntry)
        val fos = new FileOutputStream(file)

        while (is.available > 0) fos.write(is.read)

        is.close
      }
    }
  }

  def createFromDirFiles(srcPath: String, targetJarPath: String) {
    val buf: Array[Byte] = new Array[Byte](1024 * 16)
    val files = FileUtils.listFiles(new File(srcPath), null, true)

    val out: ZipOutputStream  = new ZipOutputStream(new FileOutputStream(targetJarPath))

    files.map { file: File =>
      val in: FileInputStream  = new FileInputStream(file.getAbsolutePath)

      val pathWithoutPrefix: String = Util.getStringWithoutPrefix(srcPath, file.getAbsolutePath)
      val filePathInJar = if (pathWithoutPrefix(0).toString == java.io.File.separator) pathWithoutPrefix.substring(1)
        else pathWithoutPrefix

      out.putNextEntry(new ZipEntry(filePathInJar))

      var len: Int = 0
      while ({len = in.read(buf); len > 0}) {
        out.write(buf, 0, len)
      }

      out.closeEntry()
      in.close()
    }

    out.close()
  }

}

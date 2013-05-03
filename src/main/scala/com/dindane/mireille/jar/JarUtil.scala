package main.scala.com.dindane.mireille.jar

import java.util.jar.JarFile
import java.io.{FileOutputStream, File}
import collection.JavaConversions.enumerationAsScalaIterator

object JarUtil {

  def extract(jarPath: String, destinationDir: String) = {
    val jar: JarFile = new JarFile(jarPath)

    jar.entries.toList.map { jarEntry =>
      val file: File = new File(destinationDir + java.io.File.separator + jarEntry.getName)

      if (jarEntry.isDirectory) file.mkdir

      val is = jar.getInputStream(jarEntry)
      val fos = new FileOutputStream(file)

      while (is.available > 0) fos.write(is.read)

      is.close
    }
  }

}

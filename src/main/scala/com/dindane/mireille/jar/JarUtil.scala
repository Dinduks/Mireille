package main.scala.com.dindane.mireille.jar

import collection.JavaConversions.enumerationAsScalaIterator
import java.util.jar.JarFile
import java.io.{FileOutputStream, File}
import org.apache.commons.io.FileUtils

object JarUtil {

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

}

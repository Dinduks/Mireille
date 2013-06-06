package main.scala.com.dindane.mireille.jar

import collection.JavaConversions.collectionAsScalaIterable
import collection.JavaConversions.enumerationAsScalaIterator
import java.io._
import java.nio.file.{Files, StandardOpenOption, Path}
import java.util.jar.JarFile
import java.util.zip.{ZipEntry, ZipOutputStream}
import main.scala.com.dindane.mireille.runtime
import main.scala.com.dindane.mireille.Util
import org.apache.commons.io.{IOUtils, FileUtils}

/*
 * This class' methods handle all things related to JARs manipulation
 */
object JarUtil {

  /**
   * Gets the files' names of a JAR
   *
   * @param jarPath The JAR whose files' names will be returned
   * @return A list of the files in the specified JAR
   */
  def getFiles(jarPath: Path): Seq[String] = (new JarFile(jarPath.toString)).entries.toSeq.map(file => file.getName)

  /**
   * Extract all the files of a JAR/ZIP in the specified directory
   *
   * @param jarPath The JAR which will be extracted
   * @param destinationDir The direcory where the files will be extracted
   */
  def extract(jarPath: Path, destinationDir: Path) {
    val jar: JarFile = new JarFile(jarPath.toString)

    jar.entries.toList.map { jarEntry =>
      val file: File = new File(destinationDir.toString + java.io.File.separator + jarEntry.getName)
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

  /**
   * Creates a JAR containing all the files of a directory
   *
   * @param srcPath The directory that contains the files
   * @param targetJarPath The resulting JAR file's path
   */
  def createFromDirFiles(srcPath: Path, targetJarPath: Path) {
    val buf: Array[Byte] = new Array[Byte](1024 * 16)
    val files = FileUtils.listFiles(srcPath.toFile, null, true)

    val out: ZipOutputStream  = new ZipOutputStream(new FileOutputStream(targetJarPath.toFile))

    files.map { file: File =>
      val in: FileInputStream  = new FileInputStream(file.getAbsolutePath)

      val pathWithoutPrefix: String = Util.getStringWithoutPrefix(srcPath.toString, file.getAbsolutePath)
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

  /**
   * TODO: This thing does not belong to here
   */
  def copyRunTimeClassToExtractDir(extractionDir: Path) = {
    val classLoader: ClassLoader = Thread.currentThread().getContextClassLoader
    val runTimeClassPaths: Seq[String] = Seq(classOf[runtime.RT].getName,
      classOf[runtime.NullOutputStream].getName,
      classOf[runtime.ShutDownHook].getName,
      classOf[runtime.InliningCacheCallSite].getName,
      classOf[runtime.CallSiteInformation].getName) map (_.replace('.', '/') + ".class")

    val pathsAndStreams: Map[Path, InputStream] = runTimeClassPaths.map { rcp =>
      (extractionDir.resolve(rcp), classLoader.getResource(rcp).openStream)
    }.toMap

    pathsAndStreams map { case (path, _) => Files.createDirectories(path.getParent) }
    pathsAndStreams map { case (path, is) => Files.write(path, IOUtils.toByteArray(is), StandardOpenOption.CREATE) }
  }

}
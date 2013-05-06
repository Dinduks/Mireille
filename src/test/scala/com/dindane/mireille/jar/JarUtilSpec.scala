
package test.scala.com.dindane.mireille.jar

import collection.JavaConversions.collectionAsScalaIterable
import java.io.File
import main.scala.com.dindane.mireille.jar.JarUtil
import org.apache.commons.io.FileUtils
import org.specs2.mutable._
import scala.util.Random
import main.scala.com.dindane.mireille.Util
import java.nio.file.{Path, Paths}

class JarUtilSpec extends Specification {

  val filesList = Seq("META-INF/MANIFEST.MF",
    "org/objectweb/asm/tree/AbstractInsnNode.class",
    "org/objectweb/asm/tree/AnnotationNode.class",
    "org/objectweb/asm/tree/ClassNode.class",
    "org/objectweb/asm/tree/FieldInsnNode.class",
    "org/objectweb/asm/tree/FieldNode.class",
    "org/objectweb/asm/tree/FrameNode.class",
    "org/objectweb/asm/tree/IincInsnNode.class",
    "org/objectweb/asm/tree/InnerClassNode.class",
    "org/objectweb/asm/tree/InsnList$InsnListIterator.class",
    "org/objectweb/asm/tree/InsnList.class",
    "org/objectweb/asm/tree/InsnNode.class",
    "org/objectweb/asm/tree/IntInsnNode.class",
    "org/objectweb/asm/tree/InvokeDynamicInsnNode.class",
    "org/objectweb/asm/tree/JumpInsnNode.class",
    "org/objectweb/asm/tree/LabelNode.class",
    "org/objectweb/asm/tree/LdcInsnNode.class",
    "org/objectweb/asm/tree/LineNumberNode.class",
    "org/objectweb/asm/tree/LocalVariableNode.class",
    "org/objectweb/asm/tree/LookupSwitchInsnNode.class",
    "org/objectweb/asm/tree/MethodInsnNode.class",
    "org/objectweb/asm/tree/MethodNode$1.class",
    "org/objectweb/asm/tree/MethodNode.class",
    "org/objectweb/asm/tree/MultiANewArrayInsnNode.class",
    "org/objectweb/asm/tree/TableSwitchInsnNode.class",
    "org/objectweb/asm/tree/TryCatchBlockNode.class",
    "org/objectweb/asm/tree/TypeInsnNode.class",
    "org/objectweb/asm/tree/VarInsnNode.class")

  def newTempDir: Path = Paths.get(System.getProperty("java.io.tmpdir")).resolve("mireille-test-%s".format(Random.nextInt))

  def recursiveListFiles(f: File): Array[File] = {
    f.mkdir
    val these = f.listFiles
    these ++ these.filter(_.isDirectory).flatMap(recursiveListFiles)
  }

  "JarUtil.extract" should {
    "Extract all the JAR's file in the specified directory" in {
      val destinationDirPath = newTempDir
      val destinationDir = destinationDirPath.toFile

      JarUtil.extract(Paths.get("src/test/scala/com/dindane/mireille/resources/asm-tree-4.1.jar"), destinationDirPath)

      val extractedFiles: Iterable[String] = FileUtils.listFiles(destinationDir, null, true).map { path: File =>
        Util.getStringWithoutPrefix(destinationDirPath.toString, path.getPath)
      }

      destinationDir.delete

      extractedFiles.map { path => filesList must contain(path.substring(1)) }
      extractedFiles.size must_== filesList.size
    }
  }

  "JarUtil.getFiles" should {
    "Return a Seq of all the jar files" in {
      val jarFiles: Seq[String] = JarUtil.getFiles(Paths.get("src/test/scala/com/dindane/mireille/resources/asm-tree-4.1.jar"))

      jarFiles.map { pathInJar => filesList must contain(pathInJar) }
      jarFiles.size must_== filesList.size
    }
  }

  "JarUtil.createFromDirFiles" should {
    "Create a jar file from all the files of a directory" in {
      val srcPath: Path = Paths.get("").toAbsolutePath.resolve("src/test/scala/com/dindane/mireille/resources/asm-tree-4.1")
      val destinationJarPath: Path = newTempDir.resolve("asm-tree-4.1.jar")
      val destinationJarFile = destinationJarPath.toFile

      destinationJarFile.getParentFile.mkdir

      JarUtil.createFromDirFiles(srcPath, destinationJarPath)

      val jarFiles: Seq[String] = JarUtil.getFiles(destinationJarPath)

      destinationJarFile.getParentFile.mkdir

      jarFiles.map { pathInJar => filesList must contain(pathInJar) }
      jarFiles.size must_== filesList.size
    }
  }

}

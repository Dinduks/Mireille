package test.scala.com.dindane.mireille.jar

import collection.JavaConversions.collectionAsScalaIterable
import java.io.File
import main.scala.com.dindane.mireille.jar.JarUtil
import org.apache.commons.io.FileUtils
import org.specs2.mutable._
import scala.util.Random

class JarUtilSpec extends Specification {

  def recursiveListFiles(f: File): Array[File] = {
    f.mkdir
    val these = f.listFiles
    these ++ these.filter(_.isDirectory).flatMap(recursiveListFiles)
  }

  "JarUtil.extract" should {
    "Extract all the JAR's file in the specified directory" in {
      val expectedResult = Seq("META-INF/MANIFEST.MF",
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

      val destinationDirPath: String =
        "%s%smireille-test-%s".format(System.getProperty("java.io.tmpdir"), java.io.File.separator, Random.nextInt)
      val destinationDir = new File(destinationDirPath)

      JarUtil.extract("src/test/scala/com/dindane/mireille/resources/asm-tree-4.1.jar", destinationDirPath)

      def getPathWithoutPrefix(prefix: String, fullPath: String): String = fullPath.substring(prefix.size + 0)
      val extractedFiles: Iterable[String] = FileUtils.listFiles(destinationDir, null, true).map { path: File =>
        getPathWithoutPrefix(destinationDirPath, path.getPath)
      }

      destinationDir.delete

      extractedFiles.map { path => expectedResult must contain(path) }
      extractedFiles.size must_== expectedResult.size
    }
  }

}

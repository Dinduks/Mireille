package com.dindane.mireille.samples

import org.objectweb.asm.ClassReader

object Samples {

  def basicParsingExample() {
    val classPrinter: ClassPrinter = new ClassPrinter
    val classReader: ClassReader  = new ClassReader("java.lang.Runnable");
    classReader.accept(classPrinter, 0);
  }

}

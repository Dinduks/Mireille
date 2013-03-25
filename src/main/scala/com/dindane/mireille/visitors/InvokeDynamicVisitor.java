package main.scala.com.dindane.mireille.visitors;

import org.objectweb.asm.*;
import main.scala.com.dindane.mireille.models.InvokeDynamicCall;
import scala.Option;
import scala.Some;
import scala.collection.Seq;
import java.util.ArrayList;
import java.util.Arrays;

public class InvokeDynamicVisitor extends ClassVisitor {
    public ArrayList<InvokeDynamicCall> invokeDynamicCalls = new ArrayList<InvokeDynamicCall>();
    private String className;
    private Option<String> fileName;

    public InvokeDynamicVisitor(String className) {
        super(Opcodes.ASM4);
        this.className = className;
    }

    @Override
    public void visitSource(String source, String debug) {
        fileName = new Some(source);
    }

    public MethodVisitor visitMethod(int access, String name, final String description, String signature, String[] exceptions) {
        return new MethodVisitor(Opcodes.ASM4) {
            int lineNumber;

            public void visitLineNumber(int line, Label startLabel) {
                lineNumber = line;
            }

            public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
                Seq<Type> arguments = scala.collection.JavaConversions.asScalaBuffer(
                        Arrays.asList(Type.getArgumentTypes(description))
                ).seq();

                invokeDynamicCalls.add(new InvokeDynamicCall(className,
                        name,
                        arguments,
                        Type.getReturnType(description),
                        fileName,
                        new Some(lineNumber)));
            }
        };
    }
}


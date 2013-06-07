package main.scala.com.dindane.mireille.runtime;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;

public class InliningCacheCallSite extends MutableCallSite {
    private MethodHandles.Lookup lookUp;
    private String methodName;
    private MethodType methodType;

    public InliningCacheCallSite(MethodHandles.Lookup lookUp, String methodName, MethodType methodType, MethodHandle fallBack) {
        super(methodType);

        this.lookUp = lookUp;
        this.methodName = methodName;
        this.methodType = methodType;

        MethodHandle fallbackMethodHandle = fallBack
                .bindTo(this)
                .asCollector(Object[].class, methodType.parameterCount())
                .asType(methodType);

        setTarget(fallbackMethodHandle);
    }

    public Object fallback(Object... args) throws Throwable {
        MethodHandle methodHandle = lookUp.findVirtual(methodType.parameterType(0),
                methodName,
                methodType.dropParameterTypes(0, 1));

        return methodHandle.invokeWithArguments(args);
    }
}

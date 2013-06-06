package main.scala.com.dindane.mireille.runtime;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;
import java.lang.reflect.Method;

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
        Class[] parameterTypes = methodType
                .dropParameterTypes(0, 1)
                .parameterArray();
        Method method = buildMethodObject(methodType.parameterType(0), methodName, parameterTypes);
        method.setAccessible(true);
        MethodHandle methodHandle = MethodHandles.publicLookup().unreflect(method);

        return methodHandle.invokeWithArguments(args);
    }

    /**
     * Given a Class object, a method name, and the parameters' types of the latter,
     * this method returns a well constructed Method object.
     *
     * The case where the specified method doesn't exist in the Class object,
     * but in one of its parents, is supported.
     *
     * @param klass The class where to look for the method
     * @param methodName
     * @param parameterTypes The types of the method's arguments
     * @return A well constructed Method object
     */
    private Method buildMethodObject(Class klass, String methodName, Class[] parameterTypes) {
        Method method;

        try {
            method = klass.getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            method = buildMethodObject(klass.getSuperclass(), methodName, parameterTypes);
        }

        return method;
    }

}

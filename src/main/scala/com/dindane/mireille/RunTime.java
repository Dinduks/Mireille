package main.scala.com.dindane.mireille;

import java.lang.invoke.*;
import java.util.HashMap;

public class RunTime {
    public MethodHandle FALLBACK;
    public int callsCounter = 0;
    public HashMap<String, Integer> methodCalls = new HashMap<>();

    private static RunTime instance = new RunTime();

    private RunTime() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("\n\nExecution summary:");
                System.out.println("==================");

                System.out.println("\nMethod invocations");
                System.out.println(String.format("\nTotal: %d", callsCounter));

//                for (Pair<String, Integer> methodCall : RunTime.methodCalls.values()) {
//                    System.out.println(String.format("%s() \t\t called %d times", RunTime.methodCalls.get(i), RunTime.methodCalls.get(i)));
//                }
            }
        });
    }

    public static CallSite bootstrap(MethodHandles.Lookup lookUp, String methodName, MethodType methodType) {
        return instance.bsm(lookUp, methodName, methodType);
    }

    public static RunTime getInstance() {
        return instance;
    }

    private CallSite bsm(MethodHandles.Lookup lookUp, String methodName, MethodType methodType) {
        try {
            FALLBACK = MethodHandles.lookup().findVirtual(InliningCacheCallSite.class,
                    "fallback",
                    MethodType.methodType(Object.class, Object[].class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        return new InliningCacheCallSite(lookUp, methodName, methodType, FALLBACK);
    }
}

class InliningCacheCallSite extends MutableCallSite {
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
        RunTime runTimeInstance = RunTime.getInstance();

        runTimeInstance.callsCounter += 1;

        if (runTimeInstance.methodCalls.get(methodName) == null) {
            runTimeInstance.methodCalls.put(methodName, 1);
        } else {
            runTimeInstance.methodCalls.put(methodName, runTimeInstance.methodCalls.get(methodName) + 1);
        }

        try {
            System.out.print("Object: ");
            System.out.println(args[0]);
            System.out.print("Method name: ");
            System.out.println(methodName);
            if (args[1] != null) {
                System.out.print("Argument: ");
                System.out.println(args[1]);
            }
        } catch (Exception e) {}

        MethodHandle method = lookUp.findVirtual(args[0].getClass(),
                methodName,
                methodType.dropParameterTypes(0, 1)).asType(methodType);

        return method.invokeWithArguments(args);
    }
}
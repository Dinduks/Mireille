package main.scala.com.dindane.mireille.runtime;

import java.io.PrintStream;
import java.lang.invoke.*;
import java.util.ArrayList;
import java.util.HashMap;

public class RT {
    public MethodHandle FALLBACK;
    private HashMap<String, ArrayList<CallSiteInformation>> callsInfo = new CallSiteInformationMap();

    private static RT instance;
    private PrintStream originalOutput = System.out;

    public static void init() {
        instance = getInstance();
    }

    public static RT getInstance() {
        if (instance == null) instance = new RT();

        return instance;
    }

    private RT() {
        System.setOut(new PrintStream(new NullOutputStream()));
        Runtime.getRuntime().addShutdownHook(new ShutDownHook(callsInfo, originalOutput));
    }

    public static CallSite bootstrap(MethodHandles.Lookup lookUp, String methodName, MethodType methodType,
                                     String fileName, Integer lineNumber, String desc) {
        return getInstance().bsm(lookUp, methodName, methodType, fileName, lineNumber, desc);
    }

    private CallSite bsm(MethodHandles.Lookup lookUp, String methodName, MethodType methodType,
                         String fileName, Integer lineNumber, String description) {
        try {
            FALLBACK = MethodHandles.lookup().findVirtual(InliningCacheCallSite.class,
                    "fallback",
                    MethodType.methodType(Object.class, Object[].class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        CallSite callSite = new InliningCacheCallSite(lookUp, methodName, methodType, FALLBACK);

        if (!callsInfo.containsKey(methodName)) callsInfo.put(methodName, new ArrayList<CallSiteInformation>());
        callsInfo.get(methodName).add(new CallSiteInformation(callSite,
                callSite.type().parameterType(0),
                methodName,
                fileName,
                lineNumber,
                description));

        return callSite;
    }
}

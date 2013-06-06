package main.scala.com.dindane.mireille.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShutDownHook extends Thread {
    HashMap<String, ArrayList<CallSiteInformation>> callsInfo;

    public ShutDownHook(HashMap<String, ArrayList<CallSiteInformation>> callsInfo) {
        super();
        this.callsInfo = callsInfo;
    }

    @Override
    public void run() {
        String stringBuffer;
        int nonOptimCounter = 0;

        System.out.println("\n\nNon-optimized method calls:");
        System.out.println("===========================");
        for (Map.Entry<String, ArrayList<CallSiteInformation>> info : callsInfo.entrySet()) {
            if (info.getValue().size() > 2) {
                System.out.println();
                for (CallSiteInformation callInfo : info.getValue()) {
                    stringBuffer = String.format("=> %s.%s%s in %s:%d",
                            callInfo.obj.getName(),
                            callInfo.methodName,
                            callInfo.description,
                            callInfo.fileName,
                            callInfo.lineNumber);
                    nonOptimCounter++;
                    System.out.println(stringBuffer);
                }
            }
        }

        System.out.println(String.format("\nNon-optimized method calls: %d", nonOptimCounter));
        System.out.println(String.format("Total method calls:         %d", callsInfo.size()));
    }
}

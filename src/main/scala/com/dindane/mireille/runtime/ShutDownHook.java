package main.scala.com.dindane.mireille.runtime;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShutDownHook extends Thread {
    private HashMap<String, ArrayList<CallSiteInformation>> callsInfo;
    private HashMap<String, ArrayList<CallSiteInformation>> nonOptimCallsInfo = new HashMap<>();
    private PrintStream originalOutput;

    public ShutDownHook(HashMap<String, ArrayList<CallSiteInformation>> callsInfo, PrintStream originalOutput) {
        super();
        this.callsInfo = callsInfo;
        this.originalOutput = originalOutput;
    }

    @Override
    public void run() {
        System.setOut(originalOutput);

        int totalMethodCalls = 0;

        System.out.println("Non-optimized method calls:");
        System.out.println("===========================");
        for (Map.Entry<String, ArrayList<CallSiteInformation>> info : callsInfo.entrySet()) {
            totalMethodCalls += info.getValue().size();
            if (info.getValue().size() > 2) nonOptimCallsInfo.put(info.getKey(), info.getValue());
        }

        printRawSummary(totalMethodCalls);
    }

    private void printRawSummary(int totalMethodCalls) {
        int nonOptimCounter = 0;
        String stringBuffer;

        for (Map.Entry<String, ArrayList<CallSiteInformation>> info : nonOptimCallsInfo.entrySet()) {
            System.out.println();
            for (CallSiteInformation callInfo : info.getValue()) {
                stringBuffer = String.format("=> %s.%s%s in %s, line %d",
                        callInfo.obj.getName(),
                        callInfo.methodName,
                        callInfo.description,
                        callInfo.fileName != null ? callInfo.fileName : "<unknown>",
                        callInfo.lineNumber != null ? callInfo.lineNumber : "<unknown>");
                nonOptimCounter++;
                System.out.println(stringBuffer);
            }
        }

        System.out.println(String.format("\nNon-optimized method calls: %d", nonOptimCounter));
        System.out.println(String.format("Total method calls:         %d", totalMethodCalls));
    }
}

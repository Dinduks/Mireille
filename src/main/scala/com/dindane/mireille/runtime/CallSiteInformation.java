package main.scala.com.dindane.mireille.runtime;

import java.lang.invoke.CallSite;

/**
 * An object representing a CallSite and some information related to it, such as
 * the method name, the file name and the line number of the original call
 */
public class CallSiteInformation {
    public CallSite callSite;
    public Class obj;
    public String methodName;
    public String fileName;
    public String description;
    public Integer lineNumber;

    public CallSiteInformation(CallSite callSite, Class obj, String methodName,
                               String fileName, int lineNumber, String description) {
        this.callSite = callSite;
        this.obj = obj;
        this.methodName = methodName;
        this.fileName = fileName;
        this.lineNumber = lineNumber;
        this.description = description;
    }

    public String toString() {
        return new StringBuilder("CallSite:    ").append(callSite).append("\n")
                .append("Object:      ").append(obj).append("\n")
                .append("Method name: ").append(methodName).append("\n")
                .append("File name:   ").append(fileName).append("\n")
                .append("Line number: ").append(lineNumber).append("\n")
                .append("Arguments:   ").append(description).append("\n")
                .toString();
    }

    public String jsonify() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("{");
        stringBuilder.append(String.format("\"object\" :%s,", obj));
        stringBuilder.append(String.format("\"methodName\" :%s,", methodName));
        stringBuilder.append(String.format("\"fileName\" :%s,", fileName));
        stringBuilder.append(String.format("\"lineNumber\" :%d,", lineNumber));
        stringBuilder.append(String.format("\"description\" :%s", description));
        stringBuilder.append("}");

        return stringBuilder.toString();
    }
}

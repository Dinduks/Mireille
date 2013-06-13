package main.scala.com.dindane.mireille.runtime;

import java.util.ArrayList;
import java.util.HashMap;

public class CallSiteInformationMap extends HashMap<String, ArrayList<CallSiteInformation>> {
    public String jsonify() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[");
        for (String key : keySet()) {
            for (CallSiteInformation csi : get(key)) {
                stringBuilder.append(csi.jsonify());
                stringBuilder.append(", ");
            }
        }
        if (stringBuilder.length() > 1) stringBuilder.setLength(stringBuilder.length() - 2);
        stringBuilder.append("]");

        return stringBuilder.toString();
    }
}

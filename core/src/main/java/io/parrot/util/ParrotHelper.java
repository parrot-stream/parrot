package io.parrot.util;

public class ParrotHelper {

    public static String getVersion() {
        try {
            return ParrotHelper.class.getPackage().getImplementationVersion();
        } catch (Exception ex) {
            return "0.0.0.0";
        }
    }
}

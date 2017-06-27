package io.parrot.data;

import java.util.LinkedHashMap;

public class ParrotKey extends LinkedHashMap<String, ParrotField> {

    private static final long serialVersionUID = 1L;

    public void addKey(String pKey, ParrotField pField) {
        put(pKey, pField);
    }

    @Override
    public String toString() {
        String fields = "[";
        for (ParrotField f : values()) {
            fields += f + ",";
        }
        return fields.substring(0, fields.length() - 1) + "]";
    }
}

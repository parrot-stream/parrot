package io.parrot.data;

import org.apache.kafka.connect.data.Schema;

public class ParrotField {

    Schema schema;
    Object value;
    String field;
    boolean timestamp;

    public ParrotField(String pField, Object pValue, Schema pSchema) {
        field = pField;
        value = pValue;
        schema = pSchema;
        timestamp = false;
    }

    public ParrotField(String pField, Object pValue, Schema pSchema, boolean pTimestamp) {
        field = pField;
        value = pValue;
        schema = pSchema;
        timestamp = pTimestamp;
    }

    public Object value() {
        return value;
    }

    public Schema schema() {
        return schema;
    }

    public String field() {
        return field;
    }

    public boolean isTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        String properties = "";
        if (schema().parameters() != null) {
            for (String key : schema().parameters().keySet()) {
                properties += "\"" + key + "\": \"" + schema().parameters().get(key) + "\",";
            }
            properties = ", \"properties\": {" + properties.substring(0, properties.length() - 1) + "}";
        }
        String schema = "{ \"type\": \"" + schema().type().name() + "\", \"timestamp\": " + isTimestamp()
                + ", \"class\": \"" + value().getClass().getName() + "\", \"optional\":" + schema().isOptional()
                + (schema().defaultValue() != null && schema().defaultValue().equals("null")
                        ? ", \"defaultValue\": " + schema().defaultValue() : "")
                + properties + "}";

        return "{ \"name\": \"" + field() + "\", \"schema\": " + schema + ", \"value\": " + value + "}";
    }

}
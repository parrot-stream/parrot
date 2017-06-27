package io.parrot.processor.sink.hbase.converter;

import java.math.BigDecimal;

import org.apache.camel.Converter;
import org.apache.hadoop.hbase.util.Bytes;

public class ObjectToHBaseByteArray {

   // @Converter
    public byte[] toByteArray(Object value) {

        System.out.println("\n\nCONVERTING TO BYTES: "+value+"\n\n");

        switch (value.getClass().getName()) {
        case "java.lang.Boolean":
            return Bytes.toBytes((Boolean) value);
        case "java.lang.Short":
            return Bytes.toBytes((Short) value);
        case "java.lang.Integer":
            return Bytes.toBytes((Integer) value);
        case "java.lang.Long":
            return Bytes.toBytes((Long) value);
        case "java.lang.Float":
            return Bytes.toBytes((Float) value);
        case "java.lang.Double":
            return Bytes.toBytes((Double) value);
        case "java.lang.BigDecimal":
            return Bytes.toBytes((BigDecimal) value);
        case "java.lang.String":
            return Bytes.toBytes((String) value);
        default:
        }
        return null;
    }
}

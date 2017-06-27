package io.parrot.data;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Field;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ParrotData {

    @JsonProperty("key")
    ParrotKey key;

    @JsonProperty("value")
    ParrotValue value;

    public ParrotData() {
    }

    public ParrotData(ParrotKey pKey, ParrotValue pValue) {
        key = pKey;
        value = pValue;
    }

    public ParrotKey key() {
        return key;
    }

    public ParrotValue value() {
        return value;
    }

    @Override
    public String toString() {
        return "{ \"key\": " + key + ", \"value\": " + value + "}";
    }

}

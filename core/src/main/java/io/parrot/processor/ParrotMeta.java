package io.parrot.processor;

import java.util.LinkedHashMap;

import io.parrot.data.ParrotField;
import io.parrot.processor.source.ParrotMetaType;

public class ParrotMeta extends LinkedHashMap<ParrotMetaType, ParrotField> {

    private static final long serialVersionUID = 1L;

    public void addMeta(ParrotMetaType pMeta, ParrotField pValue) {
        put(pMeta, pValue);
    }

    public ParrotField getMeta(ParrotMetaType pMeta) {
        return get(pMeta);
    }

}

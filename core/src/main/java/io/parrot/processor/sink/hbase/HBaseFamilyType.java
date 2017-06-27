package io.parrot.processor.sink.hbase;

public enum HBaseFamilyType {

	FAMILY_SOURCE("source"), FAMILY_PARROT("parrot");

	String family;

	HBaseFamilyType(String pFamily) {
		family = pFamily;
	}

	public String toString() {
		return family;
	}
	
}
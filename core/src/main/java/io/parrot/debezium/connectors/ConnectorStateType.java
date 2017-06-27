package io.parrot.debezium.connectors;

public enum ConnectorStateType {

	RUNNING("RUNNING"), PAUSED("PAUSED");

	String state;

	ConnectorStateType(String pState) {
		state = pState;
	}

	public String toString() {
		return state;
	}

}
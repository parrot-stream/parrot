package io.parrot.data.reader.mysql;

public enum MySqlSourceType {
    NAME("name"), SERVICE_ID("service_id"), TS_SEC("ts_sec"), GTID("gtid"), FILE("file"), POS("pos"), ROW(
            "row"), SNAPSHOT("snapshot");

    String field;

    MySqlSourceType(String pField) {
        field = pField;
    }

    public String field() {
        return field;
    }
}

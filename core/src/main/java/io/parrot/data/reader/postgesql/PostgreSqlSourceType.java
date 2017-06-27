package io.parrot.data.reader.postgesql;

public enum PostgreSqlSourceType {
    NAME("name"), TS_USEC("ts_usec"), TXID("txId"), LSN("lsn"), SNAPSHOT("snapshot"), LAST_SNAPSHOT_RECORD(
            "last_snapshot_record");

    String field;

    PostgreSqlSourceType(String pField) {
        field = pField;
    }

    public String field() {
        return field;
    }
}

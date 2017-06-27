package io.parrot.utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.Collection;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.SchemaBuilder.FieldAssembler;
import org.apache.avro.SchemaBuilder.FieldBuilder;
import org.apache.avro.SchemaBuilder.FieldDefault;
import org.apache.avro.SchemaBuilder.FieldTypeBuilder;
import org.apache.kafka.connect.data.Decimal;

import io.debezium.data.Envelope;
import io.parrot.api.model.ErrorApi;
import io.parrot.api.model.ParrotSinkApi;
import io.parrot.data.ParrotField;
import io.parrot.data.ParrotKey;
import io.parrot.data.ParrotValue;
import io.parrot.exception.ParrotApiException;
import io.parrot.processor.ParrotMeta;
import io.parrot.processor.source.ParrotMetaType;

public class ParrotHelper {

    public static String getSourceName(String pSchemaName) {
        int lastDotIndex = pSchemaName.lastIndexOf(".");
        return pSchemaName.substring(0, lastDotIndex);
    }

    public static boolean isDeleteChangeEventType(ParrotMeta meta) {
        ParrotField field = meta.get(ParrotMetaType.PARROT_CHANGE_TYPE);
        return Envelope.Operation.DELETE.code().equals((String) field.value());
    }

    public static void validateSink(ParrotSinkApi pSink) {
        if (pSink.getId().contains("_")) {
            throw new ParrotApiException(Status.BAD_REQUEST,
                    "Invalid character in field 'id' for Parrot Processor's Sink field '" + pSink.getId()
                            + "'. The given value was '" + pSink.getId()
                            + "' and following characters are not allowed: _");
        }
    }

    public static void checkConnection(String hostPorts) throws NumberFormatException, IOException {
        String[] hostPortsArray = hostPorts.split(",");
        for (String host : hostPortsArray) {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host.split(":")[0], Integer.parseInt(host.split(":")[1])), 5000);
            socket.close();
        }
    }

    public static <T> T execute(retrofit2.Call<T> call) {
        try {
            retrofit2.Response<T> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                ErrorApi error = null;
                if (javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR.getStatusCode() != response.code()) {
                    error = JsonUtils.byteArrayToObject(response.errorBody().bytes(), ErrorApi.class);
                    error.setMessage(response.message());
                } else {
                    error = new ErrorApi();
                    error.setErrorCode(response.code());
                    error.setMessage(response.message());
                }
                throw new ParrotApiException(error);
            }
        } catch (ParrotApiException pe) {
            throw pe;
        } catch (Exception e) {
            throw new ParrotApiException(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public static Schema toAvroSchema(String jsonKey) {
        return new Schema.Parser().parse(jsonKey);
    }

    public static Schema toAvroSchema(ParrotKey parrotKey) {
        return buildAvroSchema(parrotKey.values(), "ParrotKey");
    }

    public static Schema toAvroSchema(ParrotValue parrotValue) {
        return buildAvroSchema(parrotValue.values(), "ParrotValue");
    }

    static Schema buildAvroSchema(Collection<ParrotField> pParrotFields, String recordName) {
        FieldAssembler<Schema> fieldAssembler = SchemaBuilder.record(recordName).namespace("io.parrot.data").fields();
        for (ParrotField pf : pParrotFields) {
            FieldBuilder<Schema> fb = fieldAssembler.name(pf.field());
            FieldDefault fd = null;
            if (pf.schema().parameters() != null) {
                for (String key : pf.schema().parameters().keySet()) {
                    fb = fb.prop(key, pf.schema().parameters().get(key));
                }
            }
            FieldTypeBuilder<Schema> ftb = fb.type();
            Object defaultValue = pf.schema().defaultValue();
            switch (pf.schema().type()) {
            case INT8:
            case INT16:
            case INT32:
                if (pf.schema().isOptional()) {
                    fieldAssembler = ftb.optional().intType();
                } else {
                    if (defaultValue != null) {
                        fieldAssembler = ftb.intType().intDefault((Integer) defaultValue);
                    } else {
                        fieldAssembler = ftb.intType().noDefault();
                    }
                }
                break;
            case INT64:
                fieldAssembler = ftb.longType().noDefault();
                break;
            case FLOAT32:
                fieldAssembler = ftb.floatType().noDefault();
                break;
            case FLOAT64:
                fieldAssembler = ftb.doubleType().noDefault();
                break;
            case BYTES:
                fieldAssembler = ftb.bytesType().noDefault();
                break;
            case STRING:
                fieldAssembler = ftb.stringType().noDefault();
                break;
            case BOOLEAN:
                fieldAssembler = ftb.booleanType().noDefault();
                break;
            }

        }
        return fieldAssembler.endRecord();
    }

    public static String formatBigDecimal(ParrotField pField) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(Integer.parseInt(pField.schema().parameters().get(Decimal.SCALE_FIELD)));
        df.setMinimumFractionDigits(df.getMaximumFractionDigits());
        df.setGroupingUsed(false);
        return df.format(pField.value());
    }
}

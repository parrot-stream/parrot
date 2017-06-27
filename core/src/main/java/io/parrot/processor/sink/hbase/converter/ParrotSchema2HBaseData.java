/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.parrot.processor.sink.hbase.converter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.Instant;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.TypeConverters;
import org.apache.camel.component.hbase.model.HBaseCell;
import org.apache.camel.component.hbase.model.HBaseData;
import org.apache.camel.component.hbase.model.HBaseRow;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.kafka.connect.data.Decimal;

import io.parrot.data.ParrotData;
import io.parrot.data.ParrotField;
import io.parrot.processor.ParrotHeaderType;
import io.parrot.processor.ParrotMeta;
import io.parrot.processor.sink.hbase.HBaseFamilyType;
import io.parrot.processor.source.ParrotMetaType;
import io.parrot.utils.ParrotHelper;

@ApplicationScoped
public class ParrotSchema2HBaseData implements TypeConverters {

    @Converter
    public HBaseData toHBaseData(ParrotData parrotSchema, Exchange exchange) {
        ParrotMeta meta = (ParrotMeta) exchange.getIn().getHeader(ParrotHeaderType.PARROT_META.name());

        HBaseData data = new HBaseData();
        HBaseRow row = new HBaseRow();

        String id = "";
        for (ParrotField f : parrotSchema.value().values()) {
            if (parrotSchema.key().containsKey(f.field())) {
                id += f.value() + "#";
            }
        }
        id = id.substring(0, id.length() - 1);

        row.setId(id);

        if (!ParrotHelper.isDeleteChangeEventType(meta)) {
            for (ParrotField f : parrotSchema.value().values()) {
                if (!parrotSchema.key().containsKey(f.field())) {
                    row.getCells().add(getHBaseCell(f, HBaseFamilyType.FAMILY_SOURCE.toString()));
                }
            }

            /**
             * Adds the Parrot Metadata
             */
            for (ParrotMetaType m : meta.keySet()) {
                row.getCells().add(getHBaseCell(meta.get(m), HBaseFamilyType.FAMILY_PARROT.toString()));
            }
        }
        data.getRows().add(row);
        return data;
    }

    HBaseCell getHBaseCell(ParrotField pField, String pFamily) {
        HBaseCell cell = new HBaseCell();
        cell.setFamily(pFamily);
        cell.setQualifier(pField.field().toLowerCase());
        switch (pField.schema().type()) {
        case BOOLEAN:
            cell.setValue(String.valueOf((Boolean) pField.value()));
            return cell;
        case INT8:
        case INT16:
            cell.setValue(String.valueOf((Short) pField.value()));
            return cell;
        case INT32:
            cell.setValue(String.valueOf((Integer) pField.value()));
            return cell;
        case INT64:
            if (pField.isTimestamp()) {
                cell.setValue(Instant.ofEpochMilli((Long) pField.value()).toString());
            } else {
                cell.setValue(String.valueOf((Long) pField.value()));
            }
            return cell;
        case FLOAT32:
            cell.setValue(String.valueOf((Float) pField.value()));
            return cell;
        case FLOAT64:
            cell.setValue(String.valueOf((Double) pField.value()));
            return cell;
        case BYTES:
            cell.setValue(ParrotHelper.formatBigDecimal(pField));
            return cell;
        case STRING:
            cell.setValue(pField.value());
            return cell;
        default:
            cell.setValue(pField.value());
            return cell;
        }
    }
}
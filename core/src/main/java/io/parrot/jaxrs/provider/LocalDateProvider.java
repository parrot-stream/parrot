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
package io.parrot.jaxrs.provider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

@Provider
public class LocalDateProvider implements ParamConverterProvider {

    public static class LocalDateConverter implements ParamConverter<LocalDate> {

        public LocalDate fromString(String string) {
            LocalDate localDate = LocalDate.parse(string);
            return localDate;
        }

        public String toString(LocalDate t) {
            return t.toString();
        }
    }

    public <T> ParamConverter<T> getConverter(Class<T> type, Type type1, Annotation[] antns) {
        if (LocalDate.class.equals(type)) {
            return (ParamConverter<T>) new LocalDateConverter();
        }
        return null;
    }
}
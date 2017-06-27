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
package io.parrot.utils;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;

import io.parrot.exception.ParrotJsonException;

public class JsonUtils {

	private static ObjectMapper defaultMapper;
	private static ObjectMapper prettyPrintedMapper;

	private static ResteasyJackson2Provider provider;

	public static ObjectMapper getDefaultMapper() {
		if (defaultMapper == null) {
			defaultMapper = initMapper(new ObjectMapper());
		}
		return defaultMapper;
	}

	public static ObjectMapper getPrettyPrintedMapper() {
		if (prettyPrintedMapper == null) {
			prettyPrintedMapper = initMapper(new ObjectMapper());
			prettyPrintedMapper.enable(INDENT_OUTPUT);
		}
		return prettyPrintedMapper;
	}

	private static ObjectMapper initMapper(ObjectMapper mapper) {
		mapper.registerModule(new JSR310Module());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		mapper.setSerializationInclusion(Include.NON_NULL);
		return mapper;
	}

	public static ResteasyJackson2Provider getDefaultProvider() {
		if (provider == null) {
			provider = new ResteasyJackson2Provider();
			provider.setMapper(getDefaultMapper());
		}
		return provider;
	}

	public static <T> T jsonToObject(String json, Class<T> objClass) throws ParrotJsonException {
		try {
			return getDefaultMapper().readValue(json, objClass);
		} catch (Exception e) {
			throw new ParrotJsonException(e.getMessage(), e);
		}
	}

	public static String objectToJson(Object obj) throws ParrotJsonException {
		return objectToJson(obj, false);
	}

	public static String objectToJson(Object obj, boolean prettyPrinted) throws ParrotJsonException {
		try {
			return prettyPrinted ? getPrettyPrintedMapper().writeValueAsString(obj)
					: getDefaultMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new ParrotJsonException(e.getMessage(), e);
		}
	}

	public static <T> T byteArrayToObject(byte[] obj, Class<T> objClass) throws ParrotJsonException {
		try {
			return getDefaultMapper().readValue(obj, objClass);
		} catch (Exception e) {
			throw new ParrotJsonException(e.getMessage(), e);
		}
	}

	public static String streamToJson(InputStream is) {
		return streamToJson(is, false);
	}

	public static String streamToJson(InputStream is, boolean prettyPrinted) {
		String json = null;
		try {
			json = prettyPrinted ? getPrettyPrintedMapper().writeValueAsString(getPrettyPrintedMapper().readTree(is))
					: getDefaultMapper().writeValueAsString(getDefaultMapper().readTree(is));
		} catch (Exception e) {

		}
		return json;
	}

	public static String readFromFile(String path) throws ParrotJsonException {
		try {
			URL url = ClassLoader.getSystemResource(path);
			if (url == null) {
				throw new ParrotJsonException("Path '" + path + "' does not exists!");
			}
			return new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(path).toURI())));
		} catch (ParrotJsonException pe) {
			throw pe;
		} catch (Exception e) {
			throw new ParrotJsonException(e.getMessage(), e);
		}
	}

	public static <T> T jsonFileToObject(String resourcePath, Class<T> objClass) throws ParrotJsonException {
		try {
			return getDefaultMapper().readValue(readFromFile(resourcePath), objClass);
		} catch (Exception e) {
			throw new ParrotJsonException(e.getMessage(), e);
		}
	}

	public static JsonObject stringToJsonObject(String jsonString) {
		try (JsonReader reader = Json.createReader(new StringReader(jsonString))) {
			return reader.readObject();
		} catch (Exception e) {
			throw new ParrotJsonException(e.getMessage(), e);
		}
	}

	public static JsonObject objectToJsonObject(Object obj) throws ParrotJsonException {
		try {
			String jsonString = objectToJson(obj);
			return stringToJsonObject(jsonString);
		} catch (Exception e) {
			throw new ParrotJsonException(e.getMessage(), e);
		}
	}

	public static JsonArray stringToJsonArray(String jsonString) {
		try (JsonReader reader = Json.createReader(new StringReader(jsonString))) {
			return reader.readArray();
		}
	}

	public static JsonArray listToJsonArray(List<?> list) throws ParrotJsonException {
		try {
			final JsonArrayBuilder builder = Json.createArrayBuilder();
			for (Object obj : list) {
				builder.add(objectToJsonObject(obj));
			}
			return builder.build();
		} catch (Exception e) {
			throw new ParrotJsonException(e.getMessage(), e);
		}
	}

	public static <T> T jsonObjectToObject(JsonObject jsonObj, Class<T> objClass) throws ParrotJsonException {
		try {
			return jsonToObject(jsonObj.toString(), objClass);
		} catch (Exception e) {
			throw new ParrotJsonException(e.getMessage(), e);
		}
	}

	public static <T> List<T> jsonArraytToList(JsonArray jsonArr, Class<T> objClass) throws ParrotJsonException {
		ArrayList<T> list = new ArrayList<>();
		for (JsonValue jsonObj : jsonArr) {
			list.add(jsonObjectToObject((JsonObject) jsonObj, objClass));
		}
		return list;
	}

	public static String prettyPrint(String json) {
		try {
			return getPrettyPrintedMapper().writerWithDefaultPrettyPrinter()
					.writeValueAsString(getPrettyPrintedMapper().readValue(json, Object.class));
		} catch (Exception e) {
			throw new ParrotJsonException(e.getMessage(), e);
		}
	}

	public static String prettyPrint(JsonStructure json) {
		return jsonFormat(json, JsonGenerator.PRETTY_PRINTING);
	}

	private static String jsonFormat(JsonStructure json, String... options) {
		StringWriter stringWriter = new StringWriter();
		Map<String, Boolean> config = buildConfig(options);
		JsonWriterFactory writerFactory = Json.createWriterFactory(config);
		JsonWriter jsonWriter = writerFactory.createWriter(stringWriter);

		jsonWriter.write(json);
		jsonWriter.close();

		return stringWriter.toString();
	}

	private static Map<String, Boolean> buildConfig(String... options) {
		Map<String, Boolean> config = new HashMap<String, Boolean>();
		if (options != null) {
			for (String option : options) {
				config.put(option, true);
			}
		}
		return config;
	}
}

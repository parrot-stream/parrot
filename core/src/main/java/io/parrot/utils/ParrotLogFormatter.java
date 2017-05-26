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

public class ParrotLogFormatter {

	public static String formatLog(String headerText, String bodyText) {
		String header = "\n\n-----------------------------------------------------------------\n";
		String sep = "\n- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n";
		String footer = "\n-----------------------------------------------------------------\n\n";
		return header + headerText + sep + bodyText + footer;
	}

	public static String formatLog(String headerText, String bodyText, String footerText) {
		String header = "\n\n-----------------------------------------------------------------\n";
		String sep = "\n- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n";
		String footer = "\n-----------------------------------------------------------------\n\n";
		return header + headerText + sep + bodyText + sep + footerText + footer;
	}

	public static String formatLog(String headerText, Object... keyValues) {
		String header = "\n\n-----------------------------------------------------------------\n";
		String sep = "\n- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n";
		String footer = "\n-----------------------------------------------------------------\n\n";
		String bodyText = pad(keyValues);
		return header + headerText + sep + bodyText + footer;
	}

	public static String pad(Object... keyValues) {
		int i = 1;
		int maxLength = 0;
		String bodyText = "";
		for (Object keyValue : keyValues) {
			if (i % 2 == 1) {
				int length = keyValue != null ? String.valueOf(keyValue).length() : 0;
				maxLength = maxLength > length ? maxLength : length;
			}
			i++;
		}
		i = 1;
		String label = "";
		for (Object keyValue : keyValues) {
			if (i % 2 == 1) {
				label = keyValue != null ? String.valueOf(keyValue) : "";
				int length = label.length();
				for (int j = length; j < maxLength; j++) {
					label += " ";
				}
				label += ":\t";
				bodyText += label;
			} else {
				if (keyValue == null) {
					bodyText += "ND\n";
				} else if (keyValue instanceof String) {
					bodyText += (keyValue != null ? String.valueOf(keyValue) : "ND") + "\n";
				} else if (keyValue instanceof Object[]) {
					for (String value : (String[]) keyValue) {
						if (label.length() > 0) {
							bodyText += value + "\n";
							label = "";
						} else {
							String valueFull = "";
							for (int j = label.length(); j <= maxLength; j++) {
								valueFull += " ";
							}
							bodyText += valueFull + "\t" + value + "\n";
						}
					}
				}
			}
			i++;
		}
		return bodyText;
	}
}

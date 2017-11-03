/*
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
*/
package org.apache.edgent.topology.json;

import java.nio.charset.StandardCharsets;

import org.apache.edgent.function.Function;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Utilities for use of JSON and Json Objects in a streaming topology.
 */
public class JsonFunctions {

    /**
     * Get the JSON for a JsonObject.
     * 
     * TODO consider adding an override where the caller can specify
     * the number of significant digits to include in the string representation
     * of floating point types.
     * 
     * @return the JSON
     */
    public static Function<JsonObject,String> asString() {
        return jo -> jo.toString();
    }

    /**
     * Create a new JsonObject from JSON
     * @return the JsonObject
     */
    public static Function<String,JsonObject> fromString() {
        JsonParser jp = new JsonParser();
        return json -> jp.parse(json).getAsJsonObject();
    }

    /**
     * Get the UTF-8 bytes representation of the JSON for a JsonObject.
     * @return the byte[]
     */
    public static Function<JsonObject,byte[]> asBytes() {
        return jo -> jo.toString().getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Create a new JsonObject from the UTF8 bytes representation of JSON
     * @return the JsonObject
     */
    public static Function<byte[],JsonObject> fromBytes() {
        JsonParser jp = new JsonParser();
        return jsonbytes -> jp.parse(new String(jsonbytes, StandardCharsets.UTF_8)).getAsJsonObject();
    }

}

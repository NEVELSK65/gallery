/*
 * Copyright 2025 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.ai.edge.gallery.data

import org.junit.Assert.assertEquals
import org.junit.Test

class ConfigValueTest {

  // --- getIntConfigValue tests ---

  @Test
  fun getIntConfigValue_null_returnsDefault() {
    assertEquals(42, getIntConfigValue(null, 42))
  }

  @Test
  fun getIntConfigValue_intValue() {
    assertEquals(10, getIntConfigValue(ConfigValue.IntValue(10), 0))
  }

  @Test
  fun getIntConfigValue_floatValue_truncated() {
    assertEquals(3, getIntConfigValue(ConfigValue.FloatValue(3.9f), 0))
  }

  @Test
  fun getIntConfigValue_stringValue_returnsZero() {
    assertEquals(0, getIntConfigValue(ConfigValue.StringValue("hello"), 5))
  }

  // --- getFloatConfigValue tests ---

  @Test
  fun getFloatConfigValue_null_returnsDefault() {
    assertEquals(1.5f, getFloatConfigValue(null, 1.5f))
  }

  @Test
  fun getFloatConfigValue_intValue_convertedToFloat() {
    assertEquals(7.0f, getFloatConfigValue(ConfigValue.IntValue(7), 0f))
  }

  @Test
  fun getFloatConfigValue_floatValue() {
    assertEquals(3.14f, getFloatConfigValue(ConfigValue.FloatValue(3.14f), 0f))
  }

  @Test
  fun getFloatConfigValue_stringValue_returnsZero() {
    assertEquals(0f, getFloatConfigValue(ConfigValue.StringValue("test"), 5f))
  }

  // --- getStringConfigValue tests ---

  @Test
  fun getStringConfigValue_null_returnsDefault() {
    assertEquals("default", getStringConfigValue(null, "default"))
  }

  @Test
  fun getStringConfigValue_intValue() {
    assertEquals("42", getStringConfigValue(ConfigValue.IntValue(42), ""))
  }

  @Test
  fun getStringConfigValue_floatValue() {
    assertEquals("3.14", getStringConfigValue(ConfigValue.FloatValue(3.14f), ""))
  }

  @Test
  fun getStringConfigValue_stringValue() {
    assertEquals("hello", getStringConfigValue(ConfigValue.StringValue("hello"), ""))
  }

  // --- ConfigValue sealed class tests ---

  @Test
  fun configValue_intValue_equality() {
    val a = ConfigValue.IntValue(5)
    val b = ConfigValue.IntValue(5)
    assertEquals(a, b)
  }

  @Test
  fun configValue_floatValue_equality() {
    val a = ConfigValue.FloatValue(2.5f)
    val b = ConfigValue.FloatValue(2.5f)
    assertEquals(a, b)
  }

  @Test
  fun configValue_stringValue_equality() {
    val a = ConfigValue.StringValue("test")
    val b = ConfigValue.StringValue("test")
    assertEquals(a, b)
  }
}

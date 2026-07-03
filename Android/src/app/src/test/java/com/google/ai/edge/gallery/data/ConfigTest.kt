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
import org.junit.Assert.assertTrue
import org.junit.Test

class ConfigTest {

  // --- convertValueToTargetType tests ---

  @Test
  fun convertValueToTargetType_intFromInt() {
    assertEquals(42, convertValueToTargetType(42, ValueType.INT))
  }

  @Test
  fun convertValueToTargetType_intFromFloat() {
    assertEquals(3, convertValueToTargetType(3.7f, ValueType.INT))
  }

  @Test
  fun convertValueToTargetType_intFromDouble() {
    assertEquals(5, convertValueToTargetType(5.9, ValueType.INT))
  }

  @Test
  fun convertValueToTargetType_intFromString() {
    assertEquals(10, convertValueToTargetType("10", ValueType.INT))
  }

  @Test
  fun convertValueToTargetType_intFromInvalidString() {
    assertEquals("", convertValueToTargetType("abc", ValueType.INT))
  }

  @Test
  fun convertValueToTargetType_intFromBooleanTrue() {
    assertEquals(1, convertValueToTargetType(true, ValueType.INT))
  }

  @Test
  fun convertValueToTargetType_intFromBooleanFalse() {
    assertEquals(0, convertValueToTargetType(false, ValueType.INT))
  }

  @Test
  fun convertValueToTargetType_floatFromInt() {
    assertEquals(7.0f, convertValueToTargetType(7, ValueType.FLOAT))
  }

  @Test
  fun convertValueToTargetType_floatFromFloat() {
    assertEquals(3.14f, convertValueToTargetType(3.14f, ValueType.FLOAT))
  }

  @Test
  fun convertValueToTargetType_floatFromDouble() {
    assertEquals(2.5f, convertValueToTargetType(2.5, ValueType.FLOAT))
  }

  @Test
  fun convertValueToTargetType_floatFromString() {
    assertEquals(1.5f, convertValueToTargetType("1.5", ValueType.FLOAT))
  }

  @Test
  fun convertValueToTargetType_floatFromInvalidString() {
    assertEquals("", convertValueToTargetType("xyz", ValueType.FLOAT))
  }

  @Test
  fun convertValueToTargetType_floatFromBooleanTrue() {
    assertEquals(1f, convertValueToTargetType(true, ValueType.FLOAT))
  }

  @Test
  fun convertValueToTargetType_floatFromBooleanFalse() {
    assertEquals(0f, convertValueToTargetType(false, ValueType.FLOAT))
  }

  @Test
  fun convertValueToTargetType_doubleFromInt() {
    assertEquals(9.0, convertValueToTargetType(9, ValueType.DOUBLE))
  }

  @Test
  fun convertValueToTargetType_doubleFromFloat() {
    val result = convertValueToTargetType(1.5f, ValueType.DOUBLE) as Double
    assertEquals(1.5, result, 0.001)
  }

  @Test
  fun convertValueToTargetType_doubleFromDouble() {
    assertEquals(3.14, convertValueToTargetType(3.14, ValueType.DOUBLE))
  }

  @Test
  fun convertValueToTargetType_doubleFromString() {
    assertEquals(2.71, convertValueToTargetType("2.71", ValueType.DOUBLE))
  }

  @Test
  fun convertValueToTargetType_doubleFromBooleanTrue() {
    assertEquals(1.0, convertValueToTargetType(true, ValueType.DOUBLE))
  }

  @Test
  fun convertValueToTargetType_doubleFromBooleanFalse() {
    assertEquals(0.0, convertValueToTargetType(false, ValueType.DOUBLE))
  }

  @Test
  fun convertValueToTargetType_booleanFromInt_zero() {
    assertEquals(true, convertValueToTargetType(0, ValueType.BOOLEAN))
  }

  @Test
  fun convertValueToTargetType_booleanFromInt_nonzero() {
    assertEquals(false, convertValueToTargetType(5, ValueType.BOOLEAN))
  }

  @Test
  fun convertValueToTargetType_booleanFromBoolean() {
    assertEquals(true, convertValueToTargetType(true, ValueType.BOOLEAN))
    assertEquals(false, convertValueToTargetType(false, ValueType.BOOLEAN))
  }

  @Test
  fun convertValueToTargetType_booleanFromFloat_nonzero() {
    assertEquals(true, convertValueToTargetType(1.0f, ValueType.BOOLEAN))
  }

  @Test
  fun convertValueToTargetType_booleanFromFloat_zero() {
    assertEquals(false, convertValueToTargetType(0.0f, ValueType.BOOLEAN))
  }

  @Test
  fun convertValueToTargetType_booleanFromString_nonEmpty() {
    assertEquals(true, convertValueToTargetType("hello", ValueType.BOOLEAN))
  }

  @Test
  fun convertValueToTargetType_booleanFromString_empty() {
    assertEquals(false, convertValueToTargetType("", ValueType.BOOLEAN))
  }

  @Test
  fun convertValueToTargetType_stringFromAny() {
    assertEquals("42", convertValueToTargetType(42, ValueType.STRING))
    assertEquals("3.14", convertValueToTargetType(3.14f, ValueType.STRING))
    assertEquals("true", convertValueToTargetType(true, ValueType.STRING))
    assertEquals("hello", convertValueToTargetType("hello", ValueType.STRING))
  }

  // --- createLlmChatConfigs tests ---

  @Test
  fun createLlmChatConfigs_defaultValues_returns5Configs() {
    val configs = createLlmChatConfigs()
    assertEquals(5, configs.size)
  }

  @Test
  fun createLlmChatConfigs_firstConfigIsMaxTokensLabel() {
    val configs = createLlmChatConfigs(defaultMaxToken = 2048)
    val first = configs[0]
    assertEquals(ConfigEditorType.LABEL, first.type)
    assertEquals(ConfigKeys.MAX_TOKENS.label, first.key.label)
    assertEquals("2048", first.defaultValue)
  }

  @Test
  fun createLlmChatConfigs_topKSlider() {
    val configs = createLlmChatConfigs(defaultTopK = 50)
    val topK = configs[1] as NumberSliderConfig
    assertEquals(ConfigKeys.TOPK.label, topK.key.label)
    assertEquals(50f, topK.defaultValue)
    assertEquals(5f, topK.sliderMin)
    assertEquals(100f, topK.sliderMax)
    assertEquals(ValueType.INT, topK.valueType)
  }

  @Test
  fun createLlmChatConfigs_topPSlider() {
    val configs = createLlmChatConfigs(defaultTopP = 0.8f)
    val topP = configs[2] as NumberSliderConfig
    assertEquals(ConfigKeys.TOPP.label, topP.key.label)
    assertEquals(0.8f, topP.defaultValue)
    assertEquals(0.0f, topP.sliderMin)
    assertEquals(1.0f, topP.sliderMax)
    assertEquals(ValueType.FLOAT, topP.valueType)
  }

  @Test
  fun createLlmChatConfigs_temperatureSlider() {
    val configs = createLlmChatConfigs(defaultTemperature = 0.5f)
    val temp = configs[3] as NumberSliderConfig
    assertEquals(ConfigKeys.TEMPERATURE.label, temp.key.label)
    assertEquals(0.5f, temp.defaultValue)
    assertEquals(0.0f, temp.sliderMin)
    assertEquals(2.0f, temp.sliderMax)
    assertEquals(ValueType.FLOAT, temp.valueType)
  }

  @Test
  fun createLlmChatConfigs_acceleratorSegmentedButton() {
    val accelerators = listOf(Accelerator.CPU, Accelerator.GPU)
    val configs = createLlmChatConfigs(accelerators = accelerators)
    val acc = configs[4] as SegmentedButtonConfig
    assertEquals(ConfigKeys.ACCELERATOR.label, acc.key.label)
    assertEquals("CPU", acc.defaultValue)
    assertEquals(listOf("CPU", "GPU"), acc.options)
  }

  // --- getConfigValueString tests ---

  @Test
  fun getConfigValueString_floatFormatted() {
    val config =
      NumberSliderConfig(
        key = ConfigKeys.TEMPERATURE,
        sliderMin = 0f,
        sliderMax = 2f,
        defaultValue = 1f,
        valueType = ValueType.FLOAT,
      )
    assertEquals("1.50", getConfigValueString(1.5f, config))
  }

  @Test
  fun getConfigValueString_intNotFormatted() {
    val config =
      NumberSliderConfig(
        key = ConfigKeys.TOPK,
        sliderMin = 5f,
        sliderMax = 100f,
        defaultValue = 64f,
        valueType = ValueType.INT,
      )
    assertEquals("42", getConfigValueString(42, config))
  }

  // --- Config class tests ---

  @Test
  fun labelConfig_hasCorrectType() {
    val config = LabelConfig(key = ConfigKeys.NAME, defaultValue = "test")
    assertEquals(ConfigEditorType.LABEL, config.type)
    assertEquals(ValueType.STRING, config.valueType)
    assertEquals("test", config.defaultValue)
  }

  @Test
  fun booleanSwitchConfig_hasCorrectType() {
    val config = BooleanSwitchConfig(key = ConfigKeys.USE_GPU, defaultValue = true)
    assertEquals(ConfigEditorType.BOOLEAN_SWITCH, config.type)
    assertEquals(ValueType.BOOLEAN, config.valueType)
    assertEquals(true, config.defaultValue)
  }

  @Test
  fun segmentedButtonConfig_hasCorrectType() {
    val config =
      SegmentedButtonConfig(
        key = ConfigKeys.ACCELERATOR,
        defaultValue = "CPU",
        options = listOf("CPU", "GPU"),
      )
    assertEquals(ConfigEditorType.SEGMENTED_BUTTON, config.type)
    assertEquals(ValueType.STRING, config.valueType)
    assertEquals("CPU", config.defaultValue)
    assertEquals(listOf("CPU", "GPU"), config.options)
  }

  @Test
  fun numberSliderConfig_needReinitialization_defaultTrue() {
    val config =
      NumberSliderConfig(
        key = ConfigKeys.TOPK,
        sliderMin = 5f,
        sliderMax = 100f,
        defaultValue = 64f,
        valueType = ValueType.INT,
      )
    assertTrue(config.needReinitialization)
  }
}

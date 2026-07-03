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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ModelTest {

  // --- normalizedName tests ---

  @Test
  fun normalizedName_replacesSpecialCharacters() {
    val model = Model(name = "Gemma-2B (v1.0)")
    assertEquals("Gemma_2B__v1_0_", model.normalizedName)
  }

  @Test
  fun normalizedName_preservesAlphanumeric() {
    val model = Model(name = "Gemma2B")
    assertEquals("Gemma2B", model.normalizedName)
  }

  @Test
  fun normalizedName_replacesSlashes() {
    val model = Model(name = "models/gemma/2b")
    assertEquals("models_gemma_2b", model.normalizedName)
  }

  @Test
  fun normalizedName_replacesSpaces() {
    val model = Model(name = "my model name")
    assertEquals("my_model_name", model.normalizedName)
  }

  // --- preProcess tests ---

  @Test
  fun preProcess_setsConfigValues() {
    val model =
      Model(
        name = "test",
        configs =
          listOf(
            NumberSliderConfig(
              key = ConfigKeys.TOPK,
              sliderMin = 5f,
              sliderMax = 100f,
              defaultValue = 64f,
              valueType = ValueType.INT,
            ),
            NumberSliderConfig(
              key = ConfigKeys.TEMPERATURE,
              sliderMin = 0f,
              sliderMax = 2f,
              defaultValue = 1.0f,
              valueType = ValueType.FLOAT,
            ),
          ),
      )
    model.preProcess()

    assertEquals(64f, model.configValues[ConfigKeys.TOPK.label])
    assertEquals(1.0f, model.configValues[ConfigKeys.TEMPERATURE.label])
  }

  @Test
  fun preProcess_calculatesTotalBytes() {
    val model =
      Model(
        name = "test",
        sizeInBytes = 1000L,
        extraDataFiles =
          listOf(
            ModelDataFile(name = "extra1", url = "url1", downloadFileName = "f1", sizeInBytes = 200L),
            ModelDataFile(name = "extra2", url = "url2", downloadFileName = "f2", sizeInBytes = 300L),
          ),
      )
    model.preProcess()

    assertEquals(1500L, model.totalBytes)
  }

  @Test
  fun preProcess_noExtraFiles_totalBytesEqualsSizeInBytes() {
    val model = Model(name = "test", sizeInBytes = 500L)
    model.preProcess()
    assertEquals(500L, model.totalBytes)
  }

  // --- getExtraDataFile tests ---

  @Test
  fun getExtraDataFile_found() {
    val file = ModelDataFile(name = "vocab", url = "url", downloadFileName = "vocab.bin", sizeInBytes = 100L)
    val model = Model(name = "test", extraDataFiles = listOf(file))
    val result = model.getExtraDataFile("vocab")
    assertNotNull(result)
    assertEquals("vocab.bin", result!!.downloadFileName)
  }

  @Test
  fun getExtraDataFile_notFound() {
    val model = Model(name = "test", extraDataFiles = listOf())
    assertNull(model.getExtraDataFile("missing"))
  }

  // --- config value accessor tests ---

  @Test
  fun getIntConfigValue_returnsCorrectValue() {
    val model =
      Model(
        name = "test",
        configs =
          listOf(
            NumberSliderConfig(
              key = ConfigKeys.TOPK,
              sliderMin = 5f,
              sliderMax = 100f,
              defaultValue = 64f,
              valueType = ValueType.INT,
            )
          ),
      )
    model.preProcess()
    assertEquals(64, model.getIntConfigValue(ConfigKeys.TOPK))
  }

  @Test
  fun getFloatConfigValue_returnsCorrectValue() {
    val model =
      Model(
        name = "test",
        configs =
          listOf(
            NumberSliderConfig(
              key = ConfigKeys.TEMPERATURE,
              sliderMin = 0f,
              sliderMax = 2f,
              defaultValue = 0.95f,
              valueType = ValueType.FLOAT,
            )
          ),
      )
    model.preProcess()
    assertEquals(0.95f, model.getFloatConfigValue(ConfigKeys.TEMPERATURE))
  }

  @Test
  fun getBooleanConfigValue_returnsCorrectValue() {
    val model =
      Model(
        name = "test",
        configs = listOf(BooleanSwitchConfig(key = ConfigKeys.USE_GPU, defaultValue = true)),
      )
    model.preProcess()
    assertEquals(true, model.getBooleanConfigValue(ConfigKeys.USE_GPU))
  }

  @Test
  fun getStringConfigValue_returnsCorrectValue() {
    val model =
      Model(
        name = "test",
        configs = listOf(LabelConfig(key = ConfigKeys.MAX_TOKENS, defaultValue = "1024")),
      )
    model.preProcess()
    assertEquals("1024", model.getStringConfigValue(ConfigKeys.MAX_TOKENS))
  }

  @Test
  fun getIntConfigValue_missingKey_returnsDefault() {
    val model = Model(name = "test")
    model.preProcess()
    assertEquals(99, model.getIntConfigValue(ConfigKeys.TOPK, 99))
  }

  // --- EMPTY_MODEL tests ---

  @Test
  fun emptyModel_hasExpectedValues() {
    assertEquals("empty", EMPTY_MODEL.name)
    assertEquals("empty.tflite", EMPTY_MODEL.downloadFileName)
    assertEquals("", EMPTY_MODEL.url)
    assertEquals(0L, EMPTY_MODEL.sizeInBytes)
  }

  // --- ModelDownloadStatus tests ---

  @Test
  fun modelDownloadStatus_defaultValues() {
    val status = ModelDownloadStatus(status = ModelDownloadStatusType.NOT_DOWNLOADED)
    assertEquals(ModelDownloadStatusType.NOT_DOWNLOADED, status.status)
    assertEquals(0L, status.totalBytes)
    assertEquals(0L, status.receivedBytes)
    assertEquals("", status.errorMessage)
  }

  @Test
  fun modelDownloadStatus_withValues() {
    val status =
      ModelDownloadStatus(
        status = ModelDownloadStatusType.IN_PROGRESS,
        totalBytes = 1000L,
        receivedBytes = 500L,
        bytesPerSecond = 100L,
        remainingMs = 5000L,
      )
    assertEquals(ModelDownloadStatusType.IN_PROGRESS, status.status)
    assertEquals(1000L, status.totalBytes)
    assertEquals(500L, status.receivedBytes)
    assertEquals(100L, status.bytesPerSecond)
    assertEquals(5000L, status.remainingMs)
  }
}

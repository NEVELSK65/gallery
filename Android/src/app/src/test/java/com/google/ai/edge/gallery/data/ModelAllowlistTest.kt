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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ModelAllowlistTest {

  private fun createAllowedModel(
    name: String = "Test Model",
    modelId: String = "google/gemma-2b",
    modelFile: String = "model.bin",
    description: String = "A test model",
    sizeInBytes: Long = 2_000_000_000L,
    commitHash: String = "abc123",
    defaultConfig: DefaultConfig =
      DefaultConfig(topK = 40, topP = 0.9f, temperature = 0.8f, accelerators = null, maxTokens = 512),
    taskTypes: List<String> = listOf(BuiltInTaskId.LLM_CHAT),
    disabled: Boolean? = null,
    llmSupportImage: Boolean? = null,
    llmSupportAudio: Boolean? = null,
    llmSupportTinyGarden: Boolean? = null,
    llmSupportMobileActions: Boolean? = null,
    minDeviceMemoryInGb: Int? = null,
    bestForTaskTypes: List<String>? = null,
    localModelFilePathOverride: String? = null,
    url: String? = null,
  ): AllowedModel =
    AllowedModel(
      name = name,
      modelId = modelId,
      modelFile = modelFile,
      description = description,
      sizeInBytes = sizeInBytes,
      commitHash = commitHash,
      defaultConfig = defaultConfig,
      taskTypes = taskTypes,
      disabled = disabled,
      llmSupportImage = llmSupportImage,
      llmSupportAudio = llmSupportAudio,
      llmSupportTinyGarden = llmSupportTinyGarden,
      llmSupportMobileActions = llmSupportMobileActions,
      minDeviceMemoryInGb = minDeviceMemoryInGb,
      bestForTaskTypes = bestForTaskTypes,
      localModelFilePathOverride = localModelFilePathOverride,
      url = url,
    )

  @Test
  fun toModel_setsNameCorrectly() {
    val allowed = createAllowedModel(name = "Gemma 2B")
    val model = allowed.toModel()
    assertEquals("Gemma 2B", model.name)
  }

  @Test
  fun toModel_setsVersionFromCommitHash() {
    val allowed = createAllowedModel(commitHash = "def456")
    val model = allowed.toModel()
    assertEquals("def456", model.version)
  }

  @Test
  fun toModel_setsDescriptionAsInfo() {
    val allowed = createAllowedModel(description = "A powerful model")
    val model = allowed.toModel()
    assertEquals("A powerful model", model.info)
  }

  @Test
  fun toModel_constructsHuggingFaceDownloadUrl() {
    val allowed = createAllowedModel(modelId = "google/gemma-2b", commitHash = "abc", modelFile = "model.bin")
    val model = allowed.toModel()
    assertEquals(
      "https://huggingface.co/google/gemma-2b/resolve/abc/model.bin?download=true",
      model.url,
    )
  }

  @Test
  fun toModel_usesCustomUrl_whenProvided() {
    val customUrl = "https://example.com/model.bin"
    val allowed = createAllowedModel(url = customUrl)
    val model = allowed.toModel()
    assertEquals(customUrl, model.url)
  }

  @Test
  fun toModel_setsSizeInBytes() {
    val allowed = createAllowedModel(sizeInBytes = 5_000_000_000L)
    val model = allowed.toModel()
    assertEquals(5_000_000_000L, model.sizeInBytes)
  }

  @Test
  fun toModel_setsDownloadFileName() {
    val allowed = createAllowedModel(modelFile = "gemma.litertlm")
    val model = allowed.toModel()
    assertEquals("gemma.litertlm", model.downloadFileName)
  }

  @Test
  fun toModel_llmModel_hideBenchmarkAndRunAgain() {
    val allowed = createAllowedModel(taskTypes = listOf(BuiltInTaskId.LLM_CHAT))
    val model = allowed.toModel()
    assertFalse(model.showBenchmarkButton)
    assertFalse(model.showRunAgainButton)
  }

  @Test
  fun toModel_nonLlmModel_showBenchmarkAndRunAgain() {
    val allowed = createAllowedModel(taskTypes = listOf("image_classification"))
    val model = allowed.toModel()
    assertTrue(model.showBenchmarkButton)
    assertTrue(model.showRunAgainButton)
  }

  @Test
  fun toModel_llmModel_hasConfigs() {
    val allowed = createAllowedModel(taskTypes = listOf(BuiltInTaskId.LLM_CHAT))
    val model = allowed.toModel()
    assertEquals(5, model.configs.size)
  }

  @Test
  fun toModel_nonLlmModel_noConfigs() {
    val allowed = createAllowedModel(taskTypes = listOf("other_task"))
    val model = allowed.toModel()
    assertEquals(0, model.configs.size)
  }

  @Test
  fun toModel_setsLlmSupportImage() {
    val allowed = createAllowedModel(llmSupportImage = true)
    val model = allowed.toModel()
    assertTrue(model.llmSupportImage)
  }

  @Test
  fun toModel_setsLlmSupportAudio() {
    val allowed = createAllowedModel(llmSupportAudio = true)
    val model = allowed.toModel()
    assertTrue(model.llmSupportAudio)
  }

  @Test
  fun toModel_setsLlmSupportTinyGarden() {
    val allowed = createAllowedModel(llmSupportTinyGarden = true)
    val model = allowed.toModel()
    assertTrue(model.llmSupportTinyGarden)
  }

  @Test
  fun toModel_setsLlmSupportMobileActions() {
    val allowed = createAllowedModel(llmSupportMobileActions = true)
    val model = allowed.toModel()
    assertTrue(model.llmSupportMobileActions)
  }

  @Test
  fun toModel_nullSupportFlags_defaultToFalse() {
    val allowed = createAllowedModel()
    val model = allowed.toModel()
    assertFalse(model.llmSupportImage)
    assertFalse(model.llmSupportAudio)
    assertFalse(model.llmSupportTinyGarden)
    assertFalse(model.llmSupportMobileActions)
  }

  @Test
  fun toModel_setsMinDeviceMemory() {
    val allowed = createAllowedModel(minDeviceMemoryInGb = 8)
    val model = allowed.toModel()
    assertEquals(8, model.minDeviceMemoryInGb)
  }

  @Test
  fun toModel_setsBestForTaskIds() {
    val allowed = createAllowedModel(bestForTaskTypes = listOf(BuiltInTaskId.LLM_CHAT))
    val model = allowed.toModel()
    assertEquals(listOf(BuiltInTaskId.LLM_CHAT), model.bestForTaskIds)
  }

  @Test
  fun toModel_nullBestForTaskTypes_emptyList() {
    val allowed = createAllowedModel(bestForTaskTypes = null)
    val model = allowed.toModel()
    assertEquals(emptyList<String>(), model.bestForTaskIds)
  }

  @Test
  fun toModel_setsLocalModelFilePathOverride() {
    val allowed = createAllowedModel(localModelFilePathOverride = "/path/to/model")
    val model = allowed.toModel()
    assertEquals("/path/to/model", model.localModelFilePathOverride)
  }

  @Test
  fun toModel_nullLocalModelFilePathOverride_emptyString() {
    val allowed = createAllowedModel(localModelFilePathOverride = null)
    val model = allowed.toModel()
    assertEquals("", model.localModelFilePathOverride)
  }

  @Test
  fun toModel_setsLearnMoreUrl() {
    val allowed = createAllowedModel(modelId = "google/gemma-2b")
    val model = allowed.toModel()
    assertEquals("https://huggingface.co/google/gemma-2b", model.learnMoreUrl)
  }

  @Test
  fun toModel_acceleratorsConfig_cpuAndGpu() {
    val config = DefaultConfig(topK = 40, topP = 0.9f, temperature = 0.8f, accelerators = "cpu,gpu", maxTokens = 512)
    val allowed = createAllowedModel(defaultConfig = config, taskTypes = listOf(BuiltInTaskId.LLM_CHAT))
    val model = allowed.toModel()
    val accConfig = model.configs[4] as SegmentedButtonConfig
    assertEquals(listOf("CPU", "GPU"), accConfig.options)
  }

  @Test
  fun toModel_acceleratorsConfig_cpuOnly() {
    val config = DefaultConfig(topK = 40, topP = 0.9f, temperature = 0.8f, accelerators = "cpu", maxTokens = 512)
    val allowed = createAllowedModel(defaultConfig = config, taskTypes = listOf(BuiltInTaskId.LLM_CHAT))
    val model = allowed.toModel()
    val accConfig = model.configs[4] as SegmentedButtonConfig
    assertEquals(listOf("CPU"), accConfig.options)
    assertEquals("CPU", accConfig.defaultValue)
  }

  @Test
  fun toModel_defaultConfigValues_appliedToConfigs() {
    val config = DefaultConfig(topK = 30, topP = 0.85f, temperature = 0.7f, accelerators = null, maxTokens = 2048)
    val allowed = createAllowedModel(defaultConfig = config, taskTypes = listOf(BuiltInTaskId.LLM_PROMPT_LAB))
    val model = allowed.toModel()

    val maxTokenLabel = model.configs[0] as LabelConfig
    assertEquals("2048", maxTokenLabel.defaultValue)

    val topK = model.configs[1] as NumberSliderConfig
    assertEquals(30f, topK.defaultValue)

    val topP = model.configs[2] as NumberSliderConfig
    assertEquals(0.85f, topP.defaultValue)

    val temp = model.configs[3] as NumberSliderConfig
    assertEquals(0.7f, temp.defaultValue)
  }

  @Test
  fun toString_returnsModelIdSlashModelFile() {
    val allowed = createAllowedModel(modelId = "google/gemma-2b", modelFile = "model.bin")
    assertEquals("google/gemma-2b/model.bin", allowed.toString())
  }
}

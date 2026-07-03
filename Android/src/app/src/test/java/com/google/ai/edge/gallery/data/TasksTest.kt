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

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TasksTest {

  @Test
  fun isLegacyTasks_llmChat_isLegacy() {
    assertTrue(isLegacyTasks(BuiltInTaskId.LLM_CHAT))
  }

  @Test
  fun isLegacyTasks_llmPromptLab_isLegacy() {
    assertTrue(isLegacyTasks(BuiltInTaskId.LLM_PROMPT_LAB))
  }

  @Test
  fun isLegacyTasks_llmAskImage_isLegacy() {
    assertTrue(isLegacyTasks(BuiltInTaskId.LLM_ASK_IMAGE))
  }

  @Test
  fun isLegacyTasks_llmAskAudio_isLegacy() {
    assertTrue(isLegacyTasks(BuiltInTaskId.LLM_ASK_AUDIO))
  }

  @Test
  fun isLegacyTasks_llmMobileActions_notLegacy() {
    assertFalse(isLegacyTasks(BuiltInTaskId.LLM_MOBILE_ACTIONS))
  }

  @Test
  fun isLegacyTasks_llmTinyGarden_notLegacy() {
    assertFalse(isLegacyTasks(BuiltInTaskId.LLM_TINY_GARDEN))
  }

  @Test
  fun isLegacyTasks_unknownId_notLegacy() {
    assertFalse(isLegacyTasks("some_unknown_task"))
  }

  @Test
  fun isLegacyTasks_emptyString_notLegacy() {
    assertFalse(isLegacyTasks(""))
  }

  @Test
  fun builtInTaskId_constants_haveExpectedValues() {
    assertEquals("llm_chat", BuiltInTaskId.LLM_CHAT)
    assertEquals("llm_prompt_lab", BuiltInTaskId.LLM_PROMPT_LAB)
    assertEquals("llm_ask_image", BuiltInTaskId.LLM_ASK_IMAGE)
    assertEquals("llm_ask_audio", BuiltInTaskId.LLM_ASK_AUDIO)
    assertEquals("llm_mobile_actions", BuiltInTaskId.LLM_MOBILE_ACTIONS)
    assertEquals("llm_tiny_garden", BuiltInTaskId.LLM_TINY_GARDEN)
  }

  private fun assertEquals(expected: String, actual: String) {
    org.junit.Assert.assertEquals(expected, actual)
  }
}

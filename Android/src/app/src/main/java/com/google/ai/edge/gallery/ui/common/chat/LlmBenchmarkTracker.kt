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

package com.google.ai.edge.gallery.ui.common.chat

import com.google.ai.edge.gallery.ui.llmchat.LlmModelInstance
import com.google.ai.edge.litertlm.ExperimentalApi

/**
 * Tracks LLM inference benchmark metrics (time-to-first-token, prefill speed, decode speed,
 * latency).
 *
 * Create a new instance for each inference run. Call [onFirstToken] when the first token arrives and
 * [onSubsequentToken] for each additional token.
 */
class LlmBenchmarkTracker(private val initialPrefillTokens: Int = 0) {
  private val startMs: Long = System.currentTimeMillis()
  private var firstTokenMs: Long = 0L
  private var prefillTokens: Int = initialPrefillTokens
  private var decodeTokens: Int = 0
  private var isFirstToken: Boolean = true

  var timeToFirstToken: Float = 0f
    private set

  var prefillSpeed: Float = 0f
    private set

  /**
   * Call when the first token arrives. Returns true if this was actually the first token, false if
   * already called.
   */
  @OptIn(ExperimentalApi::class)
  fun onFirstToken(instance: LlmModelInstance): Boolean {
    if (!isFirstToken) return false
    firstTokenMs = System.currentTimeMillis()
    timeToFirstToken = (firstTokenMs - startMs) / 1000f
    prefillTokens += instance.conversation.getBenchmarkInfo().lastPrefillTokenCount
    prefillSpeed = prefillTokens / timeToFirstToken
    isFirstToken = false
    return true
  }

  fun onSubsequentToken() {
    decodeTokens++
  }

  fun computeDecodeSpeed(): Float {
    val elapsed = (System.currentTimeMillis() - firstTokenMs) / 1000f
    val speed = decodeTokens / elapsed
    return if (speed.isNaN()) 0f else speed
  }

  fun computeLatencySeconds(): Float {
    return (System.currentTimeMillis() - startMs) / 1000f
  }

  fun buildBenchmarkResult(
    running: Boolean,
    accelerator: String = "",
  ): ChatMessageBenchmarkLlmResult {
    return ChatMessageBenchmarkLlmResult(
      orderedStats = LLM_BENCHMARK_STATS,
      statValues =
        mutableMapOf(
          "prefill_speed" to prefillSpeed,
          "decode_speed" to computeDecodeSpeed(),
          "time_to_first_token" to timeToFirstToken,
          "latency" to computeLatencySeconds(),
        ),
      running = running,
      latencyMs = -1f,
      accelerator = accelerator,
    )
  }
}

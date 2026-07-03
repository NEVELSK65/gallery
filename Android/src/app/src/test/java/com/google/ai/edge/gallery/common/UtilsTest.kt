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

package com.google.ai.edge.gallery.common

import java.nio.ByteBuffer
import java.nio.ByteOrder
import org.junit.Assert.assertEquals
import org.junit.Test

class UtilsTest {

  // --- cleanUpMediapipeTaskErrorMessage tests ---

  @Test
  fun cleanUpMediapipeTaskErrorMessage_noTraceLocation_returnsOriginal() {
    val message = "Some error happened"
    assertEquals("Some error happened", cleanUpMediapipeTaskErrorMessage(message))
  }

  @Test
  fun cleanUpMediapipeTaskErrorMessage_withTraceLocation_truncates() {
    val message = "Error occurred here=== Source Location Trace: some/file.cc:123"
    assertEquals("Error occurred here", cleanUpMediapipeTaskErrorMessage(message))
  }

  @Test
  fun cleanUpMediapipeTaskErrorMessage_traceAtStart_returnsEmpty() {
    val message = "=== Source Location Trace: remaining"
    assertEquals("", cleanUpMediapipeTaskErrorMessage(message))
  }

  @Test
  fun cleanUpMediapipeTaskErrorMessage_emptyMessage_returnsEmpty() {
    assertEquals("", cleanUpMediapipeTaskErrorMessage(""))
  }

  // --- processLlmResponse tests ---

  @Test
  fun processLlmResponse_replacesBackslashN_withNewline() {
    val input = "Hello\\nWorld"
    assertEquals("Hello\nWorld", processLlmResponse(input))
  }

  @Test
  fun processLlmResponse_multipleBackslashN() {
    val input = "Line1\\nLine2\\nLine3"
    assertEquals("Line1\nLine2\nLine3", processLlmResponse(input))
  }

  @Test
  fun processLlmResponse_noBackslashN_unchanged() {
    val input = "Hello World"
    assertEquals("Hello World", processLlmResponse(input))
  }

  @Test
  fun processLlmResponse_emptyString() {
    assertEquals("", processLlmResponse(""))
  }

  // --- calculatePeakAmplitude tests ---

  @Test
  fun calculatePeakAmplitude_allZeros() {
    val buffer = ByteArray(8) { 0 }
    assertEquals(0, calculatePeakAmplitude(buffer, 8))
  }

  @Test
  fun calculatePeakAmplitude_positiveValues() {
    // Create a buffer with known 16-bit samples in little-endian
    val byteBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN)
    byteBuffer.putShort(100)
    byteBuffer.putShort(200)
    val bytes = byteBuffer.array()
    assertEquals(200, calculatePeakAmplitude(bytes, 4))
  }

  @Test
  fun calculatePeakAmplitude_negativeValues() {
    val byteBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN)
    byteBuffer.putShort(-300)
    byteBuffer.putShort(100)
    val bytes = byteBuffer.array()
    assertEquals(300, calculatePeakAmplitude(bytes, 4))
  }

  @Test
  fun calculatePeakAmplitude_mixedValues() {
    val byteBuffer = ByteBuffer.allocate(6).order(ByteOrder.LITTLE_ENDIAN)
    byteBuffer.putShort(50)
    byteBuffer.putShort(-500)
    byteBuffer.putShort(300)
    val bytes = byteBuffer.array()
    assertEquals(500, calculatePeakAmplitude(bytes, 6))
  }

  @Test
  fun calculatePeakAmplitude_partialBuffer() {
    val byteBuffer = ByteBuffer.allocate(6).order(ByteOrder.LITTLE_ENDIAN)
    byteBuffer.putShort(100)
    byteBuffer.putShort(999)
    byteBuffer.putShort(50)
    val bytes = byteBuffer.array()
    // Only read first 4 bytes (2 samples)
    assertEquals(999, calculatePeakAmplitude(bytes, 4))
  }

  @Test
  fun calculatePeakAmplitude_maxShortValue() {
    val byteBuffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN)
    byteBuffer.putShort(Short.MAX_VALUE)
    val bytes = byteBuffer.array()
    assertEquals(Short.MAX_VALUE.toInt(), calculatePeakAmplitude(bytes, 2))
  }
}

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

package com.google.ai.edge.gallery.customtasks.tinygarden

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TinyGardenToolsTest {

  // --- TinyGardenItem tests ---

  @Test
  fun tinyGardenItem_ordinals() {
    assertEquals(0, TinyGardenItem.SUNFLOWER.ordinal)
    assertEquals(1, TinyGardenItem.DAISY.ordinal)
    assertEquals(2, TinyGardenItem.ROSE.ordinal)
    assertEquals(3, TinyGardenItem.SPECIAL.ordinal)
    assertEquals(4, TinyGardenItem.WATERING_CAN.ordinal)
    assertEquals(5, TinyGardenItem.SCYTHE.ordinal)
  }

  @Test
  fun tinyGardenItem_labels() {
    assertEquals("sunflower", TinyGardenItem.SUNFLOWER.label)
    assertEquals("daisy", TinyGardenItem.DAISY.label)
    assertEquals("rose", TinyGardenItem.ROSE.label)
    assertEquals("secret", TinyGardenItem.SPECIAL.label)
    assertEquals("water", TinyGardenItem.WATERING_CAN.label)
    assertEquals("harvest", TinyGardenItem.SCYTHE.label)
  }

  // --- TinyGardenTools.waterPlots tests ---

  @Test
  fun waterPlots_callsCallbackWithWateringCanItem() {
    var receivedCommand: TinyGardenCommand? = null
    val tools = TinyGardenTools { command -> receivedCommand = command }

    tools.waterPlots(listOf(1, 3))

    assertEquals(TinyGardenItem.WATERING_CAN.ordinal + 1, receivedCommand!!.item)
    assertEquals(listOf(1, 3), receivedCommand!!.plots)
  }

  @Test
  fun waterPlots_returnsSuccessMap() {
    val tools = TinyGardenTools { }
    val result = tools.waterPlots(listOf(2))
    assertEquals("success", result["result"])
    assertEquals(listOf(2), result["plots"])
  }

  // --- TinyGardenTools.plantSeed tests ---

  @Test
  fun plantSeed_sunflower_callsCallbackWithCorrectItem() {
    var receivedCommand: TinyGardenCommand? = null
    val tools = TinyGardenTools { command -> receivedCommand = command }

    tools.plantSeed("sunflower", listOf(1))

    assertEquals(TinyGardenItem.SUNFLOWER.ordinal + 1, receivedCommand!!.item)
    assertEquals(listOf(1), receivedCommand!!.plots)
  }

  @Test
  fun plantSeed_daisy_callsCallbackWithCorrectItem() {
    var receivedCommand: TinyGardenCommand? = null
    val tools = TinyGardenTools { command -> receivedCommand = command }

    tools.plantSeed("daisy", listOf(2, 4))

    assertEquals(TinyGardenItem.DAISY.ordinal + 1, receivedCommand!!.item)
    assertEquals(listOf(2, 4), receivedCommand!!.plots)
  }

  @Test
  fun plantSeed_rose_callsCallbackWithCorrectItem() {
    var receivedCommand: TinyGardenCommand? = null
    val tools = TinyGardenTools { command -> receivedCommand = command }

    tools.plantSeed("rose", listOf(3))

    assertEquals(TinyGardenItem.ROSE.ordinal + 1, receivedCommand!!.item)
  }

  @Test
  fun plantSeed_special_callsCallbackWithCorrectItem() {
    var receivedCommand: TinyGardenCommand? = null
    val tools = TinyGardenTools { command -> receivedCommand = command }

    tools.plantSeed("special", listOf(1))

    assertEquals(TinyGardenItem.SPECIAL.ordinal + 1, receivedCommand!!.item)
  }

  @Test
  fun plantSeed_edgeGallery_callsCallbackWithSpecialItem() {
    var receivedCommand: TinyGardenCommand? = null
    val tools = TinyGardenTools { command -> receivedCommand = command }

    tools.plantSeed("edge gallery", listOf(2))

    assertEquals(TinyGardenItem.SPECIAL.ordinal + 1, receivedCommand!!.item)
  }

  @Test
  fun plantSeed_secret_callsCallbackWithSpecialItem() {
    var receivedCommand: TinyGardenCommand? = null
    val tools = TinyGardenTools { command -> receivedCommand = command }

    tools.plantSeed("secret", listOf(5))

    assertEquals(TinyGardenItem.SPECIAL.ordinal + 1, receivedCommand!!.item)
  }

  @Test
  fun plantSeed_caseInsensitive() {
    var receivedCommand: TinyGardenCommand? = null
    val tools = TinyGardenTools { command -> receivedCommand = command }

    tools.plantSeed("SUNFLOWER", listOf(1))

    assertEquals(TinyGardenItem.SUNFLOWER.ordinal + 1, receivedCommand!!.item)
  }

  @Test
  fun plantSeed_unknownSeed_doesNotCallCallback() {
    var callbackCalled = false
    val tools = TinyGardenTools { callbackCalled = true }

    tools.plantSeed("unknown_flower", listOf(1))

    assertTrue(!callbackCalled)
  }

  @Test
  fun plantSeed_returnsSuccessMap() {
    val tools = TinyGardenTools { }
    val result = tools.plantSeed("sunflower", listOf(1, 2))
    assertEquals("success", result["result"])
    assertEquals("sunflower", result["seed"])
    assertEquals(listOf(1, 2), result["plots"])
  }

  // --- TinyGardenTools.harvestPlots tests ---

  @Test
  fun harvestPlots_callsCallbackWithScytheItem() {
    var receivedCommand: TinyGardenCommand? = null
    val tools = TinyGardenTools { command -> receivedCommand = command }

    tools.harvestPlots(listOf(1, 2, 3))

    assertEquals(TinyGardenItem.SCYTHE.ordinal + 1, receivedCommand!!.item)
    assertEquals(listOf(1, 2, 3), receivedCommand!!.plots)
  }

  @Test
  fun harvestPlots_returnsSuccessMap() {
    val tools = TinyGardenTools { }
    val result = tools.harvestPlots(listOf(4))
    assertEquals("success", result["result"])
    assertEquals(listOf(4), result["plots"])
  }

  // --- TinyGardenCommand tests ---

  @Test
  fun tinyGardenCommand_equality() {
    val ts = System.currentTimeMillis()
    val a = TinyGardenCommand(item = 1, plots = listOf(1, 2), ts = ts)
    val b = TinyGardenCommand(item = 1, plots = listOf(1, 2), ts = ts)
    assertEquals(a, b)
  }
}

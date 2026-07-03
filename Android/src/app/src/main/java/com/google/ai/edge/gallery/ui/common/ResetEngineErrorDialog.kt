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

package com.google.ai.edge.gallery.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.ai.edge.gallery.R
import com.google.ai.edge.gallery.ui.theme.customColors

/**
 * An error dialog that offers the user a "Reset" option to re-initialize the engine after an
 * inference error.
 *
 * @param errorContent The error message to display.
 * @param confirmButtonColor The color for the "Reset" confirm button.
 * @param showResetNote Whether to show an additional warning note about resetting.
 * @param onDismiss Called when the dialog is dismissed without resetting.
 * @param onReset Called when the user confirms the reset action.
 */
@Composable
fun ResetEngineErrorDialog(
  errorContent: String,
  confirmButtonColor: Color,
  showResetNote: Boolean = false,
  onDismiss: () -> Unit,
  onReset: () -> Unit,
) {
  AlertDialog(
    title = { Text(stringResource(R.string.error)) },
    text = {
      if (showResetNote) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(errorContent, style = MaterialTheme.typography.bodyMedium)
          Text(
            stringResource(R.string.reset_note),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.customColors.warningTextColor,
          )
        }
      } else {
        Text(errorContent, style = MaterialTheme.typography.bodyMedium)
      }
    },
    onDismissRequest = onDismiss,
    dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) } },
    confirmButton = {
      Button(
        onClick = onReset,
        colors = ButtonDefaults.buttonColors(containerColor = confirmButtonColor),
      ) {
        Text(stringResource(R.string.reset), color = Color.White)
      }
    },
  )
}

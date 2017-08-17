/*
 * Created by Orchextra
 *
 * Copyright (C) 2017 Gigigo Mobile Services SL
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gigigo.orchextra.core.domain.entities

enum class ActionType {
  BROWSER,
  WEBVIEW,
  CUSTOM_SCHEME,
  SCANNER,
  IMAGE_RECOGNITION,
  NOTIFICATION,
  NOTHING;

  companion object {
    fun fromOxType(value: String): ActionType = when (value) {
      "browser" -> ActionType.BROWSER
      "webview" -> ActionType.WEBVIEW
      "custom_scheme" -> ActionType.CUSTOM_SCHEME
      "scan" -> ActionType.SCANNER
      "scan_vuforia" -> ActionType.IMAGE_RECOGNITION
      "notification" -> ActionType.NOTIFICATION
      else -> ActionType.NOTHING
    }
  }
}

data class Action(
    val trackId: String = "-1",
    val type: ActionType,
    val url: String = "",
    val notification: Notification = Notification(),
    val schedule: Schedule = Schedule()) {

  fun hasNotification(): Boolean {
    return notification.isNotEmpty()
  }

  fun hasSchedule(): Boolean {
    return schedule.isValid()
  }
}
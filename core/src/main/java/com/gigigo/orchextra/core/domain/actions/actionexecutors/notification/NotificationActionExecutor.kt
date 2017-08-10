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

package com.gigigo.orchextra.core.domain.actions.actionexecutors.notification

import android.app.AlertDialog
import android.content.Context
import com.gigigo.orchextra.core.Orchextra
import com.gigigo.orchextra.core.R
import com.gigigo.orchextra.core.domain.entities.Notification


class NotificationActionExecutor(private val context: Context) {

  fun showNotification(notification: Notification,
      actionExecutor: () -> Unit) = with(notification) {

    val builder = AlertDialog.Builder(context)
    builder.setTitle(title)
        .setMessage(body)
        .setIcon(R.drawable.ox_notification_large_icon)
        .setPositiveButton(android.R.string.ok, { dialog, _ ->
          dialog.dismiss()
          actionExecutor()
        })
        .show()
  }

  companion object Factory {

    fun create(): NotificationActionExecutor = NotificationActionExecutor(
        Orchextra.provideContext())
  }
}
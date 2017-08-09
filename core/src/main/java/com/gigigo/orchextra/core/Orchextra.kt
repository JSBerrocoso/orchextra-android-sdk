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

package com.gigigo.orchextra.core

import android.content.Context
import android.content.SharedPreferences
import com.gigigo.orchextra.core.actions.ActionManager
import com.gigigo.orchextra.core.domain.entities.Action
import com.gigigo.orchextra.core.domain.entities.ActionType.BROWSER
import com.gigigo.orchextra.core.domain.entities.ActionType.WEBVIEW
import com.gigigo.orchextra.core.domain.entities.Configuration
import com.gigigo.orchextra.core.domain.entities.Credentials
import com.gigigo.orchextra.core.domain.entities.LoadConfiguration
import com.gigigo.orchextra.core.domain.exceptions.NetworkException
import com.gigigo.orchextra.core.domain.interactor.GetAuthentication
import com.gigigo.orchextra.core.domain.interactor.GetConfiguration
import com.gigigo.orchextra.core.utils.extensions.getAppData
import com.gigigo.orchextra.core.utils.extensions.getDeviceData
import java.lang.IllegalStateException

object Orchextra {

  private var context: Context? = null
  private var apiKey: String? = null
  private var apiSecret: String? = null
  private lateinit var actionManager: ActionManager
  private var isReady = false
  private var orchextraStatusListener: OrchextraStatusListener? = null

  fun init(context: Context, apiKey: String, apiSecret: String) {

    this.context = context
    this.apiKey = apiKey
    this.apiSecret = apiSecret

    actionManager = ActionManager.create()

    val getAuthentication = GetAuthentication.create()
    getAuthentication.getAuthentication(Credentials(apiKey = apiKey, apiSecret = apiSecret),
        object : GetAuthentication.Callback {
          override fun onSuccess() {
            getConfiguration()
          }

          override fun onError(error: NetworkException) {
            println("getAuthentication onError: $error")
          }
        })
  }

  private fun getConfiguration() {

    val getConfiguration = GetConfiguration.create()

    val loadConfiguration = LoadConfiguration(
        app = context?.getAppData(),
        device = context?.getDeviceData())

    getConfiguration.get(loadConfiguration,
        object : GetConfiguration.Callback {
          override fun onSuccess(configuration: Configuration) {
            changeStatus(true)
            println("getConfiguration onSuccess")
          }

          override fun onError(error: NetworkException) {
            changeStatus(false)
            println("getAuthentication onError: $error")
          }
        })
  }

  fun openBrowser(url: String) {
    checkInitialization()
    actionManager.executeAction(
        Action(trackId = "-1", type = BROWSER, url = url))
  }

  fun openWebView(url: String) {
    checkInitialization()
    actionManager.executeAction(
        Action(trackId = "-1", type = WEBVIEW, url = url))
  }

  private fun changeStatus(isReady: Boolean) {
    Orchextra.isReady = isReady
    orchextraStatusListener?.onStatusChange(isReady)
  }

  fun setStatusListener(orchextraStatusListener: OrchextraStatusListener) {
    Orchextra.orchextraStatusListener = orchextraStatusListener
  }

  fun isReady(): Boolean {
    return Orchextra.isReady
  }

  private fun checkInitialization() {
    if (!isReady) {
      throw IllegalStateException("You must call init()")
    }
  }

  fun finish() {
    this.context = null
    this.apiKey = null
    this.apiSecret = null
    changeStatus(false)
  }

  fun provideContext(): Context {
    return context as Context
  }

  fun provideSharedPreferences(): SharedPreferences {
    return provideContext().getSharedPreferences("orchextra", Context.MODE_PRIVATE)
  }
}
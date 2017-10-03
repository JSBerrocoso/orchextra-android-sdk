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

package com.gigigo.orchextra.core.domain.triggers

import android.content.Context
import com.gigigo.orchextra.core.Orchextra
import com.gigigo.orchextra.core.OrchextraErrorListener
import com.gigigo.orchextra.core.data.datasources.network.models.toError
import com.gigigo.orchextra.core.domain.actions.ActionHandlerServiceExecutor
import com.gigigo.orchextra.core.domain.actions.actionexecutors.imagerecognition.ImageRecognitionActionExecutor
import com.gigigo.orchextra.core.domain.actions.actionexecutors.scanner.ScannerActionExecutor
import com.gigigo.orchextra.core.domain.entities.*
import com.gigigo.orchextra.core.domain.interactor.GetAction
import com.gigigo.orchextra.core.domain.interactor.GetTriggerConfiguration
import com.gigigo.orchextra.core.domain.interactor.GetTriggerList
import com.gigigo.orchextra.core.domain.interactor.ValidateTrigger
import com.gigigo.orchextra.core.utils.LogUtils
import kotlin.properties.Delegates

class TriggerManager(private val context: Context,
    private val getTriggerConfiguration: GetTriggerConfiguration,
    private val getTriggerList: GetTriggerList,
    private val getAction: GetAction, private val validateTrigger: ValidateTrigger,
    private val actionHandlerServiceExecutor: ActionHandlerServiceExecutor,
    private var orchextraErrorListener: OrchextraErrorListener) : TriggerListener {


  private val TAG = LogUtils.makeLogTag(TriggerManager::class.java)

  var configuration: Configuration = Configuration()
  var point: OxPoint by Delegates.observable(OxPoint(0.0, 0.0)) { _, _, _ ->
    getTriggerList.get(
        point = point,
        onSuccess = {
          configuration = it
          initGeofenceTrigger()
          initIndoorPositioningTrigger()
        },
        onError = { orchextraErrorListener.onError(it.toError()) })
  }

  var scanner by Delegates.observable(VoidTrigger<Any>() as OxTrigger<Any>)
  { _, _, new ->
    ScannerActionExecutor.scanner = new
  }

  var imageRecognizerCredentials: ImageRecognizerCredentials? = null

  var imageRecognizer by Delegates.observable(
      VoidTrigger<Any>() as OxTrigger<ImageRecognizerCredentials?>)
  { _, _, new ->

    ImageRecognitionActionExecutor.imageRecognizer = new
    initImageRecognizer()
  }

  var geofence by Delegates.observable(
      VoidTrigger<List<GeoMarketing>>() as OxTrigger<List<GeoMarketing>>) { _, _, _ ->
    initGeofenceTrigger()
  }

  var indoorPositioning by Delegates.observable(
      VoidTrigger<List<IndoorPositionConfig>>() as OxTrigger<List<IndoorPositionConfig>>) { _, _, _ ->
    initIndoorPositioningTrigger()
  }

  private fun initImageRecognizer() {
    imageRecognizerCredentials?.let {
      imageRecognizer.setConfig(it)
    }
  }

  private fun initGeofenceTrigger() {
    if (configuration.geoMarketing.isNotEmpty()) {
      geofence.setConfig(configuration.geoMarketing)
      try {
        geofence.init()
      } catch (exception: SecurityException) {
        orchextraErrorListener.onError(
            Error(code = Error.FATAL_ERROR, message = exception.message as String))
      }
    }
  }

  private fun initIndoorPositioningTrigger() {
    if (configuration.indoorPositionConfig.isNotEmpty()) {
      indoorPositioning.setConfig(configuration.indoorPositionConfig)
      try {
        indoorPositioning.init()
      } catch (exception: SecurityException) {
        orchextraErrorListener.onError(
            Error(code = Error.FATAL_ERROR, message = exception.message as String))
      }
    }
  }

  fun finish() {
    scanner.finish()
    imageRecognizer.finish()
    geofence.finish()
    indoorPositioning.finish()
  }

  override fun onTriggerDetected(trigger: Trigger) = validateTrigger.validate(trigger,
      onSuccess = {
        if (!it.isVoid()) {
          getActionByTrigger(it)
        }
      },
      onError = {
        if (it.toError().isValid()) {
          orchextraErrorListener.onError(it.toError())
        }
      })

  private fun getActionByTrigger(trigger: Trigger) = getAction.get(trigger,
      onSuccess = { actionHandlerServiceExecutor.execute(context = context, action = it) },
      onError = { orchextraErrorListener.onError(it.toError()) })

  companion object Factory {

    fun create(context: Context): TriggerManager = TriggerManager(context,
        GetTriggerConfiguration.create(),
        GetTriggerList.create(), GetAction.create(),
        ValidateTrigger.create(), ActionHandlerServiceExecutor.create(), Orchextra)
  }
}
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

package com.gigigo.orchextra.imagerecognizer

import android.app.Application
import com.gigigo.orchextra.core.domain.entities.ImageRecognizerCredentials
import com.gigigo.orchextra.core.domain.triggers.OxTrigger

class OxImageRecognizerImp private constructor(
    private val context: Application) : OxTrigger<ImageRecognizerCredentials> {

  private lateinit var credentials: ImageRecognizerCredentials

  override fun init() {
    ImageRecognizerActivity.open(context,
        licenseKey = credentials.licenseKey,
        clientAccessKey = credentials.clientAccessKey,
        clientSecretKey = credentials.clientSecretKey)
  }

  override fun setConfig(config: ImageRecognizerCredentials) {
    this.credentials = config
  }

  override fun finish() {
    ImageRecognizerActivity.finish()
  }

  companion object Factory {
    fun create(context: Application): OxImageRecognizerImp = OxImageRecognizerImp(context)

    fun recognizedPattern(code: String) {
      ImageRecognizerActivity.showCode(code)
    }
  }
}
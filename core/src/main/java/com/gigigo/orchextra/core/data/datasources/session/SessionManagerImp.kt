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

package com.gigigo.orchextra.core.data.datasources.session

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.gigigo.orchextra.core.domain.datasources.SessionManager
import com.gigigo.orchextra.core.domain.entities.Token
import com.squareup.moshi.JsonAdapter

class SessionManagerImp(private val sharedPreferences: SharedPreferences,
    private val tokenJsonAdapter: JsonAdapter<Token>) : SessionManager {

  private val TOKEN_KEY = "token_key"
  private var token = Token("")

  @SuppressLint("CommitPrefEdits")
  override fun saveSession(token: Token) {
    this.token = token
    val editor = sharedPreferences.edit()
    editor?.putString(TOKEN_KEY, tokenJsonAdapter.toJson(token))
    editor?.commit()
  }

  override fun getSession(): Token {
    if (token.isValid()) {
      return token
    } else {

      val tokenJson = sharedPreferences.getString(TOKEN_KEY, "")

      if (tokenJson == null || tokenJson.isBlank()) {
        return Token("")
      }

      token = tokenJsonAdapter.fromJson(tokenJson) ?: Token("")

      return token
    }
  }

  override fun hasSession(): Boolean = getSession().isValid()

  @SuppressLint("CommitPrefEdits")
  override fun clearSession() {
    token = Token("")
    val editor = sharedPreferences.edit()
    editor?.putString(TOKEN_KEY, tokenJsonAdapter.toJson(token))
    editor?.commit()
  }
}
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

val EMPTY_CRM = OxCRM()

data class OxCRM(
    val gender: String = "",
    val birthDate: String = "",
    val tags: List<String> = ArrayList(),
    val businessUnits: List<String> = ArrayList(),
    val customFields: Map<String, String> = HashMap()) {

  fun isEmpty(): Boolean = this == EMPTY_CRM

  fun isNotEmpty(): Boolean = !isEmpty()
}

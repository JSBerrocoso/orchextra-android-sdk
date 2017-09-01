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

package com.gigigo.orchextra.core.domain.interactor

import android.support.annotation.WorkerThread
import com.gigigo.orchextra.core.domain.datasources.DbDataSource
import com.gigigo.orchextra.core.domain.entities.Error
import com.gigigo.orchextra.core.domain.entities.Trigger
import com.gigigo.orchextra.core.domain.entities.TriggerType.VOID
import com.gigigo.orchextra.core.domain.exceptions.DbException
import com.gigigo.orchextra.core.domain.executor.PostExecutionThread
import com.gigigo.orchextra.core.domain.executor.PostExecutionThreadImp
import com.gigigo.orchextra.core.domain.executor.ThreadExecutor
import com.gigigo.orchextra.core.domain.executor.ThreadExecutorImp
import java.util.concurrent.TimeUnit

class ValidateTrigger constructor(private val threadExecutor: ThreadExecutor,
    private val postExecutionThread: PostExecutionThread,
    private val dbDataSource: DbDataSource) : Runnable {

  private lateinit var trigger: Trigger
  private lateinit var callback: Callback

  private val waitTime: Long = TimeUnit.MINUTES.toMillis(1)

  fun validate(trigger: Trigger, callback: Callback) {
    this.trigger = trigger
    this.callback = callback

    threadExecutor.execute(this)
  }

  override fun run() {
    try {
      notifySuccess(validate(trigger))

    } catch (error: DbException) {
      notifySuccess(trigger)
    } catch (e: Exception) {
      notifyError(Error(Error.INVALID_ERROR, ""))
    }
  }

  @WorkerThread
  fun validate(trigger: Trigger): Trigger {
    this.trigger = trigger

    val savedTrigger = dbDataSource.getTrigger(trigger.value)

    if (savedTrigger.isVoid()) {
      dbDataSource.saveTrigger(addEventIfNeed("enter"))
      return addEventIfNeed("enter")

    } else {
      if (savedTrigger.detectedTime + waitTime < System.currentTimeMillis()) {
        dbDataSource.saveTrigger(addEventIfNeed("stay"))
        return addEventIfNeed("stay")
      } else {
        return Trigger(VOID, "")
      }
    }
  }

  private fun addEventIfNeed(event: String): Trigger {
    return if (trigger.needEvent()) {
      trigger.copy(event = event)
    } else {
      trigger
    }
  }

  private fun notifySuccess(validTrigger: Trigger) {
    postExecutionThread.execute(Runnable { callback.onSuccess(validTrigger) })
  }

  private fun notifyError(error: Error) {
    postExecutionThread.execute(Runnable { callback.onError(error) })
  }

  interface Callback {

    fun onSuccess(validTrigger: Trigger)

    fun onError(error: Error)
  }

  companion object Factory {

    fun create(): ValidateTrigger = ValidateTrigger(ThreadExecutorImp, PostExecutionThreadImp,
        DbDataSource.create())
  }
}
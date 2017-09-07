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

package com.gigigo.orchextrasdk.demo.ui.triggerlog.adapter;

import android.support.annotation.NonNull;
import com.gigigo.orchextra.core.domain.entities.Trigger;
import java.util.Date;

public final class TriggerLog implements Comparable<TriggerLog> {

  @NonNull private final Date date;
  @NonNull private final Trigger trigger;

  public TriggerLog(@NonNull Date date, @NonNull Trigger trigger) {
    this.date = date;
    this.trigger = trigger;
  }

  @NonNull public Date getDate() {
    return date;
  }

  @NonNull public Trigger getTrigger() {
    return trigger;
  }

  @Override public int compareTo(@NonNull TriggerLog o) {
    return getDate().compareTo(o.getDate());
  }
}

/*
 * Created by Orchextra
 *
 * Copyright (C) 2016 Gigigo Mobile Services SL
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

package com.gigigo.orchextra.device.actions;

import com.gigigo.orchextra.device.notifications.dtos.AndroidBasicAction;
import com.gigigo.orchextra.device.notifications.dtos.mapper.AndroidBasicActionMapper;
import com.gigigo.orchextra.domain.model.actions.strategy.BasicAction;
import com.gigigo.orchextra.domain.interactors.actions.ActionDispatcher;
import me.panavtec.threaddecoratedview.views.ThreadSpec;


public class AndroidActionRecovery implements ActionRecovery {

  private final ActionDispatcher actionDispatcher;
  private final AndroidBasicActionMapper androidBasicActionMapper;
  //TODO LIB_CRUNCH threaddecoratedview
  private final ThreadSpec mainThreadSpec;
  //TODO LIB_CRUNCH threaddecoratedview
  public AndroidActionRecovery(ActionDispatcher actionDispatcher,
      AndroidBasicActionMapper androidBasicActionMapper, ThreadSpec mainThreadSpec) {
    this.actionDispatcher = actionDispatcher;
    this.androidBasicActionMapper = androidBasicActionMapper;
    this.mainThreadSpec = mainThreadSpec;
  }

  @Override public void recoverAction(AndroidBasicAction androidBasicAction) {
    final BasicAction basicAction = androidBasicActionMapper.externalClassToModel(androidBasicAction);
    mainThreadSpec.execute(new Runnable() {
      @Override public void run() {
        basicAction.performAction(actionDispatcher);
      }
    });
  }
}

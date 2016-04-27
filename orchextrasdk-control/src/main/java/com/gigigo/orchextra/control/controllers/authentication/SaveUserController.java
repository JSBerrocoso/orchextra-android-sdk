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

package com.gigigo.orchextra.control.controllers.authentication;

import com.gigigo.orchextra.control.InteractorResult;
import com.gigigo.orchextra.control.presenters.base.Presenter;
import com.gigigo.orchextra.control.controllers.config.ConfigObservable;
import com.gigigo.orchextra.control.invoker.InteractorExecution;
import com.gigigo.orchextra.control.invoker.InteractorInvoker;
import com.gigigo.orchextra.domain.interactors.user.SaveUserInteractor;
import com.gigigo.orchextra.domain.model.entities.authentication.Crm;
import com.gigigo.orchextra.domain.model.entities.proximity.OrchextraUpdates;
import orchextra.javax.inject.Provider;
import me.panavtec.threaddecoratedview.views.ThreadSpec;

public class SaveUserController extends Presenter<SaveUserDelegate> {

  private final InteractorInvoker interactorInvoker;
  //TODO LIB_CRUNCH Dagger
  private final Provider<InteractorExecution> interactorExecutionProvider;
  private final ConfigObservable configObservable;

  //TODO LIB_CRUNCH threaddecoratedview //TODO LIB_CRUNCH Dagger
  public SaveUserController(InteractorInvoker interactorInvoker,
      Provider<InteractorExecution> interactorExecutionProvider, ThreadSpec mainThreadSpec,
      ConfigObservable configObservable) {
    super(mainThreadSpec);
    this.interactorInvoker = interactorInvoker;
    this.interactorExecutionProvider = interactorExecutionProvider;
    this.configObservable = configObservable;
  }

  @Override public void onViewAttached() {
  }
  //TODO LIB_CRUNCH orchextrasdk-domain
  public void saveUser(Crm crm) {
    InteractorExecution interactorExecution = interactorExecutionProvider.get();
    //TODO LIB_CRUNCH orchextrasdk-domain
    SaveUserInteractor saveUserInteractor =
        (SaveUserInteractor) interactorExecution.getInteractor();
    saveUserInteractor.setCrm(crm);

    interactorExecution.result(new InteractorResult<OrchextraUpdates>() {
      @Override public void onResult(OrchextraUpdates orchextraUpdates) {
        notifyChanges(orchextraUpdates);
      }
    }).execute(interactorInvoker);
  }
  //TODO LIB_CRUNCH orchextrasdk-domain
  private void notifyChanges(OrchextraUpdates result) {
    if (result.hasChanges()) {
      configObservable.notifyObservers(result);
    }
  }
}

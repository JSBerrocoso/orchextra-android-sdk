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

package com.gigigo.orchextra.di.modules.device;

import com.gigigo.ggglib.ContextProvider;
import com.gigigo.orchextra.control.controllers.action.scheduler.ActionsSchedulerControllerImpl;
import com.gigigo.orchextra.control.controllers.action.scheduler.ActionsSchedulerPersistorNullImpl;
import com.gigigo.orchextra.device.actions.ActionExecutionImp;
import com.gigigo.orchextra.device.actions.ActionRecovery;
import com.gigigo.orchextra.device.actions.AndroidActionRecovery;
import com.gigigo.orchextra.device.actions.BrowserActionExecutor;
import com.gigigo.orchextra.device.actions.ScanActionExecutor;
import com.gigigo.orchextra.device.actions.VuforiaActionExecutor;
import com.gigigo.orchextra.device.actions.WebViewActionExecutor;
import com.gigigo.orchextra.device.actions.scheduler.ActionsSchedulerGcmImpl;
import com.gigigo.orchextra.device.actions.stats.StatsDispatcherImp;
import com.gigigo.orchextra.device.notifications.dtos.mapper.AndroidBasicActionMapper;
import com.gigigo.orchextra.device.notifications.dtos.mapper.AndroidNotificationMapper;
import com.gigigo.orchextra.device.permissions.GoogleApiPermissionChecker;
import com.gigigo.orchextra.di.qualifiers.BackThread;
import com.gigigo.orchextra.di.qualifiers.MainThread;
import com.gigigo.orchextra.domain.abstractions.actions.ActionExecution;
import com.gigigo.orchextra.domain.abstractions.actions.ActionsScheduler;
import com.gigigo.orchextra.domain.abstractions.actions.ActionsSchedulerController;
import com.gigigo.orchextra.domain.abstractions.actions.ActionsSchedulerPersistor;
import com.gigigo.orchextra.domain.abstractions.notifications.NotificationBehavior;
import com.gigigo.orchextra.domain.abstractions.stats.StatsDispatcher;
import com.gigigo.orchextra.domain.interactors.actions.ActionDispatcher;
import com.gigigo.orchextra.domain.interactors.actions.ActionDispatcherImpl;
import com.gigigo.orchextra.domain.interactors.actions.CustomSchemeReceiverContainer;
import com.google.gson.Gson;

import orchextra.javax.inject.Singleton;

import orchextra.dagger.Module;
import orchextra.dagger.Provides;
import gigigo.com.orchextra.data.datasources.api.stats.StatsDataSourceImp;
import me.panavtec.threaddecoratedview.views.ThreadSpec;


@Module
public class ActionsModule {
    //TODO LIB_CRUNCH gggLib
    @Provides
    @Singleton
    BrowserActionExecutor provideBrowserActionExecutor(ContextProvider contextProvider) {
        return new BrowserActionExecutor(contextProvider.getApplicationContext());
    }

    //TODO LIB_CRUNCH gggLib
    @Provides
    @Singleton
    WebViewActionExecutor provideWebViewActionExecutor(ContextProvider contextProvider) {
        return new WebViewActionExecutor(contextProvider.getApplicationContext());
    }

    @Provides
    @Singleton
    ScanActionExecutor provideScanActionExecutor() {
        return new ScanActionExecutor();
    }

    @Provides
    @Singleton
    VuforiaActionExecutor provideVuforiaActionExecutor() {
        return new VuforiaActionExecutor();
    }

    @Provides
    @Singleton
    ActionExecution provideActionExecution(BrowserActionExecutor browserActionExecutor,
                                           WebViewActionExecutor webViewActionExecutor,
                                           ScanActionExecutor scanActionExecutor,
                                           VuforiaActionExecutor vuforiaActionExecutor) {
        return new ActionExecutionImp(browserActionExecutor, webViewActionExecutor, scanActionExecutor,
                vuforiaActionExecutor);
    }

    @Provides
    @Singleton
    ActionDispatcher provideActionDispatcher(ActionExecution actionExecution, NotificationBehavior notificationBehavior,
                                             CustomSchemeReceiverContainer customSchemeReceiverContainer, StatsDispatcher statsDispatcher) {
        return new ActionDispatcherImpl(actionExecution, notificationBehavior, customSchemeReceiverContainer, statsDispatcher);
    }

    @Provides
    @Singleton
    AndroidBasicActionMapper provideAndroidBasicActionMapper(AndroidNotificationMapper androidNotificationMapper) {
        return new AndroidBasicActionMapper(androidNotificationMapper);
    }

    //TODO LIB_CRUNCH gggLib
    @Singleton
    @Provides
    ActionsScheduler provideActionsScheduler(ContextProvider contextProvider, Gson gson,
                                             AndroidBasicActionMapper androidBasicActionMapper, GoogleApiPermissionChecker googleApiPermissionChecker) {
        return new ActionsSchedulerGcmImpl(contextProvider.getApplicationContext(), gson,
                androidBasicActionMapper, googleApiPermissionChecker);
    }

    @Singleton
    @Provides
    Gson gson() {
        return new Gson();
    }
    //TODO LIB_CRUNCH orchextrasdk-control
    @Singleton
    @Provides
    ActionsSchedulerPersistor provideActionsSchedulerPersistorNull() {
        return new ActionsSchedulerPersistorNullImpl();
    }

    @Singleton
    @Provides
    ActionsSchedulerController provideActionsSchedulerController(
            ActionsScheduler actionsScheduler, ActionsSchedulerPersistor actionsSchedulerPersistor) {
//TODO LIB_CRUNCH orchextrasdk-control
        if (actionsScheduler.hasPersistence() &&
                !(actionsSchedulerPersistor instanceof ActionsSchedulerPersistorNullImpl)) {
            throw new IllegalArgumentException("Param ActionsSchedulerPersistor in"
                    + " ActionsSchedulerControllerImpl MUST be NullObject when ActionsScheduler "
                    + "already supports persistence ");
        }
//TODO LIB_CRUNCH orchextrasdk-control
        return new ActionsSchedulerControllerImpl(actionsScheduler, actionsSchedulerPersistor);
    }

    //TODO LIB_CRUNCH threaddecoratedview
    @Provides
    @Singleton
    ActionRecovery providesActionRecovery(AndroidBasicActionMapper androidBasicActionMapper,
                                          ActionDispatcher actionDispatcher, @MainThread ThreadSpec mainThreadSpec) {
        return new AndroidActionRecovery(actionDispatcher, androidBasicActionMapper, mainThreadSpec);
    }

    //TODO LIB_CRUNCH threaddecoratedview
    @Provides
    @Singleton
    StatsDispatcher provideStatsDispatcher(StatsDataSourceImp statsDataSourceImp,
                                           @BackThread ThreadSpec bacThreadSpec) {
        return new StatsDispatcherImp(statsDataSourceImp, bacThreadSpec);
    }
}

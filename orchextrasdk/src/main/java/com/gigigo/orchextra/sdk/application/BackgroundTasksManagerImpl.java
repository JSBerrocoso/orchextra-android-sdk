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

package com.gigigo.orchextra.sdk.application;

import com.gigigo.ggglib.permission.PermissionWrapper;
import com.gigigo.ggglib.permission.permissions.Permission;
import com.gigigo.orchextra.domain.abstractions.background.BackgroundTasksManager;
import com.gigigo.orchextra.sdk.OrchextraTasksManager;

public class BackgroundTasksManagerImpl implements BackgroundTasksManager {

  private final OrchextraTasksManager orchextraTasksManager;
  private final PermissionWrapper permissionWrapper;
  private final Permission permission;

  public BackgroundTasksManagerImpl(OrchextraTasksManager orchextraTasksManager,
      PermissionWrapper permissionWrapper, Permission permissionParam) {
    this.orchextraTasksManager = orchextraTasksManager;
    this.permissionWrapper = permissionWrapper;

    this.permission = permissionParam;//new PermissionLocationImp(null);
  }

  @Override public void startBackgroundTasks() {
    boolean granted = permissionWrapper.isGranted(permission);
    if (granted) {
      orchextraTasksManager.initBackgroundTasks(granted);
    }
  }

  @Override public void reStartBackgroundTasks() {
    if (permissionWrapper.isGranted(permission)) {
      orchextraTasksManager.reStartBackgroundTasks();
    }
  }

  @Override public void pauseBackgroundTasks() {
    orchextraTasksManager.pauseBackgroundTasks();
  }

  @Override public void finalizeBackgroundTasks() {
    orchextraTasksManager.stopBackgroundServices();
  }

  @Override public void requestConfig() {
    orchextraTasksManager.initBootTasks();
  }
}

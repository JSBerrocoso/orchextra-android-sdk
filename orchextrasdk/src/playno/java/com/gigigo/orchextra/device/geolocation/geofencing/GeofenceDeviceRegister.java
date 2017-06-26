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

package com.gigigo.orchextra.device.geolocation.geofencing;

import android.os.Bundle;
import com.gigigo.ggglib.device.providers.ContextProvider;
import com.gigigo.ggglib.permission.PermissionWrapper;
import com.gigigo.ggglib.permission.listeners.UserPermissionRequestResponseListener;
import com.gigigo.orchextra.device.GoogleApiClientConnector;
import com.gigigo.orchextra.device.geolocation.geofencing.mapper.AndroidGeofenceConverter;
import com.gigigo.orchextra.device.geolocation.geofencing.pendingintent.GeofencePendingIntentCreator;
import com.gigigo.orchextra.device.permissions.LocationPermission;
import com.gigigo.orchextra.domain.abstractions.device.OrchextraLogger;
import com.gigigo.orchextra.domain.model.entities.geofences.OrchextraGeofenceUpdates;

public class GeofenceDeviceRegister {//implements ResultCallback<Status> {

  private final ContextProvider contextProvider;
  private final GeofencePendingIntentCreator geofencePendingIntentCreator;
  private final GoogleApiClientConnector googleApiClientConnector;
  private final PermissionWrapper permissionWrapper;
  private final LocationPermission accessFineLocationPermissionImp;
  private final AndroidGeofenceConverter androidGeofenceConverter;
  private final OrchextraLogger orchextraLogger;

  private OrchextraGeofenceUpdates geofenceUpdates;
  private UserPermissionRequestResponseListener userPermissionResponseListener =
      new UserPermissionRequestResponseListener() {
        @Override public void onPermissionAllowed(boolean permissionAllowed, int i) {
          if (permissionAllowed) {
            registerGeofence();
          }
        }
      };
  /* @Override
   public void onResult(Status status) {
       if (status.isSuccess()) {
           orchextraLogger.log("Registered Geofences Success!", OrchextraSDKLogLevel.DEBUG);
       } else if (status.hasResolution()) {
           Activity currentActivity = contextProvider.getCurrentActivity();
           if (currentActivity != null) {
               try {
                   status.startResolutionForResult(currentActivity, status.getStatusCode());
               } catch (IntentSender.SendIntentException e) {
                   orchextraLogger.log("Geofences Handle resolution!", OrchextraSDKLogLevel.DEBUG);
               }
           }
       } else if (status.isCanceled()) {
           orchextraLogger.log("Registered Geofences Canceled!", OrchextraSDKLogLevel.DEBUG);
       } else if (status.isInterrupted()) {
           orchextraLogger.log("Registered Geofences Interrupted!", OrchextraSDKLogLevel.DEBUG);
       }

       if (googleApiClientConnector.isConnected()) {
           googleApiClientConnector.disconnected();
       }
   }
*/
  private GoogleApiClientConnector.OnConnectedListener onConnectedRegisterGeofenceListener =
      new GoogleApiClientConnector.OnConnectedListener() {
        @Override public void onConnected(Bundle bundle) {
          registerGeofencesOnDevice();
        }
      };
  private GoogleApiClientConnector.OnConnectedListener onConnectedRemoveGeofenceListener =
      new GoogleApiClientConnector.OnConnectedListener() {
        @Override public void onConnected(Bundle bundle) {
          clearGeofences();
        }
      };

  public GeofenceDeviceRegister(ContextProvider contextProvider,
      GoogleApiClientConnector googleApiClientConnector,
      GeofencePendingIntentCreator geofencePendingIntentCreator,
      PermissionWrapper permissionWrapper, LocationPermission accessFineLocationPermissionImp,
      AndroidGeofenceConverter androidGeofenceConverter, OrchextraLogger orchextraLogger) {

    this.contextProvider = contextProvider;
    this.googleApiClientConnector = googleApiClientConnector;
    this.geofencePendingIntentCreator = geofencePendingIntentCreator;
    this.permissionWrapper = permissionWrapper;
    this.accessFineLocationPermissionImp = accessFineLocationPermissionImp;
    this.androidGeofenceConverter = androidGeofenceConverter;
    this.orchextraLogger = orchextraLogger;
  }

  public void register(OrchextraGeofenceUpdates geofenceUpdates) {
    this.geofenceUpdates = geofenceUpdates;

    googleApiClientConnector.setOnConnectedListener(onConnectedRegisterGeofenceListener);
    googleApiClientConnector.connect();
  }

  private void registerGeofencesOnDevice() {
    boolean isGranted = permissionWrapper.isGranted(accessFineLocationPermissionImp);
    if (isGranted) {
      registerGeofence();
    } else {
      if (contextProvider.getCurrentActivity() != null) {
        permissionWrapper.askForPermission(userPermissionResponseListener,
            accessFineLocationPermissionImp);
      }
    }
  }

  @SuppressWarnings("ResourceType") private void registerGeofence() {
    orchextraLogger.log(
        "DO nothing " + geofenceUpdates.getDeleteGeofences().size() + " geofences...");
  }

  public void clean() {
    clearGeofences();
  }

  private void clearGeofences() {
  }
}

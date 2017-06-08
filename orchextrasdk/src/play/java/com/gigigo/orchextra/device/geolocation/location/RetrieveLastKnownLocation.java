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

package com.gigigo.orchextra.device.geolocation.location;

import android.location.Location;
import com.gigigo.ggglib.device.providers.ContextProvider;
import com.gigigo.orchextra.device.GoogleApiClientConnector;
import com.gigigo.orchextra.device.permissions.PermissionLocationImp;
import com.gigigo.ggglib.permission.interfaces.PermissionChecker;
import com.gigigo.ggglib.permission.interfaces.UserPermissionRequestResponseListener;
import com.google.android.gms.location.LocationServices;

public class RetrieveLastKnownLocation {

  private final ContextProvider contextProvider;
  private final GoogleApiClientConnector googleApiClientConnector;
  private final RetrieveLocationByGpsOrNetworkProvider retrieveLocationByGpsOrNetworkProvider;
  private final PermissionChecker permissionChecker;
  private final PermissionLocationImp accessFineLocationPermissionImp;

  private OnLastKnownLocationListener onLastKnownLocationListener;
  private GoogleApiClientConnector.OnConnectedListener onConnectedListener =
      new GoogleApiClientConnector.OnConnectedListener() {
        @Override public void onConnected() {
          //askPermissionAndGetLastKnownLocation();
          getLastKnownLocation();
        }

        @Override public void onConnectionFailed() {
          boolean isGranted = permissionChecker.isGranted(accessFineLocationPermissionImp);
          if (isGranted) {
            getNetworkGpsLocation();
          }
        }
      };
  private UserPermissionRequestResponseListener userPermissionResponseListener =
      new UserPermissionRequestResponseListener() {
        @Override public void onPermissionAllowed(boolean permissionAllowed, int i) {
          getLastKnownLocation();
        }
      };

  public RetrieveLastKnownLocation(ContextProvider contextProvider,
      GoogleApiClientConnector googleApiClientConnector,
      RetrieveLocationByGpsOrNetworkProvider retrieveLocationByGpsOrNetworkProvider,
      PermissionChecker permissionChecker, PermissionLocationImp accessFineLocationPermissionImp) {

    this.contextProvider = contextProvider;
    this.googleApiClientConnector = googleApiClientConnector;
    this.retrieveLocationByGpsOrNetworkProvider = retrieveLocationByGpsOrNetworkProvider;
    this.permissionChecker = permissionChecker;
    this.accessFineLocationPermissionImp = accessFineLocationPermissionImp;
  }

  public void getLastKnownLocation(OnLastKnownLocationListener onLastKnownLocationListener) {
    this.onLastKnownLocationListener = onLastKnownLocationListener;
    if (googleApiClientConnector != null) {
      googleApiClientConnector.setOnConnectedListener(onConnectedListener);
      googleApiClientConnector.connect();
    }
  }

  public void askPermissionAndGetLastKnownLocation() {
    boolean isGranted = permissionChecker.isGranted(accessFineLocationPermissionImp);
    if (isGranted) {
      getLastKnownLocation();
    } else {
      if (contextProvider.getCurrentActivity() != null) {
        permissionChecker.askForPermission(userPermissionResponseListener,
            accessFineLocationPermissionImp); //contextProvider.getCurrentActivity());
      }
    }
  }

  @SuppressWarnings("ResourceType") private void getLastKnownLocation() {
    if (googleApiClientConnector != null
        && googleApiClientConnector.getGoogleApiClient() != null
        && googleApiClientConnector.isConnected()) {
      Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(
          googleApiClientConnector.getGoogleApiClient());
      if (onLastKnownLocationListener != null) {
        onLastKnownLocationListener.onLastKnownLocation(lastLocation);
      }
    } else {
      onLastKnownLocationListener.onLastKnownLocation(null);
    }
  }

  @SuppressWarnings("ResourceType") private void getNetworkGpsLocation() {
    Location location = retrieveLocationByGpsOrNetworkProvider.retrieveLocation();

    if (onLastKnownLocationListener != null) {
      onLastKnownLocationListener.onLastKnownLocation(location);
    }
  }

  public interface OnLastKnownLocationListener {
    void onLastKnownLocation(Location location);
  }
}

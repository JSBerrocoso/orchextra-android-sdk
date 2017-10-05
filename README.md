# Orchextra SDK for Android
[![Build Status](https://travis-ci.org/Orchextra/orchextra-android-sdk.svg?branch=ocm_integration)](https://travis-ci.org/Orchextra/orchextra-android-sdk)
[![codecov.io](https://codecov.io/github/Orchextra/orchextra-android-sdk/coverage.svg?branch=master)](https://codecov.io/github/Orchextra/orchextra-android-sdk)
![Language](https://img.shields.io/badge/Language-Android-brightgreen.svg)
![Version](https://img.shields.io/badge/Version-4.1.0RC-blue.svg)
 ![](https://img.shields.io/badge/Min%20SDK-18-green.svg)

A library that gives you access to Orchextra platform from your Android sdkVersionAppInfo.

## Getting started
Start by creating a project in [Orchextra Dashboard](https://dashboard.orchextra.io/start/login), if you haven't done it yet. Go to "Setting" > "SDK Configuration" to get the **api key** and **api secret**, you will need these values to start Orchextra SDK.

## Overview
Orchextra SDK is composed of **Orchextra Core**, and add-ons

#### Orchextra Core
- Geofences
- IBeacons
- Push Notifications
- Barcode/qr Scanner
- Image recognition

## Add the dependency

Add gigigo maven repository 

```groovy
allprojects {
   repositories {
       maven {
           url  "https://dl.bintray.com/gigigo-desarrollo/maven" 
       }
   }
}
```

Add dependencies you need

```groovy
  compile 'com.gigigo.orchextra:core:0.0.1'
  compile 'com.gigigo.orchextra:geofence:0.0.1'
  compile 'com.gigigo.orchextra:indoorpositioning:0.0.1'
  compile 'com.gigigo.orchextra:scanner:0.0.1'
```

#Init Orchextra
```java
    Orchextra orchextra = Orchextra.INSTANCE;
    orchextra.setStatusListener(orchextraStatusListener);
    orchextra.init(getApplication(), apiKey, apiSecret, true);
```

#Add trigger implementation
```java
  orchextra.getTriggerManager().setGeofence(OxGeofenceImp.Factory.create(getApplication()));
```

#Get Orchextra errors
```java
  orchextra.setErrorListener(new OrchextraErrorListener() {
    @Override public void onError(@NonNull Error error) {
      hideLoading();
      Log.e(TAG, error.toString());
      Toast.makeText(EditActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT)
          .show();
    }
  });
```

License
=======

    Copyright 2016 Orchextra

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


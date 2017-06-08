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
package gigigo.com.orchextrasdk;

import android.app.Application;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.util.Log;
import com.gigigo.imagerecognition.vuforia.ImageRecognitionVuforia;
import com.gigigo.orchextra.CustomSchemeReceiver;
import com.gigigo.orchextra.Orchextra;
import com.gigigo.orchextra.OrchextraBuilder;
import com.gigigo.orchextra.OrchextraCompletionCallback;
import com.gigigo.orchextra.OrchextraLogLevel;
import com.gigigo.orchextra.device.bluetooth.beacons.BeaconBackgroundModeScan;
import gigigo.com.orchextrasdk.adonservices.UpdateConfigReceiver;
import gigigo.com.orchextrasdk.adonservices.UpdateConfigUtility;

public class App extends Application implements OrchextraCompletionCallback, CustomSchemeReceiver {

  //testdoublepush en Pr0/vuforia setted
  //  public static final String API_KEY = "3805de10dd1b363d3030456a86bf01a7449f4b4f";
  //  public static final String API_SECRET = "2f15ac2b9d291034a2f66eea784f9b3be6e668e6";
  //staging
  //public static final String API_KEY = "ee6d91068298fd04ef9ea609def0f516ebda97dc";
  //public static final String API_SECRET = "6efda2dc815af99c4e53b588e118ef76ae53fbfb";
  //staging androidSDK
  //public static final String API_KEY = "34a4654b9804eab82aae05b2a5f949eb2a9f412c";
  //public static final String API_SECRET = "2d5bce79e3e6e9cabf6d7b040d84519197dc22f3";
  //REPSOL RELEASE
  public static String API_KEY = "7bb9fa0f9b7a02846383fd6284d3c74b8155644c";    //[UAT][CSE] - DEMO CONTENT
  public static String API_SECRET = "3295dc8de90300e2977e6cec5b28b614fc644934";

  public static final String SENDER_ID = "Your_Sender_ID";
  //if is not valid sender id, orchextra disabled push receive(only inform for using pushnotifications)
  public static final String GIGIGO_URL = "http://research.gigigo.com";
  public static final String CUSTOM_SCHEME = "webview://";

  // public static MotionServiceUtility mMotionServiceUtility;

  @Override public void onCreate() {
    super.onCreate();
    initOrchextra();
  }

  public void initOrchextra() {

    OrchextraBuilder builder = new OrchextraBuilder(this)
        .setApiKeyAndSecret(API_KEY, API_SECRET)
        .setLogLevel(OrchextraLogLevel.NETWORK)
        .setOrchextraCompletionCallback(this)
        .setGcmSenderId("117687721829")
        .setNotificationActivityClass(MainActivity.class.toString())
        .setImageRecognitionModule(new ImageRecognitionVuforia())
        .setBackgroundBeaconScanMode(BeaconBackgroundModeScan.HARDCORE);
    //init Orchextra with builder configuration
    Orchextra.initialize(builder);
    //your can re set custom Scheme in other places(activities,services..)
    Orchextra.setCustomSchemeReceiver(this);

    //start Orchextra running, you can call stop() if you need
    Orchextra.start(); //for only one time, each time you start Orchextra get orchextra project configuration is call

    // use instead BeaconMode
    //Orchextra.updateBackgroundPeriodBetweenScan(BackgroundBeaconsRangingTimeType.SEVERE.getLongValue());

    //region AdOns
    // mMotionServiceUtility = new MotionServiceUtility(this);

    //bluetooth //reset BT each 65min is a test, is not c00l, but prevents beaconsscan errors
    // BluetoothResetUtility bluetoothResetUtility = new BluetoothResetUtility(this);
    // bluetoothResetUtility.createAlarmResetBluetoothEachTime(60 * 1000 * 65);

    //UpdateConfigWrapper Only when Start
    UpdateConfigUtility updater = new UpdateConfigUtility(this);
    updater.createUpdateConfigurationByTime(60 * 1000 * 60);
    enablerUpdateConfigReBootService(this, true);
    //endregion
  }

  private void enablerUpdateConfigReBootService(Application application, boolean bState) {
    int componentEnabledState;
    if (!bState) {
      componentEnabledState = PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
    } else {
      componentEnabledState = PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
    }
    ComponentName component = new ComponentName(application, UpdateConfigReceiver.class);
    application.getPackageManager()
        .setComponentEnabledSetting(component, componentEnabledState, PackageManager.DONT_KILL_APP);
  }

  @Override public void onSuccess() {
    Log.d("APP", "onSuccess");
  }

  @Override public void onError(String s) {

    // OrchextraBusinessErrors errorCode= (OrchextraBusinessErrors)s;

    //OrchextraBusinessErrors {
    //  NO_AUTH_EXPIRED(401),
    //  NO_AUTH_CREDENTIALS(403),
    //  VALIDATION_ERROR(11200),
    //  INTERNAL_SERVER_ERROR(500),
    //  GENERIC_UNKNOWN_ERROR(-999);

    Log.d("APP", "onError: " + s);
    System.out.println("onError: "
        + s
        + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
  }

  @Override public void onInit(String s) {
    Log.d("APP", "onInit: " + s);
  }

  @Override public void onConfigurationReceive(String s) {
    Log.i("", "Access Token:" + s);
    Log.d("APP", "onInit: " + s);
  }

  @Override public void onReceive(String scheme) {
    Log.d("APP", "Scheme: " + scheme);
  }
}




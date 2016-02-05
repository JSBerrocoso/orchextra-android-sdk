package com.gigigo.orchextra.di.modules;

import com.gigigo.ggglib.ContextProvider;
import com.gigigo.ggglib.permissions.PermissionChecker;
import com.gigigo.orchextra.android.beacons.BeaconScanner;
import com.gigigo.orchextra.android.beacons.BeaconScannerImpl;
import com.gigigo.orchextra.android.beacons.BluetoothAvailability;
import com.gigigo.orchextra.android.beacons.BluetoothAvailabilityImpl;
import com.gigigo.orchextra.android.beacons.BluetoothStatusInfo;
import com.gigigo.orchextra.android.beacons.BluetoothStatusInfoImpl;
import com.gigigo.orchextra.android.beacons.MonitoringListenerImpl;
import com.gigigo.orchextra.android.mapper.BeaconAndroidMapper;
import com.gigigo.orchextra.android.mapper.BeaconRegionAndroidMapper;
import com.gigigo.orchextra.control.controllers.proximity.beacons.BeaconsController;
import com.gigigo.orchextra.android.beacons.monitoring.MonitoringListener;
import com.gigigo.orchextra.android.beacons.monitoring.RegionMonitoringScanner;
import com.gigigo.orchextra.android.beacons.monitoring.RegionMonitoringScannerImpl;
import com.gigigo.orchextra.android.beacons.ranging.BeaconRangingScanner;
import com.gigigo.orchextra.android.beacons.ranging.BeaconRangingScannerImpl;
import com.gigigo.orchextra.domain.device.AppRunningMode;
import com.gigigo.orchextra.domain.interactors.actions.GetActionInteractor;
import com.gigigo.orchextra.initalization.FeatureListener;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;

/**
 * Created by Sergio Martinez Rodriguez
 * Date 26/1/16.
 */
@Module
public class BeaconsModule {

  @Provides @Singleton BeaconManager provideBeaconManager(ContextProvider contextProvider){
    BeaconManager beaconManager = BeaconManager.getInstanceForApplication(contextProvider.getApplicationContext());

    //BeaconManager.setDebug(true);

    beaconManager.getBeaconParsers().add(new BeaconParser().
        setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

          //try{
               //set the duration of the scan to be 1.1 seconds
              //beaconManager.setBackgroundScanPeriod(1500l);
              beaconManager.setBackgroundBetweenScanPeriod(23000l);//3600000l
              //beaconManager.updateScanPeriods();
              //beaconManager.startMonitoringBeaconsInRegion(region);
              //beaconManager.setAndroidLScanningDisabled(true);

          //}  catch (RemoteException e) {
          //      GGGLogImpl.log("Cannot talk to service", LogLevel.ERROR);
          //}

    return beaconManager;
  }

  @Provides @Singleton BackgroundPowerSaver BackgroundPowerSaver(ContextProvider contextProvider){
    return new BackgroundPowerSaver(contextProvider.getApplicationContext());
  }

  @Provides @Singleton BeaconScanner provideBeaconScanner(RegionMonitoringScanner regionMonitoringScanner,
      BeaconRangingScanner beaconRangingScanner, AppRunningMode appRunningMode,
      BluetoothStatusInfo bluetoothStatusInfo){

    return new BeaconScannerImpl(regionMonitoringScanner, beaconRangingScanner, bluetoothStatusInfo,
        appRunningMode);
  }

  @Provides @Singleton BeaconRangingScanner provideBeaconRangingScanner(BeaconManager beaconManager,
      BeaconsController beaconsController, BeaconRegionAndroidMapper beaconRegionControlMapper,
      BeaconAndroidMapper beaconAndroidMapper){
    return new BeaconRangingScannerImpl(beaconManager, beaconsController, beaconRegionControlMapper,
        beaconAndroidMapper);
  }

  @Provides @Singleton RegionMonitoringScanner provideRegionMonitoringScanner(ContextProvider contextProvider,
    BeaconManager beaconManager, MonitoringListener monitoringListener, BeaconsController beaconsController,
      BeaconRegionAndroidMapper beaconRegionControlMapper){
    return new RegionMonitoringScannerImpl(contextProvider, beaconManager, monitoringListener,
        beaconsController, beaconRegionControlMapper);
  }

  @Provides @Singleton BeaconsController provideBeaconsController(GetActionInteractor getActionInteractor){
    return new BeaconsController(getActionInteractor);
  }

  @Provides @Singleton MonitoringListener provideMonitoringListener(AppRunningMode appRunningMode,
      BeaconRangingScanner beaconRangingScanner){
    return new MonitoringListenerImpl(appRunningMode, beaconRangingScanner);
  }

  @Provides @Singleton BluetoothAvailability provideBluetoothAvailability(BeaconManager beaconManager){
    return new BluetoothAvailabilityImpl(beaconManager);
  }

  @Provides @Singleton BeaconRegionAndroidMapper provideBeaconRegionAndroidMapper(){
    return new BeaconRegionAndroidMapper();
  }

  @Provides @Singleton BeaconAndroidMapper provideBeaconAndroidMapper(){
    return new BeaconAndroidMapper();
  }

  @Provides @Singleton BluetoothStatusInfo provideBluetoothStatusInfo(BluetoothAvailability bluetoothAvailability,
      PermissionChecker permissionChecker, ContextProvider contextProvider, AppRunningMode appRunningMode,
      FeatureListener featureListener){
    return new BluetoothStatusInfoImpl(permissionChecker, bluetoothAvailability, contextProvider,
        appRunningMode, featureListener);
  }

}

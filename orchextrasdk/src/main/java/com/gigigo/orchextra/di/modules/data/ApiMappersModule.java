package com.gigigo.orchextra.di.modules.data;

import com.gigigo.ggglib.network.mappers.ApiGenericResponseMapper;
import com.gigigo.ggglib.mappers.ModelToExternalClassMapper;
import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;
import com.gigigo.orchextra.di.qualifiers.ActionNotificationResponse;
import com.gigigo.orchextra.di.qualifiers.ActionQueryRequest;
import com.gigigo.orchextra.di.qualifiers.ActionsResponse;
import com.gigigo.orchextra.di.qualifiers.AppRequest;
import com.gigigo.orchextra.di.qualifiers.BeaconResponse;
import com.gigigo.orchextra.di.qualifiers.ClientDataResponseMapper;
import com.gigigo.orchextra.di.qualifiers.ConfigRequest;
import com.gigigo.orchextra.di.qualifiers.ConfigResponseMapper;
import com.gigigo.orchextra.di.qualifiers.CrmRequest;
import com.gigigo.orchextra.di.qualifiers.DeviceRequest;
import com.gigigo.orchextra.di.qualifiers.GeoLocationRequest;
import com.gigigo.orchextra.di.qualifiers.GeofenceResponse;
import com.gigigo.orchextra.di.qualifiers.PointReqResMapper;
import com.gigigo.orchextra.di.qualifiers.PushNotificationRequest;
import com.gigigo.orchextra.di.qualifiers.SdkDataResponseMapper;
import com.gigigo.orchextra.di.qualifiers.ThemeResponse;
import com.gigigo.orchextra.di.qualifiers.VuforiaResponse;

import gigigo.com.orchextra.data.datasources.api.model.mappers.request.AppModelToExternalClassMapper;
import gigigo.com.orchextra.data.datasources.api.model.mappers.request.DeviceModelToExternalClassMapper;
import gigigo.com.orchextra.data.datasources.api.model.mappers.request.PushNotificationModelToExternalClassMapper;
import gigigo.com.orchextra.data.datasources.api.model.mappers.response.ActionsApiExternalClassToModelMapper;
import gigigo.com.orchextra.data.datasources.api.model.mappers.response.BeaconExternalClassToModelMapper;
import gigigo.com.orchextra.data.datasources.api.model.mappers.response.ClientApiExternalClassToModelMapper;
import gigigo.com.orchextra.data.datasources.api.model.mappers.response.ConfigApiExternalClassToModelMapper;
import gigigo.com.orchextra.data.datasources.api.model.mappers.response.GeofenceExternalClassToModelMapper;
import gigigo.com.orchextra.data.datasources.api.model.mappers.response.SdkApiExternalClassToModelMapper;
import gigigo.com.orchextra.data.datasources.api.model.mappers.response.ThemeExternalClassToModelMapper;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import gigigo.com.orchextra.data.datasources.api.model.mappers.PointMapper;
import gigigo.com.orchextra.data.datasources.api.model.mappers.request.ActionQueryModelToExternalClassMapper;
import gigigo.com.orchextra.data.datasources.api.model.mappers.request.ConfigModelToExternalClassMapper;
import gigigo.com.orchextra.data.datasources.api.model.mappers.request.CrmModelToExternalClassMapper;
import gigigo.com.orchextra.data.datasources.api.model.mappers.request.GeoLocationModelToExternalClassMapper;
import gigigo.com.orchextra.data.datasources.api.model.mappers.response.ActionNotificationExternalClassToModelMapper;
import gigigo.com.orchextra.data.datasources.api.model.mappers.response.OrchextraGenericResponseMapper;
import gigigo.com.orchextra.data.datasources.api.model.mappers.response.VuforiaExternalClassToModelMapper;

/**
 * Created by Sergio Martinez Rodriguez
 * Date 11/12/15.
 */
@Module
public class ApiMappersModule {

  //region Response Mappers

  @Provides @Singleton @SdkDataResponseMapper ApiGenericResponseMapper
  provideSdkDataResponseMapper(SdkApiExternalClassToModelMapper sdkMapper){
    return createResponseMapper(sdkMapper);
  }

  @Provides @Singleton @ClientDataResponseMapper ApiGenericResponseMapper
  provideClientDataResponseMapper(ClientApiExternalClassToModelMapper clientMapper){
    return createResponseMapper(clientMapper);
  }

  @Provides @Singleton @ConfigResponseMapper ApiGenericResponseMapper
  provideConfigResponseMapper(ConfigApiExternalClassToModelMapper configApiResponseMapper) {
    return createResponseMapper(configApiResponseMapper);
  }

  @Provides @Singleton SdkApiExternalClassToModelMapper provideSdkMapper(){
   return new SdkApiExternalClassToModelMapper();
  }

  @Provides @Singleton ClientApiExternalClassToModelMapper provideClientMapper(){
    return new ClientApiExternalClassToModelMapper();
  }

  @Provides @Singleton   ApiGenericResponseMapper provideActionsResMapper(
      @ActionsResponse ActionsApiExternalClassToModelMapper actionsApiResponseMapper){
    return createResponseMapper(actionsApiResponseMapper);
  }

  @Provides @Singleton @ActionsResponse ExternalClassToModelMapper provideActionsApiResponseMapper(
      @ActionNotificationResponse
      ActionNotificationExternalClassToModelMapper actionNotifResponseMapper){
    return new ActionsApiExternalClassToModelMapper(actionNotifResponseMapper);
  }

  @Provides @Singleton @ActionNotificationResponse
  ExternalClassToModelMapper provideActionNotificationResponseMapper(){
    return new ActionNotificationExternalClassToModelMapper();
  }

  @Provides @Singleton ConfigApiExternalClassToModelMapper provideConfigApiResponseMapper(
      @BeaconResponse BeaconExternalClassToModelMapper beaconResponse,
      @GeofenceResponse GeofenceExternalClassToModelMapper geofenceResponseMapper,
      @ThemeResponse ThemeExternalClassToModelMapper themeResponseMapper,
      @VuforiaResponse VuforiaExternalClassToModelMapper vuforiaResponseMapper){

    return new ConfigApiExternalClassToModelMapper(vuforiaResponseMapper, themeResponseMapper,
        beaconResponse , geofenceResponseMapper);
  }

  @Provides @Singleton @VuforiaResponse
  VuforiaExternalClassToModelMapper provideVuforiaResponseMapper(){
    return new VuforiaExternalClassToModelMapper();
  }

  @Provides @Singleton @ThemeResponse ThemeExternalClassToModelMapper provideThemeResponseMapper(){
    return new ThemeExternalClassToModelMapper();
  }

  @Provides @Singleton @GeofenceResponse
  GeofenceExternalClassToModelMapper provideGeofenceResponseMapper(
          @PointReqResMapper PointMapper pointMapper){
    return new GeofenceExternalClassToModelMapper(pointMapper);
  }

  @Provides @Singleton @BeaconResponse BeaconExternalClassToModelMapper provideBeaconResponseMapper(){
    return new BeaconExternalClassToModelMapper();
  }

    @Provides
    @Singleton
    @ActionsResponse
    ApiGenericResponseMapper createResponseMapper(@ActionNotificationResponse
    ExternalClassToModelMapper mapper) {
    return new OrchextraGenericResponseMapper(mapper);
  }

  //endregion

  @Provides @Singleton @PointReqResMapper PointMapper providePointMapper(){
    return new PointMapper();
  }

  //region Request Mappers

  @Provides @Singleton @ActionQueryRequest
  ModelToExternalClassMapper provideActionQueryRequestMapper(){
    return new ActionQueryModelToExternalClassMapper();
  }

  @Provides @Singleton @AppRequest AppModelToExternalClassMapper provideAppRequestMapper(){
    return new AppModelToExternalClassMapper();
  }

  @Provides @Singleton @CrmRequest CrmModelToExternalClassMapper provideCrmRequestMapper(){
    return new CrmModelToExternalClassMapper();
  }

  @Provides @Singleton @PushNotificationRequest
  PushNotificationModelToExternalClassMapper providePushNotifRequestMapper(){
    return new PushNotificationModelToExternalClassMapper();
  }

  @Provides @Singleton @DeviceRequest DeviceModelToExternalClassMapper provideDeviceRequestMapper(){
    return new DeviceModelToExternalClassMapper();
  }

  @Provides @Singleton @GeoLocationRequest
  GeoLocationModelToExternalClassMapper provideGeoLocationRequestMapper(
      @PointReqResMapper PointMapper pointMapper){
    return new GeoLocationModelToExternalClassMapper(pointMapper);
  }

  @Provides @Singleton @ConfigRequest ModelToExternalClassMapper provideConfigRequestMapper(
      @PushNotificationRequest
      PushNotificationModelToExternalClassMapper pushNotificationRequestMapper,
      @GeoLocationRequest GeoLocationModelToExternalClassMapper geoLocationRequestMapper,
      @DeviceRequest DeviceModelToExternalClassMapper deviceRequestMapper,
      @CrmRequest CrmModelToExternalClassMapper crmRequestMapper,
      @AppRequest AppModelToExternalClassMapper appRequestMapper){

    return new ConfigModelToExternalClassMapper(pushNotificationRequestMapper, geoLocationRequestMapper,
        deviceRequestMapper, crmRequestMapper, appRequestMapper);
  }

  //endregion
}
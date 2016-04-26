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

package gigigo.com.orchextra.data.datasources.api.model.mappers.request;

import com.gigigo.ggglib.mappers.MapperUtils;
import com.gigigo.ggglib.mappers.ModelToExternalClassMapper;

import com.gigigo.orchextra.domain.model.config.Config;

import gigigo.com.orchextra.data.datasources.api.model.requests.OrchextraApiConfigRequest;

//TODO LIB_CRUNCH  orchextrasdk-dataprovision //TODO LIB_CRUNCH gggLib
public class ConfigModelToExternalClassMapper
        implements ModelToExternalClassMapper<Config, OrchextraApiConfigRequest> {

    private final AppModelToExternalClassMapper appRequestMapper;
    private final CrmModelToExternalClassMapper crmRequestMapper;
    private final DeviceModelToExternalClassMapper deviceRequestMapper;
    private final GeoLocationModelToExternalClassMapper geoLocationRequestMapper;
    private final PushNotificationModelToExternalClassMapper pushNotificationRequestMapper;

    public ConfigModelToExternalClassMapper(
            PushNotificationModelToExternalClassMapper pushNotificationRequestMapper,
            GeoLocationModelToExternalClassMapper geoLocationRequestMapper,
            DeviceModelToExternalClassMapper deviceRequestMapper,
            CrmModelToExternalClassMapper crmRequestMapper,
            AppModelToExternalClassMapper appRequestMapper) {
        this.pushNotificationRequestMapper = pushNotificationRequestMapper;
        this.geoLocationRequestMapper = geoLocationRequestMapper;
        this.deviceRequestMapper = deviceRequestMapper;
        this.crmRequestMapper = crmRequestMapper;
        this.appRequestMapper = appRequestMapper;
    }

    //TODO LIB_CRUNCH  orchextrasdk-dataprovision
    @Override
    public OrchextraApiConfigRequest modelToExternalClass(Config config) {

        OrchextraApiConfigRequest configRequest = new OrchextraApiConfigRequest();
//TODO LIB_CRUNCH gggLib
        configRequest.setApp(MapperUtils.checkNullDataRequest(appRequestMapper, config.getApp()));
        configRequest.setCrm(MapperUtils.checkNullDataRequest(crmRequestMapper, config.getCrm()));
//TODO LIB_CRUNCH gggLib
        configRequest.setDevice(
                MapperUtils.checkNullDataRequest(deviceRequestMapper, config.getDevice()));
//TODO LIB_CRUNCH gggLib
        configRequest.setGeoLocation(
                MapperUtils.checkNullDataRequest(geoLocationRequestMapper, config.getGeoLocation()));
//TODO LIB_CRUNCH gggLib
        configRequest.setNotificationPush(
                MapperUtils.checkNullDataRequest(pushNotificationRequestMapper,
                        config.getNotificationPush()));

        return configRequest;
    }
}

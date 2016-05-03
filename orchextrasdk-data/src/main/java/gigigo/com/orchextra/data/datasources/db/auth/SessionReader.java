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

package gigigo.com.orchextra.data.datasources.db.auth;

import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;
import com.gigigo.ggglogger.GGGLogImpl;
import com.gigigo.orchextra.domain.model.entities.authentication.ClientAuthData;
import com.gigigo.orchextra.domain.model.entities.authentication.Crm;
import com.gigigo.orchextra.domain.model.entities.authentication.SdkAuthData;
import gigigo.com.orchextra.data.datasources.db.NotFountRealmObjectException;
import gigigo.com.orchextra.data.datasources.db.model.ClientAuthRealm;
import gigigo.com.orchextra.data.datasources.db.model.CrmRealm;
import gigigo.com.orchextra.data.datasources.db.model.SdkAuthRealm;
import gigigo.com.orchextra.data.datasources.db.model.mappers.CrmRealmMapper;
import io.realm.Realm;
import io.realm.RealmResults;


public class SessionReader {
  //TODO LIB_CRUNCH gggLib
  private final ExternalClassToModelMapper<SdkAuthRealm, SdkAuthData> sdkAuthRealmMapper;
  //TODO LIB_CRUNCH gggLib
  private final ExternalClassToModelMapper<ClientAuthRealm, ClientAuthData> clientAuthRealmMapper;
  private final CrmRealmMapper crmRealmMapper;
  //TODO LIB_CRUNCH gggLib
  public SessionReader(ExternalClassToModelMapper sdkAuthRealmMapper,
      ExternalClassToModelMapper clientAuthRealmMapper, CrmRealmMapper crmRealmMapper) {

    this.sdkAuthRealmMapper = sdkAuthRealmMapper;
    this.clientAuthRealmMapper = clientAuthRealmMapper;
    this.crmRealmMapper = crmRealmMapper;
  }
  //TODO LIB_CRUNCH realm
  public ClientAuthData readClientAuthData(Realm realm) throws NullPointerException {
    RealmResults<ClientAuthRealm> clientAuthRealm = realm.where(ClientAuthRealm.class).findAll();
    if (clientAuthRealm.size() > 0) {
      GGGLogImpl.log("Client Session found");
      return clientAuthRealmMapper.externalClassToModel(clientAuthRealm.first());
    } else {
      GGGLogImpl.log("Client Session not found");
      throw new NotFountRealmObjectException();
    }
  }
  //TODO LIB_CRUNCH realm
  public SdkAuthData readSdkAuthData(Realm realm) throws NullPointerException {
    RealmResults<SdkAuthRealm> sdkAuthRealm = realm.where(SdkAuthRealm.class).findAll();
    if (sdkAuthRealm.size() > 0) {
      GGGLogImpl.log("Sdk Session found");
      return sdkAuthRealmMapper.externalClassToModel(sdkAuthRealm.first());
    } else {
      GGGLogImpl.log("Sdk Session not found");
      throw new NotFountRealmObjectException();
    }
  }
  //TODO LIB_CRUNCH realm
  public Crm readCrm(Realm realm) {
    RealmResults<CrmRealm> crmRealm = realm.where(CrmRealm.class).findAll();
    if (crmRealm.size() > 0) {
      GGGLogImpl.log("CRM_ID found");
      return crmRealmMapper.externalClassToModel(crmRealm.first());
    } else {
      GGGLogImpl.log("CRM_ID not found");
      throw new NotFountRealmObjectException();
    }
  }
}
